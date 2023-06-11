/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/preferences/app_preferences.dart';

/// Clase que gestiona el tema de la aplicación.
class ThemeProvider extends ChangeNotifier {
  /// Preferencias de la aplicación.
  final ThemePreference _preference = ThemePreference();

  /// Booleano que indica si el tema es oscuro o no.
  bool _isDarkMode = false;

  /// Constructor.
  ThemeProvider() {
    _loadTheme();
  }

  /// Getter del booleano que indica si el tema es oscuro o no.
  bool get isDarkMode => _isDarkMode;

  /// Función que cambia el tema.
  void toggleTheme() {
    _isDarkMode = !_isDarkMode;
    _preference.setDarkMode(_isDarkMode);
    notifyListeners();
  }

  /// Función que carga el tema.
  Future<void> _loadTheme() async {
    _isDarkMode = await _preference.getDarkMode();
    notifyListeners();
  }
}
