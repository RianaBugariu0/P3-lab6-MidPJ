package com.example.interfaces;

import java.util.Collection;
import com.example.exceptions.ValidationException;
import com.example.exceptions.PersistenceException;

public interface DataManager {
    void addUser(User user) throws ValidationException, PersistenceException;
    void removeUser(String email);
    Collection<User> getAllUsers();
    void loadData() throws PersistenceException;
    void saveData() throws PersistenceException;
} 