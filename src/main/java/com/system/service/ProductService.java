package com.system.service;

import com.system.dao.ProductDAO;
import com.system.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDAO dao = new ProductDAO();

    /** Validates and inserts. Returns the generated id. */
    public int create(Product p) throws SQLException, IllegalArgumentException {
        validate(p);
        return dao.insert(p);
    }

    public List<Product> listAll() throws SQLException {
        return dao.findAll();
    }

    public List<Product> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return dao.findAll();
        return dao.search(keyword.trim());
    }

    public boolean update(Product p) throws SQLException, IllegalArgumentException {
        if (p.getId() <= 0) throw new IllegalArgumentException("Missing product id.");
        validate(p);
        return dao.update(p);
    }

    public boolean delete(int id) throws SQLException, IllegalArgumentException {
        if (id <= 0) throw new IllegalArgumentException("Missing product id.");
        return dao.delete(id);
    }

    private void validate(Product p) {
        if (p.getName() == null || p.getName().isBlank())
            throw new IllegalArgumentException("Name is required.");
        if (p.getCategory() == null || p.getCategory().isBlank())
            throw new IllegalArgumentException("Category is required.");
        if (p.getPrice() < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        if (p.getStock() < 0)
            throw new IllegalArgumentException("Stock cannot be negative.");
    }
}