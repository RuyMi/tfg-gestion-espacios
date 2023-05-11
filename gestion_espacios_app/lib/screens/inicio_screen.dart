import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

import '../models/colors.dart';

class InicioScreen extends StatefulWidget {
  const InicioScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _InicioScreenState createState() => _InicioScreenState();
}

class _InicioScreenState extends State<InicioScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: const Text('Inicio'),
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
        // actions: [
        //   IconButton(
        //     onPressed: () {},
        //     icon: const Icon(Icons.search),
        //     color: MyColors.blackApp,
        //     iconSize: 25,
        //   ),
        // ],
        backgroundColor: MyColors.whiteApp,
      ),
      body: Center(
        child: SizedBox(
          width: 200,
          height: 200,
          child: Image.asset('assets/image/logo.png'),
        ),
      ),
    );
  }
}
