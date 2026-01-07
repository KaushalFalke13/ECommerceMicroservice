import React, { useState } from 'react';
import { ShoppingBag, Trash2, Heart, Plus, Minus, ChevronRight } from 'lucide-react';

const mockCartData = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=500&fit=crop',
    brand: 'H&M',
    title: 'Relaxed Fit Hoodie',
    price: 1299,
    mrp: 2499,
    discount: 48,
    size: 'M',
    quantity: 1,
    availableSizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1542272454315-7ad9f1620a3d?w=400&h=500&fit=crop',
    brand: 'Levis',
    title: '511 Slim Fit Jeans',
    price: 2799,
    mrp: 4299,
    discount: 35,
    size: '32',
    quantity: 1,
    availableSizes: ['30', '32', '34', '36']
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?w=400&h=500&fit=crop',
    brand: 'Nike',
    title: 'Air Max Sneakers',
    price: 7995,
    mrp: 10995,
    discount: 27,
    size: '9',
    quantity: 1,
    availableSizes: ['8', '9', '10', '11']
  }
];

export default function BagPage() {
  const [cartItems, setCartItems] = useState(mockCartData);

  const updateQuantity = (id, delta) => {
    setCartItems(cartItems.map(item => {
      if (item.id === id) {
        const newQuantity = Math.max(1, Math.min(10, item.quantity + delta));
        return { ...item, quantity: newQuantity };
      }
      return item;
    }));
  };

  const updateSize = (id, newSize) => {
    setCartItems(cartItems.map(item => 
      item.id === id ? { ...item, size: newSize } : item
    ));
  };

  const removeItem = (id) => {
    setCartItems(cartItems.filter(item => item.id !== id));
  };

  const moveToWishlist = (id) => {
    alert('Item moved to wishlist!');
    removeItem(id);
  };

  const calculateTotals = () => {
    const totalMRP = cartItems.reduce((sum, item) => sum + (item.mrp * item.quantity), 0);
    const totalPrice = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const discount = totalMRP - totalPrice;
    const convenienceFee = 99;
    const deliveryCharges = totalPrice >= 1999 ? 0 : 99;
    const totalAmount = totalPrice + convenienceFee + deliveryCharges;

    return { totalMRP, discount, convenienceFee, deliveryCharges, totalAmount };
  };

  const totals = calculateTotals();

  const handlePlaceOrder = () => {
    alert(`Order placed! Total: ₹${totals.totalAmount}`);
  };

  if (cartItems.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header itemCount={0} />
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="bg-white rounded-lg shadow-sm p-12 text-center">
            <ShoppingBag className="w-24 h-24 text-gray-300 mx-auto mb-6" />
            <h3 className="text-2xl font-semibold text-gray-900 mb-2">
              Your Bag is Empty
            </h3>
            <p className="text-gray-600 mb-8">
              Add items to your bag to get started with shopping
            </p>
            <button
              onClick={() => alert('Redirecting to shop...')}
              className="bg-pink-500 hover:bg-pink-600 text-white font-semibold px-8 py-3 rounded-lg transition-colors"
            >
              Shop Now
            </button>
          </div>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-24 lg:pb-8">
      <Header itemCount={cartItems.length} />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="grid lg:grid-cols-3 gap-6">
          {/* Cart Items */}
          <div className="lg:col-span-2 space-y-4">
            {cartItems.map((item) => (
              <CartItem
                key={item.id}
                item={item}
                onUpdateQuantity={updateQuantity}
                onUpdateSize={updateSize}
                onRemove={removeItem}
                onMoveToWishlist={moveToWishlist}
              />
            ))}
          </div>

          {/* Price Summary - Desktop */}
          <div className="hidden lg:block">
            <div className="sticky top-24">
              <PriceSummary totals={totals} onPlaceOrder={handlePlaceOrder} />
            </div>
          </div>
        </div>
      </main>

      {/* Fixed Bottom Button - Mobile */}
      <div className="lg:hidden fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg p-4 z-20">
        <div className="flex items-center justify-between mb-2">
          <span className="text-sm text-gray-600">Total Amount</span>
          <span className="text-xl font-bold text-gray-900">₹{totals.totalAmount}</span>
        </div>
        <button
          onClick={handlePlaceOrder}
          className="w-full bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 rounded-lg transition-colors"
        >
          PLACE ORDER
        </button>
      </div>
    </div>
  );
}

function Header({ itemCount }) {
  return (
    <header className="bg-white shadow-sm sticky top-0 z-10">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-3">
            <ShoppingBag className="w-8 h-8 text-pink-500" />
            <h1 className="text-2xl font-bold text-gray-900">FashionHub</h1>
          </div>
        </div>

        {/* Step Indicator */}
        <div className="flex items-center space-x-2 text-sm">
          <div className="flex items-center">
            <span className="font-bold text-pink-500">BAG</span>
            <span className="text-pink-500 ml-1">({itemCount})</span>
          </div>
          <ChevronRight className="w-4 h-4 text-gray-400" />
          <span className="text-gray-400">ADDRESS</span>
          <ChevronRight className="w-4 h-4 text-gray-400" />
          <span className="text-gray-400">PAYMENT</span>
        </div>
      </div>
    </header>
  );
}

