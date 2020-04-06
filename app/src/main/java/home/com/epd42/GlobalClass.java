package home.com.epd42;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import static home.com.epd42.MainActivity.default_token;
import static home.com.epd42.MainActivity.demo_for_epd42_token;

public class GlobalClass {
    static final String TAG = "GlobalClass";
    public static String hospitalName = "";

    public static String hospitalCode = "landmark";
    public static String domainType = buildingConfig.domainType;  //設定API主機
    public static String doRtcServer = "DF";  //設定webRTC 主機
    public static String language = "en";

    //public static boolean myTest = BuildConfig.myTest;       // 沒用了
    public static boolean myTestResult = false;              //測試階段,使用寫死json結果(正式上線要設false)
    public static boolean myTestYahooResult = false;         //測試階段,使用寫死json結果(正式上線要設false)
    public static boolean myTestPandoraResult = false;       //測試階段,使用寫死json結果(正式上線要設false)
    public static boolean myTestPandoraMusic = false;        //測試階段,使用寫死music播放(正式上線要設false)
    public static boolean useProxy = false;                  //測試階段,使用北美proxy(正式上線要設false)
    public static boolean myTestPandoraUserLogin = true;     //測試階段,用戶需打帳密登入Pandora(正式上線要設true)
    public static boolean myTestMyFavoritesResult = false;   //測試階段,不使用json重設MyFavorites(正式上線要設false)
    public static boolean myTestRemoteControll = false;      //測試階段,是否可不配對，而直接進行遠端控制(正式上線要設false)
    public static boolean myTestNotShowLocation = true;      //不顯示病人位置圖文(尚無對應的api)(目前上線要設true)
    public static boolean myTestPhoto = false;               //測試病人photo(正式上線要設false)
    public static boolean myTestAutoLogin = true;            //測試使用自動登入(正式上線要設true)
    public static boolean myTestTVResult = false;            //測試階段,使用寫死TV json結果(正式上線要設false)
    public static boolean myTestTVtuner = true;              //測試階段,使用 TVtuner(正式上線要設true)
    public static boolean myTestTVtunerNoEPG = false;        //測試階段,強制設定為無EPG data(正式上線要設false)
    public static boolean myTestTVtunerTestData = false;     //測試階段,使用 TVtuner寫死測試資料(正式上線要設false)
    public static boolean myTestTVtunerShowTestInfo = false; //測試階段,使用 TVtuner播放會顯示info(正式上線要設false)
    public static boolean myTestCameraSize = false;          //測試階段(目前上線要設false)
    public static boolean myTestSound = false;                //測試階段(正式上線要設true)
    public static boolean myTestPhone = false;               //測試階段(正式上線要設false)
    public static String fake_androidId = "";                //使用假的androidId(正式上線要設 空值)
    //public static String hospitalCode = "landmark";
    public static boolean myDemoMode = false;                   //處於demoMode=true(正式上線要設false)
    public static boolean myBackupTest = false;                 //Backup上面的參數(正式上線要設false)
    public static boolean myBackupTestResult = true;            //Backup上面的參數(正式上線要設true)
    public static boolean myBackupTestmyTestYahooResult = true; //Backup上面的參數(正式上線要設true)
    public static boolean myBackupTestPandoraResult = true;     //Backup上面的參數(正式上線要設true)
    public static boolean myBackupTestMyFavoritesResult = true; //Backup上面的參數(正式上線要設true)

    //public static String domainType = BuildConfig.domainType;             //設定API主機
    //    public static String domaidoRtcservernType = "USA";             //設定API主機
    //public static String doRtcServer = "DF";  //設定webrtc 主機
    public static String versionDate = "20191231";      //設定發版日期
    public static boolean webrtc_version_check = true;    //是否進行webrtc通知版本號check
    public static String webrtc_version = "1";            //webrtc通知版本號

