import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:provider/provider.dart';

class UsuariosBOScreen extends StatelessWidget {
  const UsuariosBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final usuariosProvider = Provider.of<UsuariosProvider>(context);
    final usuarios = usuariosProvider.usuarios;

    if (usuarios.isEmpty) {
      return const Center(
        child: CircularProgressIndicator(),
      );
    } else {
      return StaggeredGridView.countBuilder(
        padding: const EdgeInsets.all(10),
        crossAxisCount: 5,
        itemCount: usuarios.length,
        staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
        mainAxisSpacing: 10,
        crossAxisSpacing: 10,
        itemBuilder: (BuildContext context, int index) {
          final usuario = usuarios[index];
          return Card(
            color: theme.colorScheme.onBackground,
            child: Container(
              constraints: const BoxConstraints(
                maxHeight: 200,
                minHeight: 150,
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(50),
                      border: Border.all(
                        color: theme.colorScheme.surface,
                        width: 2,
                      ),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(50),
                      child: Image.asset('assets/images/profile_pic.png',
                          width: 100, height: 100, fit: BoxFit.cover),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Text(
                          usuario.name,
                          style: TextStyle(
                            overflow: TextOverflow.ellipsis,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.onPrimary,
                          ),
                        ),
                        Text(usuario.username,
                            style: TextStyle(
                              overflow: TextOverflow.ellipsis,
                              fontStyle: FontStyle.italic,
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onPrimary,
                            )),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      );
    }
  }
}
