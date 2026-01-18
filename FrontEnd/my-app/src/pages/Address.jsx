import  { useState } from 'react';
import { X } from 'lucide-react';
import { useBag  } from '../context/BagContext';

const AddAddressModal = ({ onClose, onSave }) => {
  const [formData, setFormData] = useState({
    name: '',
    number: '',
    street: '',
    city: '',
    state: '',
    pincode: '',
    type: 'Home'
  });

  const handleSubmit = () => {
    if (!formData.name || !formData.number || !formData.street || !formData.city || !formData.state || !formData.pincode) {
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
              value={formData.number}
              onChange={(e) => setFormData({...formData, number: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded focus:outline-none focus:border-pink-500"
            />
          </div>
          
          <div>
            <input
              type="text"
              placeholder="Address (House No, Building, Street)*"
              value={formData.street}
              onChange={(e) => setFormData({...formData, street: e.target.value})}
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
          <p className="text-sm text-gray-700 mb-1">{address.number}</p>
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
    const { addresses, setAddresses  } = useBag();
  const { addNewAddresses } = useBag();

    const [selectedAddress, setSelectedAddress] = useState(
      // addresses.find(addr => addr.isDefault)?.id || addresses[0]?.id
    );
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

            {showAddressModal && (
              <AddAddressModal
                onClose={() => setShowAddressModal(false)}
                onSave={() => {
                  setShowAddressModal(false);
                  addNewAddresses()  }
                }
              />
            )}
              </div> 
    );
};

export default Address;
