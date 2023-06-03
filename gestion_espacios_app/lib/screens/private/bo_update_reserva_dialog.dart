import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/eliminar_elemento.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:table_calendar/table_calendar.dart';

import '../../widgets/error_widget.dart';

final List<String> horas = [
  '08:25 - 09:20',
  '09:20 - 10:15',
  '10:15 - 11:10',
  '11:10 - 12:05',
  '12:05 - 12:30',
  '12:30 - 13:25',
  '13:25 - 14:20',
  '14:20 - 15:15',
];

final List<String> statusOptions = [
  'PENDING',
  'APPROVED',
  'REJECTED',
];

String startTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String endTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String dateFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[0].split('-')[2]}/${localDateTimeString.split('T')[0].split('-')[1]}/${localDateTimeString.split('T')[0].split('-')[0].replaceAll('-', '/')}';
}

class EditarReservaBODialog extends StatefulWidget {
  final Reserva reserva;

  const EditarReservaBODialog({Key? key, required this.reserva})
      : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _EditarReservaBODialog createState() => _EditarReservaBODialog();
}

class _EditarReservaBODialog extends State<EditarReservaBODialog> {
  late TextEditingController observationsController;
  DateTime? selectedDay;
  String? selectedHour;

  @override
  void initState() {
    super.initState();
    observationsController =
        TextEditingController(text: widget.reserva.observations ?? '');
  }

  @override
  void dispose() {
    observationsController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);
    final reservasProvider = Provider.of<ReservasProvider>(context);
    final Reserva reserva = widget.reserva;
    String spaceName = reserva.spaceName;
    String userName = reserva.userName;
    String observations = reserva.observations ?? '';
    String? image = reserva.image;
    String startTime = reserva.startTime;
    String endTime = reserva.endTime;

    String myHour =
        '${startTimeFromLocalDateTime(startTime)} - ${endTimeFromLocalDateTime(endTime)}';
    String myDate = dateFromLocalDateTime(startTime);

