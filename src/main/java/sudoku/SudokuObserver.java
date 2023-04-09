package sudoku;

public interface SudokuObserver {
    void gridChanged(int column, int row, Integer value);
    void gridInitialized(SudokuGrid grid);
}
