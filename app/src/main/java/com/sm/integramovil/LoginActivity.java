package com.sm.integramovil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sm.integramovil.models.CredentialsViewModel;
import com.sm.integramovil.responses.LoginResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    public android.app.AlertDialog waitingDialog;
    public EditText usernameEditText,passwordEditText;
 public FloatingActionButton btnConfiguracion;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        requestMultiplePermissions();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);
        final Button loginButton = findViewById(R.id.login);

        getUser();



        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(LoginActivity.this, ConfiguracionActivity.class);
                startActivity(MainIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username=usernameEditText.getText().toString();
                final String password=passwordEditText.getText().toString();

                if(username.length()==0)
                {
                    usernameEditText.requestFocus();
                    usernameEditText.setError(getString(R.string.ingrese_su_usuario));
                    return;
                }

                if(password.length()==0)
                {
                    passwordEditText.requestFocus();
                    passwordEditText.setError(getString(R.string.ingrese_su_password));
                    return;
                }

                waitingDialog=new
                        SpotsDialog.Builder().setContext(LoginActivity.this).build();
                waitingDialog.setTitle("Uploading Post");
                waitingDialog.setMessage(getString(R.string.espere_por_favor));
                waitingDialog.show();

                ApiEndpointInterface mAPIService = UtilesServices.getAPIService(LoginActivity.this);

                final CredentialsViewModel usuario = new CredentialsViewModel();
                usuario.setPassword(passwordEditText.getText().toString());
                usuario.setEmail(usernameEditText.getText().toString());
                Call<LoginResponse> call = mAPIService.authenticate(usuario);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        int statusCode = response.code();
                        if(response.isSuccessful()) {
                            Log.i("TAG", "post submitted to API." + response.body().toString());

                            if(response.body().getStatus().equals("OK")) {

                                waitingDialog.dismiss();

                                CheckBox ch=(CheckBox)findViewById(R.id.ch_rememberme);
                                if(ch.isChecked()) {
                                    rememberMe(response.body().getNombre(), usuario.getPassword(), username, response.body().getToken());
                                }
                                updateUiWithUser();
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                            else
                            {
                                waitingDialog.dismiss();

                                String error=response.body().getMensaje();
                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                                materialAlertDialogBuilder.setTitle(R.string.error);
                                materialAlertDialogBuilder.setMessage(error);
                                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                materialAlertDialogBuilder.show();
                            }
                        }
                        else
                        {
                            waitingDialog.dismiss();
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setTitle(R.string.login);
                            materialAlertDialogBuilder.setMessage("Se ha producido un error al intentar iniciar sesión. Intentelo nuevamente mas tarde.");
                            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            materialAlertDialogBuilder.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse>  call, Throwable t) {
                        waitingDialog.dismiss();
                        String error=t.getMessage();
                       /* Toast toast=Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT);
                        toast.setMargin(50,50);
                        toast.show();*/
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                        materialAlertDialogBuilder.setTitle(R.string.error);
                        materialAlertDialogBuilder.setMessage(error);
                        materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        materialAlertDialogBuilder.show();
                    }
                });

            }
        });



        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });


    }



    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET ,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    public static String  PREFS_NAME="integramovil";
    public static String PREF_USERNAME="username";
    public static String PREF_PASSWORD="password";
    public static String PREF_EMAIL="email";
    public static String PREF_TOKEN="token";

    public void getUser(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String email = pref.getString(PREF_EMAIL, null);
        String password = pref.getString(PREF_PASSWORD, null);

        if (username != null || password != null) {
            usernameEditText.setText(email);
            passwordEditText.setText(password);
            CheckBox ch=(CheckBox)findViewById(R.id.ch_rememberme);
            ch.setChecked(true);
        }


    }
/*
    public void showLogout(String username){
        //display log out activity
        Intent intent=new Intent(this, LogoutActivity.class);
        intent.putExtra("user",username);
        startActivity(intent);
    }
*/
    public void rememberMe(String user, String password, String email,String token){
        //save username and password in SharedPreferences
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_USERNAME,user)
                .putString(PREF_PASSWORD,password)
                .putString(PREF_EMAIL,email)
                .putString(PREF_TOKEN,token)
                .commit();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void updateUiWithUser() {
        String welcome = "";//getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        //  Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        //SAVE TOKEN
        Intent MainIntent = new Intent(LoginActivity.this, InicioActivity.class);
        startActivity(MainIntent);

    }



    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}