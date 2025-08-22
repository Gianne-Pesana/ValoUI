/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lgvb.valoui;

import javax.swing.*;
import java.io.*;

/**
 *
 * @author giann
 */
public class Animal {

    private String name;
    private String breed;
    private String species;
    private int money;
    private ImageIcon icon;

    public Animal(String breed, String name, String species, int money) {
        this.breed = breed;
        this.name = name;
        this.species = species;
        this.money = money;
        this.icon = loadIcon();
    }

    // Default constructor
    public Animal() {
    }

    // Load animal icon from resources
    private ImageIcon loadIcon() {
        String path = "src/main/resources/animals/" + species.toLowerCase() + ".jpg";
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
