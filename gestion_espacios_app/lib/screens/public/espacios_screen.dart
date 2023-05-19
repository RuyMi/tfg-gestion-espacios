import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/providers.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';
import 'package:provider/provider.dart';

class EspaciosScreen extends StatefulWidget {
  const EspaciosScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EspaciosScreenState createState() => _EspaciosScreenState();
}

class _EspaciosScreenState extends State<EspaciosScreen> {
  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    final usuario = authProvider.usuario;

    final espaciosProvider = Provider.of<EspaciosProvider>(context);
    final espacios = espaciosProvider.espaciosReservables;

    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          centerTitle: true,
          title: Column(
            children: [
              const Text('IES Luis Vives'),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    usuario.credits.toString(),
                    style: const TextStyle(
                      fontFamily: 'KoHo',
                      color: MyColors.pinkApp,
                      fontWeight: FontWeight.bold,
                      fontSize: 15,
                    ),
                  ),
                  const Icon(
                    Icons.monetization_on_outlined,
                    color: MyColors.pinkApp,
                    size: 20,
                  ),
                ],
              )
            ],
          ),
          titleTextStyle: const TextStyle(
            fontFamily: 'KoHo',
            color: MyColors.blackApp,
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
              onPressed: () {},
              icon: const Icon(Icons.search),
              color: MyColors.blackApp,
              iconSize: 25,
            ),
          ],
          backgroundColor: MyColors.whiteApp,
        ),
        body: espacios.isEmpty
            ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(
                    Icons.hide_source_rounded,
                    size: 100,
                    color: MyColors.lightBlueApp,
                  ),
                  const SizedBox(height: 20),
                  Container(
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: MyColors.whiteApp,
                      borderRadius: BorderRadius.circular(20),
                      border: Border.all(
                        color: MyColors.lightBlueApp,
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
            )
            : ListView.builder(
                itemCount: espacios.length,
                itemBuilder: (context, index) {
                  final espacio = espacios[index];
                  return Card(
                    color: MyColors.lightBlueApp.shade50,
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
                                      color: MyColors.blackApp.withOpacity(0.5),
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
                                        espacio.name,
                                        style: const TextStyle(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 18,
                                            fontFamily: 'KoHo'),
                                      ),
                                      Text(espacio.description,
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
                                                icon: const Icon(Icons.share,
                                                    color: MyColors.blackApp),
                                                onPressed: () {},
                                              ),
                                              IconButton(
                                                icon: const Icon(
                                                    Icons.bookmark_outline,
                                                    color:
                                                        MyColors.lightBlueApp),
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
                                              Text(espacio.price.toString(),
                                                  style: const TextStyle(
                                                      fontFamily: 'KoHo',
                                                      fontWeight:
                                                          FontWeight.bold,
                                                      color: MyColors.pinkApp)),
                                              const Icon(
                                                  Icons
                                                      .monetization_on_outlined,
                                                  color: MyColors.pinkApp),
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
                }));
  }
}
