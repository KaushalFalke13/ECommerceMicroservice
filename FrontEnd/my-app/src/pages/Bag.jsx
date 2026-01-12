import React, { useState } from 'react';
import { ShoppingBag, MapPin, CreditCard, Check, Trash2, Heart, Plus, X, ChevronLeft, ChevronDown, Gift, Tag, IterationCcw } from 'lucide-react';
import { useBag } from '../context/BagContext';
import { useWishlist } from '../context/WishlistContext';


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
const OrderItemCard = ({ item, onRemove, onMoveToWishlist, onQuantityChange}) => {
  const discountPercent = Math.round((item.discount / item.mrp) * 100);
    const { selectedItems, toggleSelectedItem } = useBag();

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
            checked={selectedItems.includes(item.id)}
            onChange={() => toggleSelectedItem(item.id)}
            className="w-5 h-5 text-pink-500 cursor-pointer mr-3 mt-2"
          />
        </div>
        <div className="flex-shrink-0">
          <img 
            src={item.images1} 
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


const Bag = () => {

  const [selectedPayment, setSelectedPayment] = useState('');
  const [currentStep, setCurrentStep] = useState('bag');
  const [showOffers, setShowOffers] = useState(false);
  const [couponCode, setCouponCode] = useState('');
  const [donation, setDonation] = useState(0);
  const { addToWishlist } = useWishlist();
  
  const {
  bagItems = [],
  selectedItems = [],
  removeFromBag,
  toggleSelectedItem,
  addToSeletedItem,
  removeFromSeletedItem,
} = useBag();

  

  const handleSelectItem = (item) => {
    toggleSelectedItem(item);
  }
  // Toggle all items
  const handleSelectAll = () => {
    if (selectedItems.length === bagItems.length) {
        selectedItems.forEach(element => {
            removeFromSeletedItem(element);
        });
    } else {
        bagItems.forEach(element => {
        addToSeletedItem(element.id);
      });
    }
  };

  // Update quantity
  // const handleQuantityChange = (itemId, newQuantity) => {
  //   setCartItems(cartItems.map(item => 
  //     item.id === itemId ? {...item, quantity: newQuantity} : item
  //   ));
  // }; 

  // const selectedCartItems = cartItems.filter(item => selectedItems.includes(item.id));
  // const totalMRP = selectedCartItems.reduce((sum, item) => sum + (item.mrp * item.quantity), 0);
  // const totalDiscount = selectedCartItems.reduce((sum, item) => sum + (item.discount * item.quantity), 0);
  // const totalPrice = selectedCartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  // const platformFee = 0; // FREE
  // const finalAmount = totalPrice + platformFee + donation;

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
        bagItems.forEach(item => {
          if (selectedItems.includes(item.id))  removeFromBag(item.id);
        }); 
      }
  };    

  // Move selected items to wishlist
  const handleMoveToWishlist = () => {
    if (selectedItems.length === 0) {
      alert('Please select items to move');
      return;
    }
    if (window.confirm(`Move ${selectedItems.length} item(s) to wishlist?`)) {
      bagItems.forEach(item => {
          if (selectedItems.includes(item.id))  {
            removeFromBag(item.id);
            addToWishlist(item);
          }
        }); 
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
              checked={selectedItems.length === bagItems.length && bagItems.length!=0}
              onChange={handleSelectAll}
              className="w-5 h-5 text-pink-500 cursor-pointer"
            />
            <span className="text-sm font-bold text-gray-900">
              {selectedItems.length}/{bagItems.length} ITEMS SELECTED
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
          {bagItems.map(item => (
            <OrderItemCard
              key={item.id}
              item={item}
              isSelected={selectedItems.includes(item.id)}
              onChange={() => handleSelectItem(item.id)}
              // onQuantityChange={handleQuantityChange}
            />
          ))}
        </div>
      </div>
    </>
  );
};

export default Bag;
