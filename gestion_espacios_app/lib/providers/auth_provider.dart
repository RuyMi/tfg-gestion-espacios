/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

/// Clase que gestiona la autenticación de los usuarios.
class AuthProvider with ChangeNotifier {
  /// Token de autenticación.
  String _token = '';

  /// Id del usuario.
  String _userId = '';

  /// Usuario.
  Usuario _usuario = Usuario(
    uuid: '',
    name: '',
    username: '',
    email: '',
    password: '',
    avatar: '',
    userRole: [],
    credits: 0,
    isActive: false,
  );

  /// Getter del usuario.
  Usuario get usuario => _usuario;

  /// Getter del token.
  String get token => _token;

  /// Getter del id del usuario.
  String get userId => _userId;

  /// Url base de la API.
  String baseUrl = 'http://app.iesluisvives.org:1212';

  /// Función que cierra la sesión del usuario.
  void logout() {
    _token = '';
    _usuario = Usuario(
      uuid: '',
      name: '',
      username: '',
      email: '',
      password: '',
      avatar: '',
      userRole: [],
      credits: 0,
      isActive: false,
    );
    notifyListeners();
  }

  /// Función que inicia la sesión del usuario.
  Future<Usuario?> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/users/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(
        {
          'username': username,
          'password': password,
        },
      ),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      Usuario usuario = Usuario(
        uuid: data['user']['uuid'],
        name: data['user']['name'],
        username: data['user']['username'],
        email: data['user']['email'],
        password: data['user']['password'],
        avatar: data['user']['avatar'],
        userRole: List<String>.from(data['user']['userRole']),
        credits: data['user']['credits'],
        isActive: data['user']['isActive'],
      );

      _usuario = usuario;
      _token = data['token'];
      _userId = data['user']['uuid'];
      notifyListeners();

      return usuario;
    } else {
      notifyListeners();
      throw Exception(response.body);
    }
  }
}
