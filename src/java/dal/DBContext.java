/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author anhdu
 */
public class DBContext {
protected Connection connection;

    public DBContext() {
        try {
            // Thay đổi URL, Username, Password phù hợp với DB của bạn
            String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=MarathonDB;encrypt=true;trustServerCertificate=true;";
            String username = "sa";
            String password = "123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
    try {
        DBContext db = new DBContext();
        if (db.connection != null && !db.connection.isClosed()) {
            System.out.println("=== KẾT NỐI DATABASE THÀNH CÔNG!!! ===");
            
            // Chạy thử một câu lệnh lấy dữ liệu để chắc chắn hơn
            java.sql.DatabaseMetaData meta = db.connection.getMetaData();
            System.out.println("Tên Database: " + meta.getDatabaseProductName());
            System.out.println("Phiên bản: " + meta.getDatabaseProductVersion());
        } else {
            System.out.println("=== KẾT NỐI THẤT BẠI! Hãy kiểm tra lại Url, User hoặc Pass ===");
        }
    } catch (Exception e) {
        System.out.println("=== LỖI KẾT NỐI: ===");
        e.printStackTrace();
    }
}
}

