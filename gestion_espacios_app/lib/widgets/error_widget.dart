/// Alejandro Sánchez Monzón
/// Mireya Sánchez Pinzón
/// Rubén García-Redondo Marín

import 'package:flutter/material.dart';

/// Widget que muestra un diálogo de error.
class MyErrorMessageDialog extends StatelessWidget {
  /// El título del diálogo.
  final String title;

  /// La descripción del diálogo.
  final String description;

  const MyErrorMessageDialog({
    Key? key,
    required this.title,
    required this.description,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    /// Se obtiene el tema actual.
    var theme = Theme.of(context);

    return AlertDialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20),
      ),
      backgroundColor: theme.colorScheme.error,
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(
            Icons.warning_rounded,
            size: 60,
            color: theme.colorScheme.onError,
          ),
          const SizedBox(height: 10.0),
          Text(
            title,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: theme.colorScheme.onError,
              fontSize: 20.0,
              fontFamily: 'KoHo',
            ),
          ),
          const SizedBox(height: 10.0),
          Text(
            description,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: theme.colorScheme.onError,
              fontSize: 16.0,
              fontFamily: 'KoHo',
            ),
          ),
        ],
      ),
    );
  }
}
