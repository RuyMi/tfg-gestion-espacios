import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/screens/screens.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';

class MisReservasScreen extends StatefulWidget {
  const MisReservasScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _MisReservasScreenState createState() => _MisReservasScreenState();
}

class _MisReservasScreenState extends State<MisReservasScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Reserva> misReservasFiltradas = [];

  @override
  void initState() {
    super.initState();
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);

    misReservasFiltradas = reservasProvider.misReservas;

    reservasProvider.fetchMyReservas().then((value) => setState(() {
          misReservasFiltradas = reservasProvider.misReservas;
        }));
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

    return Scaffold(
        resizeToAvoidBottomInset: false,
        appBar: AppBar(
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
                await reservasProvider.fetchMyReservas();
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
          Padding(
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
          if (misReservasFiltradas.isEmpty)
            Center(
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
          if (misReservasFiltradas.isNotEmpty)
            SizedBox(
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
                                        children: [
                                          Text(
                                            reserva.spaceName,
                                            style: const TextStyle(
                                                fontWeight: FontWeight.bold,
                                                fontSize: 18,
                                                fontFamily: 'KoHo'),
                                          ),
                                          Text(
                                              DateFormat('dd/MM/yyyy HH:mm')
                                                  .format(DateTime.parse(
                                                      reserva.startTime)),
                                              style: const TextStyle(
                                                  fontWeight: FontWeight.normal,
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
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              Row(
                                                children: [
                                                  IconButton(
                                                    padding: EdgeInsets.zero,
                                                    constraints:
                                                        const BoxConstraints(),
                                                    icon: Icon(
                                                        Icons.share_rounded,
                                                        color: theme.colorScheme
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
                                                        Icons.bookmark_rounded,
                                                        color: theme.colorScheme
                                                            .onBackground,
                                                        size: 20),
                                                    onPressed: () {
                                                      Navigator.pushNamed(
                                                        context,
                                                        '/editar-reserva',
                                                        arguments: reserva,
                                                      );
                                                    },
                                                  ),
                                                ],
                                              ),
                                              Row(
                                                children: [
                                                  Text(reserva.status!,
                                                      style: TextStyle(
                                                          fontFamily: 'KoHo',
                                                          fontWeight:
                                                              FontWeight.bold,
                                                          color: theme
                                                              .colorScheme
                                                              .secondary,
                                                          fontSize: 12)),
                                                  const SizedBox(
                                                    width: 5,
                                                  ),
                                                  Icon(
                                                      Icons
                                                          .info_outline_rounded,
                                                      color: theme.colorScheme
                                                          .secondary,
                                                      size: 16),
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
            )
        ]));
  }
}
