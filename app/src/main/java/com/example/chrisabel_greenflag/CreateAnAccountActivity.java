package com.example.chrisabel_greenflag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateAnAccountActivity extends AppCompatActivity {
    static final int PASS = 0;
    static final int ERROR_INVALID = 1;
    static final int ERROR_MATCHING = 2;
    static final String CAPITAL = ".*[A-Z].*";
    static final String LOWERCASE = ".*[a-z].*";
    static final String NUMBER = ".*[0-9].*";

    ImageButton ibtnNext;
    EditText etPassword;
    EditText etPasswordConfirm;
    ConstraintLayout clEmailUsedError;
    ConstraintLayout clPasswordInvalidError;
    ConstraintLayout clPasswordMismatchError;
    ImageView ivCrossEmail;
    ImageView ivTickEmail;
    ImageButton ibtnBack;
    EditText etEmail;

    DBHelper dbHelper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);

        ibtnNext = findViewById(R.id.ib_next);
        clEmailUsedError = findViewById(R.id.cl_email_used);
        clPasswordInvalidError = findViewById(R.id.cl_invalid_password_error);
        clPasswordMismatchError = findViewById(R.id.cl_mismatch_password_error);
        ivTickEmail = findViewById(R.id.iv_email_border_tick);
        ivCrossEmail = findViewById(R.id.iv_email_border_cross);
        ibtnBack = findViewById(R.id.ib_back);
        etPassword = findViewById(R.id.et_password);
        etPasswordConfirm = findViewById(R.id.et_confirm_password);
        etEmail = findViewById(R.id.et_email_address);

        ibtnNext.setEnabled(false);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                int status = checkEmail(etEmail.getText().toString());
                switch(status){
                    case PASS:
                        ivTickEmail.setVisibility(View.VISIBLE);
                        ivCrossEmail.setVisibility(View.INVISIBLE);
                        clEmailUsedError.setVisibility(View.GONE);
                        ibtnNext.setEnabled(true);
                        break;
                    case ERROR_INVALID:
                        ivTickEmail.setVisibility(View.INVISIBLE);
                        ivCrossEmail.setVisibility(View.VISIBLE);
                        clEmailUsedError.setVisibility(View.GONE);
                        ibtnNext.setEnabled(false);
                        break;
                    case ERROR_MATCHING:
                        clEmailUsedError.setVisibility(View.VISIBLE);
                        ivTickEmail.setVisibility(View.INVISIBLE);
                        ivCrossEmail.setVisibility(View.VISIBLE);
                        ibtnNext.setEnabled(false);
                }
            }
        });

        ibtnNext.setOnClickListener(view ->{
              int status = checkPasswords(etPassword.getText().toString(), etPasswordConfirm.getText().toString());
              clPasswordInvalidError.setVisibility(View.GONE);
              clPasswordMismatchError.setVisibility(View.GONE);
              switch(status){
                  case PASS:
                      if(!dbHelper.isEmailTiedToAnAccount(etEmail.getText().toString())){
                          ContentValues account = new ContentValues();
                          account.put("Email", etEmail.getText().toString() );
                          account.put("Password", etPassword.getText().toString());
                          dbHelper.insertAccount(etEmail.getText().toString(),etPassword.getText().toString());
                          Toast.makeText(this, "Thank you for registering!", Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(this, MainActivity.class);
                          startActivity(intent);
                      } else {
                          ivTickEmail.setVisibility(View.INVISIBLE);
                          ivCrossEmail.setVisibility(View.VISIBLE);
                          clEmailUsedError.setVisibility(View.VISIBLE);
                      }
                      break;
                  case ERROR_INVALID:
                      clPasswordInvalidError.setVisibility(View.VISIBLE);
                      break;
                  case ERROR_MATCHING:
                      clPasswordMismatchError.setVisibility(View.VISIBLE);
              }
        });

        ibtnBack.setOnClickListener(view->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    //TODO
    //implement SQLite
    //set mainActivity to SingleTask
    //test
    //cleanup warnings


    /**
     *
     * @return 0, 1, 2
     * 0 - passwords valid, passwords match
     * 1 - passwords invalid
     * 2 - passwords valid, passwords don't match
     */
    private int checkPasswords(String pass1, String pass2){
        if(pass1!=null && pass2!=null){
            if(isValidPassword(pass1)){
                if(pass1.equals(pass2)){
                    return PASS;
                } else {
                    return ERROR_MATCHING;
                }
            }
        }
        return ERROR_INVALID;
    }
//todo
    private int checkEmail(String email){
        if(!isValidEmail(email)){
            return ERROR_INVALID;
        }
        return PASS;
    }



    private boolean isValidEmail(String email) {
        if(email == null){
            return false;
        }
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isValidPassword(String password){
        return (password.matches(CAPITAL)
                && password.matches(LOWERCASE)
                && password.matches(NUMBER)
                && password.length() >= 8);
    }
}