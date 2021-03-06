package com.xvli.cit.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.xvli.cit.CitApplication;
import com.xvli.cit.dao.LoginDao;
import com.xvli.cit.dao.TruckVo_Dao;
import com.xvli.cit.database.DatabaseHelper;
import com.xvli.cit.comm.Config;
import com.xvli.cit.vo.LoginVo;
import com.xvli.cit.vo.TruckVo;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_RUN = "first";


    /**
     * 返回当前日期
     *
     * @return
     */
    public static String getNow_toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
//        return "2015-02-09";
    }


    //有新消息时  取消 新线路 新任务 变更等震动
    public static void startVidrate(Context mContext){
        Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {1000,1000,1000,1000};   // 等待1秒 震动一秒
        vibrator.vibrate(pattern,-1);//不重复
    }

    public static String getNowDetial_toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 返回当前日期
     *
     * @return
     */
    public static Date getNow_toDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        try {
            return ConverToDate(str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return curDate;
    }

    public static String getDatatoString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //把日期转为字符串  
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }

    //把字符串转为日期  
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    //时间转换
    public static String getData(long data){
        Date date=new Date(data);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }




    /**
     * 比较时间大小,time1大於time2返回false
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareTime(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        if (result == 0)
            return false;
        else if (result < 0)
            return false;
        else
            return true;
    }

    /**
     * 得到屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static DisplayMetrics metric = new DisplayMetrics();

    /**
     * log日志使用名字
     *
     * @return
     */
    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31
    }

    /*
     *判断sd卡是否存在
     */
    public static Boolean isSDCardOK() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    }

    /**
     * 如果文件不存在就创建文件
     *
     * @param file
     */
    public static void CreateFileIfNotExtends(File file) {
        // 如果文件夹不存在则创建
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    /**
     * 传入文件夹路径，没有的话创建文件夹
     *
     * @param filePath
     */
    public static void CreateFile(String filePath) {
        File dbFile = new File(filePath);
        if (!dbFile.exists() && !dbFile.isDirectory()) {
            dbFile.mkdir();
        }
    }
    //String 字符串 转String数组
    public static String[] convertStrToArray(String str){
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
    /**
     * 复制数据库到SD卡下
     */
    public static void copyDB() {
        File dbFile = new File(Config.DB_PATH, DatabaseHelper.getTableName());
        File dirFile = new File(Config.DATABASE_PATH);
        if (dbFile.exists()) {
            Util.CreateFile(Config.DATABASE_PATH_FILE);
            Util.CreateFileIfNotExtends(dirFile);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(dirFile);
                inputStream = new FileInputStream(dbFile);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * GPS是否打开
     *
     * @return
     */
    public static boolean isGPSOpen() {
        LocationManager locationManager
                = (LocationManager) CitApplication.getInstance().getSystemService(CitApplication.getInstance().LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 强制帮用户打开GPS
     */
    public static final void openGPS() {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(CitApplication.getInstance(), 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) CitApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    ;

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) CitApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageManager pm = CitApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(CitApplication.getInstance().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            PDALogger.d("VersionInfo Exception:" + e.toString());
        }
        return versionName;
    }
    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getSystemTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    public static String showPercent(int fenzi, int total) {
        return fenzi + "/" + total;
    }

    /**
     * @param year
     * @param month
     * @return
     */
    public static  int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    /**
     * dp转换成px,代码写的是像素,而XML中写的是单位密度
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
//        if(scale == 1.5){
//            return (int) (dp *2);
//        }
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        PDALogger.d("fontScale === "+fontScale);
        return (int) (spValue * fontScale + 0.5f);
    }



    //判断后置摄像头是否可用
    public static boolean isCameraAvailable(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //获取操作人
    public static String  getOperators(LoginDao loginDao){
        String Operators = "";
        List<LoginVo> loginVos = loginDao.queryAll();
        if(loginVos != null && loginVos.size() > 0){
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <loginVos.size() ; i++) {
                buffer.append(loginVos.get(i).getName() + ",");
            }
            Operators = buffer.toString().substring(0, buffer.length() - 1);
        }
        return Operators;
    }
    //获取绑定的车辆 witch = 1 返回车牌号   ==2 返回车辆编码
    public static String  getBindTruck(TruckVo_Dao truckVoDao,int witch) {
        String platenumbers = "";
        String truckBaecodes = "";
        HashMap<String, Object> value = new HashMap<>();
        value.put("operateType", 1);
        List<TruckVo> loginVos = truckVoDao.queryAll();
        if (loginVos != null && loginVos.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            StringBuffer buffercode = new StringBuffer();
            for (int i = 0; i < loginVos.size(); i++) {
                buffer.append(loginVos.get(i).getPlatenumber() + ",");
                buffercode.append(loginVos.get(i).getBarcode() + ",");
            }
            platenumbers = buffer.toString().substring(0, buffer.length() - 1);
            truckBaecodes = buffercode.toString().substring(0, buffer.length() - 1);
        }
        if (witch == 1) {
            return platenumbers;
        } else {
            return truckBaecodes;
        }
    }
    //记录车辆行程状态  例如出车到下车 行走中 下车到网点扫描
    public static void  setTruckType(LoginDao loginDao,int type){
        HashMap<String,Object> value = new HashMap<>();
        value.put("iscaptain",true);
        List<LoginVo> loginVos = loginDao.queryAll();
        if(loginVos != null && loginVos.size() > 0){
            LoginVo loginVo = loginVos.get(0);
            loginVo.setTruckState(type);
            loginDao.upDate(loginVo);
        }
    }
    //获取车辆行程状态  例如出车到下车 行走中 下车到网点扫描
    public static int  getTruckType(LoginDao loginDao){
        int truckType = 0;
        HashMap<String,Object> value = new HashMap<>();
        value.put("iscaptain",true);
        List<LoginVo> loginVos = loginDao.queryAll();
        if(loginVos != null && loginVos.size() > 0){
            LoginVo loginVo = loginVos.get(0);
            truckType = loginVo.getTruckState();
        }
        return truckType;
    }
}
