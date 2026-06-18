const BASE_URL = "http://localhost:8090/api/inventory";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token ? { "Authorization": `Bearer ${token}` } : {};
};

const mapToFrontend = (item) => ({
  id: item.id,
  name: item.name,
  category: item.category,
  unit: item.description || "Piece",
  availableQuantity: item.quantity,
  minimumQuantity: item.minQuantityThreshold
});

export const inventoryApi = {
  getItems: async () => {
    try {
      const response = await fetch(BASE_URL, {
        headers: getAuthHeaders()
      });
      if (!response.ok) throw new Error("Failed to fetch inventory items");
      const items = await response.json();
      return items.map(mapToFrontend);
    } catch (error) {
      console.error(error);
      return [];
    }
  },

  addItem: async (item) => {
    try {
      const payload = {
        itemCode: `ITEM-${Math.floor(1000 + Math.random() * 9000)}`,
        name: item.name,
        description: item.unit || "Piece",
        quantity: parseInt(item.availableQuantity) || 0,
        minQuantityThreshold: parseInt(item.minimumQuantity) || 0,
        price: 1.0,
        category: item.category
      };

      const response = await fetch(BASE_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeaders()
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) throw new Error("Failed to add inventory item");
      const savedItem = await response.json();
      return { success: true, item: mapToFrontend(savedItem) };
    } catch (error) {
      return { success: false, error: error.message };
    }
  },

  updateItem: async (id, updatedFields) => {
    try {
      const payload = {
        itemCode: updatedFields.itemCode || `ITEM-${Math.floor(1000 + Math.random() * 9000)}`,
        name: updatedFields.name,
        description: updatedFields.unit || "Piece",
        quantity: parseInt(updatedFields.availableQuantity) || 0,
        minQuantityThreshold: parseInt(updatedFields.minimumQuantity) || 0,
        price: parseFloat(updatedFields.price) || 1.0,
        category: updatedFields.category
      };

      const response = await fetch(`${BASE_URL}/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeaders()
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) throw new Error("Failed to update inventory item");
      const updated = await response.json();
      return { success: true, item: mapToFrontend(updated) };
    } catch (error) {
      return { success: false, error: error.message };
    }
  },

  searchInventory: async (query) => {
    try {
      const response = await fetch(`${BASE_URL}/search?query=${encodeURIComponent(query)}`, {
        headers: getAuthHeaders()
      });
      if (!response.ok) throw new Error("Search failed");
      const items = await response.json();
      return items.map(mapToFrontend);
    } catch (error) {
      console.error(error);
      return [];
    }
  },

  getLowStockItems: async () => {
    try {
      const response = await fetch(`${BASE_URL}/low-stock`, {
        headers: getAuthHeaders()
      });
      if (!response.ok) throw new Error("Failed to fetch low stock items");
      const items = await response.json();
      return items.map(mapToFrontend);
    } catch (error) {
      console.error(error);
      return [];
    }
  }
};
