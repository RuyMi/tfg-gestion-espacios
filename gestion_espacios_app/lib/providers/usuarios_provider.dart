import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

class UsuariosProvider with ChangeNotifier {
  String? _token;
  final String? _userId;

  List<Usuario> _usuarios = [];
  Usuario _actualUsuario = Usuario(
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

  List<Usuario> get usuarios => _usuarios;
  Usuario get actualUsuario => _actualUsuario;

  UsuariosProvider(this._token, this._userId) {
    fetchUsuarios();
    fetchActualUsuario();
  }

  String baseUrl = 'http://magarcia.asuscomm.com:25546';

  Future<void> fetchUsuarios() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/users'),
          headers: {'Authorization': 'Bearer $_token'});

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

        notifyListeners();
      } else {
        _usuarios = [];
        notifyListeners();
      }
    } catch (e) {
      _usuarios = [];
      notifyListeners();
      throw Exception('Error al obtener los usuarios.');
    }
  }

  Future<Usuario?> fetchActualUsuario() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/users/$_userId'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _actualUsuario = Usuario(
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
        return _actualUsuario;
      } else {
        return null;
      }
    } catch (e) {
      throw Exception('Error al obtener el usuario.');
    }
  }

  Future<Usuario?> fetchUsuario(String uuid) async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/users/$uuid'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        var usuario = Usuario(
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
        return usuario;
      } else {
        return null;
      }
    } catch (e) {
      throw Exception('Error al obtener el usuario.');
    }
  }

  Future<void> register(Usuario usuario) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/users/register'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(usuario.toJson()),
      );

      if (response.statusCode == 201) {
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
        ));
        _token = data['token'];

        notifyListeners();
      } else {
        throw Exception('Error al registrar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al registrar el usuario.');
    }
  }

  Future<void> updateUsuario(String uuid, Usuario usuario) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/users/$uuid'),
        headers: {
          'Authorization': 'Bearer $_token',
          'Content-Type': 'application/json'
        },
        body: jsonEncode(usuario.toJson()),
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
        throw Exception('Error al actualizar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al actualizar el usuario.');
    }
  }

  Future<void> updateUsuarioActividad(String uuid, bool isActive) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/users/active/$uuid/$isActive'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _usuarios.firstWhere((element) => element.uuid == uuid).isActive =
            data['isActive'];
        notifyListeners();
      } else {
        throw Exception('Error al actualizar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al actualizar el usuario.');
    }
  }

  Future<void> updateUsuarioCreditos(String uuid, String creditos) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/users/credits/$uuid/$creditos'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _usuarios.firstWhere((element) => element.uuid == uuid).credits =
            data['credits'];
        notifyListeners();
      } else {
        throw Exception('Error al actualizar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al actualizar el usuario.');
    }
  }

  Future<void> updateMe(Usuario usuario) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/users/me'),
        headers: {
          'Authorization': 'Bearer $_token',
          'Content-Type': 'application/json'
        },
        body: jsonEncode(usuario.toJson()),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _usuarios[_usuarios
            .indexWhere((element) => element.uuid == usuario.uuid)] = Usuario(
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
        throw Exception('Error al actualizar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al actualizar el usuario.');
    }
  }

  Future<void> deleteUsuario(String uuid) async {
    try {
      final response = await http.delete(
        Uri.parse('$baseUrl/users/$uuid'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 204) {
        _usuarios.removeWhere((element) => element.uuid == uuid);
        notifyListeners();
      } else {
        throw Exception('Error al eliminar el usuario.');
      }
    } catch (e) {
      throw Exception('Error al eliminar el usuario.');
    }
  }
}
