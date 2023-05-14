import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

final List<Map<String, dynamic>> items = [
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Lolo Lolez',
    'username': '@lolo_lolez'
  },
  {
    'image': 'https://via.placeholder.com/150',
    'name': 'Simon Simonez',
    'username': '@simon_simonez'
  },
];

class UsuariosBOScreen extends StatelessWidget {
  const UsuariosBOScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      padding: const EdgeInsets.all(10),
      itemCount: items.length,
      gridDelegate:
          const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 2),
      itemBuilder: (BuildContext context, int index) {
        final item = items[index];
        return Card(
          color: MyColors.lightBlueApp,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(50),
                  border: Border.all(
                    color: MyColors.darkBlueApp,
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
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo',
                        color: MyColors.whiteApp,
                      ),
                    ),
                    Text(item['username'],
                        style: const TextStyle(
                          fontStyle: FontStyle.italic,
                          fontFamily: 'KoHo',
                          color: MyColors.whiteApp,
                        )),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}
