// src/main/java/com/example/calm/model/ClientServiceType.java
package com.example.Project.Calm.model;

public enum ClientServiceType {
    SA("Self Assessment"),
    RI("R&D Tax Credits"),
    HR("HR & Employment"),
    LTD("Limited Company"),
    VAT("VAT Returns"),
    PAYROLL("Payroll Processing"),
    BOOKKEEPING("Bookkeeping"),
    ACCOUNTS("Year-End Accounts");

    private final String displayName;

    ClientServiceType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
