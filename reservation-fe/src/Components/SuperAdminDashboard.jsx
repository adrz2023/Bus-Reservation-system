import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import "../Styles/superAdminTheme.css";

const API = "http://localhost:8080";

const APPROVAL_OPTIONS = [
  "UNDER_SCREENING",
  "DOCUMENT_REVIEW",
  "APPROVED",
  "REJECT"
];

export default function SuperAdminDashboard() {
  const [kpis, setKpis] = useState(null);
  const [kpiErr, setKpiErr] = useState("");

  const [vendors, setVendors] = useState([]);
  const [vendorsErr, setVendorsErr] = useState("");
  const [loadingVendors, setLoadingVendors] = useState(true);

  const [tab, setTab] = useState("ALL"); // ALL | PENDING
  const [savingId, setSavingId] = useState(null);
  const [openActionId, setOpenActionId] = useState(null);

  const fetchKpis = async () => {
    setKpiErr("");
    try {
      const res = await axios.get(`${API}/api/superadmin/dashboardkpis`);
      setKpis(res?.data?.data || null);
    } catch (e) {
      setKpiErr("Failed to load KPIs");
      setKpis(null);
    }
  };

  const fetchVendors = async (mode) => {
    setLoadingVendors(true);
    setVendorsErr("");
    try {
      const url =
        mode === "PENDING"
          ? `${API}/api/superadmin/vendors/pending`
          : `${API}/api/superadmin/vendors`;

      const res = await axios.get(url);
      setVendors(Array.isArray(res?.data?.data) ? res.data.data : []);
    } catch (e) {
      setVendorsErr("Failed to load vendors");
      setVendors([]);
    } finally {
      setLoadingVendors(false);
    }
  };

  useEffect(() => {
    fetchKpis();
  }, []);

  useEffect(() => {
    fetchVendors(tab);
  }, [tab]);

  const kpiCards = useMemo(() => {
    if (!kpis) return [];
    return [
      { label: "Total revenue", value: `₹${kpis.totalRevenue ?? 0}` },
      { label: "Ticket sales", value: `${kpis.totalTickets ?? 0}` },
      { label: "Cancellation rate", value: `${(kpis.cancellationRate ?? 0).toFixed?.(2) ?? kpis.cancellationRate ?? 0}%` },
      { label: "Total users", value: `${kpis.totalUsers ?? 0}` }
    ];
  }, [kpis]);

  const updateStatus = async (vendorId, status) => {
    setSavingId(vendorId);
    try {
      await axios.put(`${API}/api/superadmin/vendor/${vendorId}/approval-status`, { status });
      // refresh list for accuracy
      await fetchVendors(tab);
      await fetchKpis();
    } catch (e) {
      alert("Failed to update approval status");
    } finally {
      setSavingId(null);
    }
  };

  return (
    <div className="saGrid">
      <section className="saPanel">
        <div className="saPanelHead">
          <div>
            <h3>Real-time Insights</h3>
            <p>Revenue, sales, cancellations and user growth</p>
          </div>
          <button className="saBtn saBtnGhost" onClick={fetchKpis}>Refresh</button>
        </div>

        {kpiErr ? (
          <div className="saError">{kpiErr}</div>
        ) : !kpis ? (
          <div className="saEmpty">Loading KPIs…</div>
        ) : (
          <div className="saKpiGrid">
            {kpiCards.map((c) => (
              <div className="saKpiCard" key={c.label}>
                <div className="saKpiLabel">{c.label}</div>
                <div className="saKpiValue">{c.value}</div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section className="saPanel">
        <div className="saPanelHead">
          <div>
            <h3>Integrated Approval Hub</h3>
            <p>Manage vendors through lifecycle statuses</p>
          </div>

          <div className="saTabs">
            <button className={tab === "ALL" ? "active" : ""} onClick={() => setTab("ALL")}>All</button>
            <button className={tab === "PENDING" ? "active" : ""} onClick={() => setTab("PENDING")}>Pending</button>
          </div>
        </div>

        {vendorsErr ? (
          <div className="saError">{vendorsErr}</div>
        ) : loadingVendors ? (
          <div className="saEmpty">Loading vendors…</div>
        ) : vendors.length === 0 ? (
          <div className="saEmpty">No vendors found.</div>
        ) : (
          <div className="saTableWrap">
            <table className="saTable">
              <thead>
                <tr>
                  <th>Vendor</th>
                  <th>Travels</th>
                  <th>GST</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>
                {vendors.map((v) => (
                  <tr key={v.id}>
                    <td>
                      <div className="saCellTitle">{v.name}</div>
                      <div className="saCellSub">{v.email}</div>
                    </td>
                    <td>{v.travels_name}</td>
                    <td>{v.gst_number}</td>
                    <td>
                      <span className={`saStatus ${String(v.approvalStatus || "").toLowerCase()}`}>
                        {v.approvalStatus || "—"}
                      </span>
                    </td>
                   <td>
  <div className="saActionWrap">
    <span className={`saStatusPill ${String(v.approvalStatus || "").toLowerCase()}`}>
      {v.approvalStatus || "UNDER_SCREENING"}
    </span>

    <button
      type="button"
      className="saActionBtn"
      onClick={() => setOpenActionId(openActionId === v.id ? null : v.id)}
      disabled={savingId === v.id}
    >
      Change
    </button>

    {openActionId === v.id && (
      <div className="saActionMenu">
        {APPROVAL_OPTIONS.map((status) => (
          <button
            type="button"
            key={status}
            className={`saActionItem ${v.approvalStatus === status ? "active" : ""}`}
            onClick={() => {
              setOpenActionId(null);
              updateStatus(v.id, status);
            }}
          >
            {status.replaceAll("_", " ")}
          </button>
        ))}
      </div>
    )}
  </div>
</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}