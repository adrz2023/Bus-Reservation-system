
// import {Link} from "react-router-dom";
// import'../Styles/LandingPage.css';
// const LandingPage=()=>{
//     return(



//         <div className="LandingPage">
//         <div className="logo">
//                 <h1><span>~Be</span>fik<span> / </span>Re<span>Yatra</span></h1>
//             <div className="sub_lp">
//             <Link to="/adminlogin">

//                     <h3>Admin</h3>
//                 </Link>

//                 <Link to="/userlogin">

//                     <h3>User</h3>
//                 </Link>
//             </div>
//         </div>
//         </div>
//     )
// }
// export default LandingPage



import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import "../Styles/LandingPage.css";
// Booking now happens on a dedicated page: /book/:tripId

const API_BASE = "http://localhost:8080";

export default function LandingPage() {
  const navigate = useNavigate();

  const [userId, setUserId] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("userId"));
    } catch {
      return null;
    }
  });

  const [userName, setUserName] = useState(() => {
    try {
      return localStorage.getItem("userName") || "";
    } catch {
      return "";
    }
  });

  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [date, setDate] = useState("");

  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");

  const [hasSearched, setHasSearched] = useState(false);

  const [authModalOpen, setAuthModalOpen] = useState(false);
  const [authMode, setAuthMode] = useState("login"); // 'login' | 'register'

  const [showProfileMenu, setShowProfileMenu] = useState(false);

  // login form state
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  // registration form state
  const [regName, setRegName] = useState("");
  const [regPhone, setRegPhone] = useState("");
  const [regEmail, setRegEmail] = useState("");
  const [regGender, setRegGender] = useState("OTHER");
  const [regAge, setRegAge] = useState("");
  const [regPassword, setRegPassword] = useState("");

  const [authError, setAuthError] = useState("");
  const [authLoading, setAuthLoading] = useState(false);

  const logout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    setUserId(null);
    setUserName("");
    setShowProfileMenu(false);
    navigate("/");
  };

  const fetchAllTrips = async () => {
    setLoading(true);
    setErrorMsg("");
    try {
      // Optional params should return all trips when omitted
      const res = await axios.get(`${API_BASE}/api/trip/search`);
      setTrips(Array.isArray(res?.data?.data) ? res.data.data : []);
    } catch (e) {
      setErrorMsg("Could not load trips. Is the backend running on :8080?");
      setTrips([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllTrips();
  }, []);

  const searchTrips = async (e) => {
    e.preventDefault();
    setHasSearched(true);
    setLoading(true);
    setErrorMsg("");

    try {
      const res = await axios.get(`${API_BASE}/api/trip/search`, {
        params: {
          from_location: from.trim(),
          to_location: to.trim(),
          departureDate: date,
        },
      });
      setTrips(Array.isArray(res?.data?.data) ? res.data.data : []);
    } catch (e2) {
      setErrorMsg("No trips found for this route/date (or server error).");
      setTrips([]);
    } finally {
      setLoading(false);
    }
  };

  const clearSearch = () => {
    setFrom("");
    setTo("");
    setDate("");
    setHasSearched(false);
    fetchAllTrips();
  };

  const handleBook = (trip) => {
    if (!userId) {
      setAuthMode("login");
      setAuthError("");
      setAuthModalOpen(true);
      return;
    }
    navigate(`/book/${trip.id}`, { state: { trip } });
  };

  const openLoginModal = () => {
    setAuthMode("login");
    setAuthError("");
    setAuthModalOpen(true);
  };

  const openRegisterModal = () => {
    setAuthMode("register");
    setAuthError("");
    setAuthModalOpen(true);
  };

  const submitLogin = async (e) => {
    e.preventDefault();
    setAuthLoading(true);
    setAuthError("");
    try {
      const res = await axios.post(
        `${API_BASE}/api/user/verifyByEmail?email=${loginEmail}&password=${loginPassword}`
      );
      const data = res?.data?.data;
      if (!data) {
        setAuthError("Invalid email or password.");
      } else {
        localStorage.setItem("userId", JSON.stringify(data.id));
        if (data.name) {
          localStorage.setItem("userName", data.name);
          setUserName(data.name);
        }
        setUserId(data.id);
        setAuthModalOpen(false);
        setShowProfileMenu(false);
      }
    } catch (err) {
      setAuthError("Unable to login. Please check your details.");
    } finally {
      setAuthLoading(false);
    }
  };

  const submitRegister = async (e) => {
    e.preventDefault();
    setAuthLoading(true);
    setAuthError("");

    const payload = {
      name: regName,
      phone: Number(regPhone),
      email: regEmail,
      gender: regGender,
      age: Number(regAge),
      password: regPassword,
    };

    try {
      await axios.post(`${API_BASE}/api/user`, payload);
      setAuthMode("login");
      setAuthError("Registered successfully. Please login.");
    } catch (err) {
      setAuthError("Unable to register. Please verify your details.");
    } finally {
      setAuthLoading(false);
    }
  };

  return (
    <div className="lpRoot">
      <header className="lpNav">
        <div className="lpBrand" role="button" tabIndex={0} onClick={() => navigate("/")}>
          <div className="lpBrandTitle">
            <span className="lpAccent">Be</span>fik / Re<span className="lpAccent">Yatra</span>
          </div>
          <div className="lpBrandTag">Bus tickets • Live availability • Secure booking</div>
        </div>

        <nav className="lpNavLinks">
          {/* <Link className="lpNavLink" to="/adminlogin">Admin</Link> */}

          {!userId ? (
            <>
              <button
                className="lpBtn"
                type="button"
                onClick={openLoginModal}
              >
                Login / SignUp
              </button>
              {/* <button className="lpBtn" type="button" onClick={openRegisterModal}>
                SignUp
              </button> */}
            </>
          ) : (
            <button
              type="button"
              className="lpProfileChip"
              onClick={() => setShowProfileMenu((prev) => !prev)}
            >
              <div className="lpProfileAvatar">
                {(userName && userName.charAt(0).toUpperCase()) || "U"}
              </div>
              <span className="lpProfileLabel">
                {userName || "Profile"}
              </span>
            </button>
          )}
        </nav>
      </header>

      {/* Profile dropdown card */}
      {userId && showProfileMenu && (
        <div className="lpProfileMenu">
          <div className="lpProfileMenuHeader">
            <div className="lpProfileMenuAvatar">
              {(userName && userName.charAt(0).toUpperCase()) || "U"}
            </div>
            <div className="lpProfileMenuText">
              <div className="lpProfileMenuName">{userName || "User"}</div>
              <div className="lpProfileMenuSub">My Profile</div>
            </div>
          </div>
          <button type="button" className="lpProfileMenuItem" onClick={() => navigate('/userbookings')}>
            <span className="lpProfileMenuItemTitle">My Bookings</span>
            <span className="lpProfileMenuItemSub">View &amp; manage bookings</span>
          </button>
          <button type="button" className="lpProfileMenuItem" disabled>
            <span className="lpProfileMenuItemTitle">My Travellers</span>
            <span className="lpProfileMenuItemSub">Saved passengers (coming soon)</span>
          </button>
          <button
            type="button"
            className="lpProfileMenuItem lpProfileMenuLogout"
            onClick={logout}
          >
            <span className="lpProfileMenuItemTitle">Logout</span>
          </button>
        </div>
      )}

      <section className="lpHero">
        <div className="lpHeroInner">
          <h1>India's Fastest Bus Ticket Booking Platform </h1>
          <p className="lpHero">
            Compare routes & prices, and book instantly with live availability, secure payments, and trusted operators for a smooth, comfortable travel experience.
          </p>

              <form className="lpSearch" onSubmit={searchTrips}>
            <input
              value={from}
              onChange={(e) => setFrom(e.target.value)}
              placeholder="From (e.g., Kolkata)"
              aria-label="From"
              required
            />
            <input
              value={to}
              onChange={(e) => setTo(e.target.value)}
              placeholder="To (e.g., Mumbai)"
              aria-label="To"
              required
            />
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              aria-label="Date"
              required
            />
            <button className="lpBtn" type="submit">Search buses</button>
          </form>

          <div className="lpTrust">
            <div className="lpTrustItem"><strong>Transparent</strong> pricing</div>
            <div className="lpTrustItem"><strong>Live</strong> seat availability</div>
            <div className="lpTrustItem"><strong>Secure</strong> booking flow</div>
          </div>
        </div>
      </section>

      <main className="lpMain">
        <div className="lpSectionHead">
          <h2>{hasSearched ? "Search results" : "Available trips"}</h2>
          {hasSearched && (
            <button className="lpLinkBtn" onClick={clearSearch}>Clear search</button>
          )}
        </div>

        {loading ? (
          <div className="lpEmpty">Loading trips…</div>
        ) : errorMsg ? (
          <div className="lpEmpty lpError">{errorMsg}</div>
        ) : trips.length === 0 ? (
          <div className="lpEmpty">
            No trips to show.
            <div style={{ marginTop: 10 }}>
              <button className="lpBtn lpBtnGhost" onClick={fetchAllTrips}>Reload</button>
            </div>
          </div>
        ) : (
          <div className="lpGrid">
            {trips.map((trip) => {
              const key = trip.id ?? `${trip.busId}-${trip.from_location}-${trip.to_location}-${trip.departureDate}`;

              return (
                <article className="busCard" key={key}>
                  <div className="busCardImg">
                    {trip.imageUrl ? (
                      <img src={trip.imageUrl} alt={trip.busName || "Bus"} />
                    ) : (
                      <div className="busImgFallback">No Image</div>
                    )}
                  </div>

                  <div className="busCardBody">
                    <div className="busCardTop">
                      <h3 className="busName">{trip.busName || "Bus"}</h3>
                      <div className="busPrice">
                        ₹{trip.costPerSeat ?? "--"}
                        <span>/seat</span>
                      </div>
                    </div>

                    <div className="busRoute">
                      {trip.from_location} <span className="busArrow">→</span> {trip.to_location}
                    </div>

                    <div className="busMeta">
                      <span><strong>Date:</strong> {trip.departureDate}</span>
                      <span><strong>Seats:</strong> {trip.availableSeats ?? "--"}</span>
                      <span><strong>Bus #:</strong> {trip.busNumber ?? "--"}</span>
                    </div>

                    <div className="busActions">
                      <button className="lpBtn" onClick={() => handleBook(trip)}>Book</button>
                      {!userId && <span className="busHint">Login/Register required to confirm booking</span>}
                    </div>
                  </div>
                </article>
              );
            })}
          </div>
        )}

        <section className="lpFooterInfo">
          <div className="lpFooterCard">
            <h3>Why choose us?</h3>
            <ul>
              <li>Fast search + clean UI</li>
              <li>See buses first, login only when booking</li>
              <li>Works with your existing backend APIs</li>
            </ul>
          </div>

          <div className="lpFooterCard">
            <h3>For operators</h3>
            <p>Manage buses, pricing, seats, and schedules from the Admin panel.</p>
            <Link className="lpBtn lpBtnGhost" to="/adminlogin">Go to Admin</Link>
          </div>
        </section>
      </main>

      {/* Page footer (redBus-style) */}
      <footer className="lpFooter">
        <div className="lpFooterTop">
          <div className="lpFooterBrand">
            <div className="lpFooterLogo">Befik / ReYatra</div>
            <p>
              Befik / ReYatra is a bus ticket booking platform to help you discover routes,
              compare operators, and reserve seats securely through web and mobile.
            </p>
            <p>
              We work with trusted bus operators so you can focus on a comfortable journey,
              not the booking hassle.
            </p>
          </div>

          <div className="lpFooterCols">
            <div className="lpFooterCol">
              <h4>About</h4>
              <ul>
                <li><button type="button">About us</button></li>
                <li><button type="button">Contact</button></li>
                <li><button type="button">Careers</button></li>
                <li><button type="button">Offers</button></li>
              </ul>
            </div>

            <div className="lpFooterCol">
              <h4>Info</h4>
              <ul>
                <li><button type="button">FAQ</button></li>
                <li><button type="button">Terms &amp; Conditions</button></li>
                <li><button type="button">Privacy policy</button></li>
                <li><button type="button">User agreement</button></li>
              </ul>
            </div>

            <div className="lpFooterCol">
              <h4>Popular routes</h4>
              <ul>
                <li><button type="button">Kolkata → Mumbai</button></li>
                <li><button type="button">Bangalore → Kolkata</button></li>
                <li><button type="button">Kolkata → Delhi</button></li>
                <li><button type="button">Chennai → Hyderabad</button></li>
              </ul>
            </div>

            <div className="lpFooterCol">
              <h4>Partners</h4>
              <ul>
                <li><button type="button">Bus operators</button></li>
                <li><button type="button">Travel agencies</button></li>
              </ul>
            </div>
          </div>
        </div>

        <div className="lpFooterBottom">
          <span>© {new Date().getFullYear()} Befik / ReYatra. All rights reserved.</span>
          <div className="lpFooterSocial">
            <span>Follow us</span>
            <div className="lpFooterDots">
              <span />
              <span />
              <span />
            </div>
          </div>
        </div>
      </footer>

      {authModalOpen && (
        <div className="lpAuthBackdrop" onClick={() => setAuthModalOpen(false)}>
          <div className="lpAuthCard" onClick={(e) => e.stopPropagation()}>
            <div className="lpAuthLeft">
              <h2>Welcome to Befik / ReYatra</h2>
              <p>Login or create an account to manage your bookings, passengers and more.</p>
              <ul>
                <li>Save frequent routes and passengers</li>
                <li>View and download tickets anytime</li>
                <li>Get offers and fare updates</li>
              </ul>
            </div>

            <div className="lpAuthRight">
              <div className="lpAuthTabs">
                <button
                  type="button"
                  className={authMode === "login" ? "active" : ""}
                  onClick={() => setAuthMode("login")}
                >
                  Login
                </button>
                <button
                  type="button"
                  className={authMode === "register" ? "active" : ""}
                  onClick={() => setAuthMode("register")}
                >
                  Register
                </button>
              </div>

              {authError && <div className="lpAuthError">{authError}</div>}

              {authMode === "login" ? (
                <form className="lpAuthForm" onSubmit={submitLogin}>
                  <label>
                    Email
                    <input
                      type="email"
                      required
                      value={loginEmail}
                      onChange={(e) => setLoginEmail(e.target.value)}
                      placeholder="Enter your email"
                    />
                  </label>
                  <label>
                    Password
                    <input
                      type="password"
                      required
                      value={loginPassword}
                      onChange={(e) => setLoginPassword(e.target.value)}
                      placeholder="Enter password"
                    />
                  </label>
                  <button className="lpBtn" type="submit" disabled={authLoading}>
                    {authLoading ? "Logging in..." : "Login"}
                  </button>
                </form>
              ) : (
                <form className="lpAuthForm" onSubmit={submitRegister}>
                  <label>
                    Name
                    <input
                      type="text"
                      required
                      value={regName}
                      onChange={(e) => setRegName(e.target.value)}
                      placeholder="Enter your name"
                    />
                  </label>
                  <label>
                    Phone
                    <input
                      type="tel"
                      required
                      value={regPhone}
                      onChange={(e) => setRegPhone(e.target.value)}
                      placeholder="10-digit mobile number"
                    />
                  </label>
                  <label>
                    Email
                    <input
                      type="email"
                      required
                      value={regEmail}
                      onChange={(e) => setRegEmail(e.target.value)}
                      placeholder="Enter your email"
                    />
                  </label>
                  <div className="lpAuthRow">
                    <label>
                      Gender
                      <select
                        value={regGender}
                        onChange={(e) => setRegGender(e.target.value)}
                      >
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                        <option value="OTHER">Other</option>
                      </select>
                    </label>
                    <label>
                      Age
                      <input
                        type="number"
                        required
                        value={regAge}
                        onChange={(e) => setRegAge(e.target.value)}
                        min="1"
                        max="120"
                      />
                    </label>
                  </div>
                  <label>
                    Password
                    <input
                      type="password"
                      required
                      value={regPassword}
                      onChange={(e) => setRegPassword(e.target.value)}
                      placeholder="Create a password"
                    />
                  </label>
                  <button className="lpBtn" type="submit" disabled={authLoading}>
                    {authLoading ? "Registering..." : "Register"}
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}