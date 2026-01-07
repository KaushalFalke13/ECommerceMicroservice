import React, { useState, useEffect } from 'react';
import { ShoppingBag, MapPin, CreditCard, Check, Trash2, Heart } from 'lucide-react';

// Mock Data
const mockOrderData = {
  items: [
    {
      id: 1,
      image: 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400',
      name: 'Classic Slim Fit Casual Shirt',
      brand: 'H&M',
      size: 'M',
      quantity: 1,
      mrp: 2499,
      discount: 40,
      price: 1499
    },
    {
      id: 2,
      image: 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400',
      name: 'Premium Cotton T-Shirt',
      brand: 'Zara',
      size: 'L',
      quantity: 2,
      mrp: 1299,
      discount: 30,
      price: 909
    },
    {
      id: 3,
      image: 'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=400',
      name: 'Slim Fit Denim Jeans',
      brand: 'Levis',
      size: '32',
      quantity: 1,
      mrp: 3999,
      discount: 50,
      price: 1999
    }
  ],
  addresses: [
    {
      id: 1,
      name: 'John Doe',
      phone: '+91 9876543210',
      address: '123, MG Road, Sector 14',
      city: 'Bangalore',
      state: 'Karnataka',
      pincode: '560001',
      type: 'Home',
      isDefault: true
    },
    {
      id: 2,
      name: 'John Doe',
      phone: '+91 9876543210',
      address: '456, Park Street, Block B',
      city: 'Mumbai',
      state: 'Maharashtra',
      pincode: '400001',
      type: 'Work',
      isDefault: false
    }
  ]
};

// OrderItemCard Component
const OrderItemCard = ({ item, onRemove, onMoveToWishlist }) => {
  return (
    <div className="flex gap-4 p-4 bg-white rounded-lg border border-gray-200 hover:shadow-md transition-shadow">
      <div className="flex-shrink-0">
        <img 
          src={item.image} 
          alt={item.name}
          className="w-24 h-32 object-cover rounded-md"
        />
      </div>
      
      <div className="flex-1">
        <div className="flex justify-between">
          <div>
            <p className="text-sm text-gray-500 font-medium mb-1">{item.brand}</p>
            <h3 className="text-base font-medium text-gray-800 mb-2">{item.name}</h3>
            <div className="flex gap-4 text-sm text-gray-600 mb-3">
              <span>Size: <strong>{item.size}</strong></span>
              <span>Qty: <strong>{item.quantity}</strong></span>
            </div>
            <div className="flex items-center gap-3">
              <span className="text-lg font-bold text-gray-900">₹{item.price}</span>
              <span className="text-sm text-gray-400 line-through">₹{item.mrp}</span>
              <span className="text-sm text-pink-600 font-medium">{item.discount}% OFF</span>
            </div>
          </div>
        </div>
        
        <div className="flex gap-4 mt-4">
          <button 
            onClick={() => onRemove(item.id)}
            className="flex items-center gap-1 text-sm text-gray-600 hover:text-red-500 transition-colors"
          >
            <Trash2 size={16} />
            Remove
          </button>
          <button 
            onClick={() => onMoveToWishlist(item.id)}
            className="flex items-center gap-1 text-sm text-gray-600 hover:text-pink-500 transition-colors"
          >
            <Heart size={16} />
            Move to Wishlist
          </button>
        </div>
      </div>
    </div>
  );
};

// AddressCard Component
const AddressCard = ({ address, isSelected, onSelect }) => {
  return (
    <div 
      onClick={() => onSelect(address.id)}
      className={`p-4 rounded-lg border-2 cursor-pointer transition-all ${
        isSelected 
          ? 'border-pink-500 bg-pink-50' 
          : 'border-gray-200 hover:border-gray-300'
      }`}
    >
      <div className="flex items-start justify-between mb-2">
        <div className="flex items-center gap-2">
          <span className="px-2 py-1 text-xs font-semibold text-gray-600 bg-gray-100 rounded">
            {address.type}
          </span>
          {address.isDefault && (
            <span className="px-2 py-1 text-xs font-semibold text-green-600 bg-green-50 rounded">
              Default
            </span>
          )}
        </div>
        {isSelected && (
          <div className="w-5 h-5 bg-pink-500 rounded-full flex items-center justify-center">
            <Check size={14} className="text-white" />
          </div>
        )}
      </div>
      
      <h4 className="font-semibold text-gray-900 mb-1">{address.name}</h4>
      <p className="text-sm text-gray-600 mb-1">{address.phone}</p>
      <p className="text-sm text-gray-700">
        {address.address}, {address.city}, {address.state} - {address.pincode}
      </p>
    </div>
  );
};

