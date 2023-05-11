import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/main_screen.dart';
import 'package:gestion_espacios_app/screens/login_screen.dart';
import 'package:gestion_espacios_app/screens/screens.dart';
import 'package:gestion_espacios_app/theme/app_theme.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: AppTheme().lightTheme,
      initialRoute: '/',
      routes: {
        '/': (context) => const SplashScreen(),
        'login':(context) => const LoginScreen(),
        '/home': (context) => const MainScreen(),
        '/espacios': (context) => const EspaciosScreen(),
        '/buzon': (context) => const BuzonScreen(),
        '/perfil': (context) => const PerfilScreen(),
      },
    );
  }
}