package com.passinhotv.android.adapter;

import android.content.Context;

import com.passinhotv.android.R;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

/**
 * @author S.Shahini
 * @since 2/12/18
 */

public class MainSliderAdapter extends SliderAdapter {


    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(R.drawable.slider_login_1);
                break;
            case 1:
                viewHolder.bindImageSlide(R.drawable.slider_login_2);
                break;
            case 2:
                viewHolder.bindImageSlide(R.drawable.slider_login_3);
                break;
            case 3:
                viewHolder.bindImageSlide(R.drawable.slider_login_4);
                break;
        }
    }

}