    return AlertDialog(
      backgroundColor: theme.colorScheme.onBackground,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20),
          side: BorderSide(color: theme.colorScheme.onPrimary)),
      title: Text(
        'Reserva de $userName: $spaceName',
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
              TextField(
                controller: observationsController,
                onChanged: (value) => observations = value,
                cursorColor: theme.colorScheme.secondary,
                style: TextStyle(
                    color: theme.colorScheme.onPrimary, fontFamily: 'KoHo'),
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
                  labelText: 'Observaciones',
                  labelStyle: TextStyle(
                      fontFamily: 'KoHo', color: theme.colorScheme.onPrimary),
                  prefixIcon: Icon(Icons.edit_rounded,
                      color: theme.colorScheme.onPrimary),
                ),
              ),
              const SizedBox(height: 16),
              Column(
                children: [
                  Text(
                    'Estado de la reserva',
                    style: TextStyle(
                        color: theme.colorScheme.onPrimary,
                        fontSize: 18,
                        fontFamily: 'KoHo'),
                  ),
                  const SizedBox(height: 8),
                  DropdownButton<String>(
                    padding: const EdgeInsets.symmetric(horizontal: 4),
                    value: reserva.status,
                    onChanged: (String? value) {
                      setState(() {
                        reserva.status = value!;
                      });
                    },
                    isExpanded: true,
                    borderRadius: BorderRadius.circular(20),
                    dropdownColor: theme.colorScheme.onBackground,
                    icon: Icon(Icons.expand_more_rounded,
                        color: theme.colorScheme.onPrimary, size: 15),
                    style: TextStyle(
                      color: theme.colorScheme.onPrimary,
                      fontFamily: 'KoHo',
                    ),
                    underline: Container(
                      height: 2,
                      color: theme.colorScheme.secondary,
                    ),
                    items: statusOptions
                        .map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        alignment: Alignment.center,
                        child: Text(setEstadoOption(value),
                            style: TextStyle(
                                color: theme.colorScheme.onPrimary,
                                fontFamily: 'KoHo')),
                      );
                    }).toList(),
                  )
                ],
              ),
              const SizedBox(height: 16),
              Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Text(
                      'Hora de inicio/fin',
                      style: TextStyle(
                          color: theme.colorScheme.onPrimary,
                          fontSize: 18,
                          fontFamily: 'KoHo'),
                    ),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
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
                                color: theme.colorScheme.onPrimary,
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
                            firstDay: DateTime.now()
                                .subtract(const Duration(days: 365)),
                            lastDay:
                                DateTime.now().add(const Duration(days: 365)),
                            calendarFormat: CalendarFormat.month,
                            startingDayOfWeek: StartingDayOfWeek.monday,
                            daysOfWeekVisible: true,
                            calendarStyle: CalendarStyle(
                              defaultTextStyle: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontFamily: 'KoHo',
                                color: theme.colorScheme.onPrimary,
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
                                  this.selectedDay = selectedDay;
                                });
                              }
                            },
                          ),
                        ),
                        const SizedBox(width: 16),
                        Column(
                          children: [
                            Container(
                              padding: const EdgeInsets.all(10),
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(20),
                                border: Border.all(
                                  color: theme.colorScheme.secondary,
                                  width: 2,
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: horas
                                    .map((hora) => SizedBox(
                                          width: 150,
                                          child: TextButton(
                                            onPressed: () {
                                              setState(() {
                                                selectedHour = hora;
                                              });
                                            },
                                            style: ButtonStyle(
                                              backgroundColor: hora ==
                                                      selectedHour
                                                  ? MaterialStateProperty.all<Color>(
                                                      theme
                                                          .colorScheme.secondary
                                                          .withOpacity(0.5))
                                                  : hora == myHour
                                                      ? MaterialStateProperty
                                                          .all<Color>(theme
                                                              .colorScheme
                                                              .surface
                                                              .withOpacity(0.5))
                                                      : MaterialStateProperty
                                                          .all<Color>(Colors
                                                              .transparent),
                                              overlayColor:
                                                  MaterialStateProperty
                                                      .resolveWith<Color>(
                                                (Set<MaterialState> states) {
                                                  if (states.contains(
                                                      MaterialState.hovered)) {
                                                    return theme
                                                        .colorScheme.secondary
                                                        .withOpacity(0.2);
                                                  }
                                                  return Colors.transparent;
                                                },
                                              ),
                                            ),
                                            child: Row(
                                              mainAxisAlignment:
                                                  MainAxisAlignment
                                                      .spaceBetween,
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.center,
                                              children: [
                                                Icon(
                                                  Icons.access_time_rounded,
                                                  color: theme
                                                      .colorScheme.onPrimary,
                                                ),
                                                const SizedBox(width: 10),
                                                Text(
                                                  hora,
                                                  textAlign: TextAlign.right,
                                                  style: TextStyle(
                                                    color: theme
                                                        .colorScheme.onPrimary,
                                                    fontWeight:
                                                        hora == selectedHour ||
                                                                hora == myHour
                                                            ? FontWeight.bold
                                                            : FontWeight.normal,
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
                            const SizedBox(height: 16),
                            Container(
                              padding: const EdgeInsets.all(20),
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(20),
                                border: Border.all(
                                  color: theme.colorScheme.secondary,
                                  width: 2,
                                ),
                              ),
                              child: Column(
                                children: [
                                  Text(
                                    'Fecha elegida: ${selectedDay != null ? DateFormat('dd/MM/yyyy').format(selectedDay!) : myDate}',
                                    style: TextStyle(
                                      color: theme.colorScheme.onPrimary,
                                      fontWeight: FontWeight.bold,
                                      fontFamily: 'KoHo',
                                    ),
                                  ),
                                  Text(
                                    'Hora elegida: ${selectedHour ?? myHour}',
                                    style: TextStyle(
                                      color: theme.colorScheme.onPrimary,
                                      fontWeight: FontWeight.bold,
                                      fontFamily: 'KoHo',
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ]),
              const SizedBox(height: 16),
              Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
                ElevatedButton.icon(
                  onPressed: () {
                    if (selectedDay == null && selectedHour == null) {
                      startTime = reserva.startTime;
                      endTime = reserva.endTime;
                    } else if (selectedDay == null) {
                      startTime =
                          '${reserva.startTime.split('T')[0]}T${selectedHour?.split(' ')[0].padLeft(2, '0')}:01';
                      endTime =
                          '${reserva.startTime.split('T')[0]}T${selectedHour?.split(' ')[2].padLeft(2, '0')}:01';
                    } else if (selectedHour == null) {
                      startTime =
                          '${selectedDay!.year}-${selectedDay!.month.toString().padLeft(2, '0')}-${selectedDay!.day.toString().padLeft(2, '0')}T${reserva.startTime.split('T')[1]}';
                      endTime =
                          '${selectedDay!.year}-${selectedDay!.month.toString().padLeft(2, '0')}-${selectedDay!.day.toString().padLeft(2, '0')}T${reserva.endTime.split('T')[1]}';
                    } else {
                      startTime =
                          '${selectedDay!.year}-${selectedDay!.month.toString().padLeft(2, '0')}-${selectedDay!.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[0].padLeft(2, '0')}:01';
                      endTime =
                          '${selectedDay!.year}-${selectedDay!.month.toString().padLeft(2, '0')}-${selectedDay!.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[2].padLeft(2, '0')}:01';
                    }

                    Reserva reservaActualizada = Reserva(
                        uuid: reserva.uuid,
                        userId: reserva.userId,
                        spaceId: reserva.spaceId,
                        spaceName: spaceName,
                        userName: userName,
                        observations: observations,
                        image: image,
                        startTime: startTime,
                        endTime: endTime,
                        status: reserva.status);

                    reservasProvider
                        .updateReserva(reservaActualizada)
                        .then((_) {
                      Navigator.pushNamed(context, '/home-bo');
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return const MyMessageDialog(
                            title: 'Reserva actualizada',
                            description:
                                'Se ha actualizado la reserva correctamente.',
                          );
                        },
                      );
                    }).catchError((error) {
                      showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return MyErrorMessageDialog(
                              title: 'Error al actualizar la reserva',
                              description: error
                                  .toString()
                                  .substring(error.toString().indexOf(':') + 1),
                            );
                          });
                    });
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
                      builder: (BuildContext context) => MyDeleteAlert(
                        title: '¿Está seguro de que desea eliminar la reserva?',
                        ruta: '/home-bo',
                        elemento: reserva,
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
            ],
          ),
        ),
      ),
    );
  }

  String setEstadoOption(String value) {
    switch (value) {
      case 'PENDING':
        return 'PENDIENTE';
      case 'APPROVED':
        return 'APROBADA';
      case 'REJECTED':
        return 'RECHAZADA';
      default:
        return 'PENDIENTE';
    }
  }
}
