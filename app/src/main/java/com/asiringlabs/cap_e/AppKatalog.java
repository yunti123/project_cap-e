package com.asiringlabs.cap_e;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppKatalog extends ListActivity {

    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist=null;
    private ListView list;
    private AppSec appSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=(ListView) findViewById(R.id.list);
        packageManager=getPackageManager();
        appSec = (AppSec) MainActivity.sec;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app= applist.get(position);

        try{
            appSec.tagEkle("a",app.packageName);
            Toast.makeText(this, "Islem Basarili", Toast.LENGTH_LONG).show();

        }catch (NoSuchMethodError e){
            Toast.makeText(this,"HATA", Toast.LENGTH_LONG).show();

        }

        catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
        ArrayList<ApplicationInfo> applist=new ArrayList<ApplicationInfo>();

        for(ApplicationInfo info : list){
            try{
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null){
                    applist.add(info);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  applist;
    }

}

class AppAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> applist = null;
    private Context context;
    private PackageManager packageManager;
    String appNameV;
    public AppAdapter(Context context, int resource, List<ApplicationInfo> objects) {
        super(context, resource,  objects);

        this.context=context;
        this.applist=objects;
        packageManager=context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != applist) ? applist.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != applist) ? applist.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(null == view){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.apps,null);
        }
        ApplicationInfo data=applist.get(position);
        if(null != data){
            TextView appName=(TextView) view.findViewById(R.id.appName);

            ImageView iconView=(ImageView) view.findViewById(R.id.appIcon);

            appName.setText(data.loadLabel(packageManager));
            appNameV= (String) data.loadLabel(packageManager);
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }
        return view;
    }
}