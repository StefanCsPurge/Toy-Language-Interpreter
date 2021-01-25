package Repository;

import Model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private List<ProgramState> programs;
    private String logFilePath;
    private boolean firstTime = true;

    public Repository(ProgramState program, String logFile){
        this.programs = new ArrayList<>();
        this.programs.add(program);
        this.logFilePath = logFile;
    }

    @Override
    public void setLogFilePath(String path){
        logFilePath = path;
    }

    @Override
    public void addProgram(ProgramState program) { this.programs.add(program); }

    @Override
    public List<ProgramState> getProgramList(){
        return programs;
    }

    @Override
    public void setProgramList(List<ProgramState> programStates){
        this.programs = programStates;
    }

    /*@Override
    public ProgramState getCrtProgram(){
        //if(index < 0 || index > programs.size()-1)
        //    throw new Exception("Index out of bounds!");
        return programs.get(0);
    }*/

    /*@Override
    public void removeLastProgram(){
        if(programs.size() == 0)
            return;
        int index = programs.size() - 1;
        this.programs.remove(index);
    }*/

    @Override
    public void logPrgStateExec(ProgramState prgState) throws Exception{
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
        if(firstTime){
            writer.print("PROGRAM EXECUTION START ---------------------------------------------------------------\n\n");
            firstTime = false;
        }
        writer.print(prgState);
        writer.close();
    }
}
