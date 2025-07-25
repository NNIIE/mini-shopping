import React from 'react';
import { useAuth } from '../contexts/AuthContext';

const OrderHistoryPage = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div>
      <h2>주문 내역</h2>
      {!isAuthenticated ? (
        <p>로그인 후 주문 내역을 확인하실 수 있습니다.</p>
      ) : (
        <p>아직 주문 내역이 없습니다.</p>
        // In a real application, you would fetch and display order history here
      )}
    </div>
  );
};

export default OrderHistoryPage;