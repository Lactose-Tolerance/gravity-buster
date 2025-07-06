package com.shino.sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class evolveThread extends Thread{
    nBodySystem S;
    public evolveThread(nBodySystem S){
        this.S = S;
    }
    
    @Override
    @SuppressWarnings("")
    public void run(){
        while(true){
            try{
                sleep(0);
                S.evolve();
            }
            catch(InterruptedException e){
                break;
            }
        }
    }
}

class showThread extends Thread{
    Screen S;
    public showThread(Screen S){
        this.S = S;
    }
    
    @Override
    @SuppressWarnings("")
    public void run(){
        while(true){
            try{
                sleep(10);
                S.display();
            }
            catch(InterruptedException e){
                break;
            }
        }
    }
}

public final class Screen{
    private final JFrame frame;
    private final JPanel headPanel, controlPanel, titlePanel, timePanel;
    private DrawPanel drawPanel;
    private final JLabel timeLabel, titleLabel;
    private final JButton playButton, resetButton;
    private final JCheckBox showLabels;
    private final JCheckBox showTrails;
    private final JSlider timeSlider;

    private final nBodySystem ob;
    
    private Thread t1 = null, t2 = null;

    private boolean isPlaying = false;

    private final ClassLoader classLoader;

    class DrawPanel extends JPanel{
        private final ArrayList<Particle> P;
        public DrawPanel(ArrayList<Particle> P){
            this.P = P;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            for(Particle p : P){
                if(showTrails.isSelected()){
                    ArrayList<int[]> past = p.past;
                    double opacity = 0;
                    double step = 0.75 / past.size();
                    for(int i=1; i<past.size(); i++){
                        try{
                            g.setColor(new Color((int) Math.round(p.rr*opacity), (int) Math.round(p.gg*opacity), (int) Math.round(p.bb*opacity)));
                            opacity += step;
                            g.drawLine(past.get(i)[0], past.get(i)[1], past.get(i-1)[0], past.get(i-1)[1]);
                        }
                        catch(Exception e){
                            // ignore
                        }
                    }
                }
                
                g.setColor(new Color(p.rr, p.gg, p.bb));
                g.fillOval((int)(Math.round(p.r[0]) - p.d/2.0), (int)(Math.round(p.r[1])- p.d/2.0), Math.max(p.d, 1), Math.max(p.d, 1));
                if(p.rr + p.gg + p.bb < 63){
                    g.setColor(Color.WHITE);
                    g.drawOval((int)(Math.round(p.r[0]) - p.d/2.0), (int)(Math.round(p.r[1])- p.d/2.0), Math.max(p.d, 1), Math.max(p.d, 1));
                }
            }
        }
    }

