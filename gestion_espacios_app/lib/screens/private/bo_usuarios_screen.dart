import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:gestion_espacios_app/models/colors.dart';

final List<Map<String, dynamic>> items = [
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Lolo Lolez de Lolitolandia',
    'username': '@lolo_lolez'
  },
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Simon Juan III de Borbón',
    'username': '@simon_simonez'
  },
];

class UsuariosBOScreen extends StatelessWidget {
  const UsuariosBOScreen({super.key});

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
                    borderRadius: BorderRadius.circular(50),
                    border: Border.all(
                      color: MyColors.blackApp,
                      width: 2,
                    ),
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(50),
                    child: Image.asset('assets/images/profile_pic.png',
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
                          overflow: TextOverflow.ellipsis,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                          color: MyColors.whiteApp,
                        ),
                      ),
                      Text(item['username'],
                          style: const TextStyle(
                            overflow: TextOverflow.ellipsis,
                            fontStyle: FontStyle.italic,
                            fontFamily: 'KoHo',
                            color: MyColors.whiteApp,
                          )),
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
