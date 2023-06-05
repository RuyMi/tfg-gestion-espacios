import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

class AuthProvider with ChangeNotifier {
  String _token = '';
  String _userId = '';

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

  Usuario get usuario => _usuario;
  String get token => _token;
  String get userId => _userId;

  String baseUrl = 'http://app.iesluisvives.org:1212';

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