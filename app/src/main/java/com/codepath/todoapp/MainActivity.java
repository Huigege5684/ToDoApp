package com.codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TODO_ITEM = "TODO_ITEM";
    public static final String TODO_ITEM_POSITION = "TODO_ITEM_POSITION";
    public static final int EDIT_REQUEST_CODE = 12345;

    private ArrayList<String> mItemsList;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        mEditText = (EditText) findViewById(R.id.editText);

        populateArrayItems();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(getLongClickListener());
        mListView.setOnItemClickListener(getClickListener());
    }

    public void populateArrayItems() {
        readItems();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItemsList);
    }

    public void onAddItem(View view) {
        String input = mEditText.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "Item is empty. Please try it again...", Toast.LENGTH_SHORT).show();
        } else {
            mItemsList.add(input);
            writeItems();
        }

        mEditText.setText("");
    }

    private AdapterView.OnItemLongClickListener getLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mItemsList.remove(position);
                writeItems();

                mAdapter.notifyDataSetChanged();

                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener getClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(TODO_ITEM, mItemsList.get(position));
                intent.putExtra(TODO_ITEM_POSITION, position);

                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        };
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "MyToDoList.txt");

        try {
            if (!file.exists()) {
                filesDir.mkdirs();
                file.createNewFile();
            }

            mItemsList = new ArrayList<>(FileUtils.readLines(file));
        } catch (Exception e) {

        }
    }

    protected void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "MyToDoList.txt");

        try {
            if (!file.exists()) {
                filesDir.mkdirs();
                file.createNewFile();
            }

            FileUtils.writeLines(file, mItemsList);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String str = data.getStringExtra(TODO_ITEM);
            int position = data.getIntExtra(TODO_ITEM_POSITION, 0);

            mItemsList.set(position, str);

            writeItems();
            mAdapter.notifyDataSetChanged();
        }
    }
}
