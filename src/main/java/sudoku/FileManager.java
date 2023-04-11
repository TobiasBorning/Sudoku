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
            File file = new File ("/Users/tobiasborning/NTNU/Objekt/prosjekt/TDT4100-prosjekt-tobiaslb/src/main/java/sudoku/sudokusaves/"+filename);
            PrintWriter writer = new PrintWriter(file);
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    writer.write(""+board.get((r*9)+c)+",");
                }
                writer.write("\n");
            }
            writer.write("init:\n");
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    writer.write(""+initboard.get((r*9)+c)+",");
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
        File file = new File ("/Users/tobiasborning/NTNU/Objekt/prosjekt/TDT4100-prosjekt-tobiaslb/src/main/java/sudoku/sudokusaves/"+filename);
        Scanner scanner = new Scanner(file);
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
    
    public void deleteFile(String filename) {
        File file = new File("/Users/tobiasborning/NTNU/Objekt/prosjekt/TDT4100-prosjekt-tobiaslb/src/main/java/sudoku/sudokusaves/"+filename);
        if(file.exists()) {
            file.delete();
            System.out.println("File deleted successfully.");
         } else {
            System.out.println("File does not exist.");
         }
   
    }
}
