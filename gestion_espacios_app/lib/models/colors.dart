/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';

/// Clase que contiene los colores de la aplicación.
class MyColors extends MaterialColor {
  static const int _whiteAppPrimaryValue = 0xFFF1F8FC;
  static const int _lightBlueAppPrimaryValue = 0xFF266296;
  static const int _darkBlueAppPrimaryValue = 0xFF020A26;
  static const int _pinkAppPrimaryValue = 0xFFB72B7B;
  static const int _blackAppPrimaryValue = 0xFF000000;

  /// Tonos blancos.
  static const MaterialColor whiteApp =
      MaterialColor(_whiteAppPrimaryValue, <int, Color>{
    50: Color(0xFFFCFDFF),
    100: Color(0xFFF8FBFE),
    200: Color(0xFFF4F8FD),
    300: Color(0xFFF1F8FC),
    400: Color(0xFFEEF6FB),
    500: Color(_whiteAppPrimaryValue),
    600: Color(0xFFE6EAF0),
    700: Color(0xFFCDD2D9),
    800: Color(0xFFB4BCC4),
    900: Color(0xFF8A939E),
  });

  /// Tonos azules.
  static const MaterialColor lightBlueApp =
      MaterialColor(_lightBlueAppPrimaryValue, <int, Color>{
    50: Color(0xFFE1EEF7),
    100: Color(0xFFB4D4E5),
    200: Color(0xFF85B9D3),
    300: Color(0xFF56A0C1),
    400: Color(0xFF3D8FBA),
    500: Color(_lightBlueAppPrimaryValue),
    600: Color(0xFF235E7E),
    700: Color(0xFF1C4A60),
    800: Color(0xFF153640),
    900: Color(0xFF0D1F20),
  });

  /// Tonos azules oscuros.
  static const MaterialColor darkBlueApp =
      MaterialColor(_darkBlueAppPrimaryValue, <int, Color>{
    50: Color(0xFF0A0F1A),
    100: Color(0xFF0F1B2A),
    200: Color(0xFF0E1C2C),
    300: Color(0xFF0D1E2F),
    400: Color(0xFF0C202F),
    500: Color(_darkBlueAppPrimaryValue),
    600: Color(0xFF0A232F),
    700: Color(0xFF09212D),
    800: Color(0xFF081E2A),
    900: Color(0xFF071B28),
  });

  /// Tonos rosas.
  static const MaterialColor pinkApp =
      MaterialColor(_pinkAppPrimaryValue, <int, Color>{
    50: Color(0xFFF9E8F1),
    100: Color(0xFFF2C3D9),
    200: Color(0xFFEC9EC1),
    300: Color(0xFFE679A9),
    400: Color(0xFFE3669D),
    500: Color(_pinkAppPrimaryValue),
    600: Color(0xFFC83A79),
    700: Color(0xFFB02E66),
    800: Color(0xFF98234E),
    900: Color(0xFF7F1B3F),
  });

  /// Tonos negrizos.
  static const MaterialColor blackApp =
      MaterialColor(_blackAppPrimaryValue, <int, Color>{
    50: Color(0xFFE6E6E6),
    100: Color(0xFFB3B3B3),
    200: Color(0xFF808080),
    300: Color(0xFF4D4D4D),
    400: Color(0xFF262626),
    500: Color(_blackAppPrimaryValue),
    600: Color(0xFF1A1A1A),
    700: Color(0xFF141414),
    800: Color(0xFF0E0E0E),
    900: Color(0xFF070707),
  });

  const MyColors(int primary, Map<int, Color> swatch) : super(primary, swatch);

  static const List<MaterialColor> all = <MaterialColor>[
    whiteApp,
    lightBlueApp,
    darkBlueApp,
    pinkApp,
    blackApp,
  ];
}
