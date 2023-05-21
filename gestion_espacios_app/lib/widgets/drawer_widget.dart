import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/theme_provider.dart';
import 'package:provider/provider.dart';

class MyDrawer extends StatelessWidget {
  const MyDrawer({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    final usuario = authProvider.usuario;
    var themeProvider = context.watch<ThemeProvider>();
    var theme = Theme.of(context);

    return Drawer(
      backgroundColor: theme.colorScheme.onBackground,
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          UserAccountsDrawerHeader(
            decoration: BoxDecoration(
              color: theme.colorScheme.onBackground,
            ),
            accountName: Text(usuario.name,
                style: TextStyle(
                    fontFamily: 'KoHo',
                    color: theme.colorScheme.onPrimary,
                    fontWeight: FontWeight.bold)),
            accountEmail: Text('@${usuario.username}',
                style: TextStyle(
                  fontStyle: FontStyle.italic,
                  fontFamily: 'KoHo',
                  color: theme.colorScheme.onPrimary,
                )),
            currentAccountPicture: CircleAvatar(
              backgroundImage:
                  const AssetImage('assets/images/profile_pic.png'),
              backgroundColor: theme.colorScheme.onPrimary,
            ),
          ),
          Divider(
            color: theme.colorScheme.onPrimary,
          ),
          ListTile(
            leading: const Icon(Icons.person),
            iconColor: theme.colorScheme.onPrimary,
            title: Text('Perfil',
                style: TextStyle(
                    fontFamily: 'KoHo', color: theme.colorScheme.onPrimary)),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/perfil');
            },
          ),
          ListTile(
            leading: const Icon(Icons.bookmark_added),
            iconColor: theme.colorScheme.onPrimary,
            title: Text('Mis reservas',
                style: TextStyle(
                    fontFamily: 'KoHo', color: theme.colorScheme.onPrimary)),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/mis-reservas');
            },
          ),
          ListTile(
            leading: const Icon(Icons.settings),
            iconColor: theme.colorScheme.onPrimary,
            title: Text('Ajustes',
                style: TextStyle(
                    fontFamily: 'KoHo', color: theme.colorScheme.onPrimary)),
            onTap: () {},
          ),
          Divider(
            color: theme.colorScheme.onPrimary,
          ),
          ListTile(
            iconColor: theme.colorScheme.onPrimary,
            leading: themeProvider.isDarkMode
                ? const Icon(Icons.wb_sunny)
                : const Icon(Icons.nightlight_round),
            title: themeProvider.isDarkMode
                ? Text('Cambiar a tema claro',
                    style: TextStyle(
                        fontFamily: 'KoHo', color: theme.colorScheme.onPrimary))
                : Text('Cambiar a tema oscuro',
                    style: TextStyle(
                        fontFamily: 'KoHo',
                        color: theme.colorScheme.onPrimary)),
            onTap: () {
              themeProvider.toggleTheme();
            },
          ),
        ],
      ),
    );
  }
}
