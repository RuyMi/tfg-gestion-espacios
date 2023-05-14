import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class MyBottomNavigationBar extends StatelessWidget {
  const MyBottomNavigationBar({
    Key? key,
    required this.selectedIndex,
    required this.onItemTapped,
  }) : super(key: key);

  final int selectedIndex;
  final void Function(int) onItemTapped;

  @override
  Widget build(BuildContext context) {
    return BottomNavigationBar(
      items: const <BottomNavigationBarItem>[
        BottomNavigationBarItem(
            icon: Icon(Icons.home_outlined), label: 'Inicio'),
        BottomNavigationBarItem(icon: Icon(Icons.list), label: 'Espacios'),
        BottomNavigationBarItem(
            icon: Icon(Icons.notification_add_outlined),
            label: 'Tablón de anuncios'),
        BottomNavigationBarItem(
            icon: Icon(Icons.person_outline), label: 'Perfil'),
      ],
      currentIndex: selectedIndex,
      selectedItemColor: MyColors.blackApp,
      selectedIconTheme: const IconThemeData(
        size: 30,
        color: MyColors.lightBlueApp,
      ),
      unselectedItemColor: MyColors.blackApp,
      showSelectedLabels: false,
      showUnselectedLabels: false,
      iconSize: 25,
      onTap: onItemTapped,
    );
  }
}
