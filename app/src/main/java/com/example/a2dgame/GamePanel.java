package com.example.a2dgame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private SceneManager manager;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread= new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        manager.recieveTouch(event);

        return true;
        /*
        se ci fosse false vorrebbe dire che alcuni tocchi sullo schermo verrebbero ignorati
        ma in questo caso tutti i tocchi devono essere considerati quindi metto sempre true*/
        //return super.onTouchEvent(event);
    }

    public void update(){
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        manager.draw(canvas);
    }


}
