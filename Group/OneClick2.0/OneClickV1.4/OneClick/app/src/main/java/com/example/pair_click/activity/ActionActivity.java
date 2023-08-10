package com.example.pair_click.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pair_click.MainActivity;
import com.example.pair_click.R;
import com.example.pair_click.component.GestureCanvas;
import com.example.pair_click.component.PairBtn;
import com.example.pair_click.manager.DataManager;

import java.util.ArrayList;
import java.util.List;

public class ActionActivity extends AppCompatActivity {
    private GestureCanvas lockview;
    private List<Integer> passlist;
    List<PairBtn> mPairBtn = new ArrayList<>();
    DataManager data_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_manager = new DataManager();
        mPairBtn = data_manager.getLocalList(ActionActivity.this);
        setContentView(R.layout.activity_testacti);
        lockview = (GestureCanvas) findViewById(R.id.view);

        lockview.setOnDrawFinishedListener(new GestureCanvas.OnDrawFinsihListner() {
            @Override
            public boolean OnDrawFinished(List<Integer> passlist) {
                if(passlist.size()<3){
                    Toast.makeText(ActionActivity.this,"No less than 3 points in the password!",Toast.LENGTH_SHORT).show();
                    return false;
                }else{
                    ActionActivity.this.passlist = passlist;
                    for(int i = 0; i<mPairBtn.size(); i++){
                        PairBtn item = mPairBtn.get(i);
                        System.out.println(item.getSequence());
                        System.out.println("find a app to launch");
                        if(!item.getSequence().isEmpty() && item.getSequence().equals(passlist)){
                            System.out.println("not empty");
                            item.onCall(ActionActivity.this, ActionActivity.this);
                            return true;
                        }
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(com.example.pair_click.activity.ActionActivity.this)
                            .setTitle("Oops!")
                            .setMessage("The password does not exist!")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                    return false;
                }
            }
        });

        ImageButton btnreset2 = findViewById(R.id.btnreset2);
        btnreset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockview.resetPoint();
            }
        });

        ImageButton btnreturn2 = findViewById(R.id.btnreturn2);
        btnreturn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}