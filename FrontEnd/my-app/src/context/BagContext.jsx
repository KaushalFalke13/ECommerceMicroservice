import { createContext, useContext, useState ,useEffect} from "react";
import {addProductToBag , removeProductFromBag ,getProductsFromBag} from "../services/BagService.js";

const BagContext = createContext();

export const BagProvider = ({ children }) => {
  const [bagItems, setBagItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const [selectedPayment, setSelectedPayment] = useState(null);
  
const fetchBagItems = async () => {
  try {
    const products = await getProductsFromBag();
    setBagItems(products);  
  } catch (error) {
    console.error("Failed to fetch bag items:", error);
  }
};

  useEffect(() => {
    fetchBagItems();
  }, []);

  const paymentMethods = [
    { id: 'card', name: 'Credit / Debit Card', description: 'Visa, Mastercard, Rupay and more' },
    { id: 'upi', name: 'UPI', description: 'Google Pay, PhonePe, Paytm' },
    { id: 'netbanking', name: 'Net Banking', description: 'All major banks supported' },
    { id: 'cod', name: 'Cash on Delivery', description: 'Pay when you receive' }
  ];

  


  const addToBag = async (product) => {
  try {
    await addProductToBag(product.id);
    fetchBagItems(); 
  } catch (error) {
    console.error("Failed to add item to bag:", error);
  }
};

  const removeFromBag = async(productId) => {
   try {
    await removeProductFromBag(productId);
    fetchBagItems(); 
  } catch (error) {
    console.error("Failed to add item to bag:", error);
  }
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
        fetchBagItems,
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

