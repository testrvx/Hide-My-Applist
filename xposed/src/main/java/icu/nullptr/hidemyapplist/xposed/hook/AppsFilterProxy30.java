package icu.nullptr.hidemyapplist.xposed.hook;

import com.android.server.pm.AppsFilter;
import com.android.server.pm.PackageSetting;
import com.android.server.pm.SettingBase;

public class AppsFilterProxy30 extends AppsFilter {
    @Override
    public boolean shouldFilterApplication(int callingUid, SettingBase callingSetting, PackageSetting targetPkgSetting, int userId) {
        if (PmsHookTarget30.shouldFilterApplication(callingUid, targetPkgSetting)) return true;
        return super.shouldFilterApplication(callingUid, callingSetting, targetPkgSetting, userId);
    }
}
