import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:url_launcher/url_launcher.dart';

class AcercaDeWidget extends StatelessWidget {
  const AcercaDeWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      contentPadding: const EdgeInsets.all(16),
      backgroundColor: MyColors.lightBlueApp,
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
              decoration: const BoxDecoration(
                shape: BoxShape.circle,
                color: MyColors.whiteApp,
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
            const Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.location_on, color: MyColors.whiteApp, size: 24),
                SizedBox(width: 8),
                Text('P.º de la Ermita, 15, \n28918 Leganés, Madrid',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: MyColors.whiteApp,
                      fontFamily: 'KoHo',
                    )),
              ],
            ),
            const SizedBox(height: 8),
            const Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.phone, color: MyColors.whiteApp, size: 24),
                SizedBox(width: 8),
                Text('916 80 77 12',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: MyColors.whiteApp,
                      fontFamily: 'KoHo',
                    )),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(Icons.language, color: MyColors.whiteApp, size: 24),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () async {
                    final Uri url = Uri.parse('https://www.iesluisvives.es/');
                    if (await canLaunchUrl(url)) {
                      await launchUrl(url);
                    }
                  },
                  child: const Text(
                    'https://www.iesluisvives.es/',
                    style: TextStyle(
                      color: MyColors.whiteApp,
                      decoration: TextDecoration.underline,
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
                const Icon(Icons.people, color: MyColors.whiteApp, size: 24),
                const SizedBox(width: 8),
                GestureDetector(
                  onTap: () async {
                    final Uri url =
                        Uri.parse('https://twitter.com/ies_luisvives');
                    if (await canLaunchUrl(url)) {
                      await launchUrl(url);
                    }
                  },
                  child: const Text(
                    '@ies_luisvives',
                    style: TextStyle(
                      color: MyColors.whiteApp,
                      decoration: TextDecoration.underline,
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
