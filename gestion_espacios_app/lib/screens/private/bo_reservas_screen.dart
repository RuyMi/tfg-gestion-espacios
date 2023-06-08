import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/screens/private/bo_update_reserva_dialog.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:provider/provider.dart';

import '../../models/colors.dart';
import '../../providers/auth_provider.dart';
import 'bo_add_reserva_dialog.dart';

class ReservasBOScreen extends StatefulWidget {
  const ReservasBOScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _ReservasBOScreen createState() => _ReservasBOScreen();
}

class _ReservasBOScreen extends State<ReservasBOScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Reserva> reservasFiltradas = [];
  bool _showSpinner = true;

  @override
  void initState() {
    super.initState();
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);

    reservasProvider.fetchReservas().then((value) => setState(() {
          reservasFiltradas = reservasProvider.reservas;
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

  Future<List<Reserva>> filterReservas(String query) async {
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);
    List<Reserva> reservas = reservasProvider.reservas;

    return reservas
        .where((reserva) =>
            reserva.spaceName.toLowerCase().contains(query.toLowerCase()))
        .toList();
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final authProvider = Provider.of<AuthProvider>(context);
    final Usuario usuario = authProvider.usuario;

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
                    filterReservas(value).then((value) => setState(() {
                          reservasFiltradas = value;
                        }));
                  },
                ),
              ),
              const SizedBox(width: 20),
              ElevatedButton.icon(
                onPressed: () {
                  showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return NuevaReservaBODialog(usuario: usuario);
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
              )
            ],
          ),
        ),
        if (reservasFiltradas.isEmpty)
          Center(
            child: _showSpinner
                ? CircularProgressIndicator.adaptive(
                    valueColor: AlwaysStoppedAnimation<Color>(
                        theme.colorScheme.secondary),
                  )
                : Expanded(
                    child: Center(
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
                                'No existen reservas disponibles',
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
          ),
        if (reservasFiltradas.isNotEmpty)
          Expanded(
            child: StaggeredGridView.countBuilder(
              padding: const EdgeInsets.all(10),
              crossAxisCount: 5,
              itemCount: reservasFiltradas.length,
              staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
              mainAxisSpacing: 10,
              crossAxisSpacing: 10,
              itemBuilder: (BuildContext context, int index) {
                final reserva = reservasFiltradas[index];
                return InkWell(
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return EditarReservaBODialog(reserva: reserva);
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
                              child: MySpaceImageWidget(image: reserva.image),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Text(
                                  reserva.spaceName,
                                  style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontFamily: 'KoHo',
                                    color: theme.colorScheme.onPrimary,
                                  ),
                                ),
                                Text(reserva.userName,
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
                                    Text(reserva.status!,
                                        style: TextStyle(
                                            fontFamily: 'KoHo',
                                            fontWeight: FontWeight.bold,
                                            color:
                                                theme.colorScheme.secondary)),
                                    const SizedBox(width: 5),
                                    Icon(
                                      Icons.access_time_rounded,
                                      color: theme.colorScheme.secondary,
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
          ),
      ],
    );
  }
}
