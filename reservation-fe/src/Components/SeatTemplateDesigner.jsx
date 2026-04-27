import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import "../Styles/adminTheme.css";
import "../Styles/seatTemplateDesigner.css";

const API_BASE = "http://localhost:8080";

const DEFAULT_ROWS = 10;
const DEFAULT_COLS = 5; // common 2+aisle+2
const DEFAULT_AISLE_COL = 2; // 0-based col index (middle)

function rowLabel(i) {
  // A, B, C... Z, AA, AB...
  let n = i;
  let s = "";
  do {
    s = String.fromCharCode(65 + (n % 26)) + s;
    n = Math.floor(n / 26) - 1;
  } while (n >= 0);
  return s;
}

function buildInitialGrid(rows, cols, aisleCol) {
  return Array.from({ length: rows }, (_, r) =>
    Array.from({ length: cols }, (_, c) => {
      if (c === aisleCol) return { seatType: "EMPTY", isBookable: false, seatCode: "" };
      // reserve top-left as DRIVER by default
      if (r === 0 && c === 0) return { seatType: "DRIVER", isBookable: false, seatCode: "DRIVER" };
      return { seatType: "SEAT", isBookable: true, seatCode: "" };
    })
  );
}

function normalizeSeatType(nextType) {
  const t = String(nextType || "").toUpperCase();
  if (t === "DRIVER") return { seatType: "DRIVER", isBookable: false };
  if (t === "EMPTY") return { seatType: "EMPTY", isBookable: false };
  return { seatType: "SEAT", isBookable: true };
}

function cycleType(currentType) {
  const t = String(currentType || "SEAT").toUpperCase();
  if (t === "SEAT") return "EMPTY";
  if (t === "EMPTY") return "DRIVER";
  return "SEAT";
}

function regenerateSeatCodes(grid) {
  // Row-wise: A1, A2 ... then B1...
  return grid.map((row, r) => {
    let seatIdx = 1;
    return row.map((cell) => {
      if (cell.seatType === "SEAT") {
        const code = `${rowLabel(r)}${seatIdx++}`;
        return { ...cell, seatCode: code, isBookable: true };
      }
      if (cell.seatType === "DRIVER") return { ...cell, seatCode: "DRIVER", isBookable: false };
      return { ...cell, seatCode: "", isBookable: false };
    });
  });
}

