import "../Styles/superAdminTheme.css";

export default function SuperAdminTopbar() {
  return (
    <div className="saTopbar">
      <div>
        <div className="saTopTitle">Platform Insights</div>
        <div className="saTopSub">KPIs + Vendor Approval Hub</div>
      </div>

      <div className="saTopRight">
        <span className="saChip">Live</span>
      </div>
    </div>
  );
}