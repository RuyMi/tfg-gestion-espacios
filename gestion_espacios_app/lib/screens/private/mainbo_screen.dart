import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/private/espaciosbo_screen.dart';
import 'package:gestion_espacios_app/screens/private/reservasbo_screen.dart';
import 'package:gestion_espacios_app/screens/private/usuariosbo_screen.dart';

class BOMainScreen extends StatefulWidget {
  const BOMainScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _BOMainScreenState createState() => _BOMainScreenState();
}

class _BOMainScreenState extends State<BOMainScreen>
    with SingleTickerProviderStateMixin {
  final TextEditingController _searchController = TextEditingController();
  String _searchText = '';

  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          toolbarHeight: 100,
          backgroundColor: MyColors.lightBlueApp.shade100,
          automaticallyImplyLeading: false,
          title: Row(
            children: [
              Image.asset(
                'assets/images/logo.png',
                width: 100,
                height: 100,
              ),
              const SizedBox(width: 10),
              const Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'BackOffice',
                    style: TextStyle(
                      fontFamily: 'KoHo',
                      fontSize: 30,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    'IES Luis Vives',
                    style: TextStyle(
                      fontFamily: 'KoHo',
                      fontSize: 16,
                      fontWeight: FontWeight.normal,
                    ),
                  ),
                ],
              ),
            ],
          ),
          bottom: TabBar(
            controller: _tabController,
            tabs: const [
              Tab(text: 'Reservas'),
              Tab(text: 'Salas'),
              Tab(text: 'Usuarios'),
            ],
            labelColor: MyColors.blackApp,
            unselectedLabelColor: MyColors.blackApp,
            labelStyle: const TextStyle(
              fontFamily: 'KoHo',
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
            unselectedLabelStyle: const TextStyle(
              fontFamily: 'KoHo',
              fontSize: 16,
              fontWeight: FontWeight.normal,
            ),
            indicator: const UnderlineTabIndicator(
              borderSide: BorderSide(
                color: MyColors.pinkApp,
                width: 4.0,
              ),
            ),
          ),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _searchController,
                      decoration: InputDecoration(
                        filled: true,
                        fillColor: MyColors.pinkApp.shade100,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(30),
                          borderSide: BorderSide.none,
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(30),
                          borderSide: BorderSide.none,
                        ),
                        hintText: 'Buscar',
                        hintStyle: const TextStyle(
                            fontFamily: 'KoHo', color: MyColors.pinkApp),
                        prefixIcon: const Icon(Icons.search,
                            color: MyColors.pinkApp, size: 30),
                      ),
                      onChanged: (value) {
                        setState(() {
                          _searchText = value;
                        });
                      },
                    ),
                  ),
                  const SizedBox(width: 20),
                  ElevatedButton.icon(
                    onPressed: () {},
                    icon: const Icon(Icons.add, color: MyColors.whiteApp),
                    label: const Text(
                      'Nuevo',
                      style: TextStyle(
                        color: MyColors.whiteApp,
                        fontFamily: 'KoHo',
                        fontSize: 20,
                      ),
                    ),
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(30),
                      ),
                      backgroundColor: MyColors.pinkApp,
                    ),
                  ),
                ],
              ),
            ),
            Expanded(
              child: TabBarView(
                controller: _tabController,
                children: const [
                  ReservasBOScreen(),
                  EspaciosBOScreen(),
                  UsuariosBOScreen(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}