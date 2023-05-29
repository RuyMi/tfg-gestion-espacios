import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/models/reserva.dart';
import 'package:gestion_espacios_app/providers/auth_provider.dart';
import 'package:gestion_espacios_app/providers/reservas_provider.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
import 'package:gestion_espacios_app/widgets/image_widget.dart';
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

class ReservaEspacioScreen extends StatefulWidget {
  const ReservaEspacioScreen({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _ReservaSala createState() => _ReservaSala();
}

class _ReservaSala extends State<ReservaEspacioScreen> {
  bool _isDaySelected = false;
  bool _isHourSelected = false;
  DateTime? selectedDay;
  String? selectedHour;
  final ScrollController _scrollController = ScrollController();

  String observations = '';

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    final Espacio espacio =
        ModalRoute.of(context)!.settings.arguments as Espacio;
    final reservasProvider = Provider.of<ReservasProvider>(context);
    final authProvider = Provider.of<AuthProvider>(context);
    final userId = authProvider.usuario.uuid;
    final userName = authProvider.usuario.name;
    final spaceId = espacio.uuid;
    final spaceName = espacio.name;
    String startTime;
    String endTime;

    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
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
                          radius: 35,
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(75),
                            child: MyImageWidget(image: espacio.image),
                          ),
                        ),
                      ),
                      Expanded(
                        child: Padding(
                          padding: const EdgeInsets.all(20),
                          child: Text(
                            espacio.description,
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
                      color: theme.colorScheme.onPrimary,
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
                              width: 150,
                              child: TextButton(
                                onPressed: () {
                                  setState(() {
                                    _isHourSelected = true;
                                    selectedHour = hora;
                                  });
                                  _scrollController.animateTo(
                                      _scrollController
                                          .position.viewportDimension,
                                      duration:
                                          const Duration(milliseconds: 1000),
                                      curve: Curves.easeInOut);
                                },
                                style: ButtonStyle(
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
                    visible: _isDaySelected,
                    child: Text(
                      'Fecha elegida: ${selectedDay?.day}/${selectedDay?.month}/${selectedDay?.year}',
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
                const SizedBox(height: 20),
                Visibility(
                  visible: _isHourSelected,
                  child: ElevatedButton(
                    onPressed: () {
                      startTime =
                          '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[0].padLeft(2, '0')}:00';
                      endTime =
                          '${selectedDay?.year}-${selectedDay?.month.toString().padLeft(2, '0')}-${selectedDay?.day.toString().padLeft(2, '0')}T${selectedHour?.split(' ')[2].padLeft(2, '0')}:00';

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
                              return const MyErrorMessageDialog(
                                title: 'Error',
                                description:
                                    'Ha ocurrido un error al realizar la reserva.',
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
    );
  }
}
