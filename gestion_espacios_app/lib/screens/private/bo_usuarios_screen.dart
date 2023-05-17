import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:provider/provider.dart';

class UsuariosBOScreen extends StatelessWidget {
  const UsuariosBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final usuarioProvider = Provider.of<UsuariosProvider>(context);
    final usuarios = usuarioProvider.usuarios;

    return usuarios.isEmpty
        ? const Center(
            child: CircularProgressIndicator.adaptive(),
          )
        : StaggeredGridView.countBuilder(
            padding: const EdgeInsets.all(10),
            crossAxisCount: 5,
            itemCount: usuarios.length,
            staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
            mainAxisSpacing: 10,
            crossAxisSpacing: 10,
            itemBuilder: (BuildContext context, int index) {
              final usuario = usuarios[index];
              return Card(
                color: MyColors.lightBlueApp,
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
                            color: MyColors.blackApp,
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
                              style: const TextStyle(
                                overflow: TextOverflow.ellipsis,
                                fontWeight: FontWeight.bold,
                                fontFamily: 'KoHo',
                                color: MyColors.whiteApp,
                              ),
                            ),
                            Text(usuario.username,
                                style: const TextStyle(
                                  overflow: TextOverflow.ellipsis,
                                  fontStyle: FontStyle.italic,
                                  fontFamily: 'KoHo',
                                  color: MyColors.whiteApp,
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
