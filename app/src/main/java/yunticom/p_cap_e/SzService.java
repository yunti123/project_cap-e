package yunticom.p_cap_e;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


/*
 *  Sz servisi
 *
 *  kapat diyene kadar veya stopSpeechIntent()
 *  fonksiyonunu çağırana kadar sonsuz loopa girer
 *
 *  default pattern ali pattern değişkeninde tutulur
 *
 *  Çağırılabilir:
 *  startSpeechIntent() fonksiyonu ile dinlemeyi başlatır
 *  getResult() fonksiyonu ile çevirdiği Stringi döndürür
 *  getSay() fonksiyonu ile patternün (default: "ali") tekrar sayınısı döndürür
 *  onActivityResult() fonksiyonu ile yazıya çevirir
 *  stopSpeechIntent() fonksiyonu isStop boolenanı true yaparak dinlemeyi durdurur
 *
 *  Çağırılmaz:
 *  msg(mode,yaz) yardımcı fonksiyondur içeriye yollanan Stringi
 *  mode = 0 => Toast mesajı olarak
 *  mode = 1 => Debug olarak adbden yazdırır.
 *
 *  Eksiklikler:
 *  test edilmedi test et
 *
 */

public class SzService extends AppCompatActivity {

    private final int sIn = 100;

    private int say;
    private String ret;
    private Boolean isStop;
    private String pattern;

    private SpeechRecognizer speechRecognizer;
    private RecognitionListener recognitionListener;

    private Context appContext;
    private Activity ac;

    public SzService(Activity con) {
        super();

        say = 0;

        this.ac = con;

        this.pattern = new String("ali");

        ret = new String("");

        this.isStop = false;

        recognitionListener = new RecognitionListener() {

            String text = new String();

            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {



                if(!ret.equalsIgnoreCase(pattern) && !isStop ){
                    if(ret.equalsIgnoreCase("ali")){
                        say++;
                    }

                    startSpeechIntent();
                }
                else {
                    speechRecognizer.stopListening();
                }

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                if(results != null){
                    ArrayList<String> match = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    ret = match.get(0);
                    msg(1,ret);
                }
                else {
                    msg(1,"HATA");
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

        };

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.ac);
        speechRecognizer.setRecognitionListener(this.recognitionListener);


    }

    public void startSpeechIntent(){

        Intent i = new Intent((RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        this.isStop = false;

        speechRecognizer.startListening(i);

    }

    public void stopSpeechIntent(){
        this.isStop = true;
    }


    public String getResult(){
        return this.ret;
    }

    public int getSay(){
        return this.say;
    }

    private void msg(int mode ,String yaz){
        if(mode == 0)
            Toast.makeText(getApplicationContext(),yaz, Toast.LENGTH_SHORT).show();
        else if(mode == 1)
            System.out.println(yaz);
    }

}