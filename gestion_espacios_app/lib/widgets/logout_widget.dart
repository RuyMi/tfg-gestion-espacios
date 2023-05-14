import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class MyLogoutAlert extends StatelessWidget {
  const MyLogoutAlert({super.key});

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      backgroundColor: MyColors.pinkApp,
      shape: const RoundedRectangleBorder(
        side: BorderSide(
          color: MyColors.whiteApp,
          width: 2,
        ),
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      title: const Text('¿Desea cerrar sesión?',
          style: TextStyle(
            color: MyColors.whiteApp,
            fontFamily: 'KoHo',
          )),
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
            // TODO: función para cerrar sesión.

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
