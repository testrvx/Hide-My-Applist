package icu.nullptr.hidemyapplist.xposed

import android.content.pm.IPackageManager
import android.os.Binder
import android.os.Parcel
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import icu.nullptr.hidemyapplist.common.BuildConfig
import icu.nullptr.hidemyapplist.common.Constants

object BridgeService {

    private const val TAG = "HMA-Bridge"

    private var appUid = 0
    private const val MAX_BINDER_SIZE = 120

    fun register(pms: IPackageManager) {
        logI(TAG, "Initialize HMAService - Version ${BuildConfig.SERVICE_VERSION}")
        val service = HMAService(pms)
        appUid = Utils.getPackageUidCompat(service.pms, Constants.APP_PACKAGE_NAME, 0, 0)
        val appPackage = Utils.getPackageInfoCompat(service.pms, Constants.APP_PACKAGE_NAME, 0, 0)
        if (!Utils.verifyAppSignature(appPackage.applicationInfo.sourceDir)) {
            logE(TAG, "Fatal: App signature mismatch")
            return
        }
        logD(TAG, "Client uid: $appUid")
        logI(TAG, "Initialize service proxy")
        pms.javaClass.findMethod(true) {
            name == "onTransact"
        }.hookBefore { param ->
            val code = param.args[0] as Int
            val data = param.args[1] as Parcel
            val reply = param.args[2] as Parcel?
            if (myTransac(code, data, reply)) param.result = true
        }
    }

    private fun myTransac(code: Int, data: Parcel, reply: Parcel?): Boolean {
        if (code == Constants.TRANSACTION) {
            if (Binder.getCallingUid() == appUid) {
                logD(TAG, "Transaction from client")
                runCatching {
                    data.enforceInterface(Constants.DESCRIPTOR)
                    if (data.dataSize() < MAX_BINDER_SIZE) {
                        when (data.readInt()) {
                            Constants.ACTION_GET_BINDER -> {
                                reply?.writeNoException()
                                reply?.writeStrongBinder(HMAService.instance)
                                return true
                            }
                            else -> logW(TAG, "Unknown action")
                        }
                    } else {
                        logW(TAG, "Transaction size exceeds limit")
                    }
                }.onFailure {
                    logE(TAG, "Transaction error", it)
                }
            } else {
                logW(TAG, "Someone else trying to get my binder?")
            }
            data.setDataPosition(0)
            reply?.setDataPosition(0)
        }
        return true
    }
}
