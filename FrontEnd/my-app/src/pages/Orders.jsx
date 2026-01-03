import React, { useState } from 'react';
import { Heart, Search, ShoppingBag, User, ChevronLeft, ChevronRight } from 'lucide-react';

// Mock Data
const mockProducts = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400',
    brand: 'Roadster',
    title: 'Men Slim Fit Casual Shirt',
    price: 1299,
    originalPrice: 2599,
    discount: 50,
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400',
    brand: 'H&M',
    title: 'Women Floral Print Dress',
    price: 1799,
    originalPrice: 3999,
    discount: 55,
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400',
    brand: 'Nike',
    title: 'Running Shoes For Men',
    price: 4999,
    originalPrice: 7999,
    discount: 37,
  },
  {
    id: 4,
    image: 'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=400',
    brand: 'Zara',
    title: 'Women Handbag',
    price: 2499,
    originalPrice: 4999,
    discount: 50,
  },
  {
    id: 5,
    image: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400',
    brand: 'Puma',
    title: 'Sports T-Shirt',
    price: 899,
    originalPrice: 1999,
    discount: 55,
  },
  {
    id: 6,
    image: 'https://images.unsplash.com/photo-1584370848010-d7fe6bc767ec?w=400',
    brand: 'Levis',
    title: 'Men Slim Fit Jeans',
    price: 2199,
    originalPrice: 3999,
    discount: 45,
  },
  {
    id: 7,
    image: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=400',
    brand: 'Mango',
    title: 'Women Casual Top',
    price: 999,
    originalPrice: 2499,
    discount: 60,
  },
  {
    id: 8,
    image: 'https://images.unsplash.com/photo-1460353581641-37baddab0fa2?w=400',
    brand: 'Adidas',
    title: 'Sports Watch',
    price: 3499,
    originalPrice: 6999,
    discount: 50,
  },
];

const categories = [
  { name: 'Men', image: 'https://images.unsplash.com/photo-1490114538077-0a7f8cb49891?w=300' },
  { name: 'Women', image: 'https://images.unsplash.com/photo-1483985988355-763728e1935b?w=300' },
  { name: 'Kids', image: 'https://images.unsplash.com/photo-1503919545889-aef636e10ad4?w=300' },
  { name: 'Beauty', image: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=300' },
  { name: 'Home', image: 'https://images.unsplash.com/photo-1513694203232-719a280e022f?w=300' },
  { name: 'Footwear', image: 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=300' },
];

const banners = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=1600',
    title: 'New Season Arrivals',
    subtitle: 'Explore the latest trends',
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?w=1600',
    title: 'Big Fashion Sale',
    subtitle: 'Up to 70% off on selected items',
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=1600',
    title: 'Premium Collection',
    subtitle: 'Luxury meets comfort',
  },
];

const offers = [
  { id: 1, title: 'Flat 60% OFF', subtitle: 'On first purchase', color: 'bg-pink-500' },
  { id: 2, title: 'Buy 2 Get 1', subtitle: 'On selected brands', color: 'bg-purple-500' },
  { id: 3, title: 'Free Shipping', subtitle: 'On orders above ₹999', color: 'bg-blue-500' },
  { id: 4, title: 'Extra 20% OFF', subtitle: 'Use code: SAVE20', color: 'bg-orange-500' },
];

// Navbar Component
const Navbar = () => {
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
              <button className="flex flex-col items-center group" aria-label="Wishlist">
                <Heart className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700 group-hover:text-pink-600 transition-colors" />
                <span className="text-xs mt-1 text-gray-700 group-hover:text-pink-600 hidden sm:block">Wishlist</span>
              </button>
              <button className="flex flex-col items-center group relative" aria-label="Cart">
                <ShoppingBag className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700 group-hover:text-pink-600 transition-colors" />
                <span className="text-xs mt-1 text-gray-700 group-hover:text-pink-600 hidden sm:block">Bag</span>
                <span className="absolute -top-1 -right-1 bg-pink-600 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                  3
                </span>
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
};

