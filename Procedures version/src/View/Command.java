package View;

public abstract class Command {
    private final String key;
    private final String description;
    protected static boolean showStepsFlag = false;

    public Command(String key, String description){
        this.key = key;
        this.description = description;
    }
    public abstract void execute();

    public String getKey() {return key;}
    public String getDescription() {return description;}
}
