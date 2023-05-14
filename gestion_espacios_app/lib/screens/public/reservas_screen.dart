import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class ReservasScreen extends StatefulWidget {
  const ReservasScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _ReservasScreenState createState() => _ReservasScreenState();
}

class _ReservasScreenState extends State<ReservasScreen> {
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
          // leading: IconButton(
          //   onPressed: () {
          //     showDialog(
          //         context: context,
          //         builder: (BuildContext context) {
          //           return const AcercaDeWidget();
          //         });
          //   },
          //   icon: Image.asset('assets/images/logo.png'),
          //   iconSize: 25,
          // ),
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
              color: MyColors.whiteApp,
              margin: const EdgeInsets.all(16),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  children: [
                    Row(
                      children: [
                        Container(
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(
                              color: MyColors.pinkApp,
                              width: 2,
                            ),
                          ),
                          child: Image.asset('assets/images/logo.png',
                              width: 100, height: 100, fit: BoxFit.cover),
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
                                        fontSize: 12,
                                        fontFamily: 'KoHo')),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.end,
                                  children: [
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
                                    IconButton(
                                      icon: const Icon(Icons.bookmark,
                                          color: MyColors.lightBlueApp),
                                      onPressed: () {},
                                    ),
                                    IconButton(
                                      icon: const Icon(Icons.share,
                                          color: MyColors.blackApp),
                                      onPressed: () {},
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