// Hero Banner Component
const HeroBanner = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % banners.length);
  };

  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + banners.length) % banners.length);
  };

  return (
    <section className="relative w-full h-64 sm:h-80 md:h-96 lg:h-[500px] overflow-hidden">
      {banners.map((banner, index) => (
        <div
          key={banner.id}
          className={`absolute inset-0 transition-opacity duration-700 ${
            index === currentSlide ? 'opacity-100' : 'opacity-0'
          }`}
        >
          <img
            src={banner.image}
            alt={banner.title}
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-r from-black/60 to-black/30 flex items-center">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
              <div className="max-w-xl">
                <h2 className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-bold text-white mb-3 sm:mb-4">
                  {banner.title}
                </h2>
                <p className="text-base sm:text-lg md:text-xl text-gray-200 mb-6 sm:mb-8">
                  {banner.subtitle}
                </p>
                <div className="flex gap-4">
                  <button className="px-6 sm:px-8 py-2 sm:py-3 bg-pink-600 text-white font-semibold rounded-md hover:bg-pink-700 transition-colors">
                    Shop Now
                  </button>
                  <button className="px-6 sm:px-8 py-2 sm:py-3 bg-white text-gray-900 font-semibold rounded-md hover:bg-gray-100 transition-colors">
                    Explore
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      ))}

      {/* Navigation Buttons */}
      <button
        onClick={prevSlide}
        className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white p-2 rounded-full shadow-lg transition-all"
        aria-label="Previous slide"
      >
        <ChevronLeft className="w-5 h-5 sm:w-6 sm:h-6 text-gray-800" />
      </button>
      <button
        onClick={nextSlide}
        className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white p-2 rounded-full shadow-lg transition-all"
        aria-label="Next slide"
      >
        <ChevronRight className="w-5 h-5 sm:w-6 sm:h-6 text-gray-800" />
      </button>

      {/* Dots Indicator */}
      <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex gap-2">
        {banners.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentSlide(index)}
            className={`w-2 h-2 rounded-full transition-all ${
              index === currentSlide ? 'bg-white w-8' : 'bg-white/50'
            }`}
            aria-label={`Go to slide ${index + 1}`}
          />
        ))}
      </div>
    </section>
  );
};

// Category Card Component
const CategoryCard = ({ name, image }) => {
  return (
    <div className="flex flex-col items-center group cursor-pointer">
      <div className="w-24 h-24 sm:w-32 sm:h-32 md:w-40 md:h-40 rounded-full overflow-hidden border-4 border-gray-200 group-hover:border-pink-500 transition-all duration-300 group-hover:scale-105">
        <img
          src={image}
          alt={name}
          className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
        />
      </div>
      <h3 className="mt-3 sm:mt-4 text-sm sm:text-base font-semibold text-gray-800 group-hover:text-pink-600 transition-colors">
        {name}
      </h3>
    </div>
  );
};

// Product Card Component
const ProductCard = ({ product }) => {
  const [isWishlisted, setIsWishlisted] = useState(false);

  return (
    <div className="group cursor-pointer">
      <div className="relative overflow-hidden rounded-lg bg-gray-100 aspect-[3/4]">
        <img
          src={product.image}
          alt={product.title}
          className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
        />
        <button
          onClick={() => setIsWishlisted(!isWishlisted)}
          className="absolute top-3 right-3 p-2 bg-white rounded-full shadow-md hover:scale-110 transition-transform"
          aria-label="Add to wishlist"
        >
          <Heart
            className={`w-5 h-5 ${
              isWishlisted ? 'fill-pink-600 text-pink-600' : 'text-gray-600'
            }`}
          />
        </button>
        {product.discount && (
          <div className="absolute top-3 left-3 px-2 py-1 bg-pink-600 text-white text-xs font-bold rounded">
            {product.discount}% OFF
          </div>
        )}
      </div>
      <div className="mt-3">
        <h4 className="text-sm font-bold text-gray-800 uppercase">{product.brand}</h4>
        <p className="text-sm text-gray-600 mt-1 truncate">{product.title}</p>
        <div className="flex items-center gap-2 mt-2">
          <span className="text-lg font-bold text-gray-900">₹{product.price}</span>
          <span className="text-sm text-gray-500 line-through">₹{product.originalPrice}</span>
          <span className="text-sm text-orange-600 font-semibold">({product.discount}% OFF)</span>
        </div>
      </div>
    </div>
  );
};

