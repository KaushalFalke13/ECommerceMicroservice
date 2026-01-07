import api from "./api";


export const getAllProducts = async (page = 0, size = 8) => {
  const response = await api.get("/products/product", {
    params: {
      page,
      size,
    },
  });
  return response.data;
};

export const getProductById = async (productId) => {
  const response = await api.get(`/products/product/${productId}`);
  return response.data;
};
export const changeWishlistStatus = async (productId, addToWishlist) => {
  const endpoint = addToWishlist
    ? `/wishlist/add/${productId}` 
    : `/wishlist/remove/${productId}`;

  await api.post(endpoint);
};      