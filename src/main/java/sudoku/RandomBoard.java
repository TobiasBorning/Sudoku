package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomBoard {
    HashMap<Integer,List<Integer>> boards = new HashMap<Integer,List<Integer>>();

    private void CollectBoards(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> loadedGrid = new ArrayList<Integer>();
        int board = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineInfo = line.split("");
            for (int j = 0; j < 81; j++) {
                if (lineInfo[j].equals("0")) {
                    loadedGrid.add(null);
                }
                else {
                    loadedGrid.add(Integer.parseInt(lineInfo[j]));
                }
            }
            boards.put(board, loadedGrid);
            loadedGrid = new ArrayList<Integer>();
            board++;
        }
        scanner.close(); // har ikke .flush()
    }

    public List<Integer> getRandomBoard() {
        try {
            CollectBoards("sudokuEasy.txt"); 
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        Random rn = new Random();
        int board = rn.nextInt(0,50);
        return boards.get(board);
    }


    public static void main(String[] args) {
        RandomBoard rn = new RandomBoard();
        System.out.println(rn.getRandomBoard());
    }
}
