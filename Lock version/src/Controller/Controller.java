package Controller;

import Model.ADT.IList;
import Model.value.Value;
import Model.ProgramState;
import Model.value.RefValue;
import Repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepository repo;
    boolean displayFlag = false;
    //ExecutorService executor;
    List<Exception> exceptions = new ArrayList<>();

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public IRepository getRepository() { return repo; }

    public void setDisplayFlag(boolean flag){
        displayFlag = flag;
    }

    // public void addLogFilePath(String path){ repo.setLogFilePath(path); }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList){
         return inPrgList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    // gets all the addresses from the ref values stored in the symbol table
    List<Integer> getAddrFromSymTable(Collection<Value> symTableValues, Collection<Value> heap){
        return Stream.concat(
                heap.stream()
                        .filter(v-> v instanceof RefValue)
                        .map(v-> ((RefValue)v).getAddr()),
                symTableValues.stream()
                        .filter(v-> v instanceof RefValue)
                        .map(v-> ((RefValue)v).getAddr())
        ).collect(Collectors.toList());
    }

    // the safe garbage collector
    Map<Integer,Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    void callGarbageCollector(List<ProgramState> programStates){
        programStates.forEach(
                p-> p.getHeap().setContent((HashMap<Integer, Value>)
                        safeGarbageCollector(getAddrFromSymTable(p.getSymbolTable().getContent().values(),
                                                                 p.getHeap().getContent().values()),
                        p.getHeap().getContent()))
        );
    }

    // this executes one step for each existing ProgramState (namely each thread)
    public void oneStepForAllPrg(List<ProgramState> prgList, ExecutorService executor) throws Exception {
        // prepare the list of callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>)(p::oneStep))
                .collect(Collectors.toList());

        // start the execution of the callables
        // it returns the list of newly created PrgStates (namely threads)
        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                                        .map(future -> {
                                            try { return future.get();}
                                            catch(Exception e) { exceptions.add(e); return null; }
                                        })
                                        .filter(Objects::nonNull).collect(Collectors.toList());

        if(exceptions.size() > 0)
            throw new Exception(getExceptionsMessages());

        // add the newly created threads to the list of existing threads
        prgList.addAll(newPrgList);

        // after the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if(displayFlag) System.out.print(prg.toString()); // display program state
            } catch (Exception e) { e.printStackTrace(); }
        });

        // save the current programs in the repository
        repo.setProgramList(prgList);
    }

    public IList<Value> allSteps() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        var output = repo.getProgramList().get(0).getOutput();
        // now remove the completed programs
        List<ProgramState> prgList = removeCompletedPrg(repo.getProgramList());

        // before the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if(displayFlag) System.out.print(prg.toString()); // display program state
            } catch (Exception e) { e.printStackTrace(); }
        });

        while(prgList.size() > 0){
            // call garbage collector
            callGarbageCollector(prgList);

            oneStepForAllPrg(prgList, executor);

            output = prgList.get(0).getOutput();
            // remove the completed programs
            prgList = removeCompletedPrg(repo.getProgramList());
        }

        executor.shutdown();

        // update the repository state
        repo.setProgramList(prgList);

        return output;
    }

    private String getExceptionsMessages(){
        StringBuilder msg = new StringBuilder();
        for(Exception e : exceptions)
            msg.append(e.getMessage()).append("\n");
        return msg.toString();
    }
}
