package com.example;

import com.example.interfaces.DataManager;
import com.example.interfaces.User;
import com.example.models.Professor;
import com.example.models.Student;
import com.example.exceptions.ValidationException;
import com.example.exceptions.PersistenceException;
import com.example.managers.JSONManager;
import com.example.managers.StreamManager;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static DataManager dataManager;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initializare DataManager bazat pe argumentele din linia de comandă
        initializeDataManager(args);

        try {
            dataManager.loadData();
        } catch (PersistenceException e) {
            System.out.println("Eroare la încărcarea datelor: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim().toLowerCase();

            try {
                switch (choice) {
                    case "1":
                        addStudent();
                        break;
                    case "2":
                        removeStudent();
                        break;
                    case "3":
                        addProfessor();
                        break;
                    case "4":
                        removeProfessor();
                        break;
                    case "5":
                        showAllUsers();
                        break;
                    case "6":
                        showStudentsList();
                        break;
                    case "7":
                        showProfessorsList();
                        break;
                    case "8":
                        showStudentsAverage();
                        break;
                    case "9":
                        showStudentsRanking();
                        break;
                    case "10":
                        professorLogin();
                        break;
                    case "11":
                        studentLogin();
                        break;
                    case "exit":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (ValidationException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (PersistenceException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }

        try {
            dataManager.saveData();
            System.out.println("Data saved successfully!");
        } catch (PersistenceException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
        
        scanner.close();
    }

    private static void initializeDataManager(String[] args) {

        if (args.length > 0 && args[0].equals("json")) {
            dataManager = new JSONManager();
        } else {
            dataManager = new StreamManager();
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Add Professor");
        System.out.println("4. Delete Professor");
        System.out.println("5. View all users");
        System.out.println("6. View students list");
        System.out.println("7. View professors list");
        System.out.println("8. View students average");
        System.out.println("9. View students ranking");
        System.out.println("10. Professor login");
        System.out.println("11. Student login");
        System.out.println("exit. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addStudent() throws ValidationException, PersistenceException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone (optional, press Enter to skip): ");
        String phone = scanner.nextLine();
        if (phone.isEmpty()) phone = null;
        
        System.out.print("Year (1-5): ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new ValidationException("The year must be an integer between 1 and 5");
        }

        // Colectăm notele într-o listă temporară
        List<Integer> grades = new ArrayList<>();
        System.out.println("Enter the student's grades (1-10). Press Enter without entering a value to finish.");
        while (true) {
            System.out.print("Grade (or Enter to finish): ");
            String gradeInput = scanner.nextLine();
            
            if (gradeInput.isEmpty()) {
                break;
            }
            
            try {
                int grade = Integer.parseInt(gradeInput);
                if (grade < 1 || grade > 10) {
                    System.out.println("The grade must be between 1 and 10");
                    continue;
                }
                if (grades.size() >= 10) {
                    System.out.println("Cannot add more than 10 grades");
                    break;
                }
                grades.add(grade);
                System.out.println("Grade added successfully!");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number between 1 and 10");
            }
        }

        // Creăm studentul cu toate datele colectate
        Student student = new Student(name, email, phone, year, grades);
        dataManager.addUser(student);
        System.out.println("Student added successfully!");
        System.out.println("Student grades: " + student.getGrade());
    }

    private static void addProfessor() throws ValidationException, PersistenceException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone (optional, press Enter to skip): ");
        String phone = scanner.nextLine();
        if (phone.isEmpty()) phone = null;
        
        System.out.print("Subject: ");
        String subject = scanner.nextLine();

        System.out.print("Score (50-900): ");
        int score;
        try {
            score = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new ValidationException("The score must be an integer between 50 and 900");
        }
        
        Professor professor = new Professor(name, email, phone, subject, score);
        dataManager.addUser(professor);
        System.out.println("Professor added successfully!");
    }

    private static void removeStudent() {
        System.out.print("Enter the email of the student to delete: ");
        String email = scanner.nextLine();
        dataManager.removeUser(email);
        System.out.println("Operation completed!");
    }

    private static void removeProfessor() {
        System.out.print("Enter the email of the professor to delete: ");
        String email = scanner.nextLine();
        dataManager.removeUser(email);
        System.out.println("Operation completed!");
    }

    private static void showAllUsers() {
        System.out.println("\n=== Users list ===");
        boolean first = true;
        for (User user : dataManager.getAllUsers()) {
            if (!first) {
                System.out.println();
            } else {
                first = false;
            }

            if (user.getType().equals("Student")) {
                Student student = (Student) user;
                System.out.printf("Student: %s, Email: %s, Year: %d%n", 
                    student.getName(), student.getEmail(), student.getYear());
                System.out.println("Grades: " + student.getGrade());
                System.out.println(student.getPromotionStatus());
            } else {
                Professor professor = (Professor) user;
                System.out.printf("Professor: %s, Email: %s, Subject: %s, %s%n", 
                    professor.getName(), professor.getEmail(), professor.getSubject(), professor.getScore());
                System.out.println(professor.getUpgradeStatus());
            }
        }
    }

    private static void showStudentsAverage() {
        System.out.println("\n=== Students average ===");
        List<Student> students = dataManager.getAllUsers().stream()
                .filter(user -> user.getType().equals("Student"))
                .map(user -> (Student) user)
                .collect(Collectors.toList());

        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        double totalAverage = students.stream()
                .mapToDouble(Student::getAverageGrade)
                .average()
                .orElse(0.0);

        System.out.printf("Total average of all students: %.2f%n", totalAverage);
        
        // Afișăm și detalii pentru fiecare student
        System.out.println("\nStudent details:");
        for (Student student : students) {
            System.out.printf("%s - %s%n", 
                student.getName(), 
                student.getGrade());
        }
    }

    private static void showStudentsRanking() {
        System.out.println("\n=== Students ranking ===");
        List<Student> students = dataManager.getAllUsers().stream()
                .filter(user -> user.getType().equals("Student"))
                .map(user -> (Student) user)
                .sorted((s1, s2) -> Double.compare(s2.getAverageGrade(), s1.getAverageGrade()))
                .collect(Collectors.toList());

        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        // Afișăm clasamentul
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.printf("%d. %s - %.2f%n", 
                i + 1, 
                student.getName(), 
                student.getAverageGrade());
        }
    }

    private static void showStudentsList() {
        System.out.println("\n=== Students list ===");
        boolean first = true;
        for (User user : dataManager.getAllUsers()) {
            if (user.getType().equals("Student")) {
                if (!first) {
                    System.out.println();
                } else {
                    first = false;
                }
                
                Student student = (Student) user;
                System.out.printf("Student: %s, Email: %s, An: %d%n", 
                    student.getName(), student.getEmail(), student.getYear());
                System.out.println("Note: " + student.getGrade());
                System.out.println(student.getPromotionStatus());
            }
        }
        if (first) {  // Dacă first este încă true, înseamnă că nu s-a găsit niciun student
            System.out.println("No students in the system.");
        }
    }

    private static void showProfessorsList() {
        System.out.println("\n=== Professors list ===");
        boolean first = true;
        for (User user : dataManager.getAllUsers()) {
            if (user.getType().equals("Professor")) {
                if (!first) {
                    System.out.println();
                } else {
                    first = false;
                }
                
                Professor professor = (Professor) user;
                System.out.printf("Professor: %s, Email: %s, Subject: %s, %s%n", 
                    professor.getName(), professor.getEmail(), professor.getSubject(), professor.getScore());
                System.out.println(professor.getUpgradeStatus());
            }
        }
        if (first) {  // Dacă first este încă true, înseamnă că nu s-a găsit niciun profesor
            System.out.println("No professors in the system.");
        }
    }

    private static void professorLogin() throws ValidationException, PersistenceException {
        System.out.print("Professor email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Verificăm dacă există profesorul și dacă parola este corectă
        User user = dataManager.getAllUsers().stream()
                .filter(u -> u.getType().equals("Professor") && u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Professor not found!");
            return;
        }

        if (!password.equals("1234")) {
            System.out.println("Incorrect password!");
            return;
        }

        Professor professor = (Professor) user;
        System.out.println("Authentication successful! Welcome, " + professor.getName());
        showProfessorMenu(professor);
    }

    private static void showProfessorMenu(Professor professor) throws ValidationException, PersistenceException {
        while (true) {
            System.out.println("\n=== Professor menu ===");
            System.out.println("1. View students list");
            System.out.println("2. Add student grade");
            System.out.println("3. Back to main menu - log out");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showStudentsForProfessor();
                    break;
                case "2":
                    addGradeToStudent(professor);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void showStudentsForProfessor() {
        System.out.println("\n=== Students list ===");
        dataManager.getAllUsers().stream()
                .filter(user -> user.getType().equals("Student"))
                .map(user -> (Student) user)
                .forEach(student -> System.out.printf("Student: %s, Email: %s, Year: %d, %s%n",
                    student.getName(),
                    student.getEmail(),
                    student.getYear(),
                    student.getGrade()));
    }

    private static void addGradeToStudent(Professor professor) throws ValidationException, PersistenceException {
        System.out.print("Enter the student's email: ");
        String studentEmail = scanner.nextLine();

        User user = dataManager.getAllUsers().stream()
                .filter(u -> u.getType().equals("Student") && u.getEmail().equals(studentEmail))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Student not found!");
            return;
        }

        Student student = (Student) user;
        System.out.printf("Add grade for student %s%n", student.getName());
        System.out.print("Enter the grade (1-10): ");
        
        try {
            int grade = Integer.parseInt(scanner.nextLine());
            student.addGrade(grade);
            dataManager.saveData(); // Salvăm modificările
            System.out.println("Grade added successfully!");
            System.out.println("Updated grades: " + student.getGrade());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void studentLogin() throws ValidationException, PersistenceException {
        System.out.print("Student email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = dataManager.getAllUsers().stream()
                .filter(u -> u.getType().equals("Student") && u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Student not found!");
            return;
        }

        if (!password.equals("9876")) {
            System.out.println("Incorrect password!");
            return;
        }

        Student student = (Student) user;
        System.out.println("Authentication successful! Welcome, " + student.getName());
        showStudentMenu(student);
    }

    private static void showStudentMenu(Student student) throws ValidationException, PersistenceException {
        while (true) {
            System.out.println("\n=== Student menu ===");
            System.out.println("1. View professors list");
            System.out.println("2. Add points to professor");
            System.out.println("3. Back to main menu - log out");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showProfessorsForStudent();
                    break;
                case "2":
                    addPointsToProfessor(student);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void showProfessorsForStudent() {
        System.out.println("\n=== Professors list ===");
        dataManager.getAllUsers().stream()
                .filter(user -> user.getType().equals("Professor"))
                .map(user -> (Professor) user)
                .forEach(professor -> System.out.printf("Professor: %s, Email: %s, Subject: %s, Current level: %s%n",
                    professor.getName(),
                    professor.getEmail(),
                    professor.getSubject(),
                    professor.getAcademicLevel()));
    }

    private static void addPointsToProfessor(Student student) throws ValidationException, PersistenceException {
        System.out.print("Enter professor's email: ");
        String profEmail = scanner.nextLine();

        User user = dataManager.getAllUsers().stream()
                .filter(u -> u.getType().equals("Professor") && u.getEmail().equals(profEmail))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Professor not found!");
            return;
        }

        Professor professor = (Professor) user;
        System.out.println("Selected professor: " + professor.getName());
        System.out.println("Current score: " + professor.getScore());
        
        System.out.print("How many points to add (1-3): ");
        try {
            int points = Integer.parseInt(scanner.nextLine());
            if (points < 1 || points > 3) {
                System.out.println("You can only add 1-3 points!");
                return;
            }

            int newScore = professor.getScore() + points;
            if (newScore > 900) {
                System.out.println("Cannot exceed maximum score of 900!");
                return;
            }

            professor.addPoints(points);  // Trebuie să adăugăm această metodă în clasa Professor
            dataManager.saveData();
            
            System.out.println("Points added successfully!");
            System.out.println("New score: " + professor.getScore());
            System.out.println("New level: " + professor.getAcademicLevel());
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
} 