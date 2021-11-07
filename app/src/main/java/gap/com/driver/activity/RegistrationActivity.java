package gap.com.driver.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gap.com.driver.R;
import gap.com.driver.common.Constants;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.model.SuccessResponseBean;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.DateUtils;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CircularProgress;

//import com.google.firebase.analytics.FirebaseAnalytics;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText input_personalCode;
    Button loginButton;
    String personalCode;
    private CircularProgress progressbar;
    private Globals globals = Globals.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirebaseAnalytics.getInstance(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        loginButton = (Button) findViewById(R.id.btn_login);
        input_personalCode = (EditText) findViewById(R.id.input_personalCode);

        progressbar = (CircularProgress) findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        personalCode = input_personalCode.getText().toString();

        if (personalCode.isEmpty()) {
            input_personalCode.setError(getResources().getString(R.string.label_login_editText_warning));
        } else {
            isDeviceDateTimeValid();
        }
    }

    private void isDeviceDateTimeValid() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        progressbar.setVisibility(View.VISIBLE);
        ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
            @Override
            public void onResponse(ServerDateTimeResponseBean response) {
                progressbar.setVisibility(View.GONE);
                try {
                    Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                    if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                        progressbar.setVisibility(View.VISIBLE);
                        ServerCoordinator.getInstance().sendCode(personalCode,
                                new Response.Listener<SuccessResponseBean>() {
                                    @Override
                                    public void onResponse(SuccessResponseBean response) {
                                        progressbar.setVisibility(View.GONE);
                                        globals.setMobileNo(response.getRESULT().getMobileNo());
                                        enter();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressbar.setVisibility(View.GONE);
                                        Utils.showErrors(RegistrationActivity.this, error);
                                    }
                                });

                    } else {
                        Utils.showToast(RegistrationActivity.this, R.string.message_toast_invalidDateTime, false);
                        progressbar.setVisibility(View.GONE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showErrors(RegistrationActivity.this, error);
                System.out.println("ERROR====" + error.getMessage());
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    public void enter() {
        Intent intent = new Intent(getApplicationContext(), ActivationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the
        moveTaskToBack(true);
    }
}
