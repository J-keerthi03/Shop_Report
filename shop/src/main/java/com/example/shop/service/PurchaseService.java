package com.example.shop.service;
import com.example.shop.Dto.PurchaseDTO;
import com.example.shop.entity.Product;
import com.example.shop.entity.Purchase;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PurchaseService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase purchaseProduct(PurchaseDTO purchaseDTO) {
        Optional<Product> optionalProduct = productRepository.findById(purchaseDTO.getProductId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getQuantity() >= purchaseDTO.getQuantity()) {
                // Calculate the total amount
                BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(purchaseDTO.getQuantity()));

                // Update product quantity
                product.setQuantity(product.getQuantity() - purchaseDTO.getQuantity());
                productRepository.save(product);

                // Create and save purchase
                Purchase purchase = new Purchase();
                purchase.setProduct(product);
                purchase.setQuantity(purchaseDTO.getQuantity());
                purchase.setTotalAmount(totalAmount);
                purchase.setPurchaseDate(java.time.LocalDateTime.now());

                return purchaseRepository.save(purchase);
            } else {
                throw new RuntimeException("Not enough stock available");
            }
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}
