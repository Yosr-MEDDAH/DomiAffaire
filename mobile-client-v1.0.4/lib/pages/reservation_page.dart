import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:hello/pages/recommandation_page.dart';

class ReservationPage extends StatefulWidget {
  @override
  _ReservationPageState createState() => _ReservationPageState();
}

class _ReservationPageState extends State<ReservationPage> {
  final _formKey = GlobalKey<FormState>();

  // Form field controllers
  final TextEditingController _nbrPlacesController = TextEditingController();
  final TextEditingController _durationController = TextEditingController();
  final TextEditingController _startDateController = TextEditingController();
  final TextEditingController _numDaysController = TextEditingController();
  final TextEditingController _hourBeginingController = TextEditingController();
  final TextEditingController _hourEndingController = TextEditingController();
  final TextEditingController _titleController = TextEditingController();

  // Form field variables
  List<String> _selectedEquipments = [];
  String? _nature;

  final List<String> _equipments = ['Datashow', 'Board', 'Laptop', 'Projector'];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Reservation Page'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nbrPlacesController,
                decoration: InputDecoration(labelText: 'Number of Places'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter the number of places';
                  }
                  return null;
                },
              ),
              DropdownButtonFormField<String>(
                decoration: InputDecoration(labelText: 'Required Equipments'),
                items: _equipments.map((String equipment) {
                  return DropdownMenuItem<String>(
                    value: equipment,
                    child: Text(equipment),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    if (newValue != null && !_selectedEquipments.contains(newValue)) {
                      _selectedEquipments.add(newValue);
                    }
                  });
                },
                 validator: (value) {
                  if (_selectedEquipments.isEmpty) {
                    return 'Please select at least one equipment';
                  }
                  return null;
                },
              ),
              Wrap(
                spacing: 8.0,
                children: _selectedEquipments.map((e) => Chip(
                  label: Text(e),
                  onDeleted: () {
                    setState(() {
                      _selectedEquipments.remove(e);
                    });
                  },
                )).toList(),
              ),
              TextFormField(
                controller: _durationController,
                decoration: InputDecoration(labelText: 'Duration (hours)'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter the duration';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _startDateController,
                decoration: InputDecoration(labelText: 'Start Date'),
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
                      _startDateController.text = pickedDate.toIso8601String().split('T')[0];
                    });
                  }
                },
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter the start date';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _numDaysController,
                decoration: InputDecoration(labelText: 'Number of Days'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter the number of days';
                  }
                  return null;
                },
              ),
                TextFormField(
                controller: _hourBeginingController,
                decoration: InputDecoration(labelText: 'Hour Beginning Suggested'),
                readOnly: true,
                onTap: () async {
                  TimeOfDay? pickedTime = await showTimePicker(
                    context: context,
                    initialTime: TimeOfDay(hour: 9, minute: 0),
                    builder: (BuildContext context, Widget? child) {
                      return MediaQuery(
                        data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
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
                    return 'Please enter the hour beginning';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _hourEndingController,
                decoration: InputDecoration(labelText: 'Hour Ending Suggested'),
                readOnly: true,
                onTap: () async {
                  TimeOfDay? pickedTime = await showTimePicker(
                    context: context,
                    initialTime: TimeOfDay(hour: 17, minute: 0),
                    builder: (BuildContext context, Widget? child) {
                      return MediaQuery(
                        data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
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
                    return 'Please enter the hour ending';
                  }
                  return null;
                },
              ),
              TextFormField(
                controller: _titleController,
                decoration: InputDecoration(labelText: 'Title'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a title';
                  }
                  return null;
                },
              ),
              Column(
                children: [
                  CheckboxListTile(
                    title: Text('Team Meeting'),
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
                    title: Text('Training Session'),
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
                  backgroundColor: Colors.orange, // Text color
                ),
                child: Text('Submit'),
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
      print ('Reservation Data $reservationData');
      print (jsonEncode(reservationData));

      try {
        final prefs = await SharedPreferences.getInstance();
        final token = prefs.getString('auth_token') ?? '';
        print (token);

        final response = await http.post(
          Uri.parse('http://localhost:5000/recommend'),
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer $token',
          },
          body: jsonEncode(reservationData),
        );
print("response 9bal el 200");
print(response);
        if (response.statusCode == 200) {
          print("from status === 200");
          print(response);
          final responseData = jsonDecode(response.body);
          Fluttertoast.showToast(
            msg: "Reservation submitted successfully!",
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
          print("from else === 200");
          print(response);
          Fluttertoast.showToast(
            msg: "Failed to submit reservation: ${response.reasonPhrase}",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            backgroundColor: Colors.red,
            textColor: Colors.white,
            fontSize: 16.0,
          );
        }
      } catch (e) {
        print("catch");
        print(e);
        Fluttertoast.showToast(
          msg: "Error: $e",
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


