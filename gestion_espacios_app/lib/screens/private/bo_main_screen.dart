import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/screens/private/bo_espacios_screen.dart';
import 'package:gestion_espacios_app/screens/private/bo_reservas_screen.dart';
import 'package:gestion_espacios_app/screens/private/bo_usuarios_screen.dart';
import 'package:gestion_espacios_app/widgets/logout_widget.dart';

class BOMainScreen extends StatefulWidget {
  const BOMainScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _BOMainScreenState createState() => _BOMainScreenState();
}

class _BOMainScreenState extends State<BOMainScreen>
    with SingleTickerProviderStateMixin {
  final TextEditingController _searchController = TextEditingController();
  // String _searchText = '';
  bool _showNewButton = false;
  bool _sortByUsers = false;

  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
    _tabController.addListener(() {
      setState(() {
        if (_tabController.index == 0) {
          _showNewButton = false;
        } else {
          _showNewButton = true;
        }
      });
    });
  }

  void _handleSortBy(bool sortByUsers) {
    setState(() {
      if (sortByUsers) {
        //TODO: Ordenar por usuarios.
      } else {
        //TODO: Ordenar por reservas.
      }
    });
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
                      overflow: TextOverflow.ellipsis,
                      fontSize: 30,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    'IES Luis Vives',
                    style: TextStyle(
                      overflow: TextOverflow.ellipsis,
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
            indicatorSize: TabBarIndicatorSize.label,
            tabs: const [
              Tab(text: 'Reservas'),
              Tab(text: 'Salas'),
              Tab(text: 'Usuarios'),
            ],
            labelColor: MyColors.blackApp,
            unselectedLabelColor: MyColors.blackApp,
            labelPadding: const EdgeInsets.symmetric(horizontal: 16.0),
            labelStyle: const TextStyle(
              overflow: TextOverflow.ellipsis,
              fontFamily: 'KoHo',
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
            unselectedLabelStyle: const TextStyle(
              fontFamily: 'KoHo',
              overflow: TextOverflow.ellipsis,
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
          actions: [
            IconButton(
              icon: const Icon(Icons.logout),
              color: MyColors.blackApp,
              iconSize: 25,
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const MyLogoutAlert();
                  },
                );
              },
            ),
          ],
        ),
        body: SafeArea(
          child: Column(
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
                            fontFamily: 'KoHo',
                            color: MyColors.pinkApp,
                            overflow: TextOverflow.ellipsis,
                          ),
                          prefixIcon: const Icon(Icons.search,
                              color: MyColors.pinkApp, size: 30),
                        ),
                        onChanged: (value) {
                          setState(() {
                            // _searchText = value;
                          });
                        },
                      ),
                    ),
                    const SizedBox(width: 20),
                    Visibility(
                      visible: _showNewButton,
                      child: ElevatedButton.icon(
                        onPressed: () {},
                        icon: const Icon(Icons.add, color: MyColors.whiteApp),
                        label: const Text(
                          'Nuevo',
                          style: TextStyle(
                            color: MyColors.whiteApp,
                            overflow: TextOverflow.ellipsis,
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
                    ),
                    Visibility(
                      visible: _tabController.index == 0,
                      child: Row(
                        children: [
                          const Icon(Icons.person, color: MyColors.lightBlueApp),
                          Switch(
                            focusColor: MyColors.pinkApp,
                            activeColor: MyColors.pinkApp,
                            inactiveTrackColor: MyColors.lightBlueApp.shade100,
                            inactiveThumbColor: MyColors.lightBlueApp,
                            value: _sortByUsers,
                            onChanged: (value) {
                              setState(() {
                                _sortByUsers = value;
                                _handleSortBy(_sortByUsers);
                              });
                            },
                          ),
                          const Icon(Icons.calendar_today,
                              color: MyColors.pinkApp),
                        ],
                      ),
                    )
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
      ),
    );
  }
}
