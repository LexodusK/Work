package com.example.fyp;

import android.widget.ImageView;

public class RowItem {
    private boolean isSelected;
    private String checkbox;
    private int Icon;
    private String Title;

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }
}
