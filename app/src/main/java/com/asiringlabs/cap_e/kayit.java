package com.asiringlabs.cap_e;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by yunty on 4/29/17.
 */

public class kayit extends AppCompatActivity {
    private static String file = "tagFile";
    private static String tempFile = "temp";
    private static final String error = "ERROR";
    private static final String ok = "Success";
    private FileOutputStream fos;
    private FileInputStream fin;
    private String[] tag;
    private String[] pck;


    public kayit() {

    }


    public String yaz(String[] tags, String[] pcks) {         //dosyaya yazdır
        String tut = new String();
        try {
            fos = openFileOutput(file, Context.MODE_PRIVATE);
            for (int i = 0; i < tags.length; i++) {
                tut = "";
                tut += tags[i] + "-" + pcks;
                try {
                    this.fos.write(tut.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fos.close();
                return this.ok;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.error;
    }

    public void update(String tg,String pckg){
        oku();
        int son = this.tag.length;
        this.tag[son] = tg;
        this.pck[son] = pckg;
        yaz(this.tag,this.pck);
    }

    private String cek() {
        String temp = new String();
        temp = "";
        int c;
        try {
            fin = openFileInput(this.file);
            while ((c = fin.read()) != -1) {
                temp += Character.toString((char) c);
            }
            fin.close();

            return temp;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.error;
    }

    public void oku() {                                      //ilk önce burda dosyadan okutup string arrylere beslet sonra getterlardan çağır
        String[] temp;
        String[] satir = this.cek().split("/n");
        for (int i = 0; i < satir.length; i++) {
            temp = satir[i].split("-");
            for (int j = 0; j < temp.length; j++) {
                this.tag[i] = temp[j];
                this.pck[i] = temp[j];
            }
        }
    }

    public String[] getTag() {                              //burdan çağır
        return this.tag;
    }


    public String[] getPck() {                              //burdan çağır
        return this.pck;
    }


}



























