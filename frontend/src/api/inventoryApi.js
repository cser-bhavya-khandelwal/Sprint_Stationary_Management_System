// Dummy Inventory API managing static/session inventory data.
// Operations are stored in localStorage to provide a fully reactive mock UI experience.

const INITIAL_INVENTORY = [
  {
    id: "inv-1",
    name: "Classic Blue Ballpoint Pens",
    category: "Writing Instruments",
    unit: "Box (50 pcs)",
    availableQuantity: 120,
    minimumQuantity: 20
  },
  {
    id: "inv-2",
    name: "A4 Grid Notebooks",
    category: "Paper Products",
    unit: "Pack (10 pcs)",
    availableQuantity: 45,
    minimumQuantity: 15
  },
  {
    id: "inv-3",
    name: "Stainless Steel Stapler",
    category: "Office Supplies",
    unit: "Piece",
    availableQuantity: 14,
    minimumQuantity: 5
  },
  {
    id: "inv-4",
    name: "Fluorescent Highlighter Set",
    category: "Writing Instruments",
    unit: "Set (6 colors)",
    availableQuantity: 38,
    minimumQuantity: 10
  },
  {
    id: "inv-5",
    name: "Self-Stick Notes 3x3",
    category: "Paper Products",
    unit: "Pack (12 pads)",
    availableQuantity: 75,
    minimumQuantity: 15
  },
  {
    id: "inv-6",
    name: "Heavy Duty Hole Puncher",
    category: "Office Supplies",
    unit: "Piece",
    availableQuantity: 8,
    minimumQuantity: 3
  }
];

const getStoredInventory = () => {
  const data = localStorage.getItem("inventory");
  if (!data) {
    localStorage.setItem("inventory", JSON.stringify(INITIAL_INVENTORY));
    return INITIAL_INVENTORY;
  }
  return JSON.parse(data);
};

export const inventoryApi = {
  getItems: async () => {
    await new Promise((resolve) => setTimeout(resolve, 150));
    return getStoredInventory();
  },

  addItem: async (item) => {
    await new Promise((resolve) => setTimeout(resolve, 150));
    const items = getStoredInventory();
    const newItem = {
      id: `inv-${Date.now()}`,
      name: item.name,
      category: item.category,
      unit: item.unit,
      availableQuantity: parseInt(item.availableQuantity) || 0,
      minimumQuantity: parseInt(item.minimumQuantity) || 0
    };
    items.push(newItem);
    localStorage.setItem("inventory", JSON.stringify(items));
    return { success: true, item: newItem };
  },

  updateItem: async (id, updatedFields) => {
    await new Promise((resolve) => setTimeout(resolve, 150));
    const items = getStoredInventory();
    const idx = items.findIndex((item) => item.id === id);
    if (idx !== -1) {
      items[idx] = {
        ...items[idx],
        ...updatedFields,
        availableQuantity: parseInt(updatedFields.availableQuantity) ?? items[idx].availableQuantity,
        minimumQuantity: parseInt(updatedFields.minimumQuantity) ?? items[idx].minimumQuantity
      };
      localStorage.setItem("inventory", JSON.stringify(items));
      return { success: true, item: items[idx] };
    }
    return { success: false, error: "Item not found" };
  }
};
