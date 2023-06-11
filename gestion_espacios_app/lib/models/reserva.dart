/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

/// Clase que representa una reserva.
class Reserva {
  Reserva({
    this.uuid,
    required this.userId,
    required this.spaceId,
    required this.startTime,
    required this.endTime,
    required this.userName,
    required this.spaceName,
    this.observations,
    this.status,
    this.image,
  });

  String? uuid;
  String userId;
  String spaceId;
  String startTime;
  String endTime;
  String userName;
  String spaceName;
  String? observations;
  String? status;
  String? image;

  /// Función que devuelve un JSON con los datos de la reserva.
  Map<String, dynamic> toJson() {
    return {
      'uuid': uuid,
      'userId': userId,
      'spaceId': spaceId,
      'startTime': startTime,
      'endTime': endTime,
      'userName': userName,
      'spaceName': spaceName,
      'observations': observations,
      'status': status,
      'image': image,
    };
  }
}
