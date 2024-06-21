import { useEffect, useState } from 'react';
import axios from 'axios';
import "../Styles/bookbusseat.css"

const BookBus = ({ id, bookingPopup }) => {
    const [bus, setBus] = useState([]);

    const [noOfSeat, setNoOfSeat] = useState("");
    const [bookStatus, setBookStatus] = useState(false);
    const [show, setShow] = useState(bookingPopup);

    useEffect(() => {
        if (id) {
            axios.get(`http://localhost:8080/api/bus/${id}`)
                .then((res) => {
                    setBus(res.data.data);
                    console.log(res.data.data);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [id, bookStatus]);

    const bookSeat = (e) => {
        e.preventDefault();
        const userId = localStorage.getItem("userId");
        axios.post(`http://localhost:8080/api/ticket/${userId}/${bus.id}/${noOfSeat}`)
            .then((res) => {
                alert("Ticket Booked successfully");
                setBookStatus(!bookStatus);
            })
            .catch((err) => {
                alert("Unable to book Ticket");
            });
    };

    const closeBookSeat = () => {
        setShow(false);
    };

    return (
        show && (
            <div className="bookbusseat">
                <button onClick={closeBookSeat} className="close-button">close</button>
                <form>
                    <div className="bookseatbox1">
                        <h3>{bus.name}</h3>
                        <h5>From: {bus.from_location} |</h5>
                        <h5>To: {bus.to_location} |</h5>
                        <h5>Date: {bus.bus_depurture}</h5>
                    </div>
                    <div className="bookseatbox1">
                        <h5>Available: {bus.availableSeats}</h5>
                        <div className="">
                            <input
                                type="text"
                                value={noOfSeat}
                                onChange={(e) => { setNoOfSeat(e.target.value); }}
                                placeholder="Seats"
                            />
                          
                        </div>
                    </div>
                    <button onClick={bookSeat}>Book Now</button>
                </form>
            </div>
        )
    );
}

export default BookBus;