    public static Map<String, Map<String, String>> serverMap = new HashMap<String, Map<String, String>>() {{
        put("DV", new HashMap<String, String>() {{
            put("server_test_ip", "REN-AP01.REN");
            put("server_real_ip", "REN-AP01.REN");
            put("server_test", "http://REN-AP01.REN:8090/SHASTV/api/");
            put("server_real", "http://REN-AP01.REN:8090/SHASTV/api/");
        }});
        put("RNN", new HashMap<String, String>() {{
            put("server_test_ip", "10.58.183.47");
            put("server_real_ip", "10.58.183.47");
            put("server_test", "http://10.58.183.47:8090/SHASTV/api/");
            put("server_real", "http://10.58.183.47:8090/SHASTV/api/");
        }});
        put("RN", new HashMap<String, String>() {{
            put("server_test_ip", "10.58.179.13");
            put("server_real_ip", "10.58.179.13");
            put("server_test", "http://10.58.179.13:8090/SHASTV/api/");
            put("server_real", "http://10.58.179.13:8090/SHASTV/api/");
        }});
        put("USAold", new HashMap<String, String>() {{
            put("server_test_ip", "210.63.205.141");
            put("server_real_ip", "210.63.205.141");
            put("server_test", "http://210.63.205.141:8090/SHASTV/api/");
            put("server_real", "http://210.63.205.141:8090/SHASTV/api/");
        }});
        put("USA", new HashMap<String, String>() {{
            put("server_test_ip", "rensha.renown.tw");
            put("server_real_ip", "rensha.renown.tw");
            put("server_test", "https://rensha.renown.tw:8090/SHASTV/api/");
            put("server_real", "https://rensha.renown.tw:8090/SHASTV/api/");
        }});
        put("DF", new HashMap<String, String>() {{
//            put("server_test_ip", "127.0.0.1");
//            put("server_real_ip", "127.0.0.1");
            put("server_real_ip", "http://10.58.179.13:8090/SHASTV/api/");
            put("server_real_ip", "http://10.58.179.13:8090/SHASTV/api/");
//            put("server_test", "http://127.0.0.1:8090/SHASTV/api/");
//            put("server_real", "http://127.0.0.1:8090/SHASTV/api/");
            put("server_test", "http://10.58.179.13:8090/SHASTV/api/");
            put("server_real", "http://10.58.179.13:8090/SHASTV/api/");
        }});
    }};

    public static String server_test = serverMap.get(domainType).get("server_test");
    public static String server_real = serverMap.get(domainType).get("server_real");

    public static String webrtc_server = "http://webrtc.shoppalz.com/api/v1/";
    public static Map<String, String[]> allRtcServer = new HashMap() {{
        put("default", new String[]{"https://rtc.shoppalz.com", "http://webrtc.shoppalz.com"});
        put("sunlyc", new String[]{"https://webrtc.sunlyc.com", "https://dev.sunlyc.com"});
        put("rn", new String[]{"https://renrtc.renown.tw", "https://renstv.renown.tw"});
//        put("DF", new String[]{"http://127.0.0.1", "http://127.0.0.1"});
        put("DF", new String[]{"http://10.58.179.13", "http://10.58.179.13"});
    }};

    public static String getPatientInfo_LoginID = "";
    public static String getPatientInfo_APPID = "";
    public static String getPatientInfo_token = "";
    public static String getPatientInfo_surgicalSite = "";
    public static String getPatientInfo_surgicalSiteID = "";

    public static String formal_token = "";
//    public static String formal_token = "gvLPUGu4~)V.@(UE![2D]AQK><p|+@[T%)vPDW*v!nkdln850.l({D#H#A*>+@<{";  //20200218 for demo

    public static int maxTimeOut = 10000;

    public static String dischargeDate = "";
    public static String admitDate = "";

    public static String fast_login_text = "123456";
    public static String fast_login_img_url = "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=https://play.google.com/store/apps/details?id=healthcareapp&choe=UTF-8";
    public static String introduction_video = "";

    private static final String AES_KEY = "A11C3D695FE11AC547A97F06D1824547";

    final public static String MSG_VIDEO = "Video";
    final public static String MSG_VOICE = "Voice";

    public static EditText myEdit = null;
    public static ImageView myButton = null;
    public static Activity current_activity = null;
    public static Context current_context = null;
    public static String current_tag = null;

