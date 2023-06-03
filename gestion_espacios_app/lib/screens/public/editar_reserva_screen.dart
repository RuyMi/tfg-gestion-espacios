import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/eliminar_elemento.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/space_image_widget.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:table_calendar/table_calendar.dart';

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

String startTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String endTimeFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[1].split(':')[0]}:${localDateTimeString.split('T')[1].split(':')[1]}';
}

String dateFromLocalDateTime(String localDateTimeString) {
  return '${localDateTimeString.split('T')[0].split('-')[2]}/${localDateTimeString.split('T')[0].split('-')[1]}/${localDateTimeString.split('T')[0].split('-')[0].replaceAll('-', '/')}';
}

class EditarReservaScreen extends StatefulWidget {
  final Reserva reserva;

  const EditarReservaScreen({Key? key, required this.reserva})
      : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _ReservaSala createState() => _ReservaSala();
}

// ignore: must_be_immutable
class _ReservaSala extends State<EditarReservaScreen> {
  DateTime? selectedDay;
  String? selectedHour;
  final ScrollController _scrollController = ScrollController();

  late TextEditingController observationsController;

  @override
  void initState() {
    super.initState();
    observationsController = TextEditingController(
        text: widget.reserva.observations ?? 'Sin obsevaciones.');
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

    observationsController = TextEditingController(text: reserva.observations);

    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) currentFocus.unfocus();
      },
      child: Scaffold(
        resizeToAvoidBottomInset: true,
        appBar: AppBar(
          title: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                reserva.spaceName,
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
              Navigator.pushNamed(context, '/mis-reservas');
            },
            icon: const Icon(Icons.arrow_back_ios_rounded),
          ),
          actions: [
            IconButton(
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (BuildContext context) => MyDeleteAlert(
                    title: '¿Está seguro de que desea eliminar la reserva?',
                    ruta: '/mis-reservas',
                    elemento: reserva,
                  ),
                );
              },
              icon: const Icon(Icons.delete_outline_rounded),
              color: theme.colorScheme.secondary,
              iconSize: 25,
            ),
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
                            radius: 35,
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(75),
                              child: MySpaceImageWidget(image: reserva.image),
                            ),
                          ),
                        ),
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.all(20),
                            child: Text(
                              'Reserva de $userName para el espacio: $spaceName',
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
                      controller: observationsController,
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
                  const SizedBox(height: 20),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Text(
                        DateFormat('dd/MM/yyyy HH:mm')
                            .format(DateTime.parse(reserva.startTime)),
                        style: TextStyle(
                          fontSize: 15,
                          fontFamily: 'KoHo',
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.secondary,
                        ),
                      ),
                      Text(
                        DateFormat('dd/MM/yyyy HH:mm')
                            .format(DateTime.parse(reserva.endTime)),
                        style: TextStyle(
                          fontSize: 15,
                          fontFamily: 'KoHo',
                          fontWeight: FontWeight.bold,
                          color: theme.colorScheme.secondary,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
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
                          Icons.chevron_left,
                          color: theme.colorScheme.secondary,
                        ),
                        rightChevronIcon: Icon(
                          Icons.chevron_right,
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
                            color: theme.colorScheme.surface,
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
                          _scrollController.animateTo(
                              _scrollController.position.viewportDimension,
                              duration: const Duration(milliseconds: 1000),
                              curve: Curves.easeInOut);
                        }
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  Column(
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
                                  backgroundColor: hora == selectedHour
                                      ? MaterialStateProperty.all<Color>(theme
                                          .colorScheme.secondary
                                          .withOpacity(0.5))
                                      : hora == myHour
                                          ? MaterialStateProperty.all<Color>(
                                              theme.colorScheme.surface
                                                  .withOpacity(0.5))
                                          : MaterialStateProperty.all<Color>(
                                              Colors.transparent),
                                  overlayColor:
                                      MaterialStateProperty.resolveWith<Color>(
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
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Icon(
                                      Icons.access_time_rounded,
                                      color: theme.colorScheme.surface,
                                    ),
                                    const SizedBox(width: 10),
                                    Text(
                                      hora,
                                      textAlign: TextAlign.right,
                                      style: TextStyle(
                                        color: theme.colorScheme.surface,
                                        fontWeight: hora == selectedHour ||
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
                  const SizedBox(height: 20),
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
                            color: theme.colorScheme.secondary,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                          ),
                        ),
                        Text(
                          'Hora elegida: ${selectedHour ?? myHour}',
                          style: TextStyle(
                            color: theme.colorScheme.secondary,
                            fontWeight: FontWeight.bold,
                            fontFamily: 'KoHo',
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
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
                        Navigator.pushNamed(context, '/mis-reservas');
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
                    child: Text('Editar reserva',
                        style: TextStyle(
                            color: theme.colorScheme.onSecondary,
                            fontFamily: 'KoHo')),
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
