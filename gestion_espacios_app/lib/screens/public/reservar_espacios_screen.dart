import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/models/espacio.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
import 'package:gestion_espacios_app/widgets/error_widget.dart';
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

// ignore: must_be_immutable
class _ReservaSala extends State<ReservaEspacioScreen> {
  bool _isDaySelected = false;
  bool _isHourSelected = false;
  DateTime? selectedDay;
  String? selectedHour;
  final ScrollController _scrollController = ScrollController();

  @override
  Widget build(BuildContext context) {
    final Espacio episodio =
        ModalRoute.of(context)!.settings.arguments as Espacio;

    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            Text(
              episodio.name,
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
                episodio.price.toString(),
                style: const TextStyle(
                  fontFamily: 'KoHo',
                  color: MyColors.pinkApp,
                  fontWeight: FontWeight.bold,
                  fontSize: 15,
                ),
              ),
              const Padding(
                padding: EdgeInsets.only(right: 8),
                child: Icon(
                  Icons.monetization_on_outlined,
                  color: MyColors.pinkApp,
                  size: 20,
                ),
              ),
            ],
          )
        ],
        backgroundColor: MyColors.whiteApp,
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
                    color: MyColors.lightBlueApp,
                    borderRadius: BorderRadius.circular(50),
                    boxShadow: [
                      BoxShadow(
                        color: MyColors.blackApp.withOpacity(0.5),
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
                        child: const CircleAvatar(
                          radius: 30,
                          backgroundImage: AssetImage(
                            'assets/images/sala_stock.jpg',
                          ),
                        ),
                      ),
                      Expanded(
                        child: Padding(
                          padding: const EdgeInsets.all(20),
                          child: Text(
                            episodio.name,
                            maxLines: 3,
                            textAlign: TextAlign.start,
                            overflow: TextOverflow.ellipsis,
                            style: const TextStyle(
                              color: MyColors.whiteApp,
                              fontFamily: 'KoHo',
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 30),
                Container(
                  width: 300,
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(30),
                    border: Border.all(
                      color: MyColors.pinkApp,
                      width: 2,
                    ),
                  ),
                  child: TableCalendar(
                    headerStyle: const HeaderStyle(
                      titleTextStyle: TextStyle(
                        color: MyColors.pinkApp,
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo',
                      ),
                      formatButtonVisible: false,
                      leftChevronIcon: Icon(
                        Icons.chevron_left,
                        color: MyColors.pinkApp,
                      ),
                      rightChevronIcon: Icon(
                        Icons.chevron_right,
                        color: MyColors.pinkApp,
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
                      selectedDecoration: const BoxDecoration(
                        color: MyColors.pinkApp,
                        shape: BoxShape.circle,
                      ),
                      selectedTextStyle: const TextStyle(
                          color: MyColors.whiteApp,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo'),
                      todayDecoration: BoxDecoration(
                        color: MyColors.blackApp.shade100,
                        shape: BoxShape.circle,
                      ),
                      todayTextStyle: const TextStyle(
                          color: MyColors.blackApp,
                          fontWeight: FontWeight.bold,
                          fontFamily: 'KoHo'),
                      weekendTextStyle: const TextStyle(
                          color: Colors.grey, fontFamily: 'KoHo'),
                    ),
                    daysOfWeekStyle: const DaysOfWeekStyle(
                      weekdayStyle: TextStyle(
                        color: MyColors.pinkApp,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo',
                      ),
                      weekendStyle: TextStyle(
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
                                        return MyColors.pinkApp
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
                                    const Icon(
                                      Icons.access_time,
                                      color: MyColors.blackApp,
                                    ),
                                    const SizedBox(width: 10),
                                    Text(
                                      hora,
                                      textAlign: TextAlign.right,
                                      style: const TextStyle(
                                        color: MyColors.blackApp,
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
                      style: const TextStyle(
                        color: MyColors.pinkApp,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo',
                      ),
                    )),
                Visibility(
                    visible: _isHourSelected,
                    child: Text(
                      'Hora elegida: $selectedHour',
                      style: const TextStyle(
                        color: MyColors.pinkApp,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo',
                      ),
                    )),
                const SizedBox(height: 20),
                Visibility(
                  visible: _isHourSelected,
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pushNamed(context, '/mis-reservas');
                      showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return const MyMessageDialog(
                              title: 'Reserva realizada',
                              description:
                                  'Se ha realizado la reserva correctamente.',
                            );
                          });
                    },
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(30),
                      ),
                      backgroundColor: MyColors.pinkApp,
                    ),
                    child: const Text('Reservar',
                        style: TextStyle(
                            color: MyColors.whiteApp, fontFamily: 'KoHo')),
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
