package teamarv.otpattendancesystem.Cognito;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import teamarv.otpattendancesystem.R;

public class RegisterUser extends AppCompatActivity {
    private final String TAG = "SignUp";
    private EditText username;
    private EditText password;
    private EditText givenName;
    private EditText firstName;
    private EditText middleName;
    private EditText familyName;
    private String gender;
    private EditText email;
    private EditText birthday;
    private EditText phone;

    private Calendar calendar;
    private Spinner genderSpinner;
    private Button signUp;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    private String usernameInput;
    private String userPasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get back to main screen
            String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Register);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView main_title = (TextView) findViewById(R.id.signUp_toolbar_title);
        main_title.setText("Sign up");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
    }


    // This will create the list/form for registration
    private void init() {
        username = (EditText) findViewById(R.id.editTextRegUserId);
        genderSpinner = (Spinner) findViewById(R.id.spinnerRegGender);
        birthday = (EditText) findViewById(R.id.editTextRegBirthday);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserIdLabel);
                    label.setText(username.getHint());
                    username.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegUserIdMessage);
                label.setText(" ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserIdLabel);
                    label.setText("");
                    username.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }
        });

        List<String> itemsList = new ArrayList<String>();
        itemsList.add("Male");
        itemsList.add("Female");
        itemsList.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        TextView genderLabel = (TextView) findViewById(R.id.textViewRegGenderLabel);
        genderLabel.setText("Gender");
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        calendar = Calendar.getInstance();
/*        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
*/

        final DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                updateLabel();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                label.setText(birthday.getHint());
                username.setBackground(getDrawable(R.drawable.text_border_selector));
                StartTime.show();
                //new DatePickerDialog(getApplicationContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //
        password = (EditText) findViewById(R.id.editTextRegUserPassword);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText(password.getHint());
                    password.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegUserPasswordLabel);
                    label.setText("");
                }
            }
        });
        //
        givenName = (EditText) findViewById(R.id.editTextRegGivenName);
        givenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText(givenName.getHint());
                    givenName.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegGivenNameLabel);
                    label.setText("");
                }
            }
        });
        //
        firstName = (EditText) findViewById(R.id.editTextRegName);
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegNameLabel);
                    label.setText(firstName.getHint());
                    firstName.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegNameLabel);
                    label.setText("");
                }
            }
        });
        //
        middleName = (EditText) findViewById(R.id.editTextRegMiddleName);
        middleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegMiddleNameLabel);
                    label.setText(middleName.getHint());
                    middleName.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegMiddleNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegMiddleNameLabel);
                    label.setText("");
                }
            }
        });
        //
        familyName = (EditText) findViewById(R.id.editTextRegLastName);
        familyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegLastNameLabel);
                    label.setText(familyName.getHint());
                    familyName.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegLastNameMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegLastNameLabel);
                    label.setText("");
                }
            }
        });
        //
        birthday = (EditText) findViewById(R.id.editTextRegBirthday);
        birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                    label.setText(birthday.getHint());
                    birthday.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegBirthdayMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegBirthdayLabel);
                    label.setText("");
                }
            }
        });
        //
        email = (EditText) findViewById(R.id.editTextRegEmail);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText(email.getHint());
                    email.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegEmailMessage);
                label.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegEmailLabel);
                    label.setText("");
                }
            }
        });
        //
        phone = (EditText) findViewById(R.id.editTextRegPhone);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText(phone.getHint() + " with country code and no seperators");
                    phone.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) findViewById(R.id.textViewRegPhoneLabel);
                    label.setText("");
                }
            }
        });



        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read user data and register
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                usernameInput = username.getText().toString();
                if (usernameInput == null || usernameInput.isEmpty()) {
                    TextView view = (TextView) findViewById(R.id.textViewRegUserIdMessage);
                    view.setText(username.getHint() + " cannot be empty");
                    username.setBackground(getDrawable(R.drawable.text_border_error));
                    return;
                }

                String userpasswordInput = password.getText().toString();
                userPasswd = userpasswordInput;
                if (userpasswordInput == null || userpasswordInput.isEmpty()) {
                    TextView view = (TextView) findViewById(R.id.textViewUserRegPasswordMessage);
                    view.setText(password.getHint() + " cannot be empty");
                    password.setBackground(getDrawable(R.drawable.text_border_error));
                    return;
                }

                String userInput = givenName.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(givenName.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegGivenNameMessage);
                        view.setText(givenName.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = firstName.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(firstName.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegNameMessage);
                        view.setText(firstName.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = middleName.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(middleName.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegMiddleNameMessage);
                        view.setText(middleName.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = familyName.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(familyName.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegLastNameMessage);
                        view.setText(familyName.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = email.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(email.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegEmailMessage);
                        view.setText(email.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = birthday.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(birthday.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegBirthdayMessage);
                        view.setText(givenName.getHint() + " cannot be empty");
                        birthday.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = gender;
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Gender"), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegGenderMessage);
                        view.setText("Gender" + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                userInput = phone.getText().toString();
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(phone.getHint()).toString(), userInput);
                    }
                    else {
                        TextView view = (TextView) findViewById(R.id.textViewRegPhoneMessage);
                        view.setText(phone.getHint() + " cannot be empty");
                        password.setBackground(getDrawable(R.drawable.text_border_error));
                        return;
                    }
                }

                showWaitDialog("Signing up...");

                AppHelper.getPool().signUpInBackground(usernameInput, userpasswordInput, userAttributes, null, signUpHandler);

            }
        });
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Check signUpConfirmationState to see if the user is already confirmed
            closeWaitDialog();
            Boolean regState = signUpConfirmationState;
            if (signUpConfirmationState) {
                // User is already confirmed
                showDialogMessage("Sign up successful!",usernameInput+" has been Confirmed", true);
            }
            else {
                // User is not confirmed
               confirmSignUp(cognitoUserCodeDeliveryDetails);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            TextView label = (TextView) findViewById(R.id.textViewRegUserIdMessage);
            label.setText("Sign up failed");
            username.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Sign up failed",AppHelper.formatException(exception),false);
        }
    };

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        Intent intent = new Intent(this, SignUpConfirm.class);
        intent.putExtra("source","signup");
        intent.putExtra("name", usernameInput);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(resultCode == RESULT_OK){
                String name = null;
                if(data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
                exit(name, userPasswd);
            }
        }
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exit) {
                        exit(usernameInput);
                    }
                } catch (Exception e) {
                    if(exit) {
                        exit(usernameInput);
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

    private void exit(String uname) {
        exit(uname, null);
    }

    private void exit(String uname, String password) {
        Intent intent = new Intent();
        if (uname == null) {
            uname = "";
        }
        if (password == null) {
            password = "";
        }
        intent.putExtra("name", uname);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(calendar.getTime()));
    }
}
