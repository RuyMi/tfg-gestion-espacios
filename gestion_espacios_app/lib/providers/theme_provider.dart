import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/preferences/app_preferences.dart';

class ThemeProvider extends ChangeNotifier {
  final ThemePreference _preference = ThemePreference();

  bool _isDarkMode = false;

  ThemeProvider() {
    _loadTheme();
  }

  bool get isDarkMode => _isDarkMode;

  void toggleTheme() {
    _isDarkMode = !_isDarkMode;
    _preference.setDarkMode(_isDarkMode);
    notifyListeners();
  }

  Future<void> _loadTheme() async {
    _isDarkMode = await _preference.getDarkMode();
    notifyListeners();
  }
}