Quiz App

A quiz application built with Android Studio, using Firebase as a backend for storing quiz data and handling user authentication. This app allows users to take custom quizzes, generate a unique code for each quiz, and securely track their progress.

⸻

Features
	•	Custom Quiz Creation: Users can create their own quizzes with various questions.
	•	Secure Quiz Completion: The quiz will be automatically closed if the user is inactive for more than 1 second.
	•	Firebase Integration: All quiz data and user profiles are securely stored using Firebase.
	•	Unique Quiz Code Generation: Users receive a unique code to perform a quiz.
	•	Responsive UI: Built with Jetpack Compose to offer a modern, smooth user experience.

⸻

Tech Stack
	•	Android Studio
	•	Firebase
	•	Firebase Firestore (for quiz data)
	•	Firebase Authentication (for user management)
	•	Gradle (for dependency management)

⸻

Getting Started

Prerequisites
	•	Android Studio (latest version)
	•	Firebase account: Set up Firebase in your project by following the Firebase Setup Guide for Android.

⸻

Installation
	1.	Clone the repository:  git clone https://github.com/SachinVarshney454/QUIZ-APP.git
 	2.	Open the project in Android Studio.
	3.	Add your Firebase credentials:
	•	Go to the Firebase Console and create a new project.
	•	Download the google-services.json file and place it in your app’s app/ folder.
	•	Follow Firebase’s guide to enable Firestore and Authentication.
	4.	Sync your project with Gradle.

Usage
	1.	Create a New Quiz: Log in with your email and create a custom quiz with multiple-choice questions.
	2.	Take a Quiz: Enter the unique quiz code to begin a quiz.
	3.	Track Progress: Your score and progress will be tracked during the quiz session.
