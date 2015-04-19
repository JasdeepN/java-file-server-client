/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Pie extends JFrame {

    public static void main(String[] args) {
     
        MainWindow x = new MainWindow(args[0]);

    }
}

class MainWindow extends JPanel {

    int windowWidth = 850;
    int windowHeight = 400;

    MainWindow(String args) {
        setLayout(new BorderLayout());

        JFrame loading = new JFrame("loading Pie.java... please wait");
        JLabel load = new JLabel("loading data... please wait");
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

        JFrame frame = new JFrame("Pie.java");

        frame.setLayout(new BorderLayout(windowWidth, windowHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel1 = new JPanel(new GridLayout(1, 1));
        panel1.setBackground(Color.GRAY);

        panel1.add(new PieChart(args));
        panel1.add(new Legend());

        frame.add(panel1, BorderLayout.CENTER);

        //frame.pack();
        frame.setVisible(true);
        loading.setVisible(false);
    }

}

class Slice {

    float value;
    Color color;

    public Slice(float x, Color y) {
        this.value = x;
        this.color = y;
    }
}

class PieChart extends JComponent {

    Slice[] slices = new Slice[9];

    PieChart(String args) {
        Map<String, Float> sortedMap = new LinkedHashMap<>();
        ParseFile x = new ParseFile();
        Groupby g = new Groupby();
        List<Record> recordList = new ArrayList<>();
        List<Integer> n = new ArrayList<>();
        try {
            recordList = x.load(args);
            g.setData(recordList);
            g.groupby(0);
            sortedMap = g.getSortedMap();
            n = Groupby.mapToValueList(sortedMap);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error");
        }

        for (int count = 0; count < 9; count++) {
            Color col = (myColor.getColor(count));

            slices[count] = new Slice(n.get(count), col);
        }
    }

    @Override
    public void paint(Graphics g) {
        drawPie((Graphics2D) g, getBounds(), slices);

    }

    void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
        float total = 0.0f;
        for (int count = 0; count < slices.length; count++) {
            total += slices[count].value;
        }

        float Value = 0.0f;
        int startAngle = 0;

        for (int count = 0; count < slices.length; count++) {

            startAngle = (int) (Value * 360 / total);
            int arcAngle = (int) (slices[count].value * 360 / total);

            g.setColor(slices[count].color);
            g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

            Value += slices[count].value;
        }
    }
}

