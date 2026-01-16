import api from "./api";


export const addProductToWatchlist = async (productId) => {
  const response = await api.post("/products/watchlist/add", {productId});
  return response.data;
};

export const removeProductFromWatchlist = async (productId) => {
  const response = await api.post("/products/watchlist/remove", {productId});
  return response.data;
};

export const getProductsFromWatchlist = async () => {
  const response = await api.get("/products/watchlist/watchlistItems");
  return response.data;
};