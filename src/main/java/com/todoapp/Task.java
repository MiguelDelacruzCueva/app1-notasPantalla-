package main.java.com.todoapp;

import java.time.LocalDateTime;

public class Task {
    private String id;
    private String title;
    private boolean completed;
    private String color;
    private LocalDateTime createdAt;

    public Task(String title, String color) {
        this.id = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.color = color;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
