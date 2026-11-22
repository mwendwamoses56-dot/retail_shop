package com.system.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {

    private final IntegerProperty id       = new SimpleIntegerProperty();
    private final StringProperty  name     = new SimpleStringProperty();
    private final StringProperty  category = new SimpleStringProperty();
    private final DoubleProperty  price    = new SimpleDoubleProperty();
    private final IntegerProperty stock    = new SimpleIntegerProperty();

    public Product() { }

    public Product(int id, String name, String category, double price, int stock) {
        this.id.set(id);
        this.name.set(name);
        this.category.set(category);
        this.price.set(price);
        this.stock.set(stock);
    }

    public IntegerProperty idProperty()       { return id; }
    public StringProperty  nameProperty()     { return name; }
    public StringProperty  categoryProperty() { return category; }
    public DoubleProperty  priceProperty()    { return price; }
    public IntegerProperty stockProperty()    { return stock; }

    public int    getId()       { return id.get(); }
    public String getName()     { return name.get(); }
    public String getCategory() { return category.get(); }
    public double getPrice()    { return price.get(); }
    public int    getStock()    { return stock.get(); }

    public void setId(int v)          { id.set(v); }
    public void setName(String v)     { name.set(v); }
    public void setCategory(String v) { category.set(v); }
    public void setPrice(double v)    { price.set(v); }
    public void setStock(int v)       { stock.set(v); }

    @Override
    public String toString() {
        return name.get() + " (" + category.get() + ")";
    }
}