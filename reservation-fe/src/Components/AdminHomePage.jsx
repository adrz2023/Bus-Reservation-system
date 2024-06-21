import { Routes,Route } from "react-router-dom";
import AdminNavBar from "./AdminNavBar";

import AdminDashbord from "./AdminDashboard";
import ViewBus from "./ViewBus";
import AddBus from "./AddBus";
import EditBus from "./EditBus";
const AdminHomePage = () => {
    return ( 
        <div className="">
      <AdminNavBar/>
     
       <Routes>
        <Route path="/" element={<AdminDashbord/>}/>
        <Route path="/vewbus" element={<ViewBus/>}/> 
        <Route path="/addbus" element={<AddBus/>}/>
        <Route path="/editbus/:id" element={<EditBus/>}/>
       </Routes>
        </div>
     );
}
 
export default AdminHomePage;