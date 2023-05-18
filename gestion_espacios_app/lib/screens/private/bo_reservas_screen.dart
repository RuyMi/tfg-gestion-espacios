import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:provider/provider.dart';

class ReservasBOScreen extends StatelessWidget {
  const ReservasBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final reservasProvider = Provider.of<ReservasProvider>(context);
    final reservas = reservasProvider.reservas;

    if (reservas.isEmpty) {
      return const Center(
        child: Text('No hay reservas'),
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
            color: MyColors.lightBlueApp,
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
                        color: MyColors.blackApp,
                        width: 2,
                      ),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Image.asset(
                        'assets/images/sala_stock.jpg',
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
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                            color: MyColors.whiteApp,
                          ),
                        ),
                        Text('@${reserva.userName}',
                            textAlign: TextAlign.center,
                            overflow: TextOverflow.ellipsis,
                            style: const TextStyle(
                              fontFamily: 'KoHo',
                              color: MyColors.whiteApp,
                            )),
                        const SizedBox(height: 5),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text(reserva.status!,
                                style: const TextStyle(
                                    fontFamily: 'KoHo',
                                    fontWeight: FontWeight.bold,
                                    color: MyColors.pinkApp)),
                            const SizedBox(width: 5),
                            const Icon(
                              Icons.timelapse_rounded,
                              color: MyColors.pinkApp,
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