function CartItem({ item, onUpdateQuantity, onUpdateSize, onRemove, onMoveToWishlist }) {
  return (
    <div className="bg-white rounded-lg shadow-sm p-4 flex gap-4">
      {/* Product Image */}
      <div className="flex-shrink-0 w-24 h-32 sm:w-28 sm:h-36 rounded overflow-hidden bg-gray-100">
        <img
          src={item.image}
          alt={item.title}
          className="w-full h-full object-cover"
        />
      </div>

      {/* Product Details */}
      <div className="flex-1 min-w-0">
        <div className="flex justify-between gap-2">
          <div>
            <h3 className="font-bold text-gray-900 text-sm uppercase tracking-wide">
              {item.brand}
            </h3>
            <p className="text-gray-600 text-sm mt-1 line-clamp-2">
              {item.title}
            </p>
          </div>
          <button
            onClick={() => onRemove(item.id)}
            className="text-gray-400 hover:text-red-500 transition-colors flex-shrink-0"
            aria-label="Remove item"
          >
            <Trash2 className="w-5 h-5" />
          </button>
        </div>

        {/* Size and Quantity Selectors */}
        <div className="flex items-center gap-4 mt-3">
          <div>
            <label className="text-xs text-gray-500 block mb-1">Size</label>
            <select
              value={item.size}
              onChange={(e) => onUpdateSize(item.id, e.target.value)}
              className="border border-gray-300 rounded px-2 py-1 text-sm font-semibold focus:outline-none focus:border-pink-500"
            >
              {item.availableSizes.map(size => (
                <option key={size} value={size}>{size}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="text-xs text-gray-500 block mb-1">Qty</label>
            <div className="flex items-center border border-gray-300 rounded">
              <button
                onClick={() => onUpdateQuantity(item.id, -1)}
                className="px-2 py-1 hover:bg-gray-100 transition-colors"
                disabled={item.quantity <= 1}
              >
                <Minus className="w-4 h-4" />
              </button>
              <span className="px-3 py-1 font-semibold text-sm min-w-[2rem] text-center">
                {item.quantity}
              </span>
              <button
                onClick={() => onUpdateQuantity(item.id, 1)}
                className="px-2 py-1 hover:bg-gray-100 transition-colors"
                disabled={item.quantity >= 10}
              >
                <Plus className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        {/* Price Section */}
        <div className="flex items-center gap-2 mt-3">
          <span className="text-lg font-bold text-gray-900">
            ₹{item.price * item.quantity}
          </span>
          <span className="text-sm text-gray-500 line-through">
            ₹{item.mrp * item.quantity}
          </span>
          <span className="text-xs font-semibold text-pink-500">
            {item.discount}% OFF
          </span>
        </div>

        {/* Move to Wishlist */}
        <button
          onClick={() => onMoveToWishlist(item.id)}
          className="flex items-center gap-1 text-sm text-gray-600 hover:text-pink-500 mt-2 transition-colors"
        >
          <Heart className="w-4 h-4" />
          <span>Move to Wishlist</span>
        </button>
      </div>
    </div>
  );
}

function PriceSummary({ totals, onPlaceOrder }) {
  return (
    <div className="bg-white rounded-lg shadow-sm p-6">
      <h3 className="text-lg font-bold text-gray-900 mb-4 uppercase tracking-wide">
        Price Details
      </h3>

      <div className="space-y-3 border-b border-gray-200 pb-4 mb-4">
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Total MRP</span>
          <span className="font-semibold">₹{totals.totalMRP}</span>
        </div>
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Discount on MRP</span>
          <span className="font-semibold text-green-600">-₹{totals.discount}</span>
        </div>
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Convenience Fee</span>
          <span className="font-semibold">₹{totals.convenienceFee}</span>
        </div>
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Delivery Charges</span>
          {totals.deliveryCharges === 0 ? (
            <span className="font-semibold text-green-600">FREE</span>
          ) : (
            <span className="font-semibold">₹{totals.deliveryCharges}</span>
          )}
        </div>
      </div>

      <div className="flex justify-between items-center mb-6">
        <span className="font-bold text-gray-900">Total Amount</span>
        <span className="text-2xl font-bold text-gray-900">₹{totals.totalAmount}</span>
      </div>

      <button
        onClick={onPlaceOrder}
        className="w-full bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 rounded-lg transition-colors uppercase tracking-wide"
      >
        Place Order
      </button>

      {totals.deliveryCharges > 0 && (
        <p className="text-xs text-gray-500 mt-4 text-center">
          Add ₹{1999 - (totals.totalMRP - totals.discount)} more for FREE delivery
        </p>
      )}
    </div>
  );
}