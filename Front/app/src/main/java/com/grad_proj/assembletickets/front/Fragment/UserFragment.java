package com.grad_proj.assembletickets.front.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Activity.LoginActivity;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    View view;
    FragmentManager fragmentManager = getFragmentManager();
    private GoogleSignInClient mGoogleSignInClient;

    private UserVersionFragment userVersionFragment = new UserVersionFragment();
    private UserNoticeFragement userNoticeFragement = new UserNoticeFragement();
    private UserContactFragment userContactFragment = new UserContactFragment();

    private TextView editInfo;
    private TextView emailTxt;
    private TextView birthTxt;
    private TextView genderTxt;
    private boolean isEditing;
    private Spinner spinner;
    private DatePicker datePicker;

    private String[] items = { "선택안함", "여성", "남성", "기타" };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);

        isEditing = false;
        birthTxt = view.findViewById(R.id.text_birth);
        birthTxt.setText(UserSharedPreference.getUserBirth(getContext()));
        genderTxt = view.findViewById(R.id.text_gender);
        genderTxt.setText(UserSharedPreference.getUserGender(getContext()));

        datePicker = view.findViewById(R.id.picker_birth);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
                birthTxt.setText(y + "/" + (m + 1) + "/" + d);
                UserSharedPreference.setUserBirth(getContext(), birthTxt.getText().toString());
            }
        });

        spinner = view.findViewById(R.id.spinner_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderTxt.setText(items[i]);
                UserSharedPreference.setUserGender(getContext(), genderTxt.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editInfo = view.findViewById(R.id.text_edit);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditing){
                    isEditing = false;
                    datePicker.setVisibility(View.GONE);
                    birthTxt.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    genderTxt.setVisibility(View.VISIBLE);
                    editInfo.setText("편집");
                }
                else {
                    isEditing = true;
                    birthTxt.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);
                    genderTxt.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    editInfo.setText("완료");
                }
            }
        });

        emailTxt = view.findViewById(R.id.text_email);
        emailTxt.setText(UserSharedPreference.getUserEmail(getContext()));

        Button notice = view.findViewById(R.id.btn_notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(userNoticeFragement);
            }
        });

        Button version = view.findViewById(R.id.btn_version);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(userVersionFragment);
            }
        });

        Button contact = view.findViewById(R.id.btn_contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(userContactFragment);
            }
        });

        Button logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogout();
            }
        });

        return view;
    }

    private void showLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 로그아웃
                UserSharedPreference.clearAll(getContext());
                if (UserSharedPreference.getIdToken(getContext()).startsWith("google")) {
                    ((HomeActivity)getActivity()).googleSignOut();
                    Log.d("login", "google logout");
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 아무 동작도 하지 않고 dialog 종료
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
