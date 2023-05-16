import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

class UsuariosProvider with ChangeNotifier {
  String _token = '';

  List<Usuario> _usuarios = [];

  List<Usuario> get usuarios => _usuarios;
  String get token => _token;

  UsuariosProvider() {
    fetchUsuarios(token);
  }

  Future<void> fetchUsuarios(String token) async {
    final response = await http.get(
        Uri.parse('http://magarcia.asuscomm.com:25546/users'),
        headers: {'Authorization': 'Bearer $token'});

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _usuarios = results
          .map((json) => Usuario(
                uuid: json['uuid'],
                name: json['name'],
                username: json['username'],
                email: json['email'],
                password: json['password'],
                avatar: json['avatar'],
                userRole: List<String>.from(json['userRole']),
                credits: json['credits'],
                isActive: json['isActive'],
              ))
          .toList();
    } else {
      throw Exception('Failed to fetch usuarios');
    }
  }

  Future<Usuario> fetchUsuario(String uuid, String token) async {
    final response = await http.get(
        Uri.parse('http://magarcia.asuscomm.com:25546/users/$uuid'),
        headers: {'Authorization': 'Bearer $token'});

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return Usuario(
        uuid: data['uuid'],
        name: data['name'],
        username: data['username'],
        email: data['email'],
        password: data['password'],
        avatar: data['avatar'],
        userRole: List<String>.from(data['userRole']),
        credits: data['credits'],
        isActive: data['isActive'],
      );
    } else {
      throw Exception('Failed to fetch usuario');
    }
  }

  Future<String> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('http://magarcia.asuscomm.com:25546/login'),
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
      _token = data['token'];

      return data['token'];
    } else {
      throw Exception('Failed to login');
    }
  }

  Future<Usuario> register(Usuario usuario) async {
    final response = await http.post(
      Uri.parse('http://magarcia.asuscomm.com:25546/users'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(usuario),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _usuarios.add(Usuario(
        uuid: data['user']['uuid'],
        name: data['user']['name'],
        username: data['user']['username'],
        email: data['user']['email'],
        password: data['user']['password'],
        avatar: data['user']['avatar'],
        userRole: List<String>.from(data['userRole']),
        credits: data['user']['credits'],
        isActive: data['user']['isActive'],
        token: data['token'],
      ));
      notifyListeners();

      _token = data['token'];
      return usuario;
    } else {
      throw Exception('Failed to register');
    }
  }

  Future<void> updateUsuario(String uuid, String token) async {
    final response = await http.put(
      Uri.parse('http://magarcia.asuscomm.com:25546/users/$uuid'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _usuarios[_usuarios.indexWhere((element) => element.uuid == uuid)] =
          Usuario(
        uuid: data['uuid'],
        name: data['name'],
        username: data['username'],
        email: data['email'],
        password: data['password'],
        avatar: data['avatar'],
        userRole: List<String>.from(data['userRole']),
        credits: data['credits'],
        isActive: data['isActive'],
      );
      notifyListeners();
    } else {
      throw Exception('Failed to update usuario');
    }
  }

  Future<void> updateUsuarioActividad(
      String uuid, bool isActive, String token) async {
    final response = await http.put(
      Uri.parse(
          'http://magarcia.asuscomm.com:25546/users/active/$uuid/$isActive'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _usuarios.firstWhere((element) => element.uuid == uuid).isActive =
          data['isActive'];
      notifyListeners();
    } else {
      throw Exception('Failed to update usuario');
    }
  }

  Future<void> updateUsuarioCreditos(
      String uuid, String creditos, String token) async {
    final response = await http.put(
      Uri.parse(
          'http://magarcia.asuscomm.com:25546/users/credits/$uuid/$creditos'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _usuarios.firstWhere((element) => element.uuid == uuid).credits =
          data['credits'];
      notifyListeners();
    } else {
      throw Exception('Failed to update usuario');
    }
  }

  Future<void> updateMe(String token) async {
    final response = await http.put(
      Uri.parse('http://magarcia.asuscomm.com:25546/users/me'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).name =
          data['name'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).username =
          data['username'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).email =
          data['email'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).password =
          data['password'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).avatar =
          data['avatar'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).userRole =
          List<String>.from(data['userRole']);
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).credits =
          data['credits'];
      _usuarios.firstWhere((element) => element.uuid == data['uuid']).isActive =
          data['isActive'];
      notifyListeners();
    } else {
      throw Exception('Failed to update usuario');
    }
  }

  Future<void> deleteUsuarios(String uuid, String token) async {
    final response = await http.delete(
      Uri.parse('http://magarcia.asuscomm.com:25546/users/$uuid'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      _usuarios.removeWhere((element) => element.uuid == uuid);
      notifyListeners();
    } else {
      throw Exception('Failed to delete usuario');
    }
  }
}
