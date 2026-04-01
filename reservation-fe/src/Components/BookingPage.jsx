import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate, useParams, Link } from "react-router-dom";
import "../Styles/bookingPage.css";
import "../Styles/LandingPage.css";

const API_BASE = "http://localhost:8080";

export default function BookingPage() {
  const navigate = useNavigate();
  const { tripId } = useParams();
  const location = useLocation();

  const userId = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem("userId") || "null");
    } catch {
      return null;
    }
  }, []);

  const [userName, setUserName] = useState(() => {
    try {
      return localStorage.getItem("userName") || "";
    } catch {
      return "";
    }
  });
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  const [trip, setTrip] = useState(() => location?.state?.trip || null);
  const [loadingTrip, setLoadingTrip] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const [includeSelf, setIncludeSelf] = useState(true);
  const [extraPassengers, setExtraPassengers] = useState([]);
  const [submitting, setSubmitting] = useState(false);

  // Fallback: if page refreshes and state is lost, try to find trip by id
  useEffect(() => {
    const idNum = Number(tripId);
    if (trip?.id === idNum) return;
    if (!tripId) return;

    setLoadingTrip(true);
    setErrorMsg("");
    axios
      .get(`${API_BASE}/api/trip/search`)
      .then((res) => {
        const data = Array.isArray(res?.data?.data) ? res.data.data : [];
        const found = data.find((t) => Number(t.id) === idNum);
        if (!found) setErrorMsg("Trip not found. Please go back and select again.");
        setTrip(found || null);
      })
      .catch(() => setErrorMsg("Unable to load trip details."))
      .finally(() => setLoadingTrip(false));
  }, [tripId, trip]);

  const seatsBooked = (includeSelf ? 1 : 0) + extraPassengers.length;
  const totalCost = seatsBooked * (trip?.costPerSeat || 0);

  const addPassenger = () => {
    setExtraPassengers((prev) => [...prev, { name: "", age: "", gender: "MALE" }]);
  };

  const removePassenger = (idx) => {
    setExtraPassengers((prev) => prev.filter((_, i) => i !== idx));
  };

  const updatePassenger = (idx, patch) => {
    setExtraPassengers((prev) => prev.map((p, i) => (i === idx ? { ...p, ...patch } : p)));
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
      await axios.post(`${API_BASE}/api/ticket/trip/${userId}`, {
        tripId: trip.id,
        includeSelf,
        extraPassengers: extraPassengers.map((p) => ({
          name: p.name.trim(),
          age: Number(p.age),
          gender: p.gender,
        })),
      });
      alert("Ticket booked successfully");
      navigate("/userbookings");
    } catch {
      alert("Unable to book ticket");
    } finally {
      setSubmitting(false);
    }
  };

  const logout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    setUserName("");
    setShowProfileMenu(false);
    navigate("/");
  };

  return (
    <div className="bkRoot">
      <header className="lpNav">
        <div className="lpBrand" role="button" tabIndex={0} onClick={() => navigate("/")}>
          <div className="lpBrandTitle">
            <span className="lpAccent">Be</span>fik / Re<span className="lpAccent">Yatra</span>
          </div>
          <div className="lpBrandTag">Bus tickets • Live availability • Secure booking</div>
        </div>

        <nav className="lpNavLinks">
          {!userId ? (
            <span className="bkLoginHint">Login required to confirm booking</span>
          ) : (
            <button
              type="button"
              className="lpProfileChip"
              onClick={() => setShowProfileMenu((prev) => !prev)}
            >
              <div className="lpProfileAvatar">
                {(userName && userName.charAt(0).toUpperCase()) || "U"}
              </div>
              <span className="lpProfileLabel">{userName || "Profile"}</span>
            </button>
          )}
        </nav>
      </header>

      {userId && showProfileMenu && (
        <div className="lpProfileMenu" onMouseLeave={() => setShowProfileMenu(false)}>
          <div className="lpProfileMenuHeader">
            <div className="lpProfileMenuAvatar">
              {(userName && userName.charAt(0).toUpperCase()) || "U"}
            </div>
            <div className="lpProfileMenuText">
              <div className="lpProfileMenuName">{userName || "User"}</div>
              <div className="lpProfileMenuSub">My Profile</div>
            </div>
          </div>

          <button
            type="button"
            className="lpProfileMenuItem"
            onClick={() => {
              setShowProfileMenu(false);
              navigate("/userbookings");
            }}
          >
            <span className="lpProfileMenuItemTitle">My Bookings</span>
            <span className="lpProfileMenuItemSub">View &amp; manage bookings</span>
          </button>

          <button type="button" className="lpProfileMenuItem" disabled>
            <span className="lpProfileMenuItemTitle">My Travellers</span>
            <span className="lpProfileMenuItemSub">Saved passengers (coming soon)</span>
          </button>

          <button type="button" className="lpProfileMenuItem lpProfileMenuLogout" onClick={logout}>
            <span className="lpProfileMenuItemTitle">Logout</span>
            <span className="lpProfileMenuItemSub">Sign out from this device</span>
          </button>
        </div>
      )}

      <main className="bkMain">
        <div style={{ width: "min(1100px, 100%)", margin: "0 auto 12px" }}>
          <Link to="/" className="bkBack">
            ← Back to trips
          </Link>
          <div className="bkTitle">
            <h1>Book tickets</h1>
            <p>Enter passenger details and confirm your booking.</p>
          </div>
        </div>

        <section className="bkGrid">
          <aside className="bkCard">
            <h2>Trip summary</h2>
            {loadingTrip ? (
              <div className="bkMuted">Loading trip…</div>
            ) : errorMsg ? (
              <div className="bkError">{errorMsg}</div>
            ) : !trip ? (
              <div className="bkMuted">No trip selected.</div>
            ) : (
              <div className="bkSummary">
                <div className="bkSummaryTop">
                  <div className="bkBusName">{trip.busName}</div>
                  <div className="bkPrice">
                    ₹ {trip.costPerSeat} <span>/seat</span>
                  </div>
                </div>
                <div className="bkRoute">
                  {trip.from_location} <span>→</span> {trip.to_location}
                </div>
                <div className="bkMeta">
                  <span>
                    <strong>Date:</strong> {String(trip.departureDate)}
                  </span>
                  <span>
                    <strong>Available:</strong> {trip.availableSeats}
                  </span>
                  <span>
                    <strong>Bus #:</strong> {trip.busNumber}
                  </span>
                </div>
                {trip.imageUrl && (
                  <div className="bkImg">
                    <img src={trip.imageUrl} alt={trip.busName || "Bus"} />
                  </div>
                )}
              </div>
            )}
          </aside>

          <section className="bkCard">
            <h2>Passenger details</h2>

            <form className="bkForm" onSubmit={submit}>
              <label className="bkCheck">
                <input
                  type="checkbox"
                  checked={includeSelf}
                  onChange={(e) => setIncludeSelf(e.target.checked)}
                />
                Include me as passenger
              </label>

              <div className="bkRow">
                <button type="button" className="bkBtn bkBtnGhost" onClick={addPassenger}>
                  + Add passenger
                </button>
              </div>

              {extraPassengers.map((p, idx) => (
                <div className="bkPassenger" key={idx}>
                  <div className="bkPassengerHead">
                    <strong>Passenger {idx + 1}</strong>
                    <button type="button" className="bkLink" onClick={() => removePassenger(idx)}>
                      Remove
                    </button>
                  </div>
                  <div className="bkPassengerGrid">
                    <div>
                      <label>Name</label>
                      <input
                        value={p.name}
                        onChange={(e) => updatePassenger(idx, { name: e.target.value })}
                        placeholder="Passenger name"
                        required
                      />
                    </div>
                    <div>
                      <label>Age</label>
                      <input
                        type="number"
                        value={p.age}
                        onChange={(e) => updatePassenger(idx, { age: e.target.value })}
                        placeholder="Age"
                        required
                      />
                    </div>
                    <div>
                      <label>Gender</label>
                      <select value={p.gender} onChange={(e) => updatePassenger(idx, { gender: e.target.value })}>
                        <option value="MALE">MALE</option>
                        <option value="FEMALE">FEMALE</option>
                        <option value="OTHER">OTHER</option>
                      </select>
                    </div>
                  </div>
                </div>
              ))}

              <div className="bkTotal">
                <div>
                  <div className="bkMuted">Seats</div>
                  <div className="bkBig">{seatsBooked}</div>
                </div>
                <div>
                  <div className="bkMuted">Total</div>
                  <div className="bkBig">₹ {isNaN(totalCost) ? 0 : totalCost}</div>
                </div>
              </div>

              <div className="bkActions">
                <button type="submit" className="bkBtn bkBtnPrimary" disabled={submitting || !trip}>
                  {submitting ? "Booking..." : "Confirm booking"}
                </button>
              </div>
            </form>
          </section>
        </section>
      </main>
    </div>
  );
}