    //以下參數登入後的功能resume都要處理
    public static String goHome = "";
    public static String goHomeDown = "";
    public static String goMyFavorites = "";
    public static String goTV = "";
    public static String goTVGuide = "";
    public static String goTVChannels = "";
    public static String goMusic = "";
    public static String goMusicSearch = "";
    public static String goManagement = "";
    public static String goManagementPainAssessment = "";
    public static String goNewPainAssessment = "";
    public static String goManagementVisiTrend = "";
    public static String goManagementSchedule = "";
    public static String goMessages = "";
    public static String goMessagesSearch = "";
    public static String goMessagesAddContact = "";
    public static String goEducation = "";
    public static String goEducationSearch = "";
    public static String goArtcast = "";
    public static String goArtcastSearch = "";
    public static String goConciergeService = "";
    public static String goFloorGuide = "";
    public static String goTrandlate = "";
    public static String goSetting = "";
    public static String goPlayServiceSearchKeyword = "";
    public static String goCallSearchKeyword = "";
    public static String goVideoCallSearchKeyword = "";
    public static String goTVChannelSearchKeyword = "";
    public static String goLogout = "";
    //以上參數登入後的功能resume都要處理

    public static String goChatMessages = "";
    public static String goStopMedia = "";

    public static String needRefreshPainAssessment = "";
    public static String needRefreshSchedule = "";

    public static String WellDoneActivity_notAddPainAssessment = "";

    public static String PopMessages = "";

    public static String goShowInputDialog = "";

    public static Bitmap blur_bitmap, blur_bitmap2;

    public static int err_network = 0;
//    public static int bg_ImageResource = R.mipmap.bg03;

    // friends
    public static String AddFriends_Url = "AddFriends";
    public static String GetFriendList_Url = "GetFriendList";
    public static String ModifyAWSToken_Url = "ModifyAWSToken";
    public static String RespondRequest_Url = "RespondRequest";
    public static String SearchContact_Url = "SearchContact";
    public static String GetDevicePairInfo_Url = "GetDevicePairInfo";
    public static String SendDeviceID_Url = "SendDeviceID";
    public static String GetPatientLogin_Url = "GetPatientLogin";
    public static String ResetPassword_Url = "ResetPassword";
    public static String GetPainLevelOption_Url = "GetPainLevelOption";
    public static String GetPainTypeOption_Url = "GetPainTypeOption";
    public static String AddPainAssessment_Url = "AddPainAssessment";
    //    public static String UpdatePainAssessment_Url = "UpdatePainAssessment";
    public static String GetAggravatingFactor_Url = "GetAggravatingFactor";
    public static String GetPainPositionOption_Url = "GetPainPositionOption";


    public static String GetAppMenuStatus_Url = "GetAppMenuStatus";
    public static String GetWelcomeSetting_Url = "GetWelcomeSetting";

    public static String GetPatientInfo_Url = "GetPatientInfo";
    public static String GetPainAssessment_Url = "GetPainAssessment";

    public static String GetPatientSchedule_Url = "GetPatientSchedule";
    public static String GetLastVitalSign_Url = "GetLastVitalSign";


    public static String GetMediaInfoList_Url = "GetMediaInfoList";
    public static String SetMediaWatch_Url = "SetMediaWatch";

    public static String GetWidgetLayout_Url = "GetWidgetLayout";

    public static String GetPandoraAccount_Url = "GetPandoraAccount";

    public static String GetMyFavorites_Url = "GetMyFavorites";
    public static String UpdateMyFavorites_Url = "UpdateMyFavorites";

    public static String GetHospitalInfo_Url = "GetHospitalInfo";
    public static String GetFloorMap_Url = "GetFloorMap";


    public static String GetVisiRecord_Url = "GetVisiRecord";

    public static String VoiceCommand_Url = "VoiceCommand";

    public static String GetEPG_Url = "GetEPG";


    public static String GetServiceInfo_Url = "GetServiceInfo";

    public static String ReqService_Url = "ReqService";


    public static String CheckPairStatus_Url = "CheckPairStatus";
    public static String PairDevice_Url = "PairDevice";
    public static String UnPairDevice_Url = "UnPairDevice";
    public static String PairHostUpdate_Url = "PairHostUpdate";

    public static String ChangePassword_Url = "ChangePassword";
    public static String UpdatePatientDeviceID_Url = "UpdatePatientDeviceID";


    public static String GetMealInfo_Url = "GetMealInfo";
    public static String FoodOrder_Url = "FoodOrder";
    public static String GetOrderInfo_Url = "GetOrderInfo";
    public static String UpdateOrder_Url = "UpdateOrder";


    // ---------------------------------------------------------------------------------------------


    public static JSONArray GetWelcomeSettingDataArray;

