# ============================================================
# Stage 1: Build frontend
# ============================================================
FROM node:22-alpine AS build

WORKDIR /app

# Copy dependency files first
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build production frontend
RUN npm run build


# ============================================================
# Stage 2: Serve frontend
# ============================================================
FROM nginx:alpine

# Remove default Nginx page
RUN rm -rf /usr/share/nginx/html/*

# Copy built frontend
COPY --from=build /app/dist /usr/share/nginx/html

# Frontend HTTP port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]