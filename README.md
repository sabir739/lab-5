📘 Java Lab Assignment 5
Multithreaded Student Record Management System with File Handling, OOP, Sorting & Exception Handling
📌 Project Overview

This project implements a complete Student Record Management System in Java with the following capabilities:

Add, update, delete, search, and view student records

Persistent storage using students.txt

Multithreading to simulate loading effects during long operations

Exception Handling with custom exception StudentNotFoundException

Collections Framework for storing and managing students

Sorting students by marks (descending) using Comparator

Iterator for displaying students

OOP Principles: abstraction, inheritance, interfaces

Input Validation and safe error handling

This assignment combines all major Java programming concepts into one capstone project.

🧱 Features Implemented
✔ 1. Object-Oriented Design

Class Hierarchy:

🟦 Person (abstract class)

Fields: name, email

Abstract method: displayInfo()

🟩 Student (extends Person)

Fields: rollNo, course, marks, grade

Methods:

inputDetails()

displayDetails()

calculateGrade()

🟧 RecordActions (interface)

Defines:

addStudent()
deleteStudent()
updateStudent()
searchStudent()
viewAllStudents()

🟪 StudentManager (implements RecordActions)

Maintains:

List<Student>

Map<Integer, Student>

Map<String, Student>

Handles:

File loading/saving

Sorting

Searching & deleting by name

🟥 Loader (implements Runnable)

Simulates loading using multithreading during:

Add student

Save file

✔ 2. Exception Handling

The system handles:

Invalid roll numbers

Empty name/email/course fields

Invalid marks

Incorrect menu choices

StudentNotFoundException (custom exception)

Used with try–catch–finally for safe execution.

✔ 3. File Handling (Persistent Storage)

The system automatically:

🔹 Loads students.txt on startup

Using:

BufferedReader

Each line mapped to a Student object

🔹 Saves data back to file on exit

Using:

BufferedWriter

Each student saved in CSV format:

rollNo,name,email,course,marks

🔹 File auto-created if it doesn’t exist.
✔ 4. Multithreading

Loader class simulates realistic loading animation:

Loading.....
Saving.....
Adding.....


Used to enhance user experience.

✔ 5. Collections API
✔ List<Student>

Stores all student records.

✔ Map<Integer, Student>

Fast access by roll number (duplicate prevention).

✔ Map<String, Student>

Fast search & delete by name (case-insensitive).

✔ Iterator

Used in viewAllStudents() and sorted display.

✔ 6. Sorting Using Comparator

Sorts students by marks (descending):

students.sort((a, b) -> Double.compare(b.getMarks(), a.getMarks()));

📂 Project Structure
StudentManagementSystem5.java
students.txt   (auto created on first save)
README.md      (this file)

▶️ How to Run the Program
1. Compile
javac StudentManagementSystem5.java

2. Run
java StudentManagementSystem5

3. File Used

Automatically reads/writes:

students.txt

📤 Sample Output
Adding a Student
Enter Roll No: 101
Enter Name: Rahul
Enter Email: rahul@mail.com
Enter Course: B.Tech
Enter Marks: 85.0
Student added successfully.

Viewing Students
Roll No: 101
Name: Rahul
Email: rahul@mail.com
Course: B.Tech
Marks: 85.0

Searching by Name
Enter name to search: Rahul
Student Info:
Roll No: 101
Name: Rahul
Email: rahul@mail.com
Course: B.Tech
Marks: 85.0

Sorting by Marks
Sorted Student List by Marks:
Roll No: 101
Name: Rahul
Marks: 85.0

🎯 Learning Outcomes

By completing this assignment, students will learn to:

Build OOP-based applications with inheritance & interfaces

Apply exception handling for safe execution

Use file I/O to persist data

Manage records using Collections (List, Map)

Sort and display using Comparator & Iterator

Use multithreading to improve responsiveness

Write modular, maintainable Java code

🧑‍💻 Author

Sabir Ali
Java Programming Lab — Assignment 5
