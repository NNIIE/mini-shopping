import { useParams, Link } from 'react-router-dom';
import { products } from '../data/products';
import { Container, Row, Col, Image, Button, Card, Badge } from 'react-bootstrap';
import { useCart } from '../contexts/CartContext';
import { brands } from '../data/brands';
import { categories } from '../data/categories';
import { FaShoppingCart, FaCreditCard } from 'react-icons/fa';

const ProductDetailPage = () => {
  const { id } = useParams();
  const product = products.find(p => p.id === Number(id));
  const { addToCart } = useCart();

  if (!product) {
    return (
      <Container className="text-center py-5">
        <h3 className="text-muted">상품을 찾을 수 없습니다.</h3>
      </Container>
    );
  }

  const brandName = brands.find(b => b.id === product.brand)?.name || product.brand;
  const categoryName = categories.find(c => c.id === product.category)?.name || product.category;

  const handleAddToCart = () => {
    addToCart(product.id);
  };

  return (
    <Container>
      <Row className="g-4">
        <Col lg={6}>
          <Card className="border-0 shadow-sm">
            <div style={{ height: '500px', overflow: 'hidden' }}>
              <Image 
                src={product.image} 
                className="w-100 h-100"
                style={{ objectFit: 'cover' }}
              />
            </div>
          </Card>
        </Col>
        <Col lg={6}>
          <div className="py-3">
            <Badge bg="secondary" className="mb-3">{brandName}</Badge>
            <h1 className="fw-bold text-dark mb-3">{product.name}</h1>
            
            <div className="mb-4">
              <span className="text-muted me-3">카테고리: {categoryName}</span>
            </div>
            
            <div className="mb-4">
              <h2 className="fw-bold text-primary mb-0">
                {product.price.toLocaleString()}원
              </h2>
              <small className="text-muted">부가세 포함</small>
            </div>

            <div className="mb-4 p-3 bg-light rounded">
              <h6 className="fw-semibold mb-2">상품 정보</h6>
              <p className="text-muted mb-0">
                {brandName}에서 출시한 프리미엄 {categoryName} 제품입니다. 
                뛰어난 품질과 세련된 디자인으로 많은 고객들의 사랑을 받고 있습니다.
              </p>
            </div>
            
            <div className="d-grid gap-3">
              <Button 
                variant="primary" 
                size="lg" 
                onClick={handleAddToCart}
                className="fw-semibold d-flex align-items-center justify-content-center"
                style={{ height: '50px' }}
              >
                <FaShoppingCart className="me-2" />
                장바구니에 추가
              </Button>
              <Button 
                as={Link}
                to="/order"
                state={{
                  orderItems: [{ ...product, quantity: 1 }],
                  totalAmount: product.price
                }}
                variant="success" 
                size="lg"
                className="fw-semibold d-flex align-items-center justify-content-center"
                style={{ height: '50px' }}
              >
                <FaCreditCard className="me-2" />
                바로 구매하기
              </Button>
            </div>

            <div className="mt-4 p-3 border rounded bg-white">
              <div className="row text-center">
                <div className="col-4">
                  <div className="text-primary">
                    <small className="d-block text-muted">무료배송</small>
                    <strong>50,000원 이상</strong>
                  </div>
                </div>
                <div className="col-4">
                  <div className="text-primary">
                    <small className="d-block text-muted">교환/반품</small>
                    <strong>30일 이내</strong>
                  </div>
                </div>
                <div className="col-4">
                  <div className="text-primary">
                    <small className="d-block text-muted">A/S</small>
                    <strong>1년 보장</strong>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default ProductDetailPage;