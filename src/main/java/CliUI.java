import java.util.Scanner;

class CliUI {
    private SnippetManager snippetManager;
    private boolean isOpen;
    private Scanner keyboard;

    CliUI(SnippetManager snippetManager) {
        this.snippetManager = snippetManager;
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
        System.out.println("Menu:\n1. View all snippets\n2. Create a snippet\n3. Delete a snippet\n4. Edit a snippet\n5. Filter\n6. Quit");
    }

    private void getAndExecuteCommand() {
        int input = keyboard.nextInt();

        switch (input) {
            case 1: System.out.println(snippetManager.listAll()); break;
            case 2: createSnippet(); break;
            case 3: deleteSnippet(); break;
            case 4: editSnippet(); break;
            case 5: filterSnippets(); break;
            case 6: isOpen = false; break;
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

        Integer snippetId = snippetManager.create(title, "", lang, tags);
        snippetManager.edit(snippetId);
    }

    private void deleteSnippet() {
        System.out.println("Enter the id of the snippet to delete:");
        int input = keyboard.nextInt();
        snippetManager.delete(input);
    }

    private void editSnippet() {
        System.out.println("Enter the id of the snippet to edit");
        Integer snippetId = keyboard.nextInt();
        snippetManager.edit(snippetId);
    }

    private void filterSnippets() {
        System.out.println("Enter the title to filter by (... for none):");
        String title = keyboard.next();
        if (title.equals("...")) {
            title = "";
        }

        System.out.println("Enter the language of the snippet (... for none):");
        String lang = keyboard.next();
        if (lang.equals("...")) {
            lang = "";
        }

        System.out.println("Enter the tags of the snippet, like tag1,tag2,...: (... for none)");
        String[] tags = keyboard.next().split(",");
        if (tags[0].equals("...")) {
            tags[0] = "";
        }

        System.out.println(snippetManager.filter(title, lang, tags));
    }
}
