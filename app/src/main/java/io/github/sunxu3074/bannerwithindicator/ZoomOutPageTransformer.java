package io.github.sunxu3074.bannerwithindicator;

/**
 * Created by sunxu on 2015/11/6.
 */

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;


public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MAX_ROTATE = 20f;
    private float mRot;
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            //view.setAlpha(0);
            ViewHelper.setRotation(view, 0);
        }
        else if (position <= 0) {
            // [-1,1]
            // Modify the default slide transition to shrink the page as well
			/*float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2; */
            mRot = position*MAX_ROTATE;//0~-20
            ViewHelper.setPivotX(view, pageWidth / 2);
            ViewHelper.setPivotY(view, pageHeight);
            ViewHelper.setRotation(view, mRot);
        } else if(position <=1){// B页从1 ~ 0
            mRot = position*MAX_ROTATE;
            ViewHelper.setPivotX(view, pageWidth/2);
            ViewHelper.setPivotY(view, pageHeight);
            ViewHelper.setRotation(view, mRot);
        }
        else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setRotation(view, 0);
        }
    }

}

