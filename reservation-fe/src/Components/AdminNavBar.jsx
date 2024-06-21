import NavDropDown from '../Components/NavDropDown';
import '../Styles/adminnavbar.css';
const AdminNavBar = () => {
    return ( 
        <div className="adminnavbar">
            <div className="logo">
                <h1><span>~Be</span>fik<span> / </span>Re<span>Yatra</span></h1>
            </div>
            <div className="options">
                <NavDropDown/>
            </div>
        </div>
     );
}
 
export default AdminNavBar;