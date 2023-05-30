import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:provider/provider.dart';

import '../models/usuario.dart';

class MyDeleteAlert extends StatelessWidget {
  final String title;
  final String ruta;
  final dynamic elemento;

  const MyDeleteAlert({
    Key? key,
    required this.title,
    required this.ruta,
    required this.elemento,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    final espaciosProvider =
        Provider.of<EspaciosProvider>(context, listen: false);
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);
    final usuariosProvider =
        Provider.of<UsuariosProvider>(context, listen: false);
    final usuario = authProvider.usuario;

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
                    return const MyErrorMessageDialog(
                      title: 'Error',
                      description:
                          'Ha ocurrido un error al eliminar el espacio.',
                    );
                  },
                );
              });
            } else if (elemento is Reserva) {
              reservasProvider
                  .deleteReserva(elemento.uuid, usuario.uuid!)
                  .then((result) {
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
                    return const MyErrorMessageDialog(
                      title: 'Error',
                      description:
                          'Ha ocurrido un error al eliminar la reserva.',
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
                    return const MyErrorMessageDialog(
                      title: 'Error',
                      description:
                          'Ha ocurrido un error al eliminar el usuario.',
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