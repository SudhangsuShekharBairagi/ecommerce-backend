package com.shekhar.ecom_proj.controller;

import com.shekhar.ecom_proj.dto.CartItemDto;
import com.shekhar.ecom_proj.model.Product;
import com.shekhar.ecom_proj.model.Users;
import com.shekhar.ecom_proj.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin

@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if(product != null){
            return  new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
      try{
          Product product1 = service.addProduct(product, imageFile);
          return new ResponseEntity<>(product1, HttpStatus.CREATED);
      }catch (Exception e){
          return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<?> getImageByProductId(@PathVariable int productId){
       try {
           Product product = service.getProductById(productId);
           byte[] imageFile = product.getImageData();
           return  ResponseEntity.ok()
                   .body(imageFile);
       } catch (Exception e) {
           return new ResponseEntity<>(
                   e.getMessage(),
                   HttpStatus.NOT_FOUND
           );
       }
    }

//    @PutMapping("/product/{id}")
//    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,
//                                                @RequestPart MultipartFile imageFile){
//
//        Product product1 = null;
//        try {
//
//            if(imageFile.isEmpty()){
//                product1 = service.updateProductWithOutImage(id, product);
//            }else{
//                product1 = service.updateProduct(id, product, imageFile);
//            }
////            System.out.println(product1);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        if(product1 != null){
//            return  new ResponseEntity<>("Updated", HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
//        }
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,
                                                @RequestPart Product product,
                                                @RequestPart(required = false) MultipartFile imageFile) {
        try {
            Product updatedProduct = service.updateProduct(id, product, imageFile);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Image upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String > deleteProduct(@PathVariable int productId){
        Product product = service.getProductById(productId);

        if(product != null){
            service.deleteProduct(productId);
            return  new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @PostMapping("/cartItem")
    public ResponseEntity<?> cartItem(
            Authentication authentication,
            @RequestBody List<CartItemDto> cartItems
    ) {
        try {
            String email = authentication.getName();
            service.addCartItems(email, cartItems);
            return ResponseEntity.ok("Cart Items Saved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("cartItem")
    public ResponseEntity<?> getcartItem(Authentication authentication){
        try {
            String email = authentication.getName();
            return ResponseEntity.ok(service.getAllCartItem(email));
        }catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


}

