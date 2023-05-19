class Espacio {
  Espacio({
    required this.uuid,
    required this.name,
    required this.description,
    this.image,
    required this.price,
    required this.isReservable,
    required this.requiresAuthorization,
    required this.authorizedRoles,
    required this.bookingWindow,
  });

  String uuid;
  String name;
  String description;
  String? image;
  int price;
  bool isReservable;
  bool requiresAuthorization;
  List<String> authorizedRoles;
  String bookingWindow;
}