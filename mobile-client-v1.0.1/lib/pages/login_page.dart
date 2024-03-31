import 'package:flutter/material.dart';
import 'package:hello/components/my_button.dart';
import 'package:hello/components/my_textfield.dart';
import 'package:hello/components/square_tile.dart';
import 'package:hello/pages/auth_page.dart';
// pour pouvoir utiliser le contexte
import 'dart:ui';

class LoginPage extends StatelessWidget {
  LoginPage({Key? key});

  // contrôleurs de saisie de texte
  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  // méthode pour connecter l'utilisateur
  void signUserIn(BuildContext context) {
    // Vérifiez les identifiants (exemple simplifié)
    String email = emailController.text;
    String password = passwordController.text;
    // Vérifiez si les identifiants sont corrects (exemple simplifié)
    if (email == "user@example.com" && password == "password") {
      // Si les identifiants sont corrects, naviguez vers la page AuthPage
      Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => AuthPage(username: email),
      ),
    );
    }else {
    // Sinon, affichez un message d'erreur (exemple simplifié)
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

                // logo
                // const Icon(
                //   Icons.lock,
                //   size: 100,
                // ),
                // logo de l'entreprise
                Image.asset(
                  'lib/images/domi.png',
                  width: 70,
                  height: 70,
                ),

                const SizedBox(height: 30),

                // bienvenue de retour, vous avez été manqué !
                Text(
                  'Bienvenue de retour, vous avez été manqué !',
                  style: TextStyle(
                    color: Colors.grey[700],
                    fontSize: 16,
                  ),
                ),

                const SizedBox(height: 20),

                // champ de texte de l'e-mail
                MyTextField(
                  controller: emailController,
                  hintText: 'Email',
                  obscureText: false,
                ),

                const SizedBox(height: 7),

                // champ de texte du mot de passe
                MyTextField(
                  controller: passwordController,
                  hintText: 'Mot de passe',
                  obscureText: true,
                ),

                const SizedBox(height: 7),

                // mot de passe oublié ?
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 25.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Text(
                        'Mot de passe oublié ?',
                        style: TextStyle(color: Colors.grey[600]),
                      ),
                    ],
                  ),
                ),

                const SizedBox(height: 20),

                // bouton de connexion
                MyButton(
                  onTap: () {
                    signUserIn(
                        context); // Appel de la méthode signUserIn avec le contexte actuel
                  },
                ),
                const SizedBox(height: 30),

                // ou continuer avec
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 25.0),
                  child: Row(
                    children: [
                      Expanded(
                        child: Divider(
                          thickness: 0.5,
                          color: Colors.grey[400],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10.0),
                        child: Text(
                          'Ou continuer avec',
                          style: TextStyle(color: Colors.grey[700]),
                        ),
                      ),
                      Expanded(
                        child: Divider(
                          thickness: 0.5,
                          color: Colors.grey[400],
                        ),
                      ),
                    ],
                  ),
                ),

                const SizedBox(height: 30),

                // bouton de connexion avec Google
                const Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    // bouton Google
                    SquareTile(imagePath: 'lib/images/google.png'),
                  ],
                ),

                const SizedBox(height: 30),

                // pas encore membre ? inscrivez-vous maintenant
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      'Pas encore membre ?',
                      style: TextStyle(color: Colors.grey[700]),
                    ),
                    const SizedBox(width: 4),
                    const Text(
                      'Inscrivez-vous maintenant',
                      style: TextStyle(
                        color: Colors.blue,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
