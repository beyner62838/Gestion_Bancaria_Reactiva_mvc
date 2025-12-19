package com.example.Prueba_Tecnica.Config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.Prueba_Tecnica.Repository")
public class R2dbcConfig {

    @Bean
    public R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(R2dbcTransactionManager tm) {
        return TransactionalOperator.create(tm);
    }
}