    public static String pandora_partnerLogin_Url = "https://tuner.pandora.com/services/json/?method=auth.partnerLogin";
    public static String pandora_userLogin_Url = "https://tuner.pandora.com/services/json/?method=auth.userLogin";

    public static String pandora_createStation_Url = "https://tuner.pandora.com/services/json/?method=station.createStation";
    public static String pandora_getPlaylist_Url = "https://tuner.pandora.com/services/json/?method=station.getPlaylist";
    public static String pandora_search_Url = "https://tuner.pandora.com/services/json/?method=music.search";


    public static String pandora_partnerId = "";
    public static String pandora_partnerAuthToken = "";
    public static String pandora_syncTimeDecode = "";

    public static String pandora_user_id = "";
    public static String pandora_user_auth_token = "";

    public static JSONArray pandora_categories;
    public static JSONArray pandora_now_stations;
    public static JSONArray pandora_now_items;
    public static int pandora_now_playIndex;

    public static String WeatherURL = "";
    public static JSONObject weather_jsonObject;
    public static JSONObject patient_info_jsonObject;
    public static JSONArray painLevelOptionArray;
    public static JSONArray painTypeOptionArray;
    public static JSONArray aggravatingFactorArray;
    public static JSONObject pain_assessment_jsonObject;
    public static JSONObject schedule_jsonObject;
    public static JSONObject lastVisi_jsonObject;
    public static JSONObject visi_jsonObject;
    public static JSONObject last_message_jsonObject;

    public static String myTVresult = "";
    public static String myTVnoEPG = "";
    public static JSONObject myTVjsonObject;
    public static String[] myTVtypeArray;
    public static String[] myTVorg_typeArray;
    public static JSONArray myTVArray;
    public static Map<String, Integer> myTVtype_area_cnt = new HashMap<String, Integer>();
    public static String myTVmax_date;
    public static List<String> myTVreal_date_list = new ArrayList<>();
    public static String myTVtoday = "";
    public static String myTVok = "";

    public static JSONArray myFavoritesArray = new JSONArray();
    public static Map<String, Boolean> myFavoritesEducation = new HashMap<String, Boolean>();
    public static Map<String, Boolean> myFavoritesArtcast = new HashMap<String, Boolean>();
    public static Map<String, Boolean> myFavoritesMusic = new HashMap<String, Boolean>();
    public static Map<String, Boolean> myFavoritesTV = new HashMap<String, Boolean>();
    public static Map<String, String> myFavoritesEducationInformation = new HashMap<String, String>();
    public static Map<String, String> myFavoritesArtcastInformation = new HashMap<String, String>();
    public static Map<String, String> myFavoritesMusicInformation = new HashMap<String, String>();
    public static Map<String, String> myFavoritesTVInformation = new HashMap<String, String>();

    public static Map<String, String> VoiceCommandMap = new HashMap<String, String>(); //key=command,value=jsCode
    public static Map<String, String> VoiceCommandHeadMap = new HashMap<String, String>(); //key=command head,value=jsCode
    public static Map<String, String> VoiceCommandHIDMap = new HashMap<String, String>(); //key=command,value=jsCode


    public static Map<String, Integer> weather_ImageResource = new HashMap<String, Integer>();
    public static Map<String, Integer> nationality_ImageResource = new HashMap<String, Integer>();
    public static Map<String, Integer> head_ImageResource = new HashMap<String, Integer>();

    public static Map<String, Integer> diagram_circle_ids = new HashMap<String, Integer>();
    public static Map<String, Integer> diagram_circle_xs = new HashMap<String, Integer>();
    public static Map<String, Integer> diagram_circle_ys = new HashMap<String, Integer>();

    public static Map<String, String> TvChannelId2Type = new HashMap<String, String>();

    public static String service_search_keyword = "";

    public static String fakeArtcast = "N"; // value = Y , education模擬成Artcast版本


    public static String PainLevel_name = "";


