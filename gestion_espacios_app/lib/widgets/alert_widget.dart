import 'package:flutter/material.dart';

class MyMessageDialog extends StatelessWidget {
  final String title;
  final String description;

  const MyMessageDialog({
    Key? key,
    required this.title,
    required this.description,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    return AlertDialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20),
        side: BorderSide(
          color: theme.colorScheme.onPrimary,
          width: 2.0,
        ),
      ),
      backgroundColor: theme.colorScheme.onBackground,
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(
            Icons.info,
            size: 60,
            color: theme.colorScheme.onPrimary,
          ),
          const SizedBox(height: 10.0),
          Text(
            title,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: theme.colorScheme.onPrimary,
              fontSize: 20.0,
              fontFamily: 'KoHo',
            ),
          ),
          const SizedBox(height: 10.0),
          Text(
            description,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: theme.colorScheme.onPrimary,
              fontSize: 16.0,
              fontFamily: 'KoHo',
            ),
          ),
        ],
      ),
    );
  }
}
