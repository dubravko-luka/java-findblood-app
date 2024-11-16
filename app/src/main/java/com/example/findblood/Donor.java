package com.example.findblood;

public class Donor {
    private String name;
    private String contact;
    private String age;
    private String bloodType;

    public Donor(String name, String contact, String age, String bloodType) {
        this.name = name;
        this.contact = contact;
        this.age = age;
        this.bloodType = bloodType;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getAge() {
        return age;
    }

    public String getBloodType() {
        return bloodType;
    }
}