    @SuppressWarnings("Convert2Lambda")
    Screen(Window window, nBodySystem ob){
        this.ob = ob;

        frame = new JFrame();
        frame.setTitle("GRAVITATION!!!!!!");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                window.frame.setVisible(true);
                if(t1 != null && t1.isAlive())
                    t1.interrupt();
                if(t2 != null && t2.isAlive())
                    t2.interrupt();
                frame.dispose();
            }
        });
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon.png")));

        headPanel = new JPanel();
        frame.add(headPanel, BorderLayout.NORTH);
        headPanel.setBackground(Color.BLACK);
        headPanel.setLayout(new GridLayout(1,3));

        timePanel = new JPanel();
        headPanel.add(timePanel);
        timePanel.setBackground(Color.BLACK);
        timePanel.setLayout(new GridLayout(1, 2));

        timeLabel = new JLabel("      Speed: 1x");
        timePanel.add(timeLabel);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Helvetica", Font.PLAIN, 15));

        timeSlider = new JSlider(0, 8, 4);
        timePanel.add(timeSlider);
        timeSlider.setBackground(Color.BLACK);
        timeSlider.setFocusable(false);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintTrack(true);
        timeSlider.setPaintLabels(true);
        timeSlider.setMajorTickSpacing(1);
        timeSlider.setSnapToTicks(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("0"));
        labels.put(1, new JLabel(".25"));
        labels.put(2, new JLabel(".5"));
        labels.put(3, new JLabel(".75"));
        labels.put(4, new JLabel("1"));
        labels.put(5, new JLabel("2"));
        labels.put(6, new JLabel("3"));
        labels.put(7, new JLabel("4"));
        labels.put(8, new JLabel("5"));
        for(JLabel label : labels.values()){
            label.setForeground(Color.WHITE);
        }
        timeSlider.setLabelTable(labels);
        timeSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                timeLabel.setText("      Speed: " + labels.get(timeSlider.getValue()).getText() + "x");
                ob.timeScale = Double.parseDouble(labels.get(timeSlider.getValue()).getText()) * 0.0000075;
            }
        });
        timeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titlePanel = new JPanel();
        headPanel.add(titlePanel);
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setLayout(new FlowLayout());
        titleLabel = new JLabel("Gravitation");
        titlePanel.add(titleLabel);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));

        controlPanel = new JPanel();
        headPanel.add(controlPanel);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
        controlPanel.setBackground(Color.BLACK);

        controlPanel.add(Box.createHorizontalGlue());

        classLoader = Thread.currentThread().getContextClassLoader();

        showTrails = new JCheckBox("Trails    ");
        controlPanel.add(showTrails);
        showTrails.setForeground(Color.WHITE);
        showTrails.setBackground(Color.BLACK);
        showTrails.setBorder(null);
        showTrails.setFocusable(false);
        showTrails.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                display();
            }
        });

        showLabels = new JCheckBox("Labels    ");
        controlPanel.add(showLabels);
        showLabels.setForeground(Color.WHITE);
        showLabels.setBackground(Color.BLACK);
        showLabels.setBorder(null);
        showLabels.setFocusable(false);
        showLabels.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                display();
            }
        });

        controlPanel.add(Box.createHorizontalGlue());
        
        playButton = new JButton();
        controlPanel.add(playButton);
        playButton.setFocusable(false);
        playButton.setIcon(new ImageIcon(classLoader.getResource("img/play.png")));
        playButton.setBackground(new Color(220, 220, 220));
        playButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(isPlaying){
                    pause();
                }
                else{
                    play();
                }
            }
        });
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        resetButton = new JButton();
        controlPanel.add(resetButton);
        resetButton.setFocusable(false);
        resetButton.setIcon(new ImageIcon(classLoader.getResource("img/reset.png")));
        resetButton.setBackground(new Color(220, 220, 220));
        resetButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                reset();
            }
        });
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        drawPanel = new DrawPanel(ob.P);
        frame.add(drawPanel);
        display();
    }
    
    final void pause(){
        if(t1 != null && t1.isAlive())
            t1.interrupt();
        if(t2 != null && t2.isAlive())
            t2.interrupt();
        playButton.setIcon(new ImageIcon(classLoader.getResource("img/play.png")));
        isPlaying = false;
    }
    
    final void play(){
        t1 = new evolveThread(ob);
        t2 = new showThread(this);
        t1.start();
        t2.start();
        isPlaying = true;
        playButton.setIcon(new ImageIcon(classLoader.getResource("img/pause.png")));
    }
    
    final void reset(){
        pause();
        ob.reset();
        display();
    }
    
    final void display(){
        frame.remove(drawPanel);
        drawPanel = new DrawPanel(ob.P);
        drawPanel.setBackground(Color.BLACK);
        drawPanel.setLayout(null);
        frame.add(drawPanel);
        if(showLabels.isSelected()){
            for(Particle p : ob.P){
                JLabel nametag = new JLabel();
                nametag.setFont(new Font("Helvetica", Font.PLAIN, 12));
                nametag.setForeground(Color.WHITE);
                nametag.setBounds((int)Math.round(p.r[0] - 50), (int)Math.round(p.r[1] - p.d/2.0 - 15), 100, 15);
                nametag.setText(p.name);
                nametag.setBorder(new EmptyBorder(0, 5, 0, 5));
                nametag.setHorizontalAlignment(JLabel.CENTER);
                drawPanel.add(nametag);
            }
        }
        frame.revalidate();
        frame.repaint();
    }
    
    void show(){
        frame.setVisible(true);
    }
}

/*public class Main{
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                nBodySystem ns = new nBodySystem(4, "100 150 70 -16 4 255 100 100 1000 130 130 0 0 255 255 255 50 150 220 10 -2 100 255 100 25 180 200 -13 -3 255 255 255 1");
                Screen ob = new Screen(new Window(), ns);
                ob.show();
            }
        });
    }
}*/