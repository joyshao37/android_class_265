package com.example.chiayi.simpleui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
//(Alt+Enter)import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ArrayList<Order> orders;
    String drinkName = "black tea";
    String note = "";
    CheckBox checkBox;
    ListView listview;
    Spinner spinner;

    SharedPreferences sp; //read
    //像一本字典，可以存取上次的資訊（不能存list，只能存少少的，通常存個人資料）
    SharedPreferences.Editor editor;// write


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //decide that the layout of MainActivity is activity_main.xml
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        checkBox = (CheckBox)findViewById(R.id.hidecheckBox);
        orders =new ArrayList<>();
        listview = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE); // dictionary,選擇要不要寫入寫出
        editor = sp.edit(); //write the content of the dictionary

        String[] data = Utils.readFile(this,"notes").split("\n");//字串處理
        //textView.setText(Utils.readFile(this,"notes"));
        textView.setText(data[1]);

        /** read和writeFile如果都要做字串處理非常麻煩，所以真正並不會使用這個方法存取或寫入資料
         * 接下來會寫入其他方法
         */



        editText.setText(sp.getString("editText","")); // 在setting內找editText的定義,可能找不到EditText有點像else的感覺
        //interface
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String text = editText.getText().toString();
                editor.putString("editText",text); //輸入的文字(定義)他的名稱是editText，editText應該可以改成其他名稱
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
        radioGroup.check(sp.getInt("radioGroup",R.id.blackTeaRadioButton));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor.putInt("radioGroup",checkedId);
                editor.apply();

                RadioButton radioButton = (RadioButton) findViewById(checkedId); //find any radio buttons
                drinkName = radioButton.getText().toString();
                //right-click then click Refactor-> can change the words in a time
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //onItemClickListener->指按下ListView內的Item
            //onClickListener->按下整個List
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order= (Order) parent.getAdapter().getItem(position);
                 // parent.getAdapter()->OrderAdapter();
                 // getItem()的完全型態是Object->需Order轉型為order
                //Toast.makeText(MainActivity.this, order.note, Toast.LENGTH_LONG).show();
                Snackbar.make(view,order.note,Snackbar.LENGTH_LONG).show();
            }
        });

    setupListView();
    setupSpinner();

    }

    void setupListView()
    {
        OrderAdapter adapter = new OrderAdapter(this,orders);
        listview.setAdapter(adapter);
    }

    void setupSpinner()
    {
        String [] data =getResources().getStringArray(R.array.StoreInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);
    }

    public void click(View view)
    {
        note = editText.getText().toString();
        String text = note;
        textView.setText(text);

        Order order = new Order();
        order.drinkName = drinkName;
        order.note=note;
        order.storeInfo= (String)spinner.getSelectedItem();
        orders.add(order);


        Utils.writeFile(this,"notes",order.note+'\n');//空行更利於讀取不同時間寫入的東西

        editText.setText("");
        setupListView();

    }





}
