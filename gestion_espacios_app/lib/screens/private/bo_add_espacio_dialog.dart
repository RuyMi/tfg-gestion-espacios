/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/providers/espacios_provider.dart';
import 'package:gestion_espacios_app/providers/storage_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/picked_image_widget.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

/// Widget que muestra un alert dialog para añadir un nuevo espacio.
class NuevoEspacioBODialog extends StatefulWidget {
  const NuevoEspacioBODialog({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _NuevoEspacioBODialogState createState() => _NuevoEspacioBODialogState();
}

/// Clase que muestra un alert dialog para añadir un nuevo espacio.
class _NuevoEspacioBODialogState extends State<NuevoEspacioBODialog> {
  /// Nombre del espacio.
  String name = '';

  /// Descripción del espacio.
  String description = '';

  /// Imagen del espacio.
  String? image = 'placeholder';

  /// Precio del espacio.
  int price = 0;

  /// Capacidad del espacio.
  bool isReservable = false;

  /// Indica si el espacio requiere autorización.
  bool requiresAuthorization = false;

  /// Lista de roles autorizados.
  List<String> authorizedRoles = [];

  /// Ventana de tiempo para reservar el espacio.
  int bookingWindow = 0;

  /// Imagen seleccionada.
  PickedFile? selectedImage;

  /// Selector de imágenes.
  ImagePicker picker = ImagePicker();

  /// Función que comprueba si el valor es un entero.
  int tryParseInt(String value, {int fallbackValue = 0}) {
    int result;
    try {
      result = int.parse(value);
    } catch (e) {
      result = fallbackValue;
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

    /// Se obtiene el provider de espacios.
    final espaciosProvider = Provider.of<EspaciosProvider>(context);

    /// Se obtiene el provider de las imágenes.
    final storageProvider = Provider.of<StorageProvider>(context);

    return AlertDialog(
      backgroundColor: theme.colorScheme.onBackground,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20),
          side: BorderSide(color: theme.colorScheme.onPrimary)),
      title: Text(
        'Nuevo espacio',
        style: TextStyle(
            fontWeight: FontWeight.bold,
            color: theme.colorScheme.onPrimary,
            fontFamily: 'KoHo'),
      ),
      content: SingleChildScrollView(
        child: SizedBox(
          width: MediaQuery.of(context).size.width * 0.5,
          child: Column(
            children: [
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
                              labelText: 'Nombre del espacio',
                              labelStyle: TextStyle(
                                  fontFamily: 'KoHo',
                                  color: theme.colorScheme.onPrimary),
                              prefixIcon: Icon(Icons.edit_rounded,
                                  color: theme.colorScheme.onPrimary),
                            ),
                          ),
                          const SizedBox(height: 16),
                          TextField(
                            onChanged: (value) => description = value,
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
                              labelText: 'Descripción del espacio',
                              labelStyle: TextStyle(
                                  fontFamily: 'KoHo',
                                  color: theme.colorScheme.onPrimary),
                              prefixIcon: Icon(Icons.edit_rounded,
                                  color: theme.colorScheme.onPrimary),
                            ),
                          ),
                          const SizedBox(height: 16),
                          TextField(
                            onChanged: (value) => price = tryParseInt(value),
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
                              labelText: 'Valor del espacio',
                              labelStyle: TextStyle(
                                  fontFamily: 'KoHo',
                                  color: theme.colorScheme.onPrimary),
                              prefixIcon: Icon(Icons.monetization_on_outlined,
                                  color: theme.colorScheme.onPrimary),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              CheckboxListTile(
                title: Text("Reservable",
                    style: TextStyle(
                        color: theme.colorScheme.onPrimary,
                        fontFamily: 'KoHo')),
                value: isReservable,
                onChanged: (bool? newValue) {
                  setState(() {
                    isReservable = newValue!;
                  });
                },
                activeColor: theme.colorScheme.onBackground,
                checkColor: theme.colorScheme.secondary,
                side: BorderSide(color: theme.colorScheme.onPrimary),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30)),
              ),
              const SizedBox(height: 16),
              CheckboxListTile(
                title: Text(
                  'Autorización requerida',
                  style: TextStyle(
                      color: theme.colorScheme.onPrimary, fontFamily: 'KoHo'),
                ),
                value: requiresAuthorization,
                onChanged: (bool? newValue) {
                  setState(() {
                    requiresAuthorization = newValue!;
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
                    'Roles autorizados',
                    style: TextStyle(
                        color: theme.colorScheme.onPrimary,
                        fontSize: 18,
                        fontFamily: 'KoHo'),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
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
                              value: authorizedRoles.contains('ADMINISTRATOR'),
                              onChanged: (bool? newValue) {
                                setState(() {
                                  if (newValue != null && newValue) {
                                    authorizedRoles.add('ADMINISTRATOR');
                                  } else {
                                    authorizedRoles.remove('ADMINISTRATOR');
                                  }
                                });
                              },
                              activeColor: theme.colorScheme.onBackground,
                              checkColor: theme.colorScheme.secondary,
                              side: BorderSide(
                                  color: theme.colorScheme.onPrimary),
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
                              value: authorizedRoles.contains('TEACHER'),
                              onChanged: (bool? newValue) {
                                setState(() {
                                  if (newValue != null && newValue) {
                                    authorizedRoles.add('TEACHER');
                                  } else {
                                    authorizedRoles.remove('TEACHER');
                                  }
                                });
                              },
                              activeColor: theme.colorScheme.onBackground,
                              checkColor: theme.colorScheme.secondary,
                              side: BorderSide(
                                  color: theme.colorScheme.onPrimary),
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
                              value: authorizedRoles.contains('USER'),
                              onChanged: (bool? newValue) {
                                setState(() {
                                  if (newValue != null && newValue) {
                                    authorizedRoles.add('USER');
                                  } else {
                                    authorizedRoles.remove('USER');
                                  }
                                });
                              },
                              activeColor: theme.colorScheme.onBackground,
                              checkColor: theme.colorScheme.secondary,
                              side: BorderSide(
                                  color: theme.colorScheme.onPrimary),
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
              TextField(
                onChanged: (value) => bookingWindow = tryParseInt(value),
                cursorColor: theme.colorScheme.secondary,
                keyboardType: TextInputType.number,
                style: TextStyle(
                    color: theme.colorScheme.onPrimary, fontFamily: 'KoHo'),
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
                  labelText: 'Ventana de reserva (en días)',
                  labelStyle: TextStyle(
                      fontFamily: 'KoHo', color: theme.colorScheme.onPrimary),
                  prefixIcon: Icon(Icons.calendar_today_outlined,
                      color: theme.colorScheme.onPrimary),
                ),
              ),
              const SizedBox(height: 16),
              ElevatedButton.icon(
                onPressed: () {
                  Espacio espacio = Espacio(
                    name: name,
                    description: description,
                    price: price,
                    image: 'placeholder',
                    isReservable: isReservable,
                    requiresAuthorization: requiresAuthorization,
                    authorizedRoles: authorizedRoles,
                    bookingWindow: bookingWindow,
                  );

                  if (selectedImage != null) {
                    storageProvider
                        .uploadSpaceImage(selectedImage!)
                        .then((imageUrl) {
                      espacio.image = imageUrl;

                      espaciosProvider.addEspacio(espacio).then((_) {
                        Navigator.pushNamed(context, '/home-bo');
                        showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return const MyMessageDialog(
                              title: 'Espacio creado',
                              description:
                                  'Se ha creado el espacio correctamente.',
                            );
                          },
                        );
                      }).catchError((error) {
                        showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return MyErrorMessageDialog(
                                title: 'Error al crear el espacio',
                                description: error.toString().substring(
                                    error.toString().indexOf(':') + 1));
                          },
                        );
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
                    espaciosProvider.addEspacio(espacio).then((_) {
                      Navigator.pushNamed(context, '/home-bo');
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const MyMessageDialog(
                            title: 'Espacio creado',
                            description:
                                'Se ha creado el espacio correctamente.',
                          );
                        },
                      );
                    }).catchError((error) {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return MyErrorMessageDialog(
                            title: 'Error al crear el espacio',
                            description: error
                                .toString()
                                .substring(error.toString().indexOf(':') + 1),
                          );
                        },
                      );
                    });
                  }
                },
                icon: Icon(Icons.add_rounded,
                    color: theme.colorScheme.onSecondary),
                label: Text(
                  'Añadir',
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
            ],
          ),
        ),
      ),
    );
  }
}
