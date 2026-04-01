import { useEffect, useMemo, useState } from "react";
import axios from "axios";

const API_BASE = "http://localhost:8080";

export default function Trips() {
  const vendor = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem("Admin") || "null");
    } catch {
      return null;
    }
  }, []);

  const [vendorBuses, setVendorBuses] = useState([]);
  const [busId, setBusId] = useState("");

  const [loadingBuses, setLoadingBuses] = useState(false);
  const [loadingTrips, setLoadingTrips] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [tripsByBusId, setTripsByBusId] = useState({});

  const loadBuses = async () => {
    if (!vendor?.id) return;
    setLoadingBuses(true);
    try {
      const res = await axios.get(`${API_BASE}/api/bus/find/${vendor.id}`);
      setVendorBuses(Array.isArray(res?.data?.data) ? res.data.data : []);
    } catch {
      setVendorBuses([]);
    } finally {
      setLoadingBuses(false);
    }
  };

  const loadTripsForBus = async (id) => {
    setLoadingTrips(true);
    setErrorMsg("");
    try {
      const res = await axios.get(`${API_BASE}/api/trip/bus/${id}`);
      const data = Array.isArray(res?.data?.data) ? res.data.data : [];
      setTripsByBusId((prev) => ({ ...prev, [String(id)]: data }));
    } catch {
      setErrorMsg("Unable to load trips for this bus.");
    } finally {
      setLoadingTrips(false);
    }
  };

  const loadAllTrips = async () => {
    setErrorMsg("");
    if (vendorBuses.length === 0) {
      setTripsByBusId({});
      return;
    }

    setLoadingTrips(true);
    try {
      const results = await Promise.all(
        vendorBuses.map(async (b) => {
          const res = await axios.get(`${API_BASE}/api/trip/bus/${b.id}`);
          return [String(b.id), Array.isArray(res?.data?.data) ? res.data.data : []];
        })
      );
      const next = {};
      for (const [id, data] of results) next[id] = data;
      setTripsByBusId(next);
    } catch {
      setErrorMsg("Unable to load trips.");
      setTripsByBusId({});
    } finally {
      setLoadingTrips(false);
    }
  };

  useEffect(() => {
    loadBuses();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [vendor?.id]);

  useEffect(() => {
    // After buses load, fetch all trips once (default: show all trips)
    if (vendorBuses.length > 0) loadAllTrips();
    else setTripsByBusId({});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [vendorBuses]);

  const selectedTrips = busId ? (tripsByBusId[String(busId)] || []) : [];
  const allTrips = Object.values(tripsByBusId).flat();
  const tripsToShow = busId ? selectedTrips : allTrips;

  return (
    <div className="adminPanel">
      <div className="adminPanelHeader">
        <div>
          <h3>Trips</h3>
          <p>View all trips for your buses. Filter by bus if needed.</p>
        </div>
        <div style={{ display: "flex", gap: 10 }}>
          <button
            type="button"
            className="adminBtn adminBtnGhost"
            onClick={() => (busId ? loadTripsForBus(busId) : loadAllTrips())}
            disabled={loadingTrips || vendorBuses.length === 0}
          >
            Reload trips
          </button>
          <button
            type="button"
            className="adminBtn adminBtnGhost"
            onClick={loadBuses}
            disabled={loadingBuses}
          >
            Reload buses
          </button>
        </div>
      </div>

      <div className="adminPanelBody">
        <div className="adminFormPanel" style={{ marginBottom: 16 }}>
          <form className="adminForm">
            <label>Select bus</label>
            <select
              value={busId}
              onChange={(e) => setBusId(e.target.value)}
              disabled={loadingBuses}
            >
              <option value="">All buses</option>
              {vendorBuses.map((b) => (
                <option key={b.id} value={String(b.id)}>
                  {b.name} (#{b.bus_number})
                </option>
              ))}
            </select>
          </form>
        </div>

        {errorMsg && <div className="adminError">{errorMsg}</div>}

        {loadingTrips ? (
          <div className="adminEmpty">Loading trips...</div>
        ) : tripsToShow.length === 0 ? (
          <div className="adminEmpty">No trips found{busId ? " for this bus" : ""}.</div>
        ) : (
          <div className="adminTripGrid">
            {tripsToShow.map((t) => (
              <div key={`${t.id}`} className="adminTripRow">
                <div className="adminTripTop">
                  <div className="adminTripTitle">
                    {t.from_location} → {t.to_location}
                  </div>
                  <div className="adminBusPrice">
                    ₹ {t.costPerSeat} <span>/seat</span>
                  </div>
                </div>
                <div className="adminTripSub">
                  <span>
                    <strong>Date:</strong> {t.departureDate}
                  </span>
                  <span>
                    <strong>Available:</strong> {t.availableSeats}
                  </span>
                  <span>
                    <strong>Status:</strong> {t.status}
                  </span>
                  <span>
                    <strong>Bus:</strong> {t.busName} (#{t.busNumber})
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

