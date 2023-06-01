import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/screens/private/bo_update_reserva_dialog.dart';
import 'package:gestion_espacios_app/widgets/image_widget.dart';
import 'package:provider/provider.dart';

class ReservasBOScreen extends StatefulWidget {
  const ReservasBOScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _ReservasBOScreen createState() => _ReservasBOScreen();
}

class _ReservasBOScreen extends State<ReservasBOScreen> {
  @override
  void initState() {
    super.initState();
    final reservasProvider =
        Provider.of<ReservasProvider>(context, listen: false);
    reservasProvider.fetchReservas();
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final reservasProvider = Provider.of<ReservasProvider>(context);
    final reservas = reservasProvider.reservas;

    if (reservas.isEmpty) {
      return const Center(
        child: CircularProgressIndicator(),
      );
    } else {
      return StaggeredGridView.countBuilder(
        padding: const EdgeInsets.all(10),
        crossAxisCount: 5,
        itemCount: reservas.length,
        staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
        mainAxisSpacing: 10,
        crossAxisSpacing: 10,
        itemBuilder: (BuildContext context, int index) {
          final reserva = reservas[index];
          return InkWell(
            onTap: () {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return EditarReservaBODialog(reserva: reserva);
                },
              );
            },
            child: Card(
              color: theme.colorScheme.onBackground,
              child: Container(
                constraints: const BoxConstraints(
                  maxHeight: 200,
                  minHeight: 150,
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(20),
                        child: MyImageWidget(image: reserva.image),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text(
                            reserva.spaceName,
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontFamily: 'KoHo',
                              color: theme.colorScheme.onPrimary,
                            ),
                          ),
                          Text(reserva.userName,
                              textAlign: TextAlign.center,
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary,
                              )),
                          const SizedBox(height: 5),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(reserva.status!,
                                  style: TextStyle(
                                      fontFamily: 'KoHo',
                                      fontWeight: FontWeight.bold,
                                      color: theme.colorScheme.secondary)),
                              const SizedBox(width: 5),
                              Icon(
                                Icons.info_outline_rounded,
                                color: theme.colorScheme.secondary,
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      );
    }
  }
}
