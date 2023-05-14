import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/providers/theme_provider.dart';
import 'package:provider/provider.dart';

class MyDrawer extends StatelessWidget {
  const MyDrawer({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Drawer(
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
                style: TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/perfil');
            },
          ),
          ListTile(
            leading: const Icon(Icons.bookmark_added),
            iconColor: MyColors.whiteApp,
            title: const Text('Mis reservas',
                style: TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/reservas');
            },
          ),
          ListTile(
            leading: const Icon(Icons.settings),
            iconColor: MyColors.whiteApp,
            title: const Text('Ajustes',
                style: TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp)),
            onTap: () {},
          ),
          const Divider(),
          ListTile(
            iconColor: MyColors.whiteApp,
            leading: Provider.of<ThemeNotifier>(context).isDarkMode
                ? const Icon(Icons.wb_sunny)
                : const Icon(Icons.nightlight_round),
            title: Provider.of<ThemeNotifier>(context).isDarkMode
                ? const Text('Cambiar a tema claro',
                    style:
                        TextStyle(fontFamily: 'KoHo', color: MyColors.whiteApp))
                : const Text('Cambiar a tema oscuro',
                    style: TextStyle(
                        fontFamily: 'KoHo', color: MyColors.whiteApp)),
            onTap: () {
              Provider.of<ThemeNotifier>(context, listen: false).toggleTheme();
            },
          ),
        ],
      ),
    );
  }
}
