/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

/// Widget que muestra una imagen seleccionada.
class PickedImageWidget extends StatelessWidget {
  /// La imagen seleccionada.
  final PickedFile pickedImage;

  /// El ancho de la imagen.
  final double? width;

  /// El alto de la imagen.
  final double? height;

  /// El ajuste de la imagen.
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
      // Si se está ejecutando en web, se muestra la imagen desde la URL.
      return Image.network(pickedImage.path,
          width: width, height: height, fit: fit);
    } else {
      // Si se está ejecutando en un dispositivo móvil, se muestra la imagen.
      return Image.file(File(pickedImage.path),
          width: width, height: height, fit: fit);
    }
  }
}
