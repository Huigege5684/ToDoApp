package com.codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mSaveButton = (Button) findViewById(R.id.button_edit_save);
        mEditText = (EditText) findViewById(R.id.editText_user_input);
        mEditText.setText(getIntent().getStringExtra(MainActivity.TODO_ITEM));

        if (mEditText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mSaveButton.setOnClickListener(getClickListener());
    }

    private View.OnClickListener getClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEditText.getText().toString().trim();

                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(EditItemActivity.this, "Cannot save empty item. Please try to delete it in main screen...", Toast.LENGTH_SHORT).show();
                } else {

                    Intent data = new Intent();
                    data.putExtra(MainActivity.TODO_ITEM, str);
                    data.putExtra(MainActivity.TODO_ITEM_POSITION, getIntent().getIntExtra(MainActivity.TODO_ITEM_POSITION, 0));

                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        };
    }
}
