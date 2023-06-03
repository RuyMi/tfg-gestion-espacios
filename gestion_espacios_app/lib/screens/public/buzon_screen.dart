import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/widgets/acercade_widget.dart';

class BuzonScreen extends StatefulWidget {
  const BuzonScreen({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _BuzonScreenState createState() => _BuzonScreenState();
}

class _BuzonScreenState extends State<BuzonScreen> {
  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    return Scaffold(
      resizeToAvoidBottomInset: true,
      appBar: AppBar(
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: const Text('Tablón de anuncios'),
        titleTextStyle: TextStyle(
          fontFamily: 'KoHo',
          color: theme.colorScheme.surface,
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
        backgroundColor: theme.colorScheme.background,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
             Icon(
              Icons.developer_mode,
              size: 100,
              color: theme.colorScheme.onBackground,
            ),
            const SizedBox(height: 20),
            Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: theme.colorScheme.background,
                borderRadius: BorderRadius.circular(20),
                border: Border.all(
                  color: theme.colorScheme.onBackground,
                  width: 2,
                ),
              ),
              child: const Text(
                'En desarrollo',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  fontFamily: 'KoHo',
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
