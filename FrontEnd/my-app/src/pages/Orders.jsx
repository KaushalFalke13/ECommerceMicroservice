import React, { useState } from 'react';
import { ShoppingBag, MapPin, CreditCard, Check, Trash2, Heart, Plus, X, ChevronLeft, ChevronDown, Gift, Tag } from 'lucide-react';

// Mock Data
const mockOrderData = {
  items: [
    {
      id: 1,
      image: 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400',
      name: 'Brown & Orange Printed Super Soft Velvet Traditional Carpet',
      brand: 'Kuber Industries',
      seller: 'Kuber Mart Industries Private Limited',
      size: 'Onesize',
      quantity: 1,
      mrp: 999,
      discount: 570,
      price: 329,
      returnDays: 7,
      deliveryDate: '14 Jan 2026'
    },
    {
      id: 2,
      image: 'https://images.unsplash.com/photo-1585386959984-a4155224a1ad?w=400',
      name: 'Creme Multi-Purpose Moisturiser Protective Skin Care',
      brand: 'Nivea',
      seller: 'Truecom Retail',
      size: '75-100 ML',
      quantity: 1,
      mrp: 275,
      discount: 97,
      price: 178,
      exchangeOnly: true,
      deliveryDate: '14 Jan 2026'
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

// AddAddressModal Component
const AddAddressModal = ({ onClose, onSave }) => {
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    address: '',
    city: '',
    state: '',
    pincode: '',
    type: 'Home'
  });

  const handleSubmit = () => {
    if (!formData.name || !formData.phone || !formData.address || !formData.city || !formData.state || !formData.pincode) {
      alert('Please fill all required fields');
      return;
    }
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-md w-full max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b border-gray-200 p-4 flex items-center justify-between">
          <h3 className="text-lg font-bold text-gray-900">ADD NEW ADDRESS</h3>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4 space-y-4">
          <div>
            <input
              type="text"
              placeholder="Name*"
              value={formData.name}
              onChange={(e) => setFormData({...formData, name: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div>
            <input
              type="tel"
              placeholder="Phone Number*"
              value={formData.phone}
              onChange={(e) => setFormData({...formData, phone: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div>
            <input
              type="text"
              placeholder="Address (House No, Building, Street)*"
              value={formData.address}
              onChange={(e) => setFormData({...formData, address: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div className="grid grid-cols-2 gap-4">
            <input
              type="text"
              placeholder="City*"
              value={formData.city}
              onChange={(e) => setFormData({...formData, city: e.target.value})}
              className="px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
            <input
              type="text"
              placeholder="Pincode*"
              value={formData.pincode}
              onChange={(e) => setFormData({...formData, pincode: e.target.value})}
              className="px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div>
            <input
              type="text"
              placeholder="State*"
              value={formData.state}
              onChange={(e) => setFormData({...formData, state: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div>
            <p className="text-sm font-semibold text-gray-700 mb-2">Address Type</p>
            <div className="flex gap-3">
              {['Home', 'Work'].map(type => (
                <button
                  key={type}
                  onClick={() => setFormData({...formData, type})}
                  className={`px-6 py-2 rounded-full text-sm font-semibold transition-colors ${
                    formData.type === type
                      ? 'bg-pink-500 text-white'
                      : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                  }`}
                >
                  {type}
                </button>
              ))}
            </div>
          </div>
          
          <button
            onClick={handleSubmit}
            className="w-full py-3 bg-pink-500 hover:bg-pink-600 text-white font-bold rounded transition-colors"
          >
            SAVE ADDRESS
          </button>
        </div>
      </div>
    </div>
  );
};

// OrderItemCard Component
const OrderItemCard = ({ item, onRemove, onMoveToWishlist, onQuantityChange, isSelected, onSelect }) => {
  const discountPercent = Math.round((item.discount / item.mrp) * 100);
  
  const handleRemoveItem = () => {
    if (window.confirm('Remove this item from bag?')) {
      onRemove(item.id);
    }
  };

  const handleMoveItem = () => {
    if (window.confirm('Move this item to wishlist?')) {
      onMoveToWishlist(item.id);
    }
  };
  
  return (
    <div className="bg-white border border-gray-200 rounded">
      <div className="flex gap-4 p-4">
        <div className="flex-shrink-0">
          <input
            type="checkbox"
            checked={isSelected}
            onChange={() => onSelect(item.id)}
            className="w-5 h-5 text-pink-500 cursor-pointer mr-3 mt-2"
          />
        </div>
        <div className="flex-shrink-0">
          <img 
            src={item.image} 
            alt={item.name}
            className="w-28 h-32 object-cover rounded"
          />
        </div>
        
        <div className="flex-1">
          <div className="flex justify-between">
            <div className="flex-1">
              <p className="text-sm font-bold text-gray-900 mb-1">{item.brand}</p>
              <h3 className="text-sm text-gray-600 mb-1 line-clamp-2">{item.name}</h3>
              <p className="text-xs text-gray-500 mb-3">Sold by: {item.seller}</p>
              
              <div className="flex gap-4 mb-3">
                <div>
                  <span className="text-xs text-gray-600">Size: </span>
                  <select className="text-sm font-semibold border-none bg-transparent">
                    <option>{item.size}</option>
                  </select>
                </div>
                <div>
                  <span className="text-xs text-gray-600">Qty: </span>
                  <select 
                    value={item.quantity}
                    onChange={(e) => onQuantityChange(item.id, parseInt(e.target.value))}
                    className="text-sm font-semibold border-none bg-transparent"
                  >
                    {[1,2,3,4,5].map(num => (
                      <option key={num} value={num}>{num}</option>
                    ))}
                  </select>
                </div>
              </div>
              
              <div className="flex items-center gap-2 mb-3">
                <span className="text-base font-bold text-gray-900">₹{item.price}</span>
                <span className="text-sm text-gray-400 line-through">₹{item.mrp}</span>
                <span className="text-sm text-orange-600 font-semibold">{discountPercent}% OFF</span>
              </div>
              
              {item.exchangeOnly && (
                <p className="text-xs font-semibold text-gray-700 mb-2">Exchange Only</p>
              )}
              
              <div className="flex items-center gap-1 text-xs text-gray-600 mb-2">
                <div className="w-4 h-4 rounded-full border-2 border-gray-400 flex items-center justify-center">
                  <div className="w-2 h-2 rounded-full bg-gray-400"></div>
                </div>
                <span>{item.returnDays} days return available</span>
              </div>
              
              <div className="flex items-center gap-1 text-xs text-gray-900">
                <Check size={14} className="text-green-600" />
                <span>Delivery by <strong>{item.deliveryDate}</strong></span>
              </div>
            </div>
            
            <button 
              onClick={handleRemoveItem}
              className="text-gray-400 hover:text-gray-600"
            >
              <X size={20} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// AddressCard Component
const AddressCard = ({ address, isSelected, onSelect, onEdit, onRemove }) => {
  return (
    <div 
      className={`p-4 rounded border-2 transition-all ${
        isSelected 
          ? 'border-pink-500 bg-pink-50' 
          : 'border-gray-200'
      }`}
    >
      <div className="flex items-start gap-3">
        <input
          type="radio"
          checked={isSelected}
          onChange={() => onSelect(address.id)}
          className="w-4 h-4 text-pink-500 cursor-pointer mt-1"
        />
        <div className="flex-1">
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center gap-2">
              <span className="font-bold text-gray-900">{address.name}</span>
              <span className="px-2 py-0.5 text-xs font-bold text-gray-600 bg-gray-200 rounded">
                {address.type}
              </span>
              {address.isDefault && (
                <span className="px-2 py-0.5 text-xs font-bold text-gray-600 bg-gray-200 rounded">
                  DEFAULT
                </span>
              )}
            </div>
            <div className="flex gap-3">
              <button
                onClick={() => alert('Edit address functionality')}
                className="text-sm font-bold text-pink-500 hover:text-pink-600"
              >
                EDIT
              </button>
              <button
                onClick={() => {
                  if (window.confirm('Remove this address?')) {
                    onRemove(address.id);
                  }
                }}
                className="text-sm font-bold text-pink-500 hover:text-pink-600"
              >
                REMOVE
              </button>
            </div>
          </div>
          <p className="text-sm text-gray-700 mb-1">{address.phone}</p>
          <p className="text-sm text-gray-600">
            {address.address}, {address.city}, {address.state} - {address.pincode}
          </p>
        </div>
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
          className={`p-4 rounded border-2 cursor-pointer transition-all ${
            selectedPayment === method.id
              ? 'border-pink-500 bg-pink-50'
              : 'border-gray-200 hover:border-gray-300'
          }`}
        >
          <div className="flex items-center gap-3">
            <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0 ${
              selectedPayment === method.id
                ? 'border-pink-500 bg-pink-500'
                : 'border-gray-300'
            }`}>
              {selectedPayment === method.id && <Check size={14} className="text-white" />}
            </div>
            <div className="flex-1">
              <h4 className="font-bold text-sm text-gray-900 mb-1">{method.name}</h4>
              <p className="text-xs text-gray-600">{method.description}</p>
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
  const [selectedItems, setSelectedItems] = useState(cartItems.map(item => item.id));
  const [addresses, setAddresses] = useState(mockOrderData.addresses);
  const [selectedAddress, setSelectedAddress] = useState(
    addresses.find(addr => addr.isDefault)?.id || addresses[0]?.id
  );
  const [selectedPayment, setSelectedPayment] = useState('');
  const [currentStep, setCurrentStep] = useState('bag');
  const [pinCode, setPinCode] = useState('');
  const [showOffers, setShowOffers] = useState(false);
  const [couponCode, setCouponCode] = useState('');
  const [donation, setDonation] = useState(0);
  const [showAddressModal, setShowAddressModal] = useState(false);

  // Toggle item selection
  const handleSelectItem = (itemId) => {
    setSelectedItems(prev => 
      prev.includes(itemId) 
        ? prev.filter(id => id !== itemId)
        : [...prev, itemId]
    );
  };

  // Toggle all items
  const handleSelectAll = () => {
    if (selectedItems.length === cartItems.length) {
      setSelectedItems([]);
    } else {
      setSelectedItems(cartItems.map(item => item.id));
    }
  };

  // Update quantity
  const handleQuantityChange = (itemId, newQuantity) => {
    setCartItems(cartItems.map(item => 
      item.id === itemId ? {...item, quantity: newQuantity} : item
    ));
  };

  // Calculate totals
  const selectedCartItems = cartItems.filter(item => selectedItems.includes(item.id));
  const totalMRP = selectedCartItems.reduce((sum, item) => sum + (item.mrp * item.quantity), 0);
  const totalDiscount = selectedCartItems.reduce((sum, item) => sum + (item.discount * item.quantity), 0);
  const totalPrice = selectedCartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  const platformFee = 0; // FREE
  const finalAmount = totalPrice + platformFee + donation;

  // Navigate steps
  const handleContinue = () => {
    if (currentStep === 'bag') {
      if (selectedItems.length === 0) {
        alert('Please select at least one item');
        return;
      }
      setCurrentStep('address');
    } else if (currentStep === 'address') {
      if (!selectedAddress) {
        alert('Please select a delivery address');
        return;
      }
      setCurrentStep('payment');
    }
  };

  const handleBack = () => {
    if (currentStep === 'payment') {
      setCurrentStep('address');
    } else if (currentStep === 'address') {
      setCurrentStep('bag');
    }
  };

  const handlePlaceOrder = () => {
    if (!selectedPayment) {
      alert('Please select a payment method');
      return;
    }
    alert('Order placed successfully! 🎉');
  };

  // Add new address
  const handleSaveAddress = (addressData) => {
    const newAddress = {
      ...addressData,
      id: Date.now(),
      isDefault: addresses.length === 0
    };
    setAddresses([...addresses, newAddress]);
    setSelectedAddress(newAddress.id);
    setShowAddressModal(false);
  };

  // Remove address
  const handleRemoveAddress = (addressId) => {
    if (addresses.length === 1) {
      alert('You must have at least one address');
      return;
    }
    setAddresses(addresses.filter(addr => addr.id !== addressId));
    if (selectedAddress === addressId) {
      setSelectedAddress(addresses.find(addr => addr.id !== addressId)?.id);
    }
  };

  // Remove selected items
  const handleRemoveSelected = () => {
    if (selectedItems.length === 0) {
      alert('Please select items to remove');
      return;
    }
    if (window.confirm(`Remove ${selectedItems.length} item(s) from bag?`)) {
      setCartItems(cartItems.filter(item => !selectedItems.includes(item.id)));
      setSelectedItems([]);
    }
  };

  // Move selected items to wishlist
  const handleMoveToWishlist = () => {
    if (selectedItems.length === 0) {
      alert('Please select items to move');
      return;
    }
    if (window.confirm(`Move ${selectedItems.length} item(s) to wishlist?`)) {
      setCartItems(cartItems.filter(item => !selectedItems.includes(item.id)));
      setSelectedItems([]);
      alert('Items moved to wishlist!');
    }
  };

  // Apply coupon
  const handleApplyCoupon = () => {
    if (!couponCode.trim()) {
      alert('Please enter a coupon code');
      return;
    }
    alert(`Coupon "${couponCode}" applied successfully!`);
  };

  // Navigate to specific step (from header)
  const handleStepClick = (step) => {
    // Can only go back or to current step
    const stepOrder = ['bag', 'address', 'payment'];
    const currentIndex = stepOrder.indexOf(currentStep);
    const targetIndex = stepOrder.indexOf(step);
    
    if (targetIndex <= currentIndex) {
      setCurrentStep(step);
    }
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-gradient-to-r from-pink-500 via-purple-500 to-orange-500 rounded flex items-center justify-center">
                <span className="text-white font-bold text-xl">M</span>
              </div>
            </div>
            
            {/* Step Indicator */}
            <div className="flex items-center gap-12">
              <div 
                onClick={() => handleStepClick('bag')}
                className={`flex flex-col items-center cursor-pointer ${currentStep === 'bag' ? 'text-pink-500' : 'text-gray-400'}`}
              >
                <span className="text-xs font-bold mb-1">BAG</span>
                <div className={`w-12 h-1 ${currentStep === 'bag' ? 'bg-pink-500' : 'bg-gray-300'}`}></div>
              </div>
              
              <div className="flex-1 h-px bg-gray-300 w-24"></div>
              
              <div 
                onClick={() => handleStepClick('address')}
                className={`flex flex-col items-center cursor-pointer ${currentStep === 'address' ? 'text-pink-500' : 'text-gray-400'}`}
              >
                <span className="text-xs font-bold mb-1">ADDRESS</span>
                <div className={`w-16 h-1 ${currentStep === 'address' ? 'bg-pink-500' : 'bg-gray-300'}`}></div>
              </div>
              
              <div className="flex-1 h-px bg-gray-300 w-24"></div>
              
              <div 
                onClick={() => handleStepClick('payment')}
                className={`flex flex-col items-center cursor-pointer ${currentStep === 'payment' ? 'text-pink-500' : 'text-gray-400'}`}
              >
                <span className="text-xs font-bold mb-1">PAYMENT</span>
                <div className={`w-16 h-1 ${currentStep === 'payment' ? 'bg-pink-500' : 'bg-gray-300'}`}></div>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-teal-500 rounded-full flex items-center justify-center">
                <Check size={14} className="text-white" />
              </div>
              <span className="text-xs font-bold text-gray-700">100% SECURE</span>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Column */}
          <div className="lg:col-span-2 space-y-4">
            
            {/* Back Button */}
            {currentStep !== 'bag' && (
              <button
                onClick={handleBack}
                className="flex items-center gap-1 text-sm text-gray-600 hover:text-gray-900"
              >
                <ChevronLeft size={18} />
                Back
              </button>
            )}

            {/* BAG STEP */}
            {currentStep === 'bag' && (
              <>
                {/* Delivery Check */}
                <div className="bg-white border border-gray-200 rounded p-4">
                  <div className="flex items-center justify-between">
                    <h3 className="text-sm font-bold text-gray-900">Check delivery time & services</h3>
                    <button 
                      onClick={() => {
                        const pin = prompt('Enter PIN Code:');
                        if (pin) {
                          setPinCode(pin);
                          alert(`Checking delivery for PIN: ${pin}`);
                        }
                      }}
                      className="px-4 py-2 border border-pink-500 text-pink-500 text-xs font-bold rounded hover:bg-pink-50"
                    >
                      ENTER PIN CODE
                    </button>
                  </div>
                </div>

                {/* Available Offers */}
                <div className="bg-white border border-gray-200 rounded p-4">
                  <div className="flex items-start gap-3">
                    <Tag size={20} className="text-gray-700 mt-1" />
                    <div className="flex-1">
                      <h3 className="text-sm font-bold text-gray-900 mb-2">Available Offers</h3>
                      <p className="text-sm text-gray-600 mb-2">7.5% Assured Cashback* on a minimum spend of ₹100. T&C</p>
                      <button 
                        onClick={() => setShowOffers(!showOffers)}
                        className="text-sm text-pink-500 font-bold flex items-center gap-1"
                      >
                        Show More <ChevronDown size={14} />
                      </button>
                    </div>
                  </div>
                </div>

                {/* Items Selection */}
                <div className="bg-white border border-gray-200 rounded">
                  <div className="flex items-center justify-between p-4 border-b border-gray-200">
                    <div className="flex items-center gap-3">
                      <input
                        type="checkbox"
                        checked={selectedItems.length === cartItems.length}
                        onChange={handleSelectAll}
                        className="w-5 h-5 text-pink-500 cursor-pointer"
                      />
                      <span className="text-sm font-bold text-gray-900">
                        {selectedItems.length}/{cartItems.length} ITEMS SELECTED
                      </span>
                    </div>
                    <div className="flex gap-4">
                      <button 
                        onClick={handleRemoveSelected}
                        className="text-sm font-bold text-gray-700 hover:text-gray-900"
                      >
                        REMOVE
                      </button>
                      <div className="w-px h-5 bg-gray-300"></div>
                      <button 
                        onClick={handleMoveToWishlist}
                        className="text-sm font-bold text-gray-700 hover:text-gray-900"
                      >
                        MOVE TO WISHLIST
                      </button>
                    </div>
                  </div>
                  
                  <div className="divide-y divide-gray-200">
                    {cartItems.map(item => (
                      <OrderItemCard
                        key={item.id}
                        item={item}
                        isSelected={selectedItems.includes(item.id)}
                        onSelect={handleSelectItem}
                        onQuantityChange={handleQuantityChange}
                      />
                    ))}
                  </div>
                </div>
              </>
            )}

            {/* ADDRESS STEP */}
            {currentStep === 'address' && (
              <div className="bg-white border border-gray-200 rounded p-5">
                <h2 className="text-sm font-bold text-gray-900 mb-4">Select Delivery Address</h2>
                
                <div className="space-y-3">
                  {addresses.map(address => (
                    <AddressCard
                      key={address.id}
                      address={address}
                      isSelected={selectedAddress === address.id}
                      onSelect={setSelectedAddress}
                      onRemove={handleRemoveAddress}
                    />
                  ))}
                </div>
                
                <button className="mt-4 w-full py-3 border-2 border-pink-500 text-pink-500 font-bold rounded hover:bg-pink-50"
                  onClick={() => setShowAddressModal(true)}>
                  + ADD NEW ADDRESS
                </button>
              </div>
            )}

            {/* PAYMENT STEP */}
            {currentStep === 'payment' && (
              <div className="bg-white border border-gray-200 rounded p-5">
                <h2 className="text-sm font-bold text-gray-900 mb-4">Choose Payment Method</h2>
                <PaymentOptions
                  selectedPayment={selectedPayment}
                  onSelect={setSelectedPayment}
                />
              </div>
            )}
          </div>

          {/* Right Column - Price Summary */}
          <div className="lg:col-span-1">
            <div className="sticky top-24 space-y-4">
              {/* Coupons */}
              <div className="bg-white border border-gray-200 rounded">
                <div className="p-4 border-b border-gray-200">
                  <h3 className="text-xs font-bold text-gray-500 uppercase mb-3">COUPONS</h3>
                  <div className="flex gap-2">
                    <div className="flex-1 flex items-center gap-2 border border-gray-300 rounded px-3 py-2">
                      <Tag size={16} className="text-gray-600" />
                      <input
                        type="text"
                        placeholder="Apply Coupons"
                        value={couponCode}
                        onChange={(e) => setCouponCode(e.target.value)}
                        className="flex-1 text-sm outline-none"
                      />
                    </div>
                    <button className="px-5 py-2 border border-pink-500 text-pink-500 text-xs font-bold rounded hover:bg-pink-50"
                      onClick={handleApplyCoupon}>
                      APPLY
                    </button>
                  </div>
                </div>

                {/* Gift Package */}
                <div className="p-4 border-b border-gray-200">
                  <h3 className="text-xs font-bold text-gray-500 uppercase mb-3">GIFTING & PERSONALISATION</h3>
                  <div className="flex gap-3">
                    <Gift size={40} className="text-pink-500" />
                    <div className="flex-1">
                      <p className="text-sm font-bold text-gray-900 mb-1">Buying for a loved one?</p>
                      <p className="text-xs text-gray-600 mb-2">Gift Packaging and personalised message on card. Only for ₹35</p>
                      <button 
                        onClick={() => alert('Gift package added for ₹35')}
                        className="text-xs text-pink-500 font-bold"
                      >
                        ADD GIFT PACKAGE
                      </button>
                    </div>
                  </div>
                </div>

                {/* Donation */}
                <div className="p-4 border-b border-gray-200">
                  <h3 className="text-xs font-bold text-gray-500 uppercase mb-3">SUPPORT TRANSFORMATIVE SOCIAL WORK IN INDIA</h3>
                  <div className="mb-3">
                    <label className="flex items-center gap-2 text-sm">
                      <input 
                        type="checkbox" 
                        className="w-4 h-4"
                        checked={donation > 0}
                        onChange={(e) => {
                          if (!e.target.checked) {
                            setDonation(0);
                          }
                        }}
                      />
                      <span className="font-semibold text-gray-900">Donate and make a difference</span>
                    </label>
                  </div>
                  <div className="flex gap-2">
                    {[10, 20, 50, 100].map(amount => (
                      <button
                        key={amount}
                        onClick={() => setDonation(amount)}
                        className={`flex-1 py-2 rounded text-sm font-bold transition-colors ${
                          donation === amount
                            ? 'bg-pink-500 text-white'
                            : 'border border-gray-300 text-gray-700 hover:border-gray-400'
                        }`}
                      >
                        ₹{amount}
                      </button>
                    ))}
                  </div>
                  <button className="text-xs text-pink-500 font-semibold mt-2">Know More</button>
                </div>

                {/* Price Details */}
                <div className="p-4">
                  <h3 className="text-xs font-bold text-gray-500 uppercase mb-3">
                    PRICE DETAILS ({selectedItems.length} Items)
                  </h3>
                  
                  <div className="space-y-3">
                    <div className="flex justify-between text-sm">
                      <span className="text-gray-700">Total MRP</span>
                      <span className="text-gray-900">₹{totalMRP.toLocaleString()}</span>
                    </div>
                    
                    <div className="flex justify-between text-sm">
                      <span className="text-gray-700">Discount on MRP</span>
                      <span className="text-green-600 font-semibold">-₹{totalDiscount.toLocaleString()}</span>
                    </div>
                    
                    <div className="flex justify-between text-sm">
                      <span className="text-gray-700">Coupon Discount</span>
                      <button 
                        onClick={() => alert('Enter coupon code in the field above')}
                        className="text-pink-500 font-semibold text-sm"
                      >
                        Apply Coupon
                      </button>
                    </div>
                    
                    <div className="flex justify-between text-sm items-center">
                      <span className="text-gray-700">Platform Fee</span>
                      <div className="flex items-center gap-2">
                        <span className="text-green-600 font-semibold">FREE</span>
                      </div>
                    </div>
                    
                    <div className="pt-3 border-t border-gray-300">
                      <div className="flex justify-between items-center">
                        <span className="text-sm font-bold text-gray-900">Total Amount</span>
                        <span className="text-sm font-bold text-gray-900">₹{finalAmount.toLocaleString()}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              {/* Place Order Button */}
              <button
                onClick={currentStep === 'payment' ? handlePlaceOrder : handleContinue}
                disabled={currentStep === 'bag' && selectedItems.length === 0}
                className={`w-full py-4 rounded font-bold text-sm transition-all ${
                  (currentStep === 'bag' && selectedItems.length === 0) || (currentStep === 'payment' && !selectedPayment)
                    ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    : 'bg-pink-500 text-white hover:bg-pink-600'
                }`}
              >
                {currentStep === 'payment' ? 'PLACE ORDER' : 'PLACE ORDER'}
              </button>
            </div>
          </div>
        </div>
      </main>

      {/* Add Address Modal */}
      {showAddressModal && (
        <AddAddressModal
          onClose={() => setShowAddressModal(false)}
          onSave={handleSaveAddress}
        />
      )}
    </div>
  );
};

export default OrderPage;