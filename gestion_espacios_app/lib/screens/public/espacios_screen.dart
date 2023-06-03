import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/providers/providers.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:provider/provider.dart';

import '../../models/colors.dart';

class EspaciosScreen extends StatefulWidget {
  const EspaciosScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EspaciosScreenState createState() => _EspaciosScreenState();
}

class _EspaciosScreenState extends State<EspaciosScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Espacio> espaciosFiltrados = [];

  @override
  void initState() {
    super.initState();
    final espaciosProvider =
        Provider.of<EspaciosProvider>(context, listen: false);

    espaciosFiltrados = espaciosProvider.espaciosReservables;

    espaciosProvider
        .fetchEspaciosByReservable(true)
        .then((value) => setState(() {
              espaciosFiltrados = espaciosProvider.espaciosReservables;
            }));
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
  void dispose() {
    super.dispose();
    _searchController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final authProvider = Provider.of<AuthProvider>(context);
    final usuario = authProvider.usuario;

    final espaciosProvider = Provider.of<EspaciosProvider>(context);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
          resizeToAvoidBottomInset: true,
          appBar: AppBar(
            automaticallyImplyLeading: false,
            centerTitle: true,
            title: Column(
              children: [
                const Text('Espacios', style: TextStyle(fontFamily: 'KoHo')),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
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
                    Icon(
                      Icons.monetization_on_outlined,
                      color: theme.colorScheme.secondary,
                      size: 20,
                    ),
                  ],
                )
              ],
            ),
            titleTextStyle: TextStyle(
              fontFamily: 'KoHo',
              color: theme.colorScheme.surface,
              fontWeight: FontWeight.bold,
              fontSize: 25,
            ),
            leading: IconButton(
              onPressed: () {
                showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return const AcercaDeWidget();
                    });
              },
              icon: Image.asset('assets/images/logo.png'),
              iconSize: 25,
            ),
            actions: [
              IconButton(
                onPressed: () async {
                  await espaciosProvider.fetchEspaciosByReservable(true);
                  setState(() {
                    espaciosFiltrados = espaciosProvider.espaciosReservables;
                  });
                },
                icon: const Icon(Icons.refresh_rounded),
                color: theme.colorScheme.surface,
                iconSize: 25,
              ),
            ],
            backgroundColor: theme.colorScheme.background,
          ),
          body: Column(children: [
            SizedBox(
              height: 80,
              width: MediaQuery.of(context).size.width * 0.9,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
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
            ),
            if (espaciosFiltrados.isEmpty)
              Expanded(
                child: Center(
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
                          'No existen espacios disponibles',
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
            if (espaciosFiltrados.isNotEmpty)
              SizedBox(
                height: MediaQuery.of(context).size.height * 0.8,
                child: ListView.builder(
                    itemCount: espaciosFiltrados.length,
                    itemBuilder: (context, index) {
                      final espacio = espaciosFiltrados[index];
                      return InkWell(
                        onTap: () {
                          Navigator.pushNamed(
                            context,
                            '/reservar-espacio',
                            arguments: espacio,
                          );
                        },
                        child: Card(
                          color: theme.colorScheme.inversePrimary,
                          margin: const EdgeInsets.only(
                              top: 5, bottom: 5, left: 10, right: 10),
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Column(
                              children: [
                                Row(
                                  children: [
                                    Container(
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.circular(10),
                                        boxShadow: [
                                          BoxShadow(
                                            color: theme.colorScheme.surface
                                                .withOpacity(0.2),
                                            spreadRadius: 1,
                                            blurRadius: 5,
                                            offset: const Offset(0, 3),
                                          ),
                                        ],
                                      ),
                                      child: ClipRRect(
                                        borderRadius: BorderRadius.circular(10),
                                        child: MySpaceImageWidget(
                                            image: espacio.image),
                                      ),
                                    ),
                                    const SizedBox(
                                      width: 10,
                                    ),
                                    Expanded(
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              espacio.name,
                                              style: const TextStyle(
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 18,
                                                  fontFamily: 'KoHo'),
                                            ),
                                            Text(espacio.description,
                                                style: const TextStyle(
                                                    fontWeight:
                                                        FontWeight.normal,
                                                    overflow:
                                                        TextOverflow.ellipsis,
                                                    fontSize: 12,
                                                    fontFamily: 'KoHo'),
                                                maxLines: 2),
                                            const SizedBox(
                                              height: 18,
                                            ),
                                            Row(
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.end,
                                              mainAxisAlignment:
                                                  MainAxisAlignment
                                                      .spaceBetween,
                                              children: [
                                                Row(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.end,
                                                  children: [
                                                    IconButton(
                                                      padding: EdgeInsets.zero,
                                                      constraints:
                                                          const BoxConstraints(),
                                                      icon: Icon(
                                                          Icons.share_rounded,
                                                          color: theme
                                                              .colorScheme
                                                              .surface,
                                                          size: 20),
                                                      onPressed: () {},
                                                    ),
                                                    const SizedBox(
                                                      width: 10,
                                                    ),
                                                    IconButton(
                                                      padding: EdgeInsets.zero,
                                                      constraints:
                                                          const BoxConstraints(),
                                                      icon: Icon(
                                                          Icons
                                                              .bookmark_outline,
                                                          color: theme
                                                              .colorScheme
                                                              .onBackground,
                                                          size: 20),
                                                      onPressed: () {
                                                        Navigator.pushNamed(
                                                          context,
                                                          '/reservar-espacio',
                                                          arguments: espacio,
                                                        );
                                                      },
                                                    ),
                                                  ],
                                                ),
                                                Row(
                                                  children: [
                                                    Text(
                                                        espacio.price
                                                            .toString(),
                                                        style: TextStyle(
                                                            fontFamily: 'KoHo',
                                                            fontWeight:
                                                                FontWeight.bold,
                                                            color: theme
                                                                .colorScheme
                                                                .secondary,
                                                            fontSize: 14)),
                                                    Padding(
                                                      padding:
                                                          const EdgeInsets.only(
                                                              top: 2),
                                                      child: Icon(
                                                          Icons
                                                              .monetization_on_outlined,
                                                          color: theme
                                                              .colorScheme
                                                              .secondary,
                                                          size: 16),
                                                    ),
                                                  ],
                                                ),
                                              ],
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ),
                      );
                    }),
              ),
          ])),
    );
  }
}
