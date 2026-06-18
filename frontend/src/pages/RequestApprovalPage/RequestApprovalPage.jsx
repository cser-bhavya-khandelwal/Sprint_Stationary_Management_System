import React, { useState, useEffect } from "react";
import { requestApi } from "../../api/requestApi";
import styles from "./RequestApprovalPage.module.css";
import { CheckSquare, Check, X, Clock, AlertCircle, FileText, CheckCircle } from "lucide-react";

export default function RequestApprovalPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState("PENDING"); // PENDING or PROCESSED (APPROVED, REJECTED, FULFILLED)
  
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const loadRequests = async () => {
    setLoading(true);
    try {
      const data = await requestApi.getRequests();
      setRequests(data);
    } catch (err) {
      console.error("Failed to load requests.", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, []);

  const handleApprove = async (id, itemName, studentName) => {
    setError("");
    setSuccess("");
    try {
      const res = await requestApi.approveRequest(id);
      if (res.success) {
        setSuccess(`Approved request ${id} for ${studentName} (${itemName}).`);
        loadRequests();
        setTimeout(() => setSuccess(""), 3000);
      } else {
        setError("Failed to approve request.");
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    }
  };

  const handleReject = async (id, itemName, studentName) => {
    setError("");
    setSuccess("");
    try {
      const res = await requestApi.rejectRequest(id);
      if (res.success) {
        setSuccess(`Rejected request ${id} for ${studentName} (${itemName}).`);
        loadRequests();
        setTimeout(() => setSuccess(""), 3000);
      } else {
        setError("Failed to reject request.");
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    }
  };

  const filteredRequests = requests.filter((req) => {
    if (activeTab === "PENDING") {
      return req.status === "PENDING";
    } else {
      return req.status !== "PENDING";
    }
  });

  const getStatusBadge = (status) => {
    switch (status.toUpperCase()) {
      case "PENDING":
        return <span className={`${styles.badge} ${styles.badgePending}`}>PENDING</span>;
      case "APPROVED":
        return <span className={`${styles.badge} ${styles.badgeApproved}`}>APPROVED</span>;
      case "REJECTED":
        return <span className={`${styles.badge} ${styles.badgeRejected}`}>REJECTED</span>;
      case "FULFILLED":
        return <span className={`${styles.badge} ${styles.badgeFulfilled}`}>FULFILLED</span>;
      default:
        return <span className={styles.badge}>{status}</span>;
    }
  };

  return (
    <div className={styles.approvalContainer}>
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <CheckSquare className={styles.headerIcon} size={24} />
          <div>
            <h1>Request Approvals</h1>
            <p>Review, approve, or reject student stationery requisition requests.</p>
          </div>
        </div>
      </div>

      <div className={styles.tabsRow}>
        <button
          className={`${styles.tabBtn} ${activeTab === "PENDING" ? styles.activeTab : ""}`}
          onClick={() => setActiveTab("PENDING")}
        >
          <span>Pending Submissions</span>
          <span className={styles.tabCount}>
            {requests.filter((r) => r.status === "PENDING").length}
          </span>
        </button>
        <button
          className={`${styles.tabBtn} ${activeTab === "PROCESSED" ? styles.activeTab : ""}`}
          onClick={() => setActiveTab("PROCESSED")}
        >
          <span>Processed Archives</span>
          <span className={styles.tabCount}>
            {requests.filter((r) => r.status !== "PENDING").length}
          </span>
        </button>
      </div>

      {error && (
        <div className={styles.errorAlert}>
          <AlertCircle size={18} />
          <span>{error}</span>
        </div>
      )}

      {success && (
        <div className={styles.successAlert}>
          <CheckCircle size={18} />
          <span>{success}</span>
        </div>
      )}

      {loading ? (
        <div className={styles.loaderContainer}>
          <div className={styles.spinner}></div>
          <p>Fetching requisition requests...</p>
        </div>
      ) : filteredRequests.length === 0 ? (
        <div className={styles.emptyState}>
          <FileText size={48} />
          <h3>No Requests Found</h3>
          <p>
            {activeTab === "PENDING"
              ? "All student stationery requests have been processed! Good job."
              : "No historical or processed requests on record yet."}
          </p>
        </div>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.approvalTable}>
            <thead>
              <tr>
                <th>Request ID</th>
                <th>Student Name</th>
                <th>Stationery Item</th>
                <th className={styles.textCenter}>Quantity</th>
                <th>Status</th>
                {activeTab === "PENDING" && <th className={styles.textRight}>Actions</th>}
              </tr>
            </thead>
            <tbody>
              {filteredRequests.map((req) => (
                <tr key={req.keyId || req.id}>
                  <td>
                    <span className={styles.requestId}>{req.id}</span>
                  </td>
                  <td>
                    <span className={styles.studentName}>{req.studentName}</span>
                  </td>
                  <td>
                    <span className={styles.itemName}>{req.itemName}</span>
                  </td>
                  <td className={styles.textCenter}>
                    <span className={styles.quantity}>{req.quantity}</span>
                  </td>
                  <td>{getStatusBadge(req.status)}</td>
                  {activeTab === "PENDING" && (
                    <td className={styles.textRight}>
                      <div className={styles.actionsGroup}>
                        <button
                          type="button"
                          className={styles.rejectBtn}
                          onClick={() => handleReject(req.id, req.itemName, req.studentName)}
                          title="Reject Requisition"
                        >
                          <X size={15} />
                          <span>Reject</span>
                        </button>
                        <button
                          type="button"
                          className={styles.approveBtn}
                          onClick={() => handleApprove(req.id, req.itemName, req.studentName)}
                          title="Approve Requisition"
                        >
                          <Check size={15} />
                          <span>Approve</span>
                        </button>
                      </div>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
