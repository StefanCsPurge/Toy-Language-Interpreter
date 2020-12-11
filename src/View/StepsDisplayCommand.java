package View;

public class StepsDisplayCommand extends Command{
    public StepsDisplayCommand(String key, String desc){
        super(key,desc);
    }

    @Override
    public void execute(){
        showStepsFlag = !showStepsFlag;
    }
}
