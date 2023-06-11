/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';

/// Widget que muestra la barra de navegación inferior.
class MyBottomNavigationBar extends StatelessWidget {
  const MyBottomNavigationBar({
    Key? key,
    required this.selectedIndex,
    required this.onItemTapped,
  }) : super(key: key);

  /// El índice del elemento seleccionado.
  final int selectedIndex;

  /// La función que se ejecuta cuando se pulsa un elemento.
  final void Function(int) onItemTapped;

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    return BottomNavigationBar(
      items: const <BottomNavigationBarItem>[
        BottomNavigationBarItem(
            icon: Icon(Icons.home_outlined), label: 'Inicio'),
        BottomNavigationBarItem(
            icon: Icon(Icons.event_note_rounded), label: 'Espacios'),
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
