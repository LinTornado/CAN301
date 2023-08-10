package com.example.pair_click.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pair_click.R;
import com.example.pair_click.bean.AppBean;
import com.example.pair_click.component.GestureCanvas;
import com.example.pair_click.component.PairBtn;
import com.example.pair_click.manager.DataManager;
import com.example.pair_click.utils.ScreenSizeUtils;
import com.example.pair_click.views.BanslidingViewPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddingActivity extends AppCompatActivity {
    private List<View> mviews;
    private View view1, view2, view3;
    private PagerAdapter mpageAdapter;
    private BanslidingViewPage mViewPager;
    private int screen_x, screen_y;
    private GestureCanvas lockview;
    private List<Integer> sequence = null;
    private String action = "";
    private String pairname = "";
    private int type;
    int position = -1;
    List<PairBtn> mPairBtn = new ArrayList<>();
    DataManager dataManager;
    List<Integer> mbtnlist=new ArrayList<>();
    private List<AppBean> mAppBeanList = new ArrayList<>();
    private AppBean mSelectBean = null;

    private final BaseQuickAdapter<AppBean, BaseViewHolder> mAppAdapter = new BaseQuickAdapter<>(R.layout.rv_app_item) {
        @Override
        protected void convert(@NonNull BaseViewHolder helper, AppBean appBean) {
            View itemView = helper.itemView;
            ImageView ivSelect = itemView.findViewById(R.id.ivSelect);
            TextView tvName = itemView.findViewById(R.id.tvName);
            tvName.setText(appBean.getName());
            ImageView ivIcon = itemView.findViewById(R.id.ivIcon);
            ivIcon.setBackground(appBean.getIcon());
            itemView.setOnClickListener(view -> {
                mSelectBean = appBean;
                for (AppBean bean : mAppBeanList) {
                    if (Objects.equals(mSelectBean, bean)) {
                        bean.setChecked(true);
                    } else {
                        bean.setChecked(false);
                    }
                }
                mAppAdapter.notifyDataSetChanged();
            });
            if (appBean.isChecked()) {
                ivSelect.setBackground(ResourceUtils.getDrawable(R.drawable.icon_select));
            } else {
                ivSelect.setBackground(ResourceUtils.getDrawable(R.drawable.icon_unselect));
            }
        }
    };

    private AlertDialog createAppListDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_app_list, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddingActivity.this);
        builder.setView(view);
        RecyclerView rl = view.findViewById(R.id.rl);
        rl.setLayoutManager(new LinearLayoutManager(this));
        mAppAdapter.setNewData(mAppBeanList);
        mAppAdapter.bindToRecyclerView(rl);
        builder.setTitle("Please choose your app");
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> mSelectBean = null);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (mSelectBean == null) {
                ToastUtils.showShort("Please select a app");
            } else {
                action = mSelectBean.getPackageName();
                Toast.makeText(AddingActivity.this, "Action set as " + mSelectBean.getName(), Toast.LENGTH_SHORT).show();
                type = 1;
            }
        });
        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbtnlist.add(R.drawable.btn1);
        mbtnlist.add(R.drawable.btn2);
        mbtnlist.add(R.drawable.btn3);
        mbtnlist.add(R.drawable.btn4);
        getsize();
        setContentView(R.layout.activity_testchangepage);
        init();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        lockview.setOnDrawFinishedListener(new GestureCanvas.OnDrawFinsihListner() {
            @Override
            public boolean OnDrawFinished(List<Integer> passlist) {
                if (passlist.size() < 3) {
                    Toast.makeText(AddingActivity.this, "No less than 3 points in one gesture!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    AddingActivity.this.sequence = passlist;
                    return true;
                }
            }
        });
        ImageButton btnsec = view1.findViewById(R.id.btnsec);
        ImageButton btnreset = view1.findViewById(R.id.btnreset);
        ImageButton btnrecord = view1.findViewById(R.id.btnrecord);
        ImageButton btnurl = view2.findViewById(R.id.btnurl);
        ImageButton btncall = view2.findViewById(R.id.btncall);
        ImageButton btnapp = view2.findViewById(R.id.btnapp);
        ImageButton btnnext2 = view2.findViewById(R.id.btnnextp);
        ImageButton btncfm = view3.findViewById(R.id.btncfm);
        EditText edi = view3.findViewById(R.id.inputname);
        view1.clearAnimation();
        animation.setFillAfter(true);
        btnsec.startAnimation(animation);
        btnrecord.startAnimation(animation);
        btnreset.startAnimation(animation);
        view1.findViewById(R.id.jpbk1).startAnimation(animation);
        lockview.startAnimation(animation);
        view1.findViewById(R.id.gesornot).startAnimation(animation);
        btnsec.bringToFront();
        btnrecord.bringToFront();
        btnreset.bringToFront();
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockview.resetPoint();
            }
        });
        btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sequence == null) {
                    Toast.makeText(AddingActivity.this, "You haven't draw your Gesture", Toast.LENGTH_SHORT).show();
                } else {
                    boolean x = check_dup(sequence);
                    System.out.println(x);
                    if (x) {
                        Toast.makeText(AddingActivity.this, "This gesture has already been registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddingActivity.this, "Gesture Recorded", Toast.LENGTH_SHORT).show();
                        mViewPager.setCurrentItem(1);
                        view2.findViewById(R.id.title).startAnimation(animation);
                        btncall.startAnimation(animation);
                        btnapp.startAnimation(animation);
                        btnurl.startAnimation(animation);
                        btnnext2.startAnimation(animation);
                        view2.findViewById(R.id.jpbk2).startAnimation(animation);
                    }
                }
            }
        });

        btnsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
                view2.findViewById(R.id.title).startAnimation(animation);
                btncall.startAnimation(animation);
                btnapp.startAnimation(animation);
                btnurl.startAnimation(animation);
                btnnext2.startAnimation(animation);
                view2.findViewById(R.id.jpbk2).startAnimation(animation);
            }
        });

        btnurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputserver=new EditText(com.example.pair_click.activity.AddingActivity.this);
                inputserver.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder=new AlertDialog.Builder(com.example.pair_click.activity.AddingActivity.this);
                builder.setTitle("Input your url");
                builder.setView(inputserver).setNegativeButton("Cancel",null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        action = inputserver.getText().toString();
                        if (isUrl(action)){
                            Toast.makeText(com.example.pair_click.activity.AddingActivity.this,"Action set as open website",Toast.LENGTH_SHORT).show();
                            type = 0;
                        }else{
                            AlertDialog alertDialog2 = new AlertDialog.Builder(com.example.pair_click.activity.AddingActivity.this)
                                    .setTitle("Failed")
                                    .setMessage("Illegal URL Format")
                                    .setPositiveButton("OK", null)
                                    .create();
                            alertDialog2.show();
                            action = "";
                        }
                    }
                });
                builder.show();
            }
        });

        btnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getPackageManager();
                List<PackageInfo> list2 = pm.getInstalledPackages(0);
                List<AppBean> appBeanList = new ArrayList<>();
                for (PackageInfo packageInfo : list2) {
                    Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                    if (packageInfo.applicationInfo.icon != 0) {

                            String appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                            Drawable drawable = packageInfo.applicationInfo.loadIcon(getPackageManager());
                            String packagename = packageInfo.packageName;
                            if (!Objects.equals(packagename, AppUtils.getAppPackageName())) {
                                appBeanList.add(new AppBean(appname, drawable, packagename));
                            }
                    }
                }
                mAppBeanList.clear();
                mAppBeanList = appBeanList;
                if (CollectionUtils.isEmpty(mAppBeanList)) {
                    ToastUtils.showShort("There is no app!");
                } else {
                    createAppListDialog().show();
                }
            }
        });

        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputserver = new EditText(com.example.pair_click.activity.AddingActivity.this);
                inputserver.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.pair_click.activity.AddingActivity.this);
                builder.setTitle("Input the number");
                builder.setView(inputserver).setNegativeButton("Cancel",null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        action = inputserver.getText().toString();
                        if (isPhone(action)){
                            Toast.makeText(com.example.pair_click.activity.AddingActivity.this,"Action set as call",Toast.LENGTH_SHORT).show();
                            type = 2;
                        }else{
                            AlertDialog alertDialog2 = new AlertDialog.Builder(com.example.pair_click.activity.AddingActivity.this)
                                    .setTitle("Failed")
                                    .setMessage("Illegal Phone Number")
                                    .setPositiveButton("OK", null)
                                    .create();
                            alertDialog2.show();
                            action = "";
                        }
                    }
                });
                builder.show();
            }
        });

        edi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                pairname = editable.toString();
                if (pairname.isEmpty()) {
                    Toast.makeText(AddingActivity.this, "Input your name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnnext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.isEmpty()) {
                    Toast.makeText(AddingActivity.this, "Input your action!", Toast.LENGTH_SHORT).show();
                } else {
                    view3.findViewById(R.id.jpbk3).startAnimation(animation);
                    view3.findViewById(R.id.naming).startAnimation(animation);
                    btncfm.startAnimation(animation);
                    edi.startAnimation(animation);
                    mViewPager.setCurrentItem(2);
                }
            }
        });

        btncfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = (int)(Math.random()*4);
                int ranbtn = mbtnlist.get(random);
                if (pairname.isEmpty()) {
                    Toast.makeText(AddingActivity.this, "Input your name!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddingActivity.this);
                    View viewcfm = View.inflate(AddingActivity.this, R.layout.cfm, null);
                    TextView dowhat = viewcfm.findViewById(R.id.dowhat);
                    TextView dowhat2 = viewcfm.findViewById(R.id.dowhat2);
                    TextView usesomething = viewcfm.findViewById(R.id.usesomething);
                    if (type == 0) {
                        String dowh = "Open the Website:";
                        dowhat.setText(dowh);
                        dowhat.setTextColor(Color.BLUE);
                        dowhat2.setText(action);
                        dowhat2.setTextColor(Color.BLUE);
                    } else if (type == 1) {
                        String dowh = "Open the App:";
                        dowhat.setText(dowh);
                        dowhat.setTextColor(Color.BLUE);
                        PackageManager pm = getPackageManager();
                        PackageInfo info = null;
                        try {
                            info = pm.getPackageInfo(action, PackageManager.GET_ACTIVITIES);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        dowhat2.setText(info.applicationInfo.loadLabel(pm).toString());
                        dowhat2.setTextColor(Color.BLUE);
                    } else {
                        String dowh = "Make Phone Call to:";
                        dowhat.setText(dowh);
                        dowhat.setTextColor(Color.BLUE);
                        dowhat2.setText(action);
                        dowhat2.setTextColor(Color.BLUE);
                    }

                    if (sequence == null) {
                        usesomething.setTextColor(Color.BLUE);
                        usesomething.setText("Tap the button");
                    } else {
                        usesomething.setTextColor(Color.BLUE);
                        usesomething.setText("Tap the button or Draw the Gesture");
                    }

                    builder.setView(viewcfm);
                    builder.setNegativeButton("Cancel", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int length = mPairBtn.size();
                            if (type == 0) {
                                addData(length, ranbtn, R.drawable.link, pairname, R.drawable.delete, action, type, sequence);
                            } else if (type == 1) {
                                addData(length, ranbtn, R.drawable.app, pairname, R.drawable.delete, action, type, sequence);
                            } else {
                                addData(length, ranbtn, R.drawable.phone, pairname, R.drawable.delete, action, type, sequence);
                            }
                            dataManager.SaveListToLocal(AddingActivity.this, mPairBtn);
                            Toast.makeText(AddingActivity.this, "New Button Created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.show();
                }
            }
        });

        mviews = new ArrayList<View>();
        mviews.add(view1);
        mviews.add(view2);
        mviews.add(view3);
        mpageAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mviews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mviews.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mviews.get(position));
                return mviews.get(position);
            }
        };
        mViewPager.setAdapter(mpageAdapter);
    }

    private boolean check_dup(List<Integer> se) {
        boolean dup = false;
        for (int k = 0; k < mPairBtn.size(); k++) {
            if (se.equals(mPairBtn.get(k).getSequence())) {
                System.out.println(se);
                System.out.println(mPairBtn.get(k).sequence);
                dup = true;
                break;
            }
        }
        return dup;
    }

    private void init() {
        dataManager = new DataManager();
        mPairBtn = dataManager.getLocalList(AddingActivity.this);
        mViewPager = findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2, null);
        view3 = inflater.inflate(R.layout.layout3, null);
        lockview = view1.findViewById(R.id.ges);

    }

    private boolean isUrl(String url){
        url = url.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return  url.matches(regex);
    }

    private boolean isPhone(String phoneNum){
        if(phoneNum==null||phoneNum.length()<=0){
            return false;
        }
        return !Pattern.compile("(?i)[a-z]").matcher(phoneNum).find();
    }

    public void addData(int position, int btnblue, int type, String txt, int del, String action, int btntype, List<Integer> passlist) {
        PairBtn news = new PairBtn();
        news.backbtn = btnblue;
        news.type = type;
        news.btntxt = txt;
        news.del = del;
        news.action = action;
        news.btntype = btntype;
        news.sequence = passlist;
        mPairBtn.add(position, news);
    }

    private void getsize() {
        screen_x = ScreenSizeUtils.getInstance(AddingActivity.this).getScreenWidth();
        screen_y = ScreenSizeUtils.getInstance(AddingActivity.this).getScreenHeight();
    }
}