// PriceSummary Component
const PriceSummary = ({ items, convenienceFee = 99, deliveryCharge = 0 }) => {
  const totalMRP = items.reduce((sum, item) => sum + (item.mrp * item.quantity), 0);
  const totalPrice = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  const discount = totalMRP - totalPrice;
  const finalAmount = totalPrice + convenienceFee + deliveryCharge;

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-5 sticky top-4">
      <h3 className="text-lg font-bold text-gray-800 mb-4 pb-3 border-b border-gray-200">
        PRICE DETAILS
      </h3>
      
      <div className="space-y-3 mb-4">
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Total MRP</span>
          <span className="text-gray-900">₹{totalMRP.toLocaleString()}</span>
        </div>
        
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Discount on MRP</span>
          <span className="text-green-600 font-medium">-₹{discount.toLocaleString()}</span>
        </div>
        
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Convenience Fee</span>
          <span className="text-gray-900">₹{convenienceFee}</span>
        </div>
        
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Delivery Charges</span>
          <span className={deliveryCharge === 0 ? "text-green-600 font-medium" : "text-gray-900"}>
            {deliveryCharge === 0 ? 'FREE' : `₹${deliveryCharge}`}
          </span>
        </div>
      </div>
      
      <div className="pt-3 border-t-2 border-gray-300">
        <div className="flex justify-between items-center">
          <span className="text-base font-bold text-gray-900">Total Amount</span>
          <span className="text-xl font-bold text-gray-900">₹{finalAmount.toLocaleString()}</span>
        </div>
      </div>
      
      <div className="mt-4 p-3 bg-green-50 rounded-lg">
        <p className="text-sm text-green-700 font-medium">
          🎉 You saved ₹{discount.toLocaleString()} on this order!
        </p>
      </div>
    </div>
  );
};

