package com.example.demo.Duel.Rows;


public enum RowStatus {
    Rain("Rain"),
    NoStatus("");
    private final String rowStatusName;

    RowStatus(String rowStatusName) {
        this.rowStatusName = rowStatusName;
    }

    @Override
    public String toString() {
        return rowStatusName;
    }
}
