package sudoku;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.ArrayList;


public class pathSavingAlgoritm extends MistakeChecker{
    private HashMap<String,List<Integer>> map = new HashMap<String, List<Integer>>();
    private  ListIterator<String> keyIterator;
    private List<String> paths = new ArrayList<String>();
    private List<String> sortedKeys;
    private String path = "";
    String initcr;
    private List<Integer> preAlgo;

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
        preAlgo = internalgrid.GetGrid();
    }

    public void solve() {
        keyIterator = sortedKeys().listIterator();
        sortedKeys = sortedKeys().stream().filter(e -> map.get(e).size() !=1).toList();
        String tmp = keyIterator.next();
        String[] tmp2 = tmp.split(",");
        initcr = tmp;
        iterationAlgorithm(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1]));
    }

    public void iterationAlgorithm(int column, int row) {
        
        System.out.println(path + " | ");
        
        if (sortedKeys.indexOf(column+","+row) < sortedKeys.size()) {
            boolean back = false;
            Integer current = internalgrid.getCell(column,row);
            List<Integer> valuelist= map.get(column+","+row);
            
            for (int i = 0; i <= valuelist.size(); i++) {
                if (i == valuelist.size() && (internalgrid.getCell(column, row) == null || internalgrid.getCell(column, row) == current)) {
                    internalgrid.setCell(column, row, null);
                    back = true;
                    break;
                }
                if (!paths.contains(path+valuelist.get(i)) && (current == null || valuelist.get(i) > current)) {
                    internalgrid.setCell(column, row, valuelist.get(i));
                    if (checkMistake()){
                        internalgrid.setCell(column, row, null);
                    }
                    else {
                        path += ""+valuelist.get(i);
                        //System.out.println("Set: (" + column+","+row+"): " +valuelist.get(i) + " ," + valuelist + " path: " + path);
                        //System.out.println(internalgrid);
                        break;
                    }
                }
            }
            if (!completedGrid()) {
                String tmp;

                if (back) {
                    tmp = keyIterator.previous();
                    if (tmp.equals(column+","+row)) {
                        tmp = keyIterator.previous();
                    }
                    paths.add(path);
                    path = path.substring(0,path.length()-1);
                }
                else {
                    tmp = keyIterator.next();
                    if (tmp.equals(column+","+row)) {
                        tmp = keyIterator.next();
                    }

                }
                String[] tmp2 = tmp.split(",");
                column = Integer.parseInt(tmp2[0]);
                row = Integer.parseInt(tmp2[1]);
                iterationAlgorithm(column, row);
            }
            else {  
                System.out.println("Solved");
                System.out.println(internalgrid);
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
    
    public int calculatePossiblePaths() {
        int start = 1;
        for (String coordinate : map.keySet()) {
            if (map.get(coordinate).size() != 0) {
                start = start * map.get(coordinate).size();
            }
        }
        return start;
    }

    public static void main(String[] args) {
        SudokuGrid grid = new SudokuGrid();
        //MistakeChecker mistakeChecker = new MistakeChecker();
        pathSavingAlgoritm solve = new pathSavingAlgoritm();
        //FileManager mgr = new FileManager();
        System.out.println(grid);
        grid.addObserver(solve);
        solve.scanBoard();
        System.out.println(solve.internalgrid);
        System.out.println(solve.map + "  :  " + solve.map.size());
        System.out.println(solve.sortedKeys().get(0) + ": " + solve.map.get(solve.sortedKeys().get(0)));
        
        //System.out.println(grid.toString());
        
        try {
            solve.solve();
        }
        catch (StackOverflowError e) {
            System.out.println(solve.internalgrid);
            System.out.println(solve.initcr + ": " + solve.map.get(solve.initcr));
            System.out.println("Failure");
        }

        solve.internalgrid.SetGrid(solve.preAlgo);
        System.out.println(solve.internalgrid);
        
        //grid.setCell(3, 3, 8);
        //System.out.println(grid.toString());
        //System.out.println(mistakeChecker.getLegalValues(3, 3));
        
        

    }
}
