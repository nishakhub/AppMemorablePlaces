package com.rajramchandani.appmemorableplaces;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity {

    static ListView addplaces;
    static ArrayAdapter<String> adapter;
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayList<String> arr = new ArrayList<String>();

    static ArrayList<String> latitude=new ArrayList<String>();
    static ArrayList<String > longitude=new ArrayList<String>();
    static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setTheme(R.style.TextAppearance_AppCompat_Headline);
        setContentView(R.layout.activity_main);
        addplaces = (ListView) findViewById(R.id.places);


        sharedPreferences=this.getSharedPreferences("com.rajramchandani.appmemorableplaces", Context.MODE_PRIVATE);

        /*addplaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Alert");
                    alert.setMessage("Do u want to remove this place");
                    alert.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.remove(arr.get(position)).commit();
                            editor.remove(String.valueOf(locations.get(position))).commit();
                            editor.remove(latitude.get(position)).commit();
                            editor.remove(longitude.get(position)).commit();

                            // sharedPreferences.edit().remove(arr.get(position)).apply();
                            //sharedPreferences.edit().remove(latitude.get(position)).apply();
                            //sharedPreferences.edit().remove(longitude.get(position)).apply();
                            //sharedPreferences.edit().remove(String.valueOf(locations.get(position))).apply();
                           // editor.commit();
                            arr.remove(arr.get(position));
                            locations.remove(locations.get(position));
                            longitude.remove(longitude.get(position));
                            latitude.remove(latitude.get(position));
                            adapter.notifyDataSetChanged();
                            Log.i("size", String.valueOf(arr.size()));


                        }
                    });
                    alert.show();

                return true;
            }
        });*/


        try {
            arr=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("arr",ObjectSerializer.serialize(new ArrayList<String>())));
            latitude=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("latitude",ObjectSerializer.serialize(new ArrayList<String>())));
            longitude=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("longitude",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(arr.size()>0 && latitude.size()>0 && longitude.size()>0 && arr.size()==latitude.size() && arr.size()==longitude.size())
        {
            for(int i=0;i<longitude.size();i++)
            {
                locations.add(new LatLng(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i))));
            }
        }
        else
        {
            arr.add("Add new Places here!!");
            locations.add(new LatLng(0, 0));
            longitude.add("0");
            latitude.add("0");
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr);
        addplaces.setAdapter(adapter);
        addplaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });


        addplaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Do u want to remove");

                alert.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.remove(arr.get(position)).commit();
                        editor.remove(String.valueOf(locations.get(position))).commit();
                        editor.remove(latitude.get(position)).commit();
                        editor.remove(longitude.get(position)).commit();
                        //editor.remove(String.valueOf(locations.get(position))).commit();
                        // sharedPreferences.edit().remove(arr.get(position)).apply();
                        //sharedPreferences.edit().remove(latitude.get(position)).apply();
                        //sharedPreferences.edit().remove(longitude.get(position)).apply();
                        //sharedPreferences.edit().remove(String.valueOf(locations.get(position))).apply();
                        // editor.commit();
                        arr.remove(arr.get(position));
                        locations.remove(locations.get(position));
                        longitude.remove(longitude.get(position));
                        latitude.remove(latitude.get(position));
                        adapter.notifyDataSetChanged();

                    }
                });
                alert.show();



                return true;
            }
        });





    }
}
