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
