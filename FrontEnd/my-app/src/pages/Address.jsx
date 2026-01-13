import  { useState } from 'react';

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

const Address = () => {

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
  



  return (
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
  );
};

export default Address;
