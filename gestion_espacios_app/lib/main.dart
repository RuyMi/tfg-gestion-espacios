import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/login_screen.dart';
import 'package:gestion_espacios_app/screens/splash_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Gestión de Espacios - IES Luis Vives',
      theme: ThemeData(
          useMaterial3: true,
          colorSchemeSeed: MyColors.lightBlueApp,
          brightness: Brightness.light),
      darkTheme: ThemeData(
          useMaterial3: true,
          colorSchemeSeed: MyColors.darkBlueApp,
          brightness: Brightness.dark),
      themeMode: ThemeMode.light,
      initialRoute: '/',
      routes: {
        '/': (context) => const SplashScreen(),
        '/login': (context) => const LoginScreen(),
      },
    );
  }
}
