# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

- **Start development server**: `npm run dev`
- **Build for production**: `npm run build` (runs TypeScript compilation then Vite build)
- **Lint code**: `npm run lint`
- **Preview production build**: `npm run preview`

## Project Architecture

This is a React e-commerce application built with:
- **React 19** with TypeScript
- **Vite** for build tooling and development server
- **React Router DOM** for client-side routing
- **Bootstrap 5** with React Bootstrap components for UI
- **React Context API** for state management (Auth and Cart)

### Key Architecture Patterns

**Context-based State Management**:
- `AuthContext` (src/contexts/AuthContext.tsx): Manages authentication state with simple boolean flag
- `CartContext` (src/contexts/CartContext.tsx): Manages shopping cart with add/remove/clear functionality

**Data Structure**:
- Static product data in `src/data/products.ts` with Korean product names
- Categories and brands defined in separate data files
- Product interface includes: id, name, price, image, category, brand

**Routing Structure**:
- `/` - Product listing (all products)
- `/products/:category` - Filtered by category
- `/brand/:brand` - Filtered by brand
- `/product/:id` - Product detail page
- `/cart` - Shopping cart page
- `/orders` - Order history page

**Component Organization**:
- `src/components/` - Reusable components (Header)
- `src/pages/` - Route-level page components
- `src/contexts/` - React Context providers
- `src/data/` - Static data definitions

### Korean Language Support

The application uses Korean text for product names, categories, and user messages. All alert messages and UI text are in Korean.

### Development Notes

- Uses placeholder images (`https://via.placeholder.com/300`) for all products
- Simple authentication (no actual backend integration)
- Cart state is not persisted (resets on page refresh)
- No test suite is currently configured