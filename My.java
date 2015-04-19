/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.Graphics;
import java.util.*;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 * @author jasdeep
 */
public class My extends JPanel{

    public static void main(String[] argv) {
        MainWindow2 x = new MainWindow2();
    }

}

class MainWindow2 extends JPanel {

    int windowWidth = 820;
    int windowHeight = 350;

    MainWindow2() {
        setLayout(new BorderLayout());

        JFrame loading = new JFrame("loading My.java...please wait");
        JLabel load = new JLabel("loading data...please wait");
        JLabel blank = new JLabel(" ");
        JPanel loadPanel = new JPanel(new GridLayout(0, 3));
        loadPanel.setBackground(Color.GRAY);

        loading.setLayout(new BorderLayout(windowWidth, windowHeight));
        loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loading.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        loading.setLocationRelativeTo(null);
        loading.setResizable(false);

        loadPanel.add(blank);
        loadPanel.add(load);
        loading.add(loadPanel, BorderLayout.CENTER);
        loading.setVisible(true);

        JFrame frame = new JFrame("My.java");

        frame.setLayout(new BorderLayout(windowWidth, windowHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        JPanel panel3 = new JPanel();
        JLabel space = new JLabel("mine ");
        JLabel space2 = new JLabel("mine2 ");

        panel1.setBackground(Color.GRAY);
        panel2.setBackground(Color.GRAY);
        panel3.setBackground(Color.GRAY);

        panel2.add(new visual());
        panel2.add(new Legend());

        panel1.add(panel2, BorderLayout.CENTER);

        frame.add(panel1, BorderLayout.CENTER);

        //frame.pack();
        frame.setVisible(true);
        loading.setVisible(false);
    }
}

class visual extends JComponent {

    Map<String, Float> sectors = new LinkedHashMap<>();
    Map<String, Integer> peopleCount = new LinkedHashMap<>();
    ParseFile x = new ParseFile();
    Groupby g = new Groupby();
    Map<String, Float> map = new LinkedHashMap<String, Float>();
    List<Record> recordList = new ArrayList<>();
      List<String> sectorList = new ArrayList<>();

    visual() {

        try {
            recordList = x.load("output.txt");
            sectorList = group(recordList);
            sectors = g.getSortedMap();
            //n = Groupby.mapToValueList(sectors);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error");
        }

        List<String> List = new ArrayList<String>();
        for (Record iter : recordList) {
            if (List.contains(iter.sector) == false) {
                List.add(iter.sector);
                //System.out.println(iter.position);
            }
        }
        Object[] y = List.toArray();
        //Arrays.sort(y);
        for (int count = 0; count < y.length; count++) {
            peopleCount.put(y[count].toString(), 0);
            for (Record iter : recordList) {
                if (iter.sector.equals(y[count])) {
                    int total = peopleCount.get(y[count].toString());
                    int newTotal = (total + 1);
                    peopleCount.put(y[count].toString(), newTotal);
                }
            }
        }
        //PrintMap(peopleCount);
    }

    public static void PrintMap(Map x) {

        Iterator it = x.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " = ");
            int m = (int) pair.getValue();
            System.out.println((m));
        }
    }

    @Override
    public void paint(Graphics g) {
        drawVisual(g);
    }

    private void drawVisual(Graphics g) {

        //axis
        Graphics2D g2 = (Graphics2D) g;
        int x2Points[] = {40, 40, 300, 250};
        int y2Points[] = {40, 300, 300, 300};
        GeneralPath polyline
                = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x2Points.length);

        polyline.moveTo(x2Points[0], y2Points[0]);

        for (int index = 1; index < x2Points.length; index++) {
            polyline.lineTo(x2Points[index], y2Points[index]);
        }

        g2.draw(polyline);

        //bars (sectors total)
        Iterator iter = map.entrySet().iterator();
        int count = 0, colCount = 0;
        while (iter.hasNext()) {

            Map.Entry pair = (Map.Entry) iter.next();
            int xpos = (Math.round((float) pair.getValue()));

            g2.setColor(myColor.getColor(colCount));
            g2.fill3DRect(47 + count, 300 - (xpos / 10000000), 15, xpos / 10000000, true);

            count = count + 20;
            colCount++;
        }

        //line
        Graphics2D g3 = (Graphics2D) g;
        int xPoints[] = new int[9];
        int yPoints[] = new int[9];
        Iterator iter2 = peopleCount.entrySet().iterator();
        int count3 = 0, count4 = 0;
        while (iter2.hasNext()) {
            Map.Entry pair = (Map.Entry) iter2.next();
            xPoints[count3] = 47 + count4;
            int ypos = (int) pair.getValue();
            yPoints[count3] = 150 - (ypos / 500);
            count4 = count4 + 20;
            count3++;
        }
        g3.setColor(Color.RED);
        GeneralPath polyline2 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);

        polyline2.moveTo(xPoints[0], yPoints[0]);

        for (int index = 1; index < xPoints.length; index++) {
            polyline2.lineTo(xPoints[index], yPoints[index]);
        }

        g3.draw(polyline2);

    }

    public static List mapToValueList(Map x) {
        List<Integer> newList = new ArrayList<>();

        Iterator iter = x.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            newList.add(Math.round((float) pair.getValue()));
        }
        return newList;
    }

    public List<String> group(List oldlist) {

        List<String> List = new ArrayList<String>();
        for (Record iter : recordList) {

            if (List.contains(iter.sector) == false) {
                List.add(iter.sector);
                //System.out.println(iter.sector);
            }
        }

        Object[] x = List.toArray();
     

        for (int count = 0; count < x.length; count++) {
            String curr = x[count].toString();
            map.put(curr, 0.0f);

            for (Record iter : recordList) {

                if (iter.sector.equals(curr)) {
                    float total = map.get(curr);
                    float newTotal = (total + iter.salary);
                    map.put(curr, newTotal);
                }
            }
        }
        return List;
    }

}

