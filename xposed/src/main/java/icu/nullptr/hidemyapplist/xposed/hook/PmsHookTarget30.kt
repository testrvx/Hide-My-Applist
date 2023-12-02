package icu.nullptr.hidemyapplist.xposed.hook

import android.annotation.TargetApi
import android.os.Build
import com.android.server.pm.AppsFilter
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import de.robv.android.xposed.XC_MethodHook
import icu.nullptr.hidemyapplist.common.Constants
import icu.nullptr.hidemyapplist.xposed.HMAService
import icu.nullptr.hidemyapplist.xposed.Utils
import icu.nullptr.hidemyapplist.xposed.logD
import icu.nullptr.hidemyapplist.xposed.logE
import icu.nullptr.hidemyapplist.xposed.logI
import java.util.concurrent.atomic.AtomicReference

@TargetApi(Build.VERSION_CODES.R)
class PmsHookTarget30(private val service: HMAService) : IFrameworkHook {

    companion object {
        private const val TAG = "PmsHookTarget30"
        private var sInstance: PmsHookTarget30? = null

        @JvmStatic
        fun shouldFilterApplication(callingUid: Int, targetPkgSetting: Any): Boolean {
            return sInstance?.shouldFilterApp(callingUid, targetPkgSetting) ?: false
        }
    }

    private var hook: XC_MethodHook.Unhook? = null
    private var lastFilteredApp: AtomicReference<String?> = AtomicReference(null)
    private var useFallback = true
    private var origAppFilter: Any? = null
    private var unhooked = false

    override fun load() {
        logI(TAG, "Load hook")
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            sInstance = this
            doOptimizeHook()
        } else {
            doFallbackHook()
        }
    }

    private fun shouldFilterApp(callingUid: Int, targetPkgSetting: Any): Boolean {
        var shouldFilter = false
        runCatching {
            if (unhooked) return@runCatching
            if (callingUid == Constants.UID_SYSTEM) return@runCatching
            val callingApps = Utils.binderLocalScope {
                service.pms.getPackagesForUid(callingUid)
            } ?: return@runCatching
            val targetApp = Utils.getPackageNameFromPackageSettings(targetPkgSetting)
            for (caller in callingApps) {
                if (service.shouldHide(caller, targetApp)) {
                    shouldFilter = true
                    service.filterCount++
                    val last = lastFilteredApp.getAndSet(caller)
                    if (last != caller) logI(
                        TAG,
                        "@shouldFilterApplication: query from $caller"
                    )
                    logD(
                        TAG,
                        "@shouldFilterApplication caller: $callingUid $caller, target: $targetApp"
                    )
                    return@runCatching
                }
            }
        }.onFailure {
            logE(TAG, "something wrong happened, unload", it)
            unload()
        }
        return shouldFilter
    }

    private fun doOptimizeHook() {
        if (!HybridClassLoader.sInjected) {
            doFallbackHook()
            return
        }
        logI(TAG, "installing optimize hook")
        runCatching {
            val pms = service.pms
            val filter = pms.getObject("mAppsFilter")
            ArtHelper.setObjectClass(filter, AppsFilterProxy30::class.java)
            origAppFilter = filter
            useFallback = false
            logI(TAG, "install optimize hook success")
        }.onFailure {
            logE(TAG, "failed to install, use fallback", it)
            doFallbackHook()
        }
    }

    private fun doFallbackHook() {
        logI(TAG, "installing fallback hook")
        hook = findMethod("com.android.server.pm.AppsFilter") {
            name == "shouldFilterApplication"
        }.hookBefore { param ->
            runCatching {
                val callingUid = param.args[0] as Int
                if (callingUid == Constants.UID_SYSTEM) return@hookBefore
                val callingApps = Utils.binderLocalScope {
                    service.pms.getPackagesForUid(callingUid)
                } ?: return@hookBefore
                val targetApp = Utils.getPackageNameFromPackageSettings(param.args[2])
                for (caller in callingApps) {
                    if (service.shouldHide(caller, targetApp)) {
                        param.result = true
                        service.filterCount++
                        val last = lastFilteredApp.getAndSet(caller)
                        if (last != caller) logI(
                            TAG,
                            "@shouldFilterApplication: query from $caller"
                        )
                        logD(
                            TAG,
                            "@shouldFilterApplication caller: $callingUid $caller, target: $targetApp"
                        )
                        return@hookBefore
                    }
                }
            }.onFailure {
                logE(TAG, "Fatal error occurred, disable hooks", it)
                unload()
            }
        }
    }

    override fun unload() {
        hook?.unhook()
        hook = null
        unhooked = true
        sInstance = null
        if (!useFallback) {
            runCatching {
                ArtHelper.setObjectClass(
                    service.pms.getObject("mAppsFilter"),
                    AppsFilter::class.java
                )
            }.onFailure {
                logE(TAG, "failed to restore appFilter class")
            }
        }
    }
}
