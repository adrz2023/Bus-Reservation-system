import NavDropDown from "../Components/NavDropDown";
import "../Styles/adminTheme.css";

const AdminNavBar = () => {
  return (
    <div className="adminTopbar">
      <div className="adminTopbarTitle">
        <h2>Admin Dashboard</h2>
        <span>Manage buses, routes, pricing & availability</span>
      </div>

      <div className="adminTopbarActions">
        <div className="adminPill">Live</div>
        <NavDropDown />
      </div>
    </div>
  );
};

export default AdminNavBar;