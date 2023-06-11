/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:http/http.dart' as http;

/// Clase que gestiona las reservas.
class ReservasProvider with ChangeNotifier {
  /// Token de autenticación.
  final String? _token;

  /// Id del usuario.
  final String? _userId;

  /// Lista de reservas.
  List<Reserva> _reservas = [];

  /// Lista de mis reservas.
  List<Reserva> _misReservas = [];

  /// Lista de reservas por usuario.
  List<Reserva> _reservasByUser = [];

  /// Lista de reservas por espacio.
  List<Reserva> _reservasBySpace = [];

  /// Lista de reservas por estado.
  List<Reserva> _reservasByStatus = [];

  /// Lista de reservas por hora.
  List<Reserva> _reservasByTime = [];

  /// Getter de la lista de reservas.
  List<Reserva> get reservas => _reservas;

  /// Getter de la lista de reservas por usuario.
  List<Reserva> get reservasByUser => _reservasByUser;

  /// Getter de la lista de mis reservas.
  List<Reserva> get misReservas => _misReservas;

  /// Getter de la lista de reservas por espacio.
  List<Reserva> get reservasBySpace => _reservasBySpace;

  /// Getter de la lista de reservas por estado.
  List<Reserva> get reservasByStatus => _reservasByStatus;

  /// Getter de la lista de reservas por hora.
  List<Reserva> get reservasByTime => _reservasByTime;

  ReservasProvider(this._token, this._userId) {
    fetchMyReservasNotFinished();
    fetchReservas();
  }

  /// Url base de la API.
  String baseUrl = 'http://app.iesluisvives.org:1212';

  /// Función que obtiene las reservas.
  Future<void> fetchReservas() async {
    final response = await http.get(Uri.parse('$baseUrl/bookings'),
        headers: {'Authorization': 'Bearer $_token'});

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _reservas = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _reservas = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por uuid.
  Future<Reserva?> fetchReservaByUuid(String uuid) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/$uuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return Reserva(
        uuid: data['uuid'],
        userId: data['userId'],
        spaceId: data['spaceId'],
        startTime: data['startTime'],
        endTime: data['endTime'],
        observations: data['observations'],
        status: data['status'],
        userName: data['userName'],
        spaceName: data['spaceName'],
        image: data['image'],
      );
    } else {
      return null;
    }
  }

  /// Función que obtiene mis reservas.
  Future<void> fetchMyReservas() async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/user/$_userId'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _misReservas = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _misReservas = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por espacio que no han finalizado.
  Future<void> fetchMyReservasNotFinished() async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/not-finished/user/$_userId'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _misReservas = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _misReservas = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por usuario.
  Future<void> fetchReservasByUser(String userUuid) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/user/$userUuid'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _reservasByUser = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _reservasByUser = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por espacio.
  Future<void> fetchReservasBySpace(String spaceId) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/space/$spaceId'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _reservasBySpace = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _reservasBySpace = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por estado.
  Future<void> fetchReservasByStatus(String status) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/status/$status'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _reservasByStatus = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _reservasByStatus = [];
      notifyListeners();
    }
  }

  /// Función que obtiene las reservas por un tiempo.
  Future<void> fetchReservasByTime(String time, String uuidSpace) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/time/$uuidSpace/$time'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      _reservasByTime = results
          .map((reserva) => Reserva(
                uuid: reserva['uuid'],
                userId: reserva['userId'],
                spaceId: reserva['spaceId'],
                startTime: reserva['startTime'],
                endTime: reserva['endTime'],
                observations: reserva['observations'],
                status: reserva['status'],
                userName: reserva['userName'],
                spaceName: reserva['spaceName'],
                image: reserva['image'],
              ))
          .toList();

      notifyListeners();
    } else {
      _reservasByTime = [];
      notifyListeners();
    }
  }

  /// Función que obtiene los horarios ocupados por un espacio.
  Future<List<String>> fetchOccupiedTimes(String time, String uuidSpace) async {
    final response = await http.get(
      Uri.parse('$baseUrl/bookings/time/$uuidSpace/$time'),
      headers: {'Authorization': 'Bearer $_token'},
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final results = data['data'] as List<dynamic>;
      return results
          .map((reserva) => reserva['startTime'].toString())
          .toList()
          .cast<String>();
    } else {
      return [];
    }
  }

  /// Función que agrega una reserva.
  Future<void> addReserva(Reserva reserva) async {
    final response = await http.post(Uri.parse('$baseUrl/bookings'),
        headers: {
          'Authorization': 'Bearer $_token',
          'Content-Type': 'application/json'
        },
        body: jsonEncode(reserva.toJson()));

    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);
      _reservas.add(Reserva(
        uuid: data['uuid'],
        userId: data['userId'],
        spaceId: data['spaceId'],
        startTime: data['startTime'],
        endTime: data['endTime'],
        observations: data['observations'],
        status: data['status'],
        userName: data['userName'],
        spaceName: data['spaceName'],
        image: data['image'],
      ));

      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }

  /// Función que actualiza una reserva.
  Future<void> updateReserva(Reserva reserva) async {
    final response = await http.put(
        Uri.parse('$baseUrl/bookings/${reserva.uuid}'),
        headers: {
          'Authorization': 'Bearer $_token',
          'Content-Type': 'application/json'
        },
        body: jsonEncode(reserva.toJson()));

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _reservas[_reservas
          .indexWhere((element) => element.uuid == reserva.uuid)] = Reserva(
        uuid: data['uuid'],
        userId: data['userId'],
        spaceId: data['spaceId'],
        startTime: data['startTime'],
        endTime: data['endTime'],
        observations: data['observations'],
        status: data['status'],
        userName: data['userName'],
        spaceName: data['spaceName'],
        image: data['image'],
      );

      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }

  /// Función que elimina una reserva.
  Future<void> deleteReserva(String uuid) async {
    final response = await http.delete(Uri.parse('$baseUrl/bookings/$uuid'),
        headers: {'Authorization': 'Bearer $_token'});

    if (response.statusCode == 204) {
      _reservas.removeWhere((element) => element.uuid == uuid);

      notifyListeners();
    } else {
      throw Exception(response.body);
    }
  }
}
