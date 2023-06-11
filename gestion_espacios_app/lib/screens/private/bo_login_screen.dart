/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

/// Pantalla de inicio de sesión del BackOffice.
class BOLoginScreen extends StatefulWidget {
  const BOLoginScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _BOLoginScreen createState() => _BOLoginScreen();
}

/// Clase que muestra la pantalla de inicio de sesión del BackOffice.
class _BOLoginScreen extends State<BOLoginScreen> {
  /// Nombre de usuario.
  String username = '';

  /// Contraseña.
  String password = '';

  /// Indica si se está cargando la pantalla.
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    /// Se obtieen el tema actual.
    var theme = Theme.of(context);

    /// Se obtiene el provider de autenticación.
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

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
                      'BackOffice',
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
                              color: theme.colorScheme.surface),
                          prefixIcon: Icon(Icons.person_rounded,
                              color: theme.colorScheme.surface),
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
                              color: theme.colorScheme.surface),
                          prefixIcon: Icon(Icons.lock_rounded,
                              color: theme.colorScheme.surface),
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
                            final roles = authProvider.usuario.userRole;
                            if (roles.contains('ADMINISTRATOR')) {
                              Navigator.pushNamed(context, '/home-bo');

                              setState(() {
                                isLoading = false;
                              });
                            } else {
                              setState(() {
                                isLoading = false;
                              });

                              showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return const MyErrorMessageDialog(
                                      title: 'Error al iniciar sesión',
                                      description:
                                          'El usuario no tiene permisos para acceder al BackOffice.',
                                    );
                                  });
                            }
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
                                      error.toString().indexOf(':') + 1),
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
                        textAlign: TextAlign.center,
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
