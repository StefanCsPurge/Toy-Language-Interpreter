package Main;

import View.GUI.MainWindow;
import View.GUI.SelectWindow;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import java.io.IOException;

public class GUI_Main extends Application {

    public static void main(String[] args) { launch(args); }    // an object Application is created

    @Override
    public void start(Stage mainStage) throws IOException{
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("../View/GUI/MainWindow.fxml"));
        Parent mainWindow = mainLoader.load();
        mainStage.setTitle("Run program step by step");
        mainStage.setScene(new Scene(mainWindow));
        mainStage.getScene().getStylesheets().add(getClass().getResource("../View/GUI/darkTheme.css").toExternalForm());
        mainStage.getIcons().add(new Image(GUI_Main.class.getResourceAsStream("icon.png")));
        mainStage.show();
        MainWindow mainWindowController = mainLoader.getController();

        FXMLLoader secondaryLoader = new FXMLLoader(getClass().getResource("../View/GUI/SelectWindow.fxml"));
        Parent selectWindow = secondaryLoader.load();
        SelectWindow selectWindowController = secondaryLoader.getController();
        selectWindowController.setMainWindowController(mainWindowController);

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Select option");
        secondaryStage.getIcons().add(new Image(GUI_Main.class.getResourceAsStream("icon.png")));
        secondaryStage.setScene(new Scene(selectWindow));
        secondaryStage.getScene().getStylesheets().add(getClass().getResource("../View/GUI/darkTheme.css").toExternalForm());
        secondaryStage.show();
    }
}
