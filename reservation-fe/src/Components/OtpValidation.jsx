import { useState } from "react";
import axios from 'axios';
import '../Styles/OtpValidation.css';
import { useNavigate } from "react-router-dom";

export default function OtpValidation() {
  const [otp, setOtp] = useState("");
  let navigate=useNavigate()

  function validateOtp(e) {
    e.preventDefault();
    axios.get(`http://localhost:8080/api/admin/activate?token=${otp}`)
      .then((res) => {
        alert("OTP validated successfully");
        console.log(res);
        navigate('/adminlogin'); // navigate to the homepage
      })
      .catch((err) => {
        alert("Invalid OTP");
        console.log(err);
      });
  }

  return (
    <div className="OtpValidation">
      <form onSubmit={validateOtp}>
        <label htmlFor="otp">Enter OTP</label>
        <input
          type="text"
          required
          placeholder="Enter the OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
        />
        <button type="submit">Validate OTP</button>
      </form>
    </div>
  );
}
