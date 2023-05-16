class Reserva {
  Reserva({
    required this.uuid,
    required this.userId,
    required this.spaceId,
    required this.startTime,
    required this.endTime,
    this.phone,
    required this.status,
  });

  String uuid;
  String userId;
  String spaceId;
  String startTime;
  String endTime;
  String? phone;
  String status;
}