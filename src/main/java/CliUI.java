import java.util.Base64;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


class CliUI {
    private SnippetManager snippetManager;
    private boolean isOpen;
    private Scanner keyboard;

    private boolean usesGithub = false;
    private boolean usesToken = false;
    private String githubCredentials = null;

    CliUI(String pathToSnippoDir) {
        isOpen = true;
        keyboard = new Scanner(System.in);

        logIn();

        if (usesGithub) {
            String authorization;
            if (usesToken) {
                authorization = "token " + githubCredentials;
            } else {
                authorization = "Basic " + Base64.getEncoder().withoutPadding().encodeToString(githubCredentials.getBytes());
            }

            GistsApi.setAuthorization(authorization);
        } else {
            GistsApi.setUsesGithub(usesGithub);
        }

        this.snippetManager = new SnippetManager(pathToSnippoDir);
    }

    void logIn() {
        System.out.println(
                "1. Github log in using Github access token (preferred)\n" +
                "2. Github log in using username and password (to be deprecated)\n" +
                "3. Use Snippo as a guest (no access to Github Gists, only local snippets)"
        );

        int input = getIntInput();

        switch (input) {
            case 1: {
                System.out.println("Please enter your Github access token:");
                githubCredentials = keyboard.nextLine();
                usesGithub = true;
                usesToken = true;
            } break;
            case 2: {
                System.out.println("Please enter your Github credentials using the following format: username:password");
                usesGithub = true;
                githubCredentials = keyboard.nextLine();
            } break;
            default: break;
        }
    }

    void uiLoop() {
        while (isOpen) {
            displayMenu();
            executeCommand();
        }
    }

    private void displayMenu() {
        System.out.println(
                "Menu:\n" +
                "1. View all snippets\n" +
                "2. Create a snippet\n" +
                "3. Delete a snippet\n" +
                "4. Edit a snippet\n" +
                "5. Filter\n" +
                "6. Search\n" +
                "7. Quit");
    }

    private void executeCommand() {
        int input = getIntInput();

        switch (input) {
            case 1: System.out.println(snippetManager.listAll()); break;
            case 2: createSnippet(); break;
            case 3: deleteSnippet(); break;
            case 4: editSnippet(); break;
            case 5: filterSnippets(); break;
            case 6: searchSnippets(); break;
            case 7: isOpen = false; break;
            default: System.out.println(input + ": please select a valid option");
        }
    }

    private void createSnippet() {
        System.out.println("Enter the title of the snippet:");
        String title = keyboard.nextLine();

        System.out.println("Enter the language of the snippet:");
        String lang = keyboard.nextLine();

        System.out.println("Enter the tags of the snippet, like tag1,tag2,...:");
        String[] tags = keyboard.nextLine().split(",");

        snippetManager.create(title, null, lang, tags);
    }

    private void deleteSnippet() {
        System.out.println("Enter the id of the snippet to delete:");
        int input = getIntInput();
        snippetManager.delete(input);
    }

    private void editSnippet() {
        System.out.println("Enter the id of the snippet to edit");
        Integer snippetId = getIntInput();
        snippetManager.edit(snippetId);
    }

    private void filterSnippets() {
        System.out.println("Enter the term to filter by (... for none):");
        String filterTerm = keyboard.nextLine();
        if (filterTerm.equals("...")) {
            filterTerm = "";
        }

        System.out.println("Enter the language of the snippet (... for none):");
        String lang = keyboard.nextLine();
        if (lang.equals("...")) {
            lang = "";
        }

        System.out.println("Enter the tags of the snippet, like tag1,tag2,...: (... for none)");
        String[] tags = keyboard.nextLine().split(",");
        if (tags[0].equals("...")) {
            tags[0] = "";
        }

        System.out.println(snippetManager.listSnippets(snippetManager.filterSnippets(filterTerm, lang, tags)));
    }

    private void searchSnippets() {
        System.out.println("Enter the term(s) to search by (... for none):");
        String searchTerm = keyboard.nextLine();
        if (searchTerm.equals("...")) {
            searchTerm = "";
        }
        System.out.println(snippetManager.listSearchedSnippets(this.snippetManager.searchSnippets(searchTerm)));
    }

    void runCommandsOnArgs(String[] args) {
        switch (args[0]) {
            case "create":
                snippetManager.create(args[1], args[2], args[3], args[4].split(","));
                break;
            case "read":
                String snippetData = snippetManager.read(Integer.parseInt(args[1]));
                System.out.println(snippetData);
                break;
            case "delete":
                snippetManager.delete(Integer.parseInt(args[1]));
                break;
            case "listAll":
                System.out.println(snippetManager.listAll());
                break;
            case "filter":
                System.out.println(snippetManager.filterSnippets(args[1], args[2], args[3].split(",")));
                break;
            case "search":
                System.out.println(snippetManager.searchSnippets(args[1]));
                break;
            case "edit":
                snippetManager.edit(Integer.parseInt(args[1]));
                break;
            default:
                System.out.println("Unrecognized command. Exiting...");
                break;
        }
    }

    int getIntInput() {
        int input = 0;

        try {
            input = keyboard.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please enter a number");
        }
        if (input < 1 || input > 7) {
            System.out.println("Please enter a valid option");
        }
        keyboard.nextLine(); // Gets rid of the stray newline in the buffer
    }
}
