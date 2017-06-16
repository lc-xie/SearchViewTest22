package com.example.stephen.searchviewtest2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.gson.Gson;

import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Connection;

public class MainActivity extends AppCompatActivity {

    private android.widget.SearchView searchView;
    private ListView listView;
//    private ArrayAdapter<String> adapter;
    private NewAdapter adapter;
    private List<School> schoolList=new ArrayList<>();
    private List<String> arr=new ArrayList<>();
    public static final int LOAD_SCHOOL_DATA=1;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendHttpRequestWithOkHttp();

        handler=new Handler(){
            public void handleMessage(Message message){
                if(message.what==LOAD_SCHOOL_DATA){
                    listView=(ListView)findViewById(R.id.list_view);
                    adapter=new NewAdapter(MainActivity.this,R.layout.item_layout,schoolList);
//        adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, arr);
                    listView.setAdapter(adapter);
                    listView.setTextFilterEnabled(true);
                }
            }
        };


        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()!=0){
                    List<School> aList=new ArrayList<>();
                    Pattern p = Pattern.compile(newText);
                    for(int i=0;i<schoolList.size();i++){
                        School aSchool = schoolList.get(i);
                        Matcher matcher = p.matcher(aSchool.getName());
                        if(matcher.find()){
                         aList.add(aSchool);
                        }
                    }
                    adapter = new NewAdapter(MainActivity.this,R.layout.item_layout,aList);
                    listView.setAdapter(adapter);
                    //listView.setFilterText(newText);
                }else{
                    listView.clearTextFilter();
                }
                return false;
            }
        });
    }

    private String TAG="MainActivity: ";
    public void sendHttpRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL("http://192.168.58.1/schoolList.json");
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    InputStream in =connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d(TAG, "加载成功！");
                    parseJSONWithGson(response.toString());
                    Message message=new Message();
                    message.what=LOAD_SCHOOL_DATA;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void parseJSONWithGson(String request){
        Gson gson=new Gson();
        SchoolJson schoolJson=gson.fromJson(request,SchoolJson.class);
        schoolList=schoolJson.getData();
        Log.d(TAG,"get data");
        for(int i=0;i<schoolList.size();i++){
            School school=schoolList.get(i);
            arr.add(school.getName());
            Log.d("MainActivity:","school name:"+arr.get(i));
        }

    }
}
