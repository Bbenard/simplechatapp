package com.example.benard.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;
    private ListView listView;
    private ArrayAdapter<String>stringArrayAdapter;
    private ArrayList<String> list_of_rooms= new ArrayList<>();
    private String name;
    //private DatabaseReference root= FirebaseDatabase.getInstance().getReference.getroot();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);
        stringArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(stringArrayAdapter);
        request_user_name();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=new HashMap<String,Object>();
                map.put(editText.getText().toString(),"");
                root.updateChildren(map);

            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set= new HashSet<String>();
                Iterator i= dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                stringArrayAdapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //listView.setOnClickListener(new AdapterView<?>());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),Chat_room.class);
                intent.putExtra("Room_name",((TextView)view).getText().toString());
                intent.putExtra("username",name);
                startActivity(intent);
            }
        });
    }
    public void request_user_name(){
        AlertDialog.Builder bulder= new AlertDialog.Builder(this);
        bulder.setMessage("Enter Username");
        final EditText input_field= new EditText(this);
        bulder.setView(input_field);
        bulder.setPositiveButton("ok",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name=input_field.getText().toString();
            }
        });
        bulder.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        });
        bulder.show();

    }
}
