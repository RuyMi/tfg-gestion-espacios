import 'package:flutter/material.dart';

class MyUserImageWidget extends StatelessWidget {
  final String? image;

  const MyUserImageWidget({
    super.key,
    required this.image,
  });

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);

    try {
      return Image.network(
        image != null && image != ''
            ? 'http://magarcia.asuscomm.com:25546/users/storage/$image.png'
            : 'assets/images/profile_pic.png',
        width: 100,
        height: 100,
        fit: BoxFit.cover,
        loadingBuilder: (context, child, loadingProgress) {
          if (loadingProgress == null) return child;
          return SizedBox(
            height: 100,
            width: 100,
            child: Center(
              child: CircularProgressIndicator.adaptive(
                valueColor:
                    AlwaysStoppedAnimation<Color>(theme.colorScheme.surface),
                value: loadingProgress.expectedTotalBytes != null
                    ? loadingProgress.cumulativeBytesLoaded /
                        loadingProgress.expectedTotalBytes!
                    : null,
              ),
            ),
          );
        },
        errorBuilder: (context, exception, stackTrace) {
          return Image.asset(
            'assets/images/profile_pic.png',
            width: 100,
            height: 100,
            fit: BoxFit.cover,
          );
        },
      );
    } catch (e) {
      return Image.asset(
        'assets/images/profile_pic.png',
        width: 100,
        height: 100,
        fit: BoxFit.cover,
      );
    }
  }

  Widget getImageWidget(BuildContext context) {
    try {
      return Image.network(
        image != null && image != ''
            ? 'http://magarcia.asuscomm.com:25546/users/storage/$image.png'
            : 'assets/images/profile_pic.png',
        width: 100,
        height: 100,
        fit: BoxFit.cover,
      );
    } catch (e) {
      return Image.asset(
        'assets/images/profile_pic.png',
        width: 100,
        height: 100,
        fit: BoxFit.cover,
      );
    }
  }
}
