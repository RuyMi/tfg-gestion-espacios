import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

class AuthProvider with ChangeNotifier {
  String _token = '';

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

  bool loginSucceed = false;
  Usuario get usuario => _usuario;
  String get token => _token;

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
    loginSucceed = false;
    notifyListeners();
  }

  Future<Usuario?> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('http://magarcia.asuscomm.com:25546/users/login'),
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

      loginSucceed = true;

      _usuario = usuario;
      _token = data['token'];
      notifyListeners();

      return usuario;
    }

    return null;
  }
}
