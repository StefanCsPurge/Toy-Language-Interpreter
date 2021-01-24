package View;

import Controller.Controller;
import Model.ADT.*;
import Model.ProgramState;
import Model.statement.Statement;
import Repository.IRepository;
import Repository.Repository;
import SyntaxParsing.SyntaxParser;

public class UseSyntaxParsingCMD extends Command{
    private final String file;

    public UseSyntaxParsingCMD(String key, String desc, String file){
        super(key,desc);
        this.file = file;
    }

    @Override
    public void execute(){
        try{
            SyntaxParser p = new SyntaxParser(file);
            Statement realExample = p.getProgram();
            // now calling the method typeCheck for the input program before creating its associated PrgState
            realExample.typeCheck(new Dictionary<>());

            ProgramState realProgram = new ProgramState(new Stack<>(), new Dictionary<>(), new MyList<>(), new Dictionary<>(), new Heap<>(), new SemaphoreTable(), realExample);
            IRepository repo = new Repository(realProgram, "log.txt");
            Controller ctrl = new Controller(repo);
            ctrl.setDisplayFlag(showStepsFlag);

            var output = ctrl.allSteps();
            System.out.println("\nAll execution steps added to log.txt. The output is:\n" + output);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
