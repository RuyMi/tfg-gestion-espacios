import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:provider/provider.dart';

class EspaciosBOScreen extends StatelessWidget {
  const EspaciosBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final espaciosProvider = Provider.of<EspaciosProvider>(context);
    final espacios = espaciosProvider.espacios;

    if (espacios.isEmpty) {
      return const Center(
        child: CircularProgressIndicator(),
      );
    } else {
      return StaggeredGridView.countBuilder(
        padding: const EdgeInsets.all(10),
        crossAxisCount: 5,
        itemCount: espacios.length,
        staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
        mainAxisSpacing: 10,
        crossAxisSpacing: 10,
        itemBuilder: (BuildContext context, int index) {
          final espacio = espacios[index];
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
                      child: Image.asset('assets/images/sala_stock.jpg',
                          width: 100, height: 100, fit: BoxFit.cover),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Text(
                          espacio.name,
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                            color: MyColors.whiteApp,
                          ),
                        ),
                        Text(espacio.authorizedRoles.toString(),
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
                            Text(espacio.price.toString(),
                                style: const TextStyle(
                                    fontFamily: 'KoHo',
                                    fontWeight: FontWeight.bold,
                                    color: MyColors.pinkApp)),
                            const Icon(
                              Icons.monetization_on_outlined,
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
