import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const API_BASE = "http://localhost:8080";

export default function ManageTrips() {
  const { busId: busIdParam } = useParams();
  const navigate = useNavigate();

  const vendor = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem("Admin") || "null");
    } catch {
      return null;
    }
  }, []);

  const [busId, setBusId] = useState(busIdParam || "");
  const [vendorBuses, setVendorBuses] = useState([]);

  const [errorMsg, setErrorMsg] = useState("");

  const [from_location, setFrom] = useState("");
  const [to_location, setTo] = useState("");
  const [departureDate, setDepartureDate] = useState("");
  const [costPerSeat, setCostPerSeat] = useState("");

  // keep state in sync when user opens /bus/:busId/trips
  useEffect(() => {
    setBusId(busIdParam || "");
  }, [busIdParam]);

  // load vendor buses so "Add Trips" can select a bus
  useEffect(() => {
    if (!vendor?.id) return;

    axios
      .get(`${API_BASE}/api/bus/find/${vendor.id}`)
      .then((res) => setVendorBuses(Array.isArray(res?.data?.data) ? res.data.data : []))
      .catch(() => setVendorBuses([]));
  }, [vendor?.id]);

  // Note: This page is intentionally form-only (no trip list).

  const createTrip = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    if (!busId) {
      setErrorMsg("Please select a bus first.");
      return;
    }

    try {
      await axios.post(`${API_BASE}/api/trip/${busId}`, {
        from_location,
        to_location,
        departureDate,
        costPerSeat: Number(costPerSeat),
      });
      setFrom("");
      setTo("");
      setDepartureDate("");
      setCostPerSeat("");
      alert("Trip created");
    } catch {
      setErrorMsg("Unable to create trip. Please verify fields.");
    }
  };

  return (
    <div className="adminPanel">
      <div className="adminPanelHeader">
        <div>
          <h3>Add trips</h3>
          <p>Select a bus and create a trip (route/date/price) for it.</p>
        </div>
      </div>

      <div className="adminPanelBody">
        <div className="adminFormPanel" style={{ marginBottom: 16 }}>
          <form className="adminForm" onSubmit={createTrip}>
            <label>Select bus</label>
            <select
              value={busId}
              onChange={(e) => {
                const next = e.target.value;
                setBusId(next);
                // optional: keep URL in sync if selected
                if (busIdParam) navigate(`/adminhomepage/bus/${next}/trips`);
              }}
            >
              <option value="" disabled>
                {vendorBuses.length === 0 ? "No buses found" : "-- Select bus --"}
              </option>
              {vendorBuses.map((b) => (
                <option key={b.id} value={b.id}>
                  {b.name} (#{b.bus_number})
                </option>
              ))}
            </select>

            <label>From</label>
            <input value={from_location} onChange={(e) => setFrom(e.target.value)} required />

            <label>To</label>
            <input value={to_location} onChange={(e) => setTo(e.target.value)} required />

            <label>Departure date</label>
            <input type="date" value={departureDate} onChange={(e) => setDepartureDate(e.target.value)} required />

            <label>Cost per seat</label>
            <input type="number" value={costPerSeat} onChange={(e) => setCostPerSeat(e.target.value)} required />

            <button className="adminBtn adminBtnPrimary adminFormSubmit" type="submit">
              Create trip
            </button>
          </form>
        </div>

        {errorMsg && <div className="adminError">{errorMsg}</div>}
        {!busId && <div className="adminEmpty">Select a bus to create a trip.</div>}
      </div>
    </div>
  );
}