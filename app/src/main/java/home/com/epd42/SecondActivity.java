package home.com.epd42;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SecondActivity.class.getSimpleName();  //debug

    public ImageView advertisement;

    private TextView month,date,week;
    private TextView patientName, patientBedNo, patientAge;
    private TextView lastUpdate;
    private TextView announcement;
    private TextView lang_chinese;

    private Button ioCancel, ioSend;
    private EditText ioInput, ioOutput;

    private LinearLayout care_1,care_2, care_3;
    private RelativeLayout careDialogLayout;

    private LinearLayout fullNkcuLayout;
    private RelativeLayout careRecordLayout;

    private CareRecordDialog RecordDialog; //Dialog Customer
    private String CareStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //title隱藏
        setContentView(R.layout.activity_nkcu);

        initView();

        getDateInfo();

    }

    private void getDateInfo() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);           //年
        int weekday = cal.get(Calendar.DAY_OF_WEEK);  //星期
        String thisWeek = getWeek(weekday);           //今天星期幾
        String thisYear = Integer.toString(mYear);    //今年
        week.setText(thisYear + " " + thisWeek);      //xxxx xxxx
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");  //月/日
        String str_today = df.format(cal.getTime());   //今天(含月)
        String monthandday[] = str_today.split("/"); //以"/"分割
        String thisMonth = monthandday[0]; //月
        String thisdate = monthandday[1];  //日
        month.setText(thisMonth + "\n" + "月");
        date.setText(thisdate);
        String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        lastUpdate.setText("前次更新時間: " + nowDate);  //前次更新時間
    }

    @Nullable
    private String getWeek(int weekday) {
        String thisweek = null;
        if (weekday == 1)
        {
            thisweek = getString(R.string.sunday);
        }
        if (weekday == 2)
        {
            thisweek = getString(R.string.monday);
        }
        if (weekday == 3)
        {
            thisweek = getString(R.string.tuesday);
        }
        if (weekday == 4)
        {
            thisweek = getString(R.string.wednesday);
        }
        if (weekday == 5)
        {
            thisweek = getString(R.string.thursday);
        }
        if (weekday == 6)
        {
            thisweek = getString(R.string.friday);
        }
        if (weekday == 7)
        {
            thisweek = getString(R.string.saturday);
        }
        return thisweek;
    }

    private void initView() {
        month = (TextView) findViewById(R.id.tv_p_month); //月
        date = (TextView) findViewById(R.id.tv_p_date);   //日
        week = (TextView) findViewById(R.id.tv_yyweek);   //星期

        patientName = (TextView) findViewById(R.id.tv_p_name);    //病患姓名
        patientBedNo = (TextView) findViewById(R.id.tv_p_bed_no); //病床編號
        patientAge = (TextView) findViewById(R.id.tv_p_age);      //病患年紀

        lastUpdate = (TextView) findViewById(R.id.tv_p_updatetime); //最後更新日期

        care_1 = (LinearLayout) findViewById(R.id.ly_care_1);
        care_1.setOnClickListener(this);
        care_2 = (LinearLayout) findViewById(R.id.ly_care_2);
        care_2.setOnClickListener(this);
        care_3 = (LinearLayout) findViewById(R.id.ly_care_3);
        care_3.setOnClickListener(this);

        ioCancel = (Button) findViewById(R.id.bt_io_cancel);
        ioCancel.setOnClickListener(this);
        ioSend = (Button) findViewById(R.id.bt_io_send);
        ioSend.setOnClickListener(this);

        ioInput = (EditText) findViewById(R.id.et_io_input);
        ioOutput = (EditText) findViewById(R.id.et_io_output);

        lang_chinese = (TextView) findViewById(R.id.tv_lang_chinese);
        lang_chinese.setOnClickListener(this);

        careDialogLayout = (RelativeLayout) findViewById(R.id.ry_care_dialog);
        careRecordLayout = (RelativeLayout) findViewById(R.id.ry_care_record);
        fullNkcuLayout = (LinearLayout) findViewById(R.id.ly_nkcu_full);

        advertisement = (ImageView) findViewById(R.id.iv_ad);

        announcement = (TextView) findViewById(R.id.tv_ncku_announcement);
        announcement.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ncku_announcement: //最新公告
                //Read file for temp test 20200326
                String ret = "";
                try {
                    InputStream is = getResources().openRawResource(R.raw.base64_text);
                    if (is != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((receiveString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(receiveString);
                        }
                        is.close();
                        ret = stringBuilder.toString();
                        byte [] input = Base64.decode(ret, Base64.DEFAULT); //解碼
                        Bitmap bitmap = BitmapFactory.decodeByteArray(input, 0, input.length);
                        advertisement.setImageBitmap(bitmap);
                    }
                }catch (IOException e){
                    Log.e("readFromFile", "Can not read file: " + e.toString());
                }
                break;
            case R.id.ly_care_1:
                careRecordLayout.setVisibility(View.GONE);
                careDialogLayout.setVisibility(View.VISIBLE);
                ioInput.setText("");   //clear
                ioOutput.setText("");  //clear
                break;
            case R.id.ly_care_2:
                CareStr = "拍背/翻身";
                showNormalDialog();
                break;
            case R.id.ly_care_3:
                CareStr = "抽痰";
                showNormalDialog();
                break;
            case R.id.tv_lang_chinese: //中文顯示
                break;
            case R.id.bt_io_cancel: //攝取排出(取消)
            case R.id.bt_io_send:   //攝取排出(送出)
                hideKeyboard(SecondActivity.this);   //hide softkeyboard
                careDialogLayout.setVisibility(View.GONE);
                try {
                    Thread.sleep(1000);
                    careRecordLayout.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showNormalDialog() {
        RecordDialog = new CareRecordDialog(SecondActivity.this);
        RecordDialog.setTitle(CareStr);
        RecordDialog.setYesOnclickListener(new CareRecordDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                RecordDialog.dismiss();
            }
        });

        RecordDialog.setNoOnclickListener(new CareRecordDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                RecordDialog.dismiss();
            }
        });

        RecordDialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
