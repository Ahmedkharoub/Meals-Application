package com.mahdy.mealsapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.model.UserInfo;
import com.mahdy.mealsapplication.model.UserInfoRepository;


public class LoginFragment extends Fragment {
    TextView signup;
    Button login;

    ImageView gmailLogin;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient googleSignInClient;
    SignInClient oneTapClient;
    int RC_sig_in = 20;
    static final int REQ_ONE_TAP = 2;
    boolean showOneTapUI = true;
    EditText getUserName,getPassword;
    String name, password;
    private UserInfoRepository userRepository;
    private static final String TAG = "LoginFragment";
    SharedPreferences sharedPreferences;
    FragmentTransaction transaction;
    private static final String PREF_KEY_GMAIL_LOGIN = "gmail_login";
    TextView continueAsGuest;

    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneTapClient = Identity.getSignInClient(getActivity());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRepository = new UserInfoRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        signup = view.findViewById(R.id.signup);
        login = view.findViewById(R.id.loginBtn);
        gmailLogin = view.findViewById(R.id.gmail_login);
        getUserName = view.findViewById(R.id.get_user_name);
        getPassword = view.findViewById(R.id.get_password);
        continueAsGuest = view.findViewById(R.id.login_fragment_guest);

        name = getUserName.getText().toString();
        password = getPassword.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getUserName.getText().toString();
                String password = getPassword.getText().toString();

                if (!name.isEmpty() && !password.isEmpty()) {
                    userRepository.getUserByNameAndPassword(name, password).observe(getViewLifecycleOwner(), new Observer<UserInfo>() {

                        public void onChanged(UserInfo user) {
                            if (user != null) {

                               saveCredentials(getActivity(), user.getId(), name, password);

                                replaceFragment(getActivity(),new MainFragment());

                                login.setVisibility(View.GONE);
                            } else {

                                Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("guest", true);
                editor.apply();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MainFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SignupFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                login.setVisibility(View.GONE);
            }
        });


        gmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Logging with Google");
                BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(true)
                                .build())
                        .build();
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult beginSignInResult) {
                                System.out.println("SUCCESS ONE TAP CLIENT");
                                try {
                                    startIntentSenderForResult(
                                            beginSignInResult.getPendingIntent().getIntentSender(),
                                            REQ_ONE_TAP,
                                            null,
                                            0,
                                            0,
                                            0,
                                            null
                                    );
                                } catch (IntentSender.SendIntentException e) {
                                    // Handle error
                                    Log.e(TAG, "Error starting One Tap UI", e);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Log.e(TAG, "Failed to get One Tap UI intent", e);
                            }
                        });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == REQ_ONE_TAP){
           SignInCredential googleCredential = null;
           try {
               googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
               String idToken = googleCredential.getGoogleIdToken();
               if (idToken != null) {
                   AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                   auth.signInWithCredential(firebaseCredential)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       // Sign in success, update UI with the signed-in user's information
                                       Log.d(TAG, "signInWithCredential:success");
                                       FirebaseUser user = auth.getCurrentUser();
                                       saveGmailLoginStatus(getActivity());
                                       Log.d(TAG, "signInWithCredential:" + user.getUid());
                                       Log.d(TAG, "signInWithCredential:" + user.getDisplayName());
                                       Log.d(TAG, "signInWithCredential:" + user.getEmail());
                                       replaceFragment(getActivity(), new MainFragment());
                                       login.setVisibility(View.GONE);

                                   } else {
                                       Log.w(TAG, "signInWithCredential:failure", task.getException());
                                   }
                               }
                           });
               }
           } catch (ApiException e) {
               throw new RuntimeException(e);
           }
       }

    }
    public static void saveCredentials(Context context, Integer userId, String username, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();

        Log.d(TAG, "username:" +username);
        Log.d(TAG, "password:" +password);
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveGmailLoginStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_KEY_GMAIL_LOGIN, true);
        editor.apply();
    }



}

