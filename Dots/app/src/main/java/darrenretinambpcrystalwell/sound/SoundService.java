package darrenretinambpcrystalwell.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import darrenretinambpcrystalwell.dots.R;

/**
 * Created by JiaHao on 22/4/15.
 */
public class SoundService extends Service{


    MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    public void onCreate()
    {
        mp = MediaPlayer.create(this, R.raw.background_music);
        mp.setLooping(true);
        mp.setVolume(0.7f, 0.7f);

    }
    public void onDestroy()
    {
        mp.stop();
    }
    public void onStart(Intent intent,int startid){

        mp.start();
    }
}