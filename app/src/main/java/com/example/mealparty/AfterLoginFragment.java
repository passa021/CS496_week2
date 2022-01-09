package com.example.mealparty;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.transition.FadeThroughProvider;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AfterLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AfterLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AfterLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AfterLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AfterLoginFragment newInstance(String param1, String param2) {
        AfterLoginFragment fragment = new AfterLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View rootView;
    //TextView nickname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =inflater.inflate(R.layout.fragment_after_login, container, false);

        Button logoutButton = (Button) rootView.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "logout", Toast.LENGTH_LONG).show();
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>(){
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
                activity = (MainActivity)getActivity();
                activity.onFragmentChange(0);
            }
        });

        Button CrePartyListButton = (Button) rootView.findViewById(R.id.button_create_party_list);
        CrePartyListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),CreatePartyList.class);
                startActivity(intent);
            }
        });

        Button JoinPartyListButton = (Button) rootView.findViewById(R.id.button_join_party_list);
        JoinPartyListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),JoinPartyList.class);
                startActivity(intent);
            }
        });


        updateKakaoLoginUi();

        // Inflate the layout for this fragment
        return rootView;
    }

    MainActivity activity;

    public void updateKakaoLoginUi(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user !=null){
                    Log.i(TAG, "id" + user.getId());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "userimage" + user.getKakaoAccount().getProfile().getProfileImageUrl());

                    TextView nickname= rootView.findViewById(R.id.user_nickname);
                    String Nickname = user.getKakaoAccount().getProfile().getNickname();
                    nickname.setText(Nickname);


                    //로그인 정상적으로 되었을 경우 수행하는 코드 적
                }
                if(throwable != null){
                    //로그인 오류
                    Log.w(TAG, "invoke: "+throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }


    private final static String TAG = "유저";
}