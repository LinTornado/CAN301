package com.example.pair_click;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pair_click.activity.ActionActivity;
import com.example.pair_click.activity.AddingActivity;
import com.example.pair_click.activity.GuideActivity;
import com.example.pair_click.activity.Introduction;
import com.example.pair_click.component.PairBtn;
import com.example.pair_click.manager.DataManager;
import com.example.pair_click.manager.FirstManager;
import com.example.pair_click.manager.FloatManager;
import com.example.pair_click.service.ForegroundService;
import com.example.pair_click.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    DataManager dataManager;
    List<PairBtn> mPairBtn = new ArrayList<>();

    int btnmeflag = 0;
    private int screen_x,screen_y;
    public static String ISFIRST = "isFirst";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isFirst = FirstManager.getBoolean(getApplicationContext(), ISFIRST, true);
        if(isFirst){
            Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(intent);
            FirstManager.saveBoolean(getApplicationContext(), ISFIRST, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getsize();
        setContentView(R.layout.activity_main);
        init();
        intibtn();
        startFloat();
    }

    private void init(){
        dataManager = new DataManager();
        mPairBtn = dataManager.getLocalList(MainActivity.this);
        mRecyclerView = findViewById(R.id.recy);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setAdapter();
    }

    private void setAdapter(){
        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onBackClick(View view,int position){
                PairBtn news = mPairBtn.get(position);
                news.onCall(MainActivity.this, MainActivity.this);
            }

            @Override
            public void onChangeName(View view, int position) {
                final EditText inputserver = new EditText(MainActivity.this);
                inputserver.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Rename");
                builder.setView(inputserver).setNegativeButton("Cancel",null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sign = inputserver.getText().toString();
                        if(sign != null && !sign.isEmpty()){
                            mMyAdapter.rename(position,sign);
                            dataManager.SaveListToLocal(MainActivity.this, mPairBtn);
                            Toast.makeText(MainActivity.this, position+" Renamed",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "No input", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }

            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure to delete this button");
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, mPairBtn.get(position).btntxt+" Deleted",
                                Toast.LENGTH_SHORT).show();
                        mMyAdapter.removeData(position);
                        dataManager.SaveListToLocal(MainActivity.this, mPairBtn);
                    }
                });
                builder.show();
            }

            @Override
            public void onShowCardInfo(View view, int position){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                View viewcfm = View.inflate(MainActivity.this,R.layout.card_info,null);
                TextView itext = viewcfm.findViewById(R.id.tname);
                TextView iaction = viewcfm.findViewById(R.id.naction);
                TextView idetail = viewcfm.findViewById(R.id.acdetail);
                TextView itrigger = viewcfm.findViewById(R.id.ntrigger);
                TextView ipattern = viewcfm.findViewById(R.id.npattern);
                PairBtn nowCard = mPairBtn.get(position);
                if(nowCard.btntype == 0){
                    String dowh = "Open the Website:";
                    itext.setText(nowCard.btntxt);
                    itext.setTextColor(Color.BLUE);
                    iaction.setText(dowh);
                    iaction.setTextColor(Color.BLUE);
                    idetail.setText(nowCard.action);
                    idetail.setTextColor(Color.BLUE);
                }else if(nowCard.btntype == 1){
                    String dowh = "Open the App:";
                    itext.setText(nowCard.btntxt);
                    itext.setTextColor(Color.BLUE);
                    iaction.setText(dowh);
                    iaction.setTextColor(Color.BLUE);
                    idetail.setText(nowCard.action);
                    idetail.setTextColor(Color.BLUE);
                    PackageManager pm = getPackageManager();
                    PackageInfo info = null;
                    try {
                        info = pm.getPackageInfo(nowCard.action,PackageManager.GET_ACTIVITIES);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    idetail.setText(info.applicationInfo.loadLabel(pm).toString());
                }else{
                    String dowh = "Make Phone Call to:";
                    itext.setText(nowCard.btntxt);
                    itext.setTextColor(Color.BLUE);
                    iaction.setText(dowh);
                    iaction.setTextColor(Color.BLUE);
                    idetail.setText(nowCard.action);
                    idetail.setTextColor(Color.BLUE);
                }
                if(nowCard.sequence == null){
                    itrigger.setTextColor(Color.BLUE);
                    itrigger.setText("Tap the button");
                }else{
                    itrigger.setTextColor(Color.BLUE);
                    itrigger.setText("Tap the button or Draw the Gesture");
                }
                ImageView[] point = new ImageView[9];
                point[0] = viewcfm.findViewById(R.id.point_one);
                point[1] = viewcfm.findViewById(R.id.point_two);
                point[2] = viewcfm.findViewById(R.id.point_three);
                point[3] = viewcfm.findViewById(R.id.point_four);
                point[4] = viewcfm.findViewById(R.id.point_five);
                point[5] = viewcfm.findViewById(R.id.point_six);
                point[6] = viewcfm.findViewById(R.id.point_seven);
                point[7] = viewcfm.findViewById(R.id.point_eight);
                point[8] = viewcfm.findViewById(R.id.point_nine);
                Bitmap[] dot = new Bitmap[9];
                dot[0] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_one);
                dot[1] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_two);
                dot[2] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_three);
                dot[3] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_four);
                dot[4] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_five);
                dot[5] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_six);
                dot[6] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_seven);
                dot[7] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_eight);
                dot[8] = BitmapFactory.decodeResource(getResources(),R.drawable.dot_nine);
                if (nowCard.sequence != null) {
                    ipattern.setText("Your Gesture");
                    for (int i = 0; i < nowCard.sequence.size(); i++) {
                        point[nowCard.sequence.get(i) - 1].setImageBitmap(dot[i]);
                    }
                }
                builder.setView(viewcfm);
                builder.setPositiveButton("Ok", null);
                builder.show();
            }
        });
    }

    private void intibtn(){
        ImageButton btnaddnew = (ImageButton) findViewById(R.id.addnewest);
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddingActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btngesnew = (ImageButton) findViewById(R.id.newges);
        btngesnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ActionActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnintro = (ImageButton) findViewById(R.id.video);
        btnintro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Introduction.class);
                startActivity(intent);
            }
        });
    }

    private void getsize(){
        screen_x = ScreenSizeUtils.getInstance(MainActivity.this).getScreenWidth();
        screen_y = ScreenSizeUtils.getInstance(MainActivity.this).getScreenHeight();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PairBtn news = mPairBtn.get(position);
            holder.mbackbtn.setBackgroundResource(news.backbtn);
            holder.mtype.setBackgroundResource(news.type);
            holder.mbtntxt.setText(news.btntxt);
            holder.mdel.setBackgroundResource(news.del);
            if(monItemClickListener != null){
                holder.mdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        monItemClickListener.onItemClick(holder.itemView,pos);
                    }
                });

                holder.mbackbtn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = holder.getLayoutPosition();
                        monItemClickListener.onShowCardInfo(holder.itemView,pos);
                        return false;
                    }
                });

                holder.mbackbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        monItemClickListener.onBackClick(holder.itemView,pos);
                    }
                });

                holder.mbtntxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        monItemClickListener.onChangeName(holder.itemView,pos);
                    }
                });
            }
        }

        public void removeData(int position){
            mPairBtn.remove(position);
            notifyItemRemoved(position);
        }

        public void rename(int position, String newtxt){
            PairBtn oldone = mPairBtn.get(position);
            oldone.btntxt = newtxt;
            mPairBtn.set(position,oldone);
            notifyItemChanged(position);
        }

        public interface OnItemClickListener{
            void onItemClick(View view,int position);
            void onShowCardInfo(View view,int position);
            void onBackClick(View view,int position);
            void onChangeName(View view, int position);
        }

        private OnItemClickListener monItemClickListener;
        public void setOnItemClickListener(OnItemClickListener monItemClickListener){
            this.monItemClickListener=monItemClickListener;
        }

        @Override
        public int getItemCount() {
            return mPairBtn.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageButton mbackbtn;
        ImageView mtype;
        TextView mbtntxt;
        ImageButton mdel;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mbackbtn = itemView.findViewById(R.id.backcolbtn);
            mtype = itemView.findViewById(R.id.icon);
            mbtntxt = itemView.findViewById(R.id.btntext);
            mdel = itemView.findViewById(R.id.delicon);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPairBtn = dataManager.getLocalList(MainActivity.this);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startFloat() {
        FloatManager.showFloat();
        startForegroundService(new Intent(this, ForegroundService.class));
        if (!isIgnoringBatteryOptimizations()) {
            requestIgnoreBatteryOptimizations();
        }
    }

    private void close() {
        stopService((new Intent(this, ForegroundService.class)));
    }

    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}