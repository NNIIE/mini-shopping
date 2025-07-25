import React, { useState, useEffect } from 'react';
import { Container, Card, Row, Col, Button, Form, Alert } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

interface UserProfile {
  id: string;
  name: string;
  email: string;
  joinDate: string;
  phone?: string;
  address?: string;
}

const ProfilePage: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [editMode, setEditMode] = useState<boolean>(false);
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    address: ''
  });
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    
    fetchProfile();
  }, [isAuthenticated, navigate]);

  const fetchProfile = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/user/profile', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const data = await response.json();
        setProfile(data);
        setFormData({
          name: data.name,
          phone: data.phone || '',
          address: data.address || ''
        });
      } else {
        setError('프로필 정보를 불러오는데 실패했습니다.');
      }
    } catch (error) {
      setError('네트워크 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/user/profile', {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const updatedProfile = await response.json();
        setProfile(updatedProfile);
        setEditMode(false);
        setSuccess('프로필이 성공적으로 업데이트되었습니다.');
      } else {
        setError('프로필 업데이트에 실패했습니다.');
      }
    } catch (error) {
      setError('네트워크 오류가 발생했습니다.');
    }
  };

  const handleLogout = () => {
    logout();
    localStorage.removeItem('token');
    navigate('/');
  };

  if (loading) {
    return (
      <Container className="py-5">
        <div className="text-center">로딩 중...</div>
      </Container>
    );
  }

  if (!profile) {
    return (
      <Container className="py-5">
        <Alert variant="danger">프로필 정보를 불러올 수 없습니다.</Alert>
      </Container>
    );
  }

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8}>
          <Card>
            <Card.Header className="bg-primary text-white">
              <h3 className="mb-0">마이페이지</h3>
            </Card.Header>
            <Card.Body className="p-4">
              {error && <Alert variant="danger">{error}</Alert>}
              {success && <Alert variant="success">{success}</Alert>}

              {!editMode ? (
                <div>
                  <Row className="mb-3">
                    <Col sm={3}><strong>이름:</strong></Col>
                    <Col sm={9}>{profile.name}</Col>
                  </Row>
                  <Row className="mb-3">
                    <Col sm={3}><strong>이메일:</strong></Col>
                    <Col sm={9}>{profile.email}</Col>
                  </Row>
                  <Row className="mb-3">
                    <Col sm={3}><strong>가입일:</strong></Col>
                    <Col sm={9}>{new Date(profile.joinDate).toLocaleDateString('ko-KR')}</Col>
                  </Row>
                  <Row className="mb-3">
                    <Col sm={3}><strong>전화번호:</strong></Col>
                    <Col sm={9}>{profile.phone || '등록되지 않음'}</Col>
                  </Row>
                  <Row className="mb-4">
                    <Col sm={3}><strong>주소:</strong></Col>
                    <Col sm={9}>{profile.address || '등록되지 않음'}</Col>
                  </Row>

                  <div className="d-flex gap-2">
                    <Button variant="primary" onClick={() => setEditMode(true)}>
                      프로필 수정
                    </Button>
                    <Button variant="outline-danger" onClick={handleLogout}>
                      로그아웃
                    </Button>
                  </div>
                </div>
              ) : (
                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label>이름</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={formData.name}
                      onChange={handleChange}
                      required
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>이메일 (변경 불가)</Form.Label>
                    <Form.Control
                      type="email"
                      value={profile.email}
                      disabled
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>전화번호</Form.Label>
                    <Form.Control
                      type="tel"
                      name="phone"
                      value={formData.phone}
                      onChange={handleChange}
                      placeholder="전화번호를 입력하세요"
                    />
                  </Form.Group>

                  <Form.Group className="mb-4">
                    <Form.Label>주소</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      name="address"
                      value={formData.address}
                      onChange={handleChange}
                      placeholder="주소를 입력하세요"
                    />
                  </Form.Group>

                  <div className="d-flex gap-2">
                    <Button type="submit" variant="primary">
                      저장
                    </Button>
                    <Button 
                      variant="secondary" 
                      onClick={() => {
                        setEditMode(false);
                        setFormData({
                          name: profile.name,
                          phone: profile.phone || '',
                          address: profile.address || ''
                        });
                      }}
                    >
                      취소
                    </Button>
                  </div>
                </Form>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ProfilePage;