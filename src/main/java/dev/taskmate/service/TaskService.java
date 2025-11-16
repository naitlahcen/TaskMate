package dev.taskmate.service;

import dev.taskmate.model.Task;
import dev.taskmate.repo.TaskRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRepository repo;
    private final List<Task> tasks;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
        this.tasks = new ArrayList<>(repo.load());
    }

    // ---- Create ----
    public Task add(String title, LocalDate dueDate, Task.Priority priority) {
        int id = nextId();
        Task t = new Task(id, title, dueDate, priority);
        tasks.add(t);
        persist();
        return t;
    }
    // ---- Read/List ----
    public List<Task> list(boolean onlyOpen, Comparator<Task> sortBy) {
        return tasks.stream()
                .filter(t -> !onlyOpen || !t.isDone())
                .sorted(sortBy != null ? sortBy : Comparator.comparingInt(Task::getId))
                .collect(Collectors.toList());
    }

    // ---- Update ----
    public boolean markDone(int id) {
        Task t = byId(id);
        if (t == null) return false;
        t.setDone(true);
        persist();
        return true;
    }

    public boolean updateTitle(int id, String newTitle) {
        Task t = byId(id);
        if (t == null) return false;
        t.setTitle(newTitle);
        persist();
        return true;
    }

    public boolean updateDueDate(int id, LocalDate newDue) {
        Task t = byId(id);
        if (t == null) return false;
        t.setDueDate(newDue);
        persist();
        return true;
    }

    public boolean updatePriority(int id, Task.Priority prio) {
        Task t = byId(id);
        if (t == null) return false;
        t.setPriority(prio);
        persist();
        return true;
    }
    // ---- Delete ----
    public boolean delete(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) persist();
        return removed;
    }

    public int clearDone() {
        int before = tasks.size();
        tasks.removeIf(Task::isDone);
        if (tasks.size() != before) persist();
        return before - tasks.size();
    }

    // ---- Helpers ----
    public Comparator<Task> sortByDueAsc() {
        return Comparator.comparing(t -> Optional.ofNullable(t.getDueDate()).orElse(LocalDate.MAX));
    }
    public Comparator<Task> sortByPriorityDesc() {
        return Comparator.comparing(Task::getPriority).reversed();
    }

    private Task byId(int id) {
        for (Task t : tasks) if (t.getId() == id) return t;
        return null;
    }

    private int nextId() {
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    private void persist() {
        repo.save(tasks);
    }












}













