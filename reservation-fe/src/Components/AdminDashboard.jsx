
import React from "react";
import ViewBus from "./ViewBus";
import "../Styles/adminTheme.css";

const AdminDashbord = () => {
  return (
    <div className="">
      <div className="adminPanel">
        <div className="adminPanelHeader">
          <div>
            <h3>Dashboard</h3>
            <p>Overview of your inventory and availability</p>
          </div>
        </div>
        <div className="adminPanelBody">
          <div className="adminStatGrid">
            <div className="adminStatCard">
              <div className="label">Total buses</div>
              <div className="value">—</div>
            </div>
            <div className="adminStatCard">
              <div className="label">Active routes</div>
              <div className="value">—</div>
            </div>
            <div className="adminStatCard">
              <div className="label">Seats listed</div>
              <div className="value">—</div>
            </div>
          </div>

          <div style={{ marginTop: 6 }}>
            <div className="adminPanelHeader" style={{ padding: 0, borderBottom: "none" }}>
              <div>
                <h3 style={{ fontSize: 15 }}>Your buses</h3>
                <p>Manage pricing, dates, seats and details</p>
              </div>
            </div>
            <ViewBus />
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashbord;