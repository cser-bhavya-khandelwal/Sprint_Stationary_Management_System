 // Dummy Auth API simulating authorization responses
// State is persisted in localStorage for standard UI state management without a backend

const DEFAULT_USERS = [
  {
    id: "usr-1",
    fullName: "Professor Minerva",
    email: "admin@university.edu",
    role: "ADMIN"
  },
  {
    id: "usr-2",
    fullName: "Harry Potter",
    email: "student@university.edu",
    role: "STUDENT"
  }
];

export const authApi = {
  login: async (email, password) => {
    // Artificial delay to mimic API latency
    await new Promise((resolve) => setTimeout(resolve, 300));

    const user = DEFAULT_USERS.find(
      (u) => u.email.toLowerCase() === email.toLowerCase()
    );

    if (user) {
      localStorage.setItem("user", JSON.stringify(user));
      return { success: true, user };
    }

    // Default fallback - dynamic mock login if not matching default ones
    const isStudent = email.includes("student");
    const mockUser = {
      id: `usr-${Math.random().toString(36).substr(2, 9)}`,
      fullName: isStudent ? "Mock Student" : "Mock Admin",
      email,
      role: isStudent ? "STUDENT" : "ADMIN"
    };
    
    localStorage.setItem("user", JSON.stringify(mockUser));
    return { success: true, user: mockUser };
  },

  register: async (fullName, email, password, confirmPassword) => {
    await new Promise((resolve) => setTimeout(resolve, 300));
    
    if (!fullName || !email || !password || !confirmPassword) {
      return { success: false, error: "All fields are required." };
    }
    
    if (password !== confirmPassword) {
      return { success: false, error: "Passwords do not match." };
    }

    // Successfully register user and create dynamic student account
    const mockUser = {
      id: `usr-${Math.random().toString(36).substr(2, 9)}`,
      fullName,
      email,
      role: "STUDENT" // Registers as student by default
    };

    return { success: true, user: mockUser };
  },

  getCurrentUser: () => {
    const userJson = localStorage.getItem("user");
    return userJson ? JSON.parse(userJson) : null;
  },

  logout: () => {
    localStorage.removeItem("user");
    return { success: true };
  }
};
