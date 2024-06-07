import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
class RecommendationPage extends StatefulWidget {
  final Map<String, dynamic> data;

  RecommendationPage({required this.data});

  @override
  _RecommendationPageState createState() => _RecommendationPageState();
}

class _RecommendationPageState extends State<RecommendationPage> {
  bool reservationConfirmed = false;
  List<dynamic> modifiedRecommendations = [];
  bool isLoading = true;
  bool noRecommendations = false;

  @override
  void initState() {
    super.initState();
    modifyRecommendations();
  }

  Future<void> modifyRecommendations() async {
    final List<dynamic> originalRecommendations =
        widget.data['recommended_rooms'];

    List<dynamic> newRecommendations = [];
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token') ?? '';
    for (var room in originalRecommendations) {
      final roomId = room['room_id'];

      final response = await http.get(
        Uri.parse('http://192.168.1.133:8080/api/users/rooms/$roomId'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final roomDetails = jsonDecode(response.body);
        newRecommendations.add({
          'room_id': roomId,
          'name': roomDetails['name'],
          'nbrPlaces': roomDetails['nbrPlaces'],
          'equipments': roomDetails['equipments'],
          'slots': room['slots'],
        });
      }
    }

    setState(() {
      modifiedRecommendations = newRecommendations;
      isLoading = false;
      noRecommendations = newRecommendations.isEmpty;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Page de Recommandation'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (reservationConfirmed)
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.yellow[100],
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: EdgeInsets.all(12),
                  child: Row(
                    children: [
                      Icon(
                        Icons.warning,
                        color: Colors.yellow[800],
                        size: 24,
                      ),
                      SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          "Veuillez patienter pour la confirmation de l'administration par email.",
                          style: TextStyle(
                              color: Colors.yellow[800], fontSize: 16),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            isLoading
                ? Center(child: CircularProgressIndicator())
                : noRecommendations
                    ? Padding(
                        padding: const EdgeInsets.symmetric(vertical: 16.0),
                        child: Card(
                          color: Colors.red[100],
                          elevation: 3,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Row(
                              children: [
                                Icon(
                                  Icons.warning,
                                  color: Colors.red[800],
                                  size: 24,
                                ),
                                SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    "Aucune recommandation disponible pour la date et l'heure désirées.",
                                    style: TextStyle(
                                        fontSize: 18, color: Colors.red[800]),
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      )
                    : Expanded(
                        child: ListView(
                          children: [
                            Text(
                              'Recommandations',
                              style: TextStyle(
                                  fontSize: 24, fontWeight: FontWeight.bold),
                            ),
                            SizedBox(height: 16),
                            ...modifiedRecommendations.map<Widget>((room) {
                              return Padding(
                                padding:
                                    const EdgeInsets.symmetric(vertical: 8.0),
                                child: Card(
                                  elevation: 3,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(10),
                                  ),
                                  child: InkWell(
                                    onTap: () {
                                      if (!reservationConfirmed) {
                                        showDialog(
                                          context: context,
                                          builder: (BuildContext context) {
                                            return AlertDialog(
                                              title: Text(
                                                  'Confirmer la Réservation'),
                                              content: Text(
                                                  'Voulez-vous réserver cette salle ?'),
                                              actions: [
                                                TextButton(
                                                  onPressed: () {
                                                    Navigator.of(context).pop();
                                                  },
                                                  child: Row(
                                                    children: [
                                                      Icon(Icons.cancel,
                                                          color: Colors.red),
                                                      SizedBox(width: 8),
                                                      Text('Annuler'),
                                                    ],
                                                  ),
                                                ),
                                                TextButton(
                                                  onPressed: () async {
                                                    try {
                                                      final roomId =
                                                          room['room_id'];
                                                      final slots =
                                                          room['slots'];
                                                      final requestBody = {
                                                        'room_id': roomId,
                                                        'slots': slots,
                                                      };
                                                      final prefs =
                                                          await SharedPreferences
                                                              .getInstance();
                                                      final token =
                                                          prefs.getString(
                                                                  'auth_token') ??
                                                              '';
                                                      final response =
                                                          await http.post(
                                                        Uri.parse(
                                                            'http://192.168.1.133:8080/api/users/reservations'),
                                                        headers: {
                                                          'Content-Type':
                                                              'application/json',
                                                          'Authorization':
                                                              'Bearer $token',
                                                        },
                                                        body: jsonEncode(
                                                            requestBody),
                                                      );
                                                      print(
                                                          response.statusCode);
                                                      if (response.statusCode ==
                                                          200) {
                                                        setState(() {
                                                          reservationConfirmed =
                                                              true;
                                                        });
                                                        Fluttertoast.showToast(
                                                          msg:
                                                              'Salle réservée avec succès !',
                                                          toastLength: Toast
                                                              .LENGTH_SHORT,
                                                          gravity: ToastGravity
                                                              .BOTTOM,
                                                          backgroundColor:
                                                              Colors.green,
                                                          textColor:
                                                              Colors.white,
                                                          fontSize: 16.0,
                                                        );
                                                      } else {
                                                        Fluttertoast.showToast(
                                                          msg:
                                                              'Échec de la réservation de la salle : ${response.reasonPhrase}',
                                                          toastLength: Toast
                                                              .LENGTH_SHORT,
                                                          gravity: ToastGravity
                                                              .BOTTOM,
                                                          backgroundColor:
                                                              Colors.red,
                                                          textColor:
                                                              Colors.white,
                                                          fontSize: 16.0,
                                                        );
                                                      }
                                                    } catch (e) {
                                                      print('Error: $e');
                                                    }
                                                    Navigator.of(context).pop();
                                                  },
                                                  child: Row(
                                                    children: [
                                                      Icon(Icons.check_circle,
                                                          color: Colors.green),
                                                      SizedBox(width: 8),
                                                      Text('Confirmer'),
                                                    ],
                                                  ),
                                                ),
                                              ],
                                            );
                                          },
                                        );
                                      }
                                    },
                                    child: ListTile(
                                      title: Text(
                                        'Room: ${room['name']}',
                                        style: TextStyle(
                                            fontSize: 18,
                                            fontWeight: FontWeight.bold),
                                      ),
                                      subtitle: Padding(
                                        padding:
                                            const EdgeInsets.only(top: 8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Row(
                                              children: [
                                                Icon(Icons.people,
                                                    color: Color.fromARGB(
                                                        255, 239, 155, 95)),
                                                SizedBox(width: 8),
                                                Text(
                                                  'Nombre de places: ${room['nbrPlaces']}',
                                                  style:
                                                      TextStyle(fontSize: 16),
                                                ),
                                              ],
                                            ),
                                            SizedBox(height: 8),
                                            Column(
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.start,
                                              children: [
                                                Text(
                                                  'Équipements: ',
                                                  style: TextStyle(
                                                      fontSize: 16,
                                                      fontWeight:
                                                          FontWeight.bold),
                                                ),
                                                SizedBox(height: 8),
                                                Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: room['equipments']
                                                      .map<Widget>((equipment) {
                                                    return Padding(
                                                      padding:
                                                          const EdgeInsets.only(
                                                              top: 4.0),
                                                      child: Container(
                                                        padding:
                                                            EdgeInsets.all(6),
                                                        decoration:
                                                            BoxDecoration(
                                                          color: Colors.white,
                                                          borderRadius:
                                                              BorderRadius
                                                                  .circular(5),
                                                        ),
                                                        child: Text(
                                                          decodeUtf8(equipment),
                                                          style: TextStyle(
                                                              fontSize: 16),
                                                        ),
                                                      ),
                                                    );
                                                  }).toList(),
                                                ),
                                              ],
                                            ),
                                            Divider(),
                                            ...room['slots']
                                                .map<Widget>((slot) {
                                              return Padding(
                                                padding:
                                                    const EdgeInsets.symmetric(
                                                        vertical: 4.0),
                                                child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: [
                                                    Row(
                                                      children: [
                                                        Icon(
                                                            Icons
                                                                .calendar_today,
                                                            color:
                                                                Color.fromARGB(
                                                                    255,
                                                                    0,
                                                                    147,
                                                                    150)),
                                                        SizedBox(width: 8),
                                                        Text(
                                                          'Date: ${slot['date']}',
                                                          style: TextStyle(
                                                              fontSize: 16,
                                                              fontWeight:
                                                                  FontWeight
                                                                      .w500),
                                                        ),
                                                      ],
                                                    ),
                                                    SizedBox(height: 8),
                                                    Row(
                                                      children: [
                                                        Icon(Icons.access_time,
                                                            color: const Color
                                                                .fromARGB(255,
                                                                50, 49, 47)),
                                                        SizedBox(width: 8),
                                                        Text(
                                                          'Heure de début : ${slot['start_time']}',
                                                          style: TextStyle(
                                                              fontSize: 16),
                                                        ),
                                                      ],
                                                    ),
                                                    Row(
                                                      children: [
                                                        Icon(Icons.meeting_room,
                                                            color: const Color
                                                                .fromARGB(255,
                                                                50, 49, 47)),
                                                        SizedBox(width: 8),
                                                        Text(
                                                          'Heure de fin : ${slot['end_time']}',
                                                          style: TextStyle(
                                                              fontSize: 16),
                                                        ),
                                                      ],
                                                    ),
                                                  ],
                                                ),
                                              );
                                            }).toList(),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                              );
                            }).toList(),
                          ],
                        ),
                      ),
          ],
        ),
      ),
    );
  }
}
String decodeUtf8(String input) {
  List<int> bytes = input.toString().codeUnits;
  return utf8.decode(bytes);
}