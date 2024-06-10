import 'package:DomiAffaire/pages/recommandation_page.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class ReservationPage extends StatefulWidget {
  @override
  _ReservationPageState createState() => _ReservationPageState();
}

class _ReservationPageState extends State<ReservationPage> {
  final _formKey = GlobalKey<FormState>();

  final TextEditingController _nbrPlacesController = TextEditingController();
  final TextEditingController _durationController = TextEditingController();
  final TextEditingController _startDateController = TextEditingController();
  final TextEditingController _numDaysController = TextEditingController();
  final TextEditingController _hourBeginingController = TextEditingController();
  final TextEditingController _hourEndingController = TextEditingController();
  final TextEditingController _titleController = TextEditingController();

  List<String> _selectedEquipments = [];
  String? _nature;

  final List<String> _equipments = [
    'Projecteur',
    'Tableau',
    'Ordinateur Portable',
    'Conférence Vidéo'
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Page de Réservation'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nbrPlacesController,
                decoration: InputDecoration(labelText: 'Nombre de Places'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer le nombre de places';
                  }
                  return null;
                },
              ),
              DropdownButtonFormField<String>(
                decoration: InputDecoration(labelText: 'Équipements Requis'),
                items: _equipments.map((String equipment) {
                  return DropdownMenuItem<String>(
                    value: equipment,
                    child: Text(equipment),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    if (newValue != null &&
                        !_selectedEquipments.contains(newValue)) {
                      _selectedEquipments.add(newValue);
                    }
                  });
                },
                validator: (value) {
                  if (_selectedEquipments.isEmpty) {
                    return 'Veuillez sélectionner au moins un équipement';
                  }
                  return null;
                },
              ),
              Wrap(
                spacing: 8.0,
                children: _selectedEquipments
                    .map((e) => Chip(
                          label: Text(e),
                          onDeleted: () {
                            setState(() {
                              _selectedEquipments.remove(e);
                            });
                          },
                        ))
                    .toList(),
              ),
              TextFormField(
                controller: _durationController,
                decoration: InputDecoration(labelText: 'Durée (en heures)'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer la durée';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _startDateController,
                decoration:
                    InputDecoration(labelText: 'Date de Début (à partir de)'),
                readOnly: true,
                onTap: () async {
                  DateTime? pickedDate = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(2000),
                    lastDate: DateTime(2101),
                  );
                  if (pickedDate != null) {
                    setState(() {
                      _startDateController.text =
                          pickedDate.toIso8601String().split('T')[0];
                    });
                  }
                },
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer la date de début';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _numDaysController,
                decoration: InputDecoration(labelText: 'Nombre de Jours'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer le nombre de jours';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _hourBeginingController,
                decoration:
                    InputDecoration(labelText: 'Heure de Début Suggérée'),
                readOnly: true,
                onTap: () async {
                  TimeOfDay? pickedTime = await showTimePicker(
                    context: context,
                    initialTime: TimeOfDay(hour: 9, minute: 0),
                    builder: (BuildContext context, Widget? child) {
                      return MediaQuery(
                        data: MediaQuery.of(context)
                            .copyWith(alwaysUse24HourFormat: true),
                        child: child!,
                      );
                    },
                  );
                  if (pickedTime != null) {
                    setState(() {
                      _hourBeginingController.text = pickedTime.hour.toString();
                    });
                  }
                },
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Veuillez entrer l'heure de début";
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _hourEndingController,
                decoration: InputDecoration(labelText: 'Heure de Fin Suggérée'),
                readOnly: true,
                onTap: () async {
                  TimeOfDay? pickedTime = await showTimePicker(
                    context: context,
                    initialTime: TimeOfDay(hour: 17, minute: 0),
                    builder: (BuildContext context, Widget? child) {
                      return MediaQuery(
                        data: MediaQuery.of(context)
                            .copyWith(alwaysUse24HourFormat: true),
                        child: child!,
                      );
                    },
                  );
                  if (pickedTime != null) {
                    setState(() {
                      _hourEndingController.text = pickedTime.hour.toString();
                    });
                  }
                },
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Veuillez entrer l'heure de fin";
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _titleController,
                decoration:
                    InputDecoration(labelText: 'Titre de la Réservation'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer un titre';
                  }
                  return null;
                },
              ),
              Column(
                children: [
                  CheckboxListTile(
                    title: Text("Réunion d'Équipe"),
                    value: _nature == 'Team Meeting',
                    onChanged: (bool? value) {
                      if (value == true) {
                        setState(() {
                          _nature = 'Team Meeting';
                        });
                      }
                    },
                  ),
                  CheckboxListTile(
                    title: Text('Session de Formation'),
                    value: _nature == 'Training Session',
                    onChanged: (bool? value) {
                      if (value == true) {
                        setState(() {
                          _nature = 'Training Session';
                        });
                      }
                    },
                  ),
                ],
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submitReservation,
                style: ElevatedButton.styleFrom(
                  foregroundColor: Colors.white,
                  backgroundColor: Colors.orange,
                ),
                child: Text('Soumettre'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _submitReservation() async {
    if (_formKey.currentState?.validate() ?? false) {
      final reservationData = {
        'nbrPlaces': int.parse(_nbrPlacesController.text),
        'required_equipments': _selectedEquipments.join(','),
        'duration': int.parse(_durationController.text),
        'start_date': _startDateController.text,
        'num_days': int.parse(_numDaysController.text),
        'hourBeginingSuggested': int.parse(_hourBeginingController.text),
        'hourEndingSuggested': int.parse(_hourEndingController.text),
        'nature': _nature,
        'title': _titleController.text,
      };

      try {
        final prefs = await SharedPreferences.getInstance();
        final token = prefs.getString('auth_token') ?? '';
        final response = await http.post(
          Uri.parse('http://192.168.1.133:5000/recommend'),
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer $token',
          },
          body: jsonEncode(reservationData),
        );
        if (response.statusCode == 200) {
          final responseData = jsonDecode(response.body);
          Fluttertoast.showToast(
            msg: "Réservation soumise avec succès !",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            backgroundColor: Colors.green,
            textColor: Colors.white,
            fontSize: 16.0,
          );

          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => RecommendationPage(data: responseData),
            ),
          );
        } else {
          Fluttertoast.showToast(
            msg:
                "Échec de la soumission de la réservation : ${response.reasonPhrase}",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            backgroundColor: Colors.red,
            textColor: Colors.white,
            fontSize: 16.0,
          );
        }
      } catch (e) {
        Fluttertoast.showToast(
          msg: "Erreur: $e",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          backgroundColor: Colors.red,
          textColor: Colors.white,
          fontSize: 16.0,
        );
      }
    }
  }
}
