import java.util.Arrays;
import java.util.Date;

public class Snippet {
    private String pathToJson;
    private String title;
    private String content;
    private String language;
    private String[] tags;
    private Date created;
    private Date modified;

    Snippet(String pathToJson, String title, String content, String language, String[] tags) {
        this.pathToJson = pathToJson;
        this.title = title;
        this.content = content;
        this.language = language;
        this.tags = tags;
        this.created = new Date();
        this.modified = new Date();

        writeSnippetToJson();
    }

    private void writeSnippetToJson() {
        JsonIO.getInstance().writeToJson(pathToJson, this);
    }

    private void onModification() {
        this.modified = new Date();
        writeSnippetToJson();
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
        onModification();
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
        onModification();
    }

    String getLanguage() {
        return language;
    }

    void setLanguage(String language) {
        this.language = language;
        onModification();
    }

    String[] getTags() {
        return tags;
    }

    void setTags(String[] tags) {
        this.tags = tags;
        onModification();
    }

    Date getCreated() {
        return created;
    }

    Date getModified() {
        return modified;
    }

    @Override
    public String toString() {
        return  "Title: " + title +
                "\nContent: " + content +
                "\nlanguage: " + language +
                "\nTags: " + Arrays.toString(tags) +
                "\nCreated: " + created +
                "\nModified: " + modified;
    }
}

