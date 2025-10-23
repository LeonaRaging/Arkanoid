package com.arkanoid.Number_and_string_display;

import javafx.scene.layout.AnchorPane;

public class StringDisplay {
    private String variable;
    private Letter[] showString;
    int x, y;
    int spacing = 8;

    public StringDisplay(String variable,int x,int y) {
        this.variable = variable;
        this.x = x;
        this.y = y;
        this.showString = new Letter[variable.length()];
        for (int i = 0; i < variable.length(); i++) {
            showString[i] = new Letter(x + i * spacing, y, 8, 8, variable.charAt(i));
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        for (int i = 0; i < variable.length(); i++) {
            showString[i] = new Letter(x + i * spacing, y, 8, 8, variable.charAt(i));
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        for (int i = 0; i < variable.length(); i++) {
            showString[i] = new Letter(x + i * spacing, y, 8, 8, variable.charAt(i));
        }
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
        for (int i = 0; i < variable.length(); i++) {
            showString[i] = new Letter(x + i * spacing, y, 8, 8, variable.charAt(i));
        }
    }

    public Letter[] getShowString() {
        return showString;
    }

    public void setShowString(Letter[] showString) {
        this.showString = showString;
    }

    public void show(AnchorPane scene) {
        for (int i = 0; i < variable.length(); i++) {
            showString[i].display(scene);
        }
    }

    public void clear(AnchorPane scene) {
        for (int i = 0; i < variable.length(); i++) {
            showString[i].undisplay(scene);
        }
    }
}
