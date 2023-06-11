/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/usuario.dart';
import 'package:gestion_espacios_app/providers/storage_provider.dart';
import 'package:gestion_espacios_app/providers/usuarios_provider.dart';
import 'package:gestion_espacios_app/screens/private/bo_add_reserva_dialog.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/eliminar_elemento.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/picked_image_widget.dart';
import 'package:gestion_espacios_app/widgets/user_image_widget.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

/// Clase que representa el diálogo para editar un usuario.
class EditarUsuarioBODialog extends StatefulWidget {
  final Usuario usuario;

  const EditarUsuarioBODialog({Key? key, required this.usuario})
      : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EditarUsuarioBODialog createState() => _EditarUsuarioBODialog();
}

/// Clase que muestra el diálogo para editar un usuario.
class _EditarUsuarioBODialog extends State<EditarUsuarioBODialog> {
  /// El controlador del campo de texto del nombre.
  late TextEditingController nameController;

  /// El controlador del campo de texto del nombre de usuario.
  late TextEditingController usernameController;

  /// El controlador del campo de texto de la contraseña.
  late TextEditingController passwordController;

  /// El controlador del campo de texto del email.
  late TextEditingController emailController;

  /// El controlador del campo de texto de los créditos.
  late TextEditingController creditsController;

  /// El controlador del campo de texto de si está activo.
  late TextEditingController isActiveController;

  /// La imagen seleccionada.
  PickedFile? selectedImage;

