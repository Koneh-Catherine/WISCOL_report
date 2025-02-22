package com.mycompany.model;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.io.File;

public class Model_Menu {
    private String icon;
    private String name;
    private MenuType type;

    // Change this to your absolute resources directory
    private static final String IMAGE_DIRECTORY = "D:/projects/java/wiscol/WISCOL_report/wiscol_report_card_generator/src/main/resources/";

    public Model_Menu() {
    }

    public Model_Menu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }  

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }
    
    public Icon toIcon() {
        String imagePath = IMAGE_DIRECTORY + icon + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            return new ImageIcon(imagePath);
        } else {
            System.out.println("⚠️ Image not found: " + imagePath);
            return new ImageIcon(IMAGE_DIRECTORY + "default.png"); // Use a default image
        }
    }

    public static enum MenuType {
        TITLE, MENU, EMPTY
    }
}
