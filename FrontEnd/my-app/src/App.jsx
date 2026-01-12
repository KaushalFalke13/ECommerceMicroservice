import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Orders from "./pages/Orders.jsx";
import Wishlist from "./pages/Wishlist.jsx";

import Bag from "./pages/Bag.jsx";
import Address from "./pages/Address.jsx";
import Payment from "./pages/Payment.jsx";

import CheckoutLayout from "./pages/CheckoutLayout.jsx";

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

           <Route path="/checkout" element={<CheckoutLayout />}>
  <Route index element={<Navigate to="bags" replace />} />
  <Route path="bags" element={<Bag />} />
  <Route path="address" element={<Address />} />
  <Route path="payment" element={<Payment />} />
</Route>


          </Routes>
        </BagProvider>
      </WishlistProvider>
    </BrowserRouter>
  );
}

export default App;
