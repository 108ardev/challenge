# Lydia coding challenge

Thank you for applying to work in Lydia Technology.

As part of our interview process, we expect you to complete a coding challenge for us to understand your coding skills better. The challenge is a Java11 + Spring Boot project that uses PostgreSQL as the database.


# Question 1: Product Management

## Requirements

* Product adding, removing or updating services. 
* Product listing service which returns product name, description, remaining stock count and price.
* Payment service for the end user to buy their selected product.
* A product should not be sold for more than its stock.
* Customers paying for the same product at the same time should not buy the product if the stock is depleted. (i.e. if there are 2 stocks left and 3 customers pay at the same time, first 2 successful should buy the product and the 3rd one should fail with an appropriate message.)
* Test coverage for the implemented service should be above 80%. We expect both Integration and unit tests.
* Bonus: You can create a bank request and response for the payment step.


# Question 2 : Latency Management

Lydia Payment Gateway calls bank endpoints to process payments. Bank responses are persistent in the database. We expect you to simulate 100 customers calling the payment service.

```java
    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        //insert records
        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");
    }
```

In the payment simulation you will make, the bank response times in the simulation take ~5 seconds for some reason. Due to this delay, after a while the database connection problem is encountered and shows the error "Connection not available, request timed out after 30005ms." )

In this case, find a way to make the bank responses permanent in the database.

## Requirements

* DB connection pool should stay the same.
* BankService.java, PaymentServiceClients.java and classes should not be changed.
* In case of an error, there should not be any dirty data in the database.


# Bonus

A front end project developed with React js where you can connect to the services you wrote for product management from the front end and list, add, update and delete products.

(Note: Page layout and design are not important.)



