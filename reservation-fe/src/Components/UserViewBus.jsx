import axios from "axios";
import React, { useEffect, useState } from "react";
import {  useNavigate } from 'react-router-dom';
import BookBus from "./BookBus";


export default function UserViewBus() {

 let[bookingPopup,setBookingPopup]=useState(false)
let[busId,setBusId]=useState("")
    const [bus, setBus] = useState([]);
let navigate=useNavigate()
    useEffect(() => {

        let admin = JSON.parse(localStorage.getItem("Admin"))
        axios.get(`http://localhost:8080/api/bus/find/${admin.id}`)
            .then((res) => {
                console.log('API Response:', res);
                const busData = res.data.data; 
                if (Array.isArray(busData)) {
                    setBus(busData);
                } else {
                    console.error("API response data is not an array:", busData);
                }
            })
            .catch((err) => {
                console.error("API Error:", err);
            });
    }, []);

    
    function bookbus(id) {
        setBookingPopup(!bookingPopup)
        setBusId(id)
    }



    return (
        <div className="viewBus">
            {Array.isArray(bus) && bus.length > 0 ? (
                bus.map((item) => (
                    <div className="bus_details" key={item.bus_number}>

                        <img src={item.imageUrl} alt={item.name} />

                        <h4>{item.name}</h4>
                        
                        <p>From: {item.from_location} <br></br> To: {item.to_location}</p>
                        
                        <i>Seats: {item.seats}</i>
                        <p>Price ₹ {item.costPerSeat} </p>
                        <p>Date: {item.bus_depurture}</p>
                        <span>Bus Number: {item.bus_number}</span>
                        <span>Bus Description:{item.description}</span>
                        

                        <button onClick={()=>{bookbus(item.id)}} >Book Bus</button>
                        
                    </div>
                ))
            ) : (
                <p>No buses available</p>
            )
            }

       {bookingPopup && <BookBus id={busId} bookingPopup={bookingPopup} /> }
        </div>
    );
}
