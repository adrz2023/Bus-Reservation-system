import axios from "axios";
import { useState } from "react";
import '../Styles/UserSearchbar.css'
import BookBus from "./BookBus";
import UserViewBus from "./UserViewBus";
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

   export default function UserSearchbar(){
   let [from_location,setFrom_loaction]=useState("")
   let [to_location,setTo_locaton]=useState("")
   let[bus_depurture,setBus_depurture]=useState("") 
   let[buses,setBuses]=useState([]);
   let[bookingPopup,setBookingPopup]=useState(false)
    let[busId,setBusId]=useState("")

    const from_location_options = [
        { label: 'kolkata' },
        { label: 'Pokhara' },
        { label: 'Biratnagar' },
        { label: 'Dharan' },
        { label: 'Birgunj' },
        { label: 'Butwal' },
        { label: 'Hetauda' },
        { label: 'Nepaljung' },
        { label: 'Janakpur' },
        { label: 'Dhangadi' },
        { label: 'Bharatpur' },
        { label: 'Itahari' },
        { label: 'Damak' },
        { label: 'Kalaiya' },
        { label: 'Lahan' },
        { label: 'Tikapur' },
        { label: 'Ghorahi' },
        { label: 'Bhadrapur' },
        { label: 'Tulsipur' },
        { label: 'Rajbiraj' },
        { label: 'Tansen' },
        { label: 'Gulariya' },
        { label: 'Khandbari' },
    ]

    const to_location_options = [
        { label: 'mumbai' },
        { label: 'Pokhara' },
        { label: 'Biratnagar' },
        { label: 'Dharan' },
        { label: 'Birgunj' },
        { label: 'Butwal' },
        { label: 'Hetauda' },
        { label: 'Nepaljung' },
        { label: 'Janakpur' },
        { label: 'Dhangadi' },
        { label: 'Bharatpur' },
        { label: 'Itahari' },
        { label: 'Damak' },
        { label: 'Kalaiya' },
        { label: 'Lahan' },
        { label: 'Tikapur' },
        { label: 'Ghorahi' },
        { label: 'Bhadrapur' },
        { label: 'Tulsipur' },
        { label: 'Rajbiraj' },
        { label: 'Tansen' },
        { label: 'Gulariya' },
        { label: 'Khandbari' },
    ]



   function searchBus(e){
    console.log(from_location,to_location,bus_depurture)
    e.preventDefault();
    axios.get(`http://localhost:8080/api/bus/find?from_location=${from_location}&to_location=${to_location}&bus_depurture=${bus_depurture}`)
    .then( res=>{
        console.log(res.data.data)
        setBuses(res.data.data)
    })
    .catch((err)=>{
        console.log(err);
    }  )
   }


   function bookbus(id) {
    setBookingPopup(!bookingPopup)
    setBusId(id)
}

function handleSetFromLocation(e) {
    console.log(e)
    setFrom_loaction(e.target.value)
}

return(
    <div className="userSerach">
<form onSubmit={searchBus} action="" >

    {/* <input type="text" required value={from_location} onChange={(e)=>{setFrom_loaction(e.target.value)}} placeholder="enter from location" style={{
            width: '20%', 
            padding: '10px', 
            margin: '10px 0', 
            borderRadius: '10px', 
            border: '1px solid #ccc', 
            fontSize: '16px' }} /> */}

                    {/* <input type="text" required value={to_location} onChange={(e)=>{setTo_locaton(e.target.value)}} placeholder="enter to loaction" style={{
            width: '20%', 
            padding: '10px', 
            margin: '10px 0', 
            borderRadius: '10px', 
            border: '1px solid #ccc', 
            fontSize: '16px'}}/> */}

<Stack spacing={2}  direction={'row'} justifyContent={'center'}>
<Autocomplete
      disablePortal
      id="combo-box-demo"
      options={from_location_options}
      sx={{ width: 300 }}
      renderInput={(params) => <TextField {...params} label="from location" onChange={handleSetFromLocation} /> }
    //   onChange={handleSetFromLocation}
    //   onSelect={handleSetFromLocation}
    />


<Autocomplete
      disablePortal
      id="combo-box-demo"
      options={to_location_options}
      sx={{ width: 300 }}
      renderInput={(params) => <TextField {...params} label="to location" />}
      onChange={(e)=>{setTo_locaton(e.target.value)}}   
    />




    <input type="date" required value={bus_depurture} on onChange={(e)=>{setBus_depurture(e.target.value)}} placeholder="enter bus departure"  style={{
            width: '20%', 
            padding: '9px', 
            margin: '10px 0', 
            borderRadius: '10px', 
            border: '1px solid #ccc', 
            fontSize: '16px'
        }}
        />
    {/* <button type="submit"   style={{
        width: '20%', 
        padding: '10px', 
        margin: '10px 0', 
        borderRadius: '10px', 
        backgroundColor: 'orange', 
        color: '#fff', 
        border: 'none', 
        fontSize: '16px', 
        cursor: 'pointer',
        transition: 'background-color 0.3s'
    }}>search</button> */}

<Button sx={{
    // color: `linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)`,
    color: 'white',
    backgroundColor: 'black',
    borderRadius: '10px',
}}>
    Search
</Button>


    </Stack>
</form>

{buses.length===0 && <UserViewBus/>}

{buses.map((item)=>{
        return (
            <div style={{height:'100%',width:'40%',border:'2px solid black'}}>
        <div  className="bus_list">
             <img src={item.imageUrl} alt={item.name} style={{ width: '80%', height: '60px', objectFit: 'cover', borderRadius: '4px', marginBottom: '10px' }} />
            <h4>{item.name}</h4>
            <i>Seat:{item.seats}</i>
            <p>from:{item.from_location}</p>
            <p>to:{item.to_location}</p>
            <p>date:{item.bus_depurture}</p>
            <span>bus number:{item.bus_number}</span>

            <button onClick={()=>{bookbus(item.id)}} >Book Bus</button>
            </div>
            </div>
        )
      })}

      {/* <h1>Bus Booking Discount Offers</h1> */}
      {bookingPopup && <BookBus id={busId} bookingPopup={bookingPopup} /> }
            
    </div>
  )
}