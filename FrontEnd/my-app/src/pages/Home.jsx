import { useEffect, useState } from "react";
import {Heart,ChevronLeft,ChevronRight, } from "lucide-react";
import { useWishlist } from "../context/WishlistContext";
import { useBag } from "../context/BagContext";
import { getAllProducts } from "../services/productService";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";


  const PAGE_SIZE = 16;

  const categories = [
    { name: "Men", image: "https://images.unsplash.com/photo-1490114538077-0a7f8cb49891?w=300" },
    { name: "Women", image: "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=300" },
    { name: "Kids", image: "https://images.unsplash.com/photo-1503919545889-aef636e10ad4?w=300" },
    { name: "Beauty", image: "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=300" },
    { name: "Home", image: "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=300" },
    { name: "Footwear", image: "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=300" },
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
    { title: "Flat 60% OFF", subtitle: "On first purchase", color: "bg-pink-500" },
    { title: "Buy 2 Get 1", subtitle: "On selected brands", color: "bg-purple-500" },
    { title: "Free Shipping", subtitle: "Above ₹999", color: "bg-blue-500" },
    { title: "Extra 20% OFF", subtitle: "Use SAVE20", color: "bg-orange-500" },
  ];


  const HeroBanner = () => {
    const [index, setIndex] = useState(0);
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
  const ProductCard = ({ product }) => {
  const { wishlistItems, addToWishlist, removeFromWishlist } = useWishlist();
  const { bagItems, removeFromBag, addToBag } = useBag();

    const isWishlisted = wishlistItems.some(
    (item) => item.id === product.id
    );

    const isBaged = bagItems.some(
    (item) => item.id === product.id
    );

    return (
      <div className="relative group overflow-hidden rounded-lg bg-gray-100 aspect-[3/4]">
  <img
    src={product.images1}
    alt={product.title}
    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
  />

  {/* Wishlist Button */}
  <button
    onClick={(e) => {
      e.stopPropagation();
      isWishlisted
        ? removeFromWishlist(product.id)
        : addToWishlist(product);
    }}
    className="absolute top-3 right-3 z-10 p-2 bg-white rounded-full shadow-md hover:scale-110 transition-transform"
  >
    <Heart
      className={`w-5 h-5 ${
        isWishlisted
          ? "fill-pink-600 text-pink-600"
          : "text-gray-600"
      }`}
    />
  </button>

  <button
    onClick={(e) => {
      e.stopPropagation();
      isBaged
        ? removeFromBag(product.id)
        : addToBag(product);
    }}
    className="
      absolute bottom-0 left-0 right-0
      bg-pink-600 text-white font-semibold py-3
      opacity-0 translate-y-full
      group-hover:opacity-100 group-hover:translate-y-0
      transition-all duration-300
    "
  >
    {isBaged ? "Remove from Bag" : "Add to Bag"}
  </button>

  {product.discount && (
    <div className="absolute top-3 left-3 px-2 py-1 bg-pink-600 text-white text-xs font-bold rounded">
      {product.discount}% OFF
    </div>
  )}
</div>

    );
  };

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

 

  const Home = () => {
    const [products, setProducts] = useState([]);
    const [page, setPage] = useState(0);
    const [hasNext, setHasNext] = useState(true);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
      const fetchProducts = async () => {
        setLoading(true);
        try {
          const data = await getAllProducts(page, PAGE_SIZE);
          console.log("Fetched products:", data);
          setProducts(data);

          setHasNext(data.length === PAGE_SIZE);
        } catch (err) {
          console.error("Failed to fetch products", err);
        } finally {
          setLoading(false);
        }
      };

      fetchProducts();
    }, [page]);

    return (
      <div className="bg-gray-50">
        <Navbar />
        <HeroBanner />

        {/* Categories */}
        <section className="py-10 max-w-7xl mx-auto grid grid-cols-3 md:grid-cols-6 gap-6">
          {categories.map((c) => (
            <CategoryCard key={c.name} {...c} />
          ))}
        </section>

        {/* Products */}
        <section className="py-10 max-w-7xl mx-auto">
          <h2 className="text-3xl font-bold mb-6 text-center">Trending Now</h2>

          {loading && <p className="text-center">Loading products...</p>}

          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {products.map((p) => (
              <ProductCard key={p.id || p.productId} product={p} />
            ))}
          </div>

          </section>
            <section>
          {/* Pagination */}
          <div className="flex justify-center gap-4 mt-8">
            <button
              disabled={page === 0}
              onClick={() => setPage((p) => p - 1)}
              className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
            >
              Prev
            </button>

            <span className="px-4 py-2 font-semibold">
              Page {page + 1}
            </span>

            <button
              disabled={!hasNext}
              onClick={() => setPage((p) => p + 1)}
              className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
            >
              Next
            </button>
          </div>
        </section>

        <OffersSection /> 

        <Footer />
      </div>
    );
  };

  export default Home;
