package com.example.todo_app.DetailTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todo_app.R;
import com.example.todo_app.taskDatabase.TaskDatabaseHelper;
import com.example.todo_app.utils.Task;

import java.time.LocalDate;
import java.util.Calendar;

public class DetailTaskActivity extends AppCompatActivity {

    Task task;
    private CheckBox cb_completed;
    private TextView tv_expectedTime;
    private EditText et_title;
    private EditText et_content;
    private TaskDatabaseHelper mTaskDatabaseHelper;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        mTaskDatabaseHelper = new TaskDatabaseHelper(this);
        initView();
    }

    private void initView() {
        cb_completed = findViewById(R.id.detail_cb_completed);
        tv_expectedTime = findViewById(R.id.detail_tv_expectedTime);
        et_title = findViewById(R.id.detail_et_title);
        et_content = findViewById(R.id.detail_et_content);
        mToolbar = findViewById(R.id.detail_toolBar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        task = bundle.getParcelable("task"); // 从Bundle中获取Task对象
        cb_completed.setChecked(task.isCompleted());
        tv_expectedTime.setText(task.getExpectedTime());
        et_title.setText(task.getTitle());
        et_content.setText(task.getContent());

        //add text change listener
        et_title.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                task.setTitle(et_title.getText().toString().trim());
                mTaskDatabaseHelper.updateTask(task);
            }
        });
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                task.setContent(et_content.getText().toString().trim());
                mTaskDatabaseHelper.updateTask(task);
            }
        });

        //add checkBox
        cb_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setCompleted(cb_completed.isChecked());
                mTaskDatabaseHelper.updateTask(task);
            }
        });
    }

    public void onDelete(View view) {
        mTaskDatabaseHelper.deleteTask(task.getId());
        finish();
    }

    public void onTimeSelect(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String selectedDate = i + "-" + (i1 + 1) + "-" + i2;
                        task.setExpectedTime(selectedDate);
                        mTaskDatabaseHelper.updateTask(task);
                    }
                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tv_expectedTime.setText(task.getExpectedTime());
            }
        });
        datePickerDialog.show();
    }
}