package app.dizzylogic.com.killemall;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by Jeeva K on 5/3/2015.
 */

public class GameSurfaceView extends SurfaceView implements Runnable,SurfaceHolder.Callback{

    Thread thread = null;
    SoundPool gamesSoundPool;
    AssetFileDescriptor assetFD1,assetFD2,assetFD3,assetFD4,assetFD5,assetFD6,assetFD7,assetFD8,assetFD9;
    AssetManager assetManager;
    Context myContext = null;
    SurfaceHolder surfaceHolder = null;
    volatile boolean running = false;
    long previousTime = 0,currentTime = 0;
    int FPS = 0,currentSound = 0;
    int frameCounter = 0;

    int runCounter = 0;
    int playerScore = 0;
    float scaleX,scaleY;
    float cloudX = 0,cloudY = 0;
    float terrorX = 0,terrorY = 0;
    float openWindowX = 0,openWindowY = 0;
    float screenWidth,screenHeight;
    boolean inMain = true,isPlayPressed = false,isScoresPressed = false,inGame = false,inScores = false,isSinking = false,isRising = true;

    Bitmap mainScreenImage,play,playPressed,scores,scoresPressed,background,cloud;
    Bitmap windowOpened,windowClosed,cover,diffCover;
    Bitmap terror1,terror2,terror3,terror4,currentTerror;

    int soundID1,soundID2,soundID3,soundID4,soundID5,soundID6,soundID7,soundID8,soundID9;

    long sleep = 1000;
    float windowArrayX[] = {250f,375f,250f,375f,550f,750f,750f,925f,1050f,1025f};
    float windowArrayY[] = {250f,250f,475f,475f,400f,200f,450f,450f,450f,200f};
    int currentWindow = 0,terrorPosition = 0;


    public GameSurfaceView(Context context) {

        super(context);
        myContext = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        mainScreenImage = BitmapFactory.decodeResource(getResources(),R.drawable.main);
        cloud = BitmapFactory.decodeResource(getResources(),R.drawable.cloud);
        play = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        playPressed = BitmapFactory.decodeResource(getResources(),R.drawable.play_pressed);
        scores = BitmapFactory.decodeResource(getResources(),R.drawable.scores);
        scoresPressed = BitmapFactory.decodeResource(getResources(),R.drawable.scores_pressed);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        windowClosed = BitmapFactory.decodeResource(getResources(),R.drawable.window_closed);
        windowOpened = BitmapFactory.decodeResource(getResources(),R.drawable.window_opened);
        cover = BitmapFactory.decodeResource(getResources(),R.drawable.cover);
        diffCover = BitmapFactory.decodeResource(getResources(),R.drawable.diff_cover);
        terror1 = BitmapFactory.decodeResource(getResources(),R.drawable.terrorist1);
        terror2 = BitmapFactory.decodeResource(getResources(),R.drawable.terrorist2);
        terror3 = BitmapFactory.decodeResource(getResources(),R.drawable.terrorist3);
        terror4 = BitmapFactory.decodeResource(getResources(),R.drawable.terrorist4);
    }

    public void backKeyHandler(){
        if(inMain){

        }
        if(inGame){
            inMain = true;
            isPlayPressed = false;
            isScoresPressed = false;
            inGame = false;
            inScores = false;
        }
        if(inScores){
            inMain = true;
            isPlayPressed = false;
            isScoresPressed = false;
            inGame = false;
            inScores = false;
        }
    }

