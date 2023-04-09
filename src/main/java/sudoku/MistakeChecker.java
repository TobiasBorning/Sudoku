package sudoku;

import java.util.ArrayList;
import java.util.List;

public class MistakeChecker implements SudokuObserver {
    protected SudokuGrid internalgrid;
    
    public void gridInitialized(SudokuGrid grid) {
        internalgrid = new SudokuGrid(grid);
    }
    
    public void gridChanged(int column, int row, Integer value) {
        internalgrid.setCell(column, row, value);
    }

    public SudokuGrid getInternalGrid() {
        return internalgrid;
    }

    public boolean checkSquareMistake() {
        for (int sc = 0; sc < 3; sc++){ //square column
            for (int sr = 0; sr < 3; sr++) { //square row
                //square summer
                List<Integer> tmp = new ArrayList<Integer>();
                for (int c = 0; c < 3; c++) { 
                    for (int r = 0; r < 3; r++) {
                        Integer cell = internalgrid.getCell(c+(3*sc), r+(3*sr));
                        if (cell != null && tmp.contains(cell)) {
                            return true;
                        }
                        tmp.add(cell);
                    }
                }
            }
        }
        return false;
    }

    public boolean checkRowMistake() {
        for (int r = 0; r < 9; r++) {
            List<Integer> tmp = new ArrayList<Integer>();
            for (int c = 0; c < 9; c++) {
                Integer cell = internalgrid.getCell(c, r);
                if (cell != null && tmp.contains(cell)) {
                    return true;
                }
                tmp.add(cell);
            }
        }
        return false;
    }

    public boolean checkColumnMistake() {
        for (int c = 0; c < 9; c++) {
            List<Integer> tmp = new ArrayList<Integer>();
            for (int r = 0; r < 9; r++) {
                Integer cell = internalgrid.getCell(c, r);
                if (cell != null && tmp.contains(cell)) {
                    return true;
                }
                tmp.add(cell);
            }
        }
        return false;
    }

    public List<Integer> getLegalValues(int column, int row) {
        List<Integer> legal = new ArrayList<Integer>();
        if (!internalgrid.validCell(column, row)) {
            return legal;
        }
        for (int i = 1; i < 10; i++) {
            try {
                internalgrid.setCell(column, row, i);
                if (!checkMistake()) {
                    legal.add(i);
                }
                internalgrid.setCell(column, row, null);
            }
            catch (IllegalArgumentException e) {

            }
            
        }
        return legal;
    }

    public boolean completedGrid() {
        return internalgrid.GetGrid().stream().filter(e -> (e == null)).count() == 0;
    }

    public boolean checkMistake() {
        return checkRowMistake()||checkColumnMistake()||checkSquareMistake();
    }

    public boolean checkWin() {
        if (completedGrid() && !checkMistake()) {
            return true;
        }
        return false;
    }

}


