FROM node:lts as ui-build
WORKDIR /frontend
COPY ./package.json ./
RUN npm install
COPY ./ ./
RUN npm run build

FROM nginx:latest
WORKDIR /opt/www
COPY --from=ui-build /frontend/dist/ui/ ./

COPY nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
