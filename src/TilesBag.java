import java.util.ArrayList;
import java.util.HashMap;

public class TilesBag {

    private HashMap<String, Integer> scrabbleTiles;
    private ArrayList<Tiles> tilesBag;

    public TilesBag() {
        scrabbleTiles = new HashMap();
        tilesBag = new ArrayList();

        scrabbleTiles.put("A", 9);
        scrabbleTiles.put("B", 2);
        scrabbleTiles.put("C", 2);
        scrabbleTiles.put("D", 4);
        scrabbleTiles.put("E", 12);
        scrabbleTiles.put("F", 2);
        scrabbleTiles.put("G", 3);
        scrabbleTiles.put("H", 2);
        scrabbleTiles.put("I", 9);
        scrabbleTiles.put("J", 1);
        scrabbleTiles.put("K", 1);
        scrabbleTiles.put("L", 4);
        scrabbleTiles.put("M", 2);
        scrabbleTiles.put("N", 6);
        scrabbleTiles.put("O", 8);
        scrabbleTiles.put("P", 2);
        scrabbleTiles.put("Q", 1);
        scrabbleTiles.put("R", 6);
        scrabbleTiles.put("S", 4);
        scrabbleTiles.put("T", 6);
        scrabbleTiles.put("U", 4);
        scrabbleTiles.put("V", 2);
        scrabbleTiles.put("W", 2);
        scrabbleTiles.put("X", 1);
        scrabbleTiles.put("Y", 2);
        scrabbleTiles.put("Z", 1);

        for(String s: scrabbleTiles.keySet()){
            if (s.equals("A") || s.equals("E") || s.equals("I") || s.equals("O") ||
                    s.equals("U") || s.equals("L") || s.equals("S") || s.equals("T") ||
                    s.equals("R")){
                tilesBag.add(new Tiles(s, 1));
            }
            else if (s.equals("D") || s.equals("G")) {
                tilesBag.add(new Tiles(s, 2));
            }
            else if (s.equals("B") || s.equals("C") || s.equals("M") || s.equals("P")) {
                tilesBag.add(new Tiles(s, 3));
            }
            else if (s.equals("F") || s.equals("H") || s.equals("V") || s.equals("W") || s.equals("Y")) {
                tilesBag.add(new Tiles(s, 4));
            }
            else if (s.equals("K")) {
                tilesBag.add(new Tiles(s, 5));
            }
            else if (s.equals("J") || s.equals("X")) {
                tilesBag.add(new Tiles(s, 8));
            }
            else{
                tilesBag.add(new Tiles(s, 10));
            }
        }
    }

    public ArrayList<Tiles> bagArraylist(){
        return tilesBag;
    }
}

