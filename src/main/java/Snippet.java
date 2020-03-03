import java.util.Arrays;
import java.util.Date;

public class Snippet {
    private String title;
    private String content;
    private String language;
    private String[] tags;
    private Date created;
    private Date modified;

    public Snippet(String title, String content, String language, String[] tags) {
        this.title = title;
        this.content = content;
        this.language = language;
        this.tags = tags;
        this.created = new Date();
        this.modified = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.modified = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.modified = new Date();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        this.modified = new Date();
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
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

