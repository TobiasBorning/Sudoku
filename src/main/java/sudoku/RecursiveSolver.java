package sudoku;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

public class RecursiveSolver extends MistakeChecker{
    HashMap<String,List<Integer>> map;
    
    ListIterator<String> keyIterator;
    int placements = 0;
    int test = 0;

    public void place(int column, int row, int iterations, boolean enableIt) {
        test++;
        List<Integer> legal = new ArrayList<>(getLegalValues(column, row));
        //System.out.println("("+column+","+row+")"+legal.toString());
        String cr = column+","+row;
        map.put(cr,legal);
        Integer current = internalgrid.getCell(column, row);

        if (legal.size() == 1 && current != legal.get(0) && current == null){
            placements++;
            internalgrid.setCell(column, row, legal.get(0));
            System.out.println(internalgrid.toString());
            System.out.println("Placed " + legal.get(0) + " at (" + cr  +") Placements: "+ placements);
        }
        if (enableIt) {
            
            /*
            String tmp = keyIterator.next();
            String[] tmp2 = tmp.split(",");
            init = tmp;
            iterationAlgorithm(Integer.parseInt(tmp2[0]),Integer.parseInt(tmp2[1]));
            */
        }
        //loop
        if (column < 8){
            column ++;
        }
        else if (column >= 8 && row >= 8) {
            column = 0;
            row = 0;
            iterations++;
        }
        else {
            column = 0;
            row++;
        }

        //stop switch
        /* 
        if (iterations == 7) {
            keyIterator = sortedKeys().listIterator();
            place(column, row, iterations, true);
        }
        else if (iterations > 7 && iterations < 40) {
            place(column, row, iterations, true);
        }*/
        if (iterations < 7 && !completedGrid()) {
            //System.out.println(internalgrid.toString());
            place(column, row, iterations, false);
        }
        else {
            System.out.println("Ferdig");
            System.out.println("Iterajoner:" + iterations +" Placements: "+placements);
            System.out.println(test);
            System.out.println(internalgrid.toString());            
            
        }
    }
    public void solve() {
        map = new HashMap<String,List<Integer>>();
        place(0, 0, 0, false);
    }

    public void iterationAlgorithm(int column, int row) {
        //System.out.println(column+","+row+" : " + map.get(column+","+row));
        System.out.println(internalgrid);

        if (sortedKeys().indexOf(column+","+row) < sortedKeys().size()) {
            boolean back = false;
            Integer current = internalgrid.getCell(column,row);
            List<Integer> valuelist= map.get(column+","+row);
            if (valuelist.size() == 1) {
                internalgrid.setCell(column, row, valuelist.get(0));
            }
            else {
                for (int i = 0; i <= valuelist.size(); i++) {
                    if (i == valuelist.size() && (internalgrid.getCell(column, row) == null || internalgrid.getCell(column, row) == current)) {
                        internalgrid.setCell(column, row, null);
                        back = true;
                        break;
                    }
                    try {
                        if (current != null) {
                            int v = valuelist.indexOf(current);
                            if (valuelist.get(i) <= current && v != valuelist.size()-1) {
                                i = v+1;
                            }
                            else {
                                i = valuelist.size()-1;
                            }
                        }
                    }
                    catch (IndexOutOfBoundsException e) {

                    }
                    if (internalgrid.getCell(column, row) == null || internalgrid.getCell(column, row) == current) {
                        internalgrid.setCell(column, row, valuelist.get(i));
                        if (checkMistake()){
                            internalgrid.setCell(column, row, null);
                        }
                        else {
                            //System.out.println("Set: (" + column+","+row+"): " +valuelist.get(i) + " ," + valuelist + " init: "+ init);
                            System.out.println(internalgrid);
                        }
                    }
                }
            }
            
            if (!completedGrid()) {
                String tmp;
                if (back) {
                    tmp = keyIterator.previous();
                }
                else {
                    tmp = keyIterator.next();
                }
                String[] tmp2 = tmp.split(",");
                if (map.get(column+","+row).size() > 3) {
                    System.out.println("");
                }
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
            valuelist = valuelist.stream().filter(l -> l.size()!=0).toList();
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

    public static void main(String[] args) {
        //RecursiveSolver solve = new RecursiveSolver();
        
    }
}
