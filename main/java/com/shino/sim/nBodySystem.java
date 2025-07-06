package com.shino.sim;

import java.util.ArrayList;
import java.util.Scanner;

class Particle{
    final String name;
    static final double G = 10;
    double m, r[], v[];
    int d;
    int rr, gg, bb;
    int iter = 0;
    ArrayList<int[]> past;
    
    Particle(String name, double m, double rx, double ry, double vx, double vy, int r, int g, int b){
        this.name = name;
        this.m = m;
        this.r = new double[2];
        this.r[0] = rx;
        this.r[1] = ry;
        this.past = new ArrayList<>();
        v = new double[2];
        v[0] = vx;
        v[1] = vy;
        d = (int)Math.round(2 * Math.cbrt(m));
        rr = r;
        gg = g;
        bb = b;
        past.add(new int[]{(int) Math.round(rx), (int) Math.round(ry)});
    }
    
    double[] forceDueTo(Particle B){
        double distance = Math.sqrt(Math.pow(B.r[0] - r[0], 2) + Math.pow(B.r[1] - r[1], 2));
        double force = G * m * B.m / Math.pow(distance, 2);
        double[] F = new double[2];
        F[0] = force * (B.r[0] - r[0]) / distance;
        F[1] = force * (B.r[1] - r[1]) / distance;
        return F;
    }
    
    void update(double[] F, double timeScale){
        r[0] += v[0] * timeScale;
        r[1] += v[1] * timeScale;
        v[0] += F[0] / m * timeScale;
        v[1] += F[1] / m * timeScale;
        if(iter == 0){
            past.add(new int[]{(int) Math.round(r[0]), (int) Math.round(r[1])});
            if(past.size() > 1e3) past.remove(0);
        }
        iter = (iter + 1) % 10000;
    }

    Particle collide(Particle B){
        double distance = Math.sqrt(Math.pow(B.r[0] - r[0], 2) + Math.pow(B.r[1] - r[1], 2));
        if(this == B || distance >= (d+B.d)/2.0) return null;
        else return new Particle(name+"-"+B.name, m + B.m, (m*r[0] + B.m*B.r[0])/(m+B.m), (m*r[1] + B.m*B.r[1])/(m+B.m), (m*v[0] + B.m*B.v[0])/(m+B.m), (m*v[1] + B.m*B.v[1])/(m+B.m), (int)Math.round((m*rr + B.m*B.rr)/(m+B.m)), (int)Math.round((m*gg + B.m*B.gg)/(m+B.m)), (int)Math.round((m*bb + B.m*B.bb)/(m+B.m)));
    }
}

class nBodySystem{
    int n;
    ArrayList<Particle> P;
    double timeScale = 0.0000075;
    String inputStream;
    
    nBodySystem(String inputStream){
        this.inputStream = inputStream;
        initialise(inputStream);
    }
    
    @SuppressWarnings("resource")
    private void initialise(String inputStream){
        P = new ArrayList<>();
        
        Scanner sc = new Scanner(inputStream);

        n = sc.nextInt();

        double totalMass = 0, totalMomentum[] = new double[]{0, 0};
        double[] com = new double[]{0, 0};
        for(int i=0; i<n; i++){
            String name = sc.next();
            double m = sc.nextDouble();
            double rx = sc.nextDouble();
            double ry = sc.nextDouble();
            double vx = sc.nextDouble();
            double vy = sc.nextDouble();
            int rr = (int)Math.round(sc.nextDouble());
            int gg = (int)Math.round(sc.nextDouble());
            int bb = (int)Math.round(sc.nextDouble());
            
            P.add(new Particle(name, m, rx, ry, vx, vy, rr, gg, bb));
            totalMass += m;
            com[0] += m * rx;
            com[1] += m * ry;
            totalMomentum[0] += m * vx;
            totalMomentum[1] += m * vy;
        }
        
        double factor = sc.nextDouble();
        timeScale *= factor;
        
        double frameCentreX = com[0]/totalMass;
        double frameCentreY = com[1]/totalMass;
        double frameVelocityX = totalMomentum[0]/totalMass;
        double frameVelocityY = totalMomentum[1]/totalMass;
        for(int i=0; i<n; i++){
            P.get(i).r[0] -= frameCentreX - 500;
            P.get(i).r[1] -= frameCentreY - 375;
            P.get(i).v[0] -= frameVelocityX;
            P.get(i).v[1] -= frameVelocityY;

            P.get(i).past.get(0)[0] -= frameCentreX - 500;
            P.get(i).past.get(0)[1] -= frameCentreY - 375;
        }
    }

    void reset(){
        initialise(inputStream);
    }
    
    void evolve(){
        double[][] force = new double[n][2];
        for(int i=0; i<n; i++){
            force[i][0] = 0;
            force[i][1] = 0;
            for(int j=0; j<n; j++){
                if(j == i) continue;
                double[] force_ij = P.get(i).forceDueTo(P.get(j));
                force[i][0] += force_ij[0];
                force[i][1] += force_ij[1];
            }
        }
        
        for(int i=0; i<n; i++){
            P.get(i).update(force[i], timeScale);
        }

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(j == i) continue;
                Particle newParticle = P.get(i).collide(P.get(j));
                if(newParticle == null) continue;
                if(i > j){
                    P.remove(i);
                    P.remove(j);
                }
                else{
                    P.remove(j);
                    P.remove(i);
                }
                P.add(i, newParticle);
                n--;
                j = -1;
            }
        }
    }

    @SuppressWarnings("unused")
    String getState(){
        String state = n + " ";
        for(Particle p : P){
            state += p.name + " " + p.m + " " + p.r[0] + " " + p.r[1] + " " + p.v[0] + " " + p.v[1] + " " + p.rr + " " + p.gg + " " + p.bb + " ";
        }
        return state;
    }
}