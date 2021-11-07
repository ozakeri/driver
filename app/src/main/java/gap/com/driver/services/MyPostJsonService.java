package gap.com.driver.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import gap.com.driver.app.DriverApplication;

public class MyPostJsonService {

    public String PARAMETER_ATTRIBUTES = "INPUT_PARAM";
    public String PARAMETER_IS_ENCRYPED = "IS_ENCRYPED";
    public static String baseServiceUrl;
    public static Integer SERVICE_TIME_OUT = 180000;
    private Context context;

    public MyPostJsonService() {
        // TODO Auto-generated constructor stub
    }

    public MyPostJsonService(Context context) {
        this.context = context;
        String baseService = "http://192.168.2.180:8080";
        baseServiceUrl = baseService + "/rfServices/";
    }

    String output = "";
    String key;

    @SuppressLint({"NewApi", "LongLogTag"})
    public String sendData(String serviceName, JSONObject json, boolean doEncryption) throws WebServiceException, SocketException, SocketTimeoutException {
        doEncryption = false;
        BufferedReader reader = null;

        String urlString = null;
        if (baseServiceUrl != null) {
            urlString = baseServiceUrl + serviceName;
        } else {
            urlString = serviceName;
        }
        baseServiceUrl = null;
        MCrypt mcrypt = new MCrypt();
        String sentParameters = "";
        if (json == null) {
            json = new JSONObject();
        }

        try {

            json.put("documentUsername", "driver");
            json.put("documentPassword", "GAP!driver@9707");
            json.put("version", DriverApplication.getInstance().getVersionName());
            JSONObject deviceJsonObject = new JSONObject();
            //deviceJsonObject.put("macAddress", getMacAddress(context));
            deviceJsonObject.put("deviceName", getDeviceName());
            deviceJsonObject.put("osName", "Android");
            deviceJsonObject.put("osVersion", getOSVersion());
            deviceJsonObject.put("imei", getImei(context));
            json.put("device", deviceJsonObject);
            System.out.println("==--==-json=-=-=-=-" + json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (doEncryption) {
                sentParameters = MCrypt.bytesToHex(mcrypt.encrypt(json.toString()));
                Log.d("JSON:", json.toString());
                Log.d("Decrpted JSON", sentParameters);
            } else {
                sentParameters = URLEncoder.encode(String.valueOf(json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String parametersString = "";
        if (sentParameters != null) {
            parametersString = PARAMETER_ATTRIBUTES + "=" + sentParameters + "&";
        }
        parametersString += PARAMETER_IS_ENCRYPED + "=" + Boolean.valueOf(doEncryption).toString();

        // Send data
        try {

            System.out.println("======urlString=" + urlString);
            System.out.println("======parametersString=" + parametersString);

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            SSLContext sc = null;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                };

                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(SERVICE_TIME_OUT);
            conn.setReadTimeout(SERVICE_TIME_OUT);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(parametersString);
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            try {
                System.out.println("result for server" + serviceName + ":" + sb.toString());
                Log.d("JSON Decrypted result for server" + serviceName + ":", sb.toString());
                if (doEncryption) {
                    output = new String(mcrypt.decrypt(sb.toString()));
                    Log.d("JSON Decrypted result for server" + serviceName + ":", output);
                } else {
                    output = sb.toString();
                }
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                if (errorMsg == null) {
                    errorMsg = "Exception";
                }
                e.printStackTrace();
                Log.e(errorMsg, errorMsg);
                throw new WebServiceException(e.getMessage(), e);
            }
        } catch (SocketTimeoutException e) {
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = "SocketTimeoutException";
            }
            Log.e(errorMsg, errorMsg);
            throw new SocketTimeoutException(e.getMessage());
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = "Exception";
            }
            Log.e(errorMsg, errorMsg);
            throw new SocketTimeoutException(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*****************************************************/

        return output;
    }

    public String getMacAddress(Context context) {
        System.out.println("-----------MyPostJsonService.getMacAddress" + context);
        if (context != null) {
            WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String macAddress = wimanager.getConnectionInfo().getMacAddress();
            if (macAddress == null) {
                macAddress = "00:00:00:00:00:00";
            }
            return macAddress;
        } else {
            return null;
        }
    }

    @SuppressLint("HardwareIds")
    public String getImei(Context context) {
        String imei = null;
        if (context != null) {


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    imei = android.provider.Settings.Secure.getString(
                            context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    System.out.println("imei===111===" + imei);

                } else {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    imei = telephonyManager.getDeviceId();
                    System.out.println("imei===222===" + imei);
                }
            }
        }
        return imei;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getOSVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return release + " (API: " + sdkVersion + ")";
    }


}
