package com.mahdy.mealsapplication.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.model.UserInfo;
import com.mahdy.mealsapplication.model.UserInfoRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SignupFragment extends Fragment {
    Toolbar toolbar;
     EditText etName, etEmail, etPassword,etPasswordConf;
     TextView nameError, emailError, passwordError,password_conf_empty;
     CardView check_char, check_uppercase, check_number, check_symbol,check_confirm;
     boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false,confirmPassword = false;
    Button siginup;
    private UserInfoRepository userRepository;
    private String TAG = SignupFragment.class.getSimpleName();

    public SignupFragment() {
        
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        userRepository = new UserInfoRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_signup, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameError = view.findViewById(R.id.nameError);
        emailError = view.findViewById(R.id.emailError);
        passwordError = view.findViewById(R.id.passwordError);
        password_conf_empty=view.findViewById(R.id.passwordErrorConf);

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etPasswordConf = view.findViewById(R.id.etPasswordconf);


        check_char = view.findViewById(R.id.check_char);
        check_uppercase = view.findViewById(R.id.check_uppercase);
        check_number = view.findViewById(R.id.check_number);
        check_symbol = view.findViewById(R.id.check_symbol);
        check_confirm = view.findViewById(R.id.check_confirm);


        siginup = view.findViewById(R.id.sigin_up);
       siginup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name = etName.getText().toString(), email = etEmail.getText().toString(), password = etPassword.getText().toString(),passwordConf= etPasswordConf.getText().toString();

               if (name.length() > 0 && email.length() > 0 && password.length() > 0 && passwordConf.length()>0) {
                   if (isRegistrationClickable) {
                       UserInfo user = new UserInfo();
                       user.setName(name);
                       etName.setText("");
                       user.setEmail(email);
                       etEmail.setText("");
                       user.setPassword(password);
                       etPassword.setText("");
                       etPasswordConf.setText("");
                       userRepository.insert(user)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(new SingleObserver<Long>() {
                                   @Override
                                   public void onSubscribe(Disposable d) {
                                       // Disposable handling
                                   }

                                   @Override
                                   public void onSuccess(Long primaryKey) {
                                       // Primary key ID of the inserted row
                                       Log.d(TAG, "Inserted row with primary key: " + primaryKey);
                                       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putInt("userId", Math.toIntExact(primaryKey));
                                       editor.putString("username", email);
                                       editor.putString("password", password);
                                       editor.apply();
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       // Error handling
                                   }
                               });


                       Toast.makeText(getActivity(), "REGISTRATION And data saved", Toast.LENGTH_LONG).show();

                       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                       transaction.replace(R.id.fragment_container, new LoginFragment());
                       transaction.addToBackStack(null);
                       transaction.commit();
                   }
               } else {
                   if (name.length() == 0) {
                       nameError.setVisibility(View.VISIBLE);
                   }
                   if (email.length() == 0) {
                       emailError.setVisibility(View.VISIBLE);
                   }
                   if (password.length() == 0) {
                       passwordError.setVisibility(View.VISIBLE);
                   }
                   if(passwordConf.length()==0){
                       password_conf_empty.setVisibility(View.VISIBLE);
                   }
               }

           }

       });
        inputChange();
        return  view;
    }
    private void checkEmpty(String name, String email, String password,String passwordConf) {
        if (name.length() > 0 && nameError.getVisibility() == View.VISIBLE) {
            nameError.setVisibility(View.GONE);
        }
        if (password.length() > 0 && passwordError.getVisibility() == View.VISIBLE) {
            passwordError.setVisibility(View.GONE);
        }
        if (email.length() > 0 && emailError.getVisibility() == View.VISIBLE) {
            emailError.setVisibility(View.GONE);
        }
        if (passwordConf.length() > 0 && password_conf_empty.getVisibility() == View.VISIBLE) {
            password_conf_empty.setVisibility(View.GONE);
        }
    }
    private void checkAllData(String email) {
        if (isAtLeast8 && hasUppercase && hasNumber && hasSymbol && confirmPassword &&  email.length() > 0) {
            isRegistrationClickable = true;

            siginup.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            isRegistrationClickable = false;
            siginup.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.DIVIDER_COLOR));
        }
    }

    private void registrationDataCheck() {
        String password = etPassword.getText().toString(), email = etEmail.getText().toString(), name = etName.getText().toString(),passwordConf= etPasswordConf.getText().toString();

        checkEmpty(name, email, password,passwordConf);

        if (password.length() >= 8) {
            isAtLeast8 = true;
            check_char.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            isAtLeast8 = false;
            check_char.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_false));
        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            check_uppercase.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            hasUppercase = false;
            check_uppercase.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_false));
        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            check_number.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            hasNumber = false;
            check_number.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_false));
        }
        if (password.matches("(.*[_?=.*$@].*)")) {
            hasSymbol = true;
            check_symbol.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            hasSymbol = false;
            check_symbol.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_false));
        }
        boolean isValidEmail = isValidEmail(email);
        if (isValidEmail) {
            emailError.setVisibility(View.GONE);
        } else {
            emailError.setText(R.string.valid_message);
            emailError.setVisibility(View.VISIBLE);
        }
        if (!password.isEmpty() && !passwordConf.isEmpty() && passwordConf.equals(password)) {
            confirmPassword =true;
            check_confirm.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_true));
        } else {
            confirmPassword =false;
            check_confirm.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Check_color_false));
        }


        checkAllData(email);
    }

    private void inputChange() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        etPasswordConf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 8) {
                    etName.setText(s.subSequence(0, 8));
                    etName.setSelection(8); // Move cursor to the end
                    Toast.makeText(getContext(), "Maximum 8 characters allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}