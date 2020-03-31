import javax.swing.*;
import java.util.Arrays;
import java.util.Date;

public class Snippet {
    private String pathToJson;
    private String title;
    private String content;
    private String language;
    private String gistsId;
    private String[] tags;
    private Date createdDate;
    private Date modifiedDate;

    Snippet(String pathToJson, String title, String content, String language, String gistsId, String[] tags) {
        this.pathToJson = pathToJson;
        this.title = title;
        this.content = content;
        this.language = language;
        this.gistsId = gistsId;
        this.tags = tags;
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }

    void clone (Snippet snippetToClone) {
        this.pathToJson = snippetToClone.pathToJson;
        this.title = snippetToClone.title;
        this.content = snippetToClone.content;
        this.language = snippetToClone.language;
        this.gistsId = snippetToClone.gistsId;
        this.tags = snippetToClone.tags;
        this.createdDate = snippetToClone.createdDate;
        this.modifiedDate = snippetToClone.modifiedDate;
    }

    void edit() {
        new Editor(this);
    }

    void writeSnippetToJson() {
        JsonIO.getInstance().writeToJson(pathToJson, this);
    }

    private void onModification() {
        this.modifiedDate = new Date();
        writeSnippetToJson();
        if (GistsApi.getUsesGithubGists()) patchSnippetToGithubGists();
    }

    void syncWithGithubGists() {
        if (this.gistsId == null) {
            postSnippetToGithubGists();
            return;
        }

        Date gistsModifiedDate = GistsApi.getInstance().getSpecificSnippet(this.gistsId).getModified();

        if (this.modifiedDate.after(gistsModifiedDate)) {
            patchSnippetToGithubGists();
        } else {
            pullSnippetFromGithubGists();
        }
    }

    void postSnippetToGithubGists() {
        this.gistsId = GistsApi.getInstance().postSpecificSnippet(this);
    }

    void patchSnippetToGithubGists() {
        GistsApi.getInstance().patchSpecificSnippet(this);
    }

    void pullSnippetFromGithubGists() {
        String savedPathToJson = pathToJson;
        this.clone(GistsApi.getInstance().getSpecificSnippet(this.gistsId));
        this.pathToJson = savedPathToJson;
        writeSnippetToJson();
    }

    public void setPathToJson(String pathToJson) {
        this.pathToJson = pathToJson;
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

    String getGistsId() {
        return gistsId;
    }

    String[] getTags() {
        return tags;
    }

    void setTags(String[] tags) {
        this.tags = tags;
        onModification();
    }

    Date getCreated() {
        return createdDate;
    }

    Date getModified() {
        return modifiedDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return  "PathToJson: " + pathToJson +
                "\nTitle: " + title +
                "\nContent: " + content +
                "\nlanguage: " + language +
                "\ngistsId: " + gistsId +
                "\nTags: " + Arrays.toString(tags) +
                "\nCreated: " + createdDate +
                "\nModified: " + modifiedDate;
    }
}

