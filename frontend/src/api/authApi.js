const BASE_URL = "http://localhost:8090/api/auth";

export const authApi = {
  login: async (email, password) => {
    try {
      const response = await fetch(`${BASE_URL}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok && data.success && data.token) {
        // Store JWT token
        localStorage.setItem("token", data.token);
        
        // Store user details for frontend compatibility
        const user = {
          fullName: data.name,
          email: data.email,
          role: data.role
        };
        localStorage.setItem("user", JSON.stringify(user));
        return { success: true, user };
      } else {
        return { success: false, error: data.message || "Invalid email or password" };
      }
    } catch (err) {
      return { success: false, error: "Unable to connect to the authentication service." };
    }
  },

  register: async (fullName, email, password, confirmPassword, role) => {
    if (!fullName || !email || !password || !confirmPassword || !role) {
      return { success: false, error: "All fields are required." };
    }
    
    if (password !== confirmPassword) {
      return { success: false, error: "Passwords do not match." };
    }

    try {
      const response = await fetch(`${BASE_URL}/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: fullName,
          email,
          password,
          role: role
        }),
      });

      const data = await response.json();

      if (response.ok && data.success) {
        return { success: true, message: data.message };
      } else {
        return { success: false, error: data.message || "Registration failed." };
      }
    } catch (err) {
      return { success: false, error: "Unable to connect to the authentication service." };
    }
  },

  getCurrentUser: () => {
    const userJson = localStorage.getItem("user");
    return userJson ? JSON.parse(userJson) : null;
  },

  logout: () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    return { success: true };
  }
};
