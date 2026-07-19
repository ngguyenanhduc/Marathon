package model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String userName;
    private String password;
    private int roleId;
    private String fullName;
    private String email;
    private String phone;
    private String status; // ACTIVE, INACTIVE, BANNED
    private Timestamp createdAt;

    public User() {}

    public User(int userId, String userName, String password, int roleId, String fullName, String email, String phone, String status, Timestamp createdAt) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter & Setter đầy đủ cho khớp với DB của bạn
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
