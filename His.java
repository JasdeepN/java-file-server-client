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

public class His extends JPanel {

    public static void main(String[] argv) {
        MainWindow1 x = new MainWindow1();
    }

    static public void run(){
        MainWindow1 x = new MainWindow1();
    }
}

class MainWindow1 extends JPanel {

    int windowWidth = 500;
    int windowHeight = 350;

    MainWindow1() {

        setLayout(new BorderLayout());

        JFrame loading = new JFrame("loading His.java... please wait");
        JLabel load = new JLabel("loading data...");
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

        JFrame frame = new JFrame("His.java");

        frame.setLayout(new BorderLayout(windowWidth, windowHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new GridLayout(0, 3));
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel(new BorderLayout());
        JLabel xaxis = new JLabel("Income");
        JLabel yaxis = new JLabel("Number of People");
        JLabel space = new JLabel(" ");
        JLabel space2 = new JLabel(" ");

        panel1.setBackground(Color.GRAY);
        panel2.setBackground(Color.GRAY);
        panel3.setBackground(Color.GRAY);

        panel2.add(space, new GridLayout(0, 3));
        panel2.add(space2, new GridLayout(0, 3));
        panel2.add(xaxis, new GridLayout(0, 3));

        panel1.add(panel2, BorderLayout.SOUTH);

        panel1.add(yaxis, BorderLayout.WEST);
        panel1.add(new graph(), BorderLayout.CENTER);

        frame.add(panel1, BorderLayout.CENTER);

        //frame.pack();
        frame.setVisible(true);
        loading.setVisible(false);
    }
}

class graph extends JComponent {

    static Map<Integer, Integer> bins = new LinkedHashMap<>();
    static List<Integer> list = new ArrayList<>();
    ParseFile x = new ParseFile();
    Groupby g = new Groupby();
    List<Record> recordList = new ArrayList<>();
    Map<String, Float> sortedMap = new LinkedHashMap<>();

    graph() {

        try {
            recordList = x.load("output.txt");
            g.setData(recordList);
            g.groupby(3);
            sortedMap = g.getSortedMap();
            binData(sortedMap);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error");
        }

    }

    @Override
    public void paint(Graphics g) {
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

        Iterator iter = bins.entrySet().iterator();
        int count = 0;
        while (iter.hasNext()) {

            Map.Entry pair = (Map.Entry) iter.next();
            int xpos = (int) pair.getValue();

            g2.fill3DRect(40 + count, 300 - (xpos / 125), 5, xpos / 125, true);

            count = count + 3;
        }
    }

    public static void binData(Map x) {
        List<Integer> newList = new ArrayList<>();

        newList = Groupby.mapToValueList(x);
        Object array[] = newList.toArray();

        // System.out.println(Arrays.toString(array));
        int newCount = 0;
        for (Integer count2 = 0; count2 < 100; count2++) {
            Integer ppl = 0;
            Integer count = 0;
            bins.put(count2, 0);

            for (count = 0; count < array.length - 1; count++) {
                if (array[count] != null) {
                    if ((Integer) array[count] < (100000 + (17000 * count2))) {
                        ppl = ppl + 1;
                        array[count] = null;

                    }
                }
            }
            bins.put(count2, ppl);
        }
    }

    public static void PrintMap(Map x) {
        //DecimalFormat df = new DecimalFormat("$###,###,000,000.00");

        Iterator it = x.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " = ");
            //float m = (float)pair.getValue();
            //System.out.println(df.format(m));
            System.out.println(pair.getValue());
        }
    }
}
