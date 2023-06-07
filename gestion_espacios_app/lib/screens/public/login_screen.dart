import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

// ignore: must_be_immutable
class LoginScreen extends StatelessWidget {
  String username = '';
  String password = '';

  LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    var authProvider = Provider.of<AuthProvider>(context, listen: false);
    var theme = Theme.of(context);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
        resizeToAvoidBottomInset: true,
        body: Center(
          child: SingleChildScrollView(
            padding: EdgeInsets.only(
                bottom: MediaQuery.of(context).viewInsets.bottom),
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
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 50),
                    SizedBox(
                      width: 400,
                      child: TextField(
                        onChanged: (value) => username = value,
                        cursorColor: theme.colorScheme.secondary,
                        style: TextStyle(
                          color: theme.colorScheme.surface,
                          fontFamily: 'KoHo',
                        ),
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
                        style: TextStyle(
                          color: theme.colorScheme.surface,
                          fontFamily: 'KoHo',
                        ),
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
                            Navigator.pushNamed(context, '/home');
                          },
                        ).catchError((error) {
                          showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return MyErrorMessageDialog(
                                    title: 'Error al iniciar sesión',
                                    description: error.toString().substring(
                                        error.toString().indexOf(':') + 1));
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
                        Navigator.pushNamed(context, '/user-register');
                      },
                      style: TextButton.styleFrom(
                        foregroundColor: theme.colorScheme.onBackground,
                      ),
                      child: Text(
                        '¿Aún no estás registrado? Regístrate aquí.',
                        style: TextStyle(
                          fontFamily: 'KoHo',
                          color: theme.colorScheme.secondary,
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ),
                    Visibility(
                      visible:
                          defaultTargetPlatform != TargetPlatform.android &&
                              defaultTargetPlatform != TargetPlatform.iOS,
                      child: TextButton(
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
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
