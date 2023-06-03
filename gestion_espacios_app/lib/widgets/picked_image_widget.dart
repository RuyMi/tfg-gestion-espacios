import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

class PickedImageWidget extends StatelessWidget {
  final PickedFile pickedImage;
  final double? width;
  final double? height;
  final BoxFit? fit;

  const PickedImageWidget({
    super.key,
    required this.pickedImage,
    this.width,
    this.height,
    this.fit,
  });

  @override
  Widget build(BuildContext context) {
    if (kIsWeb) {
      return Image.network(pickedImage.path,
          width: width, height: height, fit: fit);
    } else {
      return Image.file(File(pickedImage.path),
          width: width, height: height, fit: fit);
    }
  }
}
