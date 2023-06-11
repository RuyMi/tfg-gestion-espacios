/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

/// Widget que muestra la información de contacto de la aplicación.
class AcercaDeWidget extends StatelessWidget {
  const AcercaDeWidget({super.key});

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    return AlertDialog(
      contentPadding: const EdgeInsets.all(16),
      backgroundColor: theme.colorScheme.onBackground,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20),
      ),
      content: SizedBox(
        width: double.maxFinite,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 100,
              height: 100,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: theme.colorScheme.onSecondary,
              ),
              padding: const EdgeInsets.all(4),
              child: ClipOval(
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 5),
                  child: Image.asset(
                    'assets/images/logo.png',
                    width: 100,
                    height: 100,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.location_on_rounded, color: theme.colorScheme.onSecondary, size: 24),
                const SizedBox(width: 8),
                Text('P.º de la Ermita, 15, \n28918 Leganés, Madrid',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: theme.colorScheme.onSecondary,
                      fontFamily: 'KoHo',
                    )),
              ],
            ),
            const SizedBox(height: 8),
             Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.phone_rounded, color: theme.colorScheme.onSecondary, size: 24),
                const SizedBox(width: 8),
                Text('916 80 77 12',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: theme.colorScheme.onSecondary,
                      fontFamily: 'KoHo',
                    )),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.language_rounded, color: theme.colorScheme.onSecondary, size: 24),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () async {
                    final Uri url = Uri.parse('https://www.iesluisvives.es/');
                    if (await canLaunchUrl(url)) {
                      await launchUrl(url);
                    }
                  },
                  child: Text(
                    'Página Web',
                    style: TextStyle(
                      color: theme.colorScheme.onSecondary,
                      decoration: TextDecoration.none,
                      fontFamily: 'KoHo',
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.people_rounded, color: theme.colorScheme.onSecondary, size: 24),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () async {
                    final Uri url =
                        Uri.parse('https://twitter.com/ies_luisvives');
                    if (await canLaunchUrl(url)) {
                      await launchUrl(url);
                    }
                  },
                  child: Text(
                    'Twitter',
                    style: TextStyle(
                      color: theme.colorScheme.onSecondary,
                      decoration: TextDecoration.none,
                      fontFamily: 'KoHo',
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
