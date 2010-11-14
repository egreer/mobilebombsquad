package jp.androidgroup.nyartoolkit.model;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * 
 * 
 * @author noritsuna
 *
 */
public class VoicePlayer {

    MediaPlayer mVoiceSound;
    
    public void initVoice(AssetFileDescriptor afd) {
        try {
        	mVoiceSound = new MediaPlayer();

            mVoiceSound.setDataSource(afd.getFileDescriptor(),
                             afd.getStartOffset(),
                             afd.getLength());
            mVoiceSound.setLooping(true);

            if (mVoiceSound != null) {
            	mVoiceSound.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            	mVoiceSound.prepare();
            }
        	mVoiceSound.seekTo(0);
        	mVoiceSound.start();
        	mVoiceSound.pause();
        } catch (Exception ex) {
            Log.w("VoicePlayer", "Couldn't create click sound", ex);
        }
    }
    
    public void startVoice() {
    	if(!mVoiceSound.isPlaying()) {
    		mVoiceSound.start();
    	}
    }
    public void stopVoice() {
    	if(mVoiceSound.isPlaying()) {
    		mVoiceSound.pause();
    	}
    }

}
