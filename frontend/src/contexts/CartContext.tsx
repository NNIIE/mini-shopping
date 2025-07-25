
import React, { createContext, useState, useContext, ReactNode } from 'react';
import { products } from '../data/products';
import { useNotificationContext } from './NotificationContext';

interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  category: string;
  brand: string;
}

interface CartItem extends Product {
  quantity: number;
}

interface CartContextType {
  cartItems: CartItem[];
  addToCart: (productId: number) => void;
  removeFromCart: (productId: number) => void;
  clearCart: () => void;
  getCartTotal: () => number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const { showSuccess, showInfo } = useNotificationContext();

  const addToCart = (productId: number) => {
    const existingItem = cartItems.find(item => item.id === productId);
    const productToAdd = products.find(p => p.id === productId);
    
    if (existingItem) {
      setCartItems(cartItems.map(item =>
        item.id === productId ? { ...item, quantity: item.quantity + 1 } : item
      ));
      showSuccess('수량 추가!', `${productToAdd?.name}의 수량이 추가되었습니다.`);
    } else {
      if (productToAdd) {
        setCartItems([...cartItems, { ...productToAdd, quantity: 1 }]);
        showSuccess('장바구니 추가!', `${productToAdd.name}이 장바구니에 추가되었습니다.`);
      }
    }
  };

  const removeFromCart = (productId: number) => {
    const item = cartItems.find(item => item.id === productId);
    setCartItems(cartItems.filter(item => item.id !== productId));
    if (item) {
      showInfo('상품 제거', `${item.name}이 장바구니에서 제거되었습니다.`);
    }
  };

  const clearCart = () => {
    setCartItems([]);
    showInfo('장바구니 비움', '모든 상품이 장바구니에서 제거되었습니다.');
  };

  const getCartTotal = () => {
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
  };

  return (
    <CartContext.Provider value={{
      cartItems,
      addToCart,
      removeFromCart,
      clearCart,
      getCartTotal,
    }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
