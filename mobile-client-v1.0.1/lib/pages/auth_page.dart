import 'package:flutter/material.dart';

class AuthPage extends StatelessWidget {
  final String username;

  const AuthPage({Key? key, required this.username}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Authentification réussie'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.check_circle,
              color: Colors.green,
              size: 100,
            ),
            SizedBox(height: 20),
            Text(
              'Bienvenue, $username !',
              style: TextStyle(fontSize: 20),
            ),
          ],
        ),
      ),
    );
  }
}
