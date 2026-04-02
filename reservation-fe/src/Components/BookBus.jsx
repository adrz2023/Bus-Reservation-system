import { useMemo, useState } from "react";
import axios from "axios";
import "../Styles/bookbusseat.css";

// Trip-based booking modal with group passengers
// Props:
// - trip: TripResponse (preferred)
// - bookingPopup: boolean
const BookBus = ({ trip, bookingPopup }) => {
  const [show, setShow] = useState(bookingPopup);

  const [includeSelf, setIncludeSelf] = useState(true);
  const [extraPassengers, setExtraPassengers] = useState([]);
  const [submitting, setSubmitting] = useState(false);

  const userId = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem("userId") || "null");
    } catch {
      return null;
    }
  }, []);

  const close = () => setShow(false);

  const seatsBooked = (includeSelf ? 1 : 0) + extraPassengers.length;
  const totalCost = seatsBooked * (trip?.costPerSeat || 0);

  const addPassenger = () => {
    setExtraPassengers((prev) => [
      ...prev,
      { name: "", age: "", gender: "MALE" },
    ]);
  };

  const removePassenger = (idx) => {
    setExtraPassengers((prev) => prev.filter((_, i) => i !== idx));
  };

  const updatePassenger = (idx, patch) => {
    setExtraPassengers((prev) =>
      prev.map((p, i) => (i === idx ? { ...p, ...patch } : p))
    );
  };

  const validate = () => {
    if (!trip?.id) return "Trip is missing";
    if (!userId) return "Please login to book";
    if (seatsBooked <= 0) return "At least one passenger is required";

    for (let i = 0; i < extraPassengers.length; i++) {
      const p = extraPassengers[i];
      if (!p.name?.trim()) return `Passenger ${i + 1}: name required`;
      const age = Number(p.age);
      if (!age || age <= 0) return `Passenger ${i + 1}: valid age required`;
      if (!p.gender?.trim()) return `Passenger ${i + 1}: gender required`;
    }
    return null;
  };

  const submit = async (e) => {
    e.preventDefault();
    const err = validate();
    if (err) {
      alert(err);
      return;
    }

    setSubmitting(true);
    try {
      await axios.post(`http://localhost:8080/api/ticket/trip/${userId}`, {
        tripId: trip.id,
        includeSelf,
        extraPassengers: extraPassengers.map((p) => ({
          name: p.name.trim(),
          age: Number(p.age),
          gender: p.gender,
        })),
      });
      alert("Ticket booked successfully");
      close();
    } catch (err2) {
      alert("Unable to book ticket");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    show && (
      <div className="bookbusseat">
        <button onClick={close} className="close-button">
          close
        </button>

        <form onSubmit={submit}>
          <div className="bookseatbox1">
            <h3>{trip?.busName || "Trip"}</h3>
            <h5>
              From: {trip?.from_location} | To: {trip?.to_location} | Date:{" "}
              {trip?.departureDate}
            </h5>
            <h5>Available: {trip?.availableSeats}</h5>
            <h5>Price: ₹ {trip?.costPerSeat} / seat</h5>
          </div>

          <div className="bookseatbox1">
            <label style={{ display: "flex", gap: 8, alignItems: "center" }}>
              <input
                type="checkbox"
                checked={includeSelf}
                onChange={(e) => setIncludeSelf(e.target.checked)}
              />
              Include me as passenger
            </label>

            <div style={{ marginTop: 10 }}>
              <button type="button" onClick={addPassenger}>
                + Add passenger
              </button>
            </div>

            {extraPassengers.map((p, idx) => (
              <div
                key={idx}
                style={{
                  marginTop: 10,
                  padding: 10,
                  border: "1px solid rgba(0,0,0,0.15)",
                  borderRadius: 10,
                  background: "rgba(255,255,255,0.08)",
                }}
              >
                <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
                  <input
                    type="text"
                    placeholder="Name"
                    value={p.name}
                    onChange={(e) => updatePassenger(idx, { name: e.target.value })}
                  />
                  <input
                    type="number"
                    placeholder="Age"
                    value={p.age}
                    onChange={(e) => updatePassenger(idx, { age: e.target.value })}
                    style={{ width: 90 }}
                  />
                  <select
                    value={p.gender}
                    onChange={(e) => updatePassenger(idx, { gender: e.target.value })}
                  >
                    <option value="MALE">MALE</option>
                    <option value="FEMALE">FEMALE</option>
                    <option value="OTHER">OTHER</option>
                  </select>
                  <button type="button" onClick={() => removePassenger(idx)}>
                    Remove
                  </button>
                </div>
              </div>
            ))}

            <div style={{ marginTop: 12 }}>
              <h5>Seats: {seatsBooked}</h5>
              <h5>Total: ₹ {isNaN(totalCost) ? 0 : totalCost}</h5>
            </div>
          </div>

          <button type="submit" disabled={submitting}>
            {submitting ? "Booking..." : "Book Now"}
          </button>
        </form>
      </div>
    )
  );
};

export default BookBus;
