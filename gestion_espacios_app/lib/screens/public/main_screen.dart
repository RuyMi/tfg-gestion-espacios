/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/screens/public/buzon_screen.dart';
import 'package:gestion_espacios_app/screens/public/espacios_screen.dart';
import 'package:gestion_espacios_app/screens/public/inicio_screen.dart';
import 'package:gestion_espacios_app/screens/public/perfil_screen.dart';
import 'package:gestion_espacios_app/widgets/bottomnav_widget.dart';
import 'package:gestion_espacios_app/widgets/drawer_widget.dart';

/// Pantalla principal de la aplicación.
class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _MainScreenState createState() => _MainScreenState();
}

/// Clase que muestra la pantalla principal de la aplicación.
class _MainScreenState extends State<MainScreen> {
  /// Índice de la pantalla actual.
  int _selectedIndex = 0;

  /// Clave para el Scaffold.
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  /// Lista de widgets que se muestran en la parte inferior de la pantalla.
  static final List<Widget> _widgetOptions = <Widget>[
    const InicioScreen(),
    const EspaciosScreen(),
    const BuzonScreen(),
    const PerfilScreen()
  ];

  /// Función que se ejecuta cuando se pulsa un elemento de la parte inferior de la pantalla.
  void _onItemTapped(int index) {
    if (index == 3) {
      _scaffoldKey.currentState?.openDrawer();
    } else {
      setState(() {
        _selectedIndex = index;
      });
    }

    setState(() {
      _selectedIndex = index;
    });
    if (index == 3) {
      _scaffoldKey.currentState?.openDrawer();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: true,
      key: _scaffoldKey,
      drawer: const MyDrawer(),
      body: Center(
        child: _widgetOptions.elementAt(_selectedIndex),
      ),
      bottomNavigationBar: MyBottomNavigationBar(
        selectedIndex: _selectedIndex,
        onItemTapped: _onItemTapped,
      ),
    );
  }
}
