import api from "./api";


export const addProductToBag = async (productId) => {
  const response = await api.post("/products/bag/add", {productId});
  console.log(response.data);
  return response.data;
};

