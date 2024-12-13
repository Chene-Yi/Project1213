package com.example.project1213;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText ed_book, ed_price;
    private Button btn_query, btn_insert, btn_update, btn_delete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_insert = findViewById(R.id.btn_insert);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();

        btn_insert.setOnClickListener(view -> {
            if (ed_book.length() < 1 || ed_price.length() < 1) {
                Toast.makeText(MainActivity.this, "書名或價格未輸入", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    dbrw.execSQL("INSERT INTO myTable(book, price) values(?, ?)",
                            new Object[]{ed_book.getText().toString(), ed_price.getText().toString()});
                    Toast.makeText(MainActivity.this, "新增書名：" + ed_book.getText().toString() +
                            " 價格：" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();
                    clearInput();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "新增失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_update.setOnClickListener(view -> {
            if (ed_book.length() < 1 || ed_price.length() < 1) {
                Toast.makeText(MainActivity.this, "書名或價格未輸入", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    dbrw.execSQL("UPDATE myTable SET price = ? WHERE book LIKE ?",
                            new Object[]{ed_price.getText().toString(), ed_book.getText().toString()});
                    Toast.makeText(MainActivity.this, "更新書名：" + ed_book.getText().toString() +
                            " 價格：" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();
                    clearInput();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "更新失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete.setOnClickListener(view -> {
            if (ed_book.length() < 1) {
                Toast.makeText(MainActivity.this, "書名未輸入", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    dbrw.execSQL("DELETE FROM myTable WHERE book LIKE ?", new Object[]{ed_book.getText().toString()});
                    Toast.makeText(MainActivity.this, "刪除書名：" + ed_book.getText().toString(), Toast.LENGTH_SHORT).show();
                    clearInput();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "刪除失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_query.setOnClickListener(view -> {
            Cursor c;
            if (ed_book.length() < 1) {
                c = dbrw.rawQuery("SELECT * FROM myTable", null);
            } else {
                c = dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE ?", new String[]{ed_book.getText().toString()});
            }

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this, "共找到 " + c.getCount() + " 筆", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < c.getCount(); i++) {
                    items.add("書名：" + c.getString(0) + "\t\t價格：" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            } else {
                Toast.makeText(MainActivity.this, "未找到符合條件的書籍", Toast.LENGTH_SHORT).show();
                items.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void clearInput() {
        ed_book.setText("");
        ed_price.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbrw.close();
    }
}
