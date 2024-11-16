package com.example.findblood;

public class DonateCenter {
    private String image;
    private String name;
    private String phone;
    private String address;

    public DonateCenter(String image, String name, String phone, String address) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
