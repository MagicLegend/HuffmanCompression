import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.example.HelloWorld;

import java.io.IOException;

public class Main {


    /**
     * For lib usage: https://code.google.com/archive/p/cliche/wikis/Manual.wiki
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("hello", "", new Main())
                .commandLoop();
    }

    @Command
    public String temp(int a) {
        return "Woo " + a;
    }

}
