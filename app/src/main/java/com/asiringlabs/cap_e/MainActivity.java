package com.asiringlabs.cap_e;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static AppSec sec;
    private final int sIn = 100;

    private TextView sTxt;
    private TextView debugTxt;
    private ImageButton sBtn;
    private ImageButton updateBtn;
    private ImageButton tagEkleBtn;


    private String[] listOfApp;
    private String oku;
    private String debug;

    private RecognitionListener recognitionListener;
    private SpeechRecognizer speechRecognizer;

    private KomutVer komut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sTxt = (TextView)findViewById(R.id.textView);
        debugTxt = (TextView)findViewById(R.id.textView2);
        sBtn = (ImageButton)findViewById(R.id.imageButton);
        updateBtn = (ImageButton)findViewById(R.id.imageButton2);
        tagEkleBtn = (ImageButton)findViewById(R.id.tags);

        oku = new String();
        oku ="";
        debug = new String();
        debug="";

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(recognitionListener);


        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setsBtn();
            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateBtn();
            }
        });

        recognitionListener = new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                setsBtn();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                oku = sTxt.getText().toString();

                if(oku != "" && komut != null){
                    debug = komut.yeniKomut(oku);                  //String döndürerek kontrol et bide openappi ayrıca dene
                    debugTxt.setText(debug);
                    komut.openApp(getApplicationContext(),debug);
                }
                else
                    msg("HATA");
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
    }

    private void  setTagEklemeBtn(){
        startActivity(new Intent(MainActivity.this,AppKatalog.class));
    }

    private void setUpdateBtn(){                            //yüklü applerin packagenamelerini döndürür

        msg("Güncelleme yapılıyor.......");
        listOfApp = listele();
        sec = new AppSec(listOfApp.length,listOfApp);
        komut = new KomutVer(sec);
        msg("Güncelleme başarılı ..");

    }

    private void setsBtn(){                                 //butona basınca recognizer intent oluşturur
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Heyacanla Dinliyorum!!");
        try{
            startActivityForResult(i,sIn);
            sTxt.setText("");
        }catch (ActivityNotFoundException e){
            msg("ERROR !!!");
        }
    }

    @Override                                                  //sesi yazıya çevirme
    protected void onActivityResult(int istek,int sonuc,Intent data){
        super.onActivityResult(istek,sonuc,data);

        switch (istek){
            case sIn:{
                if(sonuc == RESULT_OK && data != null){
                    speechRecognizer.startListening(data);
                    ArrayList<String >text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sTxt.setText(text.get(0));

                }
                else
                    msg("GG");

                break;
            }
        }
        recognitionListener.onEndOfSpeech();
    }

    public String[] listele(){                              //yüklü uygulamaları bir array de döndürür

        PackageManager pMan = getPackageManager();
        List<ApplicationInfo> pck = pMan.getInstalledApplications(PackageManager.GET_META_DATA);
        String list[] = new String[pck.size()];

        int i = 0;
        for (ApplicationInfo pckInfo : pck) {
            list[i] = pckInfo.packageName;
            i++;
        }
        return list;
    }

    public void msg(String yaz){
        Toast.makeText(getApplicationContext(),yaz, Toast.LENGTH_SHORT).show();
    }
}

class KomutVer extends AppCompatActivity{                   //Sesi yazıya dönüştürdükten sonra yapılacak işlemler
    private String[] komutlar;
    private AppSec sec;


    KomutVer(AppSec uyg){
        this.sec = uyg;
    }

    public String yeniKomut(String al){                       //stringi  boşluklara göre böler ve ona göre komut oluşturur
        String pck;
        this.komutlar = al.split(" ");
        pck = this.komuts();

        return pck;
    }

    public String komuts(){
        String yap = komutlar[komutlar.length-1];
        String pck = "bos";

        if(yap.equalsIgnoreCase("ac")||yap.equalsIgnoreCase("aç")||yap.equalsIgnoreCase("open")){
            pck = sec.geriBesle(komutlar[komutlar.length-2]);
        }

        return pck;
    }

    public void openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    public void msg(String yaz){
        Toast.makeText(getApplicationContext(),yaz, Toast.LENGTH_SHORT).show();
    }
}


class AppSec {                                              //!!!! Hemen hemen oldu
    private int appSayisi;
    private String harita,yol,browser,not,muzik;
    private String[] appList ;
    private String[] appTag;
    private String[] appName;
    private int son;

    AppSec(int a,String[] b){
        appSayisi = a;
        appList = b;
        this.son = 6;
        packageNames();
    }

    public void  tagEkle(String tagName,String packName){
        this.appTag[this.son] = tagName;
        this.appName[this.son] = packName;
        this.son++;
    }

    private void packageNames(){                            // package nameleri tutar
        appTag[0] = "harita";
        appTag[1] = "yol";
        appTag[2] = "duckduckgo";
        appTag[3] = "ara";
        appTag[4] = "muzik";
        appTag[5] = "müzik";
        appName[0] = "com.google.android.apps.maps";
        appName[1] = "com.trafi.android.tr";
        appName[2] = "com.duckduckgo.mobile.android";
        appName[3] = "com.duckduckgo.mobile.android";
        appName[4] = "com.google.android.music";
        appName[5] = "com.google.android.music";

        this.harita = "com.google.android.apps.maps";
        this.yol = "com.trafi.android.tr";
        this.browser = "com.duckduckgo.mobile.android";
        this.muzik = "com.google.android.music";
    }

    public String kes(String ad,int hane){                  // bulunamassa kelimeyi kesip dene

        return ad.copyValueOf(ad.toCharArray(),0,ad.length()-hane);
    }

    private String bul(String ad){                          //yüklü appları tarıyarak istenilen appin package nameini döndürür
        String[] adi;
        String buldum = "bos";

        for(int i=0;i<appSayisi;i++){
            adi = appList[i].split("[.]]");

            for(int j=0;j<adi.length;j++){

                if(adi[j].equalsIgnoreCase(ad)){
                    buldum = appList[i];
                    break;
                }
            }
        }

        return buldum;
    }

    public String geriBesle(String al){                     //uygulamanın packagenameini döndürür
        String hata ="bos";
        int adim = 0;

        while (adim < al.length() && adim <5){

            if(hata.equals("bos")){

                for(int i = 0;i<this.son;i++){

                    if(al.equalsIgnoreCase(appTag[i]))
                        return appName[i];
                }

                if(adim == 0)
                    hata = bul(al);
                else
                    hata = bul(kes(al,adim));
            }
            else
                break;

            adim ++;
        }

        return hata;
    }
}