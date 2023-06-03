import 'package:flutter/material.dart';
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
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    return DefaultTabController(
      length: 3,
      child: Scaffold(
        resizeToAvoidBottomInset: true,
        appBar: AppBar(
          toolbarHeight: 100,
          backgroundColor: theme.colorScheme.primary.withOpacity(0.3),
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
            labelColor: theme.colorScheme.surface,
            unselectedLabelColor: theme.colorScheme.surface,
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
            indicator: UnderlineTabIndicator(
              borderSide: BorderSide(
                color: theme.colorScheme.secondary,
                width: 4.0,
              ),
            ),
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.logout_rounded),
              color: theme.colorScheme.surface,
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
        body: Column(
          children: [
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
