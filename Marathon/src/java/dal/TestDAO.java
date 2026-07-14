/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author matvu
 */
public class TestDAO extends DBContext{
    public String getConnection(){
        return connection.toString();
    }
}
