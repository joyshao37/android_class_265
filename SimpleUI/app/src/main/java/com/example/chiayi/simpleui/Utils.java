package com.example.chiayi.simpleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chiayi on 16/4/28.
 */

//工具類功能會放進這種class
public class Utils {

    public static void writeFile(Context context,String fileName,String content)
    {
        try {   //需要try-catch 寫入(?)的時間差內可能會發生問題
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_APPEND); //MODE_APPEND累加的;MODE_PRIVATE會覆蓋
            fos.write(content.getBytes()); //將內容寫入檔案內
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFile(Context context,String fileName)
    {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer =new byte[1024];
            fis.read(buffer,0,buffer.length); //讀入buffer內
            fis.close();
            return new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
