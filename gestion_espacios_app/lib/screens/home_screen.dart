import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 1;

  static const List<Widget> _widgetOptions = <Widget>[
    Text(
      'Index 0: Home',
    ),
    Text(
      'Index 1: Espacios',
    ),
    Text(
      'Index 2: Tablón de anuncios',
    ),
    Text(
      'Index 3: Perfil',
    ),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  // ignore: library_private_types_in_public_api
  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
          onPressed: () {},
          hoverColor: Colors.transparent,
          icon: Image.asset('assets/images/logo.png'),
          color: MyColors.blackApp,
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
