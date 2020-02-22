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
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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

