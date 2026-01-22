import { createContext, useContext, useState ,useEffect} from "react";
import {addProductToBag , removeProductFromBag ,getProductsFromBag} from "../services/BagService.js";
import { getAddressofUser ,addNewAddress , deleteAddress} from "../services/Address.js";

const BagContext = createContext();

export const BagProvider = ({ children }) => {
  const [bagItems, setBagItems] = useState([]);
  const [addresses, setAddresses] = useState([]);
  const [selectedPayment, setSelectedPayment] = useState(null);
 
  const [selectedAddress, setSelectedAddress] = useState(() => {
   const stored = localStorage.getItem("selectedAddress");
  return stored ? JSON.parse(stored) :null;}); 
  
  const [selectedItems, setSelectedItems] = useState(() => {
   const stored = localStorage.getItem("selectedItems");
  return stored ? JSON.parse(stored) : [];});
  
  const paymentMethods = [
    { id: 'card', name: 'Credit / Debit Card', description: 'Visa, Mastercard, Rupay and more' },
    { id: 'upi', name: 'UPI', description: 'Google Pay, PhonePe, Paytm' },
    { id: 'netbanking', name: 'Net Banking', description: 'All major banks supported' },
    { id: 'cod', name: 'Cash on Delivery', description: 'Pay when you receive' }
  ];

  useEffect(() => {
    localStorage.setItem(
      "selectedItems",
      JSON.stringify(selectedItems)
    );
  }, [selectedItems]);

  useEffect(() => {
    localStorage.setItem(
      "selectedAddress",
      JSON.stringify(selectedAddress)
    );
  }, [selectedAddress]);

  useEffect(() => {
    fetchBagItems();
    fetchAddresses();
  }, []);


  const fetchAddresses = async () => {
    try {
      const address = await getAddressofUser();
      setAddresses(address); 
    } catch (error) {
      console.error("Failed to fetch Address:", error);
    }
  };

  const addNewAddresses = async(formData) => {
    await addNewAddress(formData);
    await fetchAddresses(); 
  }

  const removeAddresses = async(addressId) => {
    await deleteAddress(addressId);
    await fetchAddresses(); 
  }


  const fetchBagItems = async () => {
    try {
      const products = await getProductsFromBag();
      setBagItems(products);  
    } catch (error) {
      console.error("Failed to fetch bag items:", error);
    }
  };

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
        selectedAddress,
        addresses,
        setSelectedPayment,
        addToBag,
        removeFromBag,
        addNewAddresses,
        addToSeletedItem,
        removeFromSeletedItem,
        toggleSelectedItem,
        fetchBagItems,
        setSelectedAddress,
        setAddresses,
        removeAddresses
      }}>
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

