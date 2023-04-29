package Model;

import java.io.Serializable;

public class Account implements Serializable {
    private String fullName;
    private String phone;
    private boolean gender;
    private String username;
    private String password;
    private String address;
    public Account() {
    }

    public Account(String fullName, boolean gender, String username, String password,String phone, String address) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
