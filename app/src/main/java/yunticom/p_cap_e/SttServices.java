package yunticom.p_cap_e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
*  Sesi yazıya çeviren servis
*
*  kapat diyene kadar infinite loopa girer
*
*  Çağırılabilir:
*  startSpeechIntent() fonksiyonu ile dinlemeyi başlatır
*  getResult() fonksiyonu ile çevirdiği Stringi döndürür
*  onActivityResult() fonksiyonu ile yazıya çevirir
*
*  Çağırılmaz:
*  msg(mode,yaz) yardımcı fonksiyondur içeriye yollanan Stringi
*  mode = 0 => Toast mesajı olarak
*  mode = 1 => Debug olarak adbden yazdırır.
*
*  Eksiklikler:
*  Intenti kaldırmaya çalış.
*  Arkaplanda hep çalışacak şekilde ayarla.
*  Arkaplan servisi gibi thread oluşturarak yap
*
*/

public class SttServices extends AppCompatActivity {

    private final int sIn = 100;

    public String ret;
    private SpeechRecognizer speechRecognizer;
    private RecognitionListener recognitionListener;

    private Activity ac;

    public SttServices(Activity con) {
        super();

        this.ac = con;

        ret = new String("");

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

                if(ret.equalsIgnoreCase("kapat")){
                    speechRecognizer.stopListening();
                }
                else {
                    startSpeechIntent();
                }

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

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
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Heycanla Dinliyorum!!");
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        try{
            ac.startActivityForResult(i,sIn);

        }catch (ActivityNotFoundException e){
            msg(0,"ERROR!!");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == sIn){
            if(resultCode == RESULT_OK && data != null){
                speechRecognizer.startListening(data);
                ArrayList<String> text = new ArrayList<String>();
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                this.ret = text.get(0);
            } else{
                this.ret = "HATA";
                msg(0,"HATA");
            }

        }

        recognitionListener.onEndOfSpeech();

    }

    public String getResult(){
        return this.ret;
    }

    private void msg(int mode ,String yaz){
        if(mode == 0)
            Toast.makeText(getApplicationContext(),yaz, Toast.LENGTH_SHORT).show();
        else if(mode == 1)
            System.out.println(yaz);
    }

}