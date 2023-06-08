import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/widgets/logout_widget.dart';
import 'package:gestion_espacios_app/widgets/user_image_widget.dart';
import 'package:provider/provider.dart';

class PerfilScreen extends StatefulWidget {
  const PerfilScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _PerfilScreenState createState() => _PerfilScreenState();
}

class _PerfilScreenState extends State<PerfilScreen> {
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
    final usuario = usuarioProvider.actualUsuario;
    var theme = Theme.of(context);

    return Scaffold(
      resizeToAvoidBottomInset: true,
      backgroundColor: theme.colorScheme.background,
      appBar: AppBar(
        toolbarHeight: 75,
        centerTitle: true,
        title: const Text('Perfil'),
        titleTextStyle: TextStyle(
          fontFamily: 'KoHo',
          color: theme.colorScheme.surface,
          fontWeight: FontWeight.bold,
          fontSize: 25,
        ),
        leading: IconButton(
          onPressed: () {
            Navigator.pop(context);
            Navigator.pushNamed(context, '/home');
          },
          icon: const Icon(Icons.arrow_back_ios_rounded),
        ),
        backgroundColor: theme.colorScheme.background,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            const SizedBox(height: 20),
            Container(
              width: 75,
              height: 75,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(50),
              ),
              child: ClipRRect(
                  borderRadius: BorderRadius.circular(50),
                  child: MyUserImageWidget(
                    image: usuario.avatar,
                  )),
            ),
            const SizedBox(height: 10),
            Text(
              usuario.name,
              style: const TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 20,
                fontFamily: 'KoHo',
              ),
            ),
            Text(
              '@${usuario.username}',
              style: const TextStyle(
                fontSize: 16,
                fontFamily: 'KoHo',
              ),
            ),
            const SizedBox(height: 5),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(
                  usuario.credits.toString(),
                  style: TextStyle(
                    fontFamily: 'KoHo',
                    color: theme.colorScheme.secondary,
                    fontWeight: FontWeight.bold,
                    fontSize: 15,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 2),
                  child: Icon(
                    Icons.monetization_on_outlined,
                    color: theme.colorScheme.secondary,
                    size: 20,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 10),
            SizedBox(
              width: 200,
              child: Column(
                children: [
                  TextButton(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.settings_rounded,
                          color: theme.colorScheme.onBackground,
                        ),
                        const SizedBox(width: 10),
                        Text(
                          'Ajustes',
                          style: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.surface,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text('Funcionalidad no disponible.',
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onSecondary,
                                fontWeight: FontWeight.bold,
                              )),
                          duration: const Duration(seconds: 1),
                          backgroundColor: theme.colorScheme.secondary,
                        ),
                      );
                    },
                  ),
                  const SizedBox(height: 5),
                  TextButton(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.lock_rounded,
                          color: theme.colorScheme.onBackground,
                        ),
                        const SizedBox(width: 10),
                        Text(
                          'Cambiar contraseña',
                          style: TextStyle(
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.surface,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text('Funcionalidad no disponible.',
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onSecondary,
                                fontWeight: FontWeight.bold,
                              )),
                          duration: const Duration(seconds: 1),
                          backgroundColor: theme.colorScheme.secondary,
                        ),
                      );
                    },
                  ),
                  const SizedBox(height: 5),
                  TextButton(
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const MyLogoutAlert(ruta: '/login');
                        },
                      );
                    },
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.logout_rounded,
                          color: theme.colorScheme.onBackground,
                        ),
                        const SizedBox(width: 10),
                        Text(
                          'Cerrar sesión',
                          style: TextStyle(
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.surface,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 5),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
