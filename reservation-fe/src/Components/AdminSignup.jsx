
import axios from 'axios';
import '../Styles/AdminSignUp.css';
import { useState } from "react";
import { useNavigate } from 'react-router-dom';

export default function AdminSignUp() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");
  const [gst_number, setGstNumber] = useState("");
  const [travels_name, setTravelsName] = useState("");
let navigate=useNavigate();
let data={
  name,email,password,phone,gst_number,travels_name
}

function createAdmin(e){
  e.preventDefault()
  axios.post('http://localhost:8080/api/admin',data)
  .then((res)=>{
      
    alert("enter otp")
    navigate('/otpvalidation')
    // console.log(res);
  })
  .catch((err)=>{
    alert("valid")
    // console.log(err);
  })
}
  return (
    <div className="AdminSignUp">
      <form onSubmit={createAdmin}action="">
        <label htmlFor="name">Name</label>
        <input
          type="text"
          required
          placeholder="Enter the Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          required
          placeholder="Enter the Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          required
          placeholder="Enter the Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <label htmlFor="phone">Phone</label>
        <input
          type="text"
          required
          placeholder="Enter the Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        />

        <label htmlFor="gst_number">GST Number</label>
        <input
          type="text"
          required
          placeholder="Enter the GST Number"
          value={gst_number}
          onChange={(e) => setGstNumber(e.target.value)}
        />

        <label htmlFor="travels_name">Travels Name</label>
        <input
          type="text"
          required
          placeholder="Enter the Travels Name"
          value={travels_name}
          onChange={(e) => setTravelsName(e.target.value)}
        />
         <button type="submit">Register</button>
      </form>
    </div>
  );
}
