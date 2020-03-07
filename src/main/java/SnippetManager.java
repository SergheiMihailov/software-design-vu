import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class SnippetManager {
    private String pathToSnippoDir;
    private HashMap<Integer, Snippet> snippets = new HashMap<>();

    SnippetManager(String pathToSnippoDir) {
        this.pathToSnippoDir = pathToSnippoDir;

        loadSnippets() ;
    }

    private void loadSnippets() {
        // Make sure the Snippo folder is present
        File snippoFolder = new File(pathToSnippoDir);
        if (snippoFolder.mkdir()) {
            System.out.println("Folder created: " + snippoFolder.getName());
        }

        for (final File fileEntry : Objects.requireNonNull(snippoFolder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                Integer snippetId = Integer.parseInt(fileEntry.getName());
                Snippet loadedSnippet = JsonIO.getInstance().loadSnippetFromJson(generatePathToSnippetJson(snippetId));
                snippets.put(snippetId, loadedSnippet);
            }
        }
    }

    private String generatePathToSnippetJson(Integer snippetId) {
        return pathToSnippoDir + "/" + snippetId;
    }

    String listSnippets(Map<Integer, Snippet> snippetsToList) {
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
                new Snippet(generatePathToSnippetJson(newSnippetId), title, content, language, tags)
        );

        return newSnippetId;
    }

    String read(Integer id) {
        if (isValidId(id)) {
            return snippets.get(id).toString();
        } else {
            onSnippetNotFound();
            return "";
        }
    }

    void delete(Integer id) {
        if (isValidId(id)) {
            snippets.remove(id);
        } else {
            onSnippetNotFound();
        }
    }

    void edit(Integer id) {
        if (isValidId(id)) {
            Snippet snippetToEdit = snippets.get(id);
            snippetToEdit.edit();
        } else {
            onSnippetNotFound();
        }
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

    private boolean isValidId(Integer id) {
        return snippets.containsKey(id);
    }

    private void onSnippetNotFound() {
        System.out.println("Snippet not found");
    }

    HashMap<Integer, Snippet> getSnippets() {
        return snippets;
    }
}
