public class Main {
    public static void main (String[] args){
        SnippetManager snippetManager = new SnippetManager("snippo");
        new JsonIO();

        if (args.length == 0) {
            CliUI cliUI = new CliUI(snippetManager); cliUI.uiLoop();
            return;
        }

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
                System.out.println(snippetManager.filter(args[1], args[2], args[3].split(",")));
                break;
            case "edit":
                snippetManager.edit(Integer.parseInt(args[1]));
                break;
            case "search":
                snippetManager.search("something");
                break;
            default:
                System.out.println("Unrecognized command. Exiting...");
                break;
        }
    }
}
