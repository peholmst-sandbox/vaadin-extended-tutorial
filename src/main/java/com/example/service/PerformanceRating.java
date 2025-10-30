package com.example.service;

public enum PerformanceRating {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    NEEDS_IMPROVEMENT("Needs Improvement");

    private final String displayName;

    PerformanceRating(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