// Offers Section Component
const OffersSection = () => {
  return (
    <section className="py-8 sm:py-12 md:py-16 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-6 sm:mb-8 text-center">
          Deals of the Day
        </h2>
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6">
          {offers.map((offer) => (
            <div
              key={offer.id}
              className={`${offer.color} rounded-lg p-6 sm:p-8 text-white text-center hover:scale-105 transition-transform duration-300 cursor-pointer shadow-lg`}
            >
              <h3 className="text-xl sm:text-2xl md:text-3xl font-bold mb-2">{offer.title}</h3>
              <p className="text-sm sm:text-base opacity-90">{offer.subtitle}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

// Footer Component
const Footer = () => {
  return (
    <footer className="bg-gray-900 text-gray-300 pt-12 pb-6">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-8 mb-8">
          {/* Online Shopping */}
          <div>
            <h4 className="text-white font-bold mb-4 text-sm sm:text-base">ONLINE SHOPPING</h4>
            <ul className="space-y-2 text-xs sm:text-sm">
              <li><a href="#" className="hover:text-white transition-colors">Men</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Women</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Kids</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Beauty</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Home & Living</a></li>
            </ul>
          </div>

          {/* Customer Policies */}
          <div>
            <h4 className="text-white font-bold mb-4 text-sm sm:text-base">CUSTOMER POLICIES</h4>
            <ul className="space-y-2 text-xs sm:text-sm">
              <li><a href="#" className="hover:text-white transition-colors">Contact Us</a></li>
              <li><a href="#" className="hover:text-white transition-colors">FAQ</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Terms Of Use</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Track Orders</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Shipping</a></li>
            </ul>
          </div>

          {/* Useful Links */}
          <div>
            <h4 className="text-white font-bold mb-4 text-sm sm:text-base">USEFUL LINKS</h4>
            <ul className="space-y-2 text-xs sm:text-sm">
              <li><a href="#" className="hover:text-white transition-colors">Blog</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Careers</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Site Map</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Corporate Info</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Privacy Policy</a></li>
            </ul>
          </div>

          {/* Experience App */}
          <div>
            <h4 className="text-white font-bold mb-4 text-sm sm:text-base">EXPERIENCE APP</h4>
            <p className="text-xs sm:text-sm mb-4">Download our app for exclusive deals</p>
            <div className="flex gap-2 mb-6">
              <div className="w-10 h-10 bg-gray-700 rounded-lg flex items-center justify-center hover:bg-gray-600 transition-colors cursor-pointer">
                <span className="text-xl">📱</span>
              </div>
              <div className="w-10 h-10 bg-gray-700 rounded-lg flex items-center justify-center hover:bg-gray-600 transition-colors cursor-pointer">
                <span className="text-xl">🤖</span>
              </div>
            </div>
            <h4 className="text-white font-bold mb-4 text-sm sm:text-base">KEEP IN TOUCH</h4>
            <div className="flex gap-3">
              <a href="#" className="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center hover:bg-pink-600 transition-colors" aria-label="Facebook">
                <span className="text-sm">f</span>
              </a>
              <a href="#" className="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center hover:bg-pink-600 transition-colors" aria-label="Twitter">
                <span className="text-sm">𝕏</span>
              </a>
              <a href="#" className="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center hover:bg-pink-600 transition-colors" aria-label="Instagram">
                <span className="text-sm">📷</span>
              </a>
              <a href="#" className="w-8 h-8 bg-gray-700 rounded-full flex items-center justify-center hover:bg-pink-600 transition-colors" aria-label="YouTube">
                <span className="text-sm">▶</span>
              </a>
            </div>
          </div>
        </div>

        {/* Copyright */}
        <div className="border-t border-gray-800 pt-6 text-center">
          <p className="text-xs sm:text-sm text-gray-400">
            © 2026 www.myntra.com. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

// Main Home Component
const Home = () => {
  return (
    <div className="min-h-screen bg-white">
      {/* Navbar */}
      <Navbar />

      {/* Hero Banner */}
      <HeroBanner />

      {/* Category Highlights Section */}
      <section className="py-8 sm:py-12 md:py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-6 sm:mb-8 md:mb-12 text-center">
            Shop by Category
          </h2>
          <div className="grid grid-cols-3 md:grid-cols-6 gap-4 sm:gap-6 md:gap-8">
            {categories.map((category) => (
              <CategoryCard key={category.name} {...category} />
            ))}
          </div>
        </div>
      </section>

      {/* Trending Products Section */}
      <section className="py-8 sm:py-12 md:py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-6 sm:mb-8 text-center">
            Trending Now
          </h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 sm:gap-6">
            {mockProducts.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
          <div className="text-center mt-8 sm:mt-12">
            <button className="px-8 sm:px-12 py-3 sm:py-4 bg-pink-600 text-white font-semibold rounded-md hover:bg-pink-700 transition-colors text-sm sm:text-base">
              View All Products
            </button>
          </div>
        </div>
      </section>

      {/* Offers & Deals Section */}
      <OffersSection />

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default Home;