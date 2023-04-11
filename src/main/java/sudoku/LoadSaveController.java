package sudoku;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoadSaveController {
    @FXML
    private Label feedbackLabel;
    @FXML
    private Button backButton;
    @FXML
    private VBox fileListVBox;

    public void initialize() {
        File folder = new File("/Users/tobiasborning/NTNU/Objekt/prosjekt/TDT4100-prosjekt-tobiaslb/src/main/java/sudoku/sudokusaves");
        File[] files = folder.listFiles();

        for (File file : files) {
            Pane pane = new Pane();
            pane.setMinWidth(395);
            pane.setMinHeight(70);
            pane.getStyleClass().add("panecontainer");
            pane.setId(file.getName());

            Label label = new Label(file.getName());
            label.getStyleClass().add("label");

            Button loadButton = new Button("Load");
            loadButton.getStyleClass().add("loadbutton");

            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().add("deletebutton");
            
            loadButton.setOnAction(event -> {
                try {
                    //bytter scene og kaller loadFile
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
                    Parent root = loader.load();

                    SudokuController sudokuController = loader.getController();
                    sudokuController.loadFile(file.getName());

                    Stage stage;
                    Scene scene;
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }
                catch (IOException e){
                    e.printStackTrace();
                }
            });
            deleteButton.setOnAction(event -> { 
                FileManager mgr = new FileManager();
                mgr.deleteFile(file.getName());
                feedbackLabel.setText("Successfully deleted " + file.getName()); 
            });
            
            if (!file.getName().equals("tmpsoultion.txt")) {
                fileListVBox.getChildren().add(pane);
                pane.getChildren().add(label);
                pane.getChildren().add(loadButton);
                pane.getChildren().add(deleteButton);
            }
        }
    }

    public void switchToMainScene(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root = FXMLLoader .load(getClass().getResource("MainScene.fxml")); 
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
    
}
