package com.system.dao;

import com.system.config.DBConnection;
import com.system.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // CREATE
    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (name, category, price, stock) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    p.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    // READ (all)
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, category, price, stock FROM products ORDER BY id";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    // READ (search)
    public List<Product> search(String keyword) throws SQLException {
        String sql = "SELECT id, name, category, price, stock FROM products " +
                     "WHERE name LIKE ? OR category LIKE ? ORDER BY id";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // UPDATE
    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE products SET name=?, category=?, price=?, stock=? WHERE id=?";
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("stock"));
    }
}