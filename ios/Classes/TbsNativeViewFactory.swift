import Flutter
import UIKit
import WebKit
import QuickLook

class TbsNativeViewFactory: NSObject, FlutterPlatformViewFactory {
    private var messenger: FlutterBinaryMessenger
    private var appDelegate: FlutterAppDelegate
    init(messenger: FlutterBinaryMessenger,appDelegate:FlutterAppDelegate) {
        self.messenger = messenger
        self.appDelegate = appDelegate
        super.init()
    }
    
    func create( withFrame frame: CGRect,viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return TbsNativeView(frame: frame,
                             viewIdentifier: viewId,
                             arguments: args,
                             binaryMessenger: messenger,
                             appDelegate: appDelegate
        )
    }
}

class TbsNativeView: NSObject, FlutterPlatformView,QLPreviewControllerDataSource {
    private var _view: UIView
    
    private var previewController : QLPreviewController?
    
    private var filePath:String?
    
    init(
        frame: CGRect,
        viewIdentifier viewId: Int64,
        arguments args: Any?,
        binaryMessenger messenger: FlutterBinaryMessenger?,
        appDelegate:FlutterAppDelegate
    ) {
        _view = UIView(frame: frame)
        super.init()
        // iOS views can be created here
        guard let dict = args as? Dictionary<String,AnyObject> else{
            return
        }
        if let val:String = dict["filePath"] as? String {
            createNativeView(view: _view,filePath:val,frame:frame,appDelegate:appDelegate)
        }
    }
    
    func view() -> UIView {
        return _view
    }
    
    deinit {
        if let pre:UIViewController = previewController {
            _view.addSubview(pre.view)
            pre.removeFromParent()
        }
    }
    
    func createNativeView(view rootView: UIView,filePath : String , frame : CGRect, appDelegate:FlutterAppDelegate ){
        rootView.backgroundColor = UIColor.blue
        
        self.filePath = filePath
        
        previewController = QLPreviewController()
        previewController!.view.frame = frame
        previewController!.dataSource = self
        
        if let controller:UIViewController = appDelegate.window.rootViewController {
            controller.addChild(previewController!)
            rootView.addSubview(previewController!.view)
        }
    }
    
    func previewController(_ controller: QLPreviewController, previewItemAt index: Int) -> QLPreviewItem {
        return URL.init(fileURLWithPath: filePath!) as QLPreviewItem
    }
    func numberOfPreviewItems(in controller: QLPreviewController) -> Int {
        return (filePath != nil) ? 1 : 0
    }
}
