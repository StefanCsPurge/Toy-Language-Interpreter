package View;

import java.util.*;

public class Console {
    private final Map<String,Command> commands;

    public Console(){
        commands = new HashMap<>();
    }

    public void addCommand(Command c){
        commands.put(c.getKey(),c);
    }

    private void printMenu(){
        System.out.println("\nSelect program to run");
        for(Command cmd : commands.values()){
            String line = String.format("%11s : %s", cmd.getKey(), cmd.getDescription());
            System.out.println(line);
        }
    }

    private void pressAnyKeyToContinue()
    {
        System.out.print("Done. Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public void run(){
        System.out.println("\nWelcome to the best Toy interpreter in the universe!");

        Scanner reader = new Scanner(System.in);

        // System.out.print("Log file path: ");
        // String path = reader.nextLine();

        while(true){
            printMenu();

            System.out.print("Input option: ");
            String option = reader.nextLine();
            option = option.trim();

            if(option.equals("emergency exit"))
                break;

            Command cmd = commands.get(option);

            if (cmd == null) {
                System.out.println("Invalid option!");
                continue;
            }

            cmd.execute();
            pressAnyKeyToContinue();
        }
    }
}
