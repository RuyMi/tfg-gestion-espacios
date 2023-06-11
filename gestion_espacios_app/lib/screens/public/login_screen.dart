/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

/// Widget que muestra la imagen seleccionada.
class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _LoginScreen createState() => _LoginScreen();
}

/// Clase que muestra la pantalla de inicio de sesión.
class _LoginScreen extends State<LoginScreen> {
  /// El nombre de usuario.
  String username = '';

  /// La contraseña.
  String password = '';

  /// Indica si se está cargando.
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el proveedor de autenticación.
    var authProvider = Provider.of<AuthProvider>(context, listen: false);

    /// Se obtiene el tema actual.
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
                        setState(() {
                          isLoading = true;
                        });

                        authProvider.login(username, password).then(
                          (usuario) {
                            Navigator.pushNamed(context, '/home');

                            setState(() {
                              isLoading = false;
                            });
                          },
                        ).catchError((error) {
                          setState(() {
                            isLoading = false;
                          });

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
                    if (isLoading)
                      Padding(
                        padding: const EdgeInsets.only(top: 20),
                        child: CircularProgressIndicator.adaptive(
                          valueColor: AlwaysStoppedAnimation<Color>(
                              theme.colorScheme.secondary),
                        ),
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