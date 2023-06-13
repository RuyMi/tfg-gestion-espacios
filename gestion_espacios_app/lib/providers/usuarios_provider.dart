/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:http/http.dart' as http;

/// Clase que gestiona la autenticación de los usuarios.
class UsuariosProvider with ChangeNotifier {
  /// Token de autenticación.
  final String? _token;

  /// Lista de usuarios.
  List<Usuario> _usuarios = [];

  /// Usuario actual.
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

  /// Getter de la lista de usuarios.
  List<Usuario> get usuarios => _usuarios;

  /// Getter del usuario actual.
  Usuario get actualUsuario => _actualUsuario;

  UsuariosProvider(this._token) {
    fetchUsuarios();
    fetchActualUsuario();
  }

  /// Url base de la API.
  String baseUrl = 'http://app.iesluisvives.org:1212';

  /// Función que obtiene los usuarios.
  Future<void> fetchUsuarios() async {
    final response = await http.get(Uri.parse('$baseUrl/users'),
        headers: {'Authorization': 'Bearer $_token'});

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _usuarios = results
          .map((usuario) => Usuario(
                uuid: usuario['uuid'],
                name: usuario['name'],
                username: usuario['username'],
                email: usuario['email'],
                password: usuario['password'],
                avatar: usuario['avatar'],
                userRole: List<String>.from(usuario['userRole']),
                credits: usuario['credits'],
                isActive: usuario['isActive'],
              ))
          .toList();

      notifyListeners();
    } else {
      _usuarios = [];
      notifyListeners();
    }
  }

  /// Función que obtiene el usuario actual.
  Future<Usuario?> fetchActualUsuario() async {
    final response = await http.get(Uri.parse('$baseUrl/users/me'),
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
  }

  /// Función que obtiene un usuario por su uuid.
  Future<Usuario?> fetchUsuario(String uuid) async {
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
  }

  /// Función que registra un usuario.
  Future<void> register(Usuario usuario) async {
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
          userRole: List<String>.from(data['user']['userRole']),
          credits: data['user']['credits'],
          isActive: data['user']['isActive'],
          token: data['token']));

      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }

  /// Función que registra un usuario siendo administrador.
  Future<void> registerPrivate(Usuario usuario) async {
    if (_actualUsuario.userRole.contains('ADMINISTRATOR')) {
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
            userRole: List<String>.from(data['user']['userRole']),
            credits: data['user']['credits'],
            isActive: data['user']['isActive'],
            token: data['token']));

        notifyListeners();
      } else {
        throw Exception(response.body);
      }
    } else {
      throw Exception('No tienes permisos para realizar esta acción.');
    }
  }

  /// Función que actualiza un usuario.
  Future<void> updateUsuario(String uuid, Usuario usuario) async {
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
      throw Exception(response.body);
    }
  }

  /// Función que actualiza la actividad de un usuario.
  Future<void> updateUsuarioActividad(String uuid, bool isActive) async {
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
      throw Exception(response.body);
    }
  }

  /// Función que actualiza los créditos de un usuario.
  Future<void> updateUsuarioCreditos(String uuid, String creditos) async {
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
      throw Exception(response.body);
    }
  }

  /// Función que actualiza a un usuario propio.
  Future<void> updateMe(Usuario usuario) async {
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
      throw Exception(response.body);
    }
  }

  /// Función que elimina un usuario.
  Future<void> deleteUsuario(String uuid) async {
    final response = await http.delete(
      Uri.parse('$baseUrl/users/$uuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 204) {
      _usuarios.removeWhere((element) => element.uuid == uuid);
      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }
}
