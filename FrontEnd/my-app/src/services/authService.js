import axios from "axios";

const api = axios.create({
  baseURL: "/auth", // Vite proxy → http://localhost:8081
  headers: {
    "Content-Type": "application/json",
  },
});

export const login = async (data) => {
  const data1 = {
    email: data.email,
    password: data.password,
  }
  // const response = await api.post("/login", data1);
  const response = await axios.post("/auth/login", data1);
  if (response.data?.token) {
    localStorage.setItem("token", response.data.token);
  }

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