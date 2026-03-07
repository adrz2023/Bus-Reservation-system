import { useEffect, useState } from "react";
import axios from "axios";
import Usernavbar from "./Usernavbar";
import "../Styles/userbookings.css";

const UserBookings = () => {
    const [tickets, setTickets] = useState([]);
    const [activeTab, setActiveTab] = useState("Upcoming");
    const [loading, setLoading] = useState(true);

    const tabs = ["Upcoming", "Past", "Cancelled", "Unsuccessful"];

    useEffect(() => {
        const fetchTickets = async () => {
            const userId = localStorage.getItem("userId");
            if (!userId) {
                setLoading(false);
                return;
            }

            try {
                const response = await axios.get(`http://localhost:8080/api/ticket/${userId}`);
                if (response.data && response.data.data) {
                    setTickets(response.data.data);
                }
            } catch (err) {
                console.error("Failed to fetch tickets", err);
            } finally {
                setLoading(false);
            }
        };

        fetchTickets();
    }, []);

    const filterTickets = () => {
        const today = new Date();
        // Resetting time to 00:00:00 to compare dates accurately
        today.setHours(0, 0, 0, 0);

        return tickets.filter(ticket => {
            if (activeTab === "Cancelled") return ticket.status === "Cancelled";
            if (activeTab === "Unsuccessful") return ticket.status === "Unsuccessful";

            // Only consider non-cancelled and non-unsuccessful tickets for Upcoming/Past
            if (ticket.status === "Cancelled" || ticket.status === "Unsuccessful") return false;

            const bookingDate = new Date(ticket.bus_depurture || ticket.dateOfBooking);

            if (activeTab === "Upcoming") {
                return bookingDate >= today;
            } else if (activeTab === "Past") {
                return bookingDate < today;
            }
            return true;
        });
    };

    const filteredTickets = filterTickets();

    return (
        <div className="user-bookings-page">
            <Usernavbar />
            <div className="user-bookings-container">
                <div className="bookings-header">
                    <h2>Bus Bookings</h2>
                    <div className="booking-tabs">
                        {tabs.map(tab => (
                            <button
                                key={tab}
                                className={`tab-btn ${activeTab === tab ? 'active' : ''}`}
                                onClick={() => setActiveTab(tab)}
                            >
                                {tab}
                            </button>
                        ))}
                    </div>
                </div>

                {loading ? (
                    <p>Loading your bookings...</p>
                ) : filteredTickets.length === 0 ? (
                    <div className="no-bookings-alert">
                        You have no {activeTab.toLowerCase()} bookings yet
                    </div>
                ) : (
                    <div className="tickets-container">
                        {filteredTickets.map(ticket => (
                            <div key={ticket.id} className="ticket-card">
                                <div className="ticket-info">
                                    <div className="ticket-route">
                                        {ticket.from_location} &rarr; {ticket.to_location}
                                    </div>
                                    <div className="ticket-details">
                                        <p><strong>Bus:</strong> {ticket.busName} (No: {ticket.bus_number})</p>
                                        <p><strong>Departure:</strong> {new Date(ticket.bus_depurture).toLocaleDateString()}</p>
                                        <p><strong>Seats:</strong> {ticket.numberOfSeatsBooked}</p>
                                        <p><strong>Cost:</strong> ₹{ticket.cost}</p>
                                    </div>
                                </div>
                                <div className={`ticket-status status-${activeTab.toLowerCase()}`}>
                                    {ticket.status || activeTab}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default UserBookings;
