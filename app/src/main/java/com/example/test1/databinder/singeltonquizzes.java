package com.example.test1.databinder;

import com.example.test1.elapsedquizzes.modelquizzes;

import java.util.ArrayList;

public class singeltonquizzes {

        private static singeltonquizzes instance;
        // Step 2: Create the ArrayList
        private ArrayList<modelquizzes> arrayList;

        // Step 3: Make the constructor private
        private singeltonquizzes() {
            arrayList = new ArrayList<>();
        }

        // Step 4: Provide a public method to access the instance
        public static synchronized singeltonquizzes getInstance() {
            if (instance == null) {
                instance = new singeltonquizzes();
            }
            return instance;
        }

        // Method to get the ArrayList
        public ArrayList<modelquizzes> getArrayList() {
            return arrayList;
        }

        // Method to add an item to the ArrayList
        public void addItem(String name,String author,String time,String date,String completed,String marks) {
            arrayList.add(new modelquizzes(name,author,time,date,completed,marks));
        }

        // Method to clear the ArrayList
        public void clearItems() {
            arrayList.clear();
        }
    }


