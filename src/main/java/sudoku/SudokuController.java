package sudoku;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {
    @FXML
    private GridPane sudokuGrid;

    private SudokuGrid grid = new SudokuGrid();
    
    
    public void initialize() {

        SudokuObserver mistakeChecker = new MistakeChecker();
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
                }
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
        
    }

}
