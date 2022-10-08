package zzaning.com.flutter_file_preview

import android.content.Context
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** FlutterFilePreviewPlugin */
class FlutterFilePreviewPlugin : FlutterPlugin,ActivityAware {

    private var flutterEngine:FlutterPluginBinding?=null

    companion object{
        fun initialX5(applicationContext: Context) {
            // 在调用TBS初始化、创建WebView之前进行如下配置
//            val map = HashMap<String, Any?>()
////            map[TbsCoreSettings.TBS_SETTINGS_DISABLE_IPC_FLOODING_PROTECTION] = true
////            map[TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER] = true
////            map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
////            map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//            QbSdk.initTbsSettings(map)
            QbSdk.initX5Environment(applicationContext, object : PreInitCallback {
                override fun onCoreInitFinished() {
                    //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
                    TLog.d("初始化X5成功")
                }

                override fun onViewInitFinished(b: Boolean) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    TLog.e("**************", "加载内核是否成功:$b")
                }
            })
        }
    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        initialX5(binding.applicationContext)
        flutterEngine = binding
    }



    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        flutterEngine = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        flutterEngine?.platformViewRegistry?.registerViewFactory(
            "com.zzaning.file_preview",
            TbsNativeViewFactory(binding.activity)
        )
    }
    override fun onDetachedFromActivity() {
        flutterEngine=null
    }



    override fun onDetachedFromActivityForConfigChanges() {
       onDetachedFromActivity()
    }
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }
}
