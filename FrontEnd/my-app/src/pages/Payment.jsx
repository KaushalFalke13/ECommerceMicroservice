import React, { useState } from 'react';
import { ShoppingBag, MapPin, CreditCard, Check, Trash2, Heart, Plus, X, ChevronLeft, ChevronDown, Gift, Tag } from 'lucide-react';


const PaymentOptions = ({ selectedPayment, onSelect }) => {
  const paymentMethods = [
    { id: 'card', name: 'Credit / Debit Card', description: 'Visa, Mastercard, Rupay and more' },
    { id: 'upi', name: 'UPI', description: 'Google Pay, PhonePe, Paytm' },
    { id: 'netbanking', name: 'Net Banking', description: 'All major banks supported' },
    { id: 'cod', name: 'Cash on Delivery', description: 'Pay when you receive' }
  ];

  return (
    <div className="space-y-3">
      {paymentMethods.map(method => (
        <div
          key={method.id}
          onClick={() => onSelect(method.id)}
          className={`p-4 rounded border-2 cursor-pointer transition-all ${
            selectedPayment === method.id
              ? 'border-pink-500 bg-pink-50'
              : 'border-gray-200 hover:border-gray-300'
          }`}
        >
          <div className="flex items-center gap-3">
            <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0 ${
              selectedPayment === method.id
                ? 'border-pink-500 bg-pink-500'
                : 'border-gray-300'
            }`}>
              {selectedPayment === method.id && <Check size={14} className="text-white" />}
            </div>
            <div className="flex-1">
              <h4 className="font-bold text-sm text-gray-900 mb-1">{method.name}</h4>
              <p className="text-xs text-gray-600">{method.description}</p>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};


const Payment = () => {

  const [selectedPayment, setSelectedPayment] = useState('');
  
  return (
    <div className="bg-white border border-gray-200 rounded p-5">
                <h2 className="text-sm font-bold text-gray-900 mb-4">Choose Payment Method</h2>
                <PaymentOptions
                  selectedPayment={selectedPayment}
                  onSelect={setSelectedPayment}
            />
    </div>
  );
};

export default Payment;
