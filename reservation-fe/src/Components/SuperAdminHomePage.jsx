import { NavLink, Route, Routes } from "react-router-dom";
import "../Styles/superAdminTheme.css";
import SuperAdminTopbar from "./SuperAdminTopbar";
import SuperAdminDashboard from "./SuperAdminDashboard";

export default function SuperAdminHomePage() {
  return (
    <div className="saShell">
      <aside className="saSidebar">
        <div className="saBrand">
          <div className="saLogo">Befik / ReYatra</div>
          <div className="saTag">Super Admin Console</div>
        </div>

        <nav className="saNav">
          <NavLink end to="/superadmin" className={({ isActive }) => `saNavItem ${isActive ? "active" : ""}`}>
            Dashboard
          </NavLink>
        </nav>

        <div className="saSidebarFooter">
          <div className="saHint">
            Approve vendors carefully. Status impacts platform quality & revenue.
          </div>
        </div>
      </aside>

      <div className="saMain">
        <SuperAdminTopbar />
        <div className="saContent">
          <Routes>
            <Route path="/" element={<SuperAdminDashboard />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}