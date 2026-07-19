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
            // Cấu hình kết nối SQL Server của bạn
            String url = "jdbc:sqlserver://localhost:1433;databaseName=MarathonDB;encrypt=true;trustServerCertificate=true;";
            String user = "sa";
            String pass = "123"; // Đổi password của bạn
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
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

