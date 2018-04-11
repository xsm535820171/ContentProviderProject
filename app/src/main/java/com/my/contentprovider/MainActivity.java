package com.my.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ContentProvider:不同应用程序之间进行数据交换的标准API,ContentProvider以某种Uri的形式对外提供数据，
 *                允许其他应用访问和修改数据；其他应用程序使用ContentProvider根据Uri去访问指定的数据。
        由于是四大组件之一，需要在清单文件中配置。。
 API:Application Programming Interface ,,应用程序编程接口
 Uri:Uniform Resource Identifier,统一资源标识符;是一个用于标识某一互联网资源名称的字符串。

 由于不同的应用程序记录数据的方式差别很大，使用SharedPreferences、文件或数据库不利于应用程序间进行数据交换

 完整开发一个ContentProvider的步骤：
 1、定义自己的ContentProvider类，该类需要继承Android提供的ContentProvider基类；
 2、在清单文件AndroidManifest.xml中，注册ContentProvider,就像注册Activity一样。
     注册ContentProvider时需要为他绑定一个域名；

 通过ContentResolver操作ContentProvider暴露的数据的步骤：
   1、调用Activity的getContentResolver()获取ContentResolver对象;
   2、调用ContentResolver的方法操作数据
      insert(Uri uri,ContentValues contentvalues):向Uri对应的ContentProvider中插入contentvalues数据
      delete(Uri uri,String where,String[] selectionArgs):删除Uri对应的ContentProvider中where提交匹配的数据
      update(Uri uri,ContentValues contentvalues,String where,String[] selectionArgs):更新Uri对应的
                    ContentProvider中where提交匹配的数据
      query(Uri uri,String[] projection,String where,String[] selectionArgs,String sortOrder):
                查询Uri对应的ContentProvider中where提交匹配的数据

 一般来说ContentProvider是单例模式，
 */
public class MainActivity extends AppCompatActivity {

    ContentResolver contentResolver;

    @ViewInject(R.id.word_et)
    EditText word_et;
    @ViewInject(R.id.detail_et)
    EditText detail_et;
    @ViewInject(R.id.search_et)
    EditText search_et;


    @OnClick({R.id.add_bt,R.id.other_bt,R.id.search_bt,R.id.update_bt,R.id.delete_bt,R.id.seleteall_bt})
    public void OnCLick(View view){
        switch (view.getId()){
            case R.id.add_bt:
                Uri uri=Uri.parse("content://com.my.peoviderword/word");

                String add_text = word_et.getText().toString();
                String detail_text = detail_et.getText().toString();
                ContentValues contentValues=new ContentValues();
                contentValues.put("word",add_text);
                contentValues.put("detail",detail_text);
                contentResolver.insert(uri,contentValues);
                //第一种方法添加数据
//                insertData(myDatabaseHelper.getReadableDatabase(),add_text,detail_text);
                word_et.setText("");
                detail_et.setText("");
                Toast.makeText(this,"添加成功",Toast.LENGTH_LONG).show();
                //第二种方法添加数据
//                ContentValues contentValues=new ContentValues();
//                contentValues.put("word",add_text);
//                contentValues.put("detail",detail_text);
//                myDatabaseHelper.getReadableDatabase().insert("dict",null,contentValues);
                break;
            case R.id.search_bt:
                String search=search_et.getText().toString();
                Uri uri1=Uri.parse("content://com.my.peoviderword/words");
                Cursor cursor=contentResolver.query(uri1,null,"word like ? or detail like ?",new String[]{"%"+search+"%","%"+search+"%"},null);
                Bundle bundle=new Bundle();
                bundle.putSerializable("key",convertCursorToList(cursor));
                Intent intent=new Intent(MainActivity.this,MyDatabaseList.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(this,"查找成功，欧耶",Toast.LENGTH_LONG).show();
//                queryData(myDatabaseHelper.getReadableDatabase(),search);
                break;
            case R.id.update_bt:
                String add_text1 = word_et.getText().toString();
                String detail_text1 = detail_et.getText().toString();
                Uri uri2=Uri.parse("content://com.my.peoviderword/words");
                ContentValues contentValues1=new ContentValues();
                contentValues1.put("detail",detail_text1);
                contentResolver.update(uri2,contentValues1,"word=?",new String[]{add_text1});
                Toast.makeText(this,"修改成功，欧耶",Toast.LENGTH_LONG).show();
//                updateData(myDatabaseHelper.getReadableDatabase(),add_text1,detail_text1);
                break;
            case R.id.delete_bt:
                String word=search_et.getText().toString();
                Uri uri3=Uri.parse("content://com.my.peoviderword/words");
                contentResolver.delete(uri3,"word=?",new String[]{word});
                Toast.makeText(this,"删除成功，欧耶",Toast.LENGTH_LONG).show();
//                deleteData(myDatabaseHelper.getReadableDatabase(),word);
                break;
            case R.id.seleteall_bt:
//                queryAllData(myDatabaseHelper.getReadableDatabase());
                Uri uri4=Uri.parse("content://com.my.peoviderword/words");
                Cursor cursor1=contentResolver.query(uri4,null,null,null,null);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("key",convertCursorToList(cursor1));
                Intent intent1=new Intent(MainActivity.this,MyDatabaseList.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                Toast.makeText(this,"查找所有，欧耶",Toast.LENGTH_LONG).show();
            case R.id.other_bt:
//                Intent intent=new Intent(this, DbUtilesActivity.class);
//                startActivity(intent);
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentResolver=getContentResolver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        contentResolver=getContentResolver();
    }
    public ArrayList<Map<String,String>> convertCursorToList(Cursor cursor){
        ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("word",cursor.getString(1));
            map.put("detail",cursor.getString(2));
            result.add(map);
        }
        return result;
    }
}
