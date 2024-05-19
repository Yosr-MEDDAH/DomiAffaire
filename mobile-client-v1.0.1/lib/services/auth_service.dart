import 'package:http/http.dart' as http;
import 'dart:convert';
class AuthService {
  Future<Map<String, dynamic>> login(String email, String password) async {
    final Uri url = Uri.parse('http://localhost:8080/api/auth/login'); // Replace with your actual backend URL

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
      // Successful login
      return json.decode(response.body);
    } else {
      // Handle error
      throw Exception('Failed to login');
    }
  }
}

