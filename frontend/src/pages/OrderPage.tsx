import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { FaCreditCard, FaCheckCircle, FaTruck, FaUser } from 'react-icons/fa';
import { useCart } from '../contexts/CartContext';

interface OrderItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  brand: string;
}

const OrderPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { clearCart } = useCart();
  
  // Get order data from navigation state or cart
  const orderItems: OrderItem[] = location.state?.orderItems || [];
  const totalAmount = location.state?.totalAmount || 0;
  
  const [orderForm, setOrderForm] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
    detailAddress: '',
    zipCode: '',
    paymentMethod: 'card',
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    agreeTerms: false
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    const checked = (e.target as HTMLInputElement).checked;
    
    setOrderForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmitOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!orderForm.agreeTerms) {
      alert('이용약관에 동의해주세요.');
      return;
    }

    if (orderItems.length === 0) {
      alert('주문할 상품이 없습니다.');
      navigate('/cart');
      return;
    }

    setIsSubmitting(true);
    
    // Simulate order processing
    setTimeout(() => {
      alert('주문이 성공적으로 완료되었습니다!');
      clearCart();
      navigate('/orders');
      setIsSubmitting(false);
    }, 2000);
  };

  if (orderItems.length === 0) {
    return (
      <Container>
        <Alert variant="warning" className="text-center">
          <h4>주문할 상품이 없습니다</h4>
          <p>장바구니에서 상품을 추가한 후 주문해주세요.</p>
          <Button variant="primary" onClick={() => navigate('/cart')}>
            장바구니로 이동
          </Button>
        </Alert>
      </Container>
    );
  }

  return (
    <Container>
      <div className="mb-4">
        <h2 className="fw-bold text-dark mb-2">
          <FaCreditCard className="me-2" />
          주문/결제
        </h2>
        <p className="text-muted">주문 정보를 확인하고 결제를 진행해주세요.</p>
      </div>

      <Form onSubmit={handleSubmitOrder}>
        <Row className="g-4">
          {/* 주문 상품 정보 */}
          <Col lg={8}>
            <Card className="mb-4 border-0 shadow-sm">
              <Card.Header className="bg-light border-0">
                <h5 className="mb-0 fw-semibold">
                  <FaTruck className="me-2" />
                  주문 상품 ({orderItems.length}개)
                </h5>
              </Card.Header>
              <Card.Body>
                {orderItems.map(item => (
                  <div key={item.id} className="d-flex align-items-center py-3 border-bottom">
                    <div style={{ width: '80px', height: '80px' }} className="me-3">
                      <img 
                        src={item.image} 
                        alt={item.name}
                        className="w-100 h-100 rounded"
                        style={{ objectFit: 'cover' }}
                      />
                    </div>
                    <div className="flex-grow-1">
                      <h6 className="fw-semibold mb-1">{item.name}</h6>
                      <small className="text-muted">브랜드: {item.brand}</small>
                      <div className="d-flex justify-content-between mt-2">
                        <span>수량: {item.quantity}개</span>
                        <span className="fw-bold text-primary">
                          {(item.price * item.quantity).toLocaleString()}원
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </Card.Body>
            </Card>

            {/* 배송 정보 */}
            <Card className="mb-4 border-0 shadow-sm">
              <Card.Header className="bg-light border-0">
                <h5 className="mb-0 fw-semibold">
                  <FaUser className="me-2" />
                  배송 정보
                </h5>
              </Card.Header>
              <Card.Body>
                <Row className="g-3">
                  <Col md={6}>
                    <Form.Label>이름 *</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={orderForm.name}
                      onChange={handleInputChange}
                      required
                    />
                  </Col>
                  <Col md={6}>
                    <Form.Label>이메일 *</Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={orderForm.email}
                      onChange={handleInputChange}
                      required
                    />
                  </Col>
                  <Col md={6}>
                    <Form.Label>전화번호 *</Form.Label>
                    <Form.Control
                      type="tel"
                      name="phone"
                      value={orderForm.phone}
                      onChange={handleInputChange}
                      required
                    />
                  </Col>
                  <Col md={6}>
                    <Form.Label>우편번호 *</Form.Label>
                    <Form.Control
                      type="text"
                      name="zipCode"
                      value={orderForm.zipCode}
                      onChange={handleInputChange}
                      required
                    />
                  </Col>
                  <Col xs={12}>
                    <Form.Label>주소 *</Form.Label>
                    <Form.Control
                      type="text"
                      name="address"
                      value={orderForm.address}
                      onChange={handleInputChange}
                      required
                    />
                  </Col>
                  <Col xs={12}>
                    <Form.Label>상세주소</Form.Label>
                    <Form.Control
                      type="text"
                      name="detailAddress"
                      value={orderForm.detailAddress}
                      onChange={handleInputChange}
                    />
                  </Col>
                </Row>
              </Card.Body>
            </Card>

            {/* 결제 방법 */}
            <Card className="border-0 shadow-sm">
              <Card.Header className="bg-light border-0">
                <h5 className="mb-0 fw-semibold">
                  <FaCreditCard className="me-2" />
                  결제 방법
                </h5>
              </Card.Header>
              <Card.Body>
                <div className="mb-3">
                  <Form.Check
                    type="radio"
                    id="card"
                    name="paymentMethod"
                    value="card"
                    label="신용카드"
                    checked={orderForm.paymentMethod === 'card'}
                    onChange={handleInputChange}
                  />
                  <Form.Check
                    type="radio"
                    id="bank"
                    name="paymentMethod"
                    value="bank"
                    label="무통장입금"
                    checked={orderForm.paymentMethod === 'bank'}
                    onChange={handleInputChange}
                  />
                </div>

                {orderForm.paymentMethod === 'card' && (
                  <Row className="g-3">
                    <Col xs={12}>
                      <Form.Label>카드번호 *</Form.Label>
                      <Form.Control
                        type="text"
                        name="cardNumber"
                        value={orderForm.cardNumber}
                        onChange={handleInputChange}
                        placeholder="1234-5678-9012-3456"
                        required
                      />
                    </Col>
                    <Col md={6}>
                      <Form.Label>유효기간 *</Form.Label>
                      <Form.Control
                        type="text"
                        name="expiryDate"
                        value={orderForm.expiryDate}
                        onChange={handleInputChange}
                        placeholder="MM/YY"
                        required
                      />
                    </Col>
                    <Col md={6}>
                      <Form.Label>CVV *</Form.Label>
                      <Form.Control
                        type="text"
                        name="cvv"
                        value={orderForm.cvv}
                        onChange={handleInputChange}
                        placeholder="123"
                        required
                      />
                    </Col>
                  </Row>
                )}
              </Card.Body>
            </Card>
          </Col>

          {/* 결제 정보 */}
          <Col lg={4}>
            <Card className="border-0 shadow-sm sticky-top" style={{ top: '100px' }}>
              <Card.Header className="bg-primary text-white border-0">
                <h5 className="mb-0 fw-semibold">결제 정보</h5>
              </Card.Header>
              <Card.Body>
                <div className="d-flex justify-content-between mb-2">
                  <span>상품금액</span>
                  <span>{totalAmount.toLocaleString()}원</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>배송비</span>
                  <span className="text-success">무료</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>할인금액</span>
                  <span>-0원</span>
                </div>
                <hr />
                <div className="d-flex justify-content-between fw-bold text-primary mb-3">
                  <span>총 결제금액</span>
                  <span className="fs-5">{totalAmount.toLocaleString()}원</span>
                </div>

                <Form.Check
                  type="checkbox"
                  id="agreeTerms"
                  name="agreeTerms"
                  checked={orderForm.agreeTerms}
                  onChange={handleInputChange}
                  label="주문 내용을 확인하였으며, 정보 제공 등에 동의합니다."
                  className="mb-3 small"
                />

                <div className="d-grid gap-2">
                  <Button
                    type="submit"
                    variant="primary"
                    size="lg"
                    disabled={isSubmitting || !orderForm.agreeTerms}
                    className="fw-semibold"
                  >
                    {isSubmitting ? (
                      <>처리중...</>
                    ) : (
                      <>
                        <FaCheckCircle className="me-2" />
                        {totalAmount.toLocaleString()}원 결제하기
                      </>
                    )}
                  </Button>
                  <Button
                    variant="outline-secondary"
                    onClick={() => navigate('/cart')}
                  >
                    장바구니로 돌아가기
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Form>
    </Container>
  );
};

export default OrderPage;