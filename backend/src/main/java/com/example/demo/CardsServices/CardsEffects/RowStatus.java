package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.Duel.Row;

public enum RowStatus {
    Rain("Rain"),
    NoStatus("");
    private final String rowStatusName;

    private RowStatus(String rowStatusName) {
        this.rowStatusName = rowStatusName;
    }

    @Override
    public String toString() {
        return rowStatusName;
    }
}
