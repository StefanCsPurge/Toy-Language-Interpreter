package View;

import Controller.Controller;
import Model.ADT.*;
import Model.ProgramState;
import Model.statement.Statement;
import Repository.IRepository;
import Repository.Repository;

public class RunExampleCommand extends Command{
    private final Statement example;
    boolean alreadyRan = false;
    public RunExampleCommand(String key, String desc, Statement example){
        super(key,desc);
        this.example = example;
    }

    @Override
    public void execute(){
        if (alreadyRan){
            System.out.println("This example was already executed!");
            //return;
        }
        try{
            example.typeCheck(new Dictionary<>());

            ProgramState prg = new ProgramState(new Stack<>(), new Dictionary<>(), new MyList<>(), new Dictionary<>(), new Heap<>(), new LockTable(), example);
            IRepository repo = new Repository(prg, "log" + super.getKey() + ".txt");
            Controller ctrl = new Controller(repo);
            ctrl.setDisplayFlag(showStepsFlag);

            var output = ctrl.allSteps();
            System.out.println("\nThe output is:\n" + output);
            alreadyRan = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
