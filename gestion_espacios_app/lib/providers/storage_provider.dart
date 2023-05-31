import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';

class StorageProvider with ChangeNotifier {
  final String? _token;

  StorageProvider(this._token);

  String baseUrl = 'http://magarcia.asuscomm.com:25546';

  Future<String> uploadSpaceImage(Uint8List imageBytes) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('$baseUrl/spaces/storage'),
      );

      request.headers['Authorization'] = 'Bearer $_token';

      final file = http.MultipartFile.fromBytes(
        'image',
        imageBytes,
        filename: 'space.png',
        contentType: MediaType('image', 'png'),
      );

      request.files.add(file);

      final response = await request.send();

      if (response.statusCode == 201) {
        final data = await response.stream.bytesToString();
        final json = jsonDecode(data);
        final fileName = json['data']['fileName'];

        return fileName.split('.')[0];
      } else {
        throw Exception('Error al subir la imagen.');
      }
    } catch (e) {
      throw Exception('Error al subir la imagen.');
    }
  }

  Future<void> uploadUserImage(Uint8List imageBytes) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('$baseUrl/users/storage'),
      );

      request.headers['Authorization'] = 'Bearer $_token';

      final file = http.MultipartFile.fromBytes(
        'image',
        imageBytes,
        filename: 'user.png',
        contentType: MediaType('image', 'png'),
      );

      request.files.add(file);

      final response = await request.send();

      if (response.statusCode == 201) {
        final data = await response.stream.bytesToString();
        final json = jsonDecode(data);
        final fileName = json['data']['fileName'];

        return fileName.split('.')[0];
      } else {
        throw Exception('Error al subir la imagen.');
      }
    } catch (e) {
      throw Exception('Error al subir la imagen.');
    }
  }
}