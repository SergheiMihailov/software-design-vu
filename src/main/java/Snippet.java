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

        if (content == null) {
            edit();
        }
    }

    void clone(Snippet snippetToClone) {
        this.pathToJson = snippetToClone.pathToJson;
        this.title = snippetToClone.title;
        this.content = snippetToClone.content;
        this.language = snippetToClone.language;
        this.gistsId = snippetToClone.gistsId;
        this.tags = snippetToClone.tags;
        this.createdDate = snippetToClone.createdDate;
        this.modifiedDate = snippetToClone.modifiedDate;

        onModification();
    }

    void edit() {
        new Editor(this);
    }

    void writeSnippetToJson() {
        JsonIO.getInstance().writeToJson(pathToJson, this);
    }

    private void onModification() {
        this.modifiedDate = new Date();
        if (this.pathToJson != null) writeSnippetToJson();
        if (GistsApi.getUsesGithubGists()) syncWithGithubGists();
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

    private void postSnippetToGithubGists() {
        this.gistsId = GistsApi.getInstance().postSpecificSnippet(this);
    }

    private void patchSnippetToGithubGists() {
        GistsApi.getInstance().patchSpecificSnippet(this);
    }

    private void pullSnippetFromGithubGists() {
        String savedPathToJson = pathToJson;
        String[] savedTags = tags.clone();
        this.clone(GistsApi.getInstance().getSpecificSnippet(this.gistsId));
        this.tags = savedTags;
        this.pathToJson = savedPathToJson;
        writeSnippetToJson();
    }

    public void setPathToJson(String pathToJson) {
        this.pathToJson = pathToJson;
        onModification();
    }

    String getTitle() {
        return title == null ? "" : title;
    }

    void setTitle(String title) {
        this.title = title;
        onModification();
    }

    String getContent() {
        return content == null ? "" : content;
    }

    void setContent(String content) {
        this.content = content;
        onModification();
    }

    String getLanguage() {
        return language == null ? "" : language;
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
        return  "\u001B[35mPathToJson: \u001B[0m" + pathToJson +
                "; \u001B[35mTitle: \u001B[0m" + title +
                "; \u001B[35mlanguage: \u001B[0m" + language +
                "; \u001B[35mgistsId: \u001B[0m" + gistsId +
                "; \u001B[35mTags: \u001B[0m" + Arrays.toString(tags) +
                "\n\u001B[35mContent: \n\u001B[0m" + content +
                "\n\u001B[35mCreated: \u001B[0m" + createdDate +
                "; \u001B[35mModified: \u001B[0m" + modifiedDate +
                "\n_______________________________________________";
    }
}

