package com.example.pair_click.manager;

import android.view.Gravity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.example.pair_click.MainActivity;
import com.example.pair_click.R;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;

public class FloatManager {

    public static void showFloat() {
        EasyFloat.with(ActivityUtils.getTopActivity())
                .setShowPattern(ShowPattern.BACKGROUND)
                .setLayout(R.layout.dialog_float, (view -> view.setOnClickListener(v ->
                        ActivityUtils.startActivity(MainActivity.class)
                )))
                .setGravity(Gravity.END | Gravity.BOTTOM, ConvertUtils.px2dp(-20F), ConvertUtils.px2dp(-1000F))
                .show();
    }
}
