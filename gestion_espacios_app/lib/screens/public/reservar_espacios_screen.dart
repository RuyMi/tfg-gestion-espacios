/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:table_calendar/table_calendar.dart';

/// Lista de horas disponibles.
final List<Map<String, dynamic>> horas = [
  {'hora': '08:25 - 09:20', 'ocupada': false},
  {'hora': '09:20 - 10:15', 'ocupada': false},
  {'hora': '10:15 - 11:10', 'ocupada': false},
  {'hora': '11:10 - 12:05', 'ocupada': false},
  {'hora': '12:05 - 12:30', 'ocupada': false},
  {'hora': '12:30 - 13:25', 'ocupada': false},
  {'hora': '13:25 - 14:20', 'ocupada': false},
  {'hora': '14:20 - 15:15', 'ocupada': false},
];

/// Pantalla de reserva de espacios.
class ReservaEspacioScreen extends StatefulWidget {
  const ReservaEspacioScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _ReservaSala createState() => _ReservaSala();
}

/// Clase que muestra la pantalla de reserva de espacios.
class _ReservaSala extends State<ReservaEspacioScreen> {
  /// Variable que indica si se ha seleccionado un día.
  bool _isDaySelected = false;

  /// Variable que indica si se ha seleccionado una hora.
  bool _isHourSelected = false;

  /// Variable que indica si el día seleccionado.
  DateTime? selectedDay;

  /// Variable que indica la hora seleccionada.
  String? selectedHour;

  /// Variable que indica las observaciones de la reserva.
  String observations = '';

  /// Controlador del scroll.
  final ScrollController _scrollController = ScrollController();

  /// Función que convierte una fecha a un String.
  String convertirHoraLocalDateTime(String localDateTime) {
    DateTime horaDateTime = DateTime.parse(localDateTime);
    String horaInicio = DateFormat('HH:mm').format(horaDateTime);
    return horaInicio;
  }

