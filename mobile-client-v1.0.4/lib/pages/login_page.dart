import 'package:DomiAffaire/components/my_button.dart';
import 'package:DomiAffaire/components/my_textfield.dart';
import 'package:DomiAffaire/pages/auth_page.dart';
import 'package:DomiAffaire/services/auth_service.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatelessWidget {
  final AuthService authService = AuthService();
  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  LoginPage({Key? key}) : super(key: key);

void signUserIn(BuildContext context) async {
  String email = emailController.text;
  String password = passwordController.text;
  try {
    Map<String, dynamic> userData = await authService.login(email, password);
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('auth_token', userData['jwt']);
      await prefs.setString('userRole', userData['userRole']);
      await prefs.setString('user_email', email);

    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(
        builder: (context) => AuthPage(username: email), // Pass the email as the username
      ),
      (Route<dynamic> route) => false,
    );
  } catch (e) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Erreur de connexion"),
          content: Text("Identifiants incorrects. Veuillez réessayer."),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text("OK"),
            ),
          ],
        );
      },
    );
  }
}


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      body: SafeArea(
        child: SingleChildScrollView(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const SizedBox(height: 20),
                Image.asset('lib/images/domi.png', width: 70, height: 70),
                const SizedBox(height: 30),
                Text(
                  'Bienvenue de retour, vous avez été manqué !',
                  style: TextStyle(color: Colors.grey[700], fontSize: 16),
                ),
                const SizedBox(height: 20),
                MyTextField(
                  controller: emailController,
                  hintText: 'Email',
                  obscureText: false,
                ),
                const SizedBox(height: 7),
                MyTextField(
                  controller: passwordController,
                  hintText: 'Mot de passe',
                  obscureText: true,
                ),
                const SizedBox(height: 20),
                MyButton(onTap: () => signUserIn(context)),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
