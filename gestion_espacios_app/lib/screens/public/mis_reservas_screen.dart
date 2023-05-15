import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/public/editar_reserva_screen.dart';

class MisReservasScreen extends StatefulWidget {
  const MisReservasScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _MisReservasScreenState createState() => _MisReservasScreenState();
}

class _MisReservasScreenState extends State<MisReservasScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: true,
          centerTitle: true,
          title: const Text('Mis reservas'),
          titleTextStyle: const TextStyle(
            fontFamily: 'KoHo',
            color: MyColors.blackApp,
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
              color: MyColors.blackApp,
              iconSize: 25,
            ),
          ],
          backgroundColor: MyColors.whiteApp,
        ),
        body: ListView(
          children: [
            Card(
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
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  'Nombre del lugar',
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 18,
                                      fontFamily: 'KoHo'),
                                ),
                                const Text(
                                    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
                                    style: TextStyle(
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
                                          icon: const Icon(Icons.bookmark,
                                              color: MyColors.lightBlueApp),
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
                                    const Row(
                                      children: [
                                        Text('100',
                                            style: TextStyle(
                                                fontFamily: 'KoHo',
                                                fontWeight: FontWeight.bold,
                                                color: MyColors.pinkApp)),
                                        Icon(Icons.monetization_on_outlined,
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
            ),
          ],
        ));
  }
}
