
import {Link} from "react-router-dom";
import'../Styles/LandingPage.css';
const LandingPage=()=>{
    return(

       
            
        <div className="LandingPage">
        <div className="logo">
                <h1><span>~Be</span>fik<span> / </span>Re<span>Yatra</span></h1>
            <div className="sub_lp">
            <Link to="/adminlogin">
                
                    <h3>Admin</h3>
                </Link>
                
                <Link to="/userlogin">
                   
                    <h3>User</h3>
                </Link>
            </div>
        </div>
        </div>
    )
}
export default LandingPage