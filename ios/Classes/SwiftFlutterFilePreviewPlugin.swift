import Flutter
import UIKit

public class SwiftFlutterFilePreviewPlugin: NSObject, FlutterPlugin {
   public static func register(with registrar: FlutterPluginRegistrar) {
     let factory = TbsNativeViewFactory(messenger: registrar.messenger(),appDelegate:(registrar as! FlutterAppDelegate))
     registrar.register(factory, withId: "com.zzaning.file_preview")
   }
}
