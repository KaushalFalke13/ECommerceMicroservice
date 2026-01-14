import { useState } from 'react';
import {  Check, X, ChevronDown, Tag } from 'lucide-react';
import { useBag } from '../context/BagContext';
import { useWishlist } from '../context/WishlistContext';


const OrderItemCard = ({ item, onQuantityChange}) => {
  
  const discountPercent = Math.round((item.discount / item.mrp) * 100);
  const { selectedItems, toggleSelectedItem , removeFromBag } = useBag();

  const handleRemoveItem = () => {
    if (window.confirm('Remove this item from bag?')) {
      removeFromBag(item.id);
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

  const [showOffers, setShowOffers] = useState(false);
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

  const handleQuantityChange = (itemId, newQuantity) => {
    setCartItems(bagItems.map(item => 
      item.id === itemId ? {...item, quantity: newQuantity} : item
    ));
  }; 


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
              onQuantityChange={handleQuantityChange}
            />
          ))}
        </div>
      </div>
    </>
  );
};

export default Bag;
