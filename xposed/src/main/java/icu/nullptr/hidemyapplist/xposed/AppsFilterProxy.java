package icu.nullptr.hidemyapplist.xposed;

import android.util.ArrayMap;
import android.util.SparseArray;

import com.android.server.pm.AppsFilter;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.PackageSetting;
import com.android.server.pm.SettingBase;

import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;

public class AppsFilterProxy extends AppsFilter {
    private final AppsFilter mBase;

    public AppsFilterProxy(AppsFilter base) {
        mBase = base;
    }

    public FeatureConfig getFeatureConfig() {
        return mBase.getFeatureConfig();
    }


    public void grantImplicitAccess(int recipientUid, int visibleUid) {
        mBase.grantImplicitAccess(recipientUid, visibleUid);
    }

    public void onSystemReady() {
        mBase.onSystemReady();
    }

    public void addPackage(PackageSetting newPkgSetting, boolean isReplace) {
        mBase.addPackage(newPkgSetting, isReplace);
    }

    public void onUsersChanged() {
        mBase.onUsersChanged();
    }

    @Nullable
    public SparseArray<int[]> getVisibilityWhitelist(PackageSetting setting, int[] users,
                                                     ArrayMap<String, PackageSetting> existingSettings) {
        return mBase.getVisibilityWhitelist(setting, users, existingSettings);
    }

    public void addPackage(PackageSetting newPkgSetting) {
        mBase.addPackage(newPkgSetting);
    }

    public void removePackage(PackageSetting setting) {
        mBase.removePackage(setting);
    }

    public boolean shouldFilterApplication(int callingUid, @Nullable SettingBase callingSetting,
                                           PackageSetting targetPkgSetting, int userId) {
        return mBase.shouldFilterApplication(callingUid, callingSetting, targetPkgSetting, userId);
    }

    public void dumpQueries(
            PrintWriter pw, PackageManagerService pms, @Nullable Integer filteringAppId,
            DumpState dumpState,
            int[] users) {
        mBase.dumpQueries(pw, pms, filteringAppId, dumpState, users);
    }
}
