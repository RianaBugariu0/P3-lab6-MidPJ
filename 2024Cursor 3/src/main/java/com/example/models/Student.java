package com.example.models;

import com.example.interfaces.User;
import com.example.interfaces.Promovable;
import com.example.exceptions.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements User, Serializable, Promovable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String phone;
    private int year;
    private List<Integer> grades;

    public Student(String name, String email, String phone, int year, List<Integer> grades) throws ValidationException {
        validateData(name, email, phone, year);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.year = year;
        this.grades = new ArrayList<>(grades);
    }

    public Student(String name, String email, String phone, int year) throws ValidationException {
        this(name, email, phone, year, new ArrayList<>());
    }

    private void validateData(String name, String email, String phone, int year) throws ValidationException {
        if (name == null || name.length() < 5) {
            throw new ValidationException("The name must be at least 5 characters long");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email");
        }
        if (phone != null && (!phone.matches("\\d+") || phone.length() > 10)) {
            throw new ValidationException("The phone number must contain a maximum of 10 digits");
        }
        if (year < 1 || year > 5) {
            throw new ValidationException("The year must be between 1 and 5");
        }
    }

    public void addGrade(int grade) throws ValidationException {
        if (grade < 1 || grade > 10) {
            throw new ValidationException("The grade must be between 1 and 10");
        }
        if (grades.size() >= 10) {
            throw new ValidationException("Cannot add more than 10 grades");
        }
        grades.add(grade);
    }

    public List<Integer> getGrades() {
        return new ArrayList<>(grades);
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String getGrade() {
        if (grades.isEmpty()) {
            return "No grades";
        }
        return String.format("Average: %.2f, Grades: %s", getAverageGrade(), grades.toString());
    }

    @Override
    public String getSubject() {
        return "N/A";  // Students don't have a subject associated with them
    }

    @Override
    public String getName() { return name; }
    @Override
    public String getEmail() { return email; }
    @Override
    public String getPhone() { return phone; }
    @Override
    public String getType() { return "Student"; }
    
    public int getYear() { return year; }

    @Override
    public boolean isPromoted() {
        return getAverageGrade() >= 5.0;
    }

    @Override
    public String getPromotionStatus() {
        if (grades.isEmpty()) {
            return "Status: No grades for evaluation";
        }
        return String.format("Status: %s (Average: %.2f)", 
            isPromoted() ? "Promoted" : "Failed",
            getAverageGrade());
    }
} 