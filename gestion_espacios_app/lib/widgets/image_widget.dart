import 'package:flutter/material.dart';

class MyImageWidget extends StatelessWidget {
  final String? image;

  const MyImageWidget({
    super.key,
    required this.image,
  });

  @override
  Widget build(BuildContext context) {
    return Image.network(
      image != null && image != ''
          ? 'http://magarcia.asuscomm.com:25546/spaces/storage/$image.png'
          : 'assets/images/image_placeholder.png',
      width: 100,
      height: 100,
      fit: BoxFit.cover,
      loadingBuilder: (context, child, loadingProgress) {
        if (loadingProgress == null) return child;
        return const SizedBox(
          height: 100,
          width: 100,
          child: Center(
            child: CircularProgressIndicator.adaptive(
                // value: loadingProgress.expectedTotalBytes != null
                //     ? loadingProgress.cumulativeBytesLoaded /
                //         loadingProgress.expectedTotalBytes!
                //     : null,
                ),
          ),
        );
      },
      errorBuilder: (context, exception, stackTrace) {
        return Image.asset(
          'assets/images/image_placeholder.png',
          width: 100,
          height: 100,
          fit: BoxFit.cover,
        );
      },
    );
  }
}
