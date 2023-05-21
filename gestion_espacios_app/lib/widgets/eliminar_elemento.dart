import 'package:flutter/material.dart';

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
    var theme = Theme.of(context);

    return AlertDialog(
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      backgroundColor: theme.colorScheme.error,
      title: Column(
        children: [
          Icon(
            Icons.info,
            size: 60,
            color: theme.colorScheme.onError,
          ),
          const SizedBox(height: 10),
          Text(title,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: theme.colorScheme.onError,
                fontFamily: 'KoHo',
                fontSize: 20,
              )),
        ],
      ),
      actions: [
        TextButton(
          child: Text('No',
              style: TextStyle(
                color: theme.colorScheme.onError,
                fontFamily: 'KoHo',
                fontWeight: FontWeight.bold,
              )),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        TextButton(
          child: Text('Sí',
              style: TextStyle(
                color: theme.colorScheme.onError,
                fontFamily: 'KoHo',
                fontWeight: FontWeight.bold,
              )),
          onPressed: () {
            // TODO: función para eliminar elemento.

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
