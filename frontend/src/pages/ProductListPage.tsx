
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { products } from '../data/products';
import { Card, Row, Col, Container, Button, Pagination } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { categories } from '../data/categories';
import { brands } from '../data/brands';

const ITEMS_PER_PAGE = 8;

const ProductListPage = () => {
  const { category, brand } = useParams();
  const [currentPage, setCurrentPage] = useState(1);

  const filteredProducts = products.filter(product => {
    if (category) return product.category === category;
    if (brand) return product.brand === brand;
    return true;
  });

  const totalPages = Math.ceil(filteredProducts.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const currentProducts = filteredProducts.slice(startIndex, endIndex);

  const getPageTitle = () => {
    if (category) {
      const categoryData = categories.find(c => c.id === category);
      return `${categoryData?.name || ''} 상품`;
    }
    if (brand) {
      const brandData = brands.find(b => b.id === brand);
      return `${brandData?.name || ''} 브랜드`;
    }
    return '전체 상품';
  };

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const renderPagination = () => {
    if (totalPages <= 1) return null;

    const items = [];
    
    // Previous button
    items.push(
      <Pagination.Prev 
        key="prev"
        onClick={() => handlePageChange(Math.max(1, currentPage - 1))}
        disabled={currentPage === 1}
      />
    );

    // Page numbers
    for (let number = 1; number <= totalPages; number++) {
      items.push(
        <Pagination.Item
          key={number}
          active={number === currentPage}
          onClick={() => handlePageChange(number)}
        >
          {number}
        </Pagination.Item>
      );
    }

    // Next button
    items.push(
      <Pagination.Next
        key="next"
        onClick={() => handlePageChange(Math.min(totalPages, currentPage + 1))}
        disabled={currentPage === totalPages}
      />
    );

    return (
      <div className="d-flex justify-content-center mt-5">
        <Pagination size="lg">{items}</Pagination>
      </div>
    );
  };

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="fw-bold text-dark mb-0">{getPageTitle()}</h2>
        <div className="d-flex align-items-center gap-3">
          <span className="text-muted">{filteredProducts.length}개 상품</span>
          {totalPages > 1 && (
            <span className="text-muted">
              {currentPage} / {totalPages} 페이지
            </span>
          )}
        </div>
      </div>
      
      <Row xs={1} md={2} lg={3} xl={4} className="g-4">
        {currentProducts.map(product => (
          <Col key={product.id}>
            <Card 
              className="h-100 shadow-sm border-0 product-card"
              style={{ 
                transition: 'all 0.3s ease',
                cursor: 'pointer'
              }}
            >
              <div className="position-relative overflow-hidden" style={{ height: '250px' }}>
                <Card.Img 
                  variant="top" 
                  src={product.image} 
                  className="w-100 h-100"
                  style={{ 
                    objectFit: 'cover',
                    transition: 'transform 0.3s ease'
                  }}
                />
              </div>
              <Card.Body className="p-3">
                <Card.Title 
                  className="fw-bold mb-2"
                  style={{ 
                    fontSize: '1rem',
                    lineHeight: '1.4',
                    height: '2.8rem',
                    overflow: 'hidden',
                    display: '-webkit-box',
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: 'vertical'
                  }}
                >
                  {product.name}
                </Card.Title>
                <Card.Text className="fw-bold text-primary mb-3" style={{ fontSize: '1.1rem' }}>
                  {product.price.toLocaleString()}원
                </Card.Text>
                <Button 
                  as={Link} 
                  to={`/product/${product.id}`} 
                  variant="outline-primary"
                  className="w-100 fw-semibold"
                  style={{
                    borderRadius: '8px',
                    transition: 'all 0.3s ease'
                  }}
                >
                  상세보기
                </Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
      
      {filteredProducts.length === 0 ? (
        <div className="text-center py-5">
          <h4 className="text-muted">상품이 없습니다</h4>
          <p className="text-muted">다른 카테고리를 선택해보세요.</p>
        </div>
      ) : (
        renderPagination()
      )}
    </Container>
  );
};

export default ProductListPage;
