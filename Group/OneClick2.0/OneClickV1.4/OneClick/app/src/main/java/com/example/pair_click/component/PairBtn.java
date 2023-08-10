package com.example.pair_click.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.pair_click.MainActivity;
import com.example.pair_click.activity.ActionActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PairBtn implements Serializable {
    public int id;
    public int type;
    public int btntype;
    public int backbtn;
    public String btntxt;
    public int del;
    public List<Integer> sequence;
    public String action;

    public String getBtntxt(){
        return btntxt;
    }

    public boolean checkname(String str){
        return str.equals(btntxt);
    }

    public void onCall(Context context, Activity act){
        if(btntype == 0){
            String url = this.action;
            if (url.startsWith("www."))
                url = "http://" + url;
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Toast.makeText(context, "Wrong URL", Toast.LENGTH_SHORT).show();
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            context.startActivity(intent);
        }else if(btntype == 1){
            String pkname = this.action;
            Intent intent = new Intent();
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(pkname);
            context.startActivity(intent);
        }else if(btntype == 2){
            String phonenum = this.action;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{
                        Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phonenum);
                intent.setData(data);
                context.startActivity(intent);
            }
        }else{
            Intent intent = new Intent(context, ActionActivity.class);
            context.startActivity(intent);
        }
    }

    public List<Integer> getSequence() {
        if (sequence == null){
            ArrayList<Integer> s = new ArrayList<>();
            s.add(0);
            return s;
        }else {
            return sequence;
        }
    }
}
