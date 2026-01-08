
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

export default Footer;