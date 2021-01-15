package com.example.coloridentifierapplication.ColorIdentity;

public class Color {

    public String id, name, hex, rgb;
    public int r,g,b;

    public Color(){

    }

    public Color(String name, int r, int g, int b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color(String name, String rgb, String hex) {
        this.name = name;
        this.rgb = rgb;
        this.hex = hex;
    }

    public Color(String id, String name, String rgb, String hex) {
        this.id = id;
        this.name = name;
        this.rgb = rgb;
        this.hex = hex;
    }

    public Color(String id) {
        this.id = id;
    }

    public int computeMSE(int pixR, int pixG, int pixB) {
        return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}
