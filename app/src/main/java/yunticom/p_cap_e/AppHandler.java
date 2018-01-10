package yunticom.p_cap_e;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/*
 *  Uygulama açma işleri
 *
 *  Çağırılabilir:
 *  ac() fonksiyonu içeriye parametre olarak açılacak uygulamanın adı girilir
 *
 *  Çağırılmaz:
 *  search() fonksiyonu gelen stringi yüklü uygulamalar arasında arar
 *
 *  Eksiklikler:
 *  uygulama listesi döndürülebilir
 *  uygulamalara macro(kullanıcıdan alıncak adı) ata
 *
 */


public class AppHandler extends AppCompatActivity {

    private String param;
    private String appsPck[];
    private String appsName[];
    private List<ApplicationInfo> pck;
    private int appSayisi;
    private Activity activity;

    private PackageManager pMan;


    public AppHandler(Activity take) {
        super();

        this.activity = take;

        this.pMan = getPackageManager();
        this.pck = pMan.getInstalledApplications(PackageManager.GET_META_DATA);
        this.appSayisi = this.pck.size();
        this.appsPck = new String[this.appSayisi];
        this.appsName = new String[this.appSayisi];

        int i = 0;

        for (ApplicationInfo inf:pck){
            appsPck[i] = inf.packageName;
            appsName[i] = inf.manageSpaceActivityName;
            i++;
        }

    }

    public boolean ac(String prm){
        this.param = prm;

        boolean ret = false;
        int index = this.search();

        if(index >= -1){
            try{
                Intent i = pMan.getLaunchIntentForPackage(appsPck[index]);

                if(i == null)
                    throw new PackageManager.NameNotFoundException();

                i.addCategory(Intent.CATEGORY_LAUNCHER);
                ret = true;
                this.activity.startActivity(i);

            }catch (PackageManager.NameNotFoundException e){
                System.out.println(e);
            }
        }else {
            System.out.println("BULUNAMADI");
        }

        return ret;
    }

    private int search(){

        int i;
        int j = 0;
        boolean isFound = false;
        boolean isDone = false;
        boolean outOfRange = false;
        String temp = this.param;
        int ret = -1;

        while (!isFound && !outOfRange){
            i = 0;

            while (!isDone){

                if(i >= appSayisi - 1){
                    isDone = true;
                }else if(temp.equalsIgnoreCase(this.appsName[i])){
                    isDone = true;
                    isFound = true;
                    ret = i;
                }

                i++;
            }

            if(!isFound){
                temp = temp.substring(0,temp.length()-1);
                isDone = false;
            }

            j++;

            if(j >= this.param.length() - 1){
                outOfRange = true;
            }else {
                isDone = false;
            }

        }

        return ret;
    }
}