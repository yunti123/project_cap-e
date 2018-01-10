package yunticom.p_cap_e;

import android.app.Activity;

/*
*
*  Komutu algılayıp ona göre komutu execute eder
*
*  Çağırılabilir:
*  startCommand() : komutu alır ve execute etmeye çalışır
*
*  Çağırılmaz:
*  commandParser() : gelen inputu parse edip içinde mevcut komutları arar
*  commandExecute() : commandParser fonksiyonunda dönen flagi kontrol eder
*  ve gerekli fonksiyonu çağırarak komutu gerçekler
*
*  Diğer fonksiyonlar komutların gerçeklenmesini sağlayan ek işleri yapar
*
*
*  Eksiklikler:
*  Bitmemiş komut fonksiyonlarını bitir.
*  Daha fazla komut tanımla ( ara gibi ).
*
*/

public class CommandHandler {

    static final int ER = -1;
    static final int AC = 0;
    static final int KAPAT = 1;

    private String command;
    private String token[];
    private Activity activity;
    private AppHandler appHandler;

    public CommandHandler(Activity vt) {
        this.activity = vt;
        this.appHandler = new AppHandler(vt);
    }

    public void startCommand(String cmd){
        this.command = cmd;
        this.commadExecute();
    }

    private int commandParser(){

        int i;
        int ret = -1;
        String temp;

        this.token = this.command.split(" ");


        for(i=0;i<this.token.length;i++){

            temp = this.token[i];
            if(     temp.equalsIgnoreCase("ac")||
                    temp.equalsIgnoreCase("aç")||
                    temp.equalsIgnoreCase("open")){
                ret = 0;
                break;
            }else if(temp.equalsIgnoreCase("kapa")||
                    temp.equalsIgnoreCase("kapat")||
                    temp.equalsIgnoreCase("close")){
                ret = 1;
                break;
            }
        }

        return ret;

    }

    private void commadExecute(){

        int cmd = this.commandParser();

        switch (cmd){

            case AC:
                this.ac(token[0]);
                break;

            case KAPAT:
                msg("kapatiliyor");
                break;

            case ER:
                msg("Error komut yok");
                break;

        }

    }

    private void ac(String uyg){

        boolean chk;

        msg(uyg + " aciliyor");

        chk = this.appHandler.ac(uyg);

        if(!chk){
            msg(uyg + "acilamadi!!");
        }
    }

    private void msg(String yaz){

        System.out.println(yaz);

    }

}