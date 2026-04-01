import { Routes, Route, NavLink } from "react-router-dom";
import AdminNavBar from "./AdminNavBar";
import AdminDashbord from "./AdminDashboard";
import ViewBus from "./ViewBus";
import AddBus from "./AddBus";
import EditBus from "./EditBus";
import ManageTrips from "./ManageTrips";
import Trips from "./Trips";
import "../Styles/adminTheme.css";

const AdminHomePage = () => {
  return (
    <div className="adminShell">
      <aside className="adminSidebar">
        <div className="adminSidebarBrand">
          <div className="adminSidebarLogo">
            <span className="adminAccent">Be</span>fik / Re<span className="adminAccent">Yatra</span>
          </div>
          <div className="adminSidebarTag">Vendor Console</div>
        </div>

        <nav className="adminSidebarNav">
          <NavLink end to="/adminhomepage" className={({ isActive }) => `adminNavItem ${isActive ? "active" : ""}`}>
            Dashboard
          </NavLink>
          <NavLink to="/adminhomepage/vewbus" className={({ isActive }) => `adminNavItem ${isActive ? "active" : ""}`}>
            Buses
          </NavLink>
          <NavLink to="/adminhomepage/addbus" className={({ isActive }) => `adminNavItem ${isActive ? "active" : ""}`}>
            Add Bus
          </NavLink>
          <NavLink to="/adminhomepage/trips" className={({ isActive }) => `adminNavItem ${isActive ? "active" : ""}`}>
            Trips
          </NavLink>
           <NavLink to="/adminhomepage/addtrips" className={({ isActive }) => `adminNavItem ${isActive ? "active" : ""}`}>
            Add Trips
          </NavLink>
        </nav>

        <div className="adminSidebarFooter">
          <div className="adminHint">
            Tip: Add high-quality images & correct route names to improve user trust.
          </div>
        </div>
      </aside>

      <div className="adminMain">
        <AdminNavBar />
        <div className="adminContent">
          <Routes>
            <Route path="/" element={<AdminDashbord />} />
            <Route path="/vewbus" element={<ViewBus />} />
            <Route path="/addbus" element={<AddBus />} />
            <Route path="/editbus/:id" element={<EditBus />} />
            <Route path="/trips" element={<Trips />} />
            <Route path="/addtrips" element={<ManageTrips />} />
            <Route path="/bus/:busId/trips" element={<ManageTrips />} />
          </Routes>
        </div>
      </div>
    </div>
  );
};

export default AdminHomePage;