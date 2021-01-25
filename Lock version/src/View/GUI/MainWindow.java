package View.GUI;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import Controller.Controller;
import Model.ProgramState;
import Model.statement.Statement;
import Model.value.Value;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MainWindow implements Initializable{
    Controller myController;
    @FXML
    Label nrPrgStates;
    @FXML
    Button runButton;
    @FXML
    TableView<Map.Entry<Integer, Integer>> lockTable;
    @FXML
    TableColumn<Map.Entry<Integer,Integer>, Integer> lockTableKey;
    @FXML
    TableColumn<Map.Entry<Integer,Integer>, String> lockTableValue;
    @FXML
    TableView<Map.Entry<Integer, Value>> heapTable;
    @FXML
    TableColumn<Map.Entry<Integer,Value>, Integer> heapTableAddress;
    @FXML
    TableColumn<Map.Entry<Integer,Value>, String> heapTableValue;
    @FXML
    ListView<String> outList;
    @FXML
    ListView<String> fileTable;
    @FXML
    ListView<Integer> prgStateList;
    @FXML
    TableView<HashMap.Entry<String, Value>> symTable;
    @FXML
    TableColumn<HashMap.Entry<String, Integer>, String> symTableVariable;
    @FXML
    TableColumn<HashMap.Entry<String, Integer>, String> symTableValue;
    @FXML
    ListView<String> exeStackList;

    ExecutorService executor = null;

    public void setController(Controller controller) {
        this.myController = controller;
        updateUIComponents();
        List<ProgramState> prgList = myController.removeCompletedPrg(myController.getRepository().getProgramList());
        prgList.forEach(prg -> {
            try {
                myController.getRepository().logPrgStateExec(prg);
            } catch (Exception e) { e.printStackTrace(); } });
        Stage mainStage = (Stage) heapTable.getScene().getWindow();
        if(executor != null)
            executor.shutdown();
        executor = Executors.newFixedThreadPool(2);
        mainStage.show();
        mainStage.toFront();
        mainStage.setOnCloseRequest(t -> executor.shutdown());
        runButton.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.myController = null;
        runButton.setDisable(true);
        prgStateList.setOnMouseClicked(event -> setSymTableAndExeStack());

        lockTableKey.setCellValueFactory(p-> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        lockTableValue.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getValue() + " "));

        heapTableAddress.setCellValueFactory(p-> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heapTableValue.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getValue() + " "));
        symTableVariable.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getKey() + " "));
        symTableValue.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getValue() + " "));

        runButton.setOnAction(e -> {
            try {
                List<ProgramState> prgList = myController.removeCompletedPrg(myController.getRepository().getProgramList());
                if(prgList.size() <= 0)
                    throw new Exception("Done");
                myController.oneStepForAllPrg(prgList,executor);
            } catch (Exception e1) {
                executor.shutdown();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Toy Language - Current program finished");
                alert.setHeaderText(null);
                alert.setContentText(e1.getMessage());
                alert.initOwner(heapTable.getScene().getWindow());
                Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
                confirm.setDefaultButton(false);
                confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                alert.showAndWait();
            }
            updateUIComponents();
        });
    }

    public void updateUIComponents() {
        setNumberLabel();
        setHeapTable();
        setOutList();
        setFileTable();
        setPrgStateList();
        setLockTable();
        if(prgStateList.getSelectionModel().getSelectedItem() == null) {
            prgStateList.getSelectionModel().selectFirst();
        }
        setSymTableAndExeStack();
    }

    private void setLockTable() {
        lockTable.setItems(FXCollections.observableList(new ArrayList<>(myController.getRepository().getProgramList().get(0).getLockTable().getContent().entrySet())));
        lockTable.refresh();
    }

    public void setNumberLabel() {
        nrPrgStates.setText("The number of current program states: " + myController.getRepository().getProgramList().size());
    }

    public void setHeapTable() {
        heapTable.setItems(FXCollections.observableList(new ArrayList<>(myController.getRepository().getProgramList().get(0).getHeap().getContent().entrySet())));
        heapTable.refresh();
    }

    public void setOutList() {
        outList.setItems(FXCollections.observableArrayList(myController.getRepository().getProgramList().get(0).getOutput().getList().stream().map(Value::toString).collect(Collectors.toList())));
    }

    public void setFileTable() {
        fileTable.setItems(FXCollections.observableArrayList(myController.getRepository().getProgramList().get(0).getFileTable().getContent().keySet()));
    }

    public void setPrgStateList() {
        prgStateList.setItems(FXCollections.observableArrayList(myController.getRepository().getProgramList().stream().map(ProgramState::getID).collect(Collectors.toList())));
        prgStateList.refresh();
    }

    public void setSymTableAndExeStack() {
        ObservableList<String> exeStackItemsList = FXCollections.observableArrayList();
        List<ProgramState> allPrograms = myController.getRepository().getProgramList();

        ProgramState prgResult = null;
        for(ProgramState e : allPrograms) {
            if(e.getID() == prgStateList.getSelectionModel().getSelectedItem()) {
                prgResult = e;
                break;
            }
        }
        if(prgResult != null) {
            var myStack = prgResult.getExecutionStack().getStack();
            for(Statement e : myStack) {
                exeStackItemsList.add(e.toString());
            }
            symTable.setItems(FXCollections.observableArrayList(prgResult.getSymbolTable().getContent().entrySet()));
            symTable.refresh();
            exeStackList.setItems(exeStackItemsList);
            exeStackList.refresh();
        }
    }
}
