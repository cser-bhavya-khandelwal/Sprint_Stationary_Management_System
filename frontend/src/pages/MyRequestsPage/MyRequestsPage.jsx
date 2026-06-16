import React, { useState, useEffect } from "react";
import { requestApi } from "../../api/requestApi";
import styles from "./MyRequestsPage.module.css";
import { History, Clock, CheckCircle2, XCircle, PackageCheck, AlertCircle, RefreshCw } from "lucide-react";

export default function MyRequestsPage({ user }) {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadRequests = async () => {
    if (!user) return;
    setLoading(true);
    try {
      const data = await requestApi.getStudentRequests(user.fullName);
      setRequests(data);
    } catch (err) {
      console.error("Failed to load requests", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, [user]);

  const getStatusBadge = (status) => {
    switch (status.toUpperCase()) {
      case "PENDING":
        return (
          <span className={`${styles.badge} ${styles.badgePending}`}>
            <Clock size={12} className={styles.badgeIcon} />
            PENDING
          </span>
        );
      case "APPROVED":
        return (
          <span className={`${styles.badge} ${styles.badgeApproved}`}>
            <CheckCircle2 size={12} className={styles.badgeIcon} />
            APPROVED
          </span>
        );
      case "REJECTED":
        return (
          <span className={`${styles.badge} ${styles.badgeRejected}`}>
            <XCircle size={12} className={styles.badgeIcon} />
            REJECTED
          </span>
        );
      case "FULFILLED":
        return (
          <span className={`${styles.badge} ${styles.badgeFulfilled}`}>
            <PackageCheck size={12} className={styles.badgeIcon} />
            FULFILLED
          </span>
        );
      default:
        return <span className={styles.badge}>{status}</span>;
    }
  };

  return (
    <div className={styles.requestsContainer}>
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <History className={styles.headerIcon} size={24} />
          <div>
            <h1>My Requests</h1>
            <p>Track the approval status of your stationery requisitions.</p>
          </div>
        </div>
        <button className={styles.refreshBtn} onClick={loadRequests} disabled={loading}>
          <RefreshCw size={18} className={loading ? styles.spinning : ""} />
          <span>Refresh History</span>
        </button>
      </div>

      {loading ? (
        <div className={styles.loaderContainer}>
          <div className={styles.spinner}></div>
          <p>Loading your request logs...</p>
        </div>
      ) : requests.length === 0 ? (
        <div className={styles.emptyState}>
          <AlertCircle size={48} />
          <h3>No Requests Found</h3>
          <p>You haven't submitted any stationery requests yet.</p>
        </div>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.requestsTable}>
            <thead>
              <tr>
                <th>Request ID</th>
                <th>Stationery Item</th>
                <th>Request Date</th>
                <th className={styles.textCenter}>Quantity</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req) => (
                <tr key={req.id}>
                  <td>
                    <span className={styles.requestId}>{req.id}</span>
                  </td>
                  <td>
                    <span className={styles.itemName}>{req.itemName}</span>
                  </td>
                  <td>
                    <span className={styles.requestDate}>{req.date}</span>
                  </td>
                  <td className={styles.textCenter}>
                    <span className={styles.quantity}>{req.quantity}</span>
                  </td>
                  <td>{getStatusBadge(req.status)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
