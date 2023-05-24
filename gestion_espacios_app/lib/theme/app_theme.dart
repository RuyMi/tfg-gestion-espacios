import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class AppTheme {
  static ThemeData lightThemeData =
      themeData(lightColorScheme, MyColors.blackApp.withOpacity(0.12));
  static ThemeData darkThemeData = 
  themeData(darkColorScheme, MyColors.whiteApp.withOpacity(0.12));

  static ThemeData themeData(ColorScheme colorScheme, Color focusColor) {
    return ThemeData(
      useMaterial3: true,
      colorScheme: colorScheme,
      appBarTheme: AppBarTheme(
        backgroundColor: colorScheme.background,
        elevation: 0,
        iconTheme: IconThemeData(color: colorScheme.surface),
      ),
      iconTheme: IconThemeData(color: colorScheme.onPrimary),
      canvasColor: colorScheme.background,
      scaffoldBackgroundColor: colorScheme.background,
      highlightColor: Colors.transparent,
      focusColor: focusColor,
    );
  }

  static ColorScheme lightColorScheme = const ColorScheme(
    primary: MyColors.lightBlueApp,
    onPrimary: MyColors.whiteApp,
    primaryContainer: MyColors.lightBlueApp,
    secondary: MyColors.pinkApp,
    onSecondary: MyColors.whiteApp,
    secondaryContainer: MyColors.pinkApp,
    surface: MyColors.blackApp,
    onSurface: MyColors.blackApp,
    background: MyColors.whiteApp,
    onBackground: MyColors.lightBlueApp,
    error: MyColors.pinkApp,
    onError: MyColors.whiteApp,
    brightness: Brightness.light,
  );

  static ColorScheme darkColorScheme = const ColorScheme(
    primary: MyColors.darkBlueApp,
    onPrimary: MyColors.whiteApp,
    primaryContainer: MyColors.darkBlueApp,
    secondary: MyColors.pinkApp,
    onSecondary: MyColors.whiteApp,
    secondaryContainer: MyColors.pinkApp,
    surface: MyColors.whiteApp,
    onSurface: MyColors.whiteApp,
    background: MyColors.darkBlueApp,
    onBackground: MyColors.lightBlueApp,
    error: MyColors.pinkApp,
    onError: MyColors.whiteApp,
    brightness: Brightness.dark,
  );
}