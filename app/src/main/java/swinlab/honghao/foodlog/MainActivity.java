package swinlab.honghao.foodlog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String TAG = "FoodLog.Main";
    private Button speechBTN, askBTN;
    private ListView log;
    private ArrayList<LogData> logData;
    private LogAdapter adapter;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int questionID = 0;
    private Speech speech;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechBTN = (Button)findViewById(R.id.speech);
        askBTN = (Button)findViewById(R.id.ask);
        log = (ListView) findViewById(R.id.Log);


        logData = new ArrayList<LogData>();
        try {
            logData = LogData.readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }adapter = new LogAdapter(this, logData);
        log.setAdapter(adapter);
        //start text to speech
        speech = new Speech(this);
        askBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!speech.getInitState())
                    return;
                speech.speakOut(questionID);
            }
        });
        speechBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionID == 1)
                    questionID = 2;
                promptSpeechInput();
            }
        });


    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
        //Log.e(TAG, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //add new element to listview

                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    if(questionID == 0 && result.get(0).equals("no")) {
                        LogData.writeToFile(new LogData(result.get(0), currentTime, "no"));
                        logData.add(new LogData(result.get(0), currentTime, "no"));
                        adapter.notifyDataSetChanged();
                        Log.e("STT", "1");
                    }
                    if(questionID == 0 && result.get(0).equals("yes")) {
                        questionID = 1;
                        speech.speakOut(questionID);
                        Log.e("STT", "2");
                    }
                    if(questionID == 2) {
                        LogData.writeToFile(new LogData(result.get(0), currentTime, "yes"));
                        logData.add(new LogData(result.get(0), currentTime, "yes"));
                        questionID = 0;
                        adapter.notifyDataSetChanged();
                        Log.e("STT", "3");
                    }
                    Log.e(TAG, result.get(0) + currentTime);

                    Toast.makeText(getApplicationContext(),
                            result.get(0),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
}


