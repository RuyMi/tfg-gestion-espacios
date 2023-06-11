/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:shared_preferences/shared_preferences.dart';

/// Clase que gestiona las preferencias de la aplicación.
class ThemePreference {
  /// Clave para el tema en las preferencias.
  static const String _themeKey = 'theme';

  /// Función que determina el tema actual en las preferencias.
  Future<bool> setDarkMode(bool value) async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.setBool(_themeKey, value);
  }

  /// Función que devuelve el tema actual de las preferencias.
  Future<bool> getDarkMode() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_themeKey) ?? false;
  }
}
