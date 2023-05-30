import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:http/http.dart' as http;

class ReservasProvider with ChangeNotifier {
  final String? _token;
  final String? _userId;

  List<Reserva> _reservas = [];
  List<Reserva> _misReservas = [];
  List<Reserva> _reservasByUser = [];
  List<Reserva> _reservasBySpace = [];
  List<Reserva> _reservasByStatus = [];
  List<Reserva> _reservasByTime = [];

  List<Reserva> get reservas => _reservas;
  List<Reserva> get reservasByUser => _reservasByUser;
  List<Reserva> get misReservas => _misReservas;
  List<Reserva> get reservasBySpace => _reservasBySpace;
  List<Reserva> get reservasByStatus => _reservasByStatus;
  List<Reserva> get reservasByTime => _reservasByTime;

  ReservasProvider(this._token, this._userId) {
    fetchMyReservas();
    fetchReservas();
  }

  String baseUrl = 'http://magarcia.asuscomm.com:25546';

  Future<void> fetchReservas() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/bookings'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _reservas = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _reservas = [];
        notifyListeners();
      }
    } catch (e) {
      _reservas = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<Reserva?> fetchReservaByUuid(String uuid) async {
    try {
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
    } catch (e) {
      throw Exception('Error al obtener la reserva.');
    }
  }

  Future<void> fetchMyReservas() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/bookings/user/$_userId'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _misReservas = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _misReservas = [];
        notifyListeners();
      }
    } catch (e) {
      _misReservas = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<void> fetchReservasByUser(String userUuid) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/bookings/user/$userUuid'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _reservasByUser = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _reservasByUser = [];
        notifyListeners();
      }
    } catch (e) {
      _reservasByUser = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<void> fetchReservasBySpace(String spaceId) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/bookings/space/$spaceId'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _reservasBySpace = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _reservasBySpace = [];
        notifyListeners();
      }
    } catch (e) {
      _reservasBySpace = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<void> fetchReservasByStatus(String status) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/bookings/status/$status'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _reservasByStatus = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _reservasByStatus = [];
        notifyListeners();
      }
    } catch (e) {
      _reservasByStatus = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<void> fetchReservasByTime(String time, String uuidSpace) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/bookings/time/$uuidSpace/$time'),
        headers: {'Authorization': 'Bearer $_token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final results = data['data'] as List<dynamic>;
        _reservasByTime = results
            .map((json) => Reserva(
                  uuid: json['uuid'],
                  userId: json['userId'],
                  spaceId: json['spaceId'],
                  startTime: json['startTime'],
                  endTime: json['endTime'],
                  observations: json['observations'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                  image: json['image'],
                ))
            .toList();

        notifyListeners();
      } else {
        _reservasByTime = [];
        notifyListeners();
      }
    } catch (e) {
      _reservasByTime = [];
      notifyListeners();
      throw Exception('Error al obtener las reservas.');
    }
  }

  Future<void> addReserva(Reserva reserva) async {
    try {
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
        throw Exception('Error al crear la reserva.');
      }
    } catch (e) {
      throw Exception('Error al crear la reserva.');
    }
  }

  Future<void> updateReserva(Reserva reserva) async {
    try {
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
        throw Exception('Error al actualizar la reserva.');
      }
    } catch (e) {
      throw Exception('Error al actualizar la reserva.');
    }
  }

  Future<void> deleteReserva(String uuid, String userId) async {
    try {
      final response = await http.delete(
          Uri.parse('$baseUrl/bookings/$uuid/$userId'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 204) {
        _reservas.removeWhere((element) => element.uuid == uuid);

        notifyListeners();
      } else {
        throw Exception('Error al eliminar la reserva.');
      }
    } catch (e) {
      throw Exception('Error al eliminar la reserva.');
    }
  }
}
