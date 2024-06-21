import axios from "axios";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "../Styles/addbus.css";
export default function EditBus() {
    let [name, setName] = useState("");
    let [bus_number, setBus_number] = useState("");
    let [seats, setNumber_of_seats] = useState("");
    let [from_location, setFrom_location] = useState("");
    let [to_location, setTo_location] = useState("");
    let [bus_depurture, setBus_depurture] = useState("");

    let param = useParams();

    useEffect(() => {
        axios.get(`http://localhost:8080/api/bus/${param.id}`)
            .then((res) => {
                console.log(res.data.data);
                const data = res.data.data;
                setName(data.name);
                setBus_number(data.bus_number);
                setNumber_of_seats(data.seats);
                setFrom_location(data.from_location);
                setTo_location(data.to_location);
                setBus_depurture(data.bus_depurture);
            })
            .catch((err) => {
                console.error(err);
                alert("Failed to fetch bus details");
            });
    }, [param.id]);

    let newBus = {
        name,
        bus_number,
        seats,
        from_location,
        to_location,
        bus_depurture
    };

    function editBus(e) {
        e.preventDefault();
        axios.put(`http://localhost:8080/api/bus/${param.id}`, newBus)
            .then((res) => {
                console.log(res);
                alert("Bus details have been edited");
            })
            .catch((err) => {
                console.log(err);
                alert("Invalid data format");
            });
    }

    return (
        <div className="addbus">
            <form action="">
                <label htmlFor="">Name</label>
                <input type="text" required value={name} onChange={(e) => setName(e.target.value)} placeholder="enter name" />
                <label htmlFor="">Bus Number</label>
                <input type="text" required value={bus_number} onChange={(e) => setBus_number(e.target.value)} placeholder="enter bus number" />
                <label htmlFor="">Number Of Seats</label>
                <input type="text" required value={seats} onChange={(e) => setNumber_of_seats(e.target.value)} placeholder="enter number of seats" />
                <label htmlFor="">From Location</label>
                <input type="text" required value={from_location} onChange={(e) => setFrom_location(e.target.value)} placeholder="enter from location" />
                <label htmlFor="">To Location</label>
                <input type="text" required value={to_location} onChange={(e) => setTo_location(e.target.value)} placeholder="enter to location" />
                <label htmlFor="">Date Of Departure</label>
                <input type="date" required value={bus_depurture} onChange={(e) => setBus_depurture(e.target.value)} placeholder="enter date" />
                <button onClick={editBus} id="addbusbutton">Edit Bus</button>
            </form>
        </div>
    );
}
