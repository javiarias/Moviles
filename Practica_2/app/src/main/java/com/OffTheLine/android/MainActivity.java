package com.OffTheLine.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        InputStream inputStream = null;
        AssetManager assetManager = this.getAssets();
        try {
            inputStream = assetManager.open("babie.png");
            _sprite = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            android.util.Log.e("MainActivity", "Error leyendo");
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {}
        }

        _surfaceView = new SurfaceView(this);

        _surfaceView.getHolder().setFixedSize(640, 480);

        _myView = new MyView(this, _surfaceView);

        setContentView(_surfaceView);
    }

    MyView _myView;
    SurfaceView _surfaceView;

    @Override
    protected void onResume() {
        super.onResume();
        _myView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _myView.pause();
    }

    Bitmap _sprite;

    class MyView implements Runnable {

        protected void update(double delta) {

            _x += ((double)_incX) * delta;
            _y += ((double)_incY) * delta;

            if(_x < 0) {
                _x = -_x;
                _incX *= -1;
            }
            else if (_x >= (_surface.getWidth() - _imageWidth)) {
                _x = 2*(_surface.getWidth() - _imageWidth) - _x;
                _incX *= -1;
            }

            if(_y < 0) {
                _y = -_y;
                _incY *= -1;
            }
            else if (_y >= (_surface.getHeight() - _imageHeight)) {
                _y = 2*(_surface.getHeight() - _imageHeight) - _y;
                _incY *= -1;
            }
        }

        protected void render(Canvas c) {
            c.drawColor(0xFFAAEEFF); //ARGB

            if(_sprite != null) {
                c.drawBitmap(_sprite, (int)_x, (int)_y, null);
            }
        }

        public void run() {
            long lastFrame = System.nanoTime();

            while(_running) {
                long currentTime = System.nanoTime();
                long elapsed = currentTime - lastFrame;
                lastFrame = currentTime;
                double delta = (double) elapsed / 1.0e9;

                update(delta);

                while(!_surface.getHolder().getSurface().isValid())
                    ;

                Canvas c = _surface.getHolder().lockCanvas();

                render(c);

                _surface.getHolder().unlockCanvasAndPost(c);
            }
        }

        public void resume() {
            if(!_running) {
               _running = true;

                _renderThread = new Thread(this);
                _renderThread.start();
            }
        }

        public void pause() {
            _running = false;

            while(true) {
                try {
                    _renderThread.join();
                    break;
                } catch (InterruptedException ie) {
                }
            }
        }

        volatile boolean _running = false;
        Thread _renderThread;

        public MyView(Context context, SurfaceView surfaceView) {
            _surface = surfaceView;

            if(_sprite != null){
                _imageWidth = _sprite.getWidth();
                _imageHeight = _sprite.getHeight();
            }
        }

        double _x = 0;
        double _y = 0;
        int _incX = 2500;
        int _incY = 3405;
        int _imageWidth;
        int _imageHeight;

        SurfaceView _surface;
    }
}