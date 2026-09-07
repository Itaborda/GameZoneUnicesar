package com.gamezone.model;

public class Seller extends Person{
    private String codeEmployee;
    private String shift;

    public Seller(String id, String name, String phone, String codeEmployee, String shift) {
        super(id, name, phone);
        this.codeEmployee = codeEmployee;
        this.shift = shift;
    }

    public String getCodeEmployee() {
        return codeEmployee;
    }

    public void setCodeEmployee(String codeEmployee) {
        this.codeEmployee = codeEmployee;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String getRole() {
        return "Seller";
    }
}
