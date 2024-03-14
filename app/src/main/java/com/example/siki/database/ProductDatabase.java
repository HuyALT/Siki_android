package com.example.siki.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.siki.model.Product;
import com.example.siki.model.ProductPrice;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {
    private SQLiteDatabase db;
    private SikiDatabaseHelper dbHelper;
    public ProductDatabase(Context context) {
        dbHelper = new SikiDatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Product> readDb() {
        String sql = "Select * from Product";
        List<Product> listProduct = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(0));
                product.setName(cursor.getString(1));
                product.setImagePath(cursor.getString(2));
                ProductPrice productPrice = new ProductPrice();
                productPrice.setPrice(cursor.getDouble(3));
                product.setProductPrice(productPrice);
                listProduct.add(product);
            } while (cursor.moveToNext());
        }
        return listProduct;
    }

    public long addProduct(Product product) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("id", product.getId());
            values.put("name", product.getName());
            values.put("imagePath", product.getImagePath());
            values.put("productPrice", product.getProductPrice().getPrice());

            id = db.insert("Product", null, values);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        return id;
    }

    public int deleteProduct(Product product) {
        int rowsAffected = -1;
        try {
            rowsAffected = db.delete("Product", "id=?", new String[]{String.valueOf(product.getId())});
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public int updateProduct(Product product) {
        int rowsAffected = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("id", product.getId());
            values.put("name", product.getName());
            values.put("imagePath", product.getImagePath());
            values.put("productPrice", product.getProductPrice().getPrice());

            rowsAffected = db.update("Product", values, "id=?", new String[]{String.valueOf(product.getId())});
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        return rowsAffected;
    }
}