// PaymentOptions Component
const PaymentOptions = ({ selectedPayment, onSelect }) => {
  const paymentMethods = [
    { id: 'card', name: 'Credit / Debit Card', description: 'Visa, Mastercard, Rupay and more' },
    { id: 'upi', name: 'UPI', description: 'Google Pay, PhonePe, Paytm' },
    { id: 'netbanking', name: 'Net Banking', description: 'All major banks supported' },
    { id: 'cod', name: 'Cash on Delivery', description: 'Pay when you receive' }
  ];

  return (
    <div className="space-y-3">
      {paymentMethods.map(method => (
        <div
          key={method.id}
          onClick={() => onSelect(method.id)}
          className={`p-4 rounded-lg border-2 cursor-pointer transition-all ${
            selectedPayment === method.id
              ? 'border-pink-500 bg-pink-50'
              : 'border-gray-200 hover:border-gray-300'
          }`}
        >
          <div className="flex items-center justify-between">
            <div className="flex-1">
              <h4 className="font-semibold text-gray-900 mb-1">{method.name}</h4>
              <p className="text-sm text-gray-600">{method.description}</p>
            </div>
            <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center ${
              selectedPayment === method.id
                ? 'border-pink-500 bg-pink-500'
                : 'border-gray-300'
            }`}>
              {selectedPayment === method.id && <Check size={14} className="text-white" />}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

// Main OrderPage Component
const OrderPage = () => {
  const [cartItems, setCartItems] = useState(mockOrderData.items);
  const [addresses] = useState(mockOrderData.addresses);
  const [selectedAddress, setSelectedAddress] = useState(
    addresses.find(addr => addr.isDefault)?.id || addresses[0]?.id
  );
  const [selectedPayment, setSelectedPayment] = useState('');
  const [currentStep, setCurrentStep] = useState('payment');

  // Remove item from cart
  const handleRemoveItem = (itemId) => {
    setCartItems(cartItems.filter(item => item.id !== itemId));
  };

  // Move item to wishlist
  const handleMoveToWishlist = (itemId) => {
    console.log('Moving item to wishlist:', itemId);
    handleRemoveItem(itemId);
  };

  // Place order
  const handlePlaceOrder = () => {
    if (!selectedPayment) {
      alert('Please select a payment method');
      return;
    }
    
    alert('Order placed successfully! 🎉');
    console.log('Order Details:', {
      items: cartItems,
      address: addresses.find(addr => addr.id === selectedAddress),
      payment: selectedPayment
    });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <ShoppingBag className="text-pink-500" size={28} />
              <h1 className="text-2xl font-bold text-gray-900">ShopHub</h1>
            </div>
            
            {/* Step Indicator */}
            <div className="hidden md:flex items-center gap-2">
              <div className="flex items-center">
                <div className="flex items-center justify-center w-8 h-8 rounded-full bg-green-500 text-white">
                  <Check size={16} />
                </div>
                <span className="ml-2 text-sm font-medium text-gray-700">BAG</span>
              </div>
              
              <div className="w-12 h-0.5 bg-green-500 mx-2"></div>
              
              <div className="flex items-center">
                <div className="flex items-center justify-center w-8 h-8 rounded-full bg-green-500 text-white">
                  <Check size={16} />
                </div>
                <span className="ml-2 text-sm font-medium text-gray-700">ADDRESS</span>
              </div>
              
              <div className="w-12 h-0.5 bg-pink-500 mx-2"></div>
              
              <div className="flex items-center">
                <div className="flex items-center justify-center w-8 h-8 rounded-full bg-pink-500 text-white font-semibold">
                  3
                </div>
                <span className="ml-2 text-sm font-medium text-pink-500">PAYMENT</span>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-sm text-gray-600">100% SECURE</span>
              <div className="w-5 h-5 bg-green-500 rounded-full flex items-center justify-center">
                <Check size={14} className="text-white" />
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Column - Order Details */}
          <div className="lg:col-span-2 space-y-6">
            {/* Delivery Address Section */}
            <section className="bg-white rounded-lg shadow-sm p-5">
              <div className="flex items-center gap-2 mb-4">
                <MapPin className="text-pink-500" size={20} />
                <h2 className="text-lg font-bold text-gray-800">DELIVERY ADDRESS</h2>
              </div>
              
              <div className="space-y-3">
                {addresses.map(address => (
                  <AddressCard
                    key={address.id}
                    address={address}
                    isSelected={selectedAddress === address.id}
                    onSelect={setSelectedAddress}
                  />
                ))}
              </div>
              
              <button className="mt-4 w-full py-2 border-2 border-pink-500 text-pink-500 font-semibold rounded-lg hover:bg-pink-50 transition-colors">
                + Add New Address
              </button>
            </section>

            {/* Order Items Section */}
            <section className="bg-white rounded-lg shadow-sm p-5">
              <div className="flex items-center gap-2 mb-4">
                <ShoppingBag className="text-pink-500" size={20} />
                <h2 className="text-lg font-bold text-gray-800">
                  ORDER ITEMS ({cartItems.length})
                </h2>
              </div>
              
              <div className="space-y-4">
                {cartItems.map(item => (
                  <OrderItemCard
                    key={item.id}
                    item={item}
                    onRemove={handleRemoveItem}
                    onMoveToWishlist={handleMoveToWishlist}
                  />
                ))}
              </div>
            </section>

            {/* Payment Options Section */}
            <section className="bg-white rounded-lg shadow-sm p-5">
              <div className="flex items-center gap-2 mb-4">
                <CreditCard className="text-pink-500" size={20} />
                <h2 className="text-lg font-bold text-gray-800">PAYMENT OPTIONS</h2>
              </div>
              
              <PaymentOptions
                selectedPayment={selectedPayment}
                onSelect={setSelectedPayment}
              />
            </section>
          </div>

          <div className=" lg:col-span-1">
              <div className="sticky top-4">
            <PriceSummary items={cartItems} />
            
            <button
              onClick={handlePlaceOrder}
              disabled={!selectedPayment || cartItems.length === 0}
              className={`w-full mt-4 py-4 rounded-lg font-bold text-white text-lg transition-all ${
                selectedPayment && cartItems.length > 0
                  ? 'bg-pink-500 hover:bg-pink-600 shadow-lg hover:shadow-xl'
                  : 'bg-gray-300 cursor-not-allowed'
              }`}
            >
              PLACE ORDER
            </button>
          </div>
        </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-gray-200 mt-12 py-6">
        <div className="max-w-7xl mx-auto px-4 text-center text-sm text-gray-600">
          <p>© 2024 ShopHub. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default OrderPage;