package com.example.models;

import com.example.interfaces.User;
import com.example.interfaces.Upgratable;
import com.example.exceptions.ValidationException;
import java.io.Serializable;

public class Professor implements User, Serializable, Upgratable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private int score;

    public Professor(String name, String email, String phone, String subject, int score) throws ValidationException {
        validateData(name, email, phone, subject, score);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.score = score;
    }

    private void validateData(String name, String email, String phone, String subject, int score) throws ValidationException {
        if (name == null || name.length() < 5) {
            throw new ValidationException("The name must be at least 5 characters long");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Email invalid");
        }
        if (phone != null && (!phone.matches("\\d+") || phone.length() > 10)) {
            throw new ValidationException("The phone number must contain a maximum of 10 digits");
        }
        if (subject == null || subject.length() < 5 || subject.length() > 20) {
            throw new ValidationException("The subject must be between 5 and 20 characters long");
        }
        if (score < 50 || score > 900) {
            throw new ValidationException("The score must be between 50 and 900");
        }
    }

    // Implementarea interfetei User 
    @Override
    public String getName() { return name; }
    @Override
    public String getEmail() { return email; }
    @Override
    public String getPhone() { return phone; }
    @Override
    public String getType() { return "Professor"; }
    
    public String getSubject() { return subject; }
    
    public int getScore() { return score; }

    @Override
    public String getAcademicLevel() {
        if (score >= 700) {
            return "PhD";
        } else if (score >= 500) {
            return "Profesor";
        } else {
            return "Asistent profesor";
        }
    }

    @Override
    public boolean isEligibleForUpgrade() {
        return score >= 700;
    }

    @Override
    public String getUpgradeStatus() {
        return String.format("Current level: %s (Score: %d)%s", 
            getAcademicLevel(), 
            score,
            isEligibleForUpgrade() ? " - Eligible for PhD" : "");
    }

    @Override
    public String getGrade() {
        return "N/A";
    }

    public void addPoints(int points) throws ValidationException {
        if (points < 1 || points > 3) {
            throw new ValidationException("Can only add 1-3 points at a time");
        }
        if (score + points > 900) {
            throw new ValidationException("Cannot exceed maximum score of 900");
        }
        score += points;
    }
} 