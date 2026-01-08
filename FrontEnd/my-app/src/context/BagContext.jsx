import { createContext, useContext, useState } from "react";

const BagContext = createContext();

export const BagProvider = ({ children }) => {
  const [bagItems, setBagItems] = useState([]);

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
  };

  const decreaseQuantity = (productId) => {
    setBagItems((prev) =>
      prev
        .map((item) =>
          item.id === productId
            ? { ...item, quantity: item.quantity - 1 }
            : item
        )
        .filter((item) => item.quantity > 0)
    );
  };

  const isInBag = (productId) => {
    return bagItems.some((item) => item.id === productId);
  };

  return (
    <BagContext.Provider
      value={{
        bagItems,
        addToBag,
        removeFromBag,
        decreaseQuantity,
        isInBag,
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
