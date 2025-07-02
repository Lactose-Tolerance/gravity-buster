package com.shino.sim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Launcher{
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                File directory = new File(System.getProperty("user.home") + "/sim");
                if(!directory.exists()) directory.mkdirs();
                File directory2 = new File(System.getProperty("user.home") + "/sim/saves");
                if(!directory2.exists()) directory2.mkdirs();
                File index = new File(System.getProperty("user.home") + "/sim/saves/index.txt");
                if(!index.exists()){
                    try(FileWriter fw = new FileWriter(index); FileWriter fw2 = new FileWriter(new File(System.getProperty("user.home") + "/sim/saves/save0.txt"))){
                        fw.write("1\n0 Hello World\n");
                        fw2.write("3 Star 1000 0 0 0 0 255 255 63 6 Planet 100 300 0 0 -5 63 255 255 5 Moon 10 320 0 0 -12 180 180 180 7 ");
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }

                Window window = new Window();
                window.show();
            }
        });
    }
}