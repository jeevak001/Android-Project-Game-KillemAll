package app.dizzylogic.com.killemall;

import android.app.Activity;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity {

    private GameSurfaceView gameSurfaceView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
        gameSurfaceView.setKeepScreenOn(true);
    }

    @Override
    protected void onResume() {

        super.onResume();
        gameSurfaceView.resume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        gameSurfaceView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
           gameSurfaceView.backKeyHandler();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
