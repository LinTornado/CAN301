package com.example.pair_click.bean;

import android.graphics.drawable.Drawable;

import java.util.Objects;

public class AppBean {
    private String name;
    private Drawable icon;
    private String packageName;
    private boolean isChecked = false;

    public AppBean(String name, Drawable icon, String packageName) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppBean appBean = (AppBean) o;
        return Objects.equals(name, appBean.name) && Objects.equals(packageName, appBean.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, packageName);
    }

    @Override
    public String toString() {
        return "AppBean{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
