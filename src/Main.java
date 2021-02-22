import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Main {
    public static void main (String [] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        AStar s = new AStar();
        String inicial_String = sc.next();
        String objetive_String = sc.next();
        Board inicial = new Board(inicial_String, objetive_String);
        Board objetive = new Board(objetive_String, objetive_String);
        //inicial.setGoal(objetive_String);
        long startTime = System.currentTimeMillis();
        Iterator<AStar.State> it = s.solve(inicial,
                objetive);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        int seconds = (int) (totalTime / 1000) % 60 ;
        int minutes = (int) ((totalTime / (1000*60)) % 60);
        if (it==null) System.out.println("no solution was found");
        else {
            //System.out.println("Total time: " + minutes + "min and " + seconds + "s or " + totalTime + "ms");
            while(it.hasNext()) {
                AStar.State i = it.next();
                if (!it.hasNext()) System.out.println((int)i.getG());
            }
        }
        sc.close();
    }
}
