import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GistsApi {
    public static final String GITHUB_API_URL = "https://api.github.com/gists";
    private HttpClient httpClient;

    private String authorization = "";
    private boolean usesGithub = true;

    GistsApi () {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    private static final GistsApi INSTANCE = new GistsApi();

    static GistsApi getInstance() {
        return INSTANCE;
    }

    static void setAuthorization(String providedAuthorization) {
        INSTANCE.authorization = providedAuthorization;
    }

    static void setUsesGithub(boolean providedUsesGithub) {
        INSTANCE.usesGithub = providedUsesGithub;
    }

    static boolean getUsesGithubGists() {
        return INSTANCE.usesGithub;
    }

    List<Snippet> getAllSnippets() {
        HttpRequest request = buildHttpRequest("GET", GITHUB_API_URL, null);
        String responseBody = sendHttpRequest(request).body();
        return JsonIO.getInstance().parseSnippetsFromGistsApi(responseBody);
    }

    Snippet getSpecificSnippet(String gistsId) {
        HttpRequest request = buildHttpRequest("GET", GITHUB_API_URL+"/"+gistsId, null);
        String responseBody = sendHttpRequest(request).body();
        return JsonIO.getInstance().parseSnippetAttributesFromGistsApi(responseBody);
    }

    String postSpecificSnippet(Snippet snippet) {
        String gistsCompatibleObject = JsonIO.getInstance().snippetToGistsCompatibleObject(snippet);
        HttpRequest request = buildHttpRequest("POST", GITHUB_API_URL , gistsCompatibleObject);
        String responseBody = sendHttpRequest(request).body();
        return JsonIO.getInstance().parseSnippetAttributesFromGistsApi(responseBody).getGistsId();
    }

    void patchSpecificSnippet(Snippet snippet) {
        String gistsCompatibleObject = JsonIO.getInstance().snippetToGistsCompatibleObject(snippet);
        HttpRequest request = buildHttpRequest("PATCH", GITHUB_API_URL + "/" + snippet.getGistsId(), gistsCompatibleObject);
    }

    void deleteSpecificSnippet(String gistId) {
        HttpRequest request = buildHttpRequest("DELETE", GITHUB_API_URL + "/" + gistId, null);
    }

    private HttpResponse<String> sendHttpRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(Exception e) {
            onException(e);
            return null;
        }
    }

    private HttpRequest buildHttpRequest(String method, String url, String body) {
        HttpRequest.Builder request = HttpRequest.newBuilder();
        switch (method) {
            case "GET": request.GET(); break;
            case "POST": request.POST(HttpRequest.BodyPublishers.ofString(body)); break;
            case "PATCH": request.method("PATCH", HttpRequest.BodyPublishers.ofString(body)); break;
            case "DELETE": request.DELETE(); break;
            default: break;
        }

        return request.uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .setHeader("Authorization", authorization)
                .build();
    }

    private void onException(Exception e) {
        System.err.println(e.toString());
        e.printStackTrace();
    }
}
