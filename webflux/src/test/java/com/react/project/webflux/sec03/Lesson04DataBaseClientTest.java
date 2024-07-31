package com.react.project.webflux.sec03;


import com.react.project.webflux.sec02.dto.OrderDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;


public class Lesson04DataBaseClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lesson04DataBaseClientTest.class);

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    public void getProductsOrderedByCustomer() {
        String query = """
                SELECT
                    co.order_id,
                    c.name AS customer_name,
                    p.description AS product_name,
                    co.amount,
                    co.order_date
                FROM
                    customer c
                INNER JOIN customer_order co ON c.id = co.customer_id
                INNER JOIN product p ON p.id = co.product_id
                WHERE
                    p.description = :description
                ORDER BY co.amount DESC        
                    """;
        this.databaseClient.sql(query)
                .bind("description", "iphone 18")
                .mapProperties(OrderDetail.class)
                .all()
                .doOnNext(d -> log.info("{}", d))
                .as(StepVerifier::create)
                .assertNext(d -> Assertions.assertEquals(850, d.amount()))
                .assertNext(d -> Assertions.assertEquals(750, d.amount()))
                .expectComplete()
                .verify();
    }
}
