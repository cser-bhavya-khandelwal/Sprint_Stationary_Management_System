import React from "react";
import { Link, useLocation } from "react-router-dom";
import styles from "./Sidebar.module.css";
import {
  LayoutDashboard,
  BookOpen,
  FilePlus,
  History,
  Package,
  CheckSquare,
  GraduationCap,
  ShieldAlert
} from "lucide-react";

export default function Sidebar({ user }) {
  const location = useLocation();
  if (!user) return null;

  const isActive = (path) => {
    return location.pathname === path;
  };

  const studentLinks = [
    {
      path: "/student-dashboard",
      label: "Dashboard",
      icon: <LayoutDashboard size={20} />
    },
    {
      path: "/catalog",
      label: "Stationery Catalog",
      icon: <BookOpen size={20} />
    },
    {
      path: "/submit-request",
      label: "Submit Request",
      icon: <FilePlus size={20} />
    },
    {
      path: "/my-requests",
      label: "My Requests",
      icon: <History size={20} />
    }
  ];

  const adminLinks = [
    {
      path: "/admin-dashboard",
      label: "Dashboard",
      icon: <LayoutDashboard size={20} />
    },
    {
      path: "/inventory",
      label: "Manage Inventory",
      icon: <Package size={20} />
    },
    {
      path: "/request-approval",
      label: "Request Approvals",
      icon: <CheckSquare size={20} />
    }
  ];

  const links = user.role === "ADMIN" ? adminLinks : studentLinks;

  return (
    <aside className={styles.sidebar}>
      <div className={styles.roleCard}>
        <div className={styles.roleIcon}>
          {user.role === "ADMIN" ? <ShieldAlert size={28} /> : <GraduationCap size={28} />}
        </div>
        <div className={styles.roleDetails}>
          <span className={styles.roleTitle}>{user.role} PORTAL</span>
          <span className={styles.roleSub}>System Navigation</span>
        </div>
      </div>

      <nav className={styles.navMenu}>
        {links.map((link) => (
          <Link
            key={link.path}
            to={link.path}
            className={`${styles.navItem} ${
              isActive(link.path) ? styles.activeItem : ""
            }`}
          >
            <span className={styles.iconWrapper}>{link.icon}</span>
            <span className={styles.navLabel}>{link.label}</span>
          </Link>
        ))}
      </nav>

      <div className={styles.footer}>
        <span className={styles.version}>v1.0.0 (Foundation)</span>
      </div>
    </aside>
  );
}
