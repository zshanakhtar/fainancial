package com.fainancial.enums;

public enum OverallStatus {
    PROCESSING("In Progress"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    FAILURE("Failure");

    private final String status;

    OverallStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
