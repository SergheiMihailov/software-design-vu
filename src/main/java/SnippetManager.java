import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class SnippetManager {
    private String pathToSnippoJson;
    private HashMap<Integer, Snippet> snippets;
    private Gson g = new GsonBuilder().setPrettyPrinting().create();

    SnippetManager(String pathToSnippoJson) {
        this.pathToSnippoJson = pathToSnippoJson;
        snippets = loadSnippetsFromJson();
    }

    String listAll() {
        StringBuilder response = new StringBuilder("All snippets: \n");
        for (Integer snippetId: snippets.keySet()) {
            response.append("Id: "+snippetId+"\n").append(snippets.get(snippetId).toString()).append("\n");
        }

        return response.toString();
    }

    void deleteAll() {
        snippets.clear();
        writeSnippetsToJson(snippets);
    }

    void create(String title, String content, String language, String[] tags) {
        snippets.put(getNextId(), new Snippet(title, content, language, tags));
        writeSnippetsToJson(snippets);
    }

    String read(Integer id) {
        return snippets.get(id).toString();
    }

    void update(Integer id, String title, String content, String language, String[] tags) {}

    void delete(Integer id) {
        snippets.remove(id);
        writeSnippetsToJson(snippets);
    }

    void filter() {}

    private int getNextId() {
        if (snippets.isEmpty()) {
            return 1;
        } else {
            return Collections.max(snippets.keySet())+1;
        }
    }

    private HashMap<Integer, Snippet> loadSnippetsFromJson() {
        StringBuilder loadedJson = new StringBuilder();
        try {
            File snippoFile = new File(pathToSnippoJson);
            Scanner scanner = new Scanner(snippoFile);
            while (scanner.hasNextLine()) {
                loadedJson.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            onError(e);
        }

        return g.fromJson(loadedJson.toString(), new TypeToken<HashMap<Integer, Snippet>>() {
        }.getType());
    }

    private void writeSnippetsToJson(HashMap<Integer, Snippet> snippets) {
        // Make sure the snippo.json file is present
        try {
            File snippoFile = new File(pathToSnippoJson);
            if (snippoFile.createNewFile()) {
                System.out.println("File created: " + snippoFile.getName());
            }
        } catch (IOException e) {
            onError(e);
        }

        // Overwrite the contents of the snippo.json file with the current state.
        try (PrintWriter out = new PrintWriter(pathToSnippoJson)) {
            out.println(g.toJson(snippets, new TypeToken<HashMap<Integer, Snippet>>() {
            }.getType()));
        } catch (FileNotFoundException e) {
            onError(e);
        }
    }

    void onError(Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
