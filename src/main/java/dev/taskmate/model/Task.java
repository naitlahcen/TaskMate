package dev.taskmate.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task {



    private int id;
    private String title;
    private LocalDate dueDate;   // optional
    private Priority priority;   // default: MEDIUM
    private boolean done;
    public enum Priority { LOW, MEDIUM, HIGH }


    public Task(int id, String title, LocalDate dueDate, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        this.id = id;
        this.title = title.trim();
        this.dueDate = dueDate;
        this.priority = (priority == null) ? Priority.MEDIUM : priority;
        this.done = false;
    }


    public Task() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        this.title = title.trim();
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = (priority == null) ? Priority.MEDIUM : priority;
    }

    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }


    @Override
    public String toString() {
        return "Task{" +  "id=" + id +  ", title='" + title + '\'' +  ", dueDate=" + dueDate +  ", priority=" + priority + ", done=" + done + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Task))
            return false;
        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

