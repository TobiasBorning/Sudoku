package sudoku;

import javafx.event.EventHandler;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    @FXML
    private Button saveButton, loadButton, solveButton, showSolutionButton;
    @FXML
    private TextField saveFileInput, loadFileInput;

    private SudokuGrid grid;
    private MistakeChecker mistakeChecker;
    private FileManager fileManager = new FileManager(); 
    private RecursiveSolver solver;
    private SudokuGrid solvedGrid;
    private boolean solved;
    
    public void initialize() {
        boardToScene(null);
        
    }

    private void boardToScene(String filename) {
        solved = false;
        feedbackLabel.setText("");
        sudokuGrid.getChildren().clear();
        grid = null;
        solvedGrid = null;
        if (filename == null) {
            grid = new SudokuGrid();
        }
        else {
            grid = new SudokuGrid(filename);
        }
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) { 
                TextField tmp = new TextField();
                tmp.setId(""+c+","+r);
                tmp.setMinHeight(40.0);
                String style = "-fx-font-size: 14px; -fx-font-family: \"Druk Wide Bold\"; -fx-font-weight: bold;";
                tmp.setStyle(style);
                List<Integer> initgrid = grid.GetInitialGrid();
                //fastsatte ruter
                if (grid.getCell(c, r) != null && initgrid.get(r*9 + c) == grid.getCell(c, r)) {
                    //lås rute
                    BooleanProperty isNotChecked = new SimpleBooleanProperty(false);
                    tmp.editableProperty().bind(isNotChecked);
                    //fyll bakkgrunn
                    Color backgroundColor = Color.web("#EEEEEE");
                    BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
                    Background background = new Background(backgroundFill);
                    tmp.setBackground(background);
                    //endre farge
                    String textColor = "#116611";
                    tmp.setStyle(style + "-fx-text-fill:" + textColor);
                    tmp.setText(""+grid.getCell(c, r));
                }
                //tomme ruter
                else if (grid.getCell(c, r) == null) {
                    tmp.setText("");
                }
                //ruter som kan endres
                else {
                    tmp.setText(""+grid.getCell(c, r));
                }
                //set border
                Border border;
                BorderStroke border1px = new BorderStroke(Color.web("#656161"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0, 0, 0, 0));
                border = new Border(border1px);
                tmp.setBorder(border);
                
                //fet font
                Font font = Font.font("System", FontWeight.BOLD, 15);
                tmp.setFont(font);

                //midstill tekst
                tmp.setAlignment(Pos.CENTER);

                //kall funksjon på klikk
                //tmp.setOnAction(this::setCell);
                tmp.setOnKeyTyped(new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent keyEvent) {
                        setCell(keyEvent);
                    }
                });

                sudokuGrid.add(tmp, c, r);
            }
        }
        
        markOnHover(sudokuGrid);
        mistakeChecker = new MistakeChecker();
        grid.addObserver(mistakeChecker);
        solver = new RecursiveSolver();
        grid.addObserver(solver);

    }

    public void solve() {
        feedbackLabel.setText("Solving...");
        try {
            if (!solved) {
                solvedGrid = solver.solve();
                System.out.println(solvedGrid);
                feedbackLabel.setText("Solution found!");
                solved = true;
            }
        }
        catch (Exception e) {
            feedbackLabel.setText("Could not solve");
            solved = false;
        }
        
    }

    public void showSolution() {
        try {
            if (solved) {
                String file = "tmpsoultion.txt";
                fileManager.writeBoardToFile(solvedGrid, file);
                boardToScene(file);
            }
            else {
                feedbackLabel.setText("You have to press <solve> before viewing the solution");
            }
        }
        catch (Exception e){
            feedbackLabel.setText("Could not find solution");
        }
        
        
    }

    public void setCell(KeyEvent event) {
        
        TextField cell = (TextField) event.getSource();
        int column = Integer.parseInt(cell.getId().split(",")[0]);
        int row =  Integer.parseInt(cell.getId().split(",")[1]);

        try {

            Integer value;
            if (!cell.getText().equals("")) {
                value = Integer.parseInt(cell.getText());
            }
            else {
                value = null;
            }
            
            System.out.println(column +"," + row + " : " + value);
            try {

                grid.setCell(column, row, value);
                System.out.println(mistakeChecker.checkMistake());

                if (mistakeChecker.checkMistake()){
                    grid.setCell(column, row, null);
                    cell.setText(null);
                    feedbackLabel.setText("Illegal Placement of: " + value + "(Obvious mistake)");
                }
                else if (solved){
                    if (value != solvedGrid.getCell(column, row)) {
                        grid.setCell(column, row, null);
                        cell.setText(null);
                        feedbackLabel.setText("Illegal Placement of: " + value);
                        feedbackLabel.setText("Illegal Placement of: " + value + "(Complicated mistake)");
                    }
                    else {
                        feedbackLabel.setText("");
                    }
                }
                else {
                    feedbackLabel.setText("");
                }
            }
            catch (IllegalArgumentException illegalValue) {

                feedbackLabel.setText("Illegal Value");
                grid.setCell(column, row, null);
                if (cell.isEditable()) { //hindrer tab sletting
                    cell.setText(null);
                }
                
            }
        }
        //håndterer input som ikke er tall
        catch (NumberFormatException illegalInput) {
            grid.setCell(column, row , null);
            feedbackLabel.setText("Illegal input format");
            cell.setText(null);
        }
        if (mistakeChecker.checkWin()) {
            feedbackLabel.setText("Solved!");
        }
            
    }

    private void markOnHover(GridPane sudokuGrid) {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                int column = Integer.parseInt(textField.getId().split(",")[0]);
                int row = Integer.parseInt(textField.getId().split(",")[1]);
                
                textField.setOnMouseEntered(e->{
                    //vannrett
                    if(!textField.isEditable()) {
                        textField.setStyle("-fx-font-size: 14px; -fx-font-family: \"Druk Wide Bold\"; -fx-font-weight: bold; -fx-text-fill: #c95db0");
                    }

                    for (int r = 0; r < 9; r++) {
                        TextField tmp = (TextField) sudokuGrid.getChildren().get((r*9) + column);
                        //fyll bakkgrunn
                        if (tmp.isEditable()) {
                            setBackgroundColor(tmp, "#EEFFEE");
                        }
                        else {
                            setBackgroundColor(tmp, "#DDFFDD");
                        }
                    }
                    //loddrett
                    for (int c = 0; c < 9; c++) {
                        TextField tmp = (TextField) sudokuGrid.getChildren().get((row*9)+c);
                        //fyll bakkgrunn
                        if (tmp.isEditable()) {
                            setBackgroundColor(tmp, "#EEFFEE");
                        }
                        else {
                            setBackgroundColor(tmp, "#DDFFDD");
                        }
                    }
                    
                });
                textField.setOnMouseExited(e->{
                    //vannrett
                    if(!textField.isEditable()) {
                        textField.setStyle("-fx-font-size: 14px; -fx-font-family: \"Druk Wide Bold\"; -fx-font-weight: bold; -fx-text-fill: #116611");
                    }
                    
                    for (int r = 0; r < 9; r++) {
                        TextField tmp = (TextField) sudokuGrid.getChildren().get((r*9) + column);
                        //fyll bakkgrunn
                        if (tmp.isEditable()) {
                            setBackgroundColor(tmp, "#FFFFFF");
                        }
                        else {
                            setBackgroundColor(tmp, "#EEEEEE");
                        }
                    }
                    //loddrett
                    for (int c = 0; c < 9; c++) {
                        TextField tmp = (TextField) sudokuGrid.getChildren().get((row*9) + c);
                        //fyll bakkgrunn
                        if (tmp.isEditable()) {
                            setBackgroundColor(tmp, "#FFFFFF");
                        }
                        else {
                            setBackgroundColor(tmp, "#EEEEEE");
                        }
                    }
                    
                });
            }
            
        } 
    }

    private void setBackgroundColor(TextField textField, String color) {
        Color backgroundColor = Color.web(color);
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        textField.setBackground(background);
    }

    public void saveFile(){
        if (!saveFileInput.getText().equals("")) {
            String file = saveFileInput.getText() + ".txt";
            fileManager.writeBoardToFile(grid, file);
            saveFileInput.setText("");
            feedbackLabel.setText("Saved");
        }
        else {
            feedbackLabel.setText("Can't save to empty file");
        }
        
    }

    public void loadFile() {
        //clear old board
        String file = loadFileInput.getText() + ".txt";
        if (file.equals(".txt")) {
            boardToScene(null); //laster et random brett ved tom input
        }
        else {
            boardToScene(file); 
        }
        loadFileInput.setText("");
        System.out.println(grid); 
        
    }

}
