package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {
    Handler handler;
    Runnable runnable;
    final  int UPDATE_MILLIS=30;
    Bitmap background;
    Bitmap topTube,bottomtube;
    Display display;
    Point point;
    int dwidth, dheight;
    Rect rect;
    Bitmap[] birds;
    int birdFrame = 0;
    int velocity=0,gravity=3;
    int birdx,birdy;
    boolean gamestate  = false;
    int gap= 400;
    int minTubeoffset, maxTubeoffset;
    int numberofTubes =4;
    int distancebetweenTubes;
    int[] tubeX = new int[numberofTubes];
    int[] toptubeY = new int[numberofTubes];
    Random random;
    int tubeVelocity = 8;

    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
              invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        topTube =BitmapFactory.decodeResource(getResources(),R.drawable.toptube);
        bottomtube = BitmapFactory.decodeResource(getResources(),R.drawable.bottomtube);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dwidth = point.x;
        dheight = point.y;
        rect = new Rect(0,0,dwidth,dheight);
        birds = new Bitmap[2];
        birds[0]= BitmapFactory.decodeResource(getResources(),R.drawable.bird);
        birds[1]= BitmapFactory.decodeResource(getResources(),R.drawable.bird2);
        birdx = dwidth/2 - birds[0].getWidth()/2;
        birdy = dheight/2-birds[0].getHeight()/2;
        distancebetweenTubes = dwidth*1/2;
        minTubeoffset = gap/2;
        maxTubeoffset = dheight - minTubeoffset - gap;
        random  = new Random();
        for(int i=0;i<numberofTubes;i++){
            tubeX[1] = dwidth + 1* distancebetweenTubes;
            toptubeY[1] = minTubeoffset + random.nextInt(maxTubeoffset - minTubeoffset+1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);
        if(birdFrame==0){
            birdFrame=1;
        }else{
            birdFrame=0;
        }
        if(gamestate) {


            if (birdy < dheight - birds[0].getHeight() || velocity < 0) {
                velocity += gravity;
                birdy += velocity;
            }
            for (int i=0; i<numberofTubes;i++){
                tubeX[i] -=tubeVelocity;
                if(tubeX[1]<-topTube.getWidth()){
                    tubeX[1] +=numberofTubes * distancebetweenTubes;
                    toptubeY[1] = minTubeoffset + random.nextInt(maxTubeoffset - minTubeoffset+1);
                }
            }
            canvas.drawBitmap(topTube,tubeX[1],toptubeY[1] - topTube.getHeight(),null);
            canvas.drawBitmap(bottomtube,tubeX[1],toptubeY[1]+gap,null);
        }
        canvas.drawBitmap(birds[birdFrame],birdx,birdy,null);
        handler.postDelayed(runnable,UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            velocity = -30;
            gamestate = true;

        }
        return true;
    }
}
