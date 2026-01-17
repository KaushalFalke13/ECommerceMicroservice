import api from "./api";

export const createNewOrder = async () => {
  const response = await api.post("/products/bag/add", {});
  return response.data; 
};
