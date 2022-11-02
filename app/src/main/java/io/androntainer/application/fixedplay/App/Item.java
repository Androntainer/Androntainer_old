package io.androntainer.application.fixedplay.App;

import android.graphics.drawable.Drawable;

public class Item {

    private final String name;
    private final String packageName;
    private final String className;
    private final Drawable appIcon;

    public Item(String name, String packageName, String className, Drawable appIcon) {
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.appIcon = appIcon;
    }

    public String getClassName() {
        return className;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }
}