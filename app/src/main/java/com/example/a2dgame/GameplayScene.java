package com.example.a2dgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;

    public GameplayScene(){
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255,0,0));//RectPlayer(Rect,Color);  Color.rgb(red,green,blue)
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        /*
        playerGap = distanza tra i due rect in orizzontale
        obstacleGap = distanza tra i rect in verticale
        obstacle height = altezza dei rect
        color = colore dei rect
         */
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 1000){//passato un secondo(1000) se si clicca sullo schermo si resetterà il gioco
                    reset();
                    gameOver = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                //coordinate x e y di dove si preme sullo schermo
                break;

            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;

        }
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        player.draw(canvas); //disegna il giocatore
        obstacleManager.draw(canvas); //disegna gli ostacoli

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "GameOver");
        }
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();

            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
