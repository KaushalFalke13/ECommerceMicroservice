import React, { useState } from 'react';
import { Heart, ShoppingBag, Trash2, ShoppingCart } from 'lucide-react';
import Navbar from '../components/Navbar';
import { useWishlist } from "../context/WishlistContext";

const mockWishlistData = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=500&fit=crop',
    brand: 'H&M',
    title: 'Relaxed Fit Hoodie',
    price: 1299,
    mrp: 2499,
    discount: 48,
    size: 'M'
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=400&h=500&fit=crop',
    brand: 'Zara',
    title: 'Faux Leather Jacket',
    price: 3999,
    mrp: 6999,
    discount: 43,
    size: 'L'
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1542272454315-7ad9f1620a3d?w=400&h=500&fit=crop',
    brand: 'Levis',
    title: '511 Slim Fit Jeans',
    price: 2799,
    mrp: 4299,
    discount: 35,
    size: '32'
  },
  {
    id: 4,
    image: 'https://images.unsplash.com/photo-1523381294911-8d3cead13475?w=400&h=500&fit=crop',
    brand: 'Mango',
    title: 'Floral Print Dress',
    price: 2199,
    mrp: 3999,
    discount: 45,
    size: 'S'
  },
  {
    id: 5,
    image: 'https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?w=400&h=500&fit=crop',
    brand: 'Nike',
    title: 'Air Max Sneakers',
    price: 7995,
    mrp: 10995,
    discount: 27,
    size: '9'
  },
  {
    id: 6,
    image: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400&h=500&fit=crop',
    brand: 'Allen Solly',
    title: 'Formal Shirt White',
    price: 1499,
    mrp: 2999,
    discount: 50,
    size: 'M'
  }
];

export default function WishlistPage() {
  const [wishlist, setWishlist] = useState(mockWishlistData);
  const [movedItems, setMovedItems] = useState([]);
  const { wishlistItems, addToWishlist, removeFromWishlist } = useWishlist();
  const handleRemove = (id) => {
    setWishlist(wishlist.filter(item => item.id !== id));
  };

  const handleMoveToBag = (id) => {
    setMovedItems([...movedItems, id]);
    setTimeout(() => {
      setWishlist(wishlist.filter(item => item.id !== id));
      setMovedItems(movedItems.filter(itemId => itemId !== id));
    }, 600);
  };

  const handleContinueShopping = () => {
    alert('Redirecting to shopping page...');
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
              onClick={handleContinueShopping}
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
                    onClick={() => handleRemove(item.id)}
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
                    onClick={() => handleMoveToBag(item.id)}
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