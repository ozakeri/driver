package gap.com.driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gap.com.driver.activity.MainActivity;
import gap.com.driver.activity.RegistrationActivity;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.database.DataBaseClint;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.services.MyFirebaseMessagingService;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.DateUtils;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CircularProgress;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    private boolean isConnect = false;
    private CircularProgress progress_circular;
    private String token;
    private List<Driver> driverList;
    private Driver driver;
    private DriverApplication application;
    private Globals sharedData = Globals.getInstance();
    private boolean stop = false;
    private boolean firstLogin = false;
    private AppCompatTextView txt_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txt_description = findViewById(R.id.txt_description);
        txt_description.setTypeface(DriverApplication.getInstance().getTypeFaceSplash());
        SharedPreferences.Editor editor1 = DriverApplication.getInstance().getSharedPreferences().edit();
        editor1.putBoolean("firstLogin", false);
        editor1.apply();
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.GONE);
        callService();

        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString("action", null);
        editor.apply();
    }

    private void callService() {
        if (Utils.isConnected(SplashActivity.this)) {

            token = MyFirebaseMessagingService.getToken();

            if (token == null || token.isEmpty()) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        // String token = instanceIdResult.getToken();
                        SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                        editor.putString(Constants.FIRE_BASE_TOKEN, instanceIdResult.getToken());
                        editor.apply();

                        token = instanceIdResult.getToken();
                        System.out.println("token111====" + token);

                        if (token != null) {
                            //getToken(token);
                        }

                    }
                });
            } else {

                System.out.println("token222====" + token);
                if (token != null) {
                    //getToken(token);
                }
            }

            getAll();


        } else {
            Utils.showToast(SplashActivity.this, R.string.error_noConnection, false);
        }
    }

    /*
     * get driver info
     * */
    private void getDriverProfileInfo(final String userName, final String Password) {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        //progressbar.setVisibility(View.VISIBLE);
        ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
            @Override
            public void onResponse(ServerDateTimeResponseBean response) {
                //progressbar.setVisibility(View.GONE);
                try {
                    Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                    if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                        //progressbar.setVisibility(View.VISIBLE);
                        ServerCoordinator.getInstance().getDriverProfileInfo(userName, Password,
                                new Response.Listener<DriverProfileResponseBean>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(DriverProfileResponseBean response) {

                                        if (response != null) {
                                            // jsonDate.setResponse(response);
                                            sharedData.setResponse(response);

                                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                            i.putExtra("firstLogin", firstLogin);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Utils.showErrors(SplashActivity.this, error);
                                    }
                                });

                    } else {
                        Utils.showToast(SplashActivity.this, R.string.message_toast_invalidDateTime, false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showErrors(SplashActivity.this, error);
            }
        });
    }

    /*
     * get all driver from db
     * */
    private void getAll() {
        class GetAllDriver extends AsyncTask<Void, Void, List<Driver>> {

            @Override
            protected List<Driver> doInBackground(Void... voids) {
                driverList = DataBaseClint.getInstance(SplashActivity.this).getAppDataBase().driverDao().getAll();
                if (driverList.size() > 0) {
                    driver = driverList.get(0);
                    if (driver != null) {
                        application = (DriverApplication) getApplication();
                        application.setCurrentUser(driver);
                        if (Utils.isConnected(SplashActivity.this)) {
                            getDriverProfileInfo(driver.getUsername(), driver.getPassword());
                        } else {
                            Utils.showToast(SplashActivity.this, R.string.error_noConnection, false);
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return driverList;
            }

            @Override
            protected void onPostExecute(List<Driver> drivers) {
                super.onPostExecute(drivers);
                System.out.println("drivers1111====" + drivers);

            }
        }
        new GetAllDriver().execute();
    }

    public void getToken(String token) {

        ServerCoordinator.getInstance().updateFirebaseTokenId(token, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
