import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class PerfilScreen extends StatefulWidget {
  const PerfilScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _PerfilScreenState createState() => _PerfilScreenState();
}

class _PerfilScreenState extends State<PerfilScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: true,
        centerTitle: true,
        title: const Text('Perfil'),
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
        backgroundColor: MyColors.whiteApp,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            const SizedBox(height: 20),
            Container(
              width: 75,
              height: 75,
              decoration: const BoxDecoration(
                shape: BoxShape.circle,
                image: DecorationImage(
                  image: AssetImage('assets/images/logo.png'),
                  fit: BoxFit.cover,
                ),
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'Nombre y Apellidos',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 20,
                fontFamily: 'KoHo',
              ),
            ),
            const Text(
              '@nombre_de_usuario',
              style: TextStyle(
                fontSize: 16,
                fontFamily: 'KoHo',
              ),
            ),
          ],
        ),
      ),
      backgroundColor: MyColors.whiteApp,
    );
  }
}
