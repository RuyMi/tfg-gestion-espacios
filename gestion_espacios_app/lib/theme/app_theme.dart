import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class AppTheme {
  static const seedColor = MyColors.lightBlueApp;
  final _colorScheme = ColorScheme.fromSeed(seedColor: seedColor);
  final _darkColorScheme = ColorScheme.fromSwatch(
      primarySwatch: seedColor, brightness: Brightness.dark);

  ThemeData get lightTheme => ThemeData(
        brightness: Brightness.light,
        useMaterial3: true,
        colorScheme: _colorScheme,
      );

  ThemeData get darkTheme => ThemeData(
        brightness: Brightness.dark,
        useMaterial3: true,
        colorScheme: _darkColorScheme,
      );

  ThemeData getTheme(bool isDarkMode) {
    return isDarkMode ? darkTheme : lightTheme;
  }
}