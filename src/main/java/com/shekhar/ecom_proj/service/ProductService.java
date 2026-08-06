package com.shekhar.ecom_proj.service;

import com.shekhar.ecom_proj.dto.CartItemDto;
import com.shekhar.ecom_proj.model.CartItems;
import com.shekhar.ecom_proj.model.Product;
import com.shekhar.ecom_proj.model.Users;
import com.shekhar.ecom_proj.repo.CartItemsRepository;
import com.shekhar.ecom_proj.repo.ProductRepo;
import com.shekhar.ecom_proj.repo.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {

    private final ProductRepo repo;
    private final UsersRepository usersRepository;
    private final CartItemsRepository cartItemsRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepo repo, UsersRepository usersRepository, CartItemsRepository cartItemsRepository, CloudinaryService cloudinaryService) {
        this.repo = repo;
        this.usersRepository = usersRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageData(imageFile.getBytes());
//        if (imageFile.getSize() > 5 * 1024 * 1024) { // 5 MB
//            throw new RuntimeException("Image size cannot exceed 5 MB");
//        }
        String imageUrl = cloudinaryService.uploadImage(imageFile);
        product.setImageUrl(imageUrl);


        return repo.save(product);

    }

//    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        product.setImageData(imageFile.getBytes());
//        return repo.save(product);
//    }

    public void deleteProduct(int productId) {
        repo.deleteById(productId);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
//
//    public Product updateProductWithOutImage(int id, Product product) {
//        return repo.save(product);
//    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        // 1. Fetch existing product to prevent losing data
        Product existingProduct = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
//        if (imageFile.getSize() > 5 * 1024 * 1024) { // 5 MB
//            throw new RuntimeException("Image size cannot exceed 5 MB");
//        }
        // 2. Update basic fields
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setAvailable(product.isAvailable());
        // ... update other fields as needed

        // 3. Handle Image Logic
        if (imageFile != null && !imageFile.isEmpty()) {
//            existingProduct.setImageName(imageFile.getOriginalFilename());
//            existingProduct.setImageType(imageFile.getContentType());
//            existingProduct.setImageData(imageFile.getBytes());
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            product.setImageUrl(imageUrl);

        }
        // If imageFile is empty, the existingProduct keeps its current image data

        return repo.save(existingProduct);
    }


    @Transactional
    public void buyProduct(Integer productId, int quantity) {

        int updatedRows = repo.decreaseStock(productId, quantity);

        if (updatedRows == 0) {
            throw new RuntimeException("Product is Out of Stock");
        }
    }


    @Transactional
    public List<CartItems> addCartItems(String email, List<CartItemDto> cartItemDtos) {

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItemsRepository.deleteByUserUserId(user.getUserId());
        List<CartItems> cartItems = cartItemDtos.stream()
                .map(dto -> {
                    CartItems item = new CartItems();
                    item.setUser(user);
                    item.setProductId(dto.getProductId());
                    item.setQuantity(dto.getQuantity());
                    return item;
                })
                .toList();

        return cartItemsRepository.saveAll(cartItems);
    }

    public List<CartItemDto> getAllCartItem(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Something went wrong"));
        int id = user.getUserId();
       return cartItemsRepository.getCartItembyId(id);
    }
}
