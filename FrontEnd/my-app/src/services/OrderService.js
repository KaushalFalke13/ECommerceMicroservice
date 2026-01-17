import api from "./api";
 

export const createNewOrder = async () => {
  const response = await api.post("/orders/place", {});
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

export const getAddress = async () => {
  const response = await api.post("/orders/cancel", {});
  return response.data; 
}