  /// El picker de imágenes.
  ImagePicker picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    nameController = TextEditingController(text: widget.usuario.name);
    usernameController = TextEditingController(text: widget.usuario.username);
    passwordController = TextEditingController(text: widget.usuario.password);
    emailController = TextEditingController(text: widget.usuario.email);
    creditsController =
        TextEditingController(text: widget.usuario.credits.toString());
    isActiveController =
        TextEditingController(text: widget.usuario.isActive.toString());
  }

  @override
  void dispose() {
    nameController.dispose();
    usernameController.dispose();
    passwordController.dispose();
    emailController.dispose();
    creditsController.dispose();
    isActiveController.dispose();
    super.dispose();
  }

  /// Función que intenta parsear un entero.
  int tryParseInt(String value, int lastValue) {
    int result;
    try {
      result = int.parse(value);
    } catch (e) {
      result = lastValue;
    }
    return result;
  }

  /// Función que llama al selector de imágenes.
  void pickImage() async {
    PickedFile? pickedFile =
        // ignore: invalid_use_of_visible_for_testing_member
        await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        selectedImage = pickedFile;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    /// Se obtiene el provider de usuarios.
    final usuariosProvider = Provider.of<UsuariosProvider>(context);

    /// Se obtiene el provider de imágenes.
    final storageProvider = Provider.of<StorageProvider>(context);

    /// Se obtiene el usuario actual.
    final Usuario usuario = widget.usuario;

    /// El nombre del usuario.
    String name = usuario.name;

    /// El nombre de usuario.
    String username = usuario.username;

    /// La contraseña del usuario.
    String password = usuario.password;

    /// El email del usuario.
    String email = usuario.email;

    /// El avatar del usuario.
    String? avatar = usuario.avatar;

    /// Los roles del usuario.
    List<String> userRole = usuario.userRole;

    /// Los créditos del usuario.
    int credits = usuario.credits;

    return AlertDialog(
        backgroundColor: theme.colorScheme.onBackground,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: BorderSide(color: theme.colorScheme.onPrimary)),
        title: Text(
          usuario.name,
          style: TextStyle(
              fontWeight: FontWeight.bold,
              color: theme.colorScheme.onPrimary,
              fontFamily: 'KoHo'),
        ),
        content: SingleChildScrollView(
            child: SizedBox(
          width: MediaQuery.of(context).size.width * 0.5,
          child: Column(children: [
            Row(
              children: [
                Column(children: [
                  if (selectedImage != null)
                    ClipRRect(
                      borderRadius: BorderRadius.circular(30),
                      child: PickedImageWidget(
                        pickedImage: selectedImage!,
                        width: 100,
                        height: 100,
                        fit: BoxFit.cover,
                      ),
                    ),
                  if (selectedImage == null && avatar != null)
                    ClipRRect(
                        borderRadius: BorderRadius.circular(30),
                        child: MyUserImageWidget(image: usuario.avatar)),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () {
                      pickImage();
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: theme.colorScheme.secondary,
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30)),
                    ),
                    child: Text('Seleccionar imagen',
                        style: TextStyle(
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.onPrimary),
                        textAlign: TextAlign.center),
                  ),
                ]),
                const SizedBox(width: 16),
                Expanded(
                  child: SizedBox(
                    width: double.infinity,
                    child: Column(
                      children: [
                        TextField(
                          enabled: false,
                          controller: nameController,
                          onChanged: (value) => name = value,
                          cursorColor: theme.colorScheme.secondary,
                          style: TextStyle(
                              color: theme.colorScheme.onPrimary,
                              fontFamily: 'KoHo'),
                          keyboardType: TextInputType.name,
                          decoration: InputDecoration(
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            labelText: 'Nombre',
                            labelStyle: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary),
                            prefixIcon: Icon(Icons.person_rounded,
                                color: theme.colorScheme.onPrimary),
                          ),
                        ),
                        const SizedBox(height: 16),
                        TextField(
                          enabled: false,
                          controller: usernameController,
                          onChanged: (value) => username = value,
                          cursorColor: theme.colorScheme.secondary,
                          keyboardType: TextInputType.multiline,
                          maxLines: null,
                          style: TextStyle(
                              color: theme.colorScheme.onPrimary,
                              fontFamily: 'KoHo'),
                          decoration: InputDecoration(
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            labelText: 'Nombre de usuario',
                            labelStyle: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary),
                            prefixIcon: Icon(Icons.edit_rounded,
                                color: theme.colorScheme.onPrimary),
                          ),
                        ),
                        const SizedBox(height: 16),
                        TextField(
                          enabled: false,
                          controller: emailController,
                          onChanged: (value) => email = value,
                          cursorColor: theme.colorScheme.secondary,
                          keyboardType: TextInputType.number,
                          style: TextStyle(
                              color: theme.colorScheme.onPrimary,
                              fontFamily: 'KoHo'),
                          decoration: InputDecoration(
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            labelText: 'Correo electrónico',
                            labelStyle: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary),
                            prefixIcon: Icon(Icons.edit_rounded,
                                color: theme.colorScheme.onPrimary),
                          ),
                        ),
                        const SizedBox(height: 16),
                        TextField(
                          controller: creditsController,
                          onChanged: (value) =>
                              credits = tryParseInt(value, credits),
                          cursorColor: theme.colorScheme.secondary,
                          keyboardType: TextInputType.number,
                          style: TextStyle(
                              color: theme.colorScheme.onPrimary,
                              fontFamily: 'KoHo'),
                          decoration: InputDecoration(
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(30),
                              borderSide: BorderSide(
                                color: theme.colorScheme.onPrimary,
                              ),
                            ),
                            labelText: 'Créditos disponibles',
                            labelStyle: TextStyle(
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary),
                            prefixIcon: Icon(Icons.monetization_on_outlined,
                                color: theme.colorScheme.onPrimary),
                          ),
                        )
                      ],
                    ),
                  ),
                )
              ],
            ),
            const SizedBox(height: 16),
            CheckboxListTile(
              title: Text('Activo',
                  style: TextStyle(
                      color: theme.colorScheme.onPrimary, fontFamily: 'KoHo')),
              value: isActiveController.text == 'true',
              onChanged: (bool? newValue) {
                setState(() {
                  isActiveController.text = newValue!.toString();
                });
              },
              activeColor: theme.colorScheme.onBackground,
              checkColor: theme.colorScheme.secondary,
              side: BorderSide(color: theme.colorScheme.onPrimary),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30)),
            ),
            const SizedBox(height: 16),
            Column(
              children: [
                Text(
                  'Roles del usuario',
                  style: TextStyle(
                      color: theme.colorScheme.onPrimary,
                      fontSize: 18,
                      fontFamily: 'KoHo'),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Row(
                        children: [
                          Text(
                            'Administrador',
                            style: TextStyle(
                                color: theme.colorScheme.onPrimary,
                                fontFamily: 'KoHo'),
                          ),
                          Checkbox(
                            value: userRole.contains('ADMINISTRATOR'),
                            onChanged: (bool? newValue) {
                              setState(() {
                                if (newValue != null && newValue) {
                                  userRole.add('ADMINISTRATOR');
                                } else {
                                  userRole.remove('ADMINISTRATOR');
                                }
                              });
                            },
                            activeColor: theme.colorScheme.onBackground,
                            checkColor: theme.colorScheme.secondary,
                            side:
                                BorderSide(color: theme.colorScheme.onPrimary),
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(30)),
                          ),
                        ],
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Row(
                        children: [
                          Text(
                            'Profesor',
                            style: TextStyle(
                                color: theme.colorScheme.onPrimary,
                                fontFamily: 'KoHo'),
                          ),
                          Checkbox(
                            value: userRole.contains('TEACHER'),
                            onChanged: (bool? newValue) {
                              setState(() {
                                if (newValue != null && newValue) {
                                  userRole.add('TEACHER');
                                } else {
                                  userRole.remove('TEACHER');
                                }
                              });
                            },
                            activeColor: theme.colorScheme.onBackground,
                            checkColor: theme.colorScheme.secondary,
                            side:
                                BorderSide(color: theme.colorScheme.onPrimary),
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(30)),
                          ),
                        ],
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Row(
                        children: [
                          Text(
                            'Usuario',
                            style: TextStyle(
                                color: theme.colorScheme.onPrimary,
                                fontFamily: 'KoHo'),
                          ),
                          Checkbox(
                            value: userRole.contains('USER'),
                            onChanged: (bool? newValue) {
                              setState(() {
                                if (newValue != null && newValue) {
                                  userRole.add('USER');
                                } else {
                                  userRole.remove('USER');
                                }
                              });
                            },
                            activeColor: theme.colorScheme.onBackground,
                            checkColor: theme.colorScheme.secondary,
                            side:
                                BorderSide(color: theme.colorScheme.onPrimary),
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(30)),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 16),
            Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
              ElevatedButton.icon(
                onPressed: () {
                  Usuario usuarioActualizado = Usuario(
                      uuid: usuario.uuid,
                      name: name,
                      username: username,
                      email: email,
                      password: password,
                      avatar: avatar,
                      credits: creditsController.text == ''
                          ? usuario.credits
                          : credits,
                      isActive: isActiveController.text == 'true',
                      userRole: userRole);

                  if (selectedImage != null) {
                    storageProvider
                        .uploadUserImage(selectedImage!)
                        .then((imageUrl) {
                      usuarioActualizado.avatar = imageUrl;

                      usuariosProvider
                          .updateUsuario(usuario.uuid!, usuarioActualizado)
                          .then((_) {
                        Navigator.pushNamed(context, '/home-bo');
                        showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return const MyMessageDialog(
                              title: 'Usuario actualizado',
                              description:
                                  'Se ha actualizado el usuario correctamente.',
                            );
                          },
                        );
                      }).catchError((error) {
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return MyErrorMessageDialog(
                                title: 'Error al actualizar el usuario',
                                description: error.toString().substring(
                                    error.toString().indexOf(':') + 1),
                              );
                            });
                      });
                    }).catchError((error) {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return MyErrorMessageDialog(
                            title: 'Error al añadir la imagen',
                            description: error
                                .toString()
                                .substring(error.toString().indexOf(':') + 1),
                          );
                        },
                      );
                    });
                  } else {
                    usuariosProvider
                        .updateUsuario(usuario.uuid!, usuarioActualizado)
                        .then((_) {
                      Navigator.pushNamed(context, '/home-bo');
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const MyMessageDialog(
                            title: 'Usuario actualizado',
                            description:
                                'Se ha actualizado el usuario correctamente.',
                          );
                        },
                      );
                    }).catchError((error) {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return MyErrorMessageDialog(
                            title: 'Error al actualizar el usuario',
                            description: error
                                .toString()
                                .substring(error.toString().indexOf(':') + 1),
                          );
                        },
                      );
                    });
                  }
                },
                icon: Icon(Icons.edit_rounded,
                    color: theme.colorScheme.onSecondary),
                label: Text(
                  'Actualizar',
                  style: TextStyle(
                    color: theme.colorScheme.onSecondary,
                    overflow: TextOverflow.ellipsis,
                    fontFamily: 'KoHo',
                    fontSize: 20,
                  ),
                ),
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  backgroundColor: theme.colorScheme.secondary,
                ),
              ),
              const SizedBox(width: 16),
              ElevatedButton.icon(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) => NuevaReservaBODialog(
                      usuario: usuario,
                    ),
                  );
                },
                label: Text(
                  'Añadir reserva',
                  style: TextStyle(
                    color: theme.colorScheme.onSecondary,
                    overflow: TextOverflow.ellipsis,
                    fontFamily: 'KoHo',
                    fontSize: 20,
                  ),
                ),
                icon: Icon(Icons.add_rounded,
                    color: theme.colorScheme.onSecondary),
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  backgroundColor: theme.colorScheme.secondary,
                ),
              ),
              const SizedBox(width: 16),
              ElevatedButton.icon(
                onPressed: () {
                  Navigator.pop(context);
                  showDialog(
                    context: context,
                    builder: (BuildContext context) => MyDeleteAlert(
                      title: '¿Está seguro de que desea eliminar el usuario?',
                      ruta: '/home-bo',
                      elemento: usuario,
                    ),
                  );
                },
                label: Text(
                  'Eliminar',
                  style: TextStyle(
                    color: theme.colorScheme.onSecondary,
                    overflow: TextOverflow.ellipsis,
                    fontFamily: 'KoHo',
                    fontSize: 20,
                  ),
                ),
                icon: Icon(Icons.delete_outline,
                    color: theme.colorScheme.onSecondary),
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                  backgroundColor: theme.colorScheme.secondary,
                ),
              )
            ]),
          ]),
        )));
  }
}
