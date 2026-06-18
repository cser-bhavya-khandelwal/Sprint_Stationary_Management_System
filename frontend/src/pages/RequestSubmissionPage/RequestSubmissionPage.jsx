import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { inventoryApi } from "../../api/inventoryApi";
import { requestApi } from "../../api/requestApi";
import styles from "./RequestSubmissionPage.module.css";
import { Plus, Trash2, Send, ShoppingCart, AlertCircle, CheckCircle } from "lucide-react";

export default function RequestSubmissionPage({ user }) {
  const navigate = useNavigate();
  const [catalogItems, setCatalogItems] = useState([]);
  const [selectedItemId, setSelectedItemId] = useState("");
  const [quantity, setQuantity] = useState(1);
  
  // Requisition basket (items to request in this ticket)
  const [requestBasket, setRequestBasket] = useState([]);
  
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const loadCatalog = async () => {
      try {
        const data = await inventoryApi.getItems();
        setCatalogItems(data);
        if (data.length > 0) {
         setSelectedItemId(String(data[0].id));
        }
      } catch (err) {
        console.error("Failed to load catalog.", err);
      }
    };
    loadCatalog();
  }, []);

  const handleAddItem = (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!selectedItemId) {
      setError("Please select a stationery item.");
      return;
    }

    const item = catalogItems.find(
  (i) => i.id === parseInt(selectedItemId)
);
    if (!item) {
      setError("Invalid item selected.");
      return;
    }

    if (quantity <= 0) {
      setError("Quantity must be at least 1.");
      return;
    }

    // Check if item already exists in the basket
    const existsIdx = requestBasket.findIndex(
  (bi) => bi.id === parseInt(selectedItemId)
);
    if (existsIdx !== -1) {
      // Update quantity
      const updated = [...requestBasket];
      updated[existsIdx].quantity += parseInt(quantity);
      setRequestBasket(updated);
    } else {
      // Add new row to basket
      setRequestBasket([
        ...requestBasket,
        {
          id: item.id,
          name: item.name,
          category: item.category || "General",
          quantity: parseInt(quantity)
          }
      ]);
    }

    // Reset inputs
    setQuantity(1);
    setSuccess(`Added "${item.name}" to request list.`);
    setTimeout(() => setSuccess(""), 2500);
  };

  const handleRemoveItem = (index) => {
    const updated = [...requestBasket];
    updated.splice(index, 1);
    setRequestBasket(updated);
  };

  const handleSubmitRequest = async () => {
    setError("");
    setSuccess("");

    if (requestBasket.length === 0) {
      setError("Your request list is empty. Add at least one stationery item first.");
      return;
    }

    setSubmitting(true);
    try {
      const studentName = user?.fullName || "Anonymous Student";
      const result = await requestApi.createRequest(studentName, requestBasket);
      if (result.success) {
        setSuccess("Stationery request submitted successfully!");
        setRequestBasket([]);
        setTimeout(() => {
          navigate("/my-requests");
        }, 1500);
      } else {
        setError("Failed to submit request. Please try again.");
      }
    } catch (err) {
      setError("An error occurred during submission.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className={styles.submissionContainer}>
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <ShoppingCart className={styles.headerIcon} size={24} />
          <div>
            <h1>Submit Stationery Request</h1>
            <p>Select stationery items, compile your request basket, and submit for approval.</p>
          </div>
        </div>
      </div>

      <div className={styles.layoutGrid}>
        {/* Left: Add Item Form */}
        <div className={styles.formCard}>
          <h2>Add Item to Request</h2>
          
          {error && (
            <div className={styles.errorAlert}>
              <AlertCircle size={18} />
              <span>{error}</span>
            </div>
          )}

          {success && (
            <div className={styles.successAlert}>
              <CheckCircle size={18} />
              <span>{success}</span>
            </div>
          )}

          <form onSubmit={handleAddItem} className={styles.form}>
            <div className={styles.inputGroup}>
              <label htmlFor="stationeryItem">Select Stationery Item</label>
              <select
                id="stationeryItem"
                value={selectedItemId}
                onChange={(e) => setSelectedItemId(e.target.value)}
              >
                {catalogItems.map((item) => (
                  <option key={item.id} value={item.id}>
                  {item.name} - Stock: {item.quantity}
                </option>
                ))}
              </select>
            </div>

            <div className={styles.inputGroup}>
              <label htmlFor="quantity">Required Quantity</label>
              <input
                id="quantity"
                type="number"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(parseInt(e.target.value) || "")}
                required
              />
            </div>

            <button type="submit" className={styles.addBtn}>
              <Plus size={18} />
              <span>Add Item</span>
            </button>
          </form>
        </div>

        {/* Right: Request Basket Preview */}
        <div className={styles.basketCard}>
          <h2>Request Basket Preview</h2>
          
          {requestBasket.length === 0 ? (
            <div className={styles.emptyBasket}>
              <ShoppingCart size={40} className={styles.basketIcon} />
              <p>No items added to the request basket yet.</p>
              <span>Use the form on the left to add items.</span>
            </div>
          ) : (
            <div className={styles.basketActive}>
              <div className={styles.basketTableWrapper}>
                <table className={styles.basketTable}>
                  <thead>
                    <tr>
                      <th>Item Name</th>
                      <th>Category</th>
                      <th className={styles.textCenter}>Quantity</th>
                      <th className={styles.textRight}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {requestBasket.map((item, index) => (
                      <tr key={index}>
                        <td>
                          <div className={styles.basketItemName}>
                          <span>{item.name}</span>
                        </div>
                        </td>
                        <td>{item.category}</td>
                        <td className={styles.textCenter}>
                          <span className={styles.quantityBadge}>{item.quantity}</span>
                        </td>
                        <td className={styles.textRight}>
                          <button
                            type="button"
                            className={styles.removeBtn}
                            onClick={() => handleRemoveItem(index)}
                            title="Remove item"
                          >
                            <Trash2 size={16} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              <div className={styles.basketFooter}>
                <div className={styles.summaryText}>
                  <span>Total Unique Items: {requestBasket.length}</span>
                </div>
                <button
                  type="button"
                  className={styles.submitBtn}
                  onClick={handleSubmitRequest}
                  disabled={submitting}
                >
                  {submitting ? (
                    <span>Submitting Request...</span>
                  ) : (
                    <>
                      <Send size={16} />
                      <span>Submit Request</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