    public static String excuteHttpPost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {

//			 myLoge("excuteHttpPost", "0");
            // Create connection
            url = new URL(targetURL);
//			 myLoge("excuteHttpPost", "1");
            connection = (HttpURLConnection) url.openConnection();
//			 myLoge("excuteHttpPost", "2");
            // connection.setRequestProperty("User-Agent", UserAgent);
            connection.setReadTimeout(maxTimeOut);// 設定timeout時間
//			 myLoge("excuteHttpPost", "3");
            connection.setConnectTimeout(maxTimeOut);
//			 myLoge("excuteHttpPost", "4");
            connection.setRequestMethod("POST");
//			 myLoge("excuteHttpPost", "5");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			 myLoge("excuteHttpPost", "6");
//			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
//			 myLoge("excuteHttpPost", "7");
//			connection.setRequestProperty("charset", "utf-8");
//			 myLoge("excuteHttpPost", "8");
            connection.setUseCaches(false);
//			 myLoge("excuteHttpPost", "9");
            connection.setDoInput(true);
//			 myLoge("excuteHttpPost", "10");
            connection.setDoOutput(true);
//			 myLoge("excuteHttpPost", "11");

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//			 myLoge("excuteHttpPost", "12");
            wr.writeBytes(urlParameters);
//			 myLoge("excuteHttpPost", "13");
            wr.flush();
//			 myLoge("excuteHttpPost", "14");
            wr.close();
//			 myLoge("excuteHttpPost", "15");

            // Get Response
            InputStream is = connection.getInputStream();
//			 myLoge("excuteHttpPost", "16");
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
//			 myLoge("excuteHttpPost", "17");
            String line;
//			 myLoge("excuteHttpPost", "18");
            StringBuffer response = new StringBuffer();
//			 myLoge("excuteHttpPost", "19");
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
//			 myLoge("excuteHttpPost", "20");
            rd.close();
//			 myLoge("excuteHttpPost", "21");
            connection.disconnect();

            myLoge("targetURL,urlParameters", targetURL + "," + urlParameters);
            return response.toString();

        } catch (Exception e) {

            myLoge("excuteHttpPost", "Exception:" + e.getMessage());
            // e.printStackTrace();

            if (connection != null) {
                connection.disconnect();
            }
            return null;
        }
    }

    public static String excuteHttpGet(String url) {
        String result = "";
        HttpURLConnection con = null;
        try {
            URL url2 = new URL(url);
            con = (HttpURLConnection) url2.openConnection();
//            con.setRequestProperty("User-Agent", UserAgent);
            con.setReadTimeout(maxTimeOut);// 設定timeout時間
            con.setConnectTimeout(maxTimeOut);
            con.connect();
            InputStreamReader r2 = null;
            BufferedReader br2 = null;
            try {
                r2 = new InputStreamReader(con.getInputStream(), "utf-8");
                br2 = new BufferedReader(r2);
                String tempstr2 = null;
                while ((tempstr2 = br2.readLine()) != null) {
                    result += tempstr2;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    r2.close();
                    br2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
            return result;
        }
    }

    public static int status_code = 0;
    //執行跟後端POST要資料
    public static String excutePostJson(String targetURL, JSONObject jsonObject) {
        String result_sb = "";
        GlobalClass.myLoge("excutePostJson language:", "[" + language + "]");
        StringBuilder sb = new StringBuilder();

        if (targetURL.contains("https:")) {
            Log.d(TAG, "GlobalClass excutePostJson: https");
            try {
                URL url = new URL(targetURL);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                if (isDefaultToken(targetURL)) {
                    conn.setRequestProperty("Authorization", default_token);
                } else {
                    conn.setRequestProperty("Authorization", formal_token);
                }

                if (targetURL.indexOf("register") > 0) {
//                    conn.setReadTimeout(register_maxTimeOut);// 設定timeout時間
//                    conn.setConnectTimeout(register_maxTimeOut);
                } else if (targetURL.indexOf("GetEPG") > 0) {
//                    conn.setReadTimeout(getepg_maxTimeOut);// 設定timeout時間
//                    conn.setConnectTimeout(getepg_maxTimeOut);
//                } else {
                    conn.setReadTimeout(maxTimeOut);// 設定timeout時間
                    conn.setConnectTimeout(maxTimeOut);
                }

                String input = jsonObject.toString();
                if (needFullEncrypt(targetURL)) {
                   input = encrypt_str(input);
                    conn.setRequestProperty("Content-Type", "application/text");
                } else
                    conn.setRequestProperty("Content-Type", "application/json");

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) {
//                    result += output;
                    sb.append(output);
                }

                Log.d(TAG, "sb: " + sb.toString());

                status_code = conn.getResponseCode();

                conn.disconnect();

            } catch (IOException e) {

                e.printStackTrace();
                err_network += 1;
                myLoge("err_network", "" + err_network);
                myLoge("IOException", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                err_network += 1;
                myLoge("err_network", "" + err_network);
                myLoge("Exception", e.getMessage());
            }
        } else {  //if not https
            try {
                URL url = new URL(targetURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                if (isDefaultToken(targetURL)) {
//                    conn.setRequestProperty("Authorization", default_token); //20200218 mark by leona
                    conn.setRequestProperty("Authorization", demo_for_epd42_token);  //for demo
//                    Log.d(TAG, "GlobalClass Authorization is demo_for_epd42_token http = " + targetURL);
                } else {
//                    conn.setRequestProperty("Authorization", formal_token); //20200218 mark by leona
                    conn.setRequestProperty("Authorization", default_token); //for demo
//                    Log.d(TAG, "GlobalClass Authorization is default_token http = " + default_token);
                }

                if (targetURL.indexOf("register") > 0) {
//                    conn.setReadTimeout(register_maxTimeOut);// 設定timeout時間
//                    conn.setConnectTimeout(register_maxTimeOut);
                } else if (targetURL.indexOf("GetEPG") > 0) {
//                    conn.setReadTimeout(getepg_maxTimeOut);// 設定timeout時間
//                    conn.setConnectTimeout(getepg_maxTimeOut);
                } else {
                    conn.setReadTimeout(maxTimeOut);// 設定timeout時間
                    conn.setConnectTimeout(maxTimeOut);
                }

                String input = jsonObject.toString();
                if (needFullEncrypt(targetURL)) {
                  input = encrypt_str(input);
                  conn.setRequestProperty("Content-Type", "application/text");
                } else
                    conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
//                    result += output;
                    sb.append(output);
                }
                status_code = conn.getResponseCode();
                conn.disconnect();
            } catch (IOException e) {

                e.printStackTrace();
                err_network += 1;
                myLoge("err_network", "" + err_network);
                myLoge("IOException", e.getMessage());

                return "";
            } catch (Exception e) {
                e.printStackTrace();
                err_network += 1;
                myLoge("err_network", "" + err_network);
                myLoge("Exception", e.getMessage());

                return "";
            }
        }

       if (needFullEncrypt(targetURL)) {
            result_sb = decrypt(sb.toString());
        } else {
            result_sb = sb.toString();
        }
        //return check_token() ? result_sb : ""; //20200224 demo mark-it
        return result_sb;
    }

    public static String encrypt_str(String content) {
        byte[] encryptResult = encrypt(content);
        return new String(encryptResult, StandardCharsets.UTF_8);
    }


    public static byte[] encrypt(String content) {
        try {
//        //"AES"：请求的密钥算法的标准名称
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        //256：密钥生成参数；securerandom：密钥生成器的随机源
//        SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
//        securerandom.setSeed(AES_KEY.getBytes());
//        kgen.init(128, securerandom);
//        //生成秘密（对称）密钥
//        SecretKey secretKey = kgen.generateKey();
//        //返回基本编码格式的密钥
//        byte[] enCodeFormat = secretKey.getEncoded();
//        //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称
//        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//        //将提供程序添加到下一个可用位置
//        Security.addProvider(new BouncyCastleProvider());
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

            SecretKeySpec mSecretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
//        AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(AES_IV.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec,mAlgorithmParameterSpec);

            cipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec);
            byte[] byteContent = content.getBytes("utf-8");
            byte[] cryptograph = cipher.doFinal(byteContent);
            return org.bouncycastle.util.encoders.Base64.encode(cryptograph);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static class CryptoProvider extends Provider {
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    public static String decrypt(byte[] cryptograph) {
        try {
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
//            securerandom.setSeed(AES_KEY.getBytes());
//            kgen.init(128, securerandom);
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//            Security.addProvider(new BouncyCastleProvider());
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");

            SecretKeySpec mSecretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
//            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(AES_IV.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec,mAlgorithmParameterSpec);

            cipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec);
            byte[] content = cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(cryptograph));
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String str) {

        String result="";

        try {
            result = decrypt(str.getBytes("UTF-8"));
        } catch (Exception e) {

        }

        return result;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void myLoge(String tag, String msg) {

        if (tag == null) {
            tag = "set null";
        }
        if (msg == null) {
            msg = "set null";
        }

//        if (myDebug) {
//            Log.e(tag, msg);
//            try {
//                String time = formatter.format(new Date());
//                String fileName = time + "_debug.log";
//                String path = SD_PATH;
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                File file = new File(path + fileName);
//                if (!(file.exists())) {
//                    FileWriter fw = new FileWriter(path + fileName, true);
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    Field[] fields = Build.class.getDeclaredFields();
//                    for (Field field : fields) {
//                        try {
//                            field.setAccessible(true);
//                            bw.write(field.getName() + "," + field.get(null).toString() + "\r\n");
//                        } catch (Exception e) {
//                        }
//                    }
//                    bw.write("=======================" + "\r\n");
//                    bw.close();
//                }
//                String time2 = formatterDetail.format(new Date());
//                FileWriter fw = new FileWriter(path + fileName, true);
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write(time2 + "," + tag + "," + msg + "\r\n");
//                bw.close();
//            } catch (Exception e) {
//            }
//
//        }
    }

    public static Boolean isDefaultToken(String targetURL) {
//        if(targetURL.contains(ResetPassword_Url)){
//            return true;
//        }
        if (targetURL.contains(SendDeviceID_Url)) {
            return true;
        }
        if (targetURL.contains(GetDevicePairInfo_Url)) {
            return true;
        }
        if (targetURL.contains(GetPatientLogin_Url)) {
            return true;
        }

        return false;
    }

    public static Boolean needFullEncrypt(String targetURL) {
        /*if (targetURL.contains(GetPatientInfo_Url)) {
            return true;
        }*/
        return false;
    }

//    public static String encrypt_str(String content) {
//        byte[] encryptResult = encrypt(content);
//        return new String(encryptResult, StandardCharsets.UTF_8);
//    }

//    public static byte[] encrypt(String content) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
//            SecretKeySpec mSecretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
//            cipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec);
//            byte[] byteContent = content.getBytes("utf-8");
//            byte[] cryptograph = cipher.doFinal(byteContent);
//            return org.bouncycastle.util.encoders.Base64.encode(cryptograph);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static String decrypt(byte[] cryptograph) {
//        try {
//            SecretKeySpec mSecretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
//            cipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec);
//            byte[] content = cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(cryptograph));
//            return new String(content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static boolean check_token() {
        //myLoge(TAG, "123456");

        final Activity myActivity = current_activity;

        if (status_code == 401) {
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog dialog = new AlertDialog.Builder(myActivity)
                            .setTitle("")
                            .setMessage("The access token expired")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UnPairDevice_Url_json();
                                }
                            })
                            .show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(myActivity, R.color.blue266194));

                }
            });

            return false;
        }

        return true;
    }

    public static void UnPairDevice_Url_json() {
//        if (GlobalClass.pairGUID.isEmpty()) {
//            logout();
//            return;
//        }

        new Thread() {
            public void run() {
                String jsonFullURL = GlobalClass.server_real + GlobalClass.UnPairDevice_Url;
//                if (GlobalClass.myDebug) {
//                    jsonFullURL = GlobalClass.server_test + GlobalClass.UnPairDevice_Url;
//                }
                GlobalClass.myLoge(TAG + "jsonFullURL", jsonFullURL);


                DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String orderTime = formatter.format(new Date());

                // 送出需求
                JSONObject post_jsonObject = new JSONObject();

                try {
                    post_jsonObject.accumulate("apiName", "unPairDevice");
//                    post_jsonObject.accumulate("pairGUID", GlobalClass.pairGUID);
                    post_jsonObject.accumulate("orderTime", orderTime);
                    post_jsonObject.accumulate("hospitalCode", GlobalClass.hospitalCode);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                GlobalClass.myLoge(TAG + "post_jsonObject", "" + post_jsonObject.toString());

                String result = "";
                //if(GlobalClass.myTestResult) {
                //result = jsonString_GetServiceInfo_Url;
                //result = GlobalClass.loadJSONFromAsset(myContext,"req_service_echo.json");
                //}
                //else{
                result = GlobalClass.excutePostJson(jsonFullURL, post_jsonObject);
                //}

                GlobalClass.myLoge(TAG + "result", "" + result);

                // unpair
                //MyServer.get().stop();
                //connect_state = 0;
                //setConnectUI();
//                GlobalClass.pairGUID = "";

//                logout();
            }
        }.start();
    }
}
