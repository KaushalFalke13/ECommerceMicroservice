import { createContext, useContext, useState } from "react";

const BagContext = createContext();

export const BagProvider = ({ children }) => {
  const [bagItems, setBagItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);

  const addToBag = (product) => {
    setBagItems((prev) => {
      const existing = prev.find((item) => item.id === product.id);

      if (existing) {
        return prev.map((item) =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      }

      return [...prev, { ...product, quantity: 1 }];
    });
  };

  const removeFromBag = (productId) => {
    setBagItems((prev) =>
      prev.filter((item) => item.id !== productId)
    );
    setSelectedItems((prev) =>
      prev.filter((id) => id !== productId)
    );
  };

  const addToSeletedItem = (productId) => {
    setSelectedItems((prev) =>
      prev.includes(productId) ? prev : [...prev, productId]
    );
  };

  const removeFromSeletedItem = (productId) => {
    setSelectedItems((prev) =>
      prev.filter((id) => id !== productId)
    );
  };

  const toggleSelectedItem = (productId) => {
    setSelectedItems((prev) =>
      prev.includes(productId)
        ? prev.filter((id) => id !== productId)
        : [...prev, productId]
    );
  };

  return (
    <BagContext.Provider
      value={{
        bagItems,
        selectedItems,
        addToBag,
        removeFromBag,
        addToSeletedItem,
        removeFromSeletedItem,
        toggleSelectedItem,
      }}
    >
      {children}
    </BagContext.Provider>
  );
};

export const useBag = () => {
  const context = useContext(BagContext);
  if (!context) {
    throw new Error("useBag must be used inside BagProvider");
  }
  return context;
};
