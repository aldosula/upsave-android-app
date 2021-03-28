package com.appsnipp.modernlogin.adapters;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CardTransformer implements ViewPager.PageTransformer {

   public CardTransformer() {
        super();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {

        if (position < -1) {
            page.setRotation(0);
            page.setAlpha(0);
        } else if (position <= 1) {
            float rotation = (-position) * (15);
            page.setRotationY(rotation);
            float alpha = Math.min(1, 1 - Math.abs(position) / 3);
            page.setAlpha(alpha);
        } else {
            page.setRotation(0);
            page.setAlpha(0);
        }
    }
}
