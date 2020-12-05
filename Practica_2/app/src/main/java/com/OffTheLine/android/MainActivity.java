package com.OffTheLine.android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import com.OffTheLine.appEngine.Engine;
import com.OffTheLine.logic.Logic;

public class MainActivity extends AppCompatActivity {

    /*Variables*/
    private Engine _engine;
    private Logic _logic;

    /*Funciones*/

    //Por si cambia el foco
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    //Para ocultar la UI de Android
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

    //Para mostrar la UI de Android
    private void showSystemUI() {
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    //Por si cambia la rotacion de la pantalla
    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) //Comprueba la orientación de la pantalla
        {
            if(_engine != null && _engine.getGraphics() != null) //Para evitar crasheos
                _engine.getGraphics().fixAspectRatio();
        }
    }

    //Para evitar que se reinicie al cambiar la orientación de la pantalla, carga de estado
    @Override protected void onCreate(Bundle savedInstanceState) {
        //Solo planteado, al ser opcional
        /*
        if(savedInstanceState != null)
        {
        }
        else
        {
        }
        */

        super.onCreate(savedInstanceState);
        _engine = new Engine("", this.getAssets(), this);
        _logic = new Logic(_engine, "");
        _engine.init(_logic);
         setContentView(_engine.getGraphics().getSurface());
    }

    //Para evitar que se reinicie al cambiar la orientación de la pantalla, guardado de estado
    @Override public void onSaveInstanceState(Bundle savedInstanceState) {
        //Solo planteado, al ser opcional

        // Save the user's current game state

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //Para despausar
    @Override protected void onResume() {
        super.onResume();
        _engine.resume();
    }

    //Para pausar
    @Override protected void onPause() {
        super.onPause();
        _engine.pause();
    }
}