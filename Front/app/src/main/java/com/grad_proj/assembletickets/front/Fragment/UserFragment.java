package com.grad_proj.assembletickets.front.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class UserFragment extends Fragment {

    View view;
    FragmentManager fragmentManager = getFragmentManager();
    private GoogleSignInClient mGoogleSignInClient;

    private UserVersionFragment userVersionFragment = new UserVersionFragment();
    private UserNoticeFragement userNoticeFragement = new UserNoticeFragement();
    private UserContactFragment userContactFragment = new UserContactFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);

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
                UserSharedPreference.clearIdToken(getContext());
                if (UserSharedPreference.getIdToken(getContext()).startsWith("google")) {
                    signOut();
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

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleSignInClient.signOut();
    }

}
