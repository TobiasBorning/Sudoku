package sudoku;

//import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
    private List<Integer> grid = new ArrayList<Integer>();
    private final int columnCount = 9;
    private List<SudokuObserver> observers = new ArrayList<SudokuObserver>();
    private List<Integer> gridInit;

    public SudokuGrid(){
        RandomBoard init = new RandomBoard();
        List<Integer> randomboard = init.getRandomBoard();
        SetGrid(randomboard);
        gridInit = new ArrayList<Integer>(randomboard);
    }

    public void SetGrid(List<Integer> grid) {
        if (grid.size() == 81) {
            this.grid = grid;
        }
    }

    public List<Integer> GetGrid() {
        return grid;
    }

    public List<Integer> GetInititalGrid() {
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
        observers.add(obs);
        obs.gridChanged(this);
    }

    public Integer getCell(int column, int row){
        int index = calcIndex(column, row);
        return grid.get(index);
    }
    
    public void setCell(int column, int row, Integer value){
        int index = calcIndex(column, row);
        if (value == null) {
            grid.set(index, null);
        }
        else if (((int)value > 0 || (int)value <= 9) && validCell(column, row)) {
            grid.set(index, value);
            observers.stream().forEach(o->o.gridChanged(this));
        }
        else {
            System.out.println("Illegal placement or value");
        }
    }
 
    private int calcIndex(int column, int row){
        return row*columnCount + column;
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
        RecursiveSolver solve = new RecursiveSolver();

        grid.addObserver(mistakeChecker);
        grid.addObserver(solve);
        grid.setCell(3, 3, 0);
        //System.out.println(grid.toString());

        /* 
        FileManager mgr = new FileManager();
        try {
            grid.SetGrid(mgr.readBoardFromFile("test1.txt"));
        }
        catch (FileNotFoundException e){
            System.out.println("filen finnes ikke");
        }
        */
        //grid.setCell(3, 3, 8);
        //System.out.println(grid.toString());
        //System.out.println(mistakeChecker.getLegalValues(3, 3));
        
        solve.solve();
        
    }
}
