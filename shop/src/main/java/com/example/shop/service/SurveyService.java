package com.example.shop.service;



import com.example.shop.entity.Purchase;
import com.example.shop.entity.Product;
import com.example.shop.entity.Survey;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.PurchaseRepository;
import com.example.shop.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    public List<Survey> generateSurveyReport() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::calculateProfitAndLoss).collect(Collectors.toList());
    }

    private Survey calculateProfitAndLoss(Product product) {
        BigDecimal totalPurchases = purchaseRepository.findAll().stream()
                .filter(purchase -> purchase.getProduct().equals(product))
                .map(Purchase::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal costPrice = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
        BigDecimal profit = totalPurchases.subtract(costPrice);
        BigDecimal loss = (profit.compareTo(BigDecimal.ZERO) < 0) ? profit.negate() : BigDecimal.ZERO;

        Survey survey = new Survey();
        survey.setProduct(product);
        survey.setProfit(profit);
        survey.setLoss(loss);

        return surveyRepository.save(survey);
    }
}
