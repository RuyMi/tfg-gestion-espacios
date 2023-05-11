import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

import '../models/colors.dart';

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
        title: const Text('Mis reservas'),
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
    );
  }
}
