import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:provider/provider.dart';

class MyLogoutAlert extends StatelessWidget {
  const MyLogoutAlert({super.key});

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    return AlertDialog(
      backgroundColor: MyColors.pinkApp,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      title: const Column(
        children: [
          Icon(
            Icons.info,
            size: 60,
            color: MyColors.whiteApp,
          ),
          SizedBox(height: 10),
          Text('¿Desea cerrar sesión?',
              style: TextStyle(
                color: MyColors.whiteApp,
                fontFamily: 'KoHo',
                fontSize: 20,
              )),
        ],
      ),
      actions: [
        TextButton(
          child: const Text('No',
              style: TextStyle(
                color: MyColors.whiteApp,
                fontFamily: 'KoHo',
                fontWeight: FontWeight.bold,
              )),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        TextButton(
          child: const Text('Sí',
              style: TextStyle(
                color: MyColors.whiteApp,
                fontFamily: 'KoHo',
                fontWeight: FontWeight.bold,
              )),
          onPressed: () {
            authProvider.logout();

            Navigator.pushNamedAndRemoveUntil(
              context,
              '/login',
              (Route<dynamic> route) => false,
            );
          },
        ),
      ],
    );
  }
}
