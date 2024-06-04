import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  Future<Map<String, dynamic>> login(String email, String password) async {
    final Uri url = Uri.parse('http://localhost:8080/api/auth/login');

    final response = await http.post(
      url,
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'email': email,
        'pwd': password,
      }),
    );
    if (response.statusCode == 200) {
      final responseBody = json.decode(response.body);
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('auth_token', responseBody['jwt']);
      await prefs.setString('userRole', responseBody['userRole']);
      await prefs.setString('user_email', email);

      return {
        'jwt': responseBody['jwt'],
        'userRole': responseBody['userRole'],
        'user_email': email,
      };
    } else {
      throw Exception('Failed to login');
    }
  }
}
