package zzaning.com.flutter_file_preview

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsReaderView
import java.io.File

/**
 * Created by 12457 on 2017/8/29.
 */
class SuperFileView(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(mContext, attrs, defStyleAttr), TbsReaderView.ReaderCallback {
    private var mTbsReaderView: TbsReaderView?
    private val saveTime = -1
    private var mOnGetFilePathListener: OnGetFilePathListener? = null
    fun setOnGetFilePathListener(mOnGetFilePathListener: OnGetFilePathListener?) {
        this.mOnGetFilePathListener = mOnGetFilePathListener
    }

    private fun getTbsReaderView(context: Context): TbsReaderView {
        return TbsReaderView(context, this)
    }

    fun displayFile(mFile: File?) {
        if (mFile != null && !TextUtils.isEmpty(mFile.path)) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            val bsReaderTemp = "/storage/emulated/0/TbsReaderTemp"
            val bsReaderTempFile = File(bsReaderTemp)
            if (!bsReaderTempFile.exists()) {
                TLog.d("准备创建/storage/emulated/0/TbsReaderTemp！！")
                val mkdir = bsReaderTempFile.mkdir()
                if (!mkdir) {
                    TLog.e("创建/storage/emulated/0/TbsReaderTemp失败！！！！！")
                }
            }

            //加载文件
            val localBundle = Bundle()
            TLog.d(mFile.path)
            localBundle.putString("filePath", mFile.path)
            localBundle.putString("tempPath","/storage/emulated/0/TbsReaderTemp")
            if (mTbsReaderView == null) mTbsReaderView = getTbsReaderView(context)
            val bool = mTbsReaderView!!.preOpen(getFileType(mFile.path), false)
            if (bool) {
                mTbsReaderView!!.openFile(localBundle)
            }else{
                QbSdk.clearAllWebViewCache(context.applicationContext,true)
            }
        } else {
            TLog.e("文件路径无效！")
        }
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private fun getFileType(paramString: String): String {
        var str = ""
        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null")
            return str
        }
        TLog.d(TAG, "paramString:$paramString")
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            TLog.d(TAG, "i <= -1")
            return str
        }
        str = paramString.substring(i + 1)
        TLog.d(TAG, "paramString.substring(i + 1)------>$str")
        return str
    }

    fun show() {
        mOnGetFilePathListener?.onGetFilePath(this)
    }

    /***
     * 将获取File路径的工作，“外包”出去
     */
    interface OnGetFilePathListener {
        fun onGetFilePath(mSuperFileView: SuperFileView)
    }

    override fun onCallBackAction(integer: Int, o: Any, o1: Any) {
        TLog.e("****************************************************$integer")
    }

    fun onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView!!.onStop()
        }
    }

    companion object {
        private const val TAG = "SuperFileView"
    }

    init {
        mTbsReaderView = TbsReaderView(context, this)
        mTbsReaderView?.setLayerType(View.LAYER_TYPE_HARDWARE,null)
        this.addView(mTbsReaderView, LinearLayout.LayoutParams(-1, -1))
    }
}