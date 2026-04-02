import { useState } from "react";
import "../Styles/addbus.css";
import axios from "axios";
import "../Styles/adminTheme.css";

const AddBus = () => {
    let [name, setName] = useState("");
    let [bus_number, setBus_number] = useState("");
    let [seats, setNumber_of_seats] = useState("");
    let[description,setDescription]=useState("");
    let[imageUrl,setImageUrl]=useState("");

    // Bus is inventory now (Trip contains route/date/price)
    let data = { name, bus_number, seats, description, imageUrl };
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
        <div className="adminFormShell">
            <div className="adminPanel adminFormPanel">
              <div className="adminPanelHeader">
                <div>
                  <h3>Add bus</h3>
                  <p>Create a new bus inventory record. Trips (route/date/price) are managed separately.</p>
                </div>
              </div>
              <div className="adminPanelBody">
            <form className="adminForm" action="">
                <label htmlFor="">Name</label>
                <input type="text" required value={name} onChange={(e) => setName(e.target.value)} placeholder="enter name" />
                <label htmlFor="">Bus Number</label>
                <input type="text" required value={bus_number} onChange={(e) => setBus_number(e.target.value)} placeholder="enter bus number" />
                <label htmlFor="">Number Of Seats</label>
                <input type="text" required value={seats} onChange={(e) => setNumber_of_seats(e.target.value)} placeholder="enter number of seats" />
                
                <label>Description</label>
                <select 
                    required 
                    value={description} 
                    onChange={(e) => setDescription(e.target.value)}
                >
                    <option value="" disabled>Select type</option>
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

<button onClick={addbus} className="adminBtn adminBtnPrimary adminFormSubmit">Add Bus</button>
            </form>
              </div>
            </div>
        </div>
    );
}

export default AddBus;
