package com.example.service;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

@NullMarked
public record Store(long id, String storeName, String manager, BigDecimal revenue, int employeeCount,
                    double latitude, double longitude, String city, PerformanceRating performanceRating) {
}
