public class Main {
    public static void main (String[] args){
        CliUI cliUI = new CliUI("snippo");

        if (args.length == 0) {
            cliUI.uiLoop();
        } else {
            cliUI.runCommandsOnArgs(args);
        }
    }
}
