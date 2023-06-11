/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/screens/private/bo_add_usuario_dialog.dart';
import 'package:gestion_espacios_app/screens/private/bo_update_usuario_dialog.dart';
import 'package:gestion_espacios_app/widgets/user_image_widget.dart';
import 'package:provider/provider.dart';

import '../../models/colors.dart';

/// Clase que representa la pantalla de usuarios del backoffice.
class UsuariosBOScreen extends StatefulWidget {
  const UsuariosBOScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _UsuariosBOScreen createState() => _UsuariosBOScreen();
}

/// Clase que muestra la pantalla de usuarios del backoffice.
class _UsuariosBOScreen extends State<UsuariosBOScreen> {
  /// El controlador del campo de búsqueda.
  final TextEditingController _searchController = TextEditingController();
  
  /// La lista de usuarios filtrados.
  List<Usuario> usuariosFiltrados = [];

  /// Variable que indica si se muestra el spinner.
  bool _showSpinner = true;

  @override
  void initState() {
    super.initState();
    final usuariosProvider =
        Provider.of<UsuariosProvider>(context, listen: false);

    usuariosProvider.fetchUsuarios().then((value) => setState(() {
          usuariosFiltrados = usuariosProvider.usuarios;
        }));

    Timer(const Duration(seconds: 3), () {
      setState(() {
        _showSpinner = false;
      });
    });
  }

  @override
  void dispose() {
    super.dispose();
    _searchController.dispose();
  }

  /// Método que filtra los usuarios.
  Future<List<Usuario>> filterUsuarios(String query) async {
    final usuariosProvider =
        Provider.of<UsuariosProvider>(context, listen: false);
    List<Usuario> usuarios = usuariosProvider.usuarios;

    return usuarios
        .where((usuario) =>
            usuario.name.toLowerCase().contains(query.toLowerCase()))
        .toList();
  }

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    return Column(children: [
      Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          children: [
            Expanded(
              child: TextField(
                controller: _searchController,
                cursorColor: theme.colorScheme.secondary,
                style: TextStyle(
                    color: theme.colorScheme.secondary, fontFamily: 'KoHo'),
                decoration: InputDecoration(
                  filled: true,
                  fillColor: MyColors.pinkApp.shade100,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                    borderSide: BorderSide.none,
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                    borderSide: BorderSide.none,
                  ),
                  hintText: 'Buscar',
                  hintStyle: TextStyle(
                    fontFamily: 'KoHo',
                    color: theme.colorScheme.secondary,
                    overflow: TextOverflow.ellipsis,
                  ),
                  prefixIcon: Icon(Icons.search_rounded,
                      color: theme.colorScheme.secondary, size: 30),
                ),
                onChanged: (value) {
                  filterUsuarios(value).then((usuarios) {
                    setState(() {
                      usuariosFiltrados = usuarios;
                    });
                  });
                },
              ),
            ),
            const SizedBox(width: 20),
            Column(
              children: [
                ElevatedButton.icon(
                  onPressed: () {
                    showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const NuevoUsuarioBODialog();
                        });
                  },
                  icon: Icon(Icons.add_rounded,
                      color: theme.colorScheme.onSecondary),
                  label: Text(
                    'Nuevo',
                    style: TextStyle(
                      color: theme.colorScheme.onSecondary,
                      overflow: TextOverflow.ellipsis,
                      fontFamily: 'KoHo',
                      fontSize: 20,
                    ),
                  ),
                  style: ElevatedButton.styleFrom(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30),
                    ),
                    backgroundColor: theme.colorScheme.secondary,
                  ),
                ),
              ],
            )
          ],
        ),
      ),
      if (usuariosFiltrados.isEmpty)
        Center(
          child: _showSpinner
              ? CircularProgressIndicator.adaptive(
                  valueColor: AlwaysStoppedAnimation<Color>(
                      theme.colorScheme.secondary),
                )
              : Center(
                child: Container(
                  margin: const EdgeInsets.all(20),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.hide_source_rounded,
                        size: 100,
                        color: theme.colorScheme.onBackground,
                      ),
                      const SizedBox(height: 20),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: theme.colorScheme.background,
                          borderRadius: BorderRadius.circular(20),
                          border: Border.all(
                            color: theme.colorScheme.onBackground,
                            width: 2,
                          ),
                        ),
                        child: const Text(
                          'No existen usuarios disponibles',
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
        ),
      if (usuariosFiltrados.isNotEmpty)
        Expanded(
          child: StaggeredGridView.countBuilder(
            padding: const EdgeInsets.all(10),
            crossAxisCount: 5,
            itemCount: usuariosFiltrados.length,
            staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
            mainAxisSpacing: 10,
            crossAxisSpacing: 10,
            itemBuilder: (BuildContext context, int index) {
              final usuario = usuariosFiltrados[index];
              return InkWell(
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return EditarUsuarioBODialog(usuario: usuario);
                    },
                  );
                },
                child: Card(
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
                          ),
                          child: ClipRRect(
                              borderRadius: BorderRadius.circular(50),
                              child: MyUserImageWidget(
                                image: usuario.avatar,
                              )),
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
                              Text('@${usuario.username}',
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
                ),
              );
            },
          ),
        )
    ]);
  }
}
