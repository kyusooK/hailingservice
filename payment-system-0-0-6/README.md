
## Model
www.msaez.io/#/54785805/storming/payment-system

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

kafka 리스너
kafka-console-consumer --bootstrap-server localhost:9092 --topic paymentsystem --from-beginning
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- payment

## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- payment
- 결제요청
```
{
   price : 10000, // 결제 가격
   name : "상품명", // 결제 상품명
   buyerId : "buyerId", // 결제 구매자 아이디
   buyerName : "구매자명", // 결제 구매자 이름
   buyerTel: "010-1234-5678", // 결제 구매자 전화번호
   buyerEmail: "buyerEmail@example.com", // 결제 구매자 이메일
}
```
```
 http 호출
 http POST http://localhost:8088/payments price=10000 name='Mac Book' buyerId='LEE' buyerName='구매자명' buyerTel='010-1234-5678' buyerEmail='buyerEmail@example.com' paymentId='abc-123'
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by frontend
```
결제 요청 페이지 접근
http://localhost:8088/requestPayments/pay/1
```
