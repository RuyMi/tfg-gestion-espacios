/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

/// Clase que representa un usuario.
class Usuario {
  Usuario({
    this.uuid,
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

  String? uuid;
  String name;
  String username;
  String email;
  String password;
  String? avatar;
  List<String> userRole;
  int credits;
  bool isActive;
  String? token;

  /// Función que devuelve un JSON con los datos del usuario.
  Map<String, dynamic> toJson() {
    return {
      'uuid': uuid,
      'name': name,
      'username': username,
      'email': email,
      'password': password,
      'avatar': avatar,
      'userRole': userRole,
      'credits': credits,
      'isActive': isActive,
      'token': token,
    };
  }
}
