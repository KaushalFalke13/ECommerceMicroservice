import api from "./api";


export const createNewOrder = async (selectedItems, selectedAddress ,totalDiscount,finalAmount,totalPrice) => {

  const payload = {
    selectedItems,
    totalPrice,
    finalAmount,
    totalDiscount,
    addressId: selectedAddress.id
  };
  console.log(payload);
  const response = await api.post("/orders/place", payload);
  return response.data;
};

export const getOrderStatus = async (orderId) => {
  const response = await api.post("/orders/status", {orderId});
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

