import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class MyDeleteAlert extends StatelessWidget {
  final String title;
  final String ruta;

  const MyDeleteAlert({
    Key? key,
    required this.title,
    required this.ruta,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      backgroundColor: MyColors.pinkApp,
      title: Column(
        children: [
          const Icon(
            Icons.info,
            size: 60,
            color: Colors.white,
          ),
          const SizedBox(height: 10),
          Text(title,
              textAlign: TextAlign.center,
              style: const TextStyle(
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
            // TODO: función para cerrar sesión.

            Navigator.pushNamedAndRemoveUntil(
              context,
              ruta,
              (Route<dynamic> route) => false,
            );
          },
        ),
      ],
    );
  }
}
