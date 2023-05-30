import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:http/http.dart' as http;

class EspaciosProvider with ChangeNotifier {
  List<Espacio> _espacios = [];
  List<Espacio> _espaciosReservables = [];
  final String? _token;

  List<Espacio> get espacios => _espacios;
  List<Espacio> get espaciosReservables => _espaciosReservables;

  EspaciosProvider(this._token) {
    fetchEspacios();
    fetchEspaciosByReservable(true);
  }

  String baseUrl = 'http://magarcia.asuscomm.com:25546';

  Future<void> fetchEspacios() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/spaces'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _espacios = results
            .map((json) => Espacio(
                  uuid: json['uuid'],
                  name: json['name'],
                  image: json['image'],
                  price: json['price'],
                  isReservable: json['isReservable'],
                  requiresAuthorization: json['requiresAuthorization'],
                  authorizedRoles: List<String>.from(json['authorizedRoles']),
                  bookingWindow: json['bookingWindow'],
                  description: json['description'],
                ))
            .toList();
        notifyListeners();
      } else {
        _espacios = [];
        notifyListeners();
      }
    } catch (e) {
      _espacios = [];
      notifyListeners();
    }
  }

  Future<void> fetchEspaciosByReservable(bool isReservable) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/spaces/reservables/$isReservable'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _espaciosReservables = results
            .map((json) => Espacio(
                  uuid: json['uuid'],
                  name: json['name'],
                  image: json['image'],
                  price: json['price'],
                  isReservable: json['isReservable'],
                  requiresAuthorization: json['requiresAuthorization'],
                  authorizedRoles: List<String>.from(json['authorizedRoles']),
                  bookingWindow: json['bookingWindow'],
                  description: json['description'],
                ))
            .toList();
        notifyListeners();
      } else {
        _espaciosReservables = [];
        notifyListeners();
      }
    } catch (e) {
      _espaciosReservables = [];
      notifyListeners();
    }
  }

  Future<Espacio?> fetchEspacioByUuid(String uuid) async {
    try {
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
    } catch (e) {
      throw Exception('Error al obtener el espacio.');
    }
  }

  Future<Espacio?> fetchEspacioByName(String name) async {
    try {
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
    } catch (e) {
      throw Exception('Error al obtener el espacio.');
    }
  }

  Future<void> addEspacio(Espacio espacio) async {
    try {
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
        throw Exception('Error al añadir el espacio.');
      }
    } catch (e) {
      throw Exception('Error al añadir el espacio.');
    }
  }

  Future<void> updateEspacio(Espacio espacio) async {
    try {
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
        throw Exception('Error al actualizar el espacio.');
      }
    } catch (e) {
      throw Exception('Error al actualizar el espacio.');
    }
  }

  Future<void> deleteEspacio(String uuid) async {
    try {
      final response = await http.delete(
        Uri.parse('$baseUrl/spaces/$uuid'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 204) {
        _espacios.removeWhere((element) => element.uuid == uuid);
        notifyListeners();
      } else {
        throw Exception('Error al eliminar el espacio.');
      }
    } catch (e) {
      throw Exception('Error al eliminar el espacio.');
    }
  }
}
