package sudoku;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SudokuTest {
    @Test
    void testConstructors() {

        //check random board
        SudokuGrid sudokuGrid1 = new SudokuGrid();
        boolean equalGrids = true;
        for (int i = 0; i < 10; i++) {
            SudokuGrid randomGrid = new SudokuGrid();
            if (equalGrids && sudokuGrid1.equals(randomGrid)) {
                
            }
            else {
                equalGrids = false;
            }
        }
        assertFalse(equalGrids);

        //check same file loaded
        SudokuGrid sudokuGrid2 = new SudokuGrid("test1.txt");
        FileManager mgr = new FileManager();
        List<Integer> test1Grid;
        List<Integer> test1InitGrid;
        try {
            test1Grid = mgr.readBoardFromFile("test1.txt").get(0);
            test1InitGrid = mgr.readBoardFromFile("test1.txt").get(0);
        }
        catch (FileNotFoundException e) {
            test1Grid = null;
            test1InitGrid = null;
        }
        assertEquals(sudokuGrid2.GetGrid(), test1Grid);
        assertEquals(sudokuGrid2.GetInitialGrid(), test1InitGrid);
        
        //check load non-existent file
        assertThrows(IllegalArgumentException.class, () -> {
            new SudokuGrid("finnesikke.txt");
        });
        


        //check non-empty
        assertFalse(sudokuGrid1.GetGrid().isEmpty());
        assertFalse(sudokuGrid1.GetInitialGrid().isEmpty());
        assertFalse(sudokuGrid2.GetGrid().isEmpty());
        assertFalse(sudokuGrid1.GetInitialGrid().isEmpty());

        //check cloning constructor
        SudokuGrid sudokuGrid3 = new SudokuGrid(sudokuGrid1);
        assertFalse(sudokuGrid3.equals(sudokuGrid1));
        assertTrue(sudokuGrid1.GetGrid().equals(sudokuGrid3.GetGrid()));
        assertTrue(sudokuGrid1.GetInitialGrid().equals(sudokuGrid3.GetInitialGrid()));
    }

    @Test
    void testGetAndSetCell() {
        SudokuGrid sudokuGrid = new SudokuGrid("test1.txt");
        System.out.println(sudokuGrid);

        //check initial cells uneditable and illegal inputs
        assertThrows(IllegalArgumentException.class, () -> {
            sudokuGrid.setCell(3, 0, 1); //uneditable cell
            sudokuGrid.setCell(3, 0, null); //uneditable cell
            sudokuGrid.setCell(0, 0, 10);
            sudokuGrid.setCell(0, 0, 0);
            sudokuGrid.setCell(0, 0, -10);
        });

        //check set legal value and legal cell placement
        sudokuGrid.setCell(0, 0, 4);
        assertEquals(4, sudokuGrid.getCell(0, 0));
        sudokuGrid.setCell(0, 0, null);
        assertEquals(null, sudokuGrid.getCell(0, 0));

        //check ability to get value of uneditable cell
        assertEquals(7, sudokuGrid.getCell(3, 0));

    }

    @Test 
    void testObeservers() {
        SudokuGrid sudokuGrid = new SudokuGrid("test1.txt");
        MistakeChecker mistakeChecker = new MistakeChecker();

        //check observer reacts to grid changes
        sudokuGrid.addObserver(mistakeChecker);
        assertTrue(sudokuGrid.GetGrid().equals(mistakeChecker.getInternalGrid().GetGrid()));
        sudokuGrid.setCell(0, 1, 7);
        assertTrue(sudokuGrid.GetGrid().equals(mistakeChecker.getInternalGrid().GetGrid()));
        sudokuGrid.setCell(0, 1, null);
        assertTrue(sudokuGrid.GetGrid().equals(mistakeChecker.getInternalGrid().GetGrid()));

    }

    @Test 
    void testMistakeChecker() {
        SudokuGrid sudokuGrid = new SudokuGrid("test1.txt");
        MistakeChecker mistakeChecker = new MistakeChecker();
        sudokuGrid.addObserver(mistakeChecker);
        System.out.println(sudokuGrid);

        //check square mistake
        sudokuGrid.setCell(0, 0, 4);
        assertTrue(mistakeChecker.checkSquareMistake());
        sudokuGrid.setCell(0, 0, 1);
        assertFalse(mistakeChecker.checkSquareMistake());

        //check row mistake
        sudokuGrid.setCell(1, 0, 7);
        assertTrue(mistakeChecker.checkRowMistake());
        sudokuGrid.setCell(1, 0, 5);
        assertFalse(mistakeChecker.checkRowMistake());

        //check column mistake
        sudokuGrid.setCell(1, 0, 2);
        assertTrue(mistakeChecker.checkColumnMistake());
        sudokuGrid.setCell(1, 0, 3);
        assertFalse(mistakeChecker.checkColumnMistake());
    }

    @Test
    void testFileManager() {
        SudokuGrid sudokuGrid1 = new SudokuGrid("test2.txt");
        FileManager mgr = new FileManager();
        System.out.println(sudokuGrid1);

        //check write and load
        sudokuGrid1.setCell(0, 0, 3);
        mgr.writeBoardToFile(sudokuGrid1, "test3.txt");
        SudokuGrid sudokuGrid2 = new SudokuGrid("test3.txt");
        assertEquals(sudokuGrid1.GetGrid(), sudokuGrid2.GetGrid());
        assertEquals(sudokuGrid1.GetInitialGrid(), sudokuGrid2.GetInitialGrid());
    }

}
