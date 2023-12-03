package icu.nullptr.hidemyapplist.xposed.hook;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.server.pm.AppsFilterImpl;
import com.android.server.pm.AppsFilterSnapshot;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.snapshot.PackageDataSnapshot;

@Keep
@RequiresApi(33)
public class AppsFilterImplProxy extends AppsFilterImpl {
    @Override
    public boolean shouldFilterApplication(PackageDataSnapshot snapshot, int callingUid, @Nullable Object callingSetting, PackageStateInternal targetPkgSetting, int userId) {
        if (PmsHookTarget33.shouldFilterApplication(snapshot, callingUid, targetPkgSetting)) {
            return true;
        }
        return super.shouldFilterApplication(snapshot, callingUid, callingSetting, targetPkgSetting, userId);
    }

    @Override
    public AppsFilterSnapshot snapshot() {
        AppsFilterSnapshot s = super.snapshot();
        // PmsHookTarget33.setupSnapshot(s);
        // FIXME: why override snapshot has no effect?
        return s;
    }
}
