public class Main {
    public static void main (String[] args){
        SnippetManager snippetManager = new SnippetManager("snippo");
        new JsonIO();
        CliUI cliUI = new CliUI(snippetManager);

        if (args.length == 0) {
            cliUI.uiLoop();
        } else {
            cliUI.runCommandsOnArgs(args);
        }
    }
}
