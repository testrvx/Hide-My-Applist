package com.android.server.pm;

import android.util.ArrayMap;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.io.PrintWriter;

// API 30
public class AppsFilter {

    public FeatureConfig getFeatureConfig() {
        throw new RuntimeException();
    }

    /**
     * Grants access based on an interaction between a calling and target package, granting
     * visibility of the caller from the target.
     *
     * @param recipientUid the uid gaining visibility of the {@code visibleUid}.
     * @param visibleUid   the uid becoming visible to the {@code recipientUid}
     */
    public void grantImplicitAccess(int recipientUid, int visibleUid) {
        throw new RuntimeException();
    }

    public void onSystemReady() {
        throw new RuntimeException();
    }

    /**
     * Adds a package that should be considered when filtering visibility between apps.
     *
     * @param newPkgSetting the new setting being added
     * @param isReplace     if the package is being replaced and may need extra cleanup.
     */
    public void addPackage(PackageSetting newPkgSetting, boolean isReplace) {
        throw new RuntimeException();
    }

    public void onUsersChanged() {
        throw new RuntimeException();
    }

    /**
     * Fetches all app Ids that a given setting is currently visible to, per provided user. This
     * only includes UIDs >= {@link Process#FIRST_APPLICATION_UID} as all other UIDs can already see
     * all applications.
     * <p>
     * If the setting is visible to all UIDs, null is returned. If an app is not visible to any
     * applications, the int array will be empty.
     *
     * @param users            the set of users that should be evaluated for this calculation
     * @param existingSettings the set of all package settings that currently exist on device
     * @return a SparseArray mapping userIds to a sorted int array of appIds that may view the
     * provided setting or null if the app is visible to all and no whitelist should be
     * applied.
     */
    @Nullable
    public SparseArray<int[]> getVisibilityWhitelist(PackageSetting setting, int[] users,
                                                     ArrayMap<String, PackageSetting> existingSettings) {
        throw new RuntimeException();
    }

    /**
     * Equivalent to calling {@link #addPackage(PackageSetting, boolean)} with {@code isReplace}
     * equal to {@code false}.
     *
     * @see AppsFilter#addPackage(PackageSetting, boolean)
     */
    public void addPackage(PackageSetting newPkgSetting) {
        throw new RuntimeException();
    }

    /**
     * Removes a package for consideration when filtering visibility between apps.
     *
     * @param setting the setting of the package being removed.
     */
    public void removePackage(PackageSetting setting) {
        throw new RuntimeException();
    }

    /**
     * Returns true if the calling package should not be able to see the target package, false if no
     * filtering should be done.
     *
     * @param callingUid       the uid of the caller attempting to access a package
     * @param callingSetting   the setting attempting to access a package or null if it could not be
     *                         found
     * @param targetPkgSetting the package being accessed
     * @param userId           the user in which this access is being attempted
     */
    public boolean shouldFilterApplication(int callingUid, @Nullable SettingBase callingSetting,
                                           PackageSetting targetPkgSetting, int userId) {
        throw new RuntimeException();
    }

    public void dumpQueries(
            PrintWriter pw, PackageManagerService pms, @Nullable Integer filteringAppId,
            DumpState dumpState,
            int[] users) {
        throw new RuntimeException();
    }

    public interface StateProvider {
    }

    public interface FeatureConfig {
    }
}
