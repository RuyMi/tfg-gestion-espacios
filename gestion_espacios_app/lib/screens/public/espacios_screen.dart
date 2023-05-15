import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/public/reservar_espacios_screen.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

class EspaciosScreen extends StatefulWidget {
  const EspaciosScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EspaciosScreenState createState() => _EspaciosScreenState();
}

class _EspaciosScreenState extends State<EspaciosScreen> {
  @override
  Widget build(BuildContext context) {
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
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(
                              color: MyColors.pinkApp,
                              width: 2,
                            ),
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
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
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
                                              color: MyColors.lightBlueApp),
                                          onPressed: () {
                                            Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder: (context) =>
                                                    const ReservaEspacioScreen(),
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
