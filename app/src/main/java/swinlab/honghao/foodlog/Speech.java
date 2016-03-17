package swinlab.honghao.foodlog;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kinse on 3/17/2016.
 */
public class Speech implements TextToSpeech.OnInitListener{
    private ArrayList<String> question = new ArrayList<String>();
    private TextToSpeech tts;
    public static boolean initState = false;
    public Speech(Context context){
        tts = new TextToSpeech(context, this);
        question.add("Are you eating?");
        question.add("What do you eat?");
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "this language is not supported");
            }
            else{
                initState = true;
            }
        }
        else
            Log.e("TTS", "Initilization failed!");
    }

    public static boolean getInitState() { return initState; }

    public void speakOut(int i){

        tts.setPitch( 0.6f );
        tts.setSpeechRate( 0.6f );
        tts.speak(question.get(i), TextToSpeech.QUEUE_FLUSH, null);
    }
}
