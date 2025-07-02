package com.shino.sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window{
    JFrame frame;
    JPanel headPanel, mainPanel, bodyPanel, listPanel, confirmPanel;
    JLabel headLabel;
    JButton addButton, removeButton, confirmButton, saveButton, loadButton;
    JDialog save, load;
    ArrayList<JTextField> names = new ArrayList<>(), masses = new ArrayList<>(), xposes = new ArrayList<>(), yposes = new ArrayList<>(), xvels = new ArrayList<>(), yvels = new ArrayList<>(), r = new ArrayList<>(), g = new ArrayList<>(), b = new ArrayList<>();
    ArrayList<JComboBox<String>> colors = new ArrayList<>();
    int n = 0;

    private String slugify(String s){
        s = s.trim();
        char[] c = new char[s.length()];
        boolean flag = true;
        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            if(ch == ' ') c[i] = '_';
            else{
                c[i] = ch;
                flag = false;
            }
        }
        if(flag) return "";
        else return new String(c);
    }
    
    @SuppressWarnings("Convert2Lambda")
    Window(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        frame = new JFrame();
        frame.setTitle("GRAVITATION!!!!!!");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon.png")));

        headPanel = new JPanel();
        frame.add(headPanel, BorderLayout.NORTH);
        headPanel.setLayout(new FlowLayout());
        headPanel.setBackground(new Color(0, 0, 0));

        headLabel = new JLabel();
        headPanel.add(headLabel);
        headLabel.setText("Gravitation");
        headLabel.setForeground(Color.WHITE);
        headLabel.setFont(new Font("Helvetica", Font.BOLD, 15));

        mainPanel = new JPanel();
        frame.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        
        bodyPanel = new JPanel();
        mainPanel.add(bodyPanel, BorderLayout.NORTH);

        addButton = new JButton("Add new");
        bodyPanel.add(addButton);
        addButton.setEnabled(true);
        addButton.setFocusable(false);
        addButton.setBackground(new Color(220, 220, 220));
        addButton.setIcon(new ImageIcon(classLoader.getResource("img/add.png")));
        addButton.setIconTextGap(5);
        addButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                addBody();
            }
        });
        addButton.setPreferredSize(new Dimension(80, 25));
        addButton.setBorder(null);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        removeButton = new JButton("Remove");
        bodyPanel.add(removeButton);
        removeButton.setEnabled(false);
        removeButton.setFocusable(false);
        removeButton.setBackground(new Color(220, 220, 220));
        removeButton.setIcon(new ImageIcon(classLoader.getResource("img/remove.png")));
        removeButton.setIconTextGap(5);
        removeButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                removeBody();
            }
        });
        removeButton.setPreferredSize(new Dimension(80, 25));
        removeButton.setBorder(null);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        listPanel = new JPanel();
        mainPanel.add(listPanel);
        listPanel.setLayout(new GridLayout(2, 5, 10, 5));
        for(int i=0; i<10; i++){
            JPanel newBody = new JPanel();
            listPanel.add(newBody);
            newBody.setLayout(new GridLayout(0, 1, 0, 0));
            
            JPanel name = new JPanel();
            name.setBackground(new Color(220, 220, 220));
            newBody.add(name);
            name.setLayout(new FlowLayout());
            JTextField nameField = new JTextField(13);
            nameField.setText("Particle " + (i+1));
            nameField.setBackground(new Color(230, 230, 230));
            nameField.setHorizontalAlignment(JTextField.CENTER);
            name.add(nameField);
            nameField.setFont(new Font("Helvetica", Font.BOLD, 12));
            nameField.setBorder(null);

            JPanel mass = new JPanel();
            mass.setBackground(new Color(220, 220, 220));
            newBody.add(mass);
            JLabel massLabel = new JLabel("      Mass = ");
            mass.add(massLabel);
            massLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField massField = new JTextField(5);
            mass.add(massField);
            massField.setHorizontalAlignment(JTextField.CENTER);
            massField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            JPanel xpos = new JPanel();
            xpos.setBackground(new Color(220, 220, 220));
            newBody.add(xpos);
            JLabel xposLabel = new JLabel("X-Position = ");
            xpos.add(xposLabel);
            xposLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField xposField = new JTextField(5);
            xpos.add(xposField);
            xposField.setHorizontalAlignment(JTextField.CENTER);
            xposField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            
            JPanel ypos = new JPanel();
            ypos.setBackground(new Color(220, 220, 220));
            newBody.add(ypos);
            JLabel yposLabel = new JLabel("Y-Position = ");
            ypos.add(yposLabel);
            yposLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField yposField = new JTextField(5);
            ypos.add(yposField);
            yposField.setHorizontalAlignment(JTextField.CENTER);
            yposField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            
            JPanel xvel = new JPanel();
            xvel.setBackground(new Color(220, 220, 220));
            newBody.add(xvel);
            JLabel xvelLabel = new JLabel("X-Velocity = ");
            xvel.add(xvelLabel);
            xvelLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField xvelField = new JTextField(5);
            xvel.add(xvelField);
            xvelField.setHorizontalAlignment(JTextField.CENTER);
            xvelField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            
            JPanel yvel = new JPanel();
            yvel.setBackground(new Color(220, 220, 220));
            newBody.add(yvel);
            JLabel yvelLabel = new JLabel("Y-Velocity = ");
            yvel.add(yvelLabel);
            yvelLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField yvelField = new JTextField(5);
            yvel.add(yvelField);
            yvelField.setHorizontalAlignment(JTextField.CENTER);
            yvelField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            JPanel color = new JPanel();
            newBody.add(color);
            color.setBackground(new Color(220, 220, 220));
            JLabel colorLabel = new JLabel("Colour = ");
            color.add(colorLabel);
            colorLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            String[] options = new String[]{"White", "Red", "Blue", "Green", "Magenta", "Cyan", "Yellow", "Custom"};
            JComboBox<String> dropdown = new JComboBox<>(options);
            color.add(dropdown);
            dropdown.setFont(new Font("Helvetica", Font.PLAIN, 10));
            dropdown.setBounds(50, 50, 80, 30);
            DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
            listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
            dropdown.setRenderer(listRenderer);

            JPanel colorInput = new JPanel();
            newBody.add(colorInput);
            colorInput.setBackground(new Color(220, 220, 220));

            JLabel redLabel = new JLabel("r:");
            colorInput.add(redLabel);
            redLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField redField = new JTextField(2);
            colorInput.add(redField);
            redField.setHorizontalAlignment(JTextField.CENTER);
            redField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            JLabel greenLabel = new JLabel("g:");
            colorInput.add(greenLabel);
            greenLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField greenField = new JTextField(2);
            colorInput.add(greenField);
            greenField.setHorizontalAlignment(JTextField.CENTER);
            greenField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            JLabel blueLabel = new JLabel("b:");
            colorInput.add(blueLabel);
            blueLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
            JTextField blueField = new JTextField(2);
            colorInput.add(blueField);
            blueField.setHorizontalAlignment(JTextField.CENTER);
            blueField.setFont(new Font("Helvetica", Font.PLAIN, 10));

            redField.setText("255");
            greenField.setText("255");
            blueField.setText("255");
            redField.setEnabled(false);
            greenField.setEnabled(false);
            blueField.setEnabled(false);

            dropdown.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    String selected = (String) dropdown.getSelectedItem();
                    if(selected.equals("Custom")){
                        redField.setEnabled(true);
                        greenField.setEnabled(true);
                        blueField.setEnabled(true);
                    }
                    else{
                        redField.setEnabled(false);
                        greenField.setEnabled(false);
                        blueField.setEnabled(false);
                        switch (selected) {
                            case "White" -> {
                                redField.setText("255");
                                greenField.setText("255");
                                blueField.setText("255");
                            }
                            case "Red" -> {
                                redField.setText("255");
                                greenField.setText("127");
                                blueField.setText("127");
                            }
                            case "Blue" -> {
                                redField.setText("127");
                                greenField.setText("127");
                                blueField.setText("255");
                            }
                            case "Green" -> {
                                redField.setText("127");
                                greenField.setText("255");
                                blueField.setText("127");
                            }
                            case "Magenta" -> {
                                redField.setText("255");
                                greenField.setText("63");
                                blueField.setText("255");
                            }
                            case "Cyan" -> {
                                redField.setText("63");
                                greenField.setText("255");
                                blueField.setText("255");
                            }
                            case "Yellow" -> {
                                redField.setText("255");
                                greenField.setText("255");
                                blueField.setText("63");
                            }
                            default -> {
                            }
                        }
                    }
                }
            });

            newBody.setVisible(false);

            names.add(nameField);
            masses.add(massField);
            xposes.add(xposField);
            yposes.add(yposField);
            xvels.add(xvelField);
            yvels.add(yvelField);
            colors.add(dropdown);
            r.add(redField);
            g.add(greenField);
            b.add(blueField);
        }

        confirmPanel = new JPanel();
        mainPanel.add(confirmPanel, BorderLayout.SOUTH);
        confirmPanel.setLayout(new FlowLayout());

        saveButton = new JButton("Save");
        confirmPanel.add(saveButton);
        saveButton.setEnabled(false);
        saveButton.setFocusable(false);
        saveButton.setBackground(new Color(200, 200, 200));
        saveButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    save();
                }
                catch(FileNotFoundException ex){
                    System.out.println(ex);
                }
            }
        });
        saveButton.setBorder(null);
        saveButton.setPreferredSize(new Dimension(80,25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loadButton = new JButton("Load");
        confirmPanel.add(loadButton);
        loadButton.setEnabled(true);
        loadButton.setFocusable(false);
        loadButton.setBackground(new Color(200, 200, 200));
        loadButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        loadButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    load();
                }
                catch(FileNotFoundException ex){
                    System.out.println(ex);
                }
            }
        });
        loadButton.setBorder(null);
        loadButton.setPreferredSize(new Dimension(80,25));
        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmButton = new JButton("Confirm");
        confirmPanel.add(confirmButton);
        confirmButton.setEnabled(false);
        confirmButton.setFocusable(false);
        confirmButton.setBackground(new Color(200, 200, 200));
        confirmButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        confirmButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                execute();
            }
        });
        confirmButton.setBorder(null);
        confirmButton.setPreferredSize(new Dimension(80,25));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    void addBody(){
        listPanel.getComponent(n++).setVisible(true);
        if(n == 1) removeButton.setEnabled(true);
        if(n == 2){
            confirmButton.setEnabled(true);
            saveButton.setEnabled(true);
        }
        if(n == 10) addButton.setEnabled(false);
    }

    void removeBody(){
        listPanel.getComponent(--n).setVisible(false);
        if(n == 9) addButton.setEnabled(true);
        if(n == 1){
            confirmButton.setEnabled(false);
            saveButton.setEnabled(false);
        }
        if(n == 0) removeButton.setEnabled(false);
    }

    @SuppressWarnings("Convert2Lambda")
    void execute(){
        String inputStream = n + " ";
        for(int i=0; i<n; i++){
            inputStream += slugify(names.get(i).getText()) + " " + masses.get(i).getText() + " " + xposes.get(i).getText() + " " + yposes.get(i).getText() + " " + xvels.get(i).getText() + " " + yvels.get(i).getText() + " " + r.get(i).getText() + " " + g.get(i).getText() + " " + b.get(i).getText() + " ";
        }
        inputStream += "1";
        try{
            Screen s = new Screen(this, new nBodySystem(inputStream));
            s.show();
            frame.setVisible(false);
        }
        catch(Exception e){
            JDialog error = new JDialog(frame, "Error", true);
            error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            error.setSize(300, 120);
            error.setResizable(false);
            error.setLayout(new GridLayout(2, 1, 0, 0));
            error.setLocationRelativeTo(null);
            
            JPanel labelPanel = new JPanel();
            error.add(labelPanel);
            JLabel label = new JLabel("Some input fields may be empty or invalid.");
            labelPanel.add(label);
            label.setFont(new Font("Helvetica", Font.PLAIN, 12));
            
            JPanel buttonPanel = new JPanel();
            error.add(buttonPanel);
            JButton button = new JButton("OK");
            buttonPanel.add(button);
            button.setFocusable(false);
            button.setBackground(new Color(220, 220, 220));
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    error.dispose();
                }
            });

            error.setVisible(true);
        }
    }

    @SuppressWarnings({ "Convert2Lambda", "empty-statement" })
    void save() throws FileNotFoundException{
        try{
            for(int i=0; i<n; i++){
                Double.valueOf(masses.get(i).getText());
                Double.valueOf(xposes.get(i).getText());
                Double.valueOf(yposes.get(i).getText());
                Double.valueOf(xvels.get(i).getText());
                Double.valueOf(yvels.get(i).getText());
                Integer.valueOf(r.get(i).getText());
                Integer.valueOf(g.get(i).getText());
                Integer.valueOf(b.get(i).getText());
            }
        }
        catch(NumberFormatException e){
            JDialog error = new JDialog(frame, "Error", true);
            error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            error.setSize(300, 120);
            error.setResizable(false);
            error.setLayout(new GridLayout(2, 1, 0, 0));
            error.setLocationRelativeTo(null);
            
            JPanel labelPanel = new JPanel();
            error.add(labelPanel);
            JLabel label = new JLabel("Some input fields may be empty or invalid.");
            labelPanel.add(label);
            label.setFont(new Font("Helvetica", Font.PLAIN, 12));
            
            JPanel buttonPanel = new JPanel();
            error.add(buttonPanel);
            JButton button = new JButton("OK");
            buttonPanel.add(button);
            button.setFocusable(false);
            button.setBackground(new Color(220, 220, 220));
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    error.dispose();
                }
            });

            error.setVisible(true);
            return;
        }

        save = new JDialog(frame, "Save", true);
        save.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        save.setSize(500, 500);
        save.setLayout(new BorderLayout(0, 0));
        save.setLocationRelativeTo(null);

        JPanel saveHeadPanel = new JPanel();
        save.add(saveHeadPanel, BorderLayout.NORTH);
        saveHeadPanel.setBackground(Color.BLACK);

        JLabel saveHeadLabel = new JLabel("Save Files");
        saveHeadPanel.add(saveHeadLabel);
        saveHeadLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        saveHeadLabel.setForeground(Color.WHITE);

        JPanel savePanel = new JPanel();
        save.add(savePanel, BorderLayout.SOUTH);
        savePanel.setLayout(new BorderLayout(5, 0));

        JLabel saveLabel = new JLabel("  Save name: ");
        savePanel.add(saveLabel, BorderLayout.WEST);
        saveLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));

        JButton saveButton2 = new JButton("Save");
        savePanel.add(saveButton2, BorderLayout.EAST);
        saveButton2.setFocusable(false);
        saveButton2.setBackground(new Color(200, 200, 200));
        saveButton2.setFont(new Font("Helvetica", Font.BOLD, 12));
        saveButton2.setPreferredSize(new Dimension(80, 25));
        saveButton2.setBorder(null);
        saveButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JTextField saveField = new JTextField(0);
        savePanel.add(saveField);
        saveField.setFont(new Font("Helvetica", Font.PLAIN, 12));
        
        saveButton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    if(addSave(saveField.getText()))
                        save.dispose();
                }
                catch(IOException ex){
                    System.out.println(ex);
                }
            }
        });

        JPanel saveListPanel = new JPanel();
        save.add(saveListPanel);
        saveListPanel.setLayout(new GridLayout(15,2));
        
        try (Scanner sc = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"))) {
            int N = sc.nextInt();
            sc.nextLine();
            JPanel[] saves = new JPanel[N];
            for(int i=0; i<N; i++){
                saves[i] = new JPanel(new GridLayout());
                saveListPanel.add(saves[i]);

                saves[i].add(new JLabel());

                sc.nextInt();
                JPanel namePanel = new JPanel(){
                    @Override
                    public boolean contains(int x, int y){
                        return false;
                    }
                };
                saves[i].add(namePanel);
                
                JLabel nameLabel = new JLabel(sc.nextLine());
                namePanel.add(nameLabel);

                saves[i].setFont(new Font("Helvetica", Font.PLAIN, 12));
                
                JPanel buttonPanel = new JPanel();
                saves[i].add(buttonPanel);
                buttonPanel.setLayout(new BorderLayout());

                JPanel buttonPanel2 = new JPanel();
                buttonPanel.add(buttonPanel2, BorderLayout.EAST);

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                
                JPanel x = saves[i];

                JButton renameButton = new JButton();
                buttonPanel2.add(renameButton);
                renameButton.setIcon(new ImageIcon(classLoader.getResource("img/rename.png")));
                renameButton.setBackground(new Color(200, 200, 200));
                renameButton.setBorder(null);
                renameButton.setVisible(false);
                renameButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        String newName = renameSave(nameLabel.getText(), save);
                        if(newName != null){
                            nameLabel.setText(newName);
                        }
                    }
                });
                renameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JButton deleteButton = new JButton();
                buttonPanel2.add(deleteButton);
                deleteButton.setIcon(new ImageIcon(classLoader.getResource("img/delete.png")));
                deleteButton.setBackground(new Color(200, 200, 200));
                deleteButton.setBorder(null);
                deleteButton.setVisible(false);
                deleteButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(deleteSave(nameLabel.getText(), load)){
                            saveListPanel.remove(x);
                            saveListPanel.revalidate();
                            saveListPanel.repaint();
                        }
                        try(Scanner sc2 = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"))){
                            if(sc2.nextInt() == 0) saveButton2.setEnabled(false);
                        }
                        catch(FileNotFoundException ex){
                            System.out.println(ex);
                        }
                    }
                });
                deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                
                x.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        for(int j=0; j<N; j++){
                            saves[j].setBackground(Color.WHITE);
                            JPanel np = (JPanel)(saves[j].getComponent(1));
                            np.setBackground(Color.WHITE);
                            JPanel bp = (JPanel)(saves[j].getComponent(2));
                            bp.setBackground(Color.WHITE);
                            JPanel bp2 = (JPanel)(bp.getComponent(0));
                            bp2.setBackground(Color.WHITE);
                            JButton rb = (JButton)(bp2.getComponent(0));
                            rb.setVisible(false);
                            JButton db = (JButton)(bp2.getComponent(1));
                            db.setVisible(false);
                        }
                        x.setBackground(new Color(200, 200, 200));
                        namePanel.setBackground(new Color(200, 200, 200));
                        buttonPanel.setBackground(new Color(200, 200, 200));
                        buttonPanel2.setBackground(new Color(200, 200, 200));
                        saveField.setText(nameLabel.getText());
                        saveButton2.setEnabled(true);
                        renameButton.setVisible(true);
                        deleteButton.setVisible(true);
                    }
                });
            }
        }

        save.setVisible(true);
    }

    @SuppressWarnings({"Convert2Lambda"})
    void load() throws FileNotFoundException{
        load = new JDialog(frame, "Load", true);
        load.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        load.setSize(500, 500);
        load.setLayout(new BorderLayout(0, 0));
        load.setLocationRelativeTo(null);

        JPanel loadHeadPanel = new JPanel();
        load.add(loadHeadPanel, BorderLayout.NORTH);
        loadHeadPanel.setBackground(Color.BLACK);

        JLabel loadHeadLabel = new JLabel("Save Files");
        loadHeadPanel.add(loadHeadLabel);
        loadHeadLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        loadHeadLabel.setForeground(Color.WHITE);

        JPanel loadPanel = new JPanel();
        load.add(loadPanel, BorderLayout.SOUTH);
        loadPanel.setLayout(new FlowLayout());

        JButton loadButton2 = new JButton("Load");
        loadPanel.add(loadButton2);
        loadButton2.setEnabled(false);
        loadButton2.setFocusable(false);
        loadButton2.setBackground(new Color(200, 200, 200));
        loadButton2.setFont(new Font("Helvetica", Font.BOLD, 12));
        loadButton2.setPreferredSize(new Dimension(80, 25));
        loadButton2.setBorder(null);
        loadButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel loadField = new JLabel();

        loadButton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    getSave(loadField.getText());
                    load.dispose();
                }
                catch(NoSuchElementException | IOException ex){
                    JDialog error = new JDialog(load, "Error", true);
                    error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    error.setSize(300, 120);
                    error.setResizable(false);
                    error.setLayout(new GridLayout(2, 1, 0, 0));
                    error.setLocationRelativeTo(null);
                    
                    JPanel labelPanel = new JPanel();
                    error.add(labelPanel);
                    JLabel label = new JLabel("Save file is corrupted :(");
                    labelPanel.add(label);
                    label.setFont(new Font("Helvetica", Font.PLAIN, 12));
                    
                    JPanel buttonPanel = new JPanel();
                    error.add(buttonPanel);
                    JButton button = new JButton("Sad");
                    buttonPanel.add(button);
                    button.setFocusable(false);
                    button.setBackground(new Color(220, 220, 220));
                    button.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e){
                            error.dispose();
                        }
                    });
                    button.setPreferredSize(new Dimension(80, 25));
                    button.setBorder(null);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    error.setVisible(true);
                }
            }
        });

        JPanel loadListPanel = new JPanel();
        load.add(loadListPanel);
        loadListPanel.setLayout(new GridLayout(15,2));
        
        try (Scanner sc = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"))) {
            int N = sc.nextInt();
            sc.nextLine();
            JPanel[] loads = new JPanel[N];
            for(int i=0; i<N; i++){
                loads[i] = new JPanel();
                loads[i].setLayout(new GridLayout());

                loadListPanel.add(loads[i]);

                loads[i].add(new JLabel());
                
                sc.nextInt();
                JPanel namePanel = new JPanel(){
                    @Override
                    public boolean contains(int x, int y){
                        return false;
                    }
                };
                loads[i].add(namePanel);
                
                JLabel nameLabel = new JLabel(sc.nextLine());
                namePanel.add(nameLabel);

                loads[i].setFont(new Font("Helvetica", Font.PLAIN, 12));
                
                JPanel buttonPanel = new JPanel();
                loads[i].add(buttonPanel);
                buttonPanel.setLayout(new BorderLayout());

                JPanel buttonPanel2 = new JPanel();
                buttonPanel.add(buttonPanel2, BorderLayout.EAST);

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                
                JPanel x = loads[i];

                JButton renameButton = new JButton();
                buttonPanel2.add(renameButton);
                renameButton.setIcon(new ImageIcon(classLoader.getResource("img/rename.png")));
                renameButton.setBackground(new Color(200, 200, 200));
                renameButton.setBorder(null);
                renameButton.setVisible(false);
                renameButton.setFocusable(false);
                renameButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        String newName = renameSave(nameLabel.getText(), load);
                        if(newName != null){
                            nameLabel.setText(newName);
                        }
                    }
                });
                renameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JButton deleteButton = new JButton();
                buttonPanel2.add(deleteButton);
                deleteButton.setIcon(new ImageIcon(classLoader.getResource("img/delete.png")));
                deleteButton.setBackground(new Color(200, 200, 200));
                deleteButton.setBorder(null);
                deleteButton.setVisible(false);
                deleteButton.setFocusable(false);
                deleteButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(deleteSave(nameLabel.getText(), load)){
                            loadListPanel.remove(x);
                            loadListPanel.revalidate();
                            loadListPanel.repaint();
                        }
                        try(Scanner sc2 = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"))){
                            if(sc2.nextInt() == 0) loadButton2.setEnabled(false);
                        }
                        catch(FileNotFoundException ex){
                            System.out.println(ex);
                        }
                    }
                });
                deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                
                x.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        for(int j=0; j<N; j++){
                            loads[j].setBackground(Color.WHITE);
                            JPanel np = (JPanel)(loads[j].getComponent(1));
                            np.setBackground(Color.WHITE);
                            JPanel bp = (JPanel)(loads[j].getComponent(2));
                            bp.setBackground(Color.WHITE);
                            JPanel bp2 = (JPanel)(bp.getComponent(0));
                            bp2.setBackground(Color.WHITE);
                            JButton rb = (JButton)(bp2.getComponent(0));
                            rb.setVisible(false);
                            JButton db = (JButton)(bp2.getComponent(1));
                            db.setVisible(false);
                        }
                        x.setBackground(new Color(200, 200, 200));
                        namePanel.setBackground(new Color(200, 200, 200));
                        buttonPanel.setBackground(new Color(200, 200, 200));
                        buttonPanel2.setBackground(new Color(200, 200, 200));
                        loadField.setText(nameLabel.getText());
                        loadButton2.setEnabled(true);
                        renameButton.setVisible(true);
                        deleteButton.setVisible(true);
                    }
                });
            }
        }

        load.setVisible(true);
    }
    
    @SuppressWarnings({"Convert2Lambda", "empty-statement"})
    boolean addSave(String saveName) throws IOException{
        int N;
        int idx, lastidx;
        try (Scanner sc = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"))) {
            N = sc.nextInt();
            sc.nextLine();
            idx = -1;
            lastidx = -1;
            boolean[] flag = new boolean[]{true, true};
            for(int i=0; i<N; i++){
                lastidx = sc.nextInt();
                if(saveName.trim().equals(sc.nextLine().trim())){
                    JDialog error = new JDialog(frame, "Overwrite Save File", true);
                    error.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    error.addWindowListener(new WindowAdapter(){
                        @Override
                        public void windowClosing(WindowEvent e){
                            flag[0] = false;
                            flag[1] = false;
                            error.dispose();
                        }
                    });
                    error.setSize(300, 120);
                    error.setResizable(false);
                    error.setLayout(new GridLayout(2, 1, 0, 0));
                    error.setLocationRelativeTo(null);
                    
                    JPanel labelPanel = new JPanel();
                    error.add(labelPanel);
                    JLabel label = new JLabel("Do you wish to overwrite existing save file?");
                    labelPanel.add(label);
                    label.setFont(new Font("Helvetica", Font.PLAIN, 12));
                    
                    JPanel buttonPanel = new JPanel();
                    error.add(buttonPanel);
                    JButton button1 = new JButton("Yes");
                    buttonPanel.add(button1);
                    button1.setFocusable(false);
                    button1.setBackground(new Color(220, 220, 220));
                    button1.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e){
                            flag[0] = false;
                            flag[1] = false;
                            error.dispose();
                        }
                    });
                    JButton button2 = new JButton("No");
                    buttonPanel.add(button2);
                    button2.setFocusable(false);
                    button2.setBackground(new Color(220, 220, 220));
                    button2.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e){
                            flag[1] = false;
                            error.dispose();
                        }
                    });
                    
                    error.setVisible(true);
                    
                    while(flag[1]);
                    
                    if(flag[0]) return false;
                    
                    idx = i;
                    break;
                }
            }
        }

        try(FileWriter fw = new FileWriter(new File(System.getProperty("user.home") + "/sim/saves/save"+(idx == -1 ? lastidx + 1 : idx)+".txt"))){
            String state = n + " ";
            for(int i=0; i<n; i++){
                state += slugify(names.get(i).getText()) + " " + masses.get(i).getText() + " " + xposes.get(i).getText() + " " + yposes.get(i).getText() + " " + xvels.get(i).getText() + " " + yvels.get(i).getText() + " " + r.get(i).getText() + " " + g.get(i).getText() + " " + b.get(i).getText() + " " + colors.get(i).getSelectedIndex() + " ";
            }
            fw.write(state + "\n");
        }
        catch(IOException e){
            System.out.println(e);
        }

        if(idx == -1){
            File temp = new File(System.getProperty("user.home") + "/sim/saves/temp.txt");
            File index = new File(System.getProperty("user.home") + "/sim/saves/index.txt");
            try(FileWriter fw = new FileWriter(temp); Scanner sc2 = new Scanner(index)){
                sc2.nextLine();
                fw.write((N+1) + "\n");
                while(sc2.hasNextLine()){
                    fw.write(sc2.nextLine() + "\n");
                }
                fw.write((lastidx+1) + " " + saveName.trim() + "\n");
            }
            index.delete();
            temp.renameTo(index);
        }

        return true;
    }

    void getSave(String saveName) throws IOException, NoSuchElementException{
        Scanner sc = new Scanner(new File(System.getProperty("user.home") + "/sim/saves/index.txt"));
        int N = sc.nextInt();
        sc.nextLine();

        int idx = -1;
        
        for(int i=0; i<N; i++){
            idx = sc.nextInt();
            if(saveName.trim().equals(sc.nextLine().trim())){
                break;
            }
        }
        
        sc.close();
        if(idx == -1) throw new IOException("File not found");
        
        File f = new File(System.getProperty("user.home") + "/sim/saves/save"+idx+".txt");
        sc = new Scanner(f);
        n = sc.nextInt();
        
        for(int i=0; i<n; i++){
            sc.next();
            sc.nextDouble();
            sc.nextDouble();
            sc.nextDouble();
            sc.nextDouble();
            sc.nextDouble();
            sc.nextInt();
            sc.nextInt();
            sc.nextInt();
            sc.nextInt();
        }
        if(sc.hasNext()){
            sc.close();
            throw new NoSuchElementException("extra");
        }

        sc.close();

        sc = new Scanner(f);

        sc.nextInt();
        String state = sc.nextLine();
        
        sc.close();

        sc = new Scanner(state);

        for(int i=0; i<n; i++){
            listPanel.getComponent(i).setVisible(true);
        }
        for(int i=n; i<listPanel.getComponentCount(); i++){
            listPanel.getComponent(i).setVisible(false);
        }

        for(int i=0; i<n; i++){
            names.get(i).setText(sc.next());
            masses.get(i).setText(sc.next());
            xposes.get(i).setText(sc.next());
            yposes.get(i).setText(sc.next());
            xvels.get(i).setText(sc.next());
            yvels.get(i).setText(sc.next());
            r.get(i).setText(sc.next());
            g.get(i).setText(sc.next());
            b.get(i).setText(sc.next());
            colors.get(i).setSelectedIndex(sc.nextInt());
        }
        sc.close();

        confirmButton.setEnabled(true);
        saveButton.setEnabled(true);
        removeButton.setEnabled(true);
        if(n == 10) addButton.setEnabled(false);
    }

    @SuppressWarnings({"Convert2Lambda", "empty-statement"})
    String renameSave(String saveName, JDialog dialog){
        JDialog error = new JDialog(dialog, "Rename Save File", true);
        error.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        error.setSize(300, 140);
        error.setResizable(false);
        error.setLayout(new GridLayout(2, 1, 0, 0));
        error.setLocationRelativeTo(null);

        boolean flag[] = new boolean[]{true, true};

        error.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                flag[1] = false;
                error.dispose();
            }
        });
        
        JPanel labelPanel = new JPanel();
        error.add(labelPanel);
        JLabel label = new JLabel("File name: ");
        labelPanel.add(label);
        label.setFont(new Font("Helvetica", Font.PLAIN, 12));

        JTextField field = new JTextField(saveName, 25);
        labelPanel.add(field);
        field.setFont(new Font("Helvetica", Font.PLAIN, 12));
        
        JPanel buttonPanel = new JPanel();
        error.add(buttonPanel);

        JButton button1 = new JButton("Rename");
        buttonPanel.add(button1);
        button1.setFocusable(false);
        button1.setBackground(new Color(220, 220, 220));
        button1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                flag[0] = false;
                flag[1] = false;
                error.dispose();
            }
        });
        button1.setPreferredSize(new Dimension(80, 25));
        button1.setBorder(null);
        button1.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton button2 = new JButton("Cancel");
        buttonPanel.add(button2);
        button2.setFocusable(false);
        button2.setBackground(new Color(220, 220, 220));
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                flag[1] = false;
                error.dispose();
            }
        });
        button2.setPreferredSize(new Dimension(80, 25));
        button2.setBorder(null);
        button2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        error.setVisible(true);

        while(flag[1]);

        if(flag[0]){
            error.dispose();
            return null;
        }

        File index = new File(System.getProperty("user.home") + "/sim/saves/index.txt");
        File temp = new File(System.getProperty("user.home") + "/sim/saves/temp.txt");

        try(Scanner sc = new Scanner(index); FileWriter fw = new FileWriter(temp)){
            int N = sc.nextInt();

            fw.write(N + "\n");

            for(int i=0; i<N; i++){
                int idx = sc.nextInt();
                String name = sc.nextLine().trim();
                if(!name.equals(saveName.trim())){
                    fw.write(idx + " " + name + "\n");
                }
                else{
                    fw.write(idx + " " + field.getText().trim() + "\n");
                }
            }

            sc.close();
            fw.close();

            index.delete();
            temp.renameTo(index);
            return field.getText();
        }
        catch(IOException ex){
            System.out.println(ex);
            return null;
        }
    }

    @SuppressWarnings({"empty-statement", "Convert2Lambda"})
    boolean deleteSave(String saveName, JDialog dialog){
        JDialog error = new JDialog(dialog, "Delete Save File", true);
        error.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        error.setSize(300, 140);
        error.setResizable(false);
        error.setLayout(new GridLayout(3, 1, 0, 0));
        error.setLocationRelativeTo(null);

        boolean flag[] = new boolean[]{true, true};

        error.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                flag[1] = false;
                error.dispose();
            }
        });
        
        JPanel labelPanel = new JPanel();
        error.add(labelPanel);
        JLabel label = new JLabel("Do you wish to delete this save file?");
        labelPanel.add(label);
        label.setFont(new Font("Helvetica", Font.PLAIN, 12));

        JPanel labelPanel2 = new JPanel();
        error.add(labelPanel2);
        JLabel label2 = new JLabel(saveName);
        labelPanel2.add(label2);
        label2.setFont(new Font("Helvetica", Font.PLAIN, 12));
        
        JPanel buttonPanel = new JPanel();
        error.add(buttonPanel);
        JButton button1 = new JButton("Yes");
        buttonPanel.add(button1);
        button1.setFocusable(false);
        button1.setBackground(new Color(220, 220, 220));
        button1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                flag[0] = false;
                flag[1] = false;
                error.dispose();
            }
        });
        button1.setPreferredSize(new Dimension(80, 25));
        button1.setBorder(null);
        button1.setCursor(new Cursor(Cursor.HAND_CURSOR));


        JButton button2 = new JButton("No");
        buttonPanel.add(button2);
        button2.setFocusable(false);
        button2.setBackground(new Color(220, 220, 220));
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                flag[1] = false;
                error.dispose();
            }
        });
        button2.setPreferredSize(new Dimension(80, 25));
        button2.setBorder(null);
        button2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        error.setVisible(true);

        while(flag[1]);

        if(flag[0]){
            error.dispose();
            return false;
        }

        File index = new File(System.getProperty("user.home") + "/sim/saves/index.txt");
        File temp = new File(System.getProperty("user.home") + "/sim/saves/temp.txt");

        try(Scanner sc = new Scanner(index); FileWriter fw = new FileWriter(temp)){
            int N = sc.nextInt();

            fw.write((N - 1) + "\n");

            for(int i=0; i<N; i++){
                int idx = sc.nextInt();
                String name = sc.nextLine().trim();
                if(!name.equals(saveName.trim())){
                    fw.write(idx + " " + name + "\n");
                }
                else{
                    File f = new File(System.getProperty("user.home") + "/sim/saves/save" + idx + ".txt");
                    f.delete();
                }
            }

            sc.close();
            fw.close();

            index.delete();
            temp.renameTo(index);
            return true;
        }
        catch(IOException ex){
            System.out.println(ex);
            return false;
        }
    }

    void show(){
        frame.setVisible(true);
    }
}