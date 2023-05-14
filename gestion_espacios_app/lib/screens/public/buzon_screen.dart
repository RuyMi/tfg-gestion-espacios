import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

class BuzonScreen extends StatefulWidget {
  const BuzonScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _BuzonScreenState createState() => _BuzonScreenState();
}

class _BuzonScreenState extends State<BuzonScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: const Text('Tablón de anuncios'),
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
        backgroundColor: MyColors.whiteApp,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.developer_mode,
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
                'En desarrollo',
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
    );
  }
}
