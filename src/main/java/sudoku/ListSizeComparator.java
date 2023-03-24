package sudoku;

import java.util.Comparator;
import java.util.List;

public class ListSizeComparator implements Comparator<List<Integer>>{

    @Override
    public int compare(List<Integer> arg0, List<Integer> arg1) {
        return arg0.size()-arg1.size();
    }
    
}
