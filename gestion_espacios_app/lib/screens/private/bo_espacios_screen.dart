import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/colors.dart';

final List<Map<String, dynamic>> items = [
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Sala 1',
    'description':
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    'price': '10',
  },
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Sala 2',
    'description': 'Descripción de la sala 2',
    'price': '19',
  }
];

class EspaciosBOScreen extends StatelessWidget {
  const EspaciosBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return StaggeredGridView.countBuilder(
      padding: const EdgeInsets.all(10),
      crossAxisCount: 5,
      itemCount: items.length,
      staggeredTileBuilder: (int index) => const StaggeredTile.fit(1),
      mainAxisSpacing: 10,
      crossAxisSpacing: 10,
      itemBuilder: (BuildContext context, int index) {
        final item = items[index];
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
                        item['name'],
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                          color: MyColors.whiteApp,
                        ),
                      ),
                      Text(item['description'],
                          textAlign: TextAlign.center,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(
                            fontFamily: 'KoHo',
                            color: MyColors.whiteApp,
                          )),
                      const SizedBox(height: 5),
                      const Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text('50',
                              style: TextStyle(
                                  fontFamily: 'KoHo',
                                  fontWeight: FontWeight.bold,
                                  color: MyColors.pinkApp)),
                          Icon(
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
