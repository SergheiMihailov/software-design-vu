import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

class JsonIO {
    private Gson g;

    JsonIO () {
        g = new GsonBuilder().setPrettyPrinting().create();
    }

    private static final JsonIO INSTANCE = new JsonIO();

    static JsonIO getInstance() {
        return INSTANCE;
    }

    void writeToJson(String pathToJson, Object object) {
        try (PrintWriter out = new PrintWriter(pathToJson)) {
            out.println(g.toJson(object, new TypeToken<Object>() {
            }.getType()));
        } catch (FileNotFoundException e) {
            onException(e);
        }
    }

    Snippet loadFromJson(String pathToJson) {
        try {
            return g.fromJson(new FileReader(pathToJson), Snippet.class);
        } catch (FileNotFoundException ex) {
            onException(ex);
            return null;
        }
    }

    private void onException(Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
