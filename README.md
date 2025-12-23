# Gestión Bancaria Reactiva (Spring WebFlux) — API REST

Java instalado (versión según pom.xml)

Maven Wrapper incluido (mvnw)

Comandos:

./mvnw clean package -DskipTests
./mvnw spring-boot:run


La API quedará disponible en:

http://localhost:8080

Endpoints

Base URL:

http://localhost:8080

Accounts

Tú confirmaste que manejas /api/accounts.

GET /api/accounts
Lista cuentas (reactivo: Flux).

GET /api/accounts/{accountId}
Consulta una cuenta por id (reactivo: Mono).

POST /api/accounts
Crea una cuenta.

PUT /api/accounts/{accountId}
Actualiza una cuenta.

DELETE /api/accounts/{accountId}
Elimina una cuenta (o la desactiva, según tu lógica).

Transactions

Confirmado por tu API: POST /api/transactions/transfer.

POST /api/transactions/transfer
Transfiere dinero de una cuenta origen a una cuenta destino.

Request (ejemplo):

{
  "originAccountId": 1,
  "destinationAccountId": 2,
  "amount": 50000
}


Validaciones típicas:

originAccountId no puede ser null.

destinationAccountId no puede ser null.

amount debe ser mayor a 0.

Ejemplos rápidos (cURL)
Listar cuentas
curl -X GET "http://localhost:8080/api/accounts"

Consultar cuenta por id
curl -X GET "http://localhost:8080/api/accounts/1"

Transferencia
curl -X POST "http://localhost:8080/api/transactions/transfer" \
  -H "Content-Type: application/json" \
  -d '{
    "originAccountId": 1,
    "destinationAccountId": 2,
    "amount": 50000
  }'

