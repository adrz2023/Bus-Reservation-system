import '../Styles/error.css'
import React from 'react';
import { Link } from 'react-router-dom';

function ErrorPage() {
    return (
        <div  className='errorpage'>
            <h1>404 - Page Not Found</h1>
            <p>Sorry, the page you are looking for does not exist.</p>
            <Link to="/adminlogin">Go to Home</Link>
        </div>
    );
}

export default ErrorPage;
