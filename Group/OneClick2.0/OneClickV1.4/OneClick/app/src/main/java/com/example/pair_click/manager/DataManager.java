package com.example.pair_click.manager;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pair_click.component.PairBtn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class DataManager {
    List<PairBtn> mPairBtn = new ArrayList<>();

    public void SaveListToLocal(Context context, List<PairBtn> mPairBtnx){
        SharedPreferences.Editor editor = context.getSharedPreferences("PairButtonList",context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(mPairBtnx);
        editor.putString("mPairBtnJson",json);
        editor.commit();
    }

    public List<PairBtn> getLocalList(Context context){
        SharedPreferences preferences = context.getSharedPreferences("PairButtonList",context.MODE_PRIVATE);
        String json = preferences.getString("mPairBtnJson",null);
        if(json != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<PairBtn>>(){}.getType();
            List<PairBtn> savedmpair = new ArrayList<PairBtn>();
            savedmpair = gson.fromJson(json,type);
            mPairBtn = savedmpair;
        }
        return mPairBtn;
    }

    public List<PairBtn> getmPairBtn(){
        return mPairBtn;
    }

    public void reference(List<Integer> sequence, Context context, Activity act){
        List<PairBtn> key = mPairBtn;
        for(int i = 0; i < mPairBtn.size(); i++){
            PairBtn item = mPairBtn.get(i);
            if(!item.sequence.isEmpty() && item.sequence.equals(sequence)){
                item.onCall(context,act);
            }
        }
    }
}
