package Repository;

import Model.ProgramState;

import java.util.List;

public interface IRepository {
    void addProgram(ProgramState program);
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> programStates);

    //ProgramState getCrtProgram() throws Exception;
    //void removeLastProgram();

    void setLogFilePath(String path);
    void logPrgStateExec(ProgramState pState) throws Exception;
}
