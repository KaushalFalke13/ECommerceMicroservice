  import React, { useState } from 'react';
  import { Heart, ShoppingBag, Trash2, ShoppingCart } from 'lucide-react';
  import Navbar from '../components/Navbar';
  import { useWishlist } from "../context/WishlistContext";
  import { useBag } from "../context/BagContext";
  import { useNavigate } from "react-router-dom";


  export default function WishlistPage() {
    const [movedItems, setMovedItems] = useState([]);
    const { wishlistItems,  removeFromWishlist } = useWishlist();
    const { addToBag } = useBag();
    const navigate = useNavigate();



    const handleMoveToBag = (item) => {
        addToBag(item); 
        removeFromWishlist(item.id);
    };
    
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />

        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-gray-900">
              My Wishlist 
              <span className="text-gray-500 font-normal text-lg ml-2">
                ({wishlistItems.length} {wishlistItems.length === 1 ? 'item' : 'items'})
              </span>
            </h2>
          </div>

          {wishlistItems.length === 0 ? (
            // Empty State
            <div className="bg-white rounded-lg shadow-sm p-12 text-center">
              <div className="flex justify-center mb-6">
                <Heart className="w-24 h-24 text-gray-300" />
              </div>
              <h3 className="text-2xl font-semibold text-gray-900 mb-2">
                Your Wishlist is Empty
              </h3>
              <p className="text-gray-600 mb-8 max-w-md mx-auto">
                Save your favorite items here to buy them later or share with friends
              </p>
              <button
                onClick={() => navigate("/home")}
                className="bg-pink-500 hover:bg-pink-600 text-white font-semibold px-8 py-3 rounded-lg transition-colors inline-flex items-center space-x-2"
              >
                <ShoppingCart className="w-5 h-5" />
                <span>Continue Shopping</span>
              </button>
            </div>
          ) : (
            // Wishlist Grid
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {wishlistItems.map((item) => (
                <div
                  key={item.id}
                  className={`bg-white rounded-lg overflow-hidden shadow-sm hover:shadow-lg transition-all duration-300 ${
                    movedItems.includes(item.id) ? 'opacity-50 scale-95' : ''
                  }`}
                >
                  {/* Product Image */}
                  <div className="relative aspect-[4/5] overflow-hidden bg-gray-100">
                    <img
                      src={item.images1}
                      alt={item.title}
                      className="w-full h-full object-cover"
                    />
                    <button
                      onClick={() => removeFromWishlist(item.id)}
                      className="absolute top-3 right-3 bg-white rounded-full p-2 shadow-md hover:bg-gray-100 transition-colors"
                      aria-label="Remove from wishlist"
                    >
                      <Trash2 className="w-5 h-5 text-gray-600" />
                    </button>
                  </div>

                  {/* Product Details */}
                  <div className="p-4">
                    <h3 className="font-bold text-gray-900 text-sm uppercase tracking-wide mb-1">
                      {item.brand}
                    </h3>
                    <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                      {item.title}
                    </p>

                    {/* Price Section */}
                    <div className="flex items-center space-x-2 mb-3">
                      <span className="text-lg font-bold text-gray-900">
                        ₹{item.price}
                      </span>
                      <span className="text-sm text-gray-500 line-through">
                        ₹{item.mrp}
                      </span>
                      <span className="text-xs font-semibold text-pink-500">
                        ({item.discount}% OFF)
                      </span>
                    </div>

                    {/* Size */}
                    {item.size && (
                      <div className="mb-4">
                        <span className="text-xs text-gray-500">Size: </span>
                        <span className="text-sm font-semibold text-gray-900">
                          {item.size}
                        </span>
                      </div>
                    )}

                    {/* Move to Bag Button */}
                    <button
                      onClick={() => handleMoveToBag(item)}
                      className="w-full bg-pink-500 hover:bg-pink-600 text-white font-semibold py-2.5 rounded-lg transition-colors flex items-center justify-center space-x-2"
                    >
                      <ShoppingBag className="w-4 h-4" />
                      <span>Move to Bag</span>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </main>
      </div>
    );
  }