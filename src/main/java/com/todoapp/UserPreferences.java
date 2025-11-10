package main.java.com.todoapp;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class UserPreferences {
    private String nickname;
    private double windowOpacity;

    private static final String PREFS_FILE = System.getProperty("user.home") + "/.todoapp_prefs.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserPreferences() {
        this.nickname = "Usuario";
        this.windowOpacity = 0.95;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getWindowOpacity() {
        return windowOpacity;
    }

    public void setWindowOpacity(double windowOpacity) {
        this.windowOpacity = windowOpacity;
    }

    public void save() {
        try {
            String json = gson.toJson(this);
            Files.writeString(Paths.get(PREFS_FILE), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserPreferences load() {
        try {
            if (Files.exists(Paths.get(PREFS_FILE))) {
                String json = Files.readString(Paths.get(PREFS_FILE));
                return gson.fromJson(json, UserPreferences.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UserPreferences();
    }
}
