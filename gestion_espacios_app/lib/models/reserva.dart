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
