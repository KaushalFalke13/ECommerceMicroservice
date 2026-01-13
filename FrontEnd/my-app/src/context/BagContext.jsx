import { createContext, useContext, useState } from "react";

const BagContext = createContext();

export const BagProvider = ({ children }) => {
  const [bagItems, setBagItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);

  const [selectedPayment, setSelectedPayment] = useState(null);

  const paymentMethods = [
    { id: 'card', name: 'Credit / Debit Card', description: 'Visa, Mastercard, Rupay and more' },
    { id: 'upi', name: 'UPI', description: 'Google Pay, PhonePe, Paytm' },
    { id: 'netbanking', name: 'Net Banking', description: 'All major banks supported' },
    { id: 'cod', name: 'Cash on Delivery', description: 'Pay when you receive' }
  ];

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
        paymentMethods,
        selectedPayment,
        setSelectedPayment,
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
