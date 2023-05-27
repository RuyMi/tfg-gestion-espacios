import 'package:flutter/material.dart';

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
    var theme = Theme.of(context);

    return BottomNavigationBar(
      items: const <BottomNavigationBarItem>[
        BottomNavigationBarItem(
            icon: Icon(Icons.home_outlined), label: 'Inicio'),
        BottomNavigationBarItem(icon: Icon(Icons.list_rounded), label: 'Espacios'),
        BottomNavigationBarItem(
            icon: Icon(Icons.notification_add_outlined),
            label: 'Tablón de anuncios'),
        BottomNavigationBarItem(
            icon: Icon(Icons.person_outline), label: 'Perfil'),
      ],
      currentIndex: selectedIndex,
      selectedIconTheme: IconThemeData(
        size: 30,
        color: theme.colorScheme.onBackground,
      ),
      unselectedItemColor: theme.colorScheme.onSurface,
      showSelectedLabels: false,
      showUnselectedLabels: false,
      iconSize: 25,
      onTap: onItemTapped,
    );
  }
}
