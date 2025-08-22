/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lgvb.valoui;

/**
 *
 * @author giann
 */
import javax.swing.*;
import java.io.File;

public class Gun {

    private String name;
    private String type;
    private int price;
    private ImageIcon icon;

    public Gun(String name, String type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.icon = loadIcon();
    }

    // Default constructor
    public Gun() {
    }

    // Load gun icon from resources
    private ImageIcon loadIcon() {
        String path = "src/main/resources/guns/" + name + ".png";
        File file = new File(path);
        return new ImageIcon(path);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
