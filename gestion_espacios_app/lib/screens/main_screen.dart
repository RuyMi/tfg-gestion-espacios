import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/buzon_screen.dart';
import 'package:gestion_espacios_app/screens/espacios_screen.dart';
import 'package:gestion_espacios_app/screens/inicio_screen.dart';
import 'package:gestion_espacios_app/screens/perfil_screen.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  int _selectedIndex = 1;
  bool _isDarkMode = false;
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  static final List<Widget> _widgetOptions = <Widget>[
    const InicioScreen(),
    const EspaciosScreen(),
    const BuzonScreen(),
    const PerfilScreen()
  ];

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

  // ignore: library_private_types_in_public_api
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldKey,
      drawer: Drawer(
        backgroundColor: MyColors.lightBlueApp,
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            const UserAccountsDrawerHeader(
              decoration: BoxDecoration(
                color: MyColors.lightBlueApp,
              ),
              accountName: Text('Nombre',
                  style: TextStyle(
                      fontFamily: 'KoHo',
                      color: MyColors.whiteApp,
                      fontWeight: FontWeight.bold)),
              accountEmail: Text('nombre_usuario',
                  style: TextStyle(
                    fontFamily: 'KoHo',
                    color: MyColors.whiteApp,
                  )),
              currentAccountPicture: CircleAvatar(
                backgroundImage: AssetImage('assets/images/logo.png'),
                backgroundColor: MyColors.whiteApp,
              ),
            ),
            ListTile(
              leading: const Icon(Icons.person),
              iconColor: MyColors.whiteApp,
              title: const Text('Perfil',
                  style:
                      TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
              onTap: () {},
            ),
            ListTile(
              leading: const Icon(Icons.bookmark_added),
              iconColor: MyColors.whiteApp,
              title: const Text('Mis reservas',
                  style:
                      TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
              onTap: () {},
            ),
            ListTile(
              leading: const Icon(Icons.settings),
              iconColor: MyColors.whiteApp,
              title: const Text('Ajustes',
                  style:
                      TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
              onTap: () {},
            ),
            const Divider(),
            ListTile(
              iconColor: MyColors.whiteApp,
              leading: _isDarkMode
                  ? const Icon(Icons.wb_sunny)
                  : const Icon(Icons.nightlight_round),
              title: _isDarkMode
                  ? const Text('Cambiar a tema claro',
                      style: TextStyle(
                          fontFamily: 'KoHo', color: MyColors.whiteApp))
                  : const Text('Cambiar a tema oscuro',
                      style: TextStyle(
                          fontFamily: 'KoHo', color: MyColors.whiteApp)),
              onTap: () {
                setState(() {
                  _isDarkMode = !_isDarkMode;
                  if (_isDarkMode) {
                  } else {}
                });
              },
            ),
          ],
        ),
      ),
      body: Center(
        child: _widgetOptions.elementAt(_selectedIndex),
      ),
      bottomNavigationBar: BottomNavigationBar(
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
        currentIndex: _selectedIndex,
        selectedItemColor: MyColors.blackApp,
        selectedIconTheme: const IconThemeData(
          size: 30,
          color: MyColors.lightBlueApp,
        ),
        unselectedItemColor: MyColors.blackApp,
        showSelectedLabels: false,
        showUnselectedLabels: false,
        iconSize: 25,
        onTap: _onItemTapped,
      ),
    );
  }
}
