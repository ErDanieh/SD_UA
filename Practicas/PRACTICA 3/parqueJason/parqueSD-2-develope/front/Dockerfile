# pull the base image
FROM node:15.7.0-alpine

# set the working direction
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY package.json ./

COPY package-lock.json ./

RUN npm install --silence

# add app
COPY . ./

# start app
CMD ["npm", "start"]
