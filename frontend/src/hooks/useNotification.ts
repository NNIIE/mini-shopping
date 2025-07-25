import { useState, useCallback } from 'react';

interface NotificationState {
  show: boolean;
  type: 'success' | 'info' | 'warning' | 'error';
  title: string;
  message: string;
}

export const useNotification = () => {
  const [notification, setNotification] = useState<NotificationState>({
    show: false,
    type: 'info',
    title: '',
    message: ''
  });

  const showNotification = useCallback((
    type: 'success' | 'info' | 'warning' | 'error',
    title: string,
    message: string
  ) => {
    setNotification({
      show: true,
      type,
      title,
      message
    });
  }, []);

  const hideNotification = useCallback(() => {
    setNotification(prev => ({ ...prev, show: false }));
  }, []);

  const showSuccess = useCallback((title: string, message: string) => {
    showNotification('success', title, message);
  }, [showNotification]);

  const showError = useCallback((title: string, message: string) => {
    showNotification('error', title, message);
  }, [showNotification]);

  const showWarning = useCallback((title: string, message: string) => {
    showNotification('warning', title, message);
  }, [showNotification]);

  const showInfo = useCallback((title: string, message: string) => {
    showNotification('info', title, message);
  }, [showNotification]);

  return {
    notification,
    showNotification,
    hideNotification,
    showSuccess,
    showError,
    showWarning,
    showInfo
  };
};