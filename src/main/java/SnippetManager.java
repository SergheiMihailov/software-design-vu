import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class SnippetManager {
    private String pathToSnippoDir;
    private HashMap<Integer, Snippet> snippets = new HashMap<>();

    SnippetManager(String pathToSnippoDir) {
        this.pathToSnippoDir = pathToSnippoDir;
        // Make sure the Snippo folder is present
        File snippoFolder = new File(pathToSnippoDir);
        if (snippoFolder.mkdir()) {
            System.out.println("Folder created: " + snippoFolder.getName());
        }

        loadSnippets(snippoFolder) ;
    }

    private void loadSnippets(File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                Integer snippetId = Integer.parseInt(fileEntry.getName());
                Snippet loadedSnippet = JsonIO.getInstance().loadFromJson(getPathToSnippetJson(snippetId));
                snippets.put(snippetId, loadedSnippet);
            }
        }
    }

    private String getPathToSnippetJson(Integer snippetId) {
        return pathToSnippoDir + "/" + snippetId;
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

    Integer create(String title, String content, String language, String[] tags) {
        Integer newSnippetId = getNextId();
        snippets.put(
                newSnippetId,
                new Snippet(getPathToSnippetJson(newSnippetId), title, content, language, tags)
        );

        return newSnippetId;
    }

    String read(Integer id) {
        if (snippets.containsKey(id)) {
            return snippets.get(id).toString();
        } else {
            return "Snippet not found.";
        }
    }

    void delete(Integer id) {
        snippets.remove(id);
    }

    void edit(Integer id) {
        Snippet snippetToEdit = snippets.get(id);
        new Editor(snippetToEdit);
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

    // Returns the next available Id for a new snippet
    private int getNextId() {
        return snippets.isEmpty() ?
                1 : Collections.max(snippets.keySet())+1;
    }
}
