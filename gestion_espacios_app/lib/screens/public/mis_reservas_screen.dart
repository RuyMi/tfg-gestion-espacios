import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/screens/public/editar_reserva_screen.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';

class MisReservasScreen extends StatefulWidget {
  const MisReservasScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _MisReservasScreenState createState() => _MisReservasScreenState();
}

class _MisReservasScreenState extends State<MisReservasScreen> {
  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final reservasProvider = Provider.of<ReservasProvider>(context);
    final authProvider = Provider.of<AuthProvider>(context);

    final usuario = authProvider.usuario;
    reservasProvider.fetchReservasByUser(usuario.uuid);
    final misReservas = reservasProvider.reservasByUser;

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
            onPressed: () {},
            icon: const Icon(Icons.search),
            color: theme.colorScheme.surface,
            iconSize: 25,
          ),
        ],
        backgroundColor: theme.colorScheme.primary,
      ),
      body: SafeArea(
        child: misReservas.isEmpty
            ? Center(
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
                        'No se han realizado reservas aún',
                        style: TextStyle(
                          fontSize: 20,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                        ),
                      ),
                    ),
                  ],
                ),
              )
            : ListView.builder(
                itemCount: misReservas.length,
                itemBuilder: (context, index) {
                  final reserva = misReservas[index];
                  return Card(
                    color: theme.colorScheme.onBackground,
                    margin: const EdgeInsets.all(16),
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              Container(
                                margin: const EdgeInsets.only(left: 10),
                                decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(10),
                                  boxShadow: [
                                    BoxShadow(
                                      color: theme.colorScheme.surface
                                          .withOpacity(0.5),
                                      spreadRadius: 1,
                                      blurRadius: 5,
                                      offset: const Offset(0, 3),
                                    ),
                                  ],
                                ),
                                child: ClipRRect(
                                  borderRadius: BorderRadius.circular(10),
                                  child: Image.asset(
                                      'assets/images/image_placeholder.png',
                                      width: 100,
                                      height: 100,
                                      fit: BoxFit.cover),
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
                                          DateFormat('dd/MM/yyyy HH:mm').format(
                                              DateTime.parse(
                                                  reserva.startTime)),
                                          style: const TextStyle(
                                              fontWeight: FontWeight.normal,
                                              overflow: TextOverflow.ellipsis,
                                              fontSize: 12,
                                              fontFamily: 'KoHo'),
                                          maxLines: 3),
                                      Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Row(
                                            children: [
                                              IconButton(
                                                icon: Icon(Icons.share,
                                                    color: theme
                                                        .colorScheme.surface),
                                                onPressed: () {},
                                              ),
                                              IconButton(
                                                icon: Icon(Icons.bookmark,
                                                    color: theme.colorScheme
                                                        .onBackground),
                                                onPressed: () {
                                                  Navigator.push(
                                                    context,
                                                    MaterialPageRoute(
                                                      builder: (context) =>
                                                          const EditarReservaScreen(),
                                                    ),
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
                                                      color: theme.colorScheme
                                                          .secondary)),
                                              Icon(
                                                  Icons
                                                      .monetization_on_outlined,
                                                  color: theme
                                                      .colorScheme.secondary),
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
                  );
                }),
      ),
    );
  }
}
