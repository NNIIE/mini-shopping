
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import ProductListPage from './pages/ProductListPage';
import ProductDetailPage from './pages/ProductDetailPage';
import CartPage from './pages/CartPage';
import OrderHistoryPage from './pages/OrderHistoryPage';
import OrderPage from './pages/OrderPage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import ProfilePage from './pages/ProfilePage';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import { NotificationProvider } from './contexts/NotificationContext';

function App() {
  return (
    <Router>
      <NotificationProvider>
        <AuthProvider>
          <CartProvider>
            <Header />
            <main className="mt-4">
              <Routes>
                <Route path="/" element={<ProductListPage />} />
                <Route path="/products/:category" element={<ProductListPage />} />
                <Route path="/brand/:brand" element={<ProductListPage />} />
                <Route path="/product/:id" element={<ProductDetailPage />} />
                <Route path="/cart" element={<CartPage />} />
                <Route path="/order" element={<OrderPage />} />
                <Route path="/orders" element={<OrderHistoryPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/profile" element={<ProfilePage />} />
              </Routes>
            </main>
          </CartProvider>
        </AuthProvider>
      </NotificationProvider>
    </Router>
  );
}

export default App;
