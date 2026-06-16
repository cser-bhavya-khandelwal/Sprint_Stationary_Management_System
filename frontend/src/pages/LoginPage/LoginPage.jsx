import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authApi } from "../../api/authApi";
import styles from "./LoginPage.module.css";
import { GraduationCap, Mail, Lock, ArrowRight, AlertCircle } from "lucide-react";

export default function LoginPage({ onLoginSuccess }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    if (!email || !password) {
      setError("Please fill in all fields.");
      return;
    }

    setLoading(true);
    try {
      const result = await authApi.login(email, password);
      if (result.success) {
        onLoginSuccess(result.user);
        if (result.user.role === "ADMIN") {
          navigate("/admin-dashboard");
        } else {
          navigate("/student-dashboard");
        }
      } else {
        setError("Invalid email or password.");
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const fillDemoCredentials = (role) => {
    if (role === "ADMIN") {
      setEmail("admin@university.edu");
      setPassword("admin123");
    } else {
      setEmail("student@university.edu");
      setPassword("student123");
    }
  };

  return (
    <div className={styles.loginContainer}>
      <div className={styles.loginCard}>
        <div className={styles.header}>
          <div className={styles.logoBadge}>
            <GraduationCap size={32} />
          </div>
          <h1>Sign In</h1>
          <p>Stationery Management System</p>
        </div>

        {error && (
          <div className={styles.errorAlert}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleLogin} className={styles.form}>
          <div className={styles.inputGroup}>
            <label htmlFor="email">Email Address</label>
            <div className={styles.inputWrapper}>
              <Mail className={styles.inputIcon} size={18} />
              <input
                id="email"
                type="email"
                placeholder="e.g. student@university.edu"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div className={styles.inputGroup}>
            <div className={styles.labelRow}>
              <label htmlFor="password">Password</label>
            </div>
            <div className={styles.inputWrapper}>
              <Lock className={styles.inputIcon} size={18} />
              <input
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? "Signing in..." : "Sign In"}
            <ArrowRight size={18} />
          </button>
        </form>

        <div className={styles.divider}>
          <span>OR QUICK LOG IN</span>
        </div>

        <div className={styles.demoButtons}>
          <button
            type="button"
            className={`${styles.demoBtn} ${styles.studentDemo}`}
            onClick={() => fillDemoCredentials("STUDENT")}
          >
            Student Account
          </button>
          <button
            type="button"
            className={`${styles.demoBtn} ${styles.adminDemo}`}
            onClick={() => fillDemoCredentials("ADMIN")}
          >
            Admin Account
          </button>
        </div>

        <div className={styles.footer}>
          <span>Don't have an account? </span>
          <Link to="/register" className={styles.link}>
            Register here
          </Link>
        </div>
      </div>
    </div>
  );
}
