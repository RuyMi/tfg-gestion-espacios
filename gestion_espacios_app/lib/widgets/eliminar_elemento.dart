/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

import '../models/usuario.dart';

/// Widget que muestra un alert dialog para confirmar la eliminación de un elemento.
class MyDeleteAlert extends StatelessWidget {
  /// El título del alert dialog.
  final String title;

  /// La ruta a la que se redirige al eliminar el elemento.
  final String ruta;

  /// El elemento a eliminar.
  final dynamic elemento;

  const MyDeleteAlert({
    Key? key,
    required this.title,
    required this.ruta,
    required this.elemento,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    /// Se obtienen los providers necesarios.
    var theme = Theme.of(context);
    final espaciosProvider =
        Provider.of<EspaciosProvider>(context, listen: false);
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);
    final usuariosProvider =
        Provider.of<UsuariosProvider>(context, listen: false);

    return AlertDialog(
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(20)),
      ),
      backgroundColor: theme.colorScheme.error,
      title: Column(
        children: [
          Icon(
            Icons.info_rounded,
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
            if (elemento is Espacio) {
              espaciosProvider.deleteEspacio(elemento.uuid).then((result) {
                Navigator.pushNamed(context, ruta);
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const MyMessageDialog(
                      title: 'Espacio eliminado',
                      description: 'El espacio se ha eliminado correctamente.',
                    );
                  },
                );
              }).catchError((error) {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return MyErrorMessageDialog(
                      title: 'Error al eliminar el espacio',
                      description: error
                          .toString()
                          .substring(error.toString().indexOf(':') + 1),
                    );
                  },
                );
              });
            } else if (elemento is Reserva) {
              reservasProvider.deleteReserva(elemento.uuid).then((result) {
                Navigator.pushNamed(context, ruta);
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const MyMessageDialog(
                      title: 'Reserva eliminada',
                      description: 'La reserva se ha eliminado correctamente.',
                    );
                  },
                );
              }).catchError((error) {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return MyErrorMessageDialog(
                      title: 'Error al eliminar la reserva',
                      description: error
                          .toString()
                          .substring(error.toString().indexOf(':') + 1),
                    );
                  },
                );
              });
            } else if (elemento is Usuario) {
              usuariosProvider.deleteUsuario(elemento.uuid).then((result) {
                Navigator.pushNamed(context, ruta);
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const MyMessageDialog(
                      title: 'Usuario eliminado',
                      description: 'El usuario se ha eliminado correctamente.',
                    );
                  },
                );
              }).catchError((error) {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return MyErrorMessageDialog(
                      title: 'Error al eliminar el usuario',
                      description: error
                          .toString()
                          .substring(error.toString().indexOf(':') + 1),
                    );
                  },
                );
              });
            }
          },
        ),
      ],
    );
  }
}
