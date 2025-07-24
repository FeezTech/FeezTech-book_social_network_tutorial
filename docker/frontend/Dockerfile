# Build stage
FROM node:22.16.0 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

# Copy build output
COPY --from=build-stage /app/dist/book-network-ui/browser /usr/share/nginx/html

# Rename index.csr.html to index.html, only if it exists
RUN if [ -f /usr/share/nginx/html/index.csr.html ]; then mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html; fi

EXPOSE 80

#Start Nginx in foreground to prevent container exit
CMD ["nginx", "-g", "daemon off;"]