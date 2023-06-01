import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/screens/private/bo_update_espacio_dialog.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:provider/provider.dart';

import '../../models/colors.dart';
import '../../models/espacio.dart';
import 'bo_add_espacio_dialog.dart';

class EspaciosBOScreen extends StatefulWidget {
  const EspaciosBOScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EspaciosBOScreen createState() => _EspaciosBOScreen();
}

class _EspaciosBOScreen extends State<EspaciosBOScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Espacio> espaciosFiltrados = [];

  @override
  void initState() {
    super.initState();
    final espaciosProvider =
        Provider.of<EspaciosProvider>(context, listen: false);

    espaciosFiltrados = espaciosProvider.espacios;

    espaciosProvider.fetchEspacios().then((value) => setState(() {
          espaciosFiltrados = espaciosProvider.espacios;
        }));
  }

  @override
  void dispose() {
    super.dispose();
    _searchController.dispose();
  }

  Future<List<Espacio>> filterEspacios(String query) async {
    final espaciosProvider =
        Provider.of<EspaciosProvider>(context, listen: false);
    List<Espacio> espacios = espaciosProvider.espacios;

    return espacios
        .where((espacio) =>
            espacio.name.toLowerCase().contains(query.toLowerCase()))
        .toList();
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    return Column(
      children: [
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
                    filterEspacios(value).then((value) => setState(() {
                          espaciosFiltrados = value;
                        }));
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
                            return const NuevoEspacioBODialog();
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
        if (espaciosFiltrados.isEmpty) const SizedBox(height: 20),
        Center(
          child: CircularProgressIndicator.adaptive(
            valueColor:
                AlwaysStoppedAnimation<Color>(theme.colorScheme.secondary),
          ),
        ),
        if (espaciosFiltrados.isNotEmpty)
          Expanded(
            child: StaggeredGridView.countBuilder(
              padding: const EdgeInsets.all(10),
              crossAxisCount: 5,
              itemCount: espaciosFiltrados.length,
              staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
              mainAxisSpacing: 10,
              crossAxisSpacing: 10,
              itemBuilder: (BuildContext context, int index) {
                final espacio = espaciosFiltrados[index];
                return InkWell(
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return EditarEspacioBODialog(espacio: espacio);
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
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(20),
                              child: MySpaceImageWidget(image: espacio.image),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Text(
                                  espacio.name,
                                  style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontFamily: 'KoHo',
                                    color: theme.colorScheme.onPrimary,
                                  ),
                                ),
                                Text(espacio.description,
                                    textAlign: TextAlign.center,
                                    overflow: TextOverflow.ellipsis,
                                    style: TextStyle(
                                      fontFamily: 'KoHo',
                                      color: theme.colorScheme.onPrimary,
                                    )),
                                const SizedBox(height: 5),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(espacio.price.toString(),
                                        style: TextStyle(
                                            fontFamily: 'KoHo',
                                            fontWeight: FontWeight.bold,
                                            color:
                                                theme.colorScheme.secondary)),
                                    Padding(
                                      padding: const EdgeInsets.only(top: 2.0),
                                      child: Icon(
                                        Icons.monetization_on_outlined,
                                        color: theme.colorScheme.secondary,
                                      ),
                                    ),
                                  ],
                                ),
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
      ],
    );
  }
}
