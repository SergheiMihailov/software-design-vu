import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class JsonIO {

    private Gson gson;

    JsonIO () {
        gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").setPrettyPrinting().create();
    }

    private static final JsonIO INSTANCE = new JsonIO();

    static JsonIO getInstance() {
        return INSTANCE;
    }

    void writeToJson(String pathToJson, Object object) {
        try (PrintWriter out = new PrintWriter(pathToJson)) {
            out.println(gson.toJson(object, new TypeToken<Object>() {
            }.getType()));
        } catch (FileNotFoundException e) {
            onException(e);
        }
    }

    Snippet loadSnippetFromJson(String pathToJson) {
        try {
            return gson.fromJson(new FileReader(pathToJson), Snippet.class);
        } catch (FileNotFoundException ex) {
            onException(ex);
            return null;
        }
    }

    List<Snippet> parseSnippetsFromGistsApi(String allSnippetsData) {
        List<Snippet> parsedSnippets = new ArrayList<Snippet>();

        JsonArray gistArray = gson.fromJson(allSnippetsData, JsonArray.class);

        for (int i = 0; i < gistArray.size(); i++) {
            String gistUrl = gistArray.get(i).getAsJsonObject().get("id").getAsString();
            Snippet fetchedSnippet = GistsApi.getInstance().getSpecificSnippet(gistUrl);
            parsedSnippets.add(fetchedSnippet);
        }

        return parsedSnippets;
    }

    Snippet parseSnippetAttributesFromGistsApi(String snippetData) {
        JsonObject completeGistObject = gson.fromJson(snippetData, JsonObject.class);
        JsonObject relevantGistData = completeGistObject
                .get("files").getAsJsonObject() // Gist object contains files
                .entrySet().iterator().next() // But we only need one file, the first one
                .getValue().getAsJsonObject(); // This json object contains all the relevant data


        Snippet parsedSnippet = new Snippet(
                null,
                getAsStringIncludingNull(relevantGistData, "filename"),
                getAsStringIncludingNull(relevantGistData, "content"),
                getAsStringIncludingNull(relevantGistData, "language"),
                getAsStringIncludingNull(completeGistObject, "id"),
                new String[]{}
                );
        try {
            parsedSnippet.setCreatedDate(new SimpleDateFormat("yyyy-mm-dd")
                    .parse(
                            getAsStringIncludingNull(completeGistObject, "created_at")
                    ));
            parsedSnippet.setModifiedDate(new SimpleDateFormat("yyyy-mm-dd")
                    .parse(
                            getAsStringIncludingNull(completeGistObject, "updated_at")
                    ));
        } catch (Exception e) {
            onException(e);
        }
        return parsedSnippet;
    }

    String snippetToGistsCompatibleObject(Snippet snippet) {
        JsonObject gistsCompatibleObject = new JsonObject();
        JsonObject snippetObject = new JsonObject();
        JsonObject contentObject = new JsonObject();

        contentObject.addProperty("content", snippet.getContent());
        snippetObject.add(snippet.getTitle(), contentObject);
        gistsCompatibleObject.add("files", snippetObject);

        return gson.toJson(gistsCompatibleObject);
    }

    private String getAsStringIncludingNull(JsonObject obj, String attribute) {
        return obj.get(attribute).isJsonNull() ? null : obj.get(attribute).getAsString();
    }

    private void onException(Exception e) {
        System.err.println(e);
        e.printStackTrace();
    }
}
