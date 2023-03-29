package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    public void writeBoardToFile(SudokuGrid grid, String filename) {
        List<Integer> board = grid.GetGrid();
        List<Integer> initboard = grid.GetInitialGrid();
        try {
            PrintWriter writer = new PrintWriter(filename);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(""+board.get((i*9)+j)+",");
                }
                writer.write("\n");
            }
            writer.write("init:\n");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(""+initboard.get((i*9)+j)+",");
                }
                writer.write("\n");
            }
        
            writer.flush();
            writer.close(); 
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<List<Integer>> readBoardFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> loadedGrid = new ArrayList<Integer>();
        List<Integer> initialGrid = new ArrayList<Integer>();
        List<List<Integer>> outArray = new ArrayList<List<Integer>>(List.of(loadedGrid,initialGrid));
        int index = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineInfo = line.split(","); 
            if (line.equals("init:")) {
                line = scanner.nextLine();
                lineInfo = line.split(",");
                index = 1;
            }
            for (int i = 0; i < 9; i++) {
                if (lineInfo[i].equals("null")) {
                    outArray.get(index).add(null);
                }
                else {
                    outArray.get(index).add(Integer.parseInt(lineInfo[i]));
                }
            }
        }
        scanner.close(); // har ikke .flush()
        return outArray;
    }
}
