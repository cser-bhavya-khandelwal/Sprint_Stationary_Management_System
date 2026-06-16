import React, { useState, useEffect } from "react";
import { BrowserRouter, Routes, Route, Navigate, Outlet } from "react-router-dom";
import { authApi } from "./api/authApi";

// CSS
import "./app/global.css";

// Layout Components
import Navbar from "./components/Navbar/Navbar";
import Sidebar from "./components/Sidebar/Sidebar";

// Pages
import LoginPage from "./pages/LoginPage/LoginPage";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import StudentDashboardPage from "./pages/StudentDashboardPage/StudentDashboardPage";
import AdminDashboardPage from "./pages/AdminDashboardPage/AdminDashboardPage";
import CatalogPage from "./pages/CatalogPage/CatalogPage";
import RequestSubmissionPage from "./pages/RequestSubmissionPage/RequestSubmissionPage";
import MyRequestsPage from "./pages/MyRequestsPage/MyRequestsPage";
import InventoryPage from "./pages/InventoryPage/InventoryPage";
import RequestApprovalPage from "./pages/RequestApprovalPage/RequestApprovalPage";

// Portal Layout Wrapper containing Navbar & Sidebar
function PortalLayout({ user, onLogout }) {
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  const layoutStyle = {
    display: "flex",
    flexDirection: "column",
    minHeight: "100vh"
  };

  const bodyStyle = {
    display: "flex",
    flex: 1,
    position: "relative"
  };

  const contentStyle = {
    flex: 1,
    padding: "32px",
    overflowY: "auto",
    maxWidth: "1400px",
    margin: "0 auto",
    width: "100%"
  };

  return (
    <div style={layoutStyle}>
      <Navbar user={user} onLogout={onLogout} />
      <div style={bodyStyle}>
        <Sidebar user={user} />
        <main style={contentStyle}>
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default function App() {
  const [user, setUser] = useState(authApi.getCurrentUser());

  const handleLoginSuccess = (loggedInUser) => {
    setUser(loggedInUser);
  };

  const handleLogout = () => {
    setUser(null);
  };

  // Root redirect logic based on login state and user role
  const getRootRedirect = () => {
    if (!user) {
      return <Navigate to="/login" replace />;
    }
    return user.role === "ADMIN" ? (
      <Navigate to="/admin-dashboard" replace />
    ) : (
      <Navigate to="/student-dashboard" replace />
    );
  };

  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route
          path="/login"
          element={
            user ? (
              getRootRedirect()
            ) : (
              <LoginPage onLoginSuccess={handleLoginSuccess} />
            )
          }
        />
        <Route
          path="/register"
          element={
            user ? (
              getRootRedirect()
            ) : (
              <RegisterPage />
            )
          }
        />

        {/* Root Redirect Router */}
        <Route path="/" element={getRootRedirect()} />

        {/* Student Portal Routes */}
        <Route element={<PortalLayout user={user} onLogout={handleLogout} />}>
          <Route
            path="/student-dashboard"
            element={
              user && user.role === "STUDENT" ? (
                <StudentDashboardPage user={user} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/catalog"
            element={
              user && user.role === "STUDENT" ? (
                <CatalogPage />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/submit-request"
            element={
              user && user.role === "STUDENT" ? (
                <RequestSubmissionPage user={user} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/my-requests"
            element={
              user && user.role === "STUDENT" ? (
                <MyRequestsPage user={user} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />

          {/* Admin Portal Routes */}
          <Route
            path="/admin-dashboard"
            element={
              user && user.role === "ADMIN" ? (
                <AdminDashboardPage user={user} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/inventory"
            element={
              user && user.role === "ADMIN" ? (
                <InventoryPage />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/request-approval"
            element={
              user && user.role === "ADMIN" ? (
                <RequestApprovalPage />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
        </Route>

        {/* Fallback Catch-All */}
        <Route path="*" element={getRootRedirect()} />
      </Routes>
    </BrowserRouter>
  );
}
