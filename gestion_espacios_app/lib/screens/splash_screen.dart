import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/screens/private/bo_login_screen.dart';
import 'package:gestion_espacios_app/screens/public/login_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();

    _controller = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 1),
    );

    _controller.forward();

    _controller.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        if (kIsWeb) {
          Navigator.pushReplacement(
            context,
            PageRouteBuilder(
              pageBuilder: (context, animation1, animation2) =>
                  const BOLoginScreen(),
              transitionDuration: const Duration(milliseconds: 750),
              transitionsBuilder: (context, animation1, animation2, child) {
                return FadeTransition(
                  opacity: Tween<double>(begin: 0, end: 1).animate(
                    CurvedAnimation(
                      parent: animation1,
                      curve: Curves.easeIn,
                    ),
                  ),
                  child: child,
                );
              },
            ),
          );
        } else {
          Navigator.pushReplacement(
            context,
            PageRouteBuilder(
              pageBuilder: (context, animation1, animation2) =>
                  const LoginScreen(), // Pantalla para otras plataformas
              transitionDuration: const Duration(milliseconds: 750),
              transitionsBuilder: (context, animation1, animation2, child) {
                return FadeTransition(
                  opacity: Tween<double>(begin: 0, end: 1).animate(
                    CurvedAnimation(
                      parent: animation1,
                      curve: Curves.easeIn,
                    ),
                  ),
                  child: child,
                );
              },
            ),
          );
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    return Scaffold(
      backgroundColor: theme.colorScheme.background,
      body: Center(
        child: FadeTransition(
          opacity: Tween<double>(begin: 0, end: 1).animate(
            CurvedAnimation(
              parent: _controller,
              curve: Curves.easeIn,
            ),
          ),
          child: Image.asset(
            'assets/images/logo.png',
            width: 300,
            height: 300,
          ),
        ),
      ),
    );
  }
}
