package sudoku;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
    private List<Integer> grid = new ArrayList<Integer>();
    private final int columnCount = 9;
    private List<SudokuObserver> observers;
    private List<Integer> gridInit;
    private FileManager fileManager = new FileManager();

    public SudokuGrid(){ //random contstructor
        RandomBoard init = new RandomBoard();
        List<Integer> randomboard = init.getRandomBoard();
        SetGrid(randomboard);
        gridInit = new ArrayList<Integer>(randomboard);
        observers = new ArrayList<SudokuObserver>();
    }

    public SudokuGrid(SudokuGrid grid){ //clone constructor
        this.grid = new ArrayList<Integer>(grid.GetGrid());
        this.gridInit = new ArrayList<Integer>(grid.GetInitialGrid());
        observers = new ArrayList<SudokuObserver>();
    }

    public SudokuGrid(String filename) { //file constructor
        List<Integer> loadedgrid;
        List<Integer> initgrid;
        try {
            loadedgrid = fileManager.readBoardFromFile(filename).get(0);
            SetGrid(loadedgrid);
        }
        catch (FileNotFoundException e){
            throw new IllegalArgumentException("File not found");
        }
        try {
            initgrid = fileManager.readBoardFromFile(filename).get(1);
            SetInitGrid(initgrid);
        }
        catch (FileNotFoundException e){
            throw new IllegalArgumentException("File not found");
        }
        observers = new ArrayList<SudokuObserver>();
        
        
    }

    public void SetGrid(List<Integer> grid) {
        if (grid.size() == 81) {
            this.grid = grid;
        }
        else {
            throw new IllegalArgumentException("Wrong file format");
        }
    }
    
    public void SetInitGrid(List<Integer> grid) {
        if (grid.size() == 81) {
            this.gridInit = grid;
        }
        else {
            throw new IllegalArgumentException("Wrong file format");
        }
    }

    public List<Integer> GetGrid() {
        return grid;
    }

    public List<Integer> GetInitialGrid() {
        return gridInit;
    }

    public boolean validCell(int column, int row) {
        int index = calcIndex(column, row);
        if (gridInit.get(index) == null) {
            return true;
        }
        return false;   
    }

    public void addObserver(SudokuObserver obs) {
        if (!observers.contains(obs)){
            observers.add(obs);
            obs.gridInitialized(this);
        }
        else {
            throw new IllegalArgumentException("Observer is already an observer");
        }
        
    }

    public void removeObserver(SudokuObserver obs) {
        if (observers.contains(obs)){
            observers.remove(obs);
        }
        else {
            throw new IllegalArgumentException("Observer is not already an observer");
        }
    }

    public Integer getCell(int column, int row){
        int index = calcIndex(column, row);
        return grid.get(index);
    }
    
    public void setCell(int column, int row, Integer value){
        int index = calcIndex(column, row);
        if (value == null && validCell(column, row)) {
            grid.set(index, null);
            observers.stream().forEach(o->o.gridChanged(column,row,value));
        }
        else if (((int)value > 0 && (int)value <= 9) && validCell(column, row)) {
            grid.set(index, value);
            observers.stream().forEach(o->o.gridChanged(column,row,value));
        }
        else {
            throw new IllegalArgumentException("Illegal value or placement");
        }
    }
 
    private int calcIndex(int column, int row){
        return row*columnCount + column;
    }
    
    public boolean isEmpty() {
        return grid.stream().filter(e -> e == null).count() == 81;
    }

    public String toString() {
        String ut = "   0 1 2 3 4 5 6 7 8\n";
        
        for (int r = 0; r < 9; r++){
            ut += r +" ";
            for (int c = 0; c < 9; c++){
                if (getCell(c, r) == null) {
                    ut += "| ";
                }
                else {
                    ut += "|"+getCell(c, r);
                }
            }
            ut += "|\n";
        }
        return ut;
    }

    public static void main(String[] args) {
        SudokuGrid grid = new SudokuGrid();
        MistakeChecker mistakeChecker = new MistakeChecker();
        //FileManager mgr = new FileManager();

        grid.addObserver(mistakeChecker);
        
        System.out.println(grid);
        grid.setCell(0, 0, 8);
        System.out.println(mistakeChecker.getInternalGrid());
        System.out.println(grid);


        

        //grid.setCell(3, 3, 8);
        //System.out.println(grid.toString());
        //System.out.println(mistakeChecker.getLegalValues(3, 3));

    
        
        
    }
}
