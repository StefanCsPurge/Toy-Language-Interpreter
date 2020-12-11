package View;

public class ExitCommand extends Command{
    public ExitCommand(String key, String desc){
        super(key,desc);
    }

    @Override
    public void execute(){
        System.out.println("We out...              ¯\\_(ツ)_/¯");
        System.exit(0);
    }
}
