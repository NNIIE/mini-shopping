import React, { createContext, useContext, ReactNode } from 'react';
import { useNotification } from '../hooks/useNotification';
import NotificationModal from '../components/NotificationModal';

interface NotificationContextType {
  showSuccess: (title: string, message: string) => void;
  showError: (title: string, message: string) => void;
  showWarning: (title: string, message: string) => void;
  showInfo: (title: string, message: string) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export const NotificationProvider = ({ children }: { children: ReactNode }) => {
  const { notification, hideNotification, showSuccess, showError, showWarning, showInfo } = useNotification();

  const contextValue = {
    showSuccess,
    showError,
    showWarning,
    showInfo
  };

  return (
    <NotificationContext.Provider value={contextValue}>
      {children}
      <NotificationModal
        show={notification.show}
        onHide={hideNotification}
        type={notification.type}
        title={notification.title}
        message={notification.message}
      />
    </NotificationContext.Provider>
  );
};

export const useNotificationContext = () => {
  const context = useContext(NotificationContext);
  if (context === undefined) {
    throw new Error('useNotificationContext must be used within a NotificationProvider');
  }
  return context;
};