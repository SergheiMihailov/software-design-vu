public class Main {
    public static void main (String[] args){
        new JsonIO();
        CliUI cliUI = new CliUI("snippo");

        if (args.length == 0) {
            cliUI.uiLoop();
        } else {
            cliUI.runCommandsOnArgs(args);
        }
    }
}
