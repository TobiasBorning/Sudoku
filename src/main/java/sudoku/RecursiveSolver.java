package sudoku;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class RecursiveSolver extends MistakeChecker {

    private HashMap<String,List<Integer>> map = new HashMap<String, List<Integer>>();
    private List<String> sortedKeys;
    private SudokuGrid solvedGrid = null;

    public void scanBoard() {
        for (int i = 0; i < 2; i++) {
            for (int c = 0; c< 9; c++) {
                for (int r = 0; r < 9; r++) {
                    if (internalgrid.getCell(c,r) == null && getLegalValues(c, r).size() == 1) {
                        List<Integer> legalvalues = getLegalValues(c, r);
                        internalgrid.setCell(c, r, legalvalues.get(0));
                    }
                }
            } 
        }
        for (int c = 0; c< 9; c++) {
            for (int r = 0; r < 9; r++) {
                if (internalgrid.getCell(c,r) == null) {
                    String cr = c + "," +r;
                    map.put(cr, getLegalValues(c, r));
                }
            }
        }
    }

    public List<String> sortedKeys() {
        List<List<Integer>> valuelist = new ArrayList<List<Integer>>(map.values());
        Comparator<List<Integer>> sizeSort = new ListSizeComparator();
        valuelist.sort(sizeSort);
        List<String> keyList = new ArrayList<String>();
        for (List<Integer> value : valuelist) {
            for (Entry<String,List<Integer>> entry: map.entrySet()) {
                if (entry.getValue() == value) {
                    keyList.add(entry.getKey());
                }
            }
        }
        return keyList;
    }

    public SudokuGrid solve() {
        scanBoard();
        System.out.println("After scan:");
        System.out.println(internalgrid);
        sortedKeys = sortedKeys();
        recusiveAlgorithm(0);
        return solvedGrid;
    }

    public void recusiveAlgorithm(int index) {
        
        if (index < sortedKeys.size()) {

            int column = Integer.parseInt(sortedKeys.get(index).split(",")[0]);
            int row = Integer.parseInt(sortedKeys.get(index).split(",")[1]);

            for (int value : map.get(column+","+row)) {

                internalgrid.setCell(column, row, value);

                if (!checkMistake()) {
                    recusiveAlgorithm(index+1);
                }
            }

            internalgrid.setCell(column, row, null);
        }
        else {

            System.out.println("Solved!");
            if (solvedGrid == null) {
                solvedGrid = new SudokuGrid(internalgrid);
            }
            
        }
    }

    public static void main(String[] args) {
        SudokuGrid sudokuGrid = new SudokuGrid();
        RecursiveSolver solver = new RecursiveSolver();

        sudokuGrid.addObserver(solver);
        solver.scanBoard();

        System.out.println(solver.sortedKeys() + " : " + solver.sortedKeys().size());

        SudokuGrid solved = solver.solve();
        System.out.println(solved);
        
        
    }
}
