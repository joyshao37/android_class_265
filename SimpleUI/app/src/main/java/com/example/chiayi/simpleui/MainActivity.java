package com.example.chiayi.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
//(Alt+Enter)import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ArrayList<Order> orders;
    String drinkName;
    String note = "";
    CheckBox checkBox;
    ListView listview;
    Spinner spinner;
    String menuResults="";

    SharedPreferences sp; //read
    //像一本字典，可以存取上次的資訊（不能存list，只能存少少的，通常存個人資料）
    SharedPreferences.Editor editor;// write

    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //decide that the layout of MainActivity is activity_main.xml
        Log.d("debug", "Main Activity OnCreate");

        //0505 Parse
//        ParseObject testObject = new ParseObject("TestObject"); // create table
//        testObject.put("test", 456);  // create column and its value
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "save success", Toast.LENGTH_LONG).show();
//                }
//            }
//        });



        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        checkBox = (CheckBox)findViewById(R.id.hidecheckBox);
        orders =new ArrayList<>();
        listview = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE); // dictionary,選擇要不要寫入寫出
        editor = sp.edit(); //write the content of the dictionary

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build(); //add deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        editText.setText(sp.getString("editText", "")); // 在setting內找editText的定義,可能找不到EditText有點像else的感覺
        //interface
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String text = editText.getText().toString();
                editor.putString("editText", text); //輸入的文字(定義)他的名稱是editText，editText應該可以改成其他名稱
                editor.apply(); //要寫上apply()才可以寫上(?) 存在手機的記憶體上

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    click(v);
                    return true;

                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    click(v);
                    return true;
                }
                return false;
            }
        });


//        int checkedId=sp.getInt("radioGroup", R.id.blackTeaRadioButton);
//        radioGroup.check(checkedId);
//        RadioButton radioButton = (RadioButton) findViewById(checkedId); //find any radio buttons
//        drinkName = radioButton.getText().toString();
//        //right-click then click Refactor-> can change the words in a time
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                editor.putInt("radioGroup", checkedId);
//                editor.apply();
//
//
//            }
//        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //onItemClickListener->指按下ListView內的Item
            //onClickListener->按下整個List
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);
                // parent.getAdapter()->OrderAdapter();
                // getItem()的完全型態是Object->需Order轉型為order
                //Toast.makeText(MainActivity.this, order.note, Toast.LENGTH_LONG).show();
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_LONG).show();
            }
        });





    setupListView();
    setupSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("Spinner", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(sp.getInt("Spinner", 0));

    }

    void setupListView()
    {


        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Realm realm = Realm.getDefaultInstance();

                    RealmResults result=realm.allObjects(Order.class); //allObject回傳RealmResult


                    OrderAdapter adapter = new OrderAdapter(MainActivity.this,result.subList(0,result.size()));
                    listview.setAdapter(adapter);

                    realm.close();
                    return;
                }

                List<Order> orders = new ArrayList<Order>();
                for (int i = 0; i < objects.size(); i++) {
                    Order order = new Order();
                    order.setNote(objects.get(i).getString("note"));
                    order.setStoreInfo(objects.get(i).getString("storeInfo"));
                    order.setMenuResults(objects.get(i).getString("menuResults"));
                    orders.add(order);

                }


                OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                listview.setAdapter(adapter);
            }//把order弄下來
        });


    }


    void setupSpinner()
    {
        String [] data =getResources().getStringArray(R.array.StoreInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);
    }

    public void click(View view) {
        note = editText.getText().toString();
        String text = note;
        textView.setText(text);

        Order order = new Order();
        order.setMenuResults(menuResults);
        order.setNote(note);
        order.setStoreInfo((String) spinner.getSelectedItem());

        SaveCallbackWithRealm saveCallbackWithRealm = new SaveCallbackWithRealm(order, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Save Fail", Toast.LENGTH_LONG).show();
                }
                //存完Object到Parse後再做以下功能
                editText.setText("");
                menuResults = "";
                setupListView();
            }
        });


        order.saveToRemote(saveCallbackWithRealm);
    }




    public void goToMenu(View view){

        Intent intent= new Intent(); //Intent應用1:帶到另一個頁面
        intent.setClass(this,DrinkMenuActivity.class);   //找出drink me Activity Class
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);   //由request code 辨別從哪個activity傳回data
        Log.d("debug","DrinkMenuActivity onCreate");
    }


    @Override   //其他Activity回來就會call這個function
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MENU_ACTIVITY)
        {
            if(resultCode == RESULT_OK){

                menuResults =data.getStringExtra("result");


            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug","Main Activity OnStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "Main Activity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug","Main Activity OnPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "Main Activity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        Log.d("debug", "Main Activity OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "Main Activity OnRestart");
    }
}
