import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/providers/theme_provider.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/screens/public/reservar_espacios_screen.dart';
import 'package:gestion_espacios_app/screens/screens.dart';
import 'package:gestion_espacios_app/theme/app_theme.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: ThemeProvider()),
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProxyProvider<AuthProvider, UsuariosProvider>(
          create: (context) => UsuariosProvider(null),
          update: (context, authProvider, _) =>
              UsuariosProvider(authProvider.token),
        ),
        ChangeNotifierProxyProvider<AuthProvider, EspaciosProvider>(
          create: (context) => EspaciosProvider(null),
          update: (context, authProvider, _) =>
              EspaciosProvider(authProvider.token),
        ),
        ChangeNotifierProxyProvider<AuthProvider, ReservasProvider>(
          create: (context) => ReservasProvider(null),
          update: (context, authProvider, _) =>
              ReservasProvider(authProvider.token),
        ),
      ],
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    var themeProvider = context.watch<ThemeProvider>();
    
    return MaterialApp(
      title: 'Luis Vives',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightThemeData,
      darkTheme: AppTheme.darkThemeData,
      themeMode: themeProvider.isDarkMode ? ThemeMode.dark : ThemeMode.light,
      initialRoute: '/',
      routes: {
        '/': (context) => const SplashScreen(),

        // Public
        '/login': (context) => const LoginScreen(),
        '/home': (context) => const MainScreen(),
        '/espacios': (context) => const EspaciosScreen(),
        '/mis-reservas': (context) => const MisReservasScreen(),
        '/buzon': (context) => const BuzonScreen(),
        '/perfil': (context) => const PerfilScreen(),
        '/reservar-espacio': (context) => const ReservaEspacioScreen(),
        '/editar-reserva': (context) => const EditarReservaScreen(),

        // Private
        '/login-bo': (context) => const BOLoginScreen(),
        '/home-bo': (context) => const BOMainScreen(),
      },
    );
  }
}