  /// Función que actualiza las horas ocupadas.
  void actualizarHorasOcupadas(List<String> horasOcupadas) {
    setState(() {
      List<String> horasOcupadasConvertidas = horasOcupadas
          .map((hora) => convertirHoraLocalDateTime(hora))
          .toList();

      for (int i = 0; i < horas.length; i++) {
        if (horasOcupadasConvertidas
            .any((horaOcupada) => horas[i]['hora'].startsWith(horaOcupada))) {
          horas[i]['ocupada'] = true;
        } else {
          horas[i]['ocupada'] = false;
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    /// Se obtiene el espacio seleccionado.
    final Espacio espacio =
        ModalRoute.of(context)!.settings.arguments as Espacio;

    /// Se obtiene el provider de reservas.
    final reservasProvider = Provider.of<ReservasProvider>(context);

    /// Se obtiene el provider de autenticación.
    final authProvider = Provider.of<AuthProvider>(context);

    /// Se obtiene el id del usuario actual.
    final userId = authProvider.usuario.uuid;

    /// Se obtiene el nombre del usuario actual.
    final userName = authProvider.usuario.name;

    /// Se obtiene el id del espacio actual.
    final spaceId = espacio.uuid;

    /// Se obtiene el nombre del espacio actual.
    final spaceName = espacio.name;

    /// variable que indica la hora de inicio de la reserva.
    String startTime;

    /// variable que indica la hora de fin de la reserva.
    String endTime;

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
        resizeToAvoidBottomInset: true,
        appBar: AppBar(
          toolbarHeight: 75,
          centerTitle: true,
          title: Row(
            children: [
              Text(
                espacio.name,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  fontFamily: 'KoHo',
                ),
              ),
            ],
          ),
          leading: IconButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/home');
            },
            icon: const Icon(Icons.arrow_back_ios_rounded),
          ),
          actions: [
            Row(
              children: [
                Text(
                  espacio.price.toString(),
                  style: TextStyle(
                    fontFamily: 'KoHo',
                    color: theme.colorScheme.secondary,
                    fontWeight: FontWeight.bold,
                    fontSize: 15,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: Icon(
                    Icons.monetization_on_outlined,
                    color: theme.colorScheme.secondary,
                    size: 20,
                  ),
                ),
              ],
            )
          ],
          backgroundColor: theme.colorScheme.background,
        ),
        body: SingleChildScrollView(
          controller: _scrollController,
          scrollDirection: Axis.vertical,
          child: Padding(
            padding: const EdgeInsets.all(10),
            child: Center(
              child: Column(
                children: [
                  const SizedBox(height: 10),
                  Container(
                    margin: const EdgeInsets.only(right: 20, left: 20),
                    decoration: BoxDecoration(
                      color: theme.colorScheme.onBackground,
                      borderRadius: BorderRadius.circular(50),
                      boxShadow: [
                        BoxShadow(
                          color: theme.colorScheme.surface.withOpacity(0.2),
                          spreadRadius: 2,
                          blurRadius: 5,
                          offset: const Offset(0, 3),
                        ),
                      ],
                    ),
                    child: Row(
                      children: [
                        Container(
                          decoration: const BoxDecoration(
                            borderRadius: BorderRadius.only(
                              topLeft: Radius.circular(20),
                              bottomLeft: Radius.circular(20),
                            ),
                          ),
                          child: CircleAvatar(
                            radius: 50,
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(75),
                              child: MySpaceImageWidget(image: espacio.image),
                            ),
                          ),
                        ),
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.all(20),
                            child: Text(
                              espacio.description ?? '',
                              maxLines: 3,
                              textAlign: TextAlign.start,
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                color: theme.colorScheme.onSecondary,
                                fontFamily: 'KoHo',
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 30),
                  SizedBox(
                    width: 250,
                    child: TextField(
                      onChanged: (value) => observations = value,
                      keyboardType: TextInputType.multiline,
                      maxLines: 3,
                      cursorColor: theme.colorScheme.secondary,
                      style: TextStyle(
                          color: theme.colorScheme.secondary,
                          fontFamily: 'KoHo'),
                      decoration: InputDecoration(
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(30),
                          borderSide: BorderSide(
                            color: theme.colorScheme.secondary,
                          ),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(30),
                          borderSide: BorderSide(
                            color: theme.colorScheme.secondary,
                          ),
                        ),
                        labelText: 'Observaciones',
                        labelStyle: TextStyle(
                            fontFamily: 'KoHo',
                            color: theme.colorScheme.secondary),
                        prefixIcon: Icon(Icons.message_rounded,
                            color: theme.colorScheme.secondary),
                      ),
                    ),
                  ),
                  const SizedBox(height: 30),
                  Container(
                    width: 300,
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(30),
                      border: Border.all(
                        color: theme.colorScheme.secondary,
                        width: 2,
                      ),
                    ),
                    child: TableCalendar(
                      headerStyle: HeaderStyle(
                        titleTextStyle: TextStyle(
                          color: theme.colorScheme.secondary,
                          fontSize: 20,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                        ),
                        formatButtonVisible: false,
                        leftChevronIcon: Icon(
                          Icons.chevron_left_rounded,
                          color: theme.colorScheme.secondary,
                        ),
                        rightChevronIcon: Icon(
                          Icons.chevron_right_rounded,
                          color: theme.colorScheme.secondary,
                        ),
                      ),
                      focusedDay: DateTime.now(),
                      firstDay:
                          DateTime.now().subtract(const Duration(days: 365)),
                      lastDay: DateTime.now().add(const Duration(days: 365)),
                      calendarFormat: CalendarFormat.month,
                      startingDayOfWeek: StartingDayOfWeek.monday,
                      daysOfWeekVisible: true,
                      daysOfWeekHeight: 30,
                      calendarStyle: CalendarStyle(
                        defaultTextStyle: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                        ),
                        isTodayHighlighted: true,
                        selectedDecoration: BoxDecoration(
                          color: theme.colorScheme.secondary,
                          shape: BoxShape.circle,
                        ),
                        selectedTextStyle: TextStyle(
                            color: theme.colorScheme.onSecondary,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo'),
                        todayDecoration: const BoxDecoration(
                          color: Colors.grey,
                          shape: BoxShape.circle,
                        ),
                        todayTextStyle: TextStyle(
                            color: theme.colorScheme.background,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo'),
                        weekendTextStyle: const TextStyle(
                            color: Colors.grey, fontFamily: 'KoHo'),
                      ),
                      daysOfWeekStyle: DaysOfWeekStyle(
                        weekdayStyle: TextStyle(
                          color: theme.colorScheme.secondary,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo',
                        ),
                        weekendStyle: const TextStyle(
                          fontFamily: 'KoHo',
                          color: Colors.grey,
                        ),
                      ),
                      selectedDayPredicate: (day) {
                        return isSameDay(selectedDay, day);
                      },
                      onDaySelected: (selectedDay, focusedDay) {
                        final now = DateTime.now();
                        if (selectedDay.isBefore(
                                now.subtract(const Duration(days: 1))) ||
                            (selectedDay.weekday == 6 ||
                                selectedDay.weekday == 7)) {
                          showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const MyErrorMessageDialog(
                                  title: 'Fecha incorrecta',
                                  description:
                                      'Debes seleccionar una fecha no festiva posterior a hoy.');
                            },
                          );
                        } else {
                          setState(() {
                            _isDaySelected = true;
                            this.selectedDay = selectedDay;
                          });
                          _scrollController.animateTo(
                              _scrollController.position.viewportDimension,
                              duration: const Duration(milliseconds: 1000),
                              curve: Curves.easeInOut);

                          String date = DateFormat('yyyy-MM-dd')
                              .parse(selectedDay.toString())
                              .toString()
                              .split(' ')[0];
                          reservasProvider
                              .fetchOccupiedTimes(date, espacio.uuid!)
                              .then((horasOcupadas) {
                            actualizarHorasOcupadas(horasOcupadas);
                          });
                        }
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  Visibility(
                    visible: _isDaySelected,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: horas
                          .map((hora) => SizedBox(
                                width: 175,
                                child: TextButton(
                                  onPressed: hora['ocupada']
                                      ? null
                                      : () {
                                          setState(() {
                                            _isHourSelected = true;
                                            selectedHour = hora['hora'];
                                          });
                                          _scrollController.animateTo(
                                              _scrollController
                                                  .position.viewportDimension,
                                              duration: const Duration(
                                                  milliseconds: 1000),
                                              curve: Curves.easeInOut);
                                        },
                                  style: ButtonStyle(
                                    backgroundColor:
                                        hora['hora'] == selectedHour
                                            ? MaterialStateProperty.all<Color>(
                                                theme.colorScheme.secondary
                                                    .withOpacity(0.5))
                                            : null,
                                    overlayColor: MaterialStateProperty
                                        .resolveWith<Color>(
                                      (Set<MaterialState> states) {
                                        if (states
                                            .contains(MaterialState.hovered)) {
                                          return theme.colorScheme.secondary
                                              .withOpacity(0.2);
                                        }
                                        return Colors.transparent;
                                      },
                                    ),
                                  ),
                                  child: Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceBetween,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      Icon(
                                        Icons.access_time_rounded,
                                        color: hora['ocupada']
                                            ? Colors.grey
                                            : theme.colorScheme.surface,
                                      ),
                                      const SizedBox(width: 10),
                                      Text(
                                        hora['hora'],
                                        textAlign: TextAlign.right,
                                        style: TextStyle(
                                          color: hora['ocupada']
                                              ? Colors.grey
                                              : theme.colorScheme.surface,
                                          fontWeight: FontWeight.bold,
                                          fontFamily: 'KoHo',
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ))
                          .toList(),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Visibility(
                    visible: _isHourSelected || _isDaySelected,
                    child: Container(
                      padding: const EdgeInsets.all(20),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(
                          color: theme.colorScheme.secondary,
                          width: 2,
                        ),
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Visibility(
                              visible: _isDaySelected,
                              child: Text(
                                'Fecha elegida: ${selectedDay != null ? DateFormat('dd/MM/yyyy').format(selectedDay!) : ''}',
                                style: TextStyle(
                                  color: theme.colorScheme.secondary,
                                  fontWeight: FontWeight.bold,
                                  fontFamily: 'KoHo',
                                ),
                              )),
                          Visibility(
                              visible: _isHourSelected,
                              child: Text(
                                'Hora elegida: $selectedHour',
                                style: TextStyle(
                                  color: theme.colorScheme.secondary,
                                  fontWeight: FontWeight.bold,
                                  fontFamily: 'KoHo',
                                ),
                              )),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Visibility(
                    visible: _isHourSelected,
                    child: ElevatedButton(
                      onPressed: () {
                        startTime =
                            '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[0].padLeft(2, '0')}:01';
                        endTime =
                            '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[2].padLeft(2, '0')}:01';

                        final reserva = Reserva(
                          userId: userId!,
                          spaceId: spaceId!,
                          startTime: startTime,
                          endTime: endTime,
                          userName: userName,
                          spaceName: spaceName,
                          observations: observations,
                          status: espacio.requiresAuthorization
                              ? 'PENDING'
                              : 'APPROVED',
                          image: espacio.image,
                        );

                        reservasProvider.addReserva(reserva).then((_) {
                          Navigator.pushNamed(context, '/mis-reservas');
                          showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const MyMessageDialog(
                                title: 'Reserva realizada',
                                description:
                                    'Se ha realizado la reserva correctamente.',
                              );
                            },
                          );
                        }).catchError((error) {
                          showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return MyErrorMessageDialog(
                                  title: 'Error al realizar la reserva',
                                  description: error.toString().substring(
                                      error.toString().indexOf(':') + 1),
                                );
                              });
                        });
                      },
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30),
                        ),
                        backgroundColor: theme.colorScheme.secondary,
                      ),
                      child: Text('Reservar',
                          style: TextStyle(
                              color: theme.colorScheme.onSecondary,
                              fontFamily: 'KoHo')),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
