import 'package:flutter/material.dart';
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
    var authProvider = Provider.of<AuthProvider>(context, listen: false);
    var theme = Theme.of(context);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
        resizeToAvoidBottomInset: true,
        body: SingleChildScrollView(
          padding: EdgeInsets.only(
              bottom: MediaQuery.of(context).viewInsets.bottom, top: 50),
          child: Container(
            margin: const EdgeInsets.all(20),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Image.asset('assets/images/logo.png'),
                  const SizedBox(height: 10),
                  const Text(
                    'Gestión de espacios',
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
                      cursorColor: theme.colorScheme.secondary,
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
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.onSurface),
                        prefixIcon: Icon(Icons.person,
                            color: theme.colorScheme.onSurface),
                      ),
                    ),
                  ),
                  const SizedBox(height: 10),
                  SizedBox(
                    width: 400,
                    child: TextField(
                      onChanged: (value) => password = value,
                      cursorColor: theme.colorScheme.secondary,
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
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.onSurface),
                        prefixIcon: Icon(Icons.lock,
                            color: theme.colorScheme.onSurface),
                      ),
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
                                description:
                                    'Usuario o contraseña incorrectos.',
                              ),
                            );
                          }
                        },
                      ).catchError((error) {
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const MyErrorMessageDialog(
                                title: 'Error al iniciar sesión',
                                description:
                                    'Usuario o contraseña incorrectos.',
                              );
                            });
                      });
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
                      Navigator.pushNamed(context, '/login-bo');
                    },
                    style: TextButton.styleFrom(
                      foregroundColor: theme.colorScheme.onBackground,
                    ),
                    child: Text(
                      'Acceso al área privada del centro.',
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
        ),
      ),
    );
  }
}
