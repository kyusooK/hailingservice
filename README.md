# 

## Model
www.msaez.io/#/123912988/storming/hailingservice

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- user
- dispatch
- driver
- operationstatistics


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- user
```
 http :8088/users id="id"email="email"phoneNumber="phoneNumber"message="message"
```
- dispatch
```
 http :8088/operations id="id"passengerLocation="passengerLocation"destination="destination"OperationStatus = "OPERATED"UserId := '{"id": 0}'DriverId := '{"id": 0}'fee="fee"
 http :8088/matchings id="id"destination="destination"passengerLocation="passengerLocation"DriverId := '{"id": 0}'UserId := '{"id": 0}'latitude="latitude"longitude="longitude"estimatedTime="estimatedTime"estimatedDistance="estimatedDistance"
```
- driver
```
 http :8088/drivers id="id"email="email"driverLicenseNumber="driverLicenseNumber"isApproved="isApproved"isHailing="isHailing"driverLocation="driverLocation"operationRequestForm="operationRequestForm"operationInfo="operationInfo"
```
- operationstatistics
```
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