export default function SeatTemplateDesigner() {
  const navigate = useNavigate();
  const { busId } = useParams();

  const busIdNum = useMemo(() => Number(busId), [busId]);
  const [rows, setRows] = useState(DEFAULT_ROWS);
  const [cols, setCols] = useState(DEFAULT_COLS);
  const [aisleCol, setAisleCol] = useState(DEFAULT_AISLE_COL);
  const [deck] = useState(0);

  const [grid, setGrid] = useState(() => regenerateSeatCodes(buildInitialGrid(DEFAULT_ROWS, DEFAULT_COLS, DEFAULT_AISLE_COL)));
  const [saving, setSaving] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  // Optional load if backend exposes GET /api/bus/{busId}/seat-template.
  useEffect(() => {
    if (!busIdNum) return;
    setErrorMsg("");
    axios
      .get(`${API_BASE}/api/bus/${busIdNum}/seat-template`)
      .then((res) => {
        const seats = res?.data?.data?.seats;
        if (!Array.isArray(seats) || seats.length === 0) return;

        const maxRow = seats.reduce((m, s) => Math.max(m, Number(s.rowIndex) || 0), 0);
        const maxCol = seats.reduce((m, s) => Math.max(m, Number(s.colIndex) || 0), 0);
        const nextRows = maxRow + 1;
        const nextCols = maxCol + 1;
        setRows(nextRows);
        setCols(nextCols);

        const nextGrid = Array.from({ length: nextRows }, () =>
          Array.from({ length: nextCols }, () => ({ seatType: "EMPTY", isBookable: false, seatCode: "" }))
        );
        for (const s of seats) {
          const r = Number(s.rowIndex);
          const c = Number(s.colIndex);
          if (Number.isNaN(r) || Number.isNaN(c) || r < 0 || c < 0 || r >= nextRows || c >= nextCols) continue;
          const normalized = normalizeSeatType(s.seatType);
          nextGrid[r][c] = {
            seatType: normalized.seatType,
            isBookable: Boolean(s.isBookable) && normalized.seatType === "SEAT",
            seatCode: String(s.seatCode || ""),
          };
        }
        setGrid(regenerateSeatCodes(nextGrid));
      })
      .catch(() => {
        // If GET isn't implemented yet, just stay in create-from-scratch mode.
      });
  }, [busIdNum]);

  const rebuildGrid = (nextRows, nextCols, nextAisleCol) => {
    const r = Math.max(1, Math.min(30, Number(nextRows) || 1));
    const c = Math.max(2, Math.min(10, Number(nextCols) || 2));
    const a = Math.max(0, Math.min(c - 1, Number(nextAisleCol) || 0));
    setRows(r);
    setCols(c);
    setAisleCol(a);
    setGrid(regenerateSeatCodes(buildInitialGrid(r, c, a)));
  };

  const onCellClick = (r, c) => {
    setGrid((prev) => {
      const next = prev.map((row) => row.map((cell) => ({ ...cell })));
      const cur = next[r][c];
      const nextType = cycleType(cur.seatType);
      const normalized = normalizeSeatType(nextType);
      next[r][c] = { ...cur, ...normalized };
      return regenerateSeatCodes(next);
    });
  };

  const save = async () => {
    if (!busIdNum) {
      setErrorMsg("Missing bus id");
      return;
    }
    setErrorMsg("");
    setSaving(true);
    try {
      const seats = [];
      for (let r = 0; r < grid.length; r++) {
        for (let c = 0; c < grid[r].length; c++) {
          const cell = grid[r][c];
          if (!cell?.seatType) continue;
          const t = String(cell.seatType).toUpperCase();
          // always send all non-null cells so backend can enforce uniqueness cleanly
          seats.push({
            seatCode: t === "SEAT" ? String(cell.seatCode).toUpperCase() : t === "DRIVER" ? "DRIVER" : "X",
            rowIndex: r,
            colIndex: c,
            seatType: t,
            deck,
            isBookable: t === "SEAT",
          });
        }
      }
      await axios.put(`${API_BASE}/api/bus/${busIdNum}/seat-template`, { seats });
      alert("Seat template saved");
      navigate("/adminhomepage/vewbus");
    } catch (e) {
      setErrorMsg("Unable to save seat template. Please try again.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="adminPanel">
      <div className="adminPanelHeader">
        <div>
          <h3>Seat template</h3>
          <p>Design the seat layout for this bus. Click a cell to toggle Seat → Empty → Driver.</p>
        </div>
        <div className="stdHeaderActions">
          <button className="adminBtn adminBtnGhost" type="button" onClick={() => navigate(-1)}>
            Back
          </button>
          <button className="adminBtn adminBtnPrimary" type="button" onClick={save} disabled={saving}>
            {saving ? "Saving..." : "Save layout"}
          </button>
        </div>
      </div>

      <div className="adminPanelBody">
        <div className="stdControls">
          <div className="stdControl">
            <label>Rows</label>
            <input
              type="number"
              min={1}
              max={30}
              value={rows}
              onChange={(e) => rebuildGrid(e.target.value, cols, aisleCol)}
            />
          </div>
          <div className="stdControl">
            <label>Cols</label>
            <input
              type="number"
              min={2}
              max={10}
              value={cols}
              onChange={(e) => rebuildGrid(rows, e.target.value, aisleCol)}
            />
          </div>
          <div className="stdControl">
            <label>Aisle column</label>
            <input
              type="number"
              min={0}
              max={Math.max(0, cols - 1)}
              value={aisleCol}
              onChange={(e) => rebuildGrid(rows, cols, e.target.value)}
            />
          </div>
          <div className="stdLegend">
            <span className="stdLegendItem">
              <span className="stdSwatch stdSeat" /> Seat
            </span>
            <span className="stdLegendItem">
              <span className="stdSwatch stdEmpty" /> Empty/Aisle
            </span>
            <span className="stdLegendItem">
              <span className="stdSwatch stdDriver" /> Driver
            </span>
          </div>
        </div>

        {errorMsg && <div className="adminError">{errorMsg}</div>}

        <div
          className="stdGrid"
          style={{
            gridTemplateColumns: `repeat(${cols}, 52px)`,
            gridTemplateRows: `repeat(${rows}, 52px)`,
          }}
        >
          {grid.map((row, r) =>
            row.map((cell, c) => {
              const t = String(cell.seatType || "EMPTY").toUpperCase();
              const cls =
                t === "SEAT" ? "stdCell stdCellSeat" : t === "DRIVER" ? "stdCell stdCellDriver" : "stdCell stdCellEmpty";
              const label = t === "SEAT" ? cell.seatCode : t === "DRIVER" ? "D" : "";
              return (
                <button key={`${r}-${c}`} type="button" className={cls} onClick={() => onCellClick(r, c)}>
                  {label}
                </button>
              );
            })
          )}
        </div>
      </div>
    </div>
  );
}

