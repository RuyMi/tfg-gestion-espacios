import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

class BOLoginScreen extends StatelessWidget {
  const BOLoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    String username = '';
    String password = '';
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Container(
        margin: const EdgeInsets.all(20),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset('assets/images/logo.png'),
              const SizedBox(height: 10),
              const Text(
                'BackOffice',
                style: TextStyle(
                  fontFamily: 'KoHo',
                  fontSize: 35,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 50),
              SizedBox(
                width: 400,
                child: TextField(
                  onChanged: (value) => username = value,
                  decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30),
                      borderSide: BorderSide(
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30),
                      borderSide: BorderSide(
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                    labelText: 'Nombre de usuario',
                    labelStyle: TextStyle(
                        fontFamily: 'KoHo', color: theme.colorScheme.surface),
                    prefixIcon:
                        Icon(Icons.person, color: theme.colorScheme.surface),
                  ),
                ),
              ),
              const SizedBox(height: 10),
              SizedBox(
                width: 400,
                child: TextField(
                  onChanged: (value) => password = value,
                  obscureText: true,
                  decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30),
                      borderSide: BorderSide(
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30),
                      borderSide: BorderSide(
                        color: theme.colorScheme.onSurface,
                      ),
                    ),
                    labelText: 'Contraseña',
                    labelStyle: TextStyle(
                        fontFamily: 'KoHo', color: theme.colorScheme.surface),
                    prefixIcon:
                        Icon(Icons.lock, color: theme.colorScheme.surface),
                  ),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  authProvider.login(username, password).then(
                    (usuario) {
                      final loginSucceed = authProvider.loginSucceed;
                      final roles = authProvider.usuario.userRole;
        
                      if (loginSucceed && roles.contains('ADMINISTRATOR')) {
                        Navigator.pushNamed(context, '/home-bo');
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
                  backgroundColor: theme.colorScheme.secondary,
                ),
                child: Text('Validar',
                    style: TextStyle(
                        color: theme.colorScheme.onSecondary,
                        fontFamily: 'KoHo')),
              ),
              const SizedBox(height: 10),
              TextButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/login');
                },
                style: TextButton.styleFrom(
                  foregroundColor: theme.colorScheme.onBackground,
                ),
                child: Text(
                  'Acceso al servicio público.',
                  style: TextStyle(
                    fontFamily: 'KoHo',
                    color: theme.colorScheme.secondary,
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
