package com.legmod.ddd.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.stream.schema.registry.client.EnableSchemaRegistryClient;

@SpringBootApplication
//@EnableSchemaRegistryClient
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

}
