import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class SnippetManager {

    private String pathToSnippoDir;
    private HashMap<Integer, Snippet> snippets = new HashMap<>();

    SnippetManager(String pathToSnippoDir) {
        this.pathToSnippoDir = pathToSnippoDir;
        loadSnippetsFromLocal();
        if (GistsApi.getUsesGithubGists()) {
            syncWithGithubGistsAtStartup();
        }
    }

    private void loadSnippetsFromLocal() {
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

    private void syncWithGithubGistsAtStartup() {
        // Step B. GET Gists that are not in the local directory
        System.out.println("Getting snippets from Gists that are not on local...");
        List<Snippet> loadedSnippets = GistsApi.getInstance().getAllSnippets();
        List<String> localSnippetsIds = new ArrayList<String>();

        for (Snippet snippet : this.snippets.values()) {
            localSnippetsIds.add(snippet.getGistsId());
        }

        List<Snippet> snippetsNotOnLocal = loadedSnippets.stream()
                .filter(snippet -> !localSnippetsIds.contains(snippet.getGistsId()))
                .collect(Collectors.toList());

        for (final Snippet snippet : snippetsNotOnLocal) {
            Integer snippetId = getNextId();
            snippet.setPathToJson(generatePathToSnippetJson(snippetId));
            this.snippets.put(snippetId, snippet);
        }

        // Step C. PATCH (take the version that is latest) for each snippet present on both Gists and local
        //         E.g. if last modification was on Gists, the Gist version will be taken.
        //         Otherwise take the local version.
        //         the conflict is resolved at the snippet level
        //         It made sense to sync all of them as filtering would add too much complexity and most snippets
        //         are present on both Gists and local

        System.out.println("Syncing local and Gists...");
        this.snippets.values().forEach(Snippet::syncWithGithubGists);

    }

    private String generatePathToSnippetJson(Integer snippetId) {
        return pathToSnippoDir + "/" + snippetId;
    }

    String listSnippets(Map<Integer, Snippet> snippetsToList) {
        StringBuilder response = new StringBuilder("All snippetsToList: \n");

        for (Integer snippetId: snippetsToList.keySet()) {
            response.append("Id: "+snippetId+"; ").append(snippetsToList.get(snippetId).toString()).append("\n");
        }

        return response.toString();
    }

    String listAll() {
        return listSnippets(snippets);
    }

    Integer create(String title, String content, String language, String[] tags) {
        Integer newSnippetId = getNextId();
        Snippet newSnippet =
                new Snippet(generatePathToSnippetJson(newSnippetId), title, content, language, null, tags);
        snippets.put(newSnippetId, newSnippet);
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
            if (new File(generatePathToSnippetJson(id)).delete()) {
                GistsApi.getInstance().deleteSpecificSnippet(snippets.get(id).getGistsId());
                snippets.remove(id);
            } else {
                System.out.println("Cannot delete the snippet and its file. Id: " + id);
            }
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

    Map<Integer, Snippet> filter(String wordToContain, String language, String[] tags) {
        return snippets.entrySet()
                .stream()
                .filter(
                        entry -> entry.getValue().getTitle().contains(wordToContain) ||
                                entry.getValue().getContent().contains(wordToContain)
                ) // Match words
                .filter(entry -> language.equals("") ||
                        entry.getValue().getLanguage().equals(language)
                ) // Match language
                .filter(entry -> (tags.length == 1 && tags[0].equals("")) ||
                        Arrays.asList(entry.getValue().getTags()).containsAll(Arrays.asList(tags))
                ) // Match tags
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
