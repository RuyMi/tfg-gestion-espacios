/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:http/http.dart' as http;

/// Clase que gestiona los espacios.
class EspaciosProvider with ChangeNotifier {
  /// Lista de espacios.
  List<Espacio> _espacios = [];

  /// Lista de espacios reservables.
  List<Espacio> _espaciosReservables = [];

  /// Token de autenticación.
  final String? _token;

  /// Getter de la lista de espacios.
  List<Espacio> get espacios => _espacios;

  /// Getter de la lista de espacios reservables.
  List<Espacio> get espaciosReservables => _espaciosReservables;

  EspaciosProvider(this._token) {
    fetchEspacios();
    fetchEspaciosByReservable(true);
  }

  /// Url base de la API.
  String baseUrl = 'http://app.iesluisvives.org:1212';

  /// Función que obtiene los espacios.
  Future<void> fetchEspacios() async {
    final response = await http.get(Uri.parse('$baseUrl/spaces'),
        headers: {'Authorization': 'Bearer $_token'});

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _espacios = results
          .map((espacio) => Espacio(
                uuid: espacio['uuid'],
                name: espacio['name'],
                image: espacio['image'],
                price: espacio['price'],
                isReservable: espacio['isReservable'],
                requiresAuthorization: espacio['requiresAuthorization'],
                authorizedRoles: List<String>.from(espacio['authorizedRoles']),
                bookingWindow: espacio['bookingWindow'],
                description: espacio['description'],
              ))
          .toList();

      notifyListeners();
    } else {
      _espacios = [];
      notifyListeners();
    }
  }

  /// Función que obtiene los espacios reservables.
  Future<void> fetchEspaciosByReservable(bool isReservable) async {
    final response = await http.get(
      Uri.parse('$baseUrl/spaces/reservables/$isReservable'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _espaciosReservables = results
          .map((espacio) => Espacio(
                uuid: espacio['uuid'],
                name: espacio['name'],
                image: espacio['image'],
                price: espacio['price'],
                isReservable: espacio['isReservable'],
                requiresAuthorization: espacio['requiresAuthorization'],
                authorizedRoles: List<String>.from(espacio['authorizedRoles']),
                bookingWindow: espacio['bookingWindow'],
                description: espacio['description'],
              ))
          .toList();
      notifyListeners();
    } else {
      _espaciosReservables = [];
      notifyListeners();
    }
  }

  /// Función que obtiene un espacio por su uuid.
  Future<Espacio?> fetchEspacioByUuid(String uuid) async {
    final response = await http.get(
      Uri.parse('$baseUrl/spaces/$uuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return Espacio(
        uuid: data['uuid'],
        name: data['name'],
        image: data['image'],
        price: data['price'],
        isReservable: data['isReservable'],
        requiresAuthorization: data['requiresAuthorization'],
        authorizedRoles: List<String>.from(data['authorizedRoles']),
        bookingWindow: data['bookingWindow'],
        description: data['description'],
      );
    } else {
      return null;
    }
  }

  /// Función que obtiene un espacio por su nombre.
  Future<Espacio?> fetchEspacioByName(String name) async {
    final response = await http.get(
      Uri.parse('$baseUrl/spaces/nombre/$name'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return Espacio(
        uuid: data['uuid'],
        name: data['name'],
        image: data['image'],
        price: data['price'],
        isReservable: data['isReservable'],
        requiresAuthorization: data['requiresAuthorization'],
        authorizedRoles: List<String>.from(data['authorizedRoles']),
        bookingWindow: data['bookingWindow'],
        description: data['description'],
      );
    } else {
      return null;
    }
  }

  /// Función que añade un espacio.
  Future<void> addEspacio(Espacio espacio) async {
    final response = await http.post(
      Uri.parse('$baseUrl/spaces'),
      headers: {
        'Authorization': 'Bearer $_token',
        'Content-Type': 'application/json'
      },
      body: jsonEncode(espacio.toJson()),
    );

    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);
      _espacios.add(Espacio(
        uuid: data['uuid'],
        name: data['name'],
        image: data['image'],
        price: data['price'],
        isReservable: data['isReservable'],
        requiresAuthorization: data['requiresAuthorization'],
        authorizedRoles: List<String>.from(data['authorizedRoles']),
        bookingWindow: data['bookingWindow'],
        description: data['description'],
      ));
      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }

  /// Función que actualiza un espacio.
  Future<void> updateEspacio(Espacio espacio) async {
    final response = await http.put(
      Uri.parse('$baseUrl/spaces/${espacio.uuid}'),
      headers: {
        'Authorization': 'Bearer $_token',
        'Content-Type': 'application/json'
      },
      body: jsonEncode(espacio.toJson()),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _espacios[_espacios
          .indexWhere((element) => element.uuid == espacio.uuid)] = Espacio(
        uuid: data['uuid'],
        name: data['name'],
        image: data['image'],
        price: data['price'],
        isReservable: data['isReservable'],
        requiresAuthorization: data['requiresAuthorization'],
        authorizedRoles: List<String>.from(data['authorizedRoles']),
        bookingWindow: data['bookingWindow'],
        description: data['description'],
      );
      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }

  /// Función que elimina un espacio.
  Future<void> deleteEspacio(String uuid) async {
    final response = await http.delete(
      Uri.parse('$baseUrl/spaces/$uuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 204) {
      _espacios.removeWhere((element) => element.uuid == uuid);
      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }
}
