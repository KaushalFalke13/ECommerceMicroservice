import api from "./api";


export const addNewAddress = async (formData) => {
  console.log(formData)
  const response = await api.post("/orders/addNewAddress", formData);
  return response.data;
};


export const deleteAddress = async (addressId) => {
  const response = await api.post("/orders/removeAddress", {addressId});
  return response.data;
};

export const getAddressofUser = async () => {
  const response = await api.get("/orders/address");
  return response.data.data;
};