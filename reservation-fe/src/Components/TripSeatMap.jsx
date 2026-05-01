import "../Styles/tripSeatMap.css";

export default function TripSeatMap({ seatsData, selected, onToggle }) {
  if (!seatsData) return null;

  const layout = Array.isArray(seatsData.layout) ? seatsData.layout : [];
  const statuses = Array.isArray(seatsData.statuses) ? seatsData.statuses : [];

  const statusByCode = new Map(
    statuses.map((s) => [String(s.seatCode || "").toUpperCase(), String(s.status || "").toUpperCase()])
  );

  const maxRow = layout.reduce((m, s) => Math.max(m, Number(s.rowIndex) || 0), 0);
  const maxCol = layout.reduce((m, s) => Math.max(m, Number(s.colIndex) || 0), 0);

  // single-deck rendering for now (deck 0)
  const deck = 0;
  const cellByPos = new Map();
  for (const s of layout) {
    const d = s.deck == null ? 0 : Number(s.deck);
    if (d !== deck) continue;
    const key = `${Number(s.rowIndex)}:${Number(s.colIndex)}`;
    cellByPos.set(key, s);
  }

  return (
    <div className="tsmRoot">
      <div className="tsmLegend">
        <span className="tsmLegendItem">
          <span className="tsmDot available" /> Available
        </span>
        <span className="tsmLegendItem">
          <span className="tsmDot booked" /> Booked
        </span>
        <span className="tsmLegendItem">
          <span className="tsmDot selected" /> Selected
        </span>
        <span className="tsmLegendItem">
          <span className="tsmDot empty" /> Empty/Driver
        </span>
      </div>

      <div
        className="tsmGrid"
        style={{
          gridTemplateColumns: `repeat(${maxCol + 1}, 48px)`,
          gridTemplateRows: `repeat(${maxRow + 1}, 48px)`,
        }}
      >
        {Array.from({ length: (maxRow + 1) * (maxCol + 1) }).map((_, idx) => {
          const r = Math.floor(idx / (maxCol + 1));
          const c = idx % (maxCol + 1);
          const seat = cellByPos.get(`${r}:${c}`);

          if (!seat) return <div key={idx} className="tsmCell tsmCellEmpty" />;

          const seatType = String(seat.seatType || "").toUpperCase();
          const isBookable =(seat.isBookable ?? seat.bookable) === true && seatType === "SEAT";
          const seatCode = String(seat.seatCode || "").toUpperCase();

          const status = isBookable ? statusByCode.get(seatCode) || "AVAILABLE" : "NA";
          const isSelected = selected.includes(seatCode);
          const disabled = !isBookable || status !== "AVAILABLE";

          const cls =
            seatType !== "SEAT"
              ? "tsmCell tsmCellEmpty"
              : disabled
              ? "tsmCell tsmCellBooked"
              : isSelected
              ? "tsmCell tsmCellSelected"
              : "tsmCell tsmCellAvailable";

          return (
            <button
              key={idx}
              type="button"
              className={cls}
              disabled={disabled}
              onClick={() => onToggle(seatCode)}
              title={disabled ? `${seatCode} (${status})` : seatCode}
            >
              {seatType === "SEAT" ? seatCode : ""}
            </button>
          );
        })}
      </div>
    </div>
  );
}

