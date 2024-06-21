import Usernavbar from "./Usernavbar";
import UserSearchbar from "./UserSearchbar";
import UserViewBus from "./UserViewBus";
import busstand from '../Photos/busstand.png'
import "../Styles/userhomepage.css"
const Userhomepage=()=>{

    return(
<div className="">
    <Usernavbar/>

    <div className="img2" >
       
       <img src={busstand} alt="error" style= {{marginTop:'60%'}}/>   
    </div>

    <UserSearchbar/>
    {/* <UserViewBus/> */}

</div>

    );
} 

export default Userhomepage;