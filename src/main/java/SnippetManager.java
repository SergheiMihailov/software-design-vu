import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class SnippetManager {
    private String pathToSnippoJson;
    private HashMap<Integer, Snippet> snippets;
    private Gson g = new GsonBuilder().setPrettyPrinting().create();

    SnippetManager(String pathToSnippoJson) {
        this.pathToSnippoJson = pathToSnippoJson;

        // Make sure the snippo.json file is present
        try {
            File snippoFile = new File(pathToSnippoJson);
            if (snippoFile.createNewFile()) {
                System.out.println("File created: " + snippoFile.getName());
            }
        } catch (IOException e) {
            onException(e);
        }
        snippets = loadSnippetsFromJson() ;
    }

    private String listSnippets(Map<Integer, Snippet> snippetsToList) {
        StringBuilder response = new StringBuilder("All snippetsToList: \n");

        for (Integer snippetId: snippetsToList.keySet()) {
            response.append("Id: "+snippetId+"\n").append(snippetsToList.get(snippetId).toString()).append("\n");
        }

        return response.toString();
    }

    String listAll() {
        return listSnippets(snippets);
    }

    void deleteAll() {
        snippets.clear();

        writeSnippetsToJson();
    }

    Integer create(String title, String content, String language, String[] tags) {
        Integer newSnippetId = getNextId();
        snippets.put(
                newSnippetId,
                new Snippet(title, content, language, tags)
        );

        writeSnippetsToJson();

        return newSnippetId;
    }

    String read(Integer id) {
        if (snippets.containsKey(id)) {
            return snippets.get(id).toString();
        } else {
            return "Snippet not found.";
        }
    }

    void update(Integer id, Snippet snippet) {
        snippets.put(id, snippet);

        writeSnippetsToJson();
    }

    void delete(Integer id) {
        snippets.remove(id);

        writeSnippetsToJson();
    }

    synchronized void edit(Integer id) {
        Snippet snippetToEdit = snippets.get(id);
        new Editor(snippetToEdit, this);

        writeSnippetsToJson();
    }

    String filter(String wordToContain, String language, String[] tags) {
        return listSnippets(
                snippets.entrySet()
                .stream()
                .filter(
                        entry -> entry.getValue().getTitle().contains(wordToContain) ||
                                entry.getValue().getContent().contains(wordToContain)
                )
                .filter(entry -> language.equals("") ||
                        entry.getValue().getLanguage().equals(language))
                .filter(entry -> (tags.length == 1 && tags[0].equals("")) ||
                        Arrays.asList(entry.getValue().getTags()).containsAll(Arrays.asList(tags)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

     String search(String searchTerm){
         return listSnippets(
                 snippets.entrySet()
                         .stream()
                         .filter(
                                 entry -> entry.getValue().getTitle().contains(searchTerm) ||
                                         entry.getValue().getContent().contains(searchTerm)
                         )
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
         );
    }

    // Returns the next available Id for a new snippet
    private int getNextId() {
        return snippets.isEmpty() ?
                1 : Collections.max(snippets.keySet())+1;
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
            onException(e);
        }

        if (loadedJson.length() == 0) {
            return new HashMap<>();
        } else {
            return g.fromJson(loadedJson.toString(), new TypeToken<HashMap<Integer, Snippet>>() {}.getType());
        }
    }

    public void writeSnippetsToJson() {
        // Overwrite the contents of the snippo.json file with the current state.
        try (PrintWriter out = new PrintWriter(pathToSnippoJson)) {
            out.println(g.toJson(snippets, new TypeToken<HashMap<Integer, Snippet>>() {
            }.getType()));
        } catch (FileNotFoundException e) {
            onException(e);
        }
    }

    void onException(Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
