import React from 'react';
import { useCart } from '../contexts/CartContext';
import { Button, Card, Row, Col, Container, Image, Badge } from 'react-bootstrap';
import { FaTrash, FaShoppingCart, FaCreditCard } from 'react-icons/fa';
import { Link } from 'react-router-dom';

const CartPage = () => {
  const { cartItems, removeFromCart, clearCart, getCartTotal } = useCart();

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="fw-bold text-dark mb-0">장바구니</h2>
        {cartItems.length > 0 && (
          <Badge bg="primary" pill className="fs-6">{cartItems.length}개 상품</Badge>
        )}
      </div>

      {cartItems.length === 0 ? (
        <div className="text-center py-5">
          <FaShoppingCart size={80} className="text-muted mb-4" />
          <h4 className="text-muted mb-3">장바구니가 비어있습니다</h4>
          <p className="text-muted mb-4">마음에 드는 상품을 담아보세요!</p>
          <Button as={Link} to="/" variant="primary" size="lg">
            쇼핑 계속하기
          </Button>
        </div>
      ) : (
        <>
          <Row className="g-3">
            {cartItems.map(item => (
              <Col xs={12} key={item.id}>
                <Card className="cart-item-card">
                  <Card.Body className="p-3">
                    <Row className="align-items-center">
                      <Col xs={12} md={2} className="text-center mb-2 mb-md-0">
                        <div style={{ width: '80px', height: '80px', margin: '0 auto' }}>
                          <Image 
                            src={item.image} 
                            alt={item.name} 
                            className="w-100 h-100 rounded"
                            style={{ objectFit: 'cover' }}
                          />
                        </div>
                      </Col>
                      <Col xs={12} md={5} className="mb-2 mb-md-0">
                        <h6 className="fw-semibold mb-1">{item.name}</h6>
                        <small className="text-muted">브랜드: {item.brand}</small>
                      </Col>
                      <Col xs={6} md={2} className="text-center">
                        <Badge bg="light" text="dark" className="fw-semibold">
                          {item.quantity}개
                        </Badge>
                      </Col>
                      <Col xs={6} md={2} className="text-center">
                        <div className="fw-bold text-primary">
                          {(item.price * item.quantity).toLocaleString()}원
                        </div>
                        <small className="text-muted">
                          단가: {item.price.toLocaleString()}원
                        </small>
                      </Col>
                      <Col xs={12} md={1} className="text-center mt-2 mt-md-0">
                        <Button 
                          variant="outline-danger" 
                          size="sm" 
                          onClick={() => removeFromCart(item.id)}
                          className="fw-semibold"
                        >
                          <FaTrash />
                        </Button>
                      </Col>
                    </Row>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <Card className="mt-4 border-0 shadow-sm">
            <Card.Body className="p-4">
              <Row className="align-items-center">
                <Col md={6}>
                  <div className="d-flex justify-content-between mb-2">
                    <span>상품 총액</span>
                    <span>{getCartTotal().toLocaleString()}원</span>
                  </div>
                  <div className="d-flex justify-content-between mb-2">
                    <span>배송비</span>
                    <span className="text-success">무료</span>
                  </div>
                  <hr />
                  <div className="d-flex justify-content-between">
                    <h5 className="fw-bold">총 결제금액</h5>
                    <h5 className="fw-bold text-primary">{getCartTotal().toLocaleString()}원</h5>
                  </div>
                </Col>
                <Col md={6}>
                  <div className="d-grid gap-2">
                    <Button 
                      as={Link}
                      to="/order"
                      state={{
                        orderItems: cartItems,
                        totalAmount: getCartTotal()
                      }}
                      variant="primary" 
                      size="lg"
                      className="fw-semibold d-flex align-items-center justify-content-center"
                    >
                      <FaCreditCard className="me-2" />
                      주문하기
                    </Button>
                    <div className="d-flex gap-2">
                      <Button 
                        variant="outline-secondary" 
                        onClick={clearCart}
                        className="flex-fill"
                      >
                        <FaTrash className="me-2" />
                        전체 삭제
                      </Button>
                      <Button 
                        as={Link}
                        to="/"
                        variant="outline-primary"
                        className="flex-fill"
                      >
                        계속 쇼핑하기
                      </Button>
                    </div>
                  </div>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </>
      )}
    </Container>
  );
};

export default CartPage;