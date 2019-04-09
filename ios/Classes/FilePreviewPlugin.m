#import "FilePreviewPlugin.h"
#import "FilePreviewVC.h"

@implementation FilePreviewPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"com.zzaning.file_preview"
            binaryMessenger:[registrar messenger]];
  FilePreviewPlugin* instance = [[FilePreviewPlugin alloc] init];
    instance.hostViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"openFile" isEqualToString:call.method]) {
      NSString *path = call.arguments[@"path"];
      FilePreviewVC *preview = [[FilePreviewVC alloc] init];
      preview.url = path;
      UINavigationController *navCtrl = [[UINavigationController alloc] initWithRootViewController:preview];
      [self.hostViewController presentViewController:navCtrl animated:YES completion:nil];
  }
  else if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
