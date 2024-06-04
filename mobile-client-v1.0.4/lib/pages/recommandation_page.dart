import 'package:flutter/material.dart';

class RecommendationPage extends StatelessWidget {
  final Map<String, dynamic> data;

  RecommendationPage({required this.data});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Recommendation Page'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            Text(
              'Recommendations',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 16),
            ...data.entries.map((entry) {
              return ListTile(
                title: Text('${entry.key}:'),
                subtitle: Text('${entry.value}'),
              );
            }).toList(),
          ],
        ),
      ),
    );
  }
}