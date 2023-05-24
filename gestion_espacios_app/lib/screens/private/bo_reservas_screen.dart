import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:provider/provider.dart';

class ReservasBOScreen extends StatelessWidget {
  const ReservasBOScreen({super.key});

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
          return Card(
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
                      border: Border.all(
                        color: theme.colorScheme.surface,
                        width: 2,
                      ),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Image.asset(
                        'assets/images/image_placeholder.png',
                        width: 100,
                        height: 100,
                        fit: BoxFit.cover,
                      ),
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
                        Text('@${reserva.userName}',
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
                              Icons.timelapse_rounded,
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
          );
        },
      );
    }
  }
}
