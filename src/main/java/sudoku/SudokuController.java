package sudoku;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SudokuController {
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private Label feedbackLabel;

    private SudokuGrid grid = new SudokuGrid();
    MistakeChecker mistakeChecker = new MistakeChecker();
        
    
    public void initialize() {

        grid.addObserver(mistakeChecker);

        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) { 
                TextField tmp = new TextField();
                tmp.setId(""+c+","+r);
                tmp.setMinHeight(40.0);

                if (grid.getCell(c, r) != null) {
                    tmp.setText(""+grid.getCell(c, r));
                    //lås rute
                    BooleanProperty isChecked = new SimpleBooleanProperty(false);
                    tmp.editableProperty().bind(isChecked);
                    //fyll bakkgrunn
                    Color backgroundColor = Color.web("#EEEEEE");
                    BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
                    Background background = new Background(backgroundFill);
                    tmp.setBackground(background);
                    //endre farge
                    String textColor = "#1111dd";
                    tmp.setStyle("-fx-text-fill: " + textColor + ";");
                }

                //set border
                Border border;
                BorderStroke border1px = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0, 0, 0, 0));
                border = new Border(border1px);
                tmp.setBorder(border);
                
                //fet font
                Font font = Font.font("System", FontWeight.BOLD, 15);
                tmp.setFont(font);

                //midstill tekst
                tmp.setAlignment(Pos.CENTER);

                //kall funksjon på klikk
                tmp.setOnAction(this::setCell);

                sudokuGrid.add(tmp, c, r);
            }
        }
    }

    public void setCell(ActionEvent e) {
        System.out.println("suksess");
        TextField cell = (TextField) e.getSource();
        int column = Integer.parseInt(cell.getId().split(",")[0]);
        int row =  Integer.parseInt(cell.getId().split(",")[1]);
        int value = Integer.parseInt(cell.getText());
        System.out.println(column +"," + row + " : " + value);

        grid.setCell(column, row, value);
        if (mistakeChecker.checkMistake()){
            grid.setCell(column, row, null);
            cell.setText(null);
            feedbackLabel.setText("Illegal Placement");
        }
        else {
            feedbackLabel.setText("");
        }
        
    }

}
