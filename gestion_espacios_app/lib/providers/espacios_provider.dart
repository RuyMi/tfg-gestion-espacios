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

  Future<void> fetchEspacios() async {
    final response = await http.get(
        Uri.parse('http://magarcia.asuscomm.com:25546/spaces'),
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
              ))
          .toList();
      notifyListeners();
    }
  }

  Future<void> fetchEspaciosByReservable(
      bool isReservable) async {
    final response = await http.get(
      Uri.parse(
          'http://magarcia.asuscomm.com:25546/spaces/reservables/$isReservable'),
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
              ))
          .toList();
      notifyListeners();
    }
  }

  Future<Espacio?> fetchEspacioByUuid(String uuid) async {
    final response = await http.get(
      Uri.parse('http://magarcia.asuscomm.com:25546/spaces/$uuid'),
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
      );
    } else {
      return null;
    }
  }

  Future<Espacio?> fetchEspacioByName(String name) async {
    final response = await http.get(
      Uri.parse('http://magarcia.asuscomm.com:25546/spaces/nombre/$name'),
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
      );
    } else {
      return null;
    }
  }

  Future<void> addEspacio(Espacio espacio) async {
    final response = await http.post(
      Uri.parse('http://magarcia.asuscomm.com:25546/spaces'),
      headers: {'Authorization': 'Bearer $_token'},
      body: jsonEncode(espacio),
    );

    if (response.statusCode != 201) {
      throw Exception('Failed to add reserva');
    } else {
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
      ));
      notifyListeners();
    }
  }

  Future<void> updateEspacio(Espacio espacio) async {
    final response = await http.put(
      Uri.parse('http://magarcia.asuscomm.com:25546/spaces/${espacio.uuid}'),
      headers: {'Authorization': 'Bearer $_token'},
      body: jsonEncode(espacio),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to update espacio');
    } else {
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
      );
      notifyListeners();
    }
  }

  Future<void> deleteEspacio(String uuid) async {
    final response = await http.delete(
      Uri.parse('http://magarcia.asuscomm.com:25546/spaces/$uuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode != 204) {
      throw Exception('Failed to delete espacio');
    } else {
      _espacios.removeWhere((element) => element.uuid == uuid);
      notifyListeners();
    }
  }
}
