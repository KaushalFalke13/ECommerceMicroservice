import api from "./api";


export const createNewOrder = async (selectedItems, selectedAddress) => {
  // Convert items to Map<String, Integer> shape
  const items = {};
  let totalPrice = 0;
  selectedItems.forEach(item => {
    items[item.id] = item.quantity;
    totalPrice += item.price * item.quantity;
  });

  const discountAmount = 0; 
  const totalAmount = totalPrice - discountAmount;

  const payload = {
    items,
    totalPrice,
    totalAmount,
    discountAmount,
    addressId: selectedAddress.id
  };

  const response = await api.post("/orders/place", payload);
  return response.data;
};








export const getOrderStatus = async () => {
  const response = await api.post("/orders/status", {});
  return response.data; 
};

export const changeOrderAddress = async () => {
  const response = await api.post("/orders/updateAddress", {});
  return response.data; 
};

export const cancelOrder = async () => {
  const response = await api.post("/orders/cancel", {});
  return response.data; 
}

