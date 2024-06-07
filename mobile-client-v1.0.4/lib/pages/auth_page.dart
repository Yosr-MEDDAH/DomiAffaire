import 'dart:convert';
import 'package:DomiAffaire/pages/login_page.dart';
import 'package:DomiAffaire/pages/reservation_page.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class AuthPage extends StatefulWidget {
  final String username;

  const AuthPage({Key? key, required this.username}) : super(key: key);

  @override
  _AuthPageState createState() => _AuthPageState();
}

class _AuthPageState extends State<AuthPage> {
  String? _token;
  String? _userRole;
  Map<String, dynamic>? _userData;

  @override
  void initState() {
    super.initState();
    _loadToken();
  }

  Future<void> _loadToken() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');
    final userRole = prefs.getString('userRole');
    final email = prefs.getString('user_email');
    if (token != null && userRole != null && email != null) {
      setState(() {
        _token = token;
        _userRole = userRole;
      });
      String endpoint = '';
      if (_userRole == 'CLIENT') {
        endpoint = 'http://192.168.1.133:8080/api/clients/$email';
      } else if (_userRole == 'ACCOUNTANT') {
        endpoint = 'http://192.168.1.133:8080/api/accountants/$email';
      }

      final userDataResponse = await http.get(
        Uri.parse(endpoint),
        headers: {
          'Authorization': 'Bearer $_token',
        },
      );
      if (userDataResponse.statusCode == 200) {
        final userData = json.decode(userDataResponse.body);
        setState(() {
          _userData = userData;
        });
      }
    }
  }

  Future<void> _logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('auth_token');
    await prefs.remove('userRole');
    await prefs.remove('user_email');
    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(builder: (context) => LoginPage()),
      (route) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Page Principale'),
        actions: [
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: _logout,
          ),
        ],
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (_userData != null && _userData!['image'] != null)
              Image.memory(
                base64Decode(_userData!['image']),
                width: 100,
                height: 100,
                fit: BoxFit.cover,
              )
            else
              Icon(Icons.check_circle, color: Colors.green, size: 100),
            SizedBox(height: 20),
            Text(
              'Bienvenue, ${_userData?['firstName'] ?? 'Utilisateur'} ${_userData?['lastName'] ?? ''} !',
              style: TextStyle(fontSize: 20),
            ),
            if (_token != null && _userRole != null) ...[
              SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  ElevatedButton(
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => ReservationPage()),
                      );
                    },
                    child: Text('Réservation'),
                  ),
                  SizedBox(width: 20),
                 
                ],
              ),
            ],
          ],
        ),
      ),
    );
  }
}
