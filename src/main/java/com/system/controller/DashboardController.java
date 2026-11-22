package com.system.controller;

import com.system.model.Product;
import com.system.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class DashboardController {

    // ---------- FXML ----------
    @FXML private TextField  nameField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField  priceField;
    @FXML private TextField  stockField;
    @FXML private TextField  searchField;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String>  colName;
    @FXML private TableColumn<Product, String>  colCategory;
    @FXML private TableColumn<Product, Double>  colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    private final ProductService service = new ProductService();
    private final ObservableList<Product> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryCombo.setItems(FXCollections.observableArrayList(
                "Salads", "Burgers", "Mains", "Drinks", "Desserts"));

        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colCategory.setCellValueFactory(c -> c.getValue().categoryProperty());
        colPrice.setCellValueFactory(c -> c.getValue().priceProperty().asObject());
        colStock.setCellValueFactory(c -> c.getValue().stockProperty().asObject());

        productTable.setItems(data);

        productTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) populate(sel); });

        reload();
    }

    // ---------- CRUD handlers ----------

    @FXML
    private void handleAdd() {
        Product p = fromForm(0);
        if (p == null) return;
        try {
            int id = service.create(p);
            p.setId(id);
            data.add(p);
            clearForm();
        } catch (IllegalArgumentException ex) {
            warn(ex.getMessage());
        } catch (SQLException ex) {
            error("Database error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Product sel = productTable.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Select a row to update."); return; }
        Product p = fromForm(sel.getId());
        if (p == null) return;
        try {
            service.update(p);
            // refresh the observable item in place
            int idx = data.indexOf(sel);
            data.set(idx, p);
            productTable.getSelectionModel().select(idx);
        } catch (IllegalArgumentException ex) {
            warn(ex.getMessage());
        } catch (SQLException ex) {
            error("Database error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Product sel = productTable.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Select a row to delete."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + sel.getName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.YES) return;
            try {
                service.delete(sel.getId());
                data.remove(sel);
                clearForm();
            } catch (SQLException ex) {
                error("Database error: " + ex.getMessage());
            }
        });
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        reload();
    }

    @FXML
    private void handleSearch() {
        String kw = searchField.getText();
        try {
            data.setAll(service.search(kw));
        } catch (SQLException ex) {
            error("Database error: " + ex.getMessage());
        }
    }

    // ---------- helpers ----------

    private void reload() {
        try {
            data.setAll(service.listAll());
        } catch (SQLException ex) {
            error("Cannot load products: " + ex.getMessage());
        }
    }

    private Product fromForm(int id) {
        try {
            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            String cat  = categoryCombo.getValue();
            String pTxt = priceField.getText() == null ? "" : priceField.getText().trim();
            String sTxt = stockField.getText() == null ? "" : stockField.getText().trim();

            if (name.isEmpty() || cat == null || pTxt.isEmpty() || sTxt.isEmpty()) {
                warn("All fields are required.");
                return null;
            }
            double price = Double.parseDouble(pTxt);
            int    stock = Integer.parseInt(sTxt);
            return new Product(id, name, cat, price, stock);
        } catch (NumberFormatException ex) {
            warn("Price must be a number and stock must be an integer.");
            return null;
        }
    }

    private void populate(Product p) {
        nameField.setText(p.getName());
        categoryCombo.setValue(p.getCategory());
        priceField.setText(String.valueOf(p.getPrice()));
        stockField.setText(String.valueOf(p.getStock()));
    }

    private void clearForm() {
        nameField.clear();
        categoryCombo.setValue(null);
        priceField.clear();
        stockField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    private void warn(String msg) { alert(Alert.AlertType.WARNING, "Validation", msg); }
    private void error(String msg) { alert(Alert.AlertType.ERROR, "Error", msg); }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}