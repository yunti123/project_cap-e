package yunticom.p_cap_e;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    /*****************  activity değişkenleri  *****************/

    private ImageButton sesButon;
    private ImageButton updateButon;
    //private ImageButton duzenleButon;
    private TextView sesTextView;
    private TextView sayiTextView;

    /******************  class değişkenleri  *********************/

    int activityResult;
    int sayi;
    SttServices stt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sesTextView = (TextView)findViewById(R.id.textView);
        sesTextView.setText("bos");

        sayiTextView = (TextView)findViewById(R.id.textView2) ;
        sayiTextView.setText(0);

        sesButon = (ImageButton)findViewById(R.id.imageButton);
        sesButon.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
              sesButonHandler();
          }
        });

        updateButon = (ImageButton)findViewById(R.id.imageButton2);
        updateButon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateButonHandler();
            }
        });

        stt = new SttServices(MainActivity.this);

    }


    private void sesButonHandler(){

        stt.startSpeechIntent();

        this.activityResult = 1;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(this.activityResult == 1){
            String ret = new String("b");

            stt.onActivityResult(requestCode,resultCode,data);

            ret = stt.getResult();

            if(!ret.equalsIgnoreCase("b")){
                sesTextView.setText(ret);
            }else{
                sesTextView.setText("HATA");
            }
        }


    }

    private void updateButonHandler(){

        sesTextView.setText("bos");

    }


    private void msg(int mode ,String yaz){
        if(mode == 0)
            Toast.makeText(getApplicationContext(),yaz, Toast.LENGTH_SHORT).show();
        else if(mode == 1)
            System.out.println(yaz);
    }

}
