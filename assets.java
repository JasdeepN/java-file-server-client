import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 *assests contains classes used by client and server as well as data visualization programs
 * @author jasdeep
 */
public class assets {

}

class myColor {

    /**
    *gets a number (up to 7) and then returns an assosiated colour.
    *
    *@pram integer value for the number of the colour being requested
    *@return the colour at the index of the pram 'k'
    */

    public static Color getColor(int k) {
        Color col[] = {new Color(255, 105, 97), new Color(119, 158, 203), new Color(179, 158, 181), new Color(100, 20, 100),
            new Color(119, 190, 119), new Color(253, 253, 150), new Color(207, 207, 196), new Color(255, 179, 71), new Color(244, 154, 194)};

            return col[k];
        }
    }


    /**
    *creates a visual legend based output.txt
    *
    *@pram nothing
    *@return a visual legend is displayed on the parent window
    */

    class Legend extends JComponent {

        LegendEntry[] leg = new LegendEntry[9];
        List<String> newList = new ArrayList<>();

        Legend() {
            Map<String, Float> sortedMap = new LinkedHashMap<>();
            ParseFile x = new ParseFile();
            Groupby g = new Groupby();
            List<Record> recordList = new ArrayList<>();
        //List<Integer> n = new ArrayList<>();
            try {
                recordList = x.load("output.txt");
                g.setData(recordList);
                g.groupby(0);
                sortedMap = g.getUnSortedMap();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error");
            }

            Iterator iter = sortedMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry pair = (Map.Entry) iter.next();
                newList.add(pair.getKey().toString());
            }
        }

        @Override
        public void paint(Graphics g) {
            drawLegend(g, leg);
        }

        private void drawLegend(Graphics g, LegendEntry[] leg) {

            for (int count = 0; count < leg.length; count++) {
                Color col = myColor.getColor(count);

                leg[count] = new LegendEntry(newList.get(count), col);

            }
            for (int count2 = 0; count2 < leg.length; count2++) {
                g.setColor(myColor.getColor(count2));
                g.drawString(leg[count2].name, 50, 50 + (15 * count2));
            }
        }

        class LegendEntry {

            String name;
            Color color;

            public LegendEntry(String name, Color color) {
                this.name = name;
                this.color = color;
            }
        }
    }
