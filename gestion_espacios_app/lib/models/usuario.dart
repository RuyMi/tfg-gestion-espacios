class Usuario {
  Usuario({
    required this.uuid,
    required this.name,
    required this.username,
    required this.email,
    required this.password,
    this.avatar,
    required this.userRole,
    required this.credits,
    required this.isActive,
    this.token,
  });

  String uuid;
  String name;
  String username;
  String email;
  String password;
  String? avatar;
  List<String> userRole;
  int credits;
  bool isActive;
  String? token;
}
