import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
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
    final usuariosProvider = Provider.of<UsuariosProvider>(context);
    final espaciosProvider = EspaciosProvider(usuariosProvider);
    final espacios = espaciosProvider.espaciosReservables;
    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          centerTitle: true,
          title: const Column(
            children: [
              Text('IES Luis Vives'),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    '100',
                    style: TextStyle(
                      fontFamily: 'KoHo',
                      color: MyColors.pinkApp,
                      fontWeight: FontWeight.bold,
                      fontSize: 15,
                    ),
                  ),
                  Icon(
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
            ? const Center(
                child: CircularProgressIndicator.adaptive(),
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
                                      'assets/images/sala_stock.jpg',
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
                                      Text(espacio.authorizedRoles.toString(),
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
