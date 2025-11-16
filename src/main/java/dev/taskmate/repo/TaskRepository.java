package dev.taskmate.repo;
import dev.taskmate.model.Task;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final File file;
    private final Gson gson;



    public TaskRepository() {
        this(new File("tasks.json"));
    }

    public TaskRepository(File file) {
        this.file = file;
        this.gson = buildGson();

    }
    private Gson buildGson() {
        JsonSerializer<LocalDate> ser = (src, t, c) -> new JsonPrimitive(src.toString());


        JsonDeserializer<LocalDate> deser = (json, t, c) ->
                (json == null || json.isJsonNull()) ? null : LocalDate.parse(json.getAsString());

        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, ser)
                .registerTypeAdapter(LocalDate.class, deser)
                .setPrettyPrinting()
                .create();
    }

    public List<Task> load() {
        if (!file.exists())
            return new ArrayList<>();

        try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {

            Type listType = new TypeToken<List<Task>>() {}.getType();


            List<Task> tasks = gson.fromJson(r, listType);


            return (tasks == null) ? new ArrayList<>() : tasks;

        } catch (IOException e) {

            throw new UncheckedIOException("Failed to load " + file.getName(), e);
        }
    }

    public void save(List<Task> tasks) {

        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {

            gson.toJson(tasks, w);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save " + file.getName(), e);
        }
    }


}