    public void resume() {

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {

        running = false;
        while (true) {
            try {
                thread.join();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void run() {



            while (running) {
                if (!surfaceHolder.getSurface().isValid()) {
                    continue;
                }
                previousTime = currentTime;
                Canvas canvas = surfaceHolder.lockCanvas();
                currentTime = (System.nanoTime() / 1000000000);
                FPS++;
                drawBitmap(canvas);
                if(currentTime - previousTime == 1) {
                    FPS = 0;
                }

                surfaceHolder.unlockCanvasAndPost(canvas);

            }

    }

    public void drawBitmap(Canvas c) {

        if(inMain){
            setupMainScreen(c);
        }
        if(inGame){
            setupGame(c);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            c.drawText(String.valueOf(playerScore), 10, 25, paint);
        }
        if(inScores){
        }

    }


    public void setupMainScreen(Canvas c) {

        mainScreenImage = Bitmap.createScaledBitmap(mainScreenImage,(int)(screenWidth),(int)(screenHeight),false);
        c.drawBitmap(mainScreenImage,0,0,null);

        if(!isPlayPressed && !isScoresPressed) {
            play = Bitmap.createScaledBitmap(play, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(play, scaledX(750f), scaledY(200f), null);
            scores = Bitmap.createScaledBitmap(scores, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(scores, scaledX(750f), scaledY(450f), null);
        }
        if(isPlayPressed){
            playPressed = Bitmap.createScaledBitmap(playPressed, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(playPressed, scaledX(750f), scaledY(200f), null);
            scores = Bitmap.createScaledBitmap(scores, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(scores, scaledX(750f), scaledY(450f), null);
        }
        if(isScoresPressed){
            play = Bitmap.createScaledBitmap(play, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(play, scaledX(750f), scaledY(200f), null);
            scoresPressed = Bitmap.createScaledBitmap(scoresPressed, scaledX(400f), scaledY(200f), false);
            c.drawBitmap(scoresPressed, scaledX(750f), scaledY(450f), null);
        }
    }

    public void setupGame(Canvas c) {

        background = Bitmap.createScaledBitmap(background,(int)(screenWidth),(int)(screenHeight),false);
        cloud = Bitmap.createScaledBitmap(cloud,(int)(screenWidth * 2f),(int)(screenHeight),false);
        if((cloudX != (int)(-scaledX(1280)))){
            cloudX--;
        }
        if(cloudX == (int)(-scaledX(1280))){
            cloudX = 0;
        }
        c.drawBitmap(cloud, cloudX, cloudY, null);
        c.drawBitmap(background, 0, 0, null);
        setupWindows(c);
        if(currentTerror == null)
            currentTerror = terror1;
        frameCounter++;
        if(frameCounter == 30) {
            currentWindow = (int)(Math.random() * 10);
            placeTerrorist(currentWindow, c);
            frameCounter = 0;
        }
        else {

            drawTerrorist(c,currentWindow);
        }
        placeCovers(c);

    }

    public void setupWindows(Canvas c) {

        windowClosed = Bitmap.createScaledBitmap(windowClosed,scaledX(100f),scaledY(100f),false);
        windowOpened = Bitmap.createScaledBitmap(windowOpened,scaledX(100f),scaledY(100f),false);
        terror1 = Bitmap.createScaledBitmap(terror1,scaledX(100f),scaledY(100f),false);
        terror2 = Bitmap.createScaledBitmap(terror2,scaledX(100f),scaledY(100f),false);
        terror3 = Bitmap.createScaledBitmap(terror3,scaledX(100f),scaledY(100f),false);
        terror4 = Bitmap.createScaledBitmap(terror4,scaledX(100f),scaledY(100f),false);
        c.drawBitmap(windowClosed,scaledX(250f),scaledY(250f),null);
        c.drawBitmap(windowClosed,scaledX(375f),scaledY(250f),null);
        c.drawBitmap(windowClosed,scaledX(250f),scaledY(475f),null);
        c.drawBitmap(windowClosed,scaledX(375f),scaledY(475f),null);
        c.drawBitmap(windowClosed,scaledX(550f),scaledY(400f),null);
        c.drawBitmap(windowClosed,scaledX(750f),scaledY(200f),null);
        c.drawBitmap(windowClosed,scaledX(750f),scaledY(450f),null);
        c.drawBitmap(windowClosed,scaledX(925f),scaledY(450f),null);
        c.drawBitmap(windowClosed,scaledX(1050f),scaledY(450f),null);
        c.drawBitmap(windowClosed,scaledX(1025f),scaledY(200f),null);

    }

    public void placeCovers(Canvas c){
        cover = Bitmap.createScaledBitmap(cover,scaledX(100f),scaledY(100f),false);
        diffCover = Bitmap.createScaledBitmap(diffCover,scaledX(100f),scaledY(100f),false);
        c.drawBitmap(cover,scaledX(250f),scaledY(250f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(375f),scaledY(250f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(250f),scaledY(475f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(375f),scaledY(475f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(550f),scaledY(400f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(750f),scaledY(200f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(750f),scaledY(450f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(925f),scaledY(450f) + scaledY(100f),null);
        c.drawBitmap(cover,scaledX(1050f),scaledY(450f) + scaledY(100f),null);
        c.drawBitmap(diffCover,scaledX(1025f),scaledY(200f) + scaledY(100f),null);
    }

    public void placeTerrorist(int current,Canvas c){

        terrorX = scaledX(windowArrayX[current]);
        terrorY = (int)((scaledY(windowArrayY[current])) + (scaledY(100f)));
        openWindowX = scaledX(windowArrayX[current]);
        openWindowY = scaledY(windowArrayY[current]);
        currentTerror = terrorRandom();
        openWindow(current,c);
        c.drawBitmap(currentTerror,terrorX,terrorY,null);
        terrorPosition = 0;

    }

    public void drawTerrorist(Canvas c,int current){


        if(current == 0){
            terrorX = scaledX(windowArrayX[0]);
            terrorY = (int)((scaledY(windowArrayY[0])) + (scaledY(100f)));
            openWindowX = scaledX(windowArrayX[0]);
            openWindowY = scaledY(windowArrayY[0]);
        }
        openWindow(current,c);
        if(terrorPosition <= 0 && (terrorPosition >= (int)(-scaledY(100f))) && isRising){
            c.drawBitmap(currentTerror,terrorX,terrorY + terrorPosition,null);
            terrorPosition -= 5;

        }
        if(terrorPosition < (int)(-scaledY(100f))){
            isRising = false;
            isSinking = true;
        }
        if(terrorPosition <= 0 && isSinking){
            c.drawBitmap(currentTerror,terrorX,terrorY + terrorPosition,null);
            terrorPosition += 5;

        }
        if(terrorPosition > 0){
            isRising = true;
            isSinking = false;
        }

        Log.e("Position",String.valueOf(terrorPosition));



    }


    public void openWindow(int random,Canvas c) {

        c.drawBitmap(windowOpened, openWindowX,openWindowY, null);
    }

    private int scaledX(float tmp) {

        return (int)(tmp * scaleX);
    }

    private int scaledY(float tmp) {

        return (int)(tmp * scaleY);
    }

    public Bitmap terrorRandom(){
        int tmp = (int)(Math.random() * 4) + 1;
        if(tmp == 1) {
            currentTerror = terror1;
            return terror1;
        }
        if(tmp == 2) {
            currentTerror = terror2;
            return terror2;
        }
        if(tmp == 3) {
            currentTerror = terror3;
            return terror3;
        }
        if(tmp == 4) {
            currentTerror = terror4;
            return terror4;
        }
        return terror1;
    }

    public int soundRandom(){
        int tmp = (int)(Math.random() * 9) + 1;

        if(tmp == 1){
            return soundID1;
        }
        if(tmp == 2){
            return soundID2;
        }
        if(tmp == 3){
           return soundID3;
        }
        if(tmp == 4){
            return soundID4;
        }
        if(tmp == 5){
            return soundID5;
        }
        if(tmp == 6){
            return soundID6;
        }
        if(tmp == 7){
            return soundID7;
        }
        if(tmp == 8){
            return soundID8;
        }
        if(tmp == 9){
            return soundID9;
        }
        return soundID1;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        scaleX = ((float)screenWidth/1280f);
        scaleY = ((float)screenHeight/800f);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        while (true) {
            try {
                if(thread != null)
                    thread.join();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventCode = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch(eventCode){
            case MotionEvent.ACTION_MOVE: {


                break;
            }
            case MotionEvent.ACTION_DOWN: {

                if(inMain){
                    if(X >= scaledX(750f) && X <= scaledX(1150f) && Y >= scaledY(200f) && Y <= scaledY(400f) ){
                        isPlayPressed = true;
                        isScoresPressed = false;
                    }
                    if(X >= scaledX(750f) && X <= scaledX(1150f) && Y >= scaledY(450f) && Y <= scaledY(650f) ){
                        isPlayPressed = false;
                        isScoresPressed = true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {

                if(inMain){
                    if(X >= scaledX(750f) && X <= scaledX(1150f) && Y >= scaledY(200f) && Y <= scaledY(400f) ){
                        inMain = false;
                        inGame = true;
                        inScores = false;
                    }
                    if(X >= scaledX(750f) && X <= scaledX(1150f) && Y >= scaledY(450f) && Y <= scaledY(650f) ){
                        inMain = false;
                        inGame = false;
                        inScores = true;
                    }
                }

                if(inGame){


                }
                break;
            }
        }

        return true;
    }



}
