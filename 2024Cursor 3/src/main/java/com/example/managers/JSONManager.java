package com.example.managers;

import com.example.interfaces.DataManager;
import com.example.interfaces.User;
import com.example.exceptions.PersistenceException;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JSONManager implements DataManager {
    private List<User> users;
    private static final String FILE_PATH = "users.json";

    public JSONManager() {
        users = new ArrayList<>();
    }

    @Override
    public void addUser(User user) throws PersistenceException {
        users.add(user);
        saveData();
    }

    @Override
    public void removeUser(String email) {
        users.removeIf(user -> user.getEmail().equals(email));
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void loadData() throws PersistenceException {
        // TODO: Implementare cu Gson după adăugarea dependinței Maven
        System.out.println("WARNING: JSON persistence not implemented yet. Data will not be saved.");
        users = new ArrayList<>();
    }

    @Override
    public void saveData() throws PersistenceException {
        // TODO: Implementare cu Gson după adăugarea dependinței Maven
        System.out.println("WARNING: JSON persistence not implemented yet. Data will not be saved.");
    }
} 