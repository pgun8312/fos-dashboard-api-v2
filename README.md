# Food Ordering System API

This project contains three microservices: `order-service`, `product-service`, and `user-service`.

## Prerequisites

Before you begin, ensure you have the following installed:

- Docker: [Install Docker](https://docs.docker.com/get-docker/)
- Docker Compose: [Install Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/your-repo.git
   cd your-repo
Environment Variables

Create a .env file in the root of the project with the necessary environment variables. Sample content:

env
Copy code
# Database Configuration
ORDER_DB_URL=your-db-host-url
ORDER_DB_USERNAME=your-db-username
ORDER_DB_PASSWORD=your-password

PRODUCT_DB_URL=your-db-host-url
PRODUCT_DB_USERNAME=your-db-username
PRODUCT_DB_PASSWORD=your-password

USER_DB_URL=your-db-host-url
USER_DB_USERNAME=your-db-username
USER_DB_PASSWORD=your-password

# HOST URL , change this to mahcine ipv4 address
HOST_URL=your-machine-ipv4-address

# SONAR_HOST_URL=http://your-dynamic-hostname:8090, host with frontend
SONAR_HOST_URL=${HOST_URL}:8090
SONAR_LOGIN=your-sonar-login-user-name
SONAR_PASSWORD=your-sonar-password

Note: This file is not versioned in the repository for security reasons.

## Build and Start Microservices

Run the following commands to build and start the microservices:

docker-compose up --build

This command will build the Docker images for each microservice and start the containers.

## Access Microservices

The microservices should be accessible on the following ports:

order-service: http://localhost:8081
product-service: http://localhost:8080
user-service: http://localhost:8082
Stopping the Application

# To stop the application and remove the containers, run:

docker-compose down

# TO  rebuild the application and run

docker-compose up --build

# To run mvn Commands:

cd /app
mvn <your-command>

Replace <your-command> with the specific command you want to run.

Example:
mvn sonar:sonar

# To run all the TESTs

mvn clean:install

# Generate Test report 

mvn sonar:sonar

# To run tests
To run tests, navigate to the respective microservice directories and testing command

cd order-service
mvn test

cd ../product-service
mvn test

cd ../user-service
mvn test


# To run sonar
Running SonarQube Analysis
Update the credentials in the .env file and run the following command to analyze your project:

mvn sonar:sonar


Remember to replace placeholders like `your-username`, `your-repo`, `your-db-host-url`, `your-db-username`, `your-password`, `your-machine-ipv4-address`, `your-sonar-login-user-name`, and `your-sonar-password` with your actual values.


