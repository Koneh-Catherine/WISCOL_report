
package com.mycompany.model;

import javax.swing.Icon;
import javax.swing.ImageIcon;
//import java.io.File;
import java.net.URL;

public class Model_Menu {
    private String icon;
    private String name;
    private MenuType type;

    // Resource folder inside src/main/resources
    private static final String IMAGE_FOLDER = "/images/";
    
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
    
    public Model_Menu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public Model_Menu() {
    }
    
    public Icon toIcon(){
         URL imageUrl = getClass().getResource(IMAGE_FOLDER + icon + ".png");

        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        } else {
            System.out.println("⚠️ Image not found: " + IMAGE_FOLDER + icon + ".png");
            return new ImageIcon(getClass().getResource(IMAGE_FOLDER + "default.png")); // Fallback image
        }
    }
    
    public static enum MenuType{
        TITLE, MENU, EMPTY
    }
}
