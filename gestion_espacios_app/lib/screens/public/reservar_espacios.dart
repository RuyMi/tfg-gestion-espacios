import 'package:flutter/material.dart';
import 'package:gestion_espacios_app/models/colors.dart';
import 'package:gestion_espacios_app/widgets/alert_widget.dart';
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

class ReservaSala extends StatefulWidget {
  const ReservaSala({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _ReservaSala createState() => _ReservaSala();
}

// ignore: must_be_immutable
class _ReservaSala extends State<ReservaSala> {
  bool _isDaySelected = false;
  bool _isHourSelected = false;
  DateTime? selectedDay;

  void _handleDaySelected() {
    setState(() {
      _isDaySelected = true;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Text(
              'Nombre de la sala',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                fontFamily: 'KoHo',
              ),
            ),
          ],
        ),
        actions: const [
          Row(
            children: [
              Text(
                '100',
                style: TextStyle(
                  fontFamily: 'KoHo',
                  color: MyColors.pinkApp,
                  fontWeight: FontWeight.bold,
                  fontSize: 15,
                ),
              ),
              Padding(
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
      ),
      body: SingleChildScrollView(
        scrollDirection: Axis.vertical,
        child: Center(
          child: Column(
            children: [
              Image.asset('assets/images/image_placeholder.png',
                  width: 150, height: 150, fit: BoxFit.cover),
              const SizedBox(height: 20),
              const Text(
                'Descripción de la sala',
                maxLines: 3,
                textAlign: TextAlign.center,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontFamily: 'KoHo',
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
                  firstDay: DateTime.now().subtract(const Duration(days: 365)),
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
                      color: MyColors.blackApp.shade200,
                      shape: BoxShape.circle,
                    ),
                    todayTextStyle: const TextStyle(
                        color: MyColors.blackApp,
                        fontWeight: FontWeight.bold,
                        fontFamily: 'KoHo'),
                    weekendTextStyle:
                        const TextStyle(color: Colors.grey, fontFamily: 'KoHo'),
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
                    setState(() {
                      _isDaySelected = true;
                      this.selectedDay = selectedDay;
                    });
                  },
                ),
              ),
              const SizedBox(height: 20),
              Visibility(
                visible: _isDaySelected,
                child: ListView.builder(
                  itemCount: horas.length,
                  itemBuilder: (BuildContext context, int index) {
                    return GestureDetector(
                      onTap: () {
                        _isHourSelected = true;
                      },
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 8.0),
                        child: Text(
                          horas[index],
                          style: const TextStyle(
                              fontSize: 16.0,
                              fontFamily: 'KoHo',
                              fontWeight: FontWeight.bold),
                        ),
                      ),
                    );
                  },
                ),
              ),
              const SizedBox(height: 20),
              Visibility(
                visible: _isHourSelected,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pushNamed(context, '/home');
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
    );
  }
}
