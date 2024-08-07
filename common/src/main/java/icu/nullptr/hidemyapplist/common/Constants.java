package icu.nullptr.hidemyapplist.common;

import java.util.Set;

public class Constants {

    public static final String APP_PACKAGE_NAME = "com.tsng.hidemyapplist";

    public static final String PROVIDER_AUTHORITY = APP_PACKAGE_NAME + ".ServiceProvider";
    public static final String GMS_PACKAGE_NAME = "com.google.android.gms";
    public static final String GSF_PACKAGE_NAME = "com.google.android.gsf";
    public static final String UPDATE_URL_BASE = "https://api.nullptr.icu/android/hide-my-applist/static/";
    public static final String TRANSLATE_URL = "https://crowdin.com/project/hide-my-applist";
    public static final String CLASS_PMS = "com.android.server.pm.PackageManagerService";

    public static final String ANDROID_APP_DATA_ISOLATION_ENABLED_PROPERTY = "persist.zygote.app_data_isolation";
    public static final String ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY = "persist.sys.vold_app_data_isolation_enabled";

    public static final String DESCRIPTOR = "cool";
    public static final int TRANSACTION = 
    'N' << 24 | 'E' << 16 | 'W' << 8 | 'C' << 4 | 'O' << 0;
    public static final int ACTION_GET_BINDER = 2; 


    public static final int UID_SYSTEM = 1000;
    public static final int PER_USER_RANGE = 100000;

    public static final Set<String> packagesShouldNotHide = Set.of(
            "android",
            "android.media",
            "android.uid.system",
            "android.uid.shell",
            "android.uid.systemui",
            "com.android.permissioncontroller",
            "com.android.providers.downloads",
            "com.android.providers.downloads.ui",
            "com.android.providers.media",
            "com.android.providers.media.module",
            "com.android.providers.settings",
            "com.google.android.webview",
            "com.google.android.providers.media.module"
    );
}
