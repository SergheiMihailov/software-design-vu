import java.util.Scanner;

public class CliUI {
    private SnippetManager snippetManager;
    private boolean isOpen;
    private Scanner keyboard;

    public CliUI(String pathToSnippoJson) {
        this.snippetManager = new SnippetManager(pathToSnippoJson);
        isOpen = true;
        keyboard = new Scanner(System.in);
    }

    void uiLoop() {
        while (isOpen) {
            displayMenu();
            getAndExecuteCommand();
        }
    }

    private void displayMenu() {
        System.out.println("Menu:\n1. View all snippets\n2. Create a snippet\n3. Delete a snippet\n4. Quit");
    }

    private void getAndExecuteCommand() {
        int input = keyboard.nextInt();

        switch (input) {
            case 1: System.out.println(snippetManager.listAll()); break;
            case 2: createSnippet(); break;
            case 3: deleteSnippet(); break;
            case 4: isOpen = false; break;
            default: System.out.println(input + ": please select a valid option");
        }
    }

    private void createSnippet() {
        System.out.println("Enter the title of the snippet:");
        String title = keyboard.next();

        System.out.println("Enter the language of the snippet:");
        String lang = keyboard.next();

        System.out.println("Enter the tags of the snippet, like tag1,tag2,...:");
        String[] tags = keyboard.next().split(",");

        System.out.println("Enter the content of the snippet: (enter --snippo-end to indicate end of content)");
        StringBuilder content = new StringBuilder();
        while (keyboard.hasNext()) {
            String input = keyboard.nextLine();

            if (input.equals("--snippo-end")) {
                break;
            } else {
                content.append(input).append("\n");
            }
        }

        snippetManager.create(title, content.toString(), lang, tags);
    }

    private void deleteSnippet() {
        System.out.println("Enter the id of the snippet to delete:");
        int input = keyboard.nextInt();
        snippetManager.delete(input);
    }
}
