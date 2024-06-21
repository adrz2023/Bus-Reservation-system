import '../Styles/adminnavbar.css'
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';
const Usernavbar =()=>{
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem('userId');
        navigate('/userLogin');
        
    };
    return(
            <div className="adminnavbar">
        <div className="logo" style={{
            display: "flex",
            justifyContent: "space-between",    
            alignItems: "center",
        }}>
            <h1><span>~Be</span>fik<span> / </span>Re<span>Yatra</span></h1>
            <Button variant="outline-light" className="btn" onClick={handleLogout}>Logout</Button>
        </div>
       
    </div>
 );
}
export default Usernavbar;