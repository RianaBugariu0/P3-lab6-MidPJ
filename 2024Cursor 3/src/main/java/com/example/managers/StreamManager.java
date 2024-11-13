package com.example.managers;

import com.example.interfaces.DataManager;
import com.example.interfaces.User;
import com.example.exceptions.PersistenceException;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StreamManager implements DataManager {
    
    private Set<User> users;
    private static final String FILE_PATH = "users.bin";

    public StreamManager() {
        users = new HashSet<>();
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
        return new HashSet<>(users);
    }

    @Override
    public void loadData() throws PersistenceException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            users = new HashSet<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            @SuppressWarnings("unchecked")
            Set<User> loadedUsers = (Set<User>) obj;
            users = loadedUsers;
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Fișierul nu a fost găsit", e);
        } catch (IOException e) {
            throw new PersistenceException("Eroare la încărcarea datelor din fișierul binar", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException("Eroare la deserializarea datelor", e);
        }
    }

    @Override
    public void saveData() throws PersistenceException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Fișierul nu a putut fi creat", e);
        } catch (IOException e) {
            throw new PersistenceException("Eroare la salvarea datelor în fișierul binar", e);
        }
    }
} 