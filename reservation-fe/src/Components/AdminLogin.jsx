import axios from 'axios';
import React, { useState } from "react";
import '../Styles/adminlogin.css';
import { Link, useNavigate } from "react-router-dom";

export default function AdminLogin() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    
   let navigate=useNavigate()

    function verify(e) {
        e.preventDefault();
        axios.post(`http://localhost:8080/api/admin/verifyByEmail?email=${email}&password=${password}`)
            .then((res) => {
                navigate('/adminhomepage')
                alert("login successfully");
                localStorage.setItem("Admin",JSON.stringify(res.data.data))
            })
            .catch((err) => {
                alert("Invalid");
            });
    }

    return (
        <div className="AdminLogin">
            <form onSubmit={verify}>
            <h1>Admin Login</h1>
                <label htmlFor="email">Email:</label>
                <input
                    type="email"
                    id="email"
                    required
                    placeholder="Enter your email id"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                
                <label htmlFor="password">Password:</label>
                <input
                    type="password"
                    id="password"
                    required
                    placeholder="Enter password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">Login</button>
                <p className="register-link">
                  
                    <Link to="">Forgot password?</Link>
                
                </p>

                <p className="register-link">
                    Don't have an account? 
                    <Link to="/AdminSignup">Please register</Link>
                
                </p>
            </form>
        </div>
    );
}
