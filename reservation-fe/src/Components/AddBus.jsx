import { useState } from "react";
import "../Styles/addbus.css";
import axios from "axios";

const AddBus = () => {
    let [name, setName] = useState("");
    let [bus_number, setBus_number] = useState("");
    let [seats, setNumber_of_seats] = useState("");
    let [from_location, setFrom_location] = useState("");
    let [to_location, setTo_location] = useState("");
    let [bus_depurture, setBus_depurture] = useState("");
    let[costPerSeat,setCostPerSeat]=useState("");
    let[description,setDescription]=useState("");
    let[imageUrl,setImageUrl]=useState("");

    let data = { name, bus_number, seats, from_location, to_location, bus_depurture,costPerSeat,description,imageUrl };
    let admin = JSON.parse(localStorage.getItem("Admin"))
    // console.log(admin);
    // console.log(typeof(admin));
    function addbus(e) {
        e.preventDefault();
        axios.post(`http://localhost:8080/api/bus/${admin.id}`, data)
            .then((res) => {
                alert("Bus Added");
            })
            .catch((err) => {
               
                console.log("Error message:", err.message);
                alert("Failed to add Bus. Check console for details.");
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
                <label htmlFor="">Cost</label>
                <input type="text" required value={costPerSeat} onChange={(e)=> setCostPerSeat(e.target.value)} placeholder="enter cost per seat" />
                
                <label>Description</label>
                <select 
                    required 
                    value={description} 
                    onChange={(e) => setDescription(e.target.value)}
                >
                    <option value="AC">AC</option>
                    <option value="NON-AC">NON-AC</option>
                </select>
                <label>Image URL</label>
                <input 
                    type="url" 
                    required 
                    value={imageUrl} 
                    onChange={(e) => setImageUrl(e.target.value)} 
                    placeholder="Add picture URL" 
                />

<button onClick={addbus} id="addbusbutton">Add Bus</button>
            </form>
        </div>
    );
}

export default AddBus;
