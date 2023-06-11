/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:provider/provider.dart';

/// Widget que muestra un diálogo de confirmación para cerrar sesión.
class MyLogoutAlert extends StatelessWidget {
  /// La ruta a la que se redirige al cerrar sesión.
  final String ruta;
  const MyLogoutAlert({Key? key, required this.ruta}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el provider de autenticación.
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    return AlertDialog(
      backgroundColor: theme.colorScheme.error,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      title: Column(
        children: [
          Icon(
            Icons.info_rounded,
            size: 60,
            color: theme.colorScheme.onError,
          ),
          const SizedBox(height: 10),
          Text('¿Desea cerrar sesión?',
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
            authProvider.logout();

            Navigator.of(context)
                .pushNamedAndRemoveUntil(ruta, (Route<dynamic> route) => false);
          },
        ),
      ],
    );
  }
}
