import api from "./api";


export const addProductToBag = async (productId) => {
  const response = await api.post("/products/bag/add", {productId});
  return response.data;
};

export const removeProductFromBag = async (productId) => {
  const response = await api.post("/products/bag/remove", {productId});
  return response.data;
};

export const getProductsFromBag = async () => {
  const response = await api.get("/products/bag/bagItems", {productId});
  return response.data;
};