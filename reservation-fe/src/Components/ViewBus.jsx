// import axios from "axios";
// import React ,{useEffect,useState}from "react";
 import '../Styles/viewbus.css';


// export default function ViewBus(){
//     let [bus,setBus]=useState([])

//     useEffect(()=>{
        
//         axios.get(`http://localhost:8080/api/bus`)

//         .then((res)=>{
//             console.log(res);
//             setBus(res.data)
//         })
//         .catch((err)=>{
//             console.log(err);
//         })
//     },[])


// return(
//     <div className="viewBus">
//         {bus.map((item)=>{
//             return(
//                 <div className="bus_details">
//                     <h4>name:{item.name}</h4>
//                     <i>seats:{item.seats}</i>
//                     <p>from_location:{item.from_loaction}</p>
//                     <p>to_location:{item.to_location}</p>
//                     <p>bus_depurture:{item.bus_depurture}</p>
//                     <span>bus_number:{item.bus_number}</span>
//                     <button>Book Seats</button>
//                 </div>
//             )
//         })}
//     </div>
// )

// }

import axios from "axios";
import React, { useEffect, useState } from "react";
import {  useNavigate } from 'react-router-dom';

export default function ViewBus() {
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

    function removedBus(id,bus_number){
        axios.get(`http://localhost:8080/api/bus/delete/${id}`)
        .then((res)=>{
            alert(`bus number ${bus_number} has been deleted`)
        })
        .catch((err)=>{
        alert("can't remove")
        })
    }

function editNavigate(id){
    navigate(`/adminhomepage/editbus/${id}`)
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
                        

                        <button onClick={()=>{editNavigate(item.id)}}>Edit</button>
                        <button onClick={()=>{removedBus(item.id,item.bus_number)}}>Delete</button>
                    </div>
                ))
            ) : (
                <p>No buses available</p>
            )
            }
        </div>
    );
}

