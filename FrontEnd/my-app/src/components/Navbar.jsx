import { Search, User, Heart, ShoppingBag } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useWishlist } from "../context/WishlistContext";
import { useBag } from "../context/BagContext";

 
 const Navbar = () => {
    const { wishlistItems } = useWishlist();
    const { bagItems } = useBag();
    const navigate = useNavigate();
    return (
      <nav className="sticky top-0 z-50 bg-white shadow-md">
        {/* Top Bar */}
        <div className="border-b border-gray-200">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between h-16">
              {/* Logo */}
              <div className="flex-shrink-0">
                <h1 className="text-2xl sm:text-3xl font-bold text-pink-600">MYNTRA</h1>
              </div>

              {/* Category Menu - Hidden on mobile */}
              <div className="hidden md:flex space-x-8">
                {['Men', 'Women', 'Kids', 'Beauty', 'Home & Living'].map((category) => (
                  <button
                    key={category}
                    className="text-sm font-semibold text-gray-700 hover:text-pink-600 transition-colors uppercase tracking-wide"
                  >
                    {category}
                  </button>
                ))}
              </div>

              {/* Search Bar */}
              <div className="hidden lg:flex items-center flex-1 max-w-md mx-8">
                <div className="relative w-full">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                  <input
                    type="text"
                    placeholder="Search for products, brands and more"
                    className="w-full pl-10 pr-4 py-2 bg-gray-100 rounded-md focus:outline-none focus:ring-2 focus:ring-pink-500 text-sm"
                  />
                </div>
              </div>

              {/* Icons */}
              <div className="flex items-center space-x-4 sm:space-x-6">
                <button className="flex flex-col items-center group" aria-label="Profile">
                  <User className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700 group-hover:text-pink-600 transition-colors" />
                  <span className="text-xs mt-1 text-gray-700 group-hover:text-pink-600 hidden sm:block">Profile</span>
                </button>
                <button className="flex flex-col items-center group relative" aria-label="Wishlist" onClick={() => navigate("/wishlist")}>
                  <Heart className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700 group-hover:text-pink-600 transition-colors" />
                  <span className="text-xs mt-1 text-gray-700 group-hover:text-pink-600 hidden sm:block">Wishlist</span>
                {   wishlistItems.length > 0 && 
                    <span className="absolute -top-1 -right-1 bg-pink-600 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                    {wishlistItems.length}
                     </span> }              
                   </button>
                <button className="flex flex-col items-center group relative" aria-label="Cart" onClick={() => navigate("/checkout/bags")}>
                  <ShoppingBag className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700 group-hover:text-pink-600 transition-colors" />
                  <span className="text-xs mt-1 text-gray-700 group-hover:text-pink-600 hidden sm:block">Bag</span>
                  {   bagItems.length > 0 && 
                    <span className="absolute -top-1 -right-1 bg-pink-600 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                    {bagItems.length}
                     </span> }  
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Mobile Search */}
        <div className="lg:hidden px-4 py-3 border-b border-gray-200">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search for products"
              className="w-full pl-10 pr-4 py-2 bg-gray-100 rounded-md focus:outline-none focus:ring-2 focus:ring-pink-500 text-sm"
            />
          </div>
        </div>
      </nav>
    );
  }
export default Navbar;