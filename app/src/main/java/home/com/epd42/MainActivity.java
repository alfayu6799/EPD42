package home.com.epd42;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();  //debug

    public TextView  heartValue, respValue, spo2Value, skinValue, nibpValue, mmhgValue;
    public TextView  patientName, bedNoText, age, phoneText;
    public TextView  nurseName, surgeonName, hospitaList;
    public TextView  goals, achieved;
    public TextView  takePainAssessment;
    public TextView  lastTakenDate, lastTakenTime, lastTakenDrug;
    public TextView  nextTakenDate, nextTakenTime, nextTakenDrug;
    public TextView  vitalSignTime, skinf;
    public ImageView patientInfo, medicineSchedule, vitalSign, painLevel;
    public ImageView gender;
    public TextView  surgicalDate, surgicalTime, surgicalTitle, painAssessment;
    public TextView  painLevelTime, painLevelText, painRatio;
    public ImageView painLevelIcon, painPosition, painPositionClose;
    public ImageView informationIcon, goalsIcon;
    public ImageView precaution1, precaution2, precaution3;
    public ImageView networkMessages;
    public CheckBox  getUp, painSchedule;
    public RelativeLayout layoutPainPosition, layoutPrecaution, layoutBlank;
    public RelativeLayout relayPatientContext, relayCaregiverContext, relayPhysiciansContext, relayPreContext;
    public RelativeLayout relayScheduleContext, relayMedicationContext;
    public RelativeLayout relayPainLevelContext, relayVitalSignContext, relayGoals;
    public RelativeLayout relayPatientLabel, relayVitalSignLabel;
    public LinearLayout   fullScreenLayout, medicationLeft, medicationRight, painlevelLeft , relayPainLevelLabel;
    public RelativeLayout relayPosition;
    public ImageView BadIcon, PhoneIcon;
    private ImageView EmptyBed;
    private LinearLayout FullEmptyLayout;
    private TextView EmptyMessages;

    public static String token_head = "Bearer ";
    public static String default_token = token_head + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1ODAzNzk0NzcsIm5iZiI6MTU4MDM3OTQ3NywiZXhwIjoxNTgyOTcxNDc3LCJpc3MiOiJsYW5kbWFyayBzdHYifQ.qlC3LaVsNkdLhaKq8PxJT4BG_tGBM6ZjsDVqFBnnNxI";
    public static String demo_for_epd42_token = token_head + "gvLPUGu4~)V.@(UE![2D]AQK><p|+@[T%)vPDW*v!nkdln850.l({D#H#A*>+@<{";

    private String patient_err_msg;
    private String lastVisi_err_msg;
    private String schedule_err_msg;
    private String pain_assessment_err_msg;
    private String pain_assessment_SerialID;
    private JSONObject jsonObject;
    private String err_msg;
    private String rtn_message;

    private String PrecautionShow;

    //20200226 IP Address 自行輸入
    private String IPAddress;
    private TextView GetIPAddress;

    //20200121
    String firstName, middleName, lastName , phoneNumber, getAge;
    String genderText, getGenderText, getBedNo, getGoals, getNurse, getSurgeon, getHospitalist;
    String getPrecaution;

    //20200214
    protected Context myContext;
    private SharedPreferences app_Setting;

    private static MainActivity mainActivity = null;

    public static String AndroidId = "epd42devicekey"; //Demo用

    private boolean IsInfoLayout = true;  //空床判斷

    //20200215
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = getBaseContext();
        getSupportActionBar().hide(); //title隱藏
        setContentView(R.layout.activity_main);

        mainActivity = this;

        // 禁用鎖頻功能
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //init SharedPreferences
        app_Setting = myContext.getSharedPreferences("EPD42", MODE_PRIVATE);
        app_Setting.edit().putString("AndroidID","epd42devicekey").apply();
        IPAddress = getSharedPreferences("EPD42",0).getString("IPAddress","http://10.58.177.123");

        initView();

        //20200214
        initData();
    }

    private void initData() {
        reflashLayout.run();   //20200311 (每8小時就刷新畫面避免殘影)
        checkAccountNo.run();  //30秒檢查一次帳號
        try {
            Thread.sleep(1000);
            getJson.run();     //31秒檢查一次所有的資訊
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        patientName = (TextView) findViewById(R.id.tv_patient_name);
        gender = (ImageView) findViewById(R.id.iv_gender);
        bedNoText = (TextView) findViewById(R.id.tv_bed_no);
        age = (TextView) findViewById(R.id.tv_age);
        phoneText = (TextView) findViewById(R.id.tv_phone_no);
        nurseName = (TextView) findViewById(R.id.tv_nurse_name);
        surgeonName =(TextView) findViewById(R.id.tv_surgeon_name);
        hospitaList = (TextView) findViewById(R.id.tv_hospitalist);
        goals = (TextView) findViewById(R.id.tv_goals);
        precaution1 = (ImageView) findViewById(R.id.ig_precaution_1);
        precaution2 = (ImageView) findViewById(R.id.ig_precaution_2);
        precaution3 = (ImageView) findViewById(R.id.ig_precaution_3);

        //Vital Sign
        vitalSignTime = (TextView) findViewById(R.id.tv_vital_sign_date_time); //20200131
        heartValue = (TextView) findViewById(R.id.tv_heart_rate);
        respValue = (TextView) findViewById(R.id.tv_resp);
        spo2Value = (TextView) findViewById(R.id.tv_spo2);
        skinValue = (TextView) findViewById(R.id.tv_skin);
        nibpValue = (TextView) findViewById(R.id.tv_nibp);
        mmhgValue = (TextView) findViewById(R.id.tv_mmhg);
        skinf = (TextView) findViewById(R.id.tv_skinf);
//        skinf.setText("SKIN" + "\u2109 ");  //u2109 = F
        skinf.setText("SKIN" + "\u2103 ");    //u2103 = C

        //schedule & Medication
        takePainAssessment = (TextView) findViewById(R.id.tv_do_assessment_time);
        lastTakenDate = (TextView) findViewById(R.id.tv_last_taken_date);
        lastTakenTime = (TextView) findViewById(R.id.tv_last_taken_time);
        lastTakenDrug = (TextView) findViewById(R.id.tv_last_taken_drug_name);
        nextTakenDate = (TextView) findViewById(R.id.tv_next_taken_date);
        nextTakenTime = (TextView) findViewById(R.id.tv_next_taken_time);
        nextTakenDrug = (TextView) findViewById(R.id.tv_next_taken_drug_name);
        surgicalDate = (TextView) findViewById(R.id.tv_surgical_date);
        surgicalTime = (TextView) findViewById(R.id.tv_surgical_time);
        surgicalTitle = (TextView) findViewById(R.id.tv_surgical);
        painAssessment = (TextView) findViewById(R.id.tv_do_pina);

        //pain Level
        painPosition = (ImageView) findViewById(R.id.iv_pain_position); //20200120
        painLevelTime = (TextView) findViewById(R.id.tv_pain_level_date_time);
        painLevelText = (TextView) findViewById(R.id.tv_pain_level);
        painRatio = (TextView) findViewById(R.id.tv_pain_level_ratio);
        painLevelIcon = (ImageView) findViewById(R.id.pv_icon);

        //icon for onClick
        informationIcon = (ImageView) findViewById(R.id.info_icon);
        goalsIcon = (ImageView) findViewById(R.id.s9_icon);
        patientInfo = (ImageView)findViewById(R.id.s1_icon);
        medicineSchedule = (ImageView) findViewById(R.id.s5_icon);
        painLevel = (ImageView) findViewById(R.id.s7_icon);
        vitalSign = (ImageView) findViewById(R.id.s8_icon);
        getUp = (CheckBox) findViewById(R.id.cb_goals);
        getUp.setVisibility(View.INVISIBLE);         //hide
        painSchedule = (CheckBox) findViewById(R.id.cb_schedule);
//        painSchedule.setVisibility(View.INVISIBLE);  //hide
        achieved = (TextView) findViewById(R.id.tv_check_text);
        achieved.setVisibility(View.GONE);

        layoutPainPosition = (RelativeLayout) findViewById(R.id.ly_lable_pain_position);
        painPositionClose = (ImageView) findViewById(R.id.iv_close_position);
        layoutPrecaution = (RelativeLayout) findViewById(R.id.ry_precaution);
        layoutBlank = (RelativeLayout) findViewById(R.id.ry_precaution_blank); //Precaution遮殘影layout

        //2020306
        FullEmptyLayout = (LinearLayout) findViewById(R.id.ly_full_empty);  //空床layout
        EmptyBed = (ImageView) findViewById(R.id.iv_empty_bed);
        EmptyMessages = (TextView) findViewById(R.id.tv_empty_message);

        //20200220
        relayPatientContext = (RelativeLayout) findViewById(R.id.ry_patient_context);
        relayCaregiverContext = (RelativeLayout) findViewById(R.id.ry_caregiver_context);
        relayPhysiciansContext = (RelativeLayout) findViewById(R.id.ry_physician_context);
        relayPreContext = (RelativeLayout) findViewById(R.id.ry_precaution_contents);  //Precaution內容(三個icon)
        relayGoals = (RelativeLayout) findViewById(R.id.ry_goals_context);
        relayScheduleContext = (RelativeLayout) findViewById(R.id.ry_schedule_context);
        relayMedicationContext = (RelativeLayout) findViewById(R.id.ry_medicaton_context);
        relayVitalSignContext = (RelativeLayout) findViewById(R.id.ry_vital_sign_context);
        relayPainLevelContext = (RelativeLayout) findViewById(R.id.ry_pain_level_context);
        relayPainLevelLabel = (LinearLayout) findViewById(R.id.ly_label_pain_level);
        fullScreenLayout = (LinearLayout) findViewById(R.id.ly_full_screen);
        medicationLeft = (LinearLayout) findViewById(R.id.ly_medic_context_left);
        medicationRight = (LinearLayout) findViewById(R.id.ly_medic_context_right);
        painlevelLeft = (LinearLayout) findViewById(R.id.ly_pain_level_left);

        GetIPAddress = (TextView) findViewById(R.id.tv_label_info_panel);
        networkMessages = (ImageView) findViewById(R.id.iv_network_messages);
        networkMessages.setVisibility(View.INVISIBLE);

        relayPosition = (RelativeLayout) findViewById(R.id.ry_pain_position);

        //20200304 改變icon color
        BadIcon = (ImageView) findViewById(R.id.bed_icon);
        Drawable Bed_Icon = ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_bed_240);
        Bed_Icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.fontColor), PorterDuff.Mode.MULTIPLY);
        BadIcon.setImageDrawable(Bed_Icon);

        PhoneIcon = (ImageView) findViewById(R.id.phone_icon);
        Drawable Phone_Icon = ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_phone_240);
        Phone_Icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.fontColor), PorterDuff.Mode.MULTIPLY);
        PhoneIcon.setImageDrawable(Phone_Icon);

        //OnClick Listener
        GetIPAddress.setOnClickListener(this);        //dialog fxn for ipaddress input
        goals.setOnClickListener(this);              //dialog fxn
        patientInfo.setOnClickListener(this);        //get patient info api
        vitalSign.setOnClickListener(this);         //get patient vital Sign api
        medicineSchedule.setOnClickListener(this);  //get patient medicine schedule
        painLevel.setOnClickListener(this);         //get patient pain level
        painPosition.setOnClickListener(this);      //dialog for pain position 20200205
        informationIcon.setOnClickListener(this);   //get all info 20200206
        goalsIcon.setOnClickListener(this);         //reload Activity
        painPositionClose.setOnClickListener(this); //close Pain Position big
        EmptyBed.setOnClickListener(this);
    }

    //每35秒執行一次get json data from webAPI 20200215
    private Runnable getJson = new Runnable() {
        @Override
        public void run() {
            getAllJson(); //取得所有的Json data
            handler.postDelayed(this, 35000); //35 seconds
        }
    };

    //每30秒執行一次accountNo check
    private Runnable checkAccountNo = new Runnable() {
        @Override
        public void run() {
            GetDevicePairInfo_Url_json();
            handler.postDelayed(this, 30000);    //30 seconds
        }
    };

    //每8小時執行一次 20200311
    private Runnable reflashLayout = new Runnable() {

        @Override
        public void run() {
            Log.d(TAG, "reflashLayout every 8 hour !");
            reflashLayout();  //20200313
            handler.postDelayed(this, 1000 * 60 * 60 * 8 );
        }
    };

    //取得病患資料
    private void GetPatientInfo_Url_json() {
        {
            new Thread() {
                public void run() {
                    String jsonFullURL = IPAddress + "/EPD42DEMO/api/getPatientInfo"; //Bell's
                    Log.d(TAG, "GetPatientInfo_Url_json = " + jsonFullURL);

                    JSONObject post_jsonObject = new JSONObject();

                    String accountNo = getSharedPreferences("EPD42",0).getString("accountNo",null);
                    String stationNo = getSharedPreferences("EPD42", 0).getString("stationNo", null);
                    String bedNo = getSharedPreferences("EPD42", 0).getString("bedNo",null);
                    String roomNo = getSharedPreferences("EPD42", 0).getString("roomNo", null);

                    try {
                        post_jsonObject.accumulate("apiName", "getPatientInfo");
                        post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                        post_jsonObject.accumulate("accountNo", accountNo);
                        post_jsonObject.accumulate("stationNo", stationNo);
                        post_jsonObject.accumulate("bedNo", bedNo);
                        post_jsonObject.accumulate("rooMNo", roomNo);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.d(TAG, "GetPatientInfo_Url_json_POST = " + post_jsonObject.toString());

                    String result;
                    result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                    Log.d(TAG, "GetPatientInfo_Url:" + result);

                    patient_err_msg = "";
                    if (result != null && !("".equalsIgnoreCase(result))) {
                        try {
                            GlobalClass.patient_info_jsonObject = new JSONObject(result);
                            String rtn_result = GlobalClass.patient_info_jsonObject.getString("result");

                            if (GlobalClass.patient_info_jsonObject.has("Message")) {
                                rtn_message = GlobalClass.patient_info_jsonObject.getString("Message");
                            }

                            if (rtn_result.equalsIgnoreCase("Y")) runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject data = GlobalClass.patient_info_jsonObject.getJSONArray("data").getJSONObject(0);

                                        GlobalClass.dischargeDate = data.getString("dischargeDate");
                                        GlobalClass.admitDate = data.getString("admitDate");

                                        if (data.has("LoginID")) {
                                            GlobalClass.getPatientInfo_LoginID = data.getString("LoginID");
                                        }
                                        if (data.has("APPID")) {
                                            GlobalClass.getPatientInfo_APPID = data.getString("APPID");
                                        }
                                        if (data.has("token")) {
                                            GlobalClass.getPatientInfo_token = data.getString("token");
                                        }
                                        if (data.has("surgicalSite")) {
                                            GlobalClass.getPatientInfo_surgicalSite = data.getString("surgicalSite");
                                        }
                                        if (data.has("surgicalSiteID")) {
                                            GlobalClass.getPatientInfo_surgicalSiteID = data.getString("surgicalSiteID");
                                        }
                                        GlobalClass.fast_login_text = GlobalClass.getPatientInfo_LoginID;
                                        //20200120
                                        if (data.has("firstName"))
                                        {
                                            firstName = data.getString("firstName");
                                        }
                                        if (data.has("middleName"))
                                        {
                                            middleName = data.getString("middleName");
                                        }
                                        if (data.has("lastName"))
                                        {
                                            lastName = data.getString("lastName");
                                        }
                                        if (data.has("patSex"))
                                        {
                                            genderText = data.getString("patSex");
                                            if(genderText != null) {
                                                if (genderText.equals("M")) {
                                                    gender.setImageResource(R.mipmap.icon_male);
                                                    getGenderText = "Mr. ";
                                                } else {
                                                    gender.setImageResource(R.mipmap.icon_female);
                                                    getGenderText = "Ms. ";
                                                }
                                            }
                                        }
                                        patientName.setText(getGenderText + firstName + " "+ middleName + " "+ lastName);
                                        if (data.has("bedNo"))
                                        {
                                            getBedNo = data.getString("bedNo");
                                            bedNoText.setText(getBedNo);
                                        }
                                        if (data.has("tel")) {
                                            phoneNumber = data.getString("tel");
                                            if (phoneNumber != null){
                                                phoneText.setText(phoneNumber);
                                            }
                                        }
                                        if (data.has("birthday"))  //20200128
                                        {
                                            getAge = data.getString("birthday");
                                            if (getAge != null) {
                                                int birthday = Integer.parseInt(getAge.substring(0, 4));  //ex : 1947-01-27
                                                Calendar today = Calendar.getInstance();
                                                int thisYear = today.get(Calendar.YEAR);    //2020
                                                int ageCalc = thisYear - birthday;
                                                String patientAge = Integer.toString(ageCalc);
                                                age.setText(patientAge + "y");
                                            }else{
                                                age.setText("" + "y");
                                            }
                                        }
                                        if (data.has("Nurse")) //20200130
                                        {
                                            getNurse = data.getString("Nurse");
                                            if (getNurse != null) {
                                                String[] Nurse = getNurse.split(","); //20200302
                                                for (int i = 0; i < Nurse.length; i++){
                                                    if(i == 0) {
                                                        if (Nurse[0].trim().contains(" ")) {
                                                            String[] splitName = Nurse[0].split("\\s+");
                                                            nurseName.setText(splitName[0] + "\n" + " " + splitName[1]);
                                                        }else{
                                                            nurseName.setText(getNurse);
                                                        }
                                                    }
                                                }
                                            }else{
                                                nurseName.setText("");
                                            }
                                        }
                                        if (data.has("Surgeon"))
                                        {
                                            getSurgeon = data.getString("Surgeon");
                                            if (getSurgeon.isEmpty()){
                                                surgeonName.setText("");
                                            }
                                            if (getSurgeon.trim().contains(" ")){
                                                String[] Surgeon = getSurgeon.split("\\s+");
                                                surgeonName.setText(Surgeon[0] + "\n" + " " +  Surgeon[1]);
                                            }else{
                                                surgeonName.setText(getSurgeon);
                                            }
                                        }
                                        if (data.has("Hospitalist"))
                                        {
                                            getHospitalist = data.getString("Hospitalist");
                                            if (getHospitalist.isEmpty()){
                                                hospitaList.setText("");
                                            }
                                            if (getHospitalist.trim().contains(" ")){
                                                String[] hospitalist = getHospitalist.split("\\s+");
                                                hospitaList.setText(hospitalist[0] + "\n" + " " + hospitalist[1]);
                                            }else{
                                                hospitaList.setText(getHospitalist);
                                            }
                                        }
                                        if (data.has("note")) //Precaution //ex:202,301,501
                                        {
                                            getPrecaution = data.getString("note");
                                            String[] Precaution = getPrecaution.split(","); // split","
                                            int slen = Precaution.length;
                                            getPrecaution = "";
                                            for (int i = 0; i < 3 && i < slen; i++) {
                                                getPrecaution +=Precaution[i];
                                            }
                                            if (!getPrecaution.equals(PrecautionShow)){  //20200313
                                                PrecautionShow = getPrecaution;
                                                precaution1.setVisibility(View.GONE);
                                                precaution2.setVisibility(View.GONE);
                                                precaution3.setVisibility(View.GONE);

                                            if (getPrecaution != null) {
                                                for (int i = 0; i < slen; i++) {
                                                    if (i == 0) {
                                                        setIcon(Precaution[i], precaution1);
//                                                        precaution1.setVisibility(View.VISIBLE);         //顯示
                                                        precaution1.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(500);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                precaution1.setVisibility(View.VISIBLE);
                                                            }
                                                        });

                                                    } else if (i == 1) {
                                                        setIcon(Precaution[i], precaution2);
//                                                        precaution2.setVisibility(View.VISIBLE);
                                                        precaution2.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(500);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                precaution2.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                    } else if (i == 2) {
                                                        setIcon(Precaution[i], precaution3);
//                                                        precaution3.setVisibility(View.VISIBLE);
                                                        precaution3.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(500);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                precaution3.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        }
                                        if (data.has("goal"))
                                        {
                                            getGoals = data.getString("goal");
                                            goals.setText(getGoals);
                                        }
                                        Log.d(TAG, "IsInfoLayout out = " + IsInfoLayout); //20200309 debug
                                        if(!IsInfoLayout) {
                                            IsInfoLayout = true;
                                            //20200306
                                            FullEmptyLayout.setVisibility(View.GONE);     //空床Layout關閉
                                            //20200306
                                            fullScreenLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d(TAG, "fullScreenLayout IsInfoLayout = " + IsInfoLayout);
                                                    fullScreenLayout.setVisibility(View.VISIBLE);  //九宮格顯示
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        Log.d(TAG, "GetPatientInfo_Url_json 失敗 1= " + e.getMessage());
                                    }
                                }
                            });
                            else {
                                patient_err_msg = rtn_message;  //從EmptyActivity回來...
                                Log.d(TAG, "GetPatientInfo_Url_json 失敗 2 = " + rtn_message);
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "GetPatientInfo_Url_json 失敗 3 = " + e.getMessage());
                        }
                    } else {
                        patient_err_msg = getString(R.string.network_error);
                        Log.d(TAG, "GetPatientInfo_Url_json network_error = " + patient_err_msg);
                    }
                    //20200227
                    if(patient_err_msg.equalsIgnoreCase((getString(R.string.network_error)))) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(TAG, "GetPatientInfo_Url_json & patient_err_msg error");
                                Toask("Connect WebApi Network Error !! ", 150);
                            }
                        });
                    }
                }
            }.start();
        }
    }

    //20200210 將Precaution's icon match Array Fxn
    private void setIcon(String iconName, ImageView imageView) {
        String precautionName =  "icon_" + iconName;
        int iconResId = getResources().getIdentifier(precautionName, "mipmap", getPackageName());
        imageView.setImageResource(iconResId);
    }

    //取得病患用藥行程
    private void GetPatientSchedule_Url_json() {
        new Thread() {
            public void run() {
                String jsonFullURL = IPAddress + "/EPD42DEMO/api/GetPatientSchedule"; //Bell's
                Log.d(TAG, "GetPatientSchedule_Url_jsonFullURL = " + jsonFullURL);

                JSONObject post_jsonObject = new JSONObject();
                String accountNo = getSharedPreferences("EPD42", 0).getString("accountNo", null);
                try {
                    post_jsonObject.accumulate("apiName", "getPatientSchedule");
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                    post_jsonObject.accumulate("accountNo", accountNo);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                Log.d(TAG ,"GetPatientSchedule_Url:" + post_jsonObject.toString()); //20200203

                String result ;
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                Log.d(TAG, "GetPatientSchedule_Url_json Result = " + result); //20200203

                schedule_err_msg = "";
                if (result != null && !("".equalsIgnoreCase(result))) {
                    try {
                        GlobalClass.schedule_jsonObject = new JSONObject(result);
                        String rtn_result = GlobalClass.schedule_jsonObject.getString("result");
                        String rtn_message = "";
                        if(GlobalClass.schedule_jsonObject.has("message")){
                            rtn_message = GlobalClass.schedule_jsonObject.getString("message");
                        }
                        if (rtn_result.equalsIgnoreCase("Y") ) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONArray scheduleArray = GlobalClass.schedule_jsonObject.getJSONArray("data");

                                        String schedule1 = "";
                                        String schedule2 = "";
                                        String schedule3 = "";
                                        String scheduleDate1 = "";
                                        String scheduleDate2 = "";
                                        String scheduleDate3 = "";
                                        String scheduleRead1 = "";
                                        String scheduleRead2 = "";
                                        String scheduleRead3 = "";

                                        String medication1 = "";
                                        String medication2 = "";
                                        String medicationDate1 = "";
                                        String medicationDate2 = "";
                                        String medicationTime1 = "";
                                        String medicationTime2 = "";
                                        String painAssessment1 = "";      //last pain assessment
                                        String painAssessment2 = "";      //next pain assessment
                                        String painAssessmentDate1 = "";
                                        String painAssessmentDate2 = "";
                                        String painAssessmentTime1 = "";
                                        String painAssessmentTime2 = "";
                                        Date medicationDate1Date = null; //last taken
                                        Date medicationDate2Date = null; //next taken
                                        Date painAssessmentScheduleDate1 = null; //last pain assessment
                                        Date painAssessmentScheduleDate2 = null; //next pain assessment

                                        //Surgical 20200215
                                        String SurgicalScheduleTitle = "";
                                        String SuricalScheduleTime = "";
                                        String SuricalScheduleDate = "";
                                        Date SurgicalScheduleDate1 = null;
                                        Date SurgicalScheduleDate2 = null;

                                        Date nowDate = new Date();  //取得當日的日期&時間

                                        SimpleDateFormat sdf_today = new SimpleDateFormat("yyyy/MM/dd");
                                        String today_str = sdf_today.format(nowDate);

                                        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");            //12小時制
                                        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy");

                                        SimpleDateFormat medication_sdf1 = new SimpleDateFormat("MM/dd");
                                        SimpleDateFormat medication_sdf2 = new SimpleDateFormat("hh:mm a"); //24小時制

                                        if (scheduleArray.length() == 0){
                                            Log.d(TAG, "scheduleArray Empty !!");
                                            surgicalTitle.setText("No Surgical");
                                            surgicalTime.setText("");
                                            surgicalDate.setText("");
                                            painAssessment.setText(R.string.no_schedule);
                                            painAssessment.setBackgroundResource(R.color.fontColor);     //20200303
                                            painAssessment.setBackgroundResource(R.color.contentsColor); //20200303
                                            takePainAssessment.setText("");
                                            painSchedule.setVisibility(View.INVISIBLE);
                                        }

                                        for (int i = 0; i < scheduleArray.length() ; i++) {
                                            JSONObject item_jsonObject = scheduleArray.getJSONObject(i);
                                            String scheduleTitle = item_jsonObject.getString("scheduleTitle");
                                            String scheduleDate = item_jsonObject.getString("scheduleDate");
                                            String scheduleType = item_jsonObject.getString("scheduleType");
                                            if(scheduleDate == null){
                                                scheduleDate = "";
                                            }

                                            //病患手術提醒 scheduleType = "B"
                                            if (scheduleType.equalsIgnoreCase("B")){
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date SurgicalTimeDate = sdf.parse(scheduleDate);  //來自webAPI的日期

                                                if (SurgicalTimeDate.after(nowDate)) {
                                                    if (SurgicalScheduleDate2 == null) {  //未來的時間點
                                                        SurgicalScheduleDate2 = SurgicalTimeDate;
                                                        SurgicalScheduleTitle = scheduleTitle;
                                                        SuricalScheduleTime = medication_sdf2.format(SurgicalTimeDate);
                                                        SuricalScheduleDate = sdf_today.format(SurgicalTimeDate);
                                                        if (SuricalScheduleDate.contains(today_str)){
                                                            surgicalDate.setText(R.string.surgical_today);
                                                        }else {
                                                            surgicalDate.setText(R.string.surgical_tomorrow);
                                                        }
                                                        surgicalTitle.setText(SurgicalScheduleTitle);
                                                        surgicalTime.setText(SuricalScheduleTime);

//                                                        Log.d(TAG, "Surgical_B_Title_1: " + SurgicalScheduleTitle);
//                                                        Log.d(TAG, "Surgical_B_Date_1: " + SuricalScheduleDate);
//                                                        Log.d(TAG, "Surgical_B_Time_1: " + SuricalScheduleTime);
                                                    }else if (SurgicalTimeDate.before(SurgicalScheduleDate2)){ //最接近目前的時間點
                                                        SurgicalScheduleDate2 = SurgicalTimeDate;
                                                        SurgicalScheduleTitle = scheduleTitle;
                                                        SuricalScheduleTime = medication_sdf2.format(SurgicalTimeDate);
                                                        SuricalScheduleDate = sdf_today.format(SurgicalTimeDate);
                                                        if (SuricalScheduleDate.contains(today_str)){
                                                            surgicalDate.setText(R.string.surgical_today);
                                                        }else {
                                                            surgicalDate.setText(R.string.surgical_tomorrow);
                                                        }
                                                        surgicalTitle.setText(SurgicalScheduleTitle);
                                                        surgicalTime.setText(SuricalScheduleTime);

//                                                        Log.d(TAG, "Surgical_B_Title_2: " + SurgicalScheduleTitle);
//                                                        Log.d(TAG, "Surgical_B_Date_2: " + SuricalScheduleDate);
//                                                        Log.d(TAG, "Surgical_B_Time_2: " + SuricalScheduleTime);
                                                    }
                                                }else{
                                                    if (SurgicalScheduleDate1 == null) {  //已過目前的時間點
                                                        SurgicalScheduleDate1 = SurgicalTimeDate;
                                                        SurgicalScheduleTitle = scheduleTitle;
                                                        SuricalScheduleTime = medication_sdf2.format(SurgicalTimeDate);
                                                        SuricalScheduleDate = sdf_today.format(SurgicalTimeDate);

//                                                        Log.d(TAG, "Surgical_B_Title_3: " + SurgicalScheduleTitle);
//                                                        Log.d(TAG, "Surgical_B_Date_3: " + SuricalScheduleDate);
//                                                        Log.d(TAG, "Surgical_B_Time_3: " + SuricalScheduleTime);

                                                    }else if (SurgicalTimeDate.after(SurgicalScheduleDate1)){ //已過目前的時間點
                                                        SurgicalScheduleDate1 = SurgicalTimeDate;
                                                        SurgicalScheduleTitle = scheduleTitle;
                                                        SuricalScheduleTime = medication_sdf2.format(SurgicalTimeDate);
                                                        SuricalScheduleDate = sdf_today.format(SurgicalTimeDate);

//                                                        Log.d(TAG, "Surgical_B_Title_4: " + SurgicalScheduleTitle);
//                                                        Log.d(TAG, "Surgical_B_Date_4: " + SuricalScheduleDate);
//                                                        Log.d(TAG, "Surgical_B_Time_4: " + SuricalScheduleTime);
                                                    }

//                                                    Log.d(TAG, "Surgical_B_5: " + SurgicalScheduleTitle );
//                                                    Log.d(TAG, "Surgical_B_5_D1: " + SurgicalScheduleDate1 );
//                                                    Log.d(TAG, "Surgical_B_5_D2: " + SurgicalScheduleDate2 );
                                                    surgicalTitle.setText(R.string.no_surgical);
                                                    surgicalTime.setText("");
                                                    surgicalDate.setText("");
                                                }

//                                                if(!(SurgicalScheduleTitle.equalsIgnoreCase(""))){
//                                                    Log.d(TAG, "SurgicalScheduleTitle Today or Tomorrow");
////                                                  surgicalTitle.setText(SurgicalScheduleTitle);
//                                                    surgicalTime.setText(SuricalScheduleTime);
//                                                    if (SuricalScheduleDate.contains(today_str)){
//                                                        surgicalDate.setText(R.string.surgical_today);
//                                                    }else{
//                                                        surgicalDate.setText(R.string.surgical_tomorrow);
//                                                    }
//                                                }else{
//                                                    Log.d(TAG, "surgical is no Surgical_1 !! ");
//                                                    surgicalTitle.setText(R.string.no_surgical);
//                                                    surgicalTime.setText("");
//                                                    surgicalDate.setText("");
//                                                }
                                            }else{
//                                                Log.d(TAG, "surgical is no Surgical_2 !! ");
//                                                surgicalTitle.setText(R.string.no_surgical);
//                                                surgicalTime.setText("");
//                                                surgicalDate.setText("");
                                            }

                                            //病患疼痛評估schedule 20200211
                                            if (scheduleType.equalsIgnoreCase("E"))
                                            {
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date painAssessmentDateTime = sdf.parse(scheduleDate);  //來自webAPI的日期
//                                                Log.d(TAG, "schedule_E_Title = " + scheduleTitle);
//                                                Log.d(TAG, "schedule_E_DateTime: " + painAssessmentDateTime);
                                                if (painAssessmentDateTime.after(nowDate))  //next assessment schedule
                                                {
                                                    if (painAssessmentScheduleDate2 == null) {
                                                        painAssessmentScheduleDate2 = painAssessmentDateTime;
                                                        painAssessment2 = scheduleTitle;
                                                        painAssessmentDate2 = medication_sdf1.format(painAssessmentDateTime);
                                                        painAssessmentTime2 = medication_sdf2.format(painAssessmentDateTime);
                                                    }else if (painAssessmentDateTime.before(painAssessmentScheduleDate2)){
                                                        painAssessmentScheduleDate2 = painAssessmentDateTime;
                                                        painAssessment2 = scheduleTitle;
                                                        painAssessmentDate2 = medication_sdf1.format(painAssessmentDateTime);
                                                        painAssessmentTime2 = medication_sdf2.format(painAssessmentDateTime);
                                                    }
                                                }else{
                                                    if (painAssessmentScheduleDate1 == null){
                                                        painAssessmentScheduleDate1 = painAssessmentDateTime;
                                                        painAssessment1 = scheduleTitle;
                                                        painAssessmentDate1 = medication_sdf1.format(painAssessmentDateTime);
                                                        painAssessmentTime1 = medication_sdf2.format(painAssessmentDateTime);
                                                    }else if(painAssessmentDateTime.after(painAssessmentScheduleDate1)){
                                                        painAssessmentScheduleDate1 = painAssessmentDateTime;
                                                        painAssessment1 = scheduleTitle;
                                                        painAssessmentDate1 = medication_sdf1.format(painAssessmentDateTime);
                                                        painAssessmentTime1 = medication_sdf2.format(painAssessmentDateTime);
                                                    }
                                                }
                                            }

                                            if(!(painAssessment2.equalsIgnoreCase(""))){
//                                                Log.d(TAG,"painAssessment2 = " + painAssessment2 );
                                                painAssessment.setText(painAssessment2);
                                                takePainAssessment.setText(painAssessmentTime2);
                                                painSchedule.setVisibility(View.VISIBLE);
                                            }else{
//                                                Log.d(TAG,"painAssessment is No Schedule = " + painAssessment2 );
                                                painAssessment.setText(R.string.no_schedule);
                                                takePainAssessment.setText("");
                                                painSchedule.setVisibility(View.GONE);
                                            }

                                            //scheduleType = D : 病患用藥紀錄
                                            if(scheduleType.equalsIgnoreCase("D")) {
                                                //用藥
                                                try {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                    Date scheduleStTimeDate = sdf.parse(scheduleDate);  //來自webAPI的日期
//                                                    Log.d(TAG, "schedule_D_Title = " + scheduleTitle);
//                                                    Log.d(TAG, "schedule_D_DateTime: " + scheduleStTimeDate);
                                                    if (scheduleStTimeDate.after(nowDate)){ //Next Taken
                                                        if(medicationDate2Date == null){
                                                            medicationDate2Date = scheduleStTimeDate;
                                                            medication2 = scheduleTitle;
                                                            medicationDate2 = medication_sdf1.format(scheduleStTimeDate);
                                                            medicationTime2 = medication_sdf2.format(scheduleStTimeDate);
                                                        }else if(scheduleStTimeDate.before(medicationDate2Date)){
                                                            medicationDate2Date = scheduleStTimeDate;
                                                            medication2 = scheduleTitle;
                                                            medicationDate2 = medication_sdf1.format(scheduleStTimeDate);
                                                            medicationTime2 = medication_sdf2.format(scheduleStTimeDate);
                                                        }
                                                    }else{ //之前..
                                                        if(medicationDate1Date == null) {
                                                            medicationDate1Date = scheduleStTimeDate;                       //last taken/s data&Time
                                                            medication1 = scheduleTitle;                                    //last taken's name
                                                            medicationDate1 = medication_sdf1.format(scheduleStTimeDate);  //last taken's date
                                                            medicationTime1 = medication_sdf2.format(scheduleStTimeDate);  //last taken's time
                                                        }else if(scheduleStTimeDate.after(medicationDate1Date)){ //webapi日期跟last taken日期比較..
                                                            medicationDate1Date = scheduleStTimeDate;
                                                            medication1 = scheduleTitle;
                                                            medicationDate1 = medication_sdf1.format(scheduleStTimeDate);
                                                            medicationTime1 = medication_sdf2.format(scheduleStTimeDate);
                                                        }
                                                    }

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Log.d(TAG + "ParseException 失敗", "" + e.getMessage());
                                                }
                                            }else{
                                                //行程
                                                try {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                                    Date scheduleStTimeDate = sdf.parse(scheduleDate);

                                                    if (scheduleStTimeDate.after(nowDate)){
                                                        if(scheduleDate.contains(today_str)){
                                                            scheduleDate = sdf1.format(scheduleStTimeDate);
                                                        }else{
                                                            scheduleDate = sdf2.format(scheduleStTimeDate) +"\n" + sdf1.format(scheduleStTimeDate);
                                                        }
                                                        if(schedule1.equalsIgnoreCase("")){
                                                            schedule1 = scheduleTitle;
                                                            scheduleDate1 = scheduleDate;
//                                                            scheduleRead1 = scheduleRead;
                                                        }else if(schedule2.equalsIgnoreCase("")){
                                                            schedule2 = scheduleTitle;
                                                            scheduleDate2 = scheduleDate;
//                                                            scheduleRead2 = scheduleRead;
                                                        }else if(schedule3.equalsIgnoreCase("")){
                                                            schedule3 = scheduleTitle;
                                                            scheduleDate3 = scheduleDate;
//                                                            scheduleRead3 = scheduleRead;
                                                        }
                                                    }

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    GlobalClass.myLoge(TAG + "ParseException 失敗", "" + e.getMessage());
                                                }
                                            }
                                        } //end scheduleArray.length()

                                        //用藥
                                        if(!(medication1.equalsIgnoreCase(""))){ //Last Taken
                                            lastTakenDate.setText(medicationDate1);
                                            lastTakenTime.setText(medicationTime1);
                                            lastTakenDrug.setText(medication1);
                                        }else{
                                            lastTakenDate.setText("");
                                            lastTakenTime.setText("");
                                            lastTakenDrug.setText("");
                                            medicationLeft.setBackgroundResource(R.color.fontColor);
                                            medicationLeft.setBackgroundResource(R.color.contentsColor);
                                        }
                                        if(!(medication2.equalsIgnoreCase(""))){ //Next Taken
                                            nextTakenDate.setText(medicationDate2);
                                            nextTakenTime.setText(medicationTime2);
                                            nextTakenDrug.setText(medication2);
                                        }else{
                                            nextTakenDate.setText("");
                                            nextTakenTime.setText("");
                                            nextTakenDrug.setText("");
                                            medicationRight.setBackgroundResource(R.color.fontColor);
                                            medicationRight.setBackgroundResource(R.color.contentsColor);
                                        }
                                    } catch (Exception e) {
                                        GlobalClass.myLoge(TAG + "GetPatientSchedule_Url_json 失敗", "" + e.getMessage());
                                    }
                                }});
                        } else {
                            schedule_err_msg = rtn_message;
                            Log.d(TAG, "GetPatientSchedule_Url_json 失敗 = " + rtn_message);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "GetPatientSchedule_Url_json 失敗 = " + e.getMessage());
                    }
                }else{
                    schedule_err_msg = getString(R.string.network_error);
                    Log.d(TAG, "GetPatientSchedule_Url_json 失敗 = " + schedule_err_msg);
                }

                if(schedule_err_msg.equalsIgnoreCase(getString(R.string.network_error))) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toask("Connect WebApi Network Error !!", 150);
                        }
                    });
                }
            }
        }.start();
    }

    //取得病患量測指數
    private void GetLastVitalSign_Url_json() {
        new Thread() {
            public void run() {
                String jsonFullURL = IPAddress + "/EPD42DEMO/api/GetLastVitalSign";   //Bell's
                Log.d(TAG, "GetLastVitalSign_Url = " + jsonFullURL);

                JSONObject post_jsonObject = new JSONObject();
                String accountNo = getSharedPreferences("EPD42", 0).getString("accountNo", null);
                try {
                    post_jsonObject.accumulate("apiName", "getLastVitalSign");
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                    post_jsonObject.accumulate("accountNo", accountNo);
                    post_jsonObject.accumulate("period", "1h");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d(TAG + "post_jsonObject", "" + post_jsonObject.toString());

                final String result ;
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                Log.d(TAG, "GetLastVitalSign_Url_json Result = " + result);

                lastVisi_err_msg = "";
                if (result != null && !("".equalsIgnoreCase(result))) {
                    try {
                        GlobalClass.lastVisi_jsonObject = new JSONObject(result);
                        String rtn_result = GlobalClass.lastVisi_jsonObject.getString("result");
                        String rtn_message = "";
                        if(GlobalClass.lastVisi_jsonObject.has("message")){
                            rtn_message = GlobalClass.lastVisi_jsonObject.getString("message");
                        }
                        if (rtn_result.equalsIgnoreCase("Y") ) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        String date = GlobalClass.lastVisi_jsonObject.getString("observationTime");
//                                        Log.d(TAG, "lastVisi_jsonObject = " + date);  //20200225

                                        if (date.contains("null") ){ //20200225
                                            vitalSignTime.setText("");
                                            vitalSignTime.setText("");
                                            heartValue.setText("");
                                            respValue.setText("");
                                            spo2Value.setText("");
                                            skinValue.setText("");
                                            nibpValue.setText("");
                                            mmhgValue.setText("");
                                            blankLastVital();  //刷屏 20200303
                                        }

                                        if (date != null) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                            Date d = sdf.parse(date);
                                            String observationTime = "";
                                            if (d != null) {
//                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy HH:mm");
                                                observationTime = sdf2.format(d);
                                            }
                                            vitalSignTime.setText(observationTime);
                                        }

                                        JSONArray dataArray = GlobalClass.lastVisi_jsonObject.getJSONArray("data");

                                        String HR_value = "";
                                        String NIBP_value = "";
                                        String mmHg_value = "";
                                        String RESP_value = "";
                                        String SpO2_value = "";
                                        String TEMP_value = "";

                                        for (int i = 0; i < dataArray.length() ; i++) {
                                            JSONObject item_jsonObject = dataArray.getJSONObject(i);
                                            String id = item_jsonObject.getString("id");
                                            String value = item_jsonObject.getString("value");
                                            String unit = item_jsonObject.getString("unit");
//
                                            if(i == 0 /* && id.equalsIgnoreCase("HR")*/){  //心跳
                                                HR_value = value;
                                                if (HR_value.length() < 3 && !(HR_value.isEmpty())){ //字串長度若小於3就在前面補0
                                                    heartValue.setText("0" + HR_value);
                                                }else{
                                                    heartValue.setText(HR_value);
                                                }
                                            }else if(i == 1 /* && id.equalsIgnoreCase("RESP") */){
                                                RESP_value = value;
                                                if (RESP_value.length() < 3 && !(RESP_value.isEmpty())){ //字串長度若小於3就在前面補0
                                                    respValue.setText("0" + RESP_value);
                                                }else{
                                                    respValue.setText(RESP_value);
                                                }
                                            }else if(i == 2 /* && id.equalsIgnoreCase("SpO2%") */){
                                                SpO2_value = value;
                                                spo2Value.setText(SpO2_value);
                                            }else if(i == 3){
                                                TEMP_value = value;
                                                skinValue.setText(TEMP_value);
                                            }else if(i == 4 /* && id.equalsIgnoreCase("NIBP") */){
                                                NIBP_value = value;
                                                nibpValue.setText(NIBP_value);
                                            }else if(i == 5 /* && id.equalsIgnoreCase("NIBP2") */){
                                                mmHg_value = value;
                                                mmhgValue.setText(mmHg_value);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d(TAG, "GetLastVitalSign_Url_json 失敗 = " + e.getMessage());
                                    }
                                }});

                        } else {
                            lastVisi_err_msg = rtn_message;
                            Log.d(TAG, "GetLastVitalSign_Url_json 失敗 = " + rtn_message);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "GetLastVitalSign_Url_json 失敗 = " + e.getMessage());
                    }
                }else{
                    lastVisi_err_msg = getString(R.string.network_error);
                    Log.d(TAG, "GetLastVitalSign_Url_json network error = " + lastVisi_err_msg);
                }
                //20200227
                if(lastVisi_err_msg.equalsIgnoreCase(getString(R.string.network_error))) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toask("Connect WebApi Network Error !!", 150);
                        }
                    });
                }
            }
        }.start();
    }

    private void blankLastVital() {
        heartValue.setBackgroundResource(R.color.fontColor);
        respValue.setBackgroundResource(R.color.fontColor);
        spo2Value.setBackgroundResource(R.color.fontColor);
        skinValue.setBackgroundResource(R.color.fontColor);
        nibpValue.setBackgroundResource(R.color.fontColor);
        mmhgValue.setBackgroundResource(R.color.fontColor);
        heartValue.setBackgroundResource(R.color.dialogColor);
        respValue.setBackgroundResource(R.color.dialogColor);
        spo2Value.setBackgroundResource(R.color.dialogColor);
        skinValue.setBackgroundResource(R.color.dialogColor);
        nibpValue.setBackgroundResource(R.color.dialogColor);
        mmhgValue.setBackgroundResource(R.color.dialogColor);
    }

    //取得病患疼痛指數
    private void GetPainAssessment_Url_json() {
        new Thread() {
            public void run() {
                String jsonFullURL = IPAddress + "/EPD42DEMO/api/GetPainAssessment";  //Bell's

                Log.d(TAG, "GetPainAssessment_Url_json_jsonFullURL = " + jsonFullURL);

                JSONObject post_jsonObject = new JSONObject();
                String accountNo = getSharedPreferences("EPD42", 0).getString("accountNo", null);
                try {
                    post_jsonObject.accumulate("apiName", "getPainAssessment");
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                    post_jsonObject.accumulate("accountNo", accountNo);
                    post_jsonObject.accumulate("pageOffset", "0");
                    post_jsonObject.accumulate("recordPerPage", "99999");
                    post_jsonObject.accumulate("pages", "1");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d(TAG + "post_jsonObject", "" + post_jsonObject.toString());

                String result ;
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                Log.d(TAG ,"GetPainAssessment_Url Result = " + result);

                pain_assessment_err_msg = "";
                if (result != null && !("".equalsIgnoreCase(result))) {
                    try {
                        GlobalClass.pain_assessment_jsonObject = new JSONObject(result);
                        String rtn_result = GlobalClass.pain_assessment_jsonObject.getString("result");
                        String rtn_message = "";
                        if(GlobalClass.pain_assessment_jsonObject.has("message")){
                            rtn_message = GlobalClass.pain_assessment_jsonObject.getString("message");
                        }
                        if (rtn_result.equalsIgnoreCase("Y") ) runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    GlobalClass.myLoge(TAG + "GlobalClass.language:", "" + GlobalClass.language);

                                    //20200225
                                    if(GlobalClass.pain_assessment_jsonObject.getJSONArray("painRecord").length() == 0){
                                        painLevelText.setText("");
                                        painLevelIcon.setVisibility(View.INVISIBLE);        //表情圖hide 20200225
                                        painRatio.setText("");
                                        painLevelTime.setText("");
                                        painPosition.setVisibility(View.INVISIBLE);                 //人形圖hide  20200225
                                        painPosition.setBackgroundResource(R.color.contentsColor);  //人形圖刷屏  20200303
                                        painlevelLeft.setBackgroundResource(R.color.contentsColor); //表情塗刷屏  20200303
                                    }

                                    if(GlobalClass.pain_assessment_jsonObject.getJSONArray("painRecord").length() > 0){
                                        JSONObject data = GlobalClass.pain_assessment_jsonObject.getJSONArray("painRecord").getJSONObject(0);
                                        pain_assessment_SerialID = data.getString("SerialID");
                                        String PainDateTime = data.getString("PainDateTime");
                                        String PainLevel = data.getString("PainLevel");
                                        String PainPosition = data.getString("PainPosition");
                                        String PainType = data.getString("PainType");

                                        //Pain Level 針對pain level 填入相對的image和數字比例 20200204
                                        if(PainLevel != null && !(PainLevel.equalsIgnoreCase(""))){
                                            switch (PainLevel){
                                                case "1":
                                                    painLevelText.setText(R.string.one_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv01);
                                                    painRatio.setText(R.string.one);
                                                    break;
                                                case "2":
                                                    painLevelText.setText(R.string.two_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv02);
                                                    painRatio.setText(R.string.two);
                                                    break;
                                                case "3":
                                                    painLevelText.setText(R.string.three_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv03);
                                                    painRatio.setText(R.string.three);
                                                    break;
                                                case "4":
                                                    painLevelText.setText(R.string.four_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv04);
                                                    painRatio.setText(R.string.four);
                                                    break;
                                                case "5":
                                                    painLevelText.setText(R.string.five_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv05);
                                                    painRatio.setText(R.string.five);
                                                    break;
                                                case "6":
                                                    painLevelText.setText(R.string.six_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv06);
                                                    painRatio.setText(R.string.six);
                                                    break;
                                                case "7":
                                                    painLevelText.setText(R.string.seven_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv07);
                                                    painRatio.setText(R.string.seven);
                                                    break;
                                                case "8":
                                                    painLevelText.setText(R.string.eight_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv08);
                                                    painRatio.setText(R.string.eight);
                                                    break;
                                                case "9":
                                                    painLevelText.setText(R.string.nine_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv09);
                                                    painRatio.setText(R.string.nine);
                                                    break;
                                                case "10":
                                                    painLevelText.setText(R.string.ten_levle);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv10);
                                                    painRatio.setText(R.string.ten);
                                                    break;
                                                default:
                                                    painLevelText.setText(R.string.zero_level);
                                                    painLevelIcon.setImageResource(R.mipmap.icon_pv00);
                                                    painRatio.setText(R.string.zero);
                                            }

                                            String pain_text_str = "";
                                            if(GlobalClass.painLevelOptionArray != null){
                                                try{
                                                    Integer pain = Integer.valueOf(PainLevel);
                                                    JSONObject painLevelJSONObject = GlobalClass.painLevelOptionArray.getJSONObject(pain);
                                                    pain_text_str = painLevelJSONObject.getString("codeDscr"); //another webApi
                                                }catch (Exception e){
                                                }
                                            }
                                        }
                                        // 針對人形圖 PainPosition 填入疼痛點位置 20200304
                                        for (int i = 1; i <= 27 ;i++){ //刷新
                                            int resID = getResources().getIdentifier("F" + i, "id", getPackageName());
                                            ImageView imageView = findViewById(resID);
                                            imageView.setVisibility(View.VISIBLE);
                                        }

                                        for (int i = 1; i <= 22 ;i++){ //刷新
                                            int resID = getResources().getIdentifier("B" + i, "id", getPackageName());
                                            ImageView imageView = findViewById(resID);
                                            imageView.setVisibility(View.VISIBLE);
                                        }

                                        if(PainPosition != null && !(PainPosition.equalsIgnoreCase(""))){
                                            String[] PainPositions = PainPosition.split(",");
//                                            Log.d(TAG, "PainPositions Array = " + Arrays.toString(PainPositions));

                                            for ( String imageViewId : PainPositions){
                                                //第一個引數為ID名，第二個為資源屬性是ID或者是Drawable，第三個為包名。 //20200304
                                                int resID = getResources().getIdentifier(imageViewId, "id", getPackageName());
                                                ImageView imageView = findViewById(resID);
                                                imageView.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                            Date date = sdf.parse(PainDateTime);
                                            String getPainLevelDate = "";
                                            if (date != null){
                                                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy HH:mm");
                                                getPainLevelDate = sdf2.format(date);
                                            }
                                            painLevelTime.setText(getPainLevelDate);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            GlobalClass.myLoge(TAG + "ParseException 失敗", "" + e.getMessage());
                                        }
                                    }
                                } catch (Exception e) {
                                    GlobalClass.myLoge(TAG + "GetPainAssessment_Url_json 失敗", "" + e.getMessage());
                                }
                            }
                        });
                        else {
                            pain_assessment_err_msg = rtn_message;
                            Log.d(TAG, "GetPainAssessment_Url_json 失敗 = " + rtn_message);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "GetPainAssessment_Url_json 失敗 = " + e.getMessage());
                    }
                }else{
                    pain_assessment_err_msg = getString(R.string.network_error);
                    Log.d(TAG, "GetPainAssessment_Url_json network error = " + pain_assessment_err_msg);
                }

                if(pain_assessment_err_msg.equalsIgnoreCase(getString(R.string.network_error))){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toask("Connect WebApi Network Error !! ", 150);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_label_info_panel:
                if (handler != null){
                    handler.removeCallbacks(checkAccountNo);
                    handler.removeCallbacks(getJson);
                    handler.removeCallbacks(reflashLayout);
                }
                inputIPAddress();  //自行輸入ip 20200226
                break;
            case R.id.info_icon:   //info's icon 啟動thread
                if(handler != null){  //20200304
                    handler.removeCallbacks(checkAccountNo);
                    handler.removeCallbacks(getJson);
                }
                checkAccountNo.run();  //每30秒gGetDevicePairInfo
                getJson.run();         //每31秒get All json
                break;
            case R.id.s1_icon:    //取得病患相關資料
                fullScreenLayout.setVisibility(View.VISIBLE);  //九宮格顯示
                GetDevicePairInfo_Url_json();
                GetPatientInfo_Url_json();
                GetPatientSchedule_Url_json(); //20200303
                GetPainAssessment_Url_json();  //20200303
                GetLastVitalSign_Url_json();   //20200303
                break;
            case R.id.s5_icon:  //取得病患用藥行程記錄 20200203
                fullScreenLayout.setVisibility(View.VISIBLE);  //九宮格顯示
                relayScheduleContext.setBackgroundResource(R.color.dialogColor);
                relayMedicationContext.setBackgroundResource(R.color.dialogColor);
                GetDevicePairInfo_Url_json();
                GetPatientInfo_Url_json();
                GetPatientSchedule_Url_json();
                GetPainAssessment_Url_json();
                GetLastVitalSign_Url_json();
                break;
            case R.id.s7_icon:  //取得病患疼痛評估紀錄  20200204
                fullScreenLayout.setVisibility(View.VISIBLE);  //九宮格顯示
                relayPainLevelContext.setBackgroundResource(R.color.dialogColor);
                GetDevicePairInfo_Url_json();
                GetPatientInfo_Url_json();
                GetPainAssessment_Url_json();
                GetPatientSchedule_Url_json();
                GetLastVitalSign_Url_json();
                break;
            case R.id.s8_icon: //取得病患生理指數  20200131
                fullScreenLayout.setVisibility(View.VISIBLE);  //九宮格顯示
                relayVitalSignContext.setBackgroundResource(R.color.dialogColor);
                GetDevicePairInfo_Url_json();
                GetPatientInfo_Url_json();
                GetLastVitalSign_Url_json();
                GetPatientSchedule_Url_json();
                GetPainAssessment_Url_json();
                break;
            case R.id.s9_icon:
                //stop Thread fxn
                if(handler != null) {
                    handler.removeCallbacks(getJson);
                    handler.removeCallbacks(checkAccountNo);
                    handler.removeCallbacks(reflashLayout);
                }
                blankFullScreen();  //可避免EPD殘影
                reload();           //refresh
                break;
            case R.id.tv_goals: //跳到另一個專案首頁(暫時)
                if(handler != null) {
                    handler.removeCallbacks(getJson);
                    handler.removeCallbacks(checkAccountNo);
                    handler.removeCallbacks(reflashLayout);
                }
                fullScreenLayout.setVisibility(View.GONE);
                Intent secInt = new Intent(this, SecondActivity.class);
                startActivity(secInt);
                break;
            case R.id.iv_empty_bed: //空床imageview
//                Log.d(TAG, "onClick: iv_empty_bed");
                reload();
                break;
        }
    }

    //20200226 input ip address 自行輸入ip address
    private void inputIPAddress(){
        //關閉九宮格避免殘影情況 20200320
        fullScreenLayout.setVisibility(View.GONE);
        //對話框Dialog
        final Dialog IPdialog = new Dialog(MainActivity.this, R.style.popupDialog);
        IPdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        IPdialog.setContentView(R.layout.dialog_ipaddress);      //dialog xml
        WindowManager.LayoutParams lay = IPdialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
        final EditText dialogMessages = (EditText) IPdialog.findViewById(R.id.et_dialog_ipaddress);
        dialogMessages.setText(IPAddress);
        dialogMessages.setTextSize(55);
        dialogMessages.setSelection(IPAddress.length()); //游標會出現在最後
        Button send = (Button) IPdialog.findViewById(R.id.bt_dialog_send);
        Button cancel = (Button) IPdialog.findViewById(R.id.bt_dialog_cancel);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPAddress = dialogMessages.getText().toString();
                app_Setting.edit().putString("IPAddress", IPAddress).apply(); //寫入檔案
                GetDevicePairInfo_Url_json();
                IPdialog.dismiss();
                blankFullScreen();
                reload();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPdialog.dismiss();
                blankFullScreen();
                reload();
            }
        });

        IPdialog.show();
    }

    //20201030 Goals需要互動
    private void dialogGoals() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.popupDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_goal);      //dialog xml
        WindowManager.LayoutParams lay = dialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //禁止keyboard自動彈出

        EditText dialogMessages = (EditText) dialog.findViewById(R.id.et_dialog_goals);
        dialogMessages.setCursorVisible(false);                 //隱藏光標
        Button send = (Button) dialog.findViewById(R.id.bt_dialog_ok);
        Button cancel = (Button) dialog.findViewById(R.id.bt_dialog_cancel);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        dialog.show();
    }

    //20200313 僅刷新頁面
    private void reflashLayout(){
        fullScreenLayout.setVisibility(View.INVISIBLE);  //九宮格隱藏
        fullScreenLayout.post(new Runnable() {
            @Override
            public void run() {
                fullScreenLayout.setVisibility(View.VISIBLE); //九宮格顯示
            }
        });
    }

    //20200210
    public void reload() {  //重新讀取MainActivity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    //20200213 一次讀取所有的json(除了帳戶配對)
    public void getAllJson() {
        GetPatientInfo_Url_json();
        GetPatientSchedule_Url_json();
        GetPainAssessment_Url_json();
        GetLastVitalSign_Url_json();
    }

    //20200212 checkBox 分別設置於Schedule以及Goals
    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.cb_goals:
                if (checked) {
                    achieved.setText("Achieved");
                    achieved.setTextColor(Color.parseColor("#000000"));
                }
                else{
                    achieved.setText("UnDone");
                    achieved.setTextColor(Color.parseColor("#000000"));
                }
                break;
            case R.id.cb_schedule:
                if (checked) {
//                    Toask("Good Job", 915);
//                    painSchedule.setEnabled(false); //disable checkbox
                }
                break;
        }
    }

    //去跟後端註冊 (Demo時未用到)
    private void SendDeviceID_Url_json() {
        new Thread() {
            public void run() {
                String jsonFullURL = GlobalClass.server_real+GlobalClass.SendDeviceID_Url;

                JSONObject post_jsonObject = new JSONObject();

                try {
                    post_jsonObject.accumulate("apiName", "sendDeviceID");
                    post_jsonObject.accumulate("DeviceIDKey", AndroidId);
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d(TAG, "POST SendDeviceID_Url_json: " + post_jsonObject.toString());

                String result ;
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                Log.d(TAG, "SendDeviceID_Url_json Result = " + result);

                err_msg = "";
                if (result != null && !("".equalsIgnoreCase(result))) {
                    try {
                        jsonObject = new JSONObject(result);
                        String rtn_result = jsonObject.getString("result");
                        String rtn_message = "";
                        if(jsonObject.has("message")){
                            rtn_message = jsonObject.getString("message");
                        }
                        if (rtn_result.equalsIgnoreCase("Y") || rtn_result.equalsIgnoreCase("N") ) {
                            //GetDevicePairInfo_Url_json(); //取得病床與病患配對的資料 20200217
                        } else {
                            err_msg = rtn_message;
                            Log.d(TAG, "SendDeviceID_Url_json 失敗 = " + rtn_message);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "SendDeviceID_Url_json 失敗 = " + e.getMessage());
                    }
                }else{
                    Log.d(TAG, "SendDeviceID_Url_json 失敗 = " + err_msg);
                }

                if(!(err_msg.equals(""))){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d(TAG, "SendDeviceID_Url_json error messages = " + err_msg);
                        }
                    });
                }
            }
        }.start();
    }

    //取得病床與病患配對的資料
    private void GetDevicePairInfo_Url_json() {
        new Thread() {
            public void run() {
                String jsonFullURL = IPAddress + "/EPD42DEMO/api/GetDevicePairInfo";
                Log.d(TAG, "GetDevicePairInfo_json_FullURL : " + jsonFullURL);

                JSONObject post_jsonObject = new JSONObject();
                try {
                    post_jsonObject.accumulate("apiName", "getDevicePairInfo");
                    post_jsonObject.accumulate("DeviceIDKey", "epd42devicekey");
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d(TAG, "GetDevicePairInfo_Url_json POST = " + post_jsonObject.toString());

                String result;
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                Log.d(TAG, "GetDevicePairInfo_Url_json Result = " + result);

                err_msg = "";
                if (result != null && !("".equalsIgnoreCase(result))) {
                    try {
                        jsonObject = new JSONObject(result);
                        String rtn_result = jsonObject.getString("result");
                        String rtn_message = "";
                        if(jsonObject.has("message")){
                            rtn_message = jsonObject.getString("message");
                        }
                        if (rtn_result.equalsIgnoreCase("Y") || rtn_result.equalsIgnoreCase("N") ) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        String status = jsonObject.getString("status");
                                        if(status.equalsIgnoreCase("0")) {
//                                            SendDeviceID_Url_json();  //如果沒拿到資料就在去後台申請認證
                                        }else if(status.equalsIgnoreCase("9")) {
                                            if (jsonObject.has("data")) {
                                                String stationNo = "";
                                                String bedNo = "";
                                                String accountNo = "";
                                                String roomNo = "";
                                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                                JSONObject dataobj = dataArray.getJSONObject(0);
                                                if (dataobj.has("stationNo")) {
                                                    //非院方綁定設備則回傳空值
                                                    stationNo = dataobj.getString("stationNo");
                                                    app_Setting.edit().putString("stationNo", stationNo).apply();
                                                } else {
                                                    app_Setting.edit().putString("stationNo", "0").apply();
                                                }
                                                if (dataobj.has("bedNo")) {
                                                    //未配對床位則回傳空值
                                                    bedNo = dataobj.getString("bedNo");
                                                    app_Setting.edit().putString("bedNo", bedNo).apply();
                                                } else {
                                                    app_Setting.edit().putString("bedNo", "0").apply();
                                                }
                                                if (dataobj.has("accountNo")) {
                                                    //空床則回傳空值
                                                    accountNo = dataobj.getString("accountNo");
                                                    app_Setting.edit().putString("accountNo", accountNo).apply();
                                                } else {
                                                    app_Setting.edit().putString("accountNo", "0").apply();
                                                }
                                                if (dataobj.has("roomNo")) {
                                                    roomNo = dataobj.getString("roomNo");
                                                    app_Setting.edit().putString("roomNo", roomNo).apply();
                                                } else {
                                                    app_Setting.edit().putString("roomNo", "0").apply();
                                                }
                                            } else {
                                                Log.d(TAG, "GetDevicePairInfo_Url_json 失敗 : jsonObject data error");
                                            }

                                            if (jsonObject.has("token")) {
//                                                default_token = jsonObject.getString("token");
//                                                Log.d(TAG, "token is = " + default_token);
                                            } else {
                                                Log.d(TAG, "GetDevicePairInfo_Url_json 失敗 : jsonObject data error");
                                            }
                                        }else if (status.equalsIgnoreCase("2")){ //2 = 空床
                                            if(IsInfoLayout) {
                                                IsInfoLayout = false;
                                                fullScreenLayout.setVisibility(View.GONE);       //九宮格關閉
                                                FullEmptyLayout.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Thread.sleep(1500);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Log.d(TAG, "FullEmptyLayout 1");
                                                        FullEmptyLayout.setVisibility(View.VISIBLE);  //顯示
//                                                    FullEmptyLayout.post(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            try {
//                                                                Thread.sleep(500);
//                                                            } catch (InterruptedException e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                            Log.d(TAG, "FullEmptyLayout 2");
//                                                            FullEmptyLayout.setVisibility(View.GONE);  //九宮格顯示
//                                                            FullEmptyLayout.post(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    try {
//                                                                        Thread.sleep(200);
//                                                                    } catch (InterruptedException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                    Log.d(TAG, "FullEmptyLayout 3");
//                                                                    FullEmptyLayout.setVisibility(View.VISIBLE);  //九宮格顯示
//
//                                                                }
//                                                            });
                                                        // }

                                                        //});
                                                    }
                                                });
                                            }
                                        }else {
//                                            status_msg.setText(jsonObject.getString("message"));
//                                            set_press_any_key_flag(true);
                                        }
                                    } catch (JSONException e) {
                                        GlobalClass.myLoge(TAG + "GetDevicePairInfo_Url_json 失敗", "" + e.getMessage());
                                    }
                                }});
                        } else {
                            err_msg = rtn_message; //result = Y or N message
                            Log.d(TAG, "GetDevicePairInfo_Url_json messages 失敗 :" + err_msg);
                        }
                    } catch (JSONException e) {  //result != null
                        Log.d(TAG, "GetDevicePairInfo_Url_json 失敗 : " + e.getMessage());
                    }
                }else{
                    err_msg = getString(R.string.network_error);
                    Log.d(TAG, "GetDevicePairInfo_Url_json 失敗 : " + err_msg);
                }

                if(err_msg.equalsIgnoreCase(getString(R.string.network_error))){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toask("Connect WebApi Network Error !! ", 150);
                            networkMessages.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }.start();
    }

    //Toask顯示位置及內容
    private void Toask(String messages, int i) {
        Toast toast = Toast.makeText(MainActivity.this, messages, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 150, i);
        toast.show();
    }

    private void blankFullScreen(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        Button myButton = new Button(this);
        myButton.setEnabled(false);
        myButton.setBackgroundColor(getResources().getColor(R.color.fontColor));
        myButton.setLayoutParams(params);
        fullScreenLayout.addView(myButton);
    }

    //20200310
    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop handler 20200215
        handler.removeCallbacks(getJson);
        handler.removeCallbacks(checkAccountNo);
        handler.removeCallbacks(reflashLayout);
    }

}
