package com.example.htp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.htp.api.ApiHttpClient;
import com.example.htp.base.BaseApplication;
import com.example.htp.bean.Constants;
import com.example.htp.bean.User;
import com.example.htp.cache.DataCleanManager;
import com.example.htp.greendao.dao.DaoMaster;
import com.example.htp.greendao.dao.DaoSession;
import com.example.htp.greendao.entity.Note;
import com.example.htp.htpandroidframe.BuildConfig;
import com.example.htp.htpandroidframe.R;
import com.example.htp.model.SysAcct;
import com.example.htp.ui.activity.MainActivity;
import com.example.htp.utils.CyptoUtils;
import com.example.htp.utils.MethodsCompat;
import com.example.htp.utils.StringUtils;
import com.example.htp.utils.TDevice;
import com.example.htp.utils.TLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapConfig;
import org.kymjs.kjframe.utils.KJLoger;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @author htp
 * @version 1.0
 */
public class AppContext extends BaseApplication {
    // 登录用户
    private User user = null;

    public static final int PAGE_SIZE = 20;// 默认分页大小

    private static AppContext instance;

    private int loginUid;

    private boolean login;

    // 请求队列
    private RequestQueue mRequestQueue;

    private static List<Activity> mActivityList = new LinkedList<Activity>();
    // 百度地图相关

    public TextView trigger, exit;
    public Vibrator mVibrator;

    public SQLiteDatabase db;
    public DaoMaster daoMaster;
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();
        mApplication = this;
        mNetWorkState = TDevice.getNetworkType();
        //imageloader初始化
        initImageLoader(getApplicationContext());
        //百度地图初始化
//		SDKInitializer.initialize(getApplicationContext());


        mVibrator = (Vibrator) getApplicationContext().getSystemService(
                Service.VIBRATOR_SERVICE);

		 Thread.setDefaultUncaughtExceptionHandler(AppException
		 .getAppExceptionHandler(this));
//		 UIHelper.sendBroadcastForNotice(this);
        //初始化greendao
        setupDatabase();

    }

    public void setupDatabase()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }


    /**
     * 添加页面到堆栈
     */
    public static void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    private void init() {
        // 初始化网络请求

          AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		  client.setCookieStore(myCookieStore);
		  ApiHttpClient.setHttpClient(client);
		  ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器

		  KJLoger.openDebutLog(true); TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
         BitmapConfig.CACHEPATH = "htp/imagecache";
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPortrait(getProperty("user.face"));//用户头像-文件名
        user.setAccount(getProperty("user.account"));
        user.setLocation(getProperty("user.location"));
        user.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        user.setGender(getProperty("user.gender"));
        return user;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    // 添加和获取RequestQueue队列
    public <T> void addRequest(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    // 添加和获取RequestQueue队列，并指定超时重试次数（确保最大重试次数为1，以保证超时后不重新请求。）
    public <T> void addRequest(Request<T> request, String tag, int maxNum) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, maxNum, 1.0f));
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.account", user.getAccount());
                setProperty("user.pwd",
                        CyptoUtils.encode("oschinaApp", user.getPwd()));
                setProperty("user.location", user.getLocation());
                setProperty("user.gender", String.valueOf(user.getGender()));
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.gender", String.valueOf(user.getGender()));
            }
        });
    }
    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location","user.isRememberMe", "user.gender");
    }


    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }
    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        set(AppConfig.KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(AppConfig.KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(AppConfig.KEY_FRITST_START, frist);
    }

    // 夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(AppConfig.KEY_NIGHT_MODE_SWITCH,
                false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(AppConfig.KEY_NIGHT_MODE_SWITCH, on);
    }

    private static Application mApplication;
    public static int mNetWorkState;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }






    /**
     * Imageloader配置
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
		/*
		 * ImageLoaderConfiguration config = new
		 * ImageLoaderConfiguration.Builder(
		 * context).threadPriority(Thread.NORM_PRIORITY - 2)
		 * .denyCacheImageMultipleSizesInMemory()
		 * .discCacheFileNameGenerator(new Md5FileNameGenerator())
		 * .tasksProcessingOrder(QueueProcessingType.LIFO) // .writeDebugLogs()
		 * // Remove for release app .build(); // Initialize ImageLoader with
		 * configuration. ImageLoader.getInstance().init(config);
		 */

        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "htp/Cache");// 获取到缓存的目录地址
        Log.e("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context);
        builder.memoryCacheExtraOptions(480, 800);
        builder.threadPoolSize(3);
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.denyCacheImageMultipleSizesInMemory();
        builder.discCacheSize(50 * 1024 * 1024);
        builder.discCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
        builder.defaultDisplayImageOptions(getListOptions());
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        builder.discCacheFileCount(100);
        builder.diskCache(new UnlimitedDiskCache(cacheDir));
        builder.writeDebugLogs();
        ImageLoaderConfiguration config = builder.build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置

    }

    /**
     * 新闻列表中用到的图片加载配置
     */
    public static DisplayImageOptions getListOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.ic_stub)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.icon_addpic_unfocused)
                        // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.ic_error)
                        // 设置下载的图片是否缓存在内存中
                .cacheInMemory(false)
                        // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisc(true)
                        // 保留Exif信息
                .considerExifParams(true)
                        // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // .decodingOptions(android.graphics.BitmapFactory.Options
                        // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                        // 设置图片下载前的延迟
                .delayBeforeLoading(100)// int
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                        // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();
        return options;
    }
}
