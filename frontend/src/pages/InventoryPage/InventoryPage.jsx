import React, { useState, useEffect } from "react";
import { inventoryApi } from "../../api/inventoryApi";
import styles from "./InventoryPage.module.css";
import { Package, Plus, Edit3, Save, X, Search, AlertCircle, Eye, Info } from "lucide-react";

export default function InventoryPage() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  
  // Form States
  const [showForm, setShowForm] = useState(false);
  const [editItemId, setEditItemId] = useState(null);
  
  const [name, setName] = useState("");
  const [category, setCategory] = useState("Writing Instruments");
  const [unit, setUnit] = useState("");
  const [availableQuantity, setAvailableQuantity] = useState("");
  const [minimumQuantity, setMinimumQuantity] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const categoriesList = [
    "Writing Instruments",
    "Paper Products",
    "Office Supplies",
    "Markers & Drawing",
    "Adhesives & Tapes",
    "Organizers & Storage"
  ];

  const loadInventory = async () => {
    setLoading(true);
    try {
      const data = await inventoryApi.getItems();
      setItems(data);
    } catch (err) {
      console.error("Failed to load inventory.", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadInventory();
  }, []);

  const handleEdit = (item) => {
    setError("");
    setSuccess("");
    setEditItemId(item.id);
    setName(item.name);
    setCategory(item.category);
    setUnit(item.unit);
    setAvailableQuantity(item.availableQuantity);
    setMinimumQuantity(item.minimumQuantity);
    setShowForm(true);
    // Scroll to form or focus
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleCancel = () => {
    setEditItemId(null);
    setName("");
    setCategory("Writing Instruments");
    setUnit("");
    setAvailableQuantity("");
    setMinimumQuantity("");
    setShowForm(false);
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!name || !category || !unit || availableQuantity === "" || minimumQuantity === "") {
      setError("Please fill in all fields.");
      return;
    }

    const payload = {
      name,
      category,
      unit,
      availableQuantity: parseInt(availableQuantity),
      minimumQuantity: parseInt(minimumQuantity)
    };

    try {
      if (editItemId) {
        // Update item
        const res = await inventoryApi.updateItem(editItemId, payload);
        if (res.success) {
          setSuccess(`Successfully updated "${name}".`);
          loadInventory();
          handleCancel();
        } else {
          setError(res.error || "Failed to update item.");
        }
      } else {
        // Add new item
        const res = await inventoryApi.addItem(payload);
        if (res.success) {
          setSuccess(`Successfully added "${name}" to inventory.`);
          loadInventory();
          handleCancel();
        } else {
          setError("Failed to add item.");
        }
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    }
  };

  const filteredItems = items.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    item.category.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className={styles.inventoryContainer}>
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <Package className={styles.headerIcon} size={24} />
          <div>
            <h1>Manage Inventory</h1>
            <p>Monitor available stock levels, add new supplies, and update thresholds.</p>
          </div>
        </div>
        
        {!showForm && (
          <button className={styles.addBtn} onClick={() => { handleCancel(); setShowForm(true); }}>
            <Plus size={18} />
            <span>Add Stationery Item</span>
          </button>
        )}
      </div>

      {showForm && (
        <div className={styles.formCard}>
          <div className={styles.formHeader}>
            <h2>{editItemId ? "Update Stationery Item" : "Add New Stationery Item"}</h2>
            <button className={styles.closeBtn} onClick={handleCancel} title="Cancel">
              <X size={18} />
            </button>
          </div>

          {error && (
            <div className={styles.errorAlert}>
              <AlertCircle size={18} />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className={styles.form}>
            <div className={styles.formGrid}>
              <div className={styles.inputGroup}>
                <label htmlFor="itemName">Item Name</label>
                <input
                  id="itemName"
                  type="text"
                  placeholder="e.g. Premium Gel Pens Black"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="category">Category</label>
                <select
                  id="category"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                >
                  {categoriesList.map((cat, idx) => (
                    <option key={idx} value={cat}>
                      {cat}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="unit">Unit Measure</label>
                <input
                  id="unit"
                  type="text"
                  placeholder="e.g. Box (12 pcs), Piece, Pack"
                  value={unit}
                  onChange={(e) => setUnit(e.target.value)}
                  required
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="availableQuantity">Available Quantity</label>
                <input
                  id="availableQuantity"
                  type="number"
                  min="0"
                  placeholder="0"
                  value={availableQuantity}
                  onChange={(e) => setAvailableQuantity(e.target.value)}
                  required
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="minimumQuantity">Minimum Threshold (Alert)</label>
                <input
                  id="minimumQuantity"
                  type="number"
                  min="0"
                  placeholder="5"
                  value={minimumQuantity}
                  onChange={(e) => setMinimumQuantity(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className={styles.formActions}>
              <button type="button" className={styles.cancelBtn} onClick={handleCancel}>
                Cancel
              </button>
              <button type="submit" className={styles.saveBtn}>
                <Save size={16} />
                <span>{editItemId ? "Save Changes" : "Save Item"}</span>
              </button>
            </div>
          </form>
        </div>
      )}

      {success && (
        <div className={styles.successAlert}>
          <Eye size={18} />
          <span>{success}</span>
        </div>
      )}

      <div className={styles.searchBar}>
        <Search className={styles.searchIcon} size={18} />
        <input
          type="text"
          placeholder="Filter inventory by name or category..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className={styles.loaderContainer}>
          <div className={styles.spinner}></div>
          <p>Loading inventory logbook...</p>
        </div>
      ) : filteredItems.length === 0 ? (
        <div className={styles.emptyState}>
          <AlertCircle size={48} />
          <h3>No Inventory Items Found</h3>
          <p>Try searching for a different keyword or create a new stationery item.</p>
        </div>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.inventoryTable}>
            <thead>
              <tr>
                <th>Item Details</th>
                <th>Category</th>
                <th>Unit</th>
                <th className={styles.textRight}>Available Stock</th>
                <th className={styles.textRight}>Min Threshold</th>
                <th className={styles.textCenter}>Status</th>
                <th className={styles.textRight}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredItems.map((item) => {
                const isLowStock = item.availableQuantity <= item.minimumQuantity;
                return (
                  <tr key={item.id}>
                    <td>
                      <div className={styles.itemNameWrapper}>
                        <span className={styles.itemName}>{item.name}</span>
                        <span className={styles.itemId}>ID: {item.id}</span>
                      </div>
                    </td>
                    <td>
                      <span className={styles.categoryBadge}>{item.category}</span>
                    </td>
                    <td>{item.unit}</td>
                    <td className={`${styles.textRight} ${styles.boldText}`}>
                      <span className={isLowStock ? styles.lowStockText : styles.inStockText}>
                        {item.availableQuantity}
                      </span>
                    </td>
                    <td className={`${styles.textRight} ${styles.mutedText}`}>{item.minimumQuantity}</td>
                    <td className={styles.textCenter}>
                      {isLowStock ? (
                        <span className={`${styles.statusLabel} ${styles.statusLow}`}>
                          <AlertTriangle size={12} /> Low Stock
                        </span>
                      ) : (
                        <span className={`${styles.statusLabel} ${styles.statusOk}`}>
                          Good Stock
                        </span>
                      )}
                    </td>
                    <td className={styles.textRight}>
                      <button
                        type="button"
                        className={styles.editRowBtn}
                        onClick={() => handleEdit(item)}
                        title="Edit Item details"
                      >
                        <Edit3 size={16} />
                        <span>Edit</span>
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
