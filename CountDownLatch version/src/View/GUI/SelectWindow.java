package View.GUI;

import Controller.Controller;
import Model.ADT.Heap;
import Model.ADT.LatchTable;
import Model.ProgramState;
import Model.statement.*;
import Repository.IRepository;
import Repository.Repository;
import SyntaxParsing.SyntaxParser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectWindow implements Initializable {

    @FXML
    private ListView<String> optionsList;

    @FXML
    private Button runOptionButton;

    private MainWindow mainWindowController;

    public void setMainWindowController(MainWindow mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> optStr = new ArrayList<>();
        optStr.add("USE SYNTAX PARSING INTERPRETER -> write your program in 'toyEx.txt' !");
        optStr.add("int v; v=2; Print(v)");
        optStr.add("int a; int b; a=2+3*5; b=a+1; Print(b)");
        optStr.add("bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)");
        optStr.add("string varf; varf=test.in; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)");
        optStr.add("Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a))) // ok with safe garbage collector");
        optStr.add("Ref int v; new(v,20); Ref Ref int a; new(a,v); wH(v,30); print(rH(rH(a)))");
        optStr.add("int v; v=4; (while (v>0) print(v); v=v-1); print(v)");
        optStr.add("int v; Ref int a; v=10; new(a,22); fork( wH(a,30); v=32; print(v); print(rH(a)) ); print(v); print(rH(a))");
        optStr.add("*conditional assignment example*");
        optStr.add("*CountDownLatch example*");
        optionsList.setItems(FXCollections.observableList(optStr));
        optionsList.getSelectionModel().select(0);
        List<Statement> examples = Main.Main.getExamples();

        runOptionButton.setOnAction(actionEvent -> {
            int index = optionsList.getSelectionModel().getSelectedIndex();
            Statement programTorRun;
            String logFileName;
            if(index == 0){
                try {
                    SyntaxParser p = new SyntaxParser("toyEx.txt");
                    programTorRun = p.getProgram();
                    logFileName = "log.txt";
                }
                catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.initOwner(optionsList.getScene().getWindow());
                    alert.show();
                    return;
                }
            }
            else{
                programTorRun = examples.get(index-1);
                logFileName = "log" + index + ".txt";
            }

            try {
                programTorRun.typeCheck(new Model.ADT.Dictionary<>());
                ProgramState realProgram = new ProgramState(new Model.ADT.Stack<>(), new Model.ADT.Dictionary<>(), new Model.ADT.List<>(), new Model.ADT.Dictionary<>(), new Heap<>(), new LatchTable(), programTorRun);
                IRepository repo = new Repository(realProgram, logFileName);
                Controller programCtrl = new Controller(repo);
                mainWindowController.setController(programCtrl);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                alert.initOwner(optionsList.getScene().getWindow());
                alert.show();
            }
        });
    }

}
