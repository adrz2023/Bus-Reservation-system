import axios from "axios";
import { useState } from "react";
import '../Styles/UserSearchbar.css'
import BookBus from "./BookBus";
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

   export default function UserSearchbar(){
   let [from_location,setFrom_loaction]=useState("")
   let [to_location,setTo_locaton]=useState("")
   let[departureDate,setDepartureDate]=useState("") 
   let[trips,setTrips]=useState([]);
   let[bookingPopup,setBookingPopup]=useState(false)
    let[selectedTrip,setSelectedTrip]=useState(null)

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
    e.preventDefault();

    // Trip-first search (optional params supported)
    axios.get(`http://localhost:8080/api/trip/search`, {
      params: {
        from_location: from_location || undefined,
        to_location: to_location || undefined,
        departureDate: departureDate || undefined,
      }
    })
    .then( res=>{
        setTrips(Array.isArray(res.data.data) ? res.data.data : [])
    })
    .catch((err)=>{
        console.log(err);
    }  )
   }


   function bookTrip(trip) {
    setBookingPopup(true)
    setSelectedTrip(trip)
}

function handleSetFromLocation(e) {
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
      onChange={(_, value)=>{setTo_locaton(value?.label || "")}}   
    />




    <input type="date" value={departureDate} onChange={(e)=>{setDepartureDate(e.target.value)}} placeholder="enter departure date"  style={{
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

<Button type="submit" sx={{
    // color: `linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)`,
    color: 'white',
    backgroundColor: 'black',
    borderRadius: '10px',
}}>
    Search
</Button>


    </Stack>
</form>

{trips.length===0 && (
  <div style={{ textAlign: "center", marginTop: 14, color: "#555" }}>
    Search trips by route/date to see availability.
  </div>
)}

{trips.map((item)=>{
        return (
            <div style={{height:'100%',width:'40%',border:'2px solid black'}}>
        <div  className="bus_list">
             <img src={item.imageUrl} alt={item.busName} style={{ width: '80%', height: '60px', objectFit: 'cover', borderRadius: '4px', marginBottom: '10px' }} />
            <h4>{item.busName}</h4>
            <i>Available:{item.availableSeats}</i>
            <p>from:{item.from_location}</p>
            <p>to:{item.to_location}</p>
            <p>date:{item.departureDate}</p>
            <span>bus number:{item.busNumber}</span>

            <button onClick={()=>{bookTrip(item)}} >Book Trip</button>
            </div>
            </div>
        )
      })}

      {/* <h1>Bus Booking Discount Offers</h1> */}
      {bookingPopup && selectedTrip && <BookBus trip={selectedTrip} bookingPopup={bookingPopup} /> }
            
    </div>
  )
}