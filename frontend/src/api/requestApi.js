// Dummy Request API managing stationery submission and approval lists.
// Uses localStorage for interactive UI flow.

const INITIAL_REQUESTS = [
  {
    id: "REQ-001",
    studentName: "Harry Potter",
    itemName: "Classic Blue Ballpoint Pens",
    quantity: 2,
    status: "PENDING",
    date: "2026-06-14"
  },
  {
    id: "REQ-002",
    studentName: "Hermione Granger",
    itemName: "A4 Grid Notebooks",
    quantity: 5,
    status: "APPROVED",
    date: "2026-06-15"
  },
  {
    id: "REQ-003",
    studentName: "Ron Weasley",
    itemName: "Stainless Steel Stapler",
    quantity: 1,
    status: "REJECTED",
    date: "2026-06-15"
  },
  {
    id: "REQ-004",
    studentName: "Harry Potter",
    itemName: "Fluorescent Highlighter Set",
    quantity: 1,
    status: "FULFILLED",
    date: "2026-06-12"
  }
];

const getStoredRequests = () => {
  const data = localStorage.getItem("requests");
  if (!data) {
    localStorage.setItem("requests", JSON.stringify(INITIAL_REQUESTS));
    return INITIAL_REQUESTS;
  }
  return JSON.parse(data);
};

export const requestApi = {
  getRequests: async () => {
    await new Promise((resolve) => setTimeout(resolve, 200));
    return getStoredRequests();
  },

  getStudentRequests: async (studentName) => {
    await new Promise((resolve) => setTimeout(resolve, 200));
    const requests = getStoredRequests();
    return requests.filter(
      (r) => r.studentName.toLowerCase() === studentName.toLowerCase()
    );
  },

  createRequest: async (studentName, items) => {
    await new Promise((resolve) => setTimeout(resolve, 200));
    const requests = getStoredRequests();
    
    const newRequests = items.map((item, index) => ({
      id: `REQ-${Math.floor(100 + Math.random() * 900)}`,
      studentName,
      itemName: item.name,
      quantity: parseInt(item.quantity) || 1,
      status: "PENDING",
      date: new Date().toISOString().split("T")[0]
    }));

    const updated = [...newRequests, ...requests];
    localStorage.setItem("requests", JSON.stringify(updated));
    return { success: true, requests: newRequests };
  },

  approveRequest: async (id) => {
    await new Promise((resolve) => setTimeout(resolve, 200));
    const requests = getStoredRequests();
    const idx = requests.findIndex((r) => r.id === id);
    if (idx !== -1) {
      requests[idx].status = "APPROVED";
      localStorage.setItem("requests", JSON.stringify(requests));
      return { success: true, request: requests[idx] };
    }
    return { success: false, error: "Request not found" };
  },

  rejectRequest: async (id) => {
    await new Promise((resolve) => setTimeout(resolve, 200));
    const requests = getStoredRequests();
    const idx = requests.findIndex((r) => r.id === id);
    if (idx !== -1) {
      requests[idx].status = "REJECTED";
      localStorage.setItem("requests", JSON.stringify(requests));
      return { success: true, request: requests[idx] };
    }
    return { success: false, error: "Request not found" };
  }
};
