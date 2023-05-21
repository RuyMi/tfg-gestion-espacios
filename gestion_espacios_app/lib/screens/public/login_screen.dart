import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

// ignore: must_be_immutable
class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
  String username = '';
  String password = '';
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: SafeArea(
        child: Container(
          margin: const EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset('assets/images/logo.png'),
              const SizedBox(height: 50),
              TextField(
                onChanged: (value) => username = value,
                decoration: InputDecoration(
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  labelText: 'Nombre de usuario',
                  labelStyle: const TextStyle(
                      fontFamily: 'KoHo', color: MyColors.blackApp),
                  prefixIcon:
                      const Icon(Icons.person, color: MyColors.lightBlueApp),
                ),
              ),
              const SizedBox(height: 10),
              TextField(
                onChanged: (value) => password = value,
                obscureText: true,
                decoration: InputDecoration(
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  labelText: 'Contraseña',
                  labelStyle: const TextStyle(
                      fontFamily: 'KoHo', color: MyColors.blackApp),
                  prefixIcon:
                      const Icon(Icons.lock, color: MyColors.lightBlueApp),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  authProvider.login(username, password).then(
                    (usuario) {
                      final loginSucceed = authProvider.loginSucceed;
      
                      if (loginSucceed) {
                        Navigator.pushNamed(context, '/home');
                      } else {
                        showDialog(
                          context: context,
                          builder: (context) => const MyErrorMessageDialog(
                            title: 'Error al iniciar sesión',
                            description: 'Usuario o contraseña incorrectos.',
                          ),
                        );
                      }
                    },
                  );
                },
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  backgroundColor: MyColors.pinkApp,
                ),
                child: const Text('Validar',
                    style:
                        TextStyle(color: MyColors.whiteApp, fontFamily: 'KoHo')),
              ),
              const SizedBox(height: 10),
              TextButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/login-bo');
                },
                child: const Text(
                  'Acceso al área privada del centro.',
                  style: TextStyle(
                    fontFamily: 'KoHo',
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
