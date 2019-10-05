package com.example.a2dgame;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/*
Riguarda gli ostacoli e il come vengono generati. Durante il gioco lo "sfondo" copn gli ostacoli si muoverà verso il basso.Per
rendere il gioco fluido servira una regione di buffering nella quale essi verranno creati. Questa regione verra messa esternamente
allo schermo in modo tale che gli oggetti si creino prima della visualizzazione dell'utente.
L'obbiettivo è creare per ogni x,distanziati di una certa y l'una dall'altra, una coppia di rettangoli con in mezzo uno spazio dove
l'oggetto player dovrà passare .
 */
public class ObstacleManager {
    //higher index = lower on screen = higer y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacle();
    }

    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob : obstacles){
            if(ob.playerCollide(player))
                return true;
        }
        return false;
    }

    private void populateObstacle(){
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0){
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));//sottraggo playerGap perche senno potrebbe generare ostacoli non superabili dall'utente
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update(){
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000))*Constants.SCREEN_HEIGHT/(10000.0f);
        /*
        (float)(Math.sqrt(1 + (startTime - initTime)/2000)) serve per aumentare la velocità all'aumentare del tempo.
        "1+" serve perche al tempo 0 in questo modo moltiplico per 1
         */

        for (Obstacle ob : obstacles){
            ob.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));//sottraggo playerGap perche senno potrebbe generare ostacoli non superabili dall'utente
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++; // in questo modo ogni volta che un obstacle viene rimosso si incrementa il punteggio;
        }
    }

    public void draw(Canvas canvas){
        for(Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText(""+ score, 50, 50 + paint.descent() - paint.ascent(), paint); //posizione del punteggio nella schermata
    }
}
