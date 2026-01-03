import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home";
import Orders from "./pages/Orders.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
         <Route path="/" element={<Navigate to="/home" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/orders" element={ <Orders />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
