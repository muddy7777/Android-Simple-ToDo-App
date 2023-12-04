package com.example.todo_app.addTask;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todo_app.R;
import com.example.todo_app.taskDatabase.TaskDatabaseHelper;
import com.example.todo_app.utils.OnFragmentInteractionListener;
import com.example.todo_app.utils.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addTaskFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText et_title;
    private EditText et_content;
    public EditText getEt_title() {
        return et_title;
    }
    public EditText getEt_content() {
        return et_content;
    }
    public Button getBt_confirm() {
        return bt_confirm;
    }
    private Button bt_confirm;
    private TaskDatabaseHelper mTaskDatabaseHelper;

    public addTaskFragment() {
        // Required empty public constructor
    }
    public static addTaskFragment newInstance(String param1, String param2) {
        addTaskFragment fragment = new addTaskFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        et_title = root.findViewById(R.id.et_title);
        et_content = root.findViewById(R.id.et_content);
        bt_confirm = root.findViewById(R.id.bt_confirm);
        return root;
    }

    @Override
    public void onStart() {
        et_title.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_title, InputMethodManager.SHOW_IMPLICIT);
        super.onStart();
    }

    private void onFragmentExit() {
        if (mListener != null) {
            mListener.onFragmentExit();
        }
    }

    @Override
    public void onDestroy() {
        onFragmentExit();        //notify TaskActivity that this fragment is destroyed
        //收回软键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        super.onDestroy();
    }
}

