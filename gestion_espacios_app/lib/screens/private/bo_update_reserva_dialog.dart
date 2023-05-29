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
    observationsController = TextEditingController(
        text: widget.reserva.observations ?? 'Sin observaciones');
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
    String observations = reserva.observations ?? 'Sin observaciones';
    String? image = reserva.image;
    String? startTime = reserva.startTime;
    String? endTime = reserva.endTime;
    String status = reserva.status ?? 'PENDING';

    String myHour =
        '${startTimeFromLocalDateTime(startTime)} - ${endTimeFromLocalDateTime(endTime)}';
    String myDate = dateFromLocalDateTime(startTime);

    void updateStatus(String newStatus) {
      setState(() {
        if (status == newStatus) {
          status = '';
        } else {
          status = newStatus;
        }
      });
    }

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
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Row(
                          children: [
                            Text(
                              'Pendiente',
                              style: TextStyle(
                                  color: theme.colorScheme.onPrimary,
                                  fontFamily: 'KoHo'),
                            ),
                            StatefulBuilder(
                              builder:
                                  (BuildContext context, StateSetter setState) {
                                return Checkbox(
                                  value: status == 'PENDING',
                                  onChanged: (bool? newValue) {
                                    setState(() {
                                      updateStatus('PENDING');
                                    });
                                  },
                                  activeColor: theme.colorScheme.onBackground,
                                  checkColor: theme.colorScheme.secondary,
                                  side: BorderSide(
                                      color: theme.colorScheme.onPrimary),
                                  shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30)),
                                );
                              },
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Row(
                          children: [
                            Text(
                              'Aceptada',
                              style: TextStyle(
                                  color: theme.colorScheme.onPrimary,
                                  fontFamily: 'KoHo'),
                            ),
                            StatefulBuilder(
                              builder:
                                  (BuildContext context, StateSetter setState) {
                                return Checkbox(
                                  value: status == 'APPROVED',
                                  onChanged: (bool? newValue) {
                                    setState(() {
                                      updateStatus('APPROVED');
                                    });
                                  },
                                  activeColor: theme.colorScheme.onBackground,
                                  checkColor: theme.colorScheme.secondary,
                                  side: BorderSide(
                                      color: theme.colorScheme.onPrimary),
                                  shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30)),
                                );
                              },
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Row(
                          children: [
                            Text(
                              'Rechazada',
                              style: TextStyle(
                                  color: theme.colorScheme.onPrimary,
                                  fontFamily: 'KoHo'),
                            ),
                            StatefulBuilder(
                              builder:
                                  (BuildContext context, StateSetter setState) {
                                return Checkbox(
                                  value: status == 'REJECTED',
                                  onChanged: (bool? newValue) {
                                    setState(() {
                                      updateStatus('REJECTED');
                                    });
                                  },
                                  activeColor: theme.colorScheme.onBackground,
                                  checkColor: theme.colorScheme.secondary,
                                  side: BorderSide(
                                      color: theme.colorScheme.onPrimary),
                                  shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30)),
                                );
                              },
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
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
                            focusedDay: DateTime.parse(startTime),
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
                                                  color: hora == selectedHour ||
                                                          hora == myHour
                                                      ? theme
                                                          .colorScheme.surface
                                                      : theme.colorScheme
                                                          .onPrimary,
                                                ),
                                                const SizedBox(width: 10),
                                                Text(
                                                  hora,
                                                  textAlign: TextAlign.right,
                                                  style: TextStyle(
                                                    color:
                                                        hora == selectedHour ||
                                                                hora == myHour
                                                            ? theme.colorScheme
                                                                .surface
                                                            : theme.colorScheme
                                                                .onPrimary,
                                                    fontWeight: hora ==
                                                                myHour ||
                                                            hora == selectedHour
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
                    startTime =
                        '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[0].padLeft(2, '0')}:00';
                    endTime =
                        '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[2].padLeft(2, '0')}:00';

                    Reserva reservaActualizada = Reserva(
                        uuid: reserva.uuid,
                        userId: reserva.userId,
                        spaceId: reserva.spaceId,
                        spaceName: spaceName,
                        userName: userName,
                        observations: observations,
                        image: image,
                        startTime: startTime ?? reserva.startTime,
                        endTime: endTime ?? reserva.endTime,
                        status: status);

                    Navigator.of(context).pop();

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
                            return const MyErrorMessageDialog(
                              title: 'Error',
                              description:
                                  'Ha ocurrido un error al actualizar la reserva.',
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
}

String startTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String endTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String dateFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[0].split('-')[2]}/${localDateTimeString.split('T')[0].split('-')[1]}/${localDateTimeString.split('T')[0].split('-')[0].replaceAll('-', '/')}';
}