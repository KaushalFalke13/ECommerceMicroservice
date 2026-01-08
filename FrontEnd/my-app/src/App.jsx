import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home";
import Orders from "./pages/Orders.jsx";
import Wishlist from "./pages/Wishlist.jsx";
import Bag from "./pages/bag.jsx";

import { WishlistProvider } from "./context/WishlistContext";
import { BagProvider } from "./context/BagContext";
function App() {
  return (
    <BrowserRouter>
      <WishlistProvider>
      <BagProvider>
        <Routes>
          <Route path="/" element={<Navigate to="/home" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/home" element={<Home />} />
          <Route path="/orders" element={<Orders />} />
          <Route path="/wishlist" element={<Wishlist />} />
          <Route path="/bags" element={<Bag />} />
        </Routes>
        </BagProvider>
      </WishlistProvider>
    </BrowserRouter>
  );
}

export default App;
