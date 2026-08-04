package com.shekhar.ecom_proj.controller;

import com.shekhar.ecom_proj.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final ProductService service;

    public CheckoutController(ProductService service) {
        this.service = service;
    }


    @PostMapping("/{id}")
    public ResponseEntity<String> buyProduct(
            @PathVariable Integer id,
            @RequestParam int quantity) {
//        System.out.println(quantity);


        try {

            service.buyProduct(id, quantity);

            return ResponseEntity.ok("Order Placed Successfully");

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}