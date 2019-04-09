import 'dart:async';

import 'package:flutter/services.dart';

class FilePreview {
  static const MethodChannel _channel =
      const MethodChannel('com.zzaning.file_preview');

  static Future<String> openFile(String path) async {
    Map<String, String> map = {"path": path};
    final String result = await _channel.invokeMethod('openFile', map);
    return result;
  }

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
