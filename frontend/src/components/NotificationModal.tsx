import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import { FaCheckCircle, FaInfoCircle, FaExclamationCircle, FaTimes } from 'react-icons/fa';

interface NotificationModalProps {
  show: boolean;
  onHide: () => void;
  type: 'success' | 'info' | 'warning' | 'error';
  title: string;
  message: string;
  autoClose?: boolean;
  autoCloseDelay?: number;
}

const NotificationModal: React.FC<NotificationModalProps> = ({
  show,
  onHide,
  type,
  title,
  message,
  autoClose = true,
  autoCloseDelay = 2000
}) => {
  React.useEffect(() => {
    if (show && autoClose) {
      const timer = setTimeout(() => {
        onHide();
      }, autoCloseDelay);

      return () => clearTimeout(timer);
    }
  }, [show, autoClose, autoCloseDelay, onHide]);

  const getIcon = () => {
    switch (type) {
      case 'success':
        return <FaCheckCircle size={24} className="text-success" />;
      case 'info':
        return <FaInfoCircle size={24} className="text-info" />;
      case 'warning':
        return <FaExclamationCircle size={24} className="text-warning" />;
      case 'error':
        return <FaTimes size={24} className="text-danger" />;
      default:
        return <FaInfoCircle size={24} className="text-info" />;
    }
  };

  const getVariant = () => {
    switch (type) {
      case 'success':
        return 'success';
      case 'info':
        return 'info';
      case 'warning':
        return 'warning';
      case 'error':
        return 'danger';
      default:
        return 'info';
    }
  };

  return (
    <Modal
      show={show}
      onHide={onHide}
      centered
      className="notification-modal"
      backdrop={false}
      style={{ zIndex: 9999 }}
    >
      <div 
        className="modal-content"
        style={{
          border: 'none',
          borderRadius: '20px',
          background: 'linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)',
          boxShadow: '0 20px 40px rgba(0,0,0,0.15)',
          overflow: 'hidden',
          animation: 'slideInDown 0.5s ease-out'
        }}
      >
        <Modal.Header 
          className="border-0 pb-0"
          style={{
            background: 'transparent',
            padding: '20px 25px 10px 25px'
          }}
        >
          <div className="d-flex align-items-center w-100">
            <div className="me-3" style={{ 
              padding: '12px',
              borderRadius: '50%',
              background: `var(--bs-${getVariant()}-rgb, 13, 110, 253)`,
              background: type === 'success' ? 'rgba(25, 135, 84, 0.1)' :
                         type === 'info' ? 'rgba(13, 202, 240, 0.1)' :
                         type === 'warning' ? 'rgba(255, 193, 7, 0.1)' :
                         'rgba(220, 53, 69, 0.1)'
            }}>
              {getIcon()}
            </div>
            <Modal.Title 
              className="fw-bold mb-0"
              style={{
                fontSize: '1.3rem',
                color: '#2c3e50'
              }}
            >
              {title}
            </Modal.Title>
            <Button
              variant="link"
              onClick={onHide}
              className="ms-auto p-0 border-0"
              style={{
                color: '#6c757d',
                fontSize: '1.5rem',
                textDecoration: 'none',
                lineHeight: 1
              }}
            >
              <FaTimes />
            </Button>
          </div>
        </Modal.Header>
        
        <Modal.Body style={{ padding: '10px 25px 25px 25px' }}>
          <p 
            className="mb-0"
            style={{
              fontSize: '1rem',
              color: '#495057',
              lineHeight: '1.6',
              marginLeft: '48px'
            }}
          >
            {message}
          </p>
        </Modal.Body>

        {/* 자동 닫힘 진행 바 */}
        {autoClose && (
          <div 
            style={{
              height: '3px',
              background: 'rgba(0,0,0,0.1)',
              position: 'relative',
              overflow: 'hidden'
            }}
          >
            <div
              style={{
                height: '100%',
                background: type === 'success' ? '#28a745' :
                           type === 'info' ? '#17a2b8' :
                           type === 'warning' ? '#ffc107' :
                           '#dc3545',
                width: '100%',
                animation: `progressBar ${autoCloseDelay}ms linear`
              }}
            />
          </div>
        )}
      </div>

      <style jsx>{`
        @keyframes slideInDown {
          from {
            opacity: 0;
            transform: translate3d(0, -100%, 0);
          }
          to {
            opacity: 1;
            transform: translate3d(0, 0, 0);
          }
        }

        @keyframes progressBar {
          from {
            width: 100%;
          }
          to {
            width: 0%;
          }
        }

        .notification-modal .modal-dialog {
          margin-top: 20px;
        }
      `}</style>
    </Modal>
  );
};

export default NotificationModal;