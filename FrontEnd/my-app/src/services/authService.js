import axios from "axios";
import api from "../services/api";

export const login = async ({ email, password }) => {
  const response = await api.post("/auth/login", {
    email,
    password,
  });

  if (!response.data?.token) {
    throw new Error("Token not received from server");
  }

  // 🔐 Store token
  localStorage.setItem("token", response.data.token);

  return response.data;
};

/**
 * SIGNUP
 * POST /auth/register
 */

export const signup = async (data) => {
  const data1 = {
    email: data.email,
    password: data.password,
  }
  const response = await axios.post("/auth/register", data1);
  if (response.data?.token) {
    localStorage.setItem("token", response.data.token);
  }
  return response.data;
};