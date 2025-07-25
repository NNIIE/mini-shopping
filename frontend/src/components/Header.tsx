
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Navbar, Nav, NavDropdown, Button, Container, Badge } from 'react-bootstrap';
import { FaUserCircle, FaShoppingCart, FaStore } from 'react-icons/fa';
import { categories } from '../data/categories';
import { brands } from '../data/brands';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';

const Header = () => {
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuth();
  const { cartItems } = useCart();

  const handleProfileClick = () => {
    if (isAuthenticated) {
      navigate('/profile');
    } else {
      navigate('/login');
    }
  };

  const handleLoginClick = () => {
    navigate('/login');
  };

  const cartItemCount = cartItems.reduce((total, item) => total + item.quantity, 0);

  return (
    <Navbar 
      expand="lg" 
      className="shadow-sm mb-4"
      style={{ 
        backgroundColor: '#ffffff',
        minHeight: '70px',
        borderBottom: '1px solid #e9ecef',
        position: 'sticky',
        top: 0,
        zIndex: 1030
      }}
      variant="light"
    >
      <Container>
        <Navbar.Brand 
          as={Link} 
          to="/" 
          className="fw-bold fs-3 d-flex align-items-center"
          style={{ 
            color: '#2c3e50',
            textDecoration: 'none',
            transition: 'color 0.2s ease'
          }}
        >
          <FaStore className="me-2" style={{ 
            fontSize: '1.5rem', 
            color: '#3498db'
          }} />
          StyleShop
        </Navbar.Brand>
        
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto ms-4">
            <NavDropdown 
              title={
                <span style={{ 
                  color: '#495057', 
                  fontSize: '1rem',
                  fontWeight: '500'
                }}>
                  브랜드
                </span>
              } 
              id="brand-dropdown" 
              className="me-4"
              drop="down"
              menuVariant="light"
            >
              {brands.map(brand => (
                <NavDropdown.Item 
                  key={brand.id} 
                  as={Link} 
                  to={`/brand/${brand.id}`}
                >
                  {brand.name}
                </NavDropdown.Item>
              ))}
            </NavDropdown>
            <NavDropdown 
              title={
                <span style={{ 
                  color: '#495057', 
                  fontSize: '1rem',
                  fontWeight: '500'
                }}>
                  카테고리
                </span>
              } 
              id="category-dropdown"
              drop="down"
              menuVariant="light"
            >
              {categories.map(category => (
                <NavDropdown.Item 
                  key={category.id} 
                  as={Link} 
                  to={`/products/${category.id}`}
                >
                  {category.name}
                </NavDropdown.Item>
              ))}
            </NavDropdown>
          </Nav>

          <Nav className="d-flex align-items-center">
            <Nav.Link 
              as={Link} 
              to="/cart" 
              className="position-relative me-4"
              style={{
                color: '#495057',
                fontSize: '1.3rem',
                transition: 'color 0.2s ease'
              }}
            >
              <FaShoppingCart />
              {cartItemCount > 0 && (
                <Badge 
                  bg="danger" 
                  pill 
                  className="position-absolute top-0 start-100 translate-middle"
                  style={{ 
                    fontSize: '0.7rem',
                    transform: 'translate(-50%, -50%)'
                  }}
                >
                  {cartItemCount}
                </Badge>
              )}
            </Nav.Link>
            
            {isAuthenticated ? (
              <NavDropdown 
                title={
                  <span className="d-flex align-items-center" style={{
                    color: '#495057',
                    fontSize: '0.95rem',
                    fontWeight: '500'
                  }}>
                    <FaUserCircle size={18} className="me-2" />
                    {user?.name || '사용자'}
                  </span>
                } 
                id="user-dropdown"
                align="end"
                menuVariant="light"
              >
                <NavDropdown.Item onClick={handleProfileClick}>
                  마이페이지
                </NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/orders">
                  주문 내역
                </NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={logout} className="text-danger">
                  로그아웃
                </NavDropdown.Item>
              </NavDropdown>
            ) : (
              <div className="d-flex gap-2">
                <Button 
                  variant="outline-primary" 
                  onClick={handleLoginClick}
                  size="sm"
                  style={{
                    borderRadius: '6px',
                    fontSize: '0.9rem',
                    fontWeight: '500',
                    paddingX: '16px'
                  }}
                >
                  로그인
                </Button>
                <Button 
                  variant="primary" 
                  as={Link}
                  to="/signup"
                  size="sm"
                  style={{
                    borderRadius: '6px',
                    fontSize: '0.9rem',
                    fontWeight: '500',
                    paddingX: '16px',
                    textDecoration: 'none'
                  }}
                >
                  회원가입
                </Button>
              </div>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
