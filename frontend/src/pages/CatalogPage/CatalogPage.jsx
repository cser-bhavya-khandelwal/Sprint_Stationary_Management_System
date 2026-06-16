import React, { useState, useEffect } from "react";
import { inventoryApi } from "../../api/inventoryApi";
import styles from "./CatalogPage.module.css";
import { Search, Filter, BookOpen, AlertCircle, RefreshCw } from "lucide-react";

export default function CatalogPage() {
  const [items, setItems] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [loading, setLoading] = useState(true);

  const loadCatalog = async () => {
    setLoading(true);
    try {
      const data = await inventoryApi.getItems();
      setItems(data);
    } catch (err) {
      console.error("Failed to load catalog data", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCatalog();
  }, []);

  const categories = ["All", ...new Set(items.map((item) => item.category))];

  const filteredItems = items.filter((item) => {
    const matchesSearch =
      item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.category.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesCategory =
      selectedCategory === "All" || item.category === selectedCategory;

    return matchesSearch && matchesCategory;
  });

  return (
    <div className={styles.catalogContainer}>
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <BookOpen className={styles.headerIcon} size={24} />
          <div>
            <h1>Stationery Catalog</h1>
            <p>View currently available office and classroom stationery stock.</p>
          </div>
        </div>
        <button className={styles.refreshBtn} onClick={loadCatalog} disabled={loading} title="Reload catalog">
          <RefreshCw size={18} className={loading ? styles.spinning : ""} />
          <span>Refresh</span>
        </button>
      </div>

      <div className={styles.filterBar}>
        <div className={styles.searchBox}>
          <Search className={styles.searchIcon} size={18} />
          <input
            type="text"
            placeholder="Search by item name or category..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>

        <div className={styles.filterBox}>
          <Filter className={styles.filterIcon} size={18} />
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
          >
            {categories.map((cat, idx) => (
              <option key={idx} value={cat}>
                {cat}
              </option>
            ))}
          </select>
        </div>
      </div>

      {loading ? (
        <div className={styles.loaderContainer}>
          <div className={styles.spinner}></div>
          <p>Loading stationery stock details...</p>
        </div>
      ) : filteredItems.length === 0 ? (
        <div className={styles.emptyState}>
          <AlertCircle size={48} />
          <h3>No Stationery Items Found</h3>
          <p>Try adjusting your search terms or filter category settings.</p>
        </div>
      ) : (
        <div className={styles.tableWrapper}>
          <table className={styles.catalogTable}>
            <thead>
              <tr>
                <th>Item Name</th>
                <th>Category</th>
                <th>Unit Type</th>
                <th className={styles.textRight}>Available Stock</th>
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
                        {isLowStock && (
                          <span className={styles.lowStockBadge}>Low Stock</span>
                        )}
                      </div>
                    </td>
                    <td>
                      <span className={styles.categoryBadge}>{item.category}</span>
                    </td>
                    <td>{item.unit}</td>
                    <td className={`${styles.textRight} ${styles.stockCell}`}>
                      <span
                        className={`${styles.stockValue} ${
                          isLowStock ? styles.lowStockText : styles.inStockText
                        }`}
                      >
                        {item.availableQuantity}
                      </span>
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
