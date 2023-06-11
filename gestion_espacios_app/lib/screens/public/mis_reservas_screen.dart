import 'dart:async';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/screens/public/editar_reserva_screen.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import 'package:share_plus/share_plus.dart';

class MisReservasScreen extends StatefulWidget {
  const MisReservasScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _MisReservasScreenState createState() => _MisReservasScreenState();
}

class _MisReservasScreenState extends State<MisReservasScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Reserva> misReservasFiltradas = [];
  bool _showSpinner = true;

  @override
  void initState() {
    super.initState();
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);

    misReservasFiltradas = reservasProvider.misReservas;

    reservasProvider.fetchMyReservasNotFinished().then((value) => setState(() {
          misReservasFiltradas = reservasProvider.misReservas;
        }));

    Timer(const Duration(seconds: 3), () {
      setState(() {
        _showSpinner = false;
      });
    });
  }

  Future<List<Reserva>> filterReserva(String query) async {
    final espaciosProvider =
        Provider.of<ReservasProvider>(context, listen: false);
    List<Reserva> reservas = espaciosProvider.misReservas;

    return reservas
        .where((reserva) =>
            reserva.spaceName.toLowerCase().contains(query.toLowerCase()))
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

    final reservasProvider = Provider.of<ReservasProvider>(context);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
          resizeToAvoidBottomInset: true,
          appBar: AppBar(
            toolbarHeight: 75,
            automaticallyImplyLeading: true,
            centerTitle: true,
            title: const Text('Mis reservas'),
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
            actions: [
              IconButton(
                onPressed: () async {
                  await reservasProvider.fetchMyReservasNotFinished();
                  setState(() {
                    misReservasFiltradas = reservasProvider.misReservas;
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
                    filterReserva(value).then((value) => setState(() {
                          misReservasFiltradas = value;
                        }));
                  },
                ),
              ),
            ),
            if (misReservasFiltradas.isEmpty)
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
            if (misReservasFiltradas.isNotEmpty)
              Expanded(
                child: SizedBox(
                  height: MediaQuery.of(context).size.height * 0.8,
                  child: ListView.builder(
                      itemCount: misReservasFiltradas.length,
                      itemBuilder: (context, index) {
                        final reserva = misReservasFiltradas[index];
                        return InkWell(
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => EditarReservaScreen(
                                  reserva: reserva,
                                ),
                              ),
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
                                          borderRadius:
                                              BorderRadius.circular(10),
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
                                          borderRadius:
                                              BorderRadius.circular(10),
                                          child: MySpaceImageWidget(
                                              image: reserva.image),
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
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    reserva.spaceName,
                                                    style: const TextStyle(
                                                        fontWeight:
                                                            FontWeight.bold,
                                                        fontSize: 18,
                                                        fontFamily: 'KoHo'),
                                                    overflow:
                                                        TextOverflow.ellipsis,
                                                    maxLines: 1,
                                                  ),
                                                  Text(
                                                      DateFormat(
                                                              'dd/MM/yyyy HH:mm')
                                                          .format(DateTime
                                                              .parse(reserva
                                                                  .startTime)),
                                                      style: const TextStyle(
                                                          fontWeight:
                                                              FontWeight.normal,
                                                          overflow: TextOverflow
                                                              .ellipsis,
                                                          fontSize: 12,
                                                          fontFamily: 'KoHo'),
                                                      maxLines: 2),
                                                ],
                                              ),
                                              const SizedBox(
                                                height: 20,
                                              ),
                                              Row(
                                                mainAxisAlignment:
                                                    MainAxisAlignment
                                                        .spaceBetween,
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.end,
                                                children: [
                                                  Row(
                                                    children: [
                                                      Row(
                                                        children: [
                                                          SizedBox(
                                                            width: 25,
                                                            height: 25,
                                                            child: IconButton(
                                                              padding:
                                                                  EdgeInsets
                                                                      .zero,
                                                              constraints:
                                                                  const BoxConstraints(),
                                                              icon: Icon(
                                                                  Icons
                                                                      .share_rounded,
                                                                  color: theme
                                                                      .colorScheme
                                                                      .surface,
                                                                  size: 20),
                                                              onPressed: () {
                                                                Share.share(
                                                                    '🎈 Acabo de reservar ${reserva.spaceName}! ¿Quieres venirte?');
                                                              },
                                                            ),
                                                          ),
                                                          const SizedBox(
                                                            width: 10,
                                                          ),
                                                          SizedBox(
                                                            width: 25,
                                                            height: 25,
                                                            child: IconButton(
                                                              padding:
                                                                  EdgeInsets
                                                                      .zero,
                                                              constraints:
                                                                  const BoxConstraints(),
                                                              icon: Icon(
                                                                  Icons
                                                                      .bookmark_rounded,
                                                                  color: theme
                                                                      .colorScheme
                                                                      .onBackground,
                                                                  size: 20),
                                                              onPressed: () {
                                                                Navigator
                                                                    .pushNamed(
                                                                  context,
                                                                  '/editar-reserva',
                                                                  arguments:
                                                                      reserva,
                                                                );
                                                              },
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ],
                                                  ),
                                                  Container(
                                                    height: 25,
                                                    padding:
                                                        const EdgeInsets.all(5),
                                                    child: Row(
                                                      children: [
                                                        Text(reserva.status!,
                                                            style: TextStyle(
                                                                fontFamily:
                                                                    'KoHo',
                                                                fontWeight:
                                                                    FontWeight
                                                                        .bold,
                                                                color: theme
                                                                    .colorScheme
                                                                    .secondary,
                                                                fontSize: 12)),
                                                        const SizedBox(
                                                          width: 5,
                                                        ),
                                                        Icon(
                                                            Icons
                                                                .access_time_rounded,
                                                            color: theme
                                                                .colorScheme
                                                                .secondary,
                                                            size: 16),
                                                      ],
                                                    ),
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
              )
          ])),
    );
  }
}
