package com.example.shop.controller;

import com.example.shop.Dto.PurchaseDTO;
import com.example.shop.entity.Purchase;
import com.example.shop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Purchase> purchaseProduct(@RequestBody PurchaseDTO purchaseDTO) {
        return ResponseEntity.ok(purchaseService.purchaseProduct(purchaseDTO));
    }
}
