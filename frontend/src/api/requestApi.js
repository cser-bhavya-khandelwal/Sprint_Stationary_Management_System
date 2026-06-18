const BASE_URL = "http://localhost:8090/api/requests";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token ? { "Authorization": `Bearer ${token}` } : {};
};

const mapToFrontend = (backendRequests) => {
  if (!backendRequests) return [];
  const list = Array.isArray(backendRequests) ? backendRequests : [backendRequests];
  const flattened = [];
  list.forEach(req => {
    if (req.items && req.items.length > 0) {
      req.items.forEach(item => {
        flattened.push({
          id: req.id,
          keyId: `${req.id}-${item.inventoryItemId}`,
          studentName: req.studentName || req.studentEmail || "Student",
          itemName: item.itemName,
          quantity: item.quantity,
          status: req.status,
          date: req.createdAt ? req.createdAt.split("T")[0] : new Date().toISOString().split("T")[0]
        });
      });
    } else {
      flattened.push({
        id: req.id,
        keyId: `${req.id}-none`,
        studentName: req.studentName || req.studentEmail || "Student",
        itemName: "No Items",
        quantity: 0,
        status: req.status,
        date: req.createdAt ? req.createdAt.split("T")[0] : new Date().toISOString().split("T")[0]
      });
    }
  });
  return flattened;
};

export const requestApi = {
  getRequests: async () => {
    try {
      const response = await fetch(BASE_URL, {
        headers: getAuthHeaders()
      });
      if (!response.ok) throw new Error("Failed to fetch requests");
      const data = await response.json();
      return mapToFrontend(data);
    } catch (error) {
      console.error(error);
      return [];
    }
  },

  getStudentRequests: async (studentName) => {
    try {
      const response = await fetch(`${BASE_URL}/my`, {
        headers: getAuthHeaders()
      });
      if (!response.ok) throw new Error("Failed to fetch student requests");
      const data = await response.json();
      return mapToFrontend(data);
    } catch (error) {
      console.error(error);
      return [];
    }
  },

  createRequest: async (studentName, items) => {
    try {
      const payload = {
        studentName,
        items: items.map(item => ({
          inventoryItemId: parseInt(item.id),
          itemName: item.name,
          quantity: parseInt(item.quantity) || 1
        }))
      };

      const response = await fetch(BASE_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeaders()
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errText = await response.text();
        let errMsg = "Failed to submit request";
        try {
          const errJson = JSON.parse(errText);
          errMsg = errJson.message || errMsg;
        } catch (e) {
          errMsg = errText || errMsg;
        }
        throw new Error(errMsg);
      }
      const savedRequest = await response.json();
      return { success: true, requests: mapToFrontend(savedRequest) };
    } catch (error) {
      console.error(error);
      return { success: false, error: error.message };
    }
  },

  approveRequest: async (id) => {
    try {
      const response = await fetch(`${BASE_URL}/${id}/approve`, {
        method: "PUT",
        headers: getAuthHeaders()
      });
      if (!response.ok) {
        const errText = await response.text();
        let errMsg = "Failed to approve request";
        try {
          const errJson = JSON.parse(errText);
          errMsg = errJson.message || errMsg;
        } catch (e) {
          errMsg = errText || errMsg;
        }
        throw new Error(errMsg);
      }
      const updated = await response.json();
      return { success: true, request: mapToFrontend(updated)[0] };
    } catch (error) {
      console.error(error);
      return { success: false, error: error.message };
    }
  },

  rejectRequest: async (id) => {
    try {
      const response = await fetch(`${BASE_URL}/${id}/reject`, {
        method: "PUT",
        headers: getAuthHeaders()
      });
      if (!response.ok) {
        const errText = await response.text();
        let errMsg = "Failed to reject request";
        try {
          const errJson = JSON.parse(errText);
          errMsg = errJson.message || errMsg;
        } catch (e) {
          errMsg = errText || errMsg;
        }
        throw new Error(errMsg);
      }
      const updated = await response.json();
      return { success: true, request: mapToFrontend(updated)[0] };
    } catch (error) {
      console.error(error);
      return { success: false, error: error.message };
    }
  }
};
