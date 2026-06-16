import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { requestApi } from "../../api/requestApi";
import { inventoryApi } from "../../api/inventoryApi";
import styles from "./StudentDashboardPage.module.css";
import { BookOpen, FilePlus, Clock, ArrowRight, CheckCircle, AlertTriangle, HelpCircle } from "lucide-react";

export default function StudentDashboardPage({ user }) {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    pending: 0,
    approved: 0,
    rejected: 0,
    fulfilled: 0,
    totalItems: 0
  });

  useEffect(() => {
    const fetchStats = async () => {
      if (!user) return;
      const studentReqs = await requestApi.getStudentRequests(user.fullName);
      const items = await inventoryApi.getItems();
      
      const counts = studentReqs.reduce(
        (acc, req) => {
          const status = req.status.toLowerCase();
          if (acc[status] !== undefined) {
            acc[status]++;
          }
          acc.total++;
          return acc;
        },
        { pending: 0, approved: 0, rejected: 0, fulfilled: 0, total: 0 }
      );

      setStats({
        pending: counts.pending,
        approved: counts.approved,
        rejected: counts.rejected,
        fulfilled: counts.fulfilled,
        totalItems: items.length
      });
    };

    fetchStats();
  }, [user]);

  const dashboardCards = [
    {
      title: "Browse Stationery Catalog",
      description: "Explore available academic and office stationery supplies, search by categories, and check stock units.",
      icon: <BookOpen size={28} />,
      link: "/catalog",
      colorClass: styles.cardBlue,
      actionText: "View Catalog"
    },
    {
      title: "Submit Stationery Request",
      description: "Create a new stationery supply requisition. Add items to your request and submit for review.",
      icon: <FilePlus size={28} />,
      link: "/submit-request",
      colorClass: styles.cardIndigo,
      actionText: "Request Items"
    },
    {
      title: "Track Request Status",
      description: "Check the state of your requested supplies, view history, and inspect admin approval notes.",
      icon: <Clock size={28} />,
      link: "/my-requests",
      colorClass: styles.cardEmerald,
      actionText: "Track History"
    }
  ];

  return (
    <div className={styles.dashboardContainer}>
      <div className={styles.welcomeBanner}>
        <div className={styles.welcomeText}>
          <h1>Welcome back, {user?.fullName}!</h1>
          <p>Request and manage academic stationery supplies directly from your student portal.</p>
        </div>
      </div>

      <div className={styles.statsRow}>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconPending}`}>
            <Clock size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.pending}</span>
            <span className={styles.statLabel}>Pending Approvals</span>
          </div>
        </div>
        
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconApproved}`}>
            <CheckCircle size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.approved}</span>
            <span className={styles.statLabel}>Approved Requests</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconFulfilled}`}>
            <CheckCircle size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.fulfilled}</span>
            <span className={styles.statLabel}>Fulfilled Requests</span>
          </div>
        </div>

        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.iconCatalog}`}>
            <BookOpen size={20} />
          </div>
          <div className={styles.statInfo}>
            <span className={styles.statValue}>{stats.totalItems}</span>
            <span className={styles.statLabel}>Items in Catalog</span>
          </div>
        </div>
      </div>

      <h2 className={styles.sectionTitle}>Portal Activities</h2>
      <div className={styles.cardsGrid}>
        {dashboardCards.map((card, idx) => (
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
