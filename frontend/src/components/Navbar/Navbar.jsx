import React from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "../../api/authApi";
import styles from "./Navbar.module.css";
import { LogOut, User, GraduationCap, Shield } from "lucide-react";

export default function Navbar({ user, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    authApi.logout();
    if (onLogout) onLogout();
    navigate("/login");
  };

  return (
    <header className={styles.navbar}>
      <div className={styles.logoSection} onClick={() => navigate(user?.role === "ADMIN" ? "/admin-dashboard" : "/student-dashboard")}>
        <div className={styles.logoIcon}>
          <GraduationCap size={24} />
        </div>
        <div className={styles.logoText}>
          <span className={styles.brand}>Hogwarts Academy</span>
          <span className={styles.subBrand}>Stationery Portal</span>
        </div>
      </div>

      {user && (
        <div className={styles.userSection}>
          <div className={styles.userInfo}>
            <span className={styles.userName}>{user.fullName}</span>
            <span className={`${styles.roleBadge} ${user.role === "ADMIN" ? styles.adminBadge : styles.studentBadge}`}>
              {user.role === "ADMIN" ? (
                <>
                  <Shield size={12} className={styles.badgeIcon} /> Admin
                </>
              ) : (
                <>
                  <GraduationCap size={12} className={styles.badgeIcon} /> Student
                </>
              )}
            </span>
          </div>

          <div className={styles.profileAvatar}>
            <User size={18} />
          </div>

          <button className={styles.logoutBtn} onClick={handleLogout} title="Log Out">
            <LogOut size={18} />
            <span className={styles.logoutText}>Log Out</span>
          </button>
        </div>
      )}
    </header>
  );
}
