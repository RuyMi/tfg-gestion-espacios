import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/user_image_widget.dart';
import 'package:provider/provider.dart';

import '../../widgets/alert_widget.dart';

class RegistroUsuarioScreen extends StatefulWidget {
  const RegistroUsuarioScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _RegistroUsuarioScreenState createState() => _RegistroUsuarioScreenState();
}

class _RegistroUsuarioScreenState extends State<RegistroUsuarioScreen> {
  String name = '';
  String email = '';
  String username = '';
  String password = '';
  String password2 = '';

  @override
  void initState() {
    super.initState();
    final usuarioProvider =
        Provider.of<UsuariosProvider>(context, listen: false);
    usuarioProvider.fetchActualUsuario();
  }

  @override
  Widget build(BuildContext context) {
    final usuarioProvider = Provider.of<UsuariosProvider>(context);
    var theme = Theme.of(context);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
          resizeToAvoidBottomInset: true,
          backgroundColor: theme.colorScheme.background,
          appBar: AppBar(
            centerTitle: true,
            title: const Text('Registro de usuario'),
            titleTextStyle: TextStyle(
              fontFamily: 'KoHo',
              color: theme.colorScheme.surface,
              fontWeight: FontWeight.bold,
              fontSize: 25,
            ),
            leading: IconButton(
              onPressed: () {
                Navigator.pop(context);
                Navigator.pushNamed(context, '/login');
              },
              icon: const Icon(Icons.arrow_back_ios_rounded),
            ),
            backgroundColor: theme.colorScheme.background,
          ),
          body: SingleChildScrollView(
            padding: EdgeInsets.only(
                bottom: MediaQuery.of(context).viewInsets.bottom),
            child: Center(
              child: Container(
                margin: const EdgeInsets.all(20),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    const SizedBox(height: 20),
                    const Text(
                      'Bienvenido a la aplicación del IES Luis Vives',
                      style: TextStyle(
                        fontSize: 20,
                        fontFamily: 'KoHo',
                      ),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 20),
                    Container(
                      width: 100,
                      height: 100,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(50),
                      ),
                      child: ClipRRect(
                          borderRadius: BorderRadius.circular(50),
                          child: const MyUserImageWidget(
                            image: 'assets/images/profile_pic.png',
                          )),
                    ),
                    const SizedBox(height: 40),
                    const Text(
                      'Por favor, complete los datos de registro:',
                      style: TextStyle(
                        fontSize: 16,
                        fontFamily: 'KoHo',
                      ),
                    ),
                    const SizedBox(height: 20),
                    SizedBox(
                      width: 400,
                      child: TextField(
                        onChanged: (value) => name = value,
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
                          labelText: 'Nombre de pila',
                          labelStyle: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onSurface),
                          prefixIcon: Icon(Icons.person,
                              color: theme.colorScheme.onSurface),
                        ),
                      ),
                    ),
                    const SizedBox(height: 15),
                    SizedBox(
                      width: 400,
                      child: TextField(
                        onChanged: (value) => email = value,
                        cursorColor: theme.colorScheme.secondary,
                        keyboardType: TextInputType.emailAddress,
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
                          labelText: 'Correo electrónico',
                          labelStyle: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onSurface),
                          prefixIcon: Icon(Icons.email_rounded,
                              color: theme.colorScheme.onSurface),
                        ),
                      ),
                    ),
                    const SizedBox(height: 15),
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
                          prefixIcon: Icon(Icons.abc_rounded,
                              color: theme.colorScheme.onSurface),
                        ),
                      ),
                    ),
                    const SizedBox(height: 15),
                    SizedBox(
                      width: 400,
                      child: TextField(
                        onChanged: (value) => password = value,
                        obscureText: true,
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
                          labelText: 'Contraseña',
                          labelStyle: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onSurface),
                          prefixIcon: Icon(Icons.lock_rounded,
                              color: theme.colorScheme.onSurface),
                        ),
                      ),
                    ),
                    const SizedBox(height: 15),
                    SizedBox(
                      width: 400,
                      child: TextField(
                        onChanged: (value) => password2 = value,
                        obscureText: true,
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
                          labelText: 'Confirmar contraseña',
                          labelStyle: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onSurface),
                          prefixIcon: Icon(Icons.lock_rounded,
                              color: theme.colorScheme.onSurface),
                        ),
                      ),
                    ),
                    const SizedBox(height: 40),
                    ElevatedButton(
                      onPressed: () {
                        if (password == password2) {
                          Usuario usuario = Usuario(
                              name: name,
                              email: email,
                              username: username,
                              password: password,
                              userRole: ['USER'],
                              credits: 20,
                              avatar: 'placeholder',
                              isActive: true);

                          usuarioProvider.register(usuario).then(
                            (_) {
                              Navigator.pushNamed(context, '/login');
                              showDialog(
                                context: context,
                                builder: (BuildContext context) {
                                  return const MyMessageDialog(
                                    title: 'Registro completado',
                                    description:
                                        'Ya puedes disfrutar de los servicios de la aplicación.',
                                  );
                                },
                              );
                            },
                          ).catchError((error) {
                            showDialog(
                                context: context,
                                builder: (BuildContext context) {
                                  return MyErrorMessageDialog(
                                      title: 'Error al registrarse',
                                      description: error.toString().substring(
                                          error.toString().indexOf(':') + 1));
                                });
                          });
                        } else {
                          showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return const MyErrorMessageDialog(
                                    title: 'Error al registrarse',
                                    description:
                                        'Las contraseñas no coinciden.');
                              });
                        }
                      },
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30),
                        ),
                        backgroundColor: theme.colorScheme.secondary,
                      ),
                      child: Text('Registrarse',
                          style: TextStyle(
                              color: theme.colorScheme.onSecondary,
                              fontFamily: 'KoHo')),
                    ),
                    const SizedBox(height: 20),
                  ],
                ),
              ),
            ),
          )),
    );
  }
}
