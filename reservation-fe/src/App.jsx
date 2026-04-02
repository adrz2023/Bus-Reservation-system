
import './App.css';

import { BrowserRouter, Route, Routes } from 'react-router-dom';

import LandingPage from './Components/LandingPage';
import AdminSignUp from './Components/AdminSignup';
import AdminLogin from './Components/AdminLogin';
import AdminHomePage from './Components/AdminHomePage';
import OtpValidation from './Components/OtpValidation';
import ErrorPage from './Components/Errorpage';
import Protected from './Components/Protected';
import Userlogin from './Components/Userlogin';
import UserSignUp from './Components/UserSignUp';
import Userhomepage from './Components/Userhomepage';
import UserBookings from './Components/UserBookings';
import SuperAdminHomePage from './Components/SuperAdminHomePage';
import BookingPage from './Components/BookingPage';
function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/*' element={<ErrorPage />} />
          <Route path='/' element={<LandingPage />} />
          <Route path='/book/:tripId' element={<BookingPage />} />
          <Route path='/adminlogin' element={<AdminLogin />} />
          <Route path='/Adminsignup' element={<AdminSignUp />} />
          <Route path='/usersignup' element={<UserSignUp />} />
          <Route path='/adminhomepage/*' element={<Protected Child={AdminHomePage} />} />
          <Route path='/otpvalidation' element={<OtpValidation />} />
          <Route path='/userlogin' element={<Userlogin />} />
          <Route path='/userhomepage' element={<Userhomepage />} />
          <Route path='/userbookings' element={<UserBookings />} />
          <Route path='/superadminhomepage' element={<SuperAdminHomePage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
