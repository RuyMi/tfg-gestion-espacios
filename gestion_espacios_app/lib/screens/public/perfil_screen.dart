import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/widgets/logout_widget.dart';

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
                  image: AssetImage('assets/images/profile_pic.png'),
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
            const SizedBox(height: 20),
            const Row(
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
            ),
            const SizedBox(height: 20),
            SizedBox(
              width: 150,
              child: Column(
                children: [
                  TextButton(
                    child: const Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.settings),
                        SizedBox(width: 10),
                        Text(
                          'Ajustes',
                          style: TextStyle(
                              fontFamily: 'KoHo',
                              color: MyColors.blackApp,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    onPressed: () {},
                  ),
                  const SizedBox(height: 10),
                  TextButton(
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const MyLogoutAlert();
                        },
                      );
                    },
                    child: const Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.logout),
                        SizedBox(width: 10),
                        Text(
                          'Cerrar sesión',
                          style: TextStyle(
                            fontFamily: 'KoHo',
                            color: MyColors.blackApp,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
      backgroundColor: MyColors.whiteApp,
    );
  }
}
