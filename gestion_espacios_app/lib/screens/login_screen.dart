import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        margin: const EdgeInsets.all(20),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset('assets/images/logo.png'),
            const SizedBox(height: 50),
            TextField(
              decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                labelText: 'Nombre de usuario',
                labelStyle: const TextStyle(fontFamily: 'KoHo'),
                prefixIcon: const Icon(Icons.person),
              ),
            ),
            const SizedBox(height: 10),
            TextField(
              obscureText: true,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                labelText: 'Contraseña',
                labelStyle: const TextStyle(fontFamily: 'KoHo'),
                prefixIcon: const Icon(Icons.lock),
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.pushNamed(context, '/home');
              },
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                backgroundColor: MyColors.pinkApp,
              ),
              child: const Text('Validar',
                  style:
                      TextStyle(color: MyColors.whiteApp, fontFamily: 'KoHo')),
            ),
            const SizedBox(height: 10),
            TextButton(
              onPressed: () {},
              child: const Text(
                '¿Has olvidado tu contraseña?',
                style: TextStyle(
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
