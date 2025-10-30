package com.example.service;

import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@NullMarked
@Service
public class StoreService {

    private final List<Store> stores = createTestData();

    public List<Store> findStores() {
        return stores;
    }

    private List<Store> createTestData() {
        return List.of(
                new Store(1L, "Downtown Flagship", "Sarah Johnson", new BigDecimal("2850000"), 45,
                        40.7128, -74.0060, "New York", PerformanceRating.EXCELLENT),
                new Store(2L, "Westside Mall", "Michael Chen", new BigDecimal("1920000"), 32,
                        34.0522, -118.2437, "Los Angeles", PerformanceRating.GOOD),
                new Store(3L, "Lakefront Square", "Jennifer Davis", new BigDecimal("2100000"), 38,
                        41.8781, -87.6298, "Chicago", PerformanceRating.EXCELLENT),
                new Store(4L, "Sunrise Plaza", "Roberto Martinez", new BigDecimal("1450000"), 28,
                        25.7617, -80.1918, "Miami", PerformanceRating.GOOD),
                new Store(5L, "Tech Center", "Amanda Williams", new BigDecimal("3200000"), 52,
                        37.7749, -122.4194, "San Francisco", PerformanceRating.EXCELLENT),
                new Store(6L, "Historic District", "David Thompson", new BigDecimal("980000"), 22,
                        42.3601, -71.0589, "Boston", PerformanceRating.NEEDS_IMPROVEMENT),
                new Store(7L, "River Walk", "Lisa Anderson", new BigDecimal("1750000"), 30,
                        47.6062, -122.3321, "Seattle", PerformanceRating.GOOD),
                new Store(8L, "Desert Crossing", "James Wilson", new BigDecimal("1250000"), 24,
                        33.4484, -112.0740, "Phoenix", PerformanceRating.GOOD),
                new Store(9L, "Music Row", "Patricia Brown", new BigDecimal("1680000"), 29,
                        36.1627, -86.7816, "Nashville", PerformanceRating.GOOD),
                new Store(10L, "Harbor Point", "Christopher Lee", new BigDecimal("2200000"), 40,
                        32.7157, -117.1611, "San Diego", PerformanceRating.EXCELLENT),
                new Store(11L, "Mountain View", "Emily Rodriguez", new BigDecimal("1420000"), 26,
                        39.7392, -104.9903, "Denver", PerformanceRating.GOOD),
                new Store(12L, "Uptown Center", "Kevin Moore", new BigDecimal("890000"), 20,
                        35.2271, -80.8431, "Charlotte", PerformanceRating.NEEDS_IMPROVEMENT),
                new Store(13L, "Pearl District", "Maria Garcia", new BigDecimal("1950000"), 35,
                        45.5152, -122.6784, "Portland", PerformanceRating.EXCELLENT),
                new Store(14L, "Midtown Commons", "Thomas White", new BigDecimal("1100000"), 23,
                        33.7490, -84.3880, "Atlanta", PerformanceRating.NEEDS_IMPROVEMENT),
                new Store(15L, "Capitol Square", "Jessica Taylor", new BigDecimal("1580000"), 31,
                        38.9072, -77.0369, "Washington DC", PerformanceRating.GOOD),
                new Store(16L, "Strip District", "Daniel Martinez", new BigDecimal("1320000"), 25,
                        40.4406, -79.9959, "Pittsburgh", PerformanceRating.GOOD),
                new Store(17L, "Beale Street", "Nicole Johnson", new BigDecimal("1480000"), 27,
                        35.1495, -90.0490, "Memphis", PerformanceRating.GOOD),
                new Store(18L, "French Quarter", "William Davis", new BigDecimal("2450000"), 42,
                        29.9511, -90.0715, "New Orleans", PerformanceRating.EXCELLENT),
                new Store(19L, "Riverfront Plaza", "Sophia Anderson", new BigDecimal("1150000"), 24,
                        39.0997, -94.5786, "Kansas City", PerformanceRating.NEEDS_IMPROVEMENT),
                new Store(20L, "University District", "Ryan Thompson", new BigDecimal("1850000"), 33,
                        30.2672, -97.7431, "Austin", PerformanceRating.EXCELLENT)
        );
    }
}
