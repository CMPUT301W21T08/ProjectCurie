package com.example.projectcurie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Scannable implements Serializable {

    abstract boolean submitTrial(@NotNull Context context);

    abstract boolean isEqual(String code);

    abstract Bitmap show();

    abstract String getCode();

    abstract String getOutcome();

    abstract String getTitle();

    abstract String getType();

    public Bitmap printableBitmap() {
        int borderSize = 800;
        Bitmap bitmap = show();
        Bitmap bmpWithBorder = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);
        return bmpWithBorder;
    }


}
