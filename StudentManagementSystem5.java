import java.io.*;
import java.util.*;

// ===================== Custom Exception ==========================
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

// ===================== Loader Thread (Runnable) ==================
class Loader implements Runnable {
    private String message;

    public Loader(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.print(message);
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300); // simulate delay
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            System.out.println("\nLoading interrupted.");
        }
        System.out.println(); // new line after loading
    }
}

// ===================== Abstract Person Class =====================
abstract class Person {
    protected String name;
    protected String email;

    public Person() {}

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// ===================== Student Class =============================
class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student() {}

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public double getMarks() { return marks; }
    public char getGrade() { return grade; }

    public void setMarks(double marks) {
        this.marks = marks;
        calculateGrade();
    }

    // Input details from user with basic validation
    public void inputDetails(Scanner sc) {
        rollNo = readValidRollNo(sc);

        System.out.print("Enter Name: ");
        name = readNonEmpty(sc, "Name");

        System.out.print("Enter Email: ");
        email = readNonEmpty(sc, "Email");

        System.out.print("Enter Course: ");
        course = readNonEmpty(sc, "Course");

        marks = readValidMarks(sc);

        calculateGrade();
    }

    private int readValidRollNo(Scanner sc) {
        while (true) {
            System.out.print("Enter Roll No: ");
            String line = sc.nextLine();
            try {
                int r = Integer.parseInt(line);
                if (r <= 0) {
                    throw new NumberFormatException("Roll number must be positive.");
                }
                return r;
            } catch (NumberFormatException e) {
                System.out.println("Invalid roll number. Please enter a positive integer.");
            }
        }
    }

    private String readNonEmpty(Scanner sc, String fieldName) {
        while (true) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) {
                System.out.print(fieldName + " cannot be empty. Enter " + fieldName + " again: ");
            } else {
                return line.trim();
            }
        }
    }

    private double readValidMarks(Scanner sc) {
        while (true) {
            System.out.print("Enter Marks: ");
            String line = sc.nextLine();
            try {
                double m = Double.parseDouble(line);
                if (m < 0 || m > 100) {
                    System.out.println("Marks must be between 0 and 100.");
                } else {
                    return m;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid marks. Please enter a numeric value.");
            }
        }
    }

    // Calculate grade based on marks
    public void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 80) grade = 'B';
        else if (marks >= 70) grade = 'C';
        else grade = 'D';
    }

    // Display student details
    public void displayDetails() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }

    @Override
    public void displayInfo() {
        displayDetails();
    }

    // Convert to file line
    public String toFileString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    // Parse from file line
    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;
        try {
            int roll = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            String course = parts[3];
            double marks = Double.parseDouble(parts[4]);
            return new Student(roll, name, email, course, marks);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

// ===================== RecordActions Interface ===================
interface RecordActions {
    void addStudent(Student s) throws IllegalArgumentException;
    void deleteStudent(String name) throws StudentNotFoundException;
    void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException;
    Student searchStudent(String name) throws StudentNotFoundException;
    void viewAllStudents();
}

// ===================== StudentManager Class ======================
class StudentManager implements RecordActions {

    private List<Student> students = new ArrayList<>();
    private Map<Integer, Student> studentByRoll = new HashMap<>();
    private Map<String, Student> studentByName = new HashMap<>();
    private final String FILE_NAME = "students.txt";

    public StudentManager() {
        loadFromFile();
    }

    // Load from file using BufferedReader
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // nothing to load
        }

        System.out.println("Loading students from file...");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromFileString(line);
                if (s != null) {
                    students.add(s);
                    studentByRoll.put(s.getRollNo(), s);
                    studentByName.put(s.getName().toLowerCase(), s);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading students.txt: " + e.getMessage());
        }

        if (!students.isEmpty()) {
            System.out.println("Loaded students from file:");
            for (Student s : students) {
                s.displayDetails();
                System.out.println("-----------------------");
            }
        }
    }

    // Save to file using BufferedWriter
    public void saveToFile() {
        Thread loader = new Thread(new Loader("Saving"));
        loader.start();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                bw.write(s.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("\nError writing to students.txt: " + e.getMessage());
        } finally {
            try {
                loader.join();
            } catch (InterruptedException e) {
                System.out.println("Save operation interrupted.");
            }
        }

        System.out.println("Data saved to file.");
    }

    @Override
    public void addStudent(Student s) throws IllegalArgumentException {
        if (studentByRoll.containsKey(s.getRollNo())) {
            throw new IllegalArgumentException("Duplicate roll number. Student not added.");
        }
        students.add(s);
        studentByRoll.put(s.getRollNo(), s);
        studentByName.put(s.getName().toLowerCase(), s);
        System.out.println("Student added successfully.");
    }

    @Override
    public void deleteStudent(String name) throws StudentNotFoundException {
        Student s = studentByName.get(name.toLowerCase());
        if (s == null) {
            throw new StudentNotFoundException("Student with name '" + name + "' not found.");
        }
        students.remove(s);
        studentByRoll.remove(s.getRollNo());
        studentByName.remove(name.toLowerCase());
        System.out.println("Student record deleted.");
    }

    @Override
    public void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException {
        Student s = studentByRoll.get(rollNo);
        if (s == null) {
            throw new StudentNotFoundException("Student with roll no " + rollNo + " not found.");
        }
        s.setMarks(newMarks);
        System.out.println("Student marks updated.");
    }

    @Override
    public Student searchStudent(String name) throws StudentNotFoundException {
        Student s = studentByName.get(name.toLowerCase());
        if (s == null) {
            throw new StudentNotFoundException("Student with name '" + name + "' not found.");
        }
        return s;
    }

    @Override
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            s.displayDetails();
            System.out.println("-----------------------");
        }
    }

    // Sort by marks (descending) using Comparator and display via Iterator
    public void sortByMarksAndDisplay() {
        if (students.isEmpty()) {
            System.out.println("No student records to sort.");
            return;
        }
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return Double.compare(b.getMarks(), a.getMarks()); // descending
            }
        });

        System.out.println("Sorted Student List by Marks:");
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            s.displayDetails();
            System.out.println("-----------------------");
        }
    }
}

// ===================== Main Application ==========================
public class StudentManagementSystem5 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        int choice = 0;

        do {
            System.out.println("===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number from 1 to 6.");
                continue;
            }

            try {
                switch (choice) {

                    case 1: {
                        // Multithreading simulation on adding record
                        Thread loader = new Thread(new Loader("Adding"));
                        loader.start();

                        Student s = new Student();
                        s.inputDetails(sc);

                        try {
                            loader.join();
                        } catch (InterruptedException e) {
                            System.out.println("Add operation interrupted.");
                        }

                        try {
                            manager.addStudent(s);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                        break;
                    }

                    case 2:
                        manager.viewAllStudents();
                        break;

                    case 3:
                        System.out.print("Enter name to search: ");
                        String searchName = sc.nextLine();
                        try {
                            Student found = manager.searchStudent(searchName);
                            System.out.println("Student Info:");
                            found.displayDetails();
                        } catch (StudentNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Enter name to delete: ");
                        String delName = sc.nextLine();
                        try {
                            manager.deleteStudent(delName);
                        } catch (StudentNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 5:
                        manager.sortByMarksAndDisplay();
                        break;

                    case 6:
                        manager.saveToFile();
                        System.out.println("Saved and exiting.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

        } while (choice != 6);

        sc.close();
    }
}
