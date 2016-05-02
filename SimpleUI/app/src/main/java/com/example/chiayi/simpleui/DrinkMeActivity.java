package com.example.chiayi.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_me);
    }


    public void add(View view){

        Button button = (Button)view;
        String text = button.getText().toString();
        int count = Integer.parseInt(text); //自動將string轉成int
        count++;
        button.setText(String.valueOf(count));//將int轉成String

    }

    public void cancel(View view){

        finish(); //Activity會被Destroy掉

    }


    public void done(View view){




    }

    //蒐集所有data的function
    public JSONArray getData() throws JSONException {

        LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.root);
        JSONArray jsonArray = new JSONArray();
                //"order":
                //        [{"name":"black tea"},]

        for(int i = 0; i<=3 ; i++){

            LinearLayout linearLayout = (LinearLayout) rootLinearLayout.getChildAt(i); //拿出要的第n層linear layout
            TextView textView = (TextView)linearLayout.getChildAt(0);
            Button mButton = (Button)linearLayout.getChildAt(1);
            Button lButton = (Button)linearLayout.getChildAt(2);


            String drinkName = textView.getText().toString();
            int m = Integer.parseInt(mButton.getText().toString());
            int l = Integer.parseInt(lButton.getText().toString());


            JSONObject jsonObject = new JSONObject();
            try{

                jsonObject.put("name",drinkName); // key-value
                jsonObject.put("m",m);
                jsonObject.put("l",l);
                jsonArray.put(jsonObject);

            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }
        return jsonArray;
    }



    public void goToMenu(View view){

        Intent intent= new Intent(); //Intent應用1:帶到另一個頁面
        intent.setClass(this,DrinkMeActivity.class);   //找出drinkMeActivity Class
        startActivity(intent);
        Log.d("debug", "DrinkMenuActivity onCreate");
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug","DrinkMenuActivity OnStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "DrinkMenuActivity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug","DrinkMenuActivity OnPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "DrinkMenuActivity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "DrinkMenuActivity OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "DrinkMenuActivity OnRestart");
    }






}
