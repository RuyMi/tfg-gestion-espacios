import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 1;
  bool _isDarkMode = false;
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  static final List<Widget> _widgetOptions = <Widget>[
    const Text(
      'Index 0: Home',
    ),
    const Text(
      'Index 1: Espacios',
    ),
    const Text(
      'Index 2: Tablón de anuncios',
    ),
    Container(color: MyColors.whiteApp)
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
      appBar: AppBar(
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: const Text('IES Luis Vives'),
        titleTextStyle: const TextStyle(
          fontFamily: 'KoHo',
          color: MyColors.blackApp,
          fontWeight: FontWeight.bold,
          fontSize: 25,
        ),
        leading: IconButton(
          onPressed: () {
            showDialog(
                context: context,
                builder: (BuildContext context) {
                  return const AcercaDeWidget();
                });
          },
          icon: Image.asset('assets/images/logo.png'),
          iconSize: 25,
        ),
        actions: [
          IconButton(
            onPressed: () {},
            icon: const Icon(Icons.search),
            color: MyColors.blackApp,
            iconSize: 25,
          ),
        ],
        backgroundColor: MyColors.whiteApp,
      ),
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
