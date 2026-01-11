import { useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { Check, Gift, Tag } from "lucide-react";
import { useBag } from "../context/BagContext";



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







const CheckoutLayout = () => {
  const location = useLocation();
  const navigate = useNavigate();


  // ✅ LOCAL STATE (safe for layout)
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
  
  

  // ✅ Dummy values for now (to avoid crashes)
  const selectedItemsCount = 1;
  const totalMRP = 0;
  const totalDiscount = 0;
  const finalAmount = 0;

  const goToStep = (step) => {
    const order = ["bag", "address", "payment"];
    if (order.indexOf(step) <= order.indexOf(currentStep)) {
      navigate(`/${step}`);
    }
  };

  const handleApplyCoupon = () => {
    if (!couponCode.trim()) {
      alert("Please enter a coupon code");
      return;
    }
    alert(`Coupon "${couponCode}" applied`);
  };

  const handlePrimaryAction = () => {
    if (currentStep === "bag") navigate("/address");
    else if (currentStep === "address") navigate("/payment");
    else alert("Order placed successfully 🎉");
  };

  return (

    
    <div className="min-h-screen bg-white">

      {/* HEADER */}
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
      {/* MAIN CONTENT */}
      <main className="max-w-7xl mx-auto px-4 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

          {/* LEFT SIDE */}
          <div className="lg:col-span-2 space-y-4">
            <Outlet />
          </div>

          {/* RIGHT SIDE */}
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
                        //  onClick={}
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
    </div>
  );
};

export default CheckoutLayout;