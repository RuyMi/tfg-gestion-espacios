import 'package:shared_preferences/shared_preferences.dart';

class ThemePreference {
  static const String _themeKey = 'theme';

  Future<bool> setDarkMode(bool value) async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.setBool(_themeKey, value);
  }

  Future<bool> getDarkMode() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_themeKey) ?? false;
  }
}