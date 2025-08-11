import React, { useState } from "react";
import { Container, Form, Button, Card, Alert } from "react-bootstrap";
import { useNavigate, Link } from "react-router-dom";

const SignupPage: React.FC = () => {
  const [formData, setFormData] = useState({
    email: "",
    nickname: "",
    password: "",
    confirmPassword: "",
    phoneNumber: "",
  });
  const [error, setError] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);

  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError("");
  };

  const validateForm = () => {
    // 이메일 유효성 검사
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
    if (!emailRegex.test(formData.email)) {
      setError("이메일 형식이 올바르지 않습니다.");
      return false;
    }

    // 닉네임 유효성 검사
    const nicknameRegex = /^[가-힣a-zA-Z]+$/;
    if (!nicknameRegex.test(formData.nickname)) {
      setError("닉네임은 한글 또는 영어이어야 합니다.");
      return false;
    }
    if (formData.nickname.length > 10) {
      setError("닉네임은 10글자 이하이어야 합니다.");
      return false;
    }

    // 비밀번호 유효성 검사
    const passwordRegex = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{10,16}/;
    if (!passwordRegex.test(formData.password)) {
      setError(
        "비밀번호는 10~16자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
      );
      return false;
    }
    if (formData.password !== formData.confirmPassword) {
      setError("비밀번호가 일치하지 않습니다.");
      return false;
    }

    // 전화번호 유효성 검사
    const phoneRegex = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
    if (!phoneRegex.test(formData.phoneNumber)) {
      setError("전화번호 형식이 올바르지 않습니다. 예시) 010-1234-5678");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      // Backend API 호출
      const response = await fetch("http://localhost:8082/user/signUp", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: formData.email,
          nickname: formData.nickname,
          password: formData.password,
          phoneNumber: formData.phoneNumber,
        }),
      });

      if (response.ok) {
        alert("회원가입이 완료되었습니다. 로그인해주세요.");
        navigate("/login");
      } else {
        const errorData = await response.json();
        setError(errorData.message || "회원가입에 실패했습니다.");
      }
    } catch (error) {
      setError("네트워크 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="py-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <Card>
            <Card.Body className="p-4">
              <h2 className="text-center mb-4">회원가입</h2>

              {error && <Alert variant="danger">{error}</Alert>}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>이메일 *</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="이메일을 입력하세요"
                    required
                  />
                  <Form.Text className="text-muted">
                    유효한 이메일 주소를 입력하세요.
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>닉네임 *</Form.Label>
                  <Form.Control
                    type="text"
                    name="nickname"
                    value={formData.nickname}
                    onChange={handleChange}
                    placeholder="닉네임을 입력하세요 (최대 10자, 한글/영어만)"
                    required
                    maxLength={10}
                  />
                  <Form.Text className="text-muted">
                    한글 또는 영어로 10자 이내로 입력하세요.
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>비밀번호 *</Form.Label>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="비밀번호를 입력하세요"
                    required
                    minLength={10}
                    maxLength={16}
                  />
                  <Form.Text className="text-muted">
                    10~16자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>비밀번호 확인 *</Form.Label>
                  <Form.Control
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="비밀번호를 다시 입력하세요"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>전화번호 *</Form.Label>
                  <Form.Control
                    type="tel"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    placeholder="전화번호를 입력하세요 (예: 010-1234-5678)"
                    required
                  />
                  <Form.Text className="text-muted">
                    하이픈(-)을 포함하여 입력하세요.
                  </Form.Text>
                </Form.Group>

                <Button
                  type="submit"
                  variant="primary"
                  className="w-100 mb-3"
                  disabled={loading}
                >
                  {loading ? "회원가입 중..." : "회원가입"}
                </Button>
              </Form>

              <div className="text-center">
                <span>이미 계정이 있으신가요? </span>
                <Link to="/login" className="text-decoration-none">
                  로그인
                </Link>
              </div>
            </Card.Body>
          </Card>
        </div>
      </div>
    </Container>
  );
};

export default SignupPage;
