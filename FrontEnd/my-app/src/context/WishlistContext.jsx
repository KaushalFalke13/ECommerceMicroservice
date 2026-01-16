import { createContext, useContext, useState , useEffect} from "react";
import {getProductsFromWatchlist ,addProductToWatchlist , removeProductFromWatchlist} from "../services/WishlistService.js";

const WishlistContext = createContext();

export const WishlistProvider = ({ children }) => {
  const [wishlistItems, setWishlistItems] = useState([]);

  const fetchWishlistItems = async () => {
    try {
      const products = await getProductsFromWatchlist();
      setWishlistItems(products);  
    } catch (error) {
      console.error("Failed to fetch wishlist items:", error);
    }
  };

    useEffect(() => {
      fetchWishlistItems();
    }, []);


  const addToWishlist = async (product) => {
    try {
        await addProductToWatchlist(product.id);
        fetchWishlistItems(); 
      } catch (error) {
        console.error("Failed to add item to wishlist:", error);
      }
  };

  const removeFromWishlist =async (productId) => {
    try {
        await removeProductFromWatchlist(productId);
        fetchWishlistItems(); 
      } catch (error) {
        console.error("Failed to add item to bag:", error);
      }
  };

  const isWishlisted = (productId) => {
    return wishlistItems.some((item) => item.id === productId);
  };

  return (
    <WishlistContext.Provider
      value={{
        wishlistItems,
        addToWishlist,
        removeFromWishlist,
        isWishlisted,
      }}
    >
      {children}
    </WishlistContext.Provider>
  );
};

export const useWishlist = () => useContext(WishlistContext);
