package zzaning.com.flutter_file_preview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import io.flutter.plugin.platform.PlatformView
import java.io.File

class TbsNativeView(context: Context, val id: Int, creationParams: Map<String, *>?) :
    PlatformView {
    var mFrameLayout: FrameLayout? = null
    var mSuperFileView: SuperFileView? = null

    init {
        if (creationParams != null) {
            val filePath = (creationParams["filePath"] as? String)
            if (filePath != null) {
                mFrameLayout = FrameLayout(context).apply {
                    val parent = this
                    mSuperFileView = SuperFileView(context).apply {
                        parent.addView(
                            this,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )
                        this.displayFile(File(filePath))
                    }
                }
            }
        }
    }

    override fun getView(): View? {
        return mFrameLayout
    }

    override fun dispose() {
        mSuperFileView?.onStopDisplay()
        mSuperFileView?.removeAllViews()
        mFrameLayout?.removeAllViews()
        mSuperFileView = null
        mFrameLayout = null
    }
}