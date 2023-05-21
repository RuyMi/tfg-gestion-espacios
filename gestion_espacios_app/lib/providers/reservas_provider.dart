import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:http/http.dart' as http;

class ReservasProvider with ChangeNotifier {
  final String? _token;

  List<Reserva> _reservas = [];
  List<Reserva> _reservasByUser = [];
  List<Reserva> _reservasBySpace = [];
  List<Reserva> _reservasByStatus = [];
  List<Reserva> _reservasByTime = [];

  List<Reserva> get reservas => _reservas;
  List<Reserva> get reservasByUser => _reservasByUser;
  List<Reserva> get reservasBySpace => _reservasBySpace;
  List<Reserva> get reservasByStatus => _reservasByStatus;
  List<Reserva> get reservasByTime => _reservasByTime;

  ReservasProvider(this._token) {
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
                  phone: json['phone'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                ))
            .toList();

        notifyListeners();
      } else {
        throw Exception('Error al obtener las reservas.');
      }
    } catch (e) {
      _reservas = [];
      notifyListeners();
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
          phone: data['phone'],
          status: data['status'],
          userName: data['userName'],
          spaceName: data['spaceName'],
        );
      } else {
        throw Exception('Error al obtener la reserva.');
      }
    } catch (e) {
      return null;
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
                  phone: json['phone'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                ))
            .toList();

        notifyListeners();
      } else {
        throw Exception('Error al obtener las reservas.');
      }
    } catch (e) {
      _reservasByUser = [];
      notifyListeners();
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
                  phone: json['phone'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                ))
            .toList();

        notifyListeners();
      } else {
        throw Exception('Error al obtener las reservas.');
      }
    } catch (e) {
      _reservasBySpace = [];
      notifyListeners();
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
                  phone: json['phone'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                ))
            .toList();

        notifyListeners();
      } else {
        throw Exception('Error al obtener las reservas.');
      }
    } catch (e) {
      _reservasByStatus = [];
      notifyListeners();
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
                  phone: json['phone'],
                  status: json['status'],
                  userName: json['userName'],
                  spaceName: json['spaceName'],
                ))
            .toList();

        notifyListeners();
      } else {
        throw Exception('Error al obtener las reservas.');
      }
    } catch (e) {
      _reservasByTime = [];
      notifyListeners();
    }
  }

  Future<void> addReserva(Reserva reserva) async {
    try {
      final response = await http.post(Uri.parse('$baseUrl/bookings'),
          headers: {'Authorization': 'Bearer $_token'},
          body: jsonEncode(reserva));

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _reservas.add(Reserva(
          uuid: data['uuid'],
          userId: data['userId'],
          spaceId: data['spaceId'],
          startTime: data['startTime'],
          endTime: data['endTime'],
          phone: data['phone'],
          status: data['status'],
          userName: data['userName'],
          spaceName: data['spaceName'],
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
          headers: {'Authorization': 'Bearer $_token'},
          body: jsonEncode(reserva));

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _reservas[_reservas
            .indexWhere((element) => element.uuid == reserva.uuid)] = Reserva(
          uuid: data['uuid'],
          userId: data['userId'],
          spaceId: data['spaceId'],
          startTime: data['startTime'],
          endTime: data['endTime'],
          phone: data['phone'],
          status: data['status'],
          userName: data['userName'],
          spaceName: data['spaceName'],
        );

        notifyListeners();
      } else {
        throw Exception('Error al actualizar la reserva.');
      }
    } catch (e) {
      return;
    }
  }

  Future<void> deleteReserva(String uuid) async {
    try {
      final response = await http.delete(Uri.parse('$baseUrl/bookings/$uuid'),
          headers: {'Authorization': 'Bearer $_token'});

      if (response.statusCode == 200) {
        _reservas.removeWhere((element) => element.uuid == uuid);

        notifyListeners();
      } else {
        throw Exception('Error al eliminar la reserva.');
      }
    } catch (e) {
      return;
    }
  }
}