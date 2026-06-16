import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { requestApi } from "../../api/requestApi";
import { inventoryApi } from "../../api/inventoryApi";
import styles from "./AdminDashboardPage.module.css";
import { Package, CheckSquare, XCircle, AlertTriangle, Users, FileText, ArrowRight } from "lucide-react";

export default function AdminDashboardPage({ user }) {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    totalItems: 0,
    lowStock: 0,
    pendingRequests: 0,
    processedRequests: 0
  });

  useEffect(() => {
    const fetchAdminStats = async () => {
      const items = await inventoryApi.getItems();
      const requests = await requestApi.getRequests();

      const lowStockCount = items.filter(
        (item) => item.availableQuantity <= item.minimumQuantity
      ).length;

      const pendingCount = requests.filter((r) => r.status === "PENDING").length;
      const processedCount = requests.filter((r) => r.status !== "PENDING").length;

      setStats({
        totalItems: items.length,
        lowStock: lowStockCount,
        pendingRequests: pendingCount,
        processedRequests: processedCount
      });
    };

    fetchAdminStats();
  }, []);

  const adminCards = [
    {
      title: "Manage Inventory",
      description: "Inspect active stationery stock, update available counts, add new school supplies, or set minimum threshold alerts.",
      icon: <Package size={28} />,
      link: "/inventory",
      colorClass: styles.cardBlue,
      actionText: "Open Inventory"
    },
    {
      title: "Approve Requests",
      description: "Review pending student submissions, inspect requested quantities, and approve requisitions to dispatch items.",
      icon: <CheckSquare size={28} />,
      link: "/request-approval",
      colorClass: styles.cardEmerald,
      actionText: "Review Approvals"
    },
    {
      title: "Reject Requests",
      description: "Inspect student requests that do not align with department supply quotas and mark them as rejected.",
      icon: <XCircle size={28} />,
      link: "/request-approval",
      colorClass: styles.cardRose,
      actionText: "Review Rejections"
    }
  ];

  return (
    <div className={styles.adminContainer}>
      <div className={styles.welcomeBanner}>
        <div className={styles.welcomeText}>
          <h1>Admin Control Panel</h1>
          <p>Supervise academy-wide stationery allocation, monitor stock levels, and authorize requisition requests.</p>
        </div>
      </div>

      <div className={styles.statsRow}>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconInventory}`}>
            <Package size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.totalItems}</span>
            <span className={styles.statLabel}>Stationery Items</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconPending}`}>
            <FileText size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.pendingRequests}</span>
            <span className={styles.statLabel}>Pending Requests</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconLowStock} ${stats.lowStock > 0 ? styles.animatePulse : ""}`}>
            <AlertTriangle size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.lowStock}</span>
            <span className={styles.statLabel}>Low Stock Alerts</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconProcessed}`}>
            <CheckSquare size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.processedRequests}</span>
            <span className={styles.statLabel}>Processed Requisitions</span>
          </div>
        </div>
      </div>

      <h2 className={styles.sectionTitle}>Inventory & Requisition Control</h2>
      
      <div className={styles.cardsGrid}>
        {adminCards.map((card, idx) => (
          <div key={idx} className={`${styles.actionCard} ${card.colorClass}`}>
            <div className={styles.cardHeader}>
              <div className={styles.cardIcon}>{card.icon}</div>
              <h3>{card.title}</h3>
            </div>
            <p className={styles.cardDescription}>{card.description}</p>
            <button
              onClick={() => navigate(card.link)}
              className={styles.cardActionBtn}
            >
              <span>{card.actionText}</span>
              <ArrowRight size={16} />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
