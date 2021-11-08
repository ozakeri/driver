package gap.com.driver.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.enumtype.SendingStatusEn;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.LoginResponseBean;
import gap.com.driver.model.ReportListResponseBean;
import gap.com.driver.model.SalaryAttributeResponseBean;
import gap.com.driver.model.SavedPersonTimeOffResponseBean;
import gap.com.driver.model.SendCommentResponseBean;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.model.SuccessResponseBean;
import gap.com.driver.model.VerifyCodeResponseBean;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.update.UpdateVersionResponseBean;
import gap.com.driver.util.Constant;
import gap.com.driver.util.GsonRequest;
import gap.com.driver.util.ImageUtil;
import gap.com.driver.util.RestClient;
import gap.com.driver.util.Util;
import gap.com.driver.util.Utils;

/**
 * Created by GapCom on 07/29/2018.
 */

public class ServerCoordinator {
    private static ServerCoordinator instance = null;
    public static Integer MAX_ATTACH_FILE_PACKET_SIZE = 8192;

    public static synchronized ServerCoordinator getInstance() {
        if (instance == null)
            instance = new ServerCoordinator();
        return instance;
    }

    private ServerCoordinator() {

    }


    public void getServerDateTime(Response.Listener listener
            , Response.ErrorListener errorListener) {

        String ws = Constants.WS + "getServerDateTime";
        ArrayList<Utils.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Utils.WSParameter("getServerDateTime", ""));
        String json = Utils.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                ServerDateTimeResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }




    public void getLastDocumentVersion(Response.Listener listener
            , Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getLastDocumentVersion";
        ArrayList<Utils.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Utils.WSParameter("getLastDocumentVersion", ""));
        String json = Utils.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                UpdateVersionResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    public void saveComplaintReport(String username, String password, String id, String identifier, int entityNameEn, String reportStr, Date reportDate
            , double xLatitude, double yLongitude, Response.Listener listener
            , Response.ErrorListener errorListener) {

        String ws = Constants.WS + "saveComplaintReport";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        Map<String, ArrayList<Util.WSParameter>> complaintReportListMap = new HashMap<>();
        //wsParameters.add(new Util.WSParameter("username", username));
        //wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("id", id));
        wsParameters.add(new Util.WSParameter("identifier", identifier));
        wsParameters.add(new Util.WSParameter("entityNameEn", entityNameEn));
        wsParameters.add(new Util.WSParameter("reportStr", reportStr));
        wsParameters.add(new Util.WSParameter("reportDate", reportDate));
        wsParameters.add(new Util.WSParameter("xLatitude", xLatitude));
        wsParameters.add(new Util.WSParameter("yLongitude", yLongitude));
        complaintReportListMap.put("complaintReport", wsParameters);
        String json = Util.createJson(username, password, complaintReportListMap);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                UpdateVersionResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void driverAuth(String username,
                           Response.Listener listener,
                           Response.ErrorListener errorListener) {
        String ws = Constants.WS + "driverAuth";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        //wsParameters.add(new Util.WSParameter("password", password));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                LoginResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void getDriverProfileInfo(String username, String password,
                                     Response.Listener listener,
                                     Response.ErrorListener errorListener) {
        String ws = Constants.WS + "driverProfileInfo";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    public void updateFirebaseTokenId(String token,
                                      Response.Listener listener,
                                      Response.ErrorListener errorListener) {
        String ws = Constants.WS + "updateFirebaseTokenId";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("firebaseTokenId", token));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void sendCode(String personalCode,
                         Response.Listener listener,
                         Response.ErrorListener errorListener) {
        String ws = Constants.WS + "mobileNoConfirmation";
        ArrayList<Utils.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Utils.WSParameter("driverCode", personalCode));
        String json = Utils.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                SuccessResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    public void verifyCode(String mobilNo, String activationCode,
                           Response.Listener listener,
                           Response.ErrorListener errorListener) {
        String ws = Constants.WS + "activationCodeValidation";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("activationCode", activationCode));

        SharedPreferences sharedPreferences = DriverApplication.getInstance().getSharedPreferences();
        wsParameters.add(new Util.WSParameter("mobileNo", mobilNo));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                VerifyCodeResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void createDriverPassword(String username, String newPassword,
                                     Response.Listener listener,
                                     Response.ErrorListener errorListener) {
        String ws = Constants.WS + "createDriverPassword";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("newPassword", newPassword));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                VerifyCodeResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }


    public void getDriverSalaryAttribute(String username, String password,
                                         int salaryType, String fromDate,
                                         Response.Listener listener,
                                         Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverSalaryAttribute";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("salaryType", salaryType));
        wsParameters.add(new Util.WSParameter("fromDate", fromDate));
        //wsParameters.add(new Util.WSParameter("toDate", toDate));
        String json = Util.createJson(wsParameters);
        System.out.println("json1111===" + json);
        json = URLEncoder.encode(json);
        System.out.println("json2222===" + json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                SalaryAttributeResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void getDriverPersonTimeOffList(String username, String password,
                                           int driverId, String fromDate, String toDate,
                                           Response.Listener listener,
                                           Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverPersonTimeOffList";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("driverId", driverId));
        wsParameters.add(new Util.WSParameter("fromDate", fromDate));
        wsParameters.add(new Util.WSParameter("toDate", toDate));

        System.out.println("fromDate====" + fromDate);
        System.out.println("toDate====" + toDate);

        //wsParameters.add(new Util.WSParameter("toDate", toDate));
        String json = Util.createJson(wsParameters);
        System.out.println("json1111===" + json);
        json = URLEncoder.encode(json);
        System.out.println("json2222===" + json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileModel.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void getDriverPersonTimeOffAttachList(String username, String password,
                                           int id,
                                           Response.Listener listener,
                                           Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverPersonTimeOffAttachList";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("id", id));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileModel.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void deleteAttachFile(String username, String password,
                                           int id,
                                           Response.Listener listener,
                                           Response.ErrorListener errorListener) {
        String ws = Constants.WS + "deleteAttachFile";
        System.out.println("id==========" + id);
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("id", id));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileModel.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }


    public void getDriverComplaintList(String username, String password,
                                       int driverId, String fromDate, String toDate,
                                       Response.Listener listener,
                                       Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverComplaintList";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("driverId", driverId));
        wsParameters.add(new Util.WSParameter("fromDate", fromDate));
        wsParameters.add(new Util.WSParameter("toDate", toDate));
        wsParameters.add(new Util.WSParameter("documentType", "driver"));
        //wsParameters.add(new Util.WSParameter("toDate", toDate));
        String json = Util.createJson(wsParameters);
        System.out.println("json1111===" + json);
        json = URLEncoder.encode(json);
        System.out.println("json2222===" + json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileModel.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void getDriverSACommentList(String username, String password,
                                       String salaryAttributeId,
                                       Response.Listener listener,
                                       Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverSACommentList";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("salaryAttributeId", salaryAttributeId));
        //wsParameters.add(new Util.WSParameter("toDate", toDate));
        String json = Util.createJson(wsParameters);
        System.out.println("json1111===" + json);
        json = URLEncoder.encode(json);
        System.out.println("json2222===" + json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileModel.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    public void saveDriverSAComment(String username, String password, int salaryAttributeId, String reportStr, Response.Listener listener
            , Response.ErrorListener errorListener) {

        String ws = Constants.WS + "saveDriverSAComment";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        Map<String, ArrayList<Util.WSParameter>> complaintReportListMap = new HashMap<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("commentText", reportStr));
        wsParameters.add(new Util.WSParameter("salaryAttributeId", salaryAttributeId));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                UpdateVersionResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void editDriverSAComment(String username, String password, int id, String reportStr, Response.Listener listener
            , Response.ErrorListener errorListener) {
        String ws = Constants.WS + "editDriverSAComment";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("id", id));
        wsParameters.add(new Util.WSParameter("commentText", reportStr));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                UpdateVersionResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void deleteDriverSAComment(String username, String password, int id, Response.Listener listener
            , Response.ErrorListener errorListener) {

        String ws = Constants.WS + "deleteDriverSAComment";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("id", id));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                UpdateVersionResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }

    public void savePersonTimeOff(String username, String password, String startDate, String finishDate, int type, String description, Response.Listener listener
            , Response.ErrorListener errorListener) {
        String ws = Constants.WS + "savePersonTimeOff";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("fromDate", startDate));
        wsParameters.add(new Util.WSParameter("toDate", finishDate));
        wsParameters.add(new Util.WSParameter("leaveType", type));
        wsParameters.add(new Util.WSParameter("leaveDescription", description));

        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                SavedPersonTimeOffResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    public void saveAttachPersonTimeOff(String username, String password, String attachFileLocalPath, String id, String serverAttachFileId, String entityNameEn, String serverEntityId,
                                        String serverAttachFileSettingId, String attachFileUserFileName, Response.Listener listener
            , Response.ErrorListener errorListener) throws Exception {
        String ws = Constants.WS + "saveEntityAttachFileResumable";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        ArrayList<Util.WSParameter> wsParametersCopy = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParametersCopy.add(new Util.WSParameter("id", createTransactionID()));
        wsParametersCopy.add(new Util.WSParameter("serverId", serverAttachFileId));
        wsParametersCopy.add(new Util.WSParameter("entityNameEn", 128));
        wsParametersCopy.add(new Util.WSParameter("entityId", serverEntityId));
        wsParametersCopy.add(new Util.WSParameter("attachFileSettingId", 1009));
        wsParametersCopy.add(new Util.WSParameter("attachFileUserFileName", attachFileUserFileName));
        if (attachFileLocalPath != null) {
            File attachedFile = new File(attachFileLocalPath);
            if (attachedFile.exists()) {
                FileInputStream inputStream = new FileInputStream(attachedFile);
                byte[] fileBytes = new byte[MAX_ATTACH_FILE_PACKET_SIZE];
                int res = inputStream.read(fileBytes);

                byte[] fixedFileBytes = Arrays.copyOf(fileBytes, res);

                JSONArray attachmentByteJsonArray = new JSONArray();
                for (int i = 0; i < fixedFileBytes.length; i++) {
                    byte fileByte = fixedFileBytes[i];
                    attachmentByteJsonArray.put(fileByte);
                }
                wsParametersCopy.add(new Util.WSParameter("attachmentBytes", attachmentByteJsonArray));
                wsParametersCopy.add(new Util.WSParameter("attachmentChecksum", ImageUtil.getMD5Checksum(attachFileLocalPath)));
            }
        }
        wsParameters.add(new Util.WSParameter("attachFile", Util.createJson(wsParametersCopy)));

        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                SavedPersonTimeOffResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    public void editPersonTimeOff(String id, String username, String password, String startDate, String finishDate, int type, String description, Response.Listener listener
            , Response.ErrorListener errorListener) {
        String ws = Constants.WS + "editPersonTimeOff";
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("id", id));
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("fromDate", startDate));
        wsParameters.add(new Util.WSParameter("toDate", finishDate));
        wsParameters.add(new Util.WSParameter("leaveType", type));
        wsParameters.add(new Util.WSParameter("leaveDescription", description));

        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                DriverProfileResponseBean.class,
                listener,
                errorListener,
                false
        );
        RestClient.getInstance().addToRequestQueue(gsonRequest);
    }


    private String getDate(Calendar calendar) {
        String format = "%02d/%02d/%02d %02d:%02d:%02d";
        Object[] objects = new Object[]{
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        };

        return String.format(Locale.US, format, objects);
    }


    public void sendComment(String username, String password, String comment,
                            int type,
                            Response.Listener listener,
                            Response.ErrorListener errorListener) {
        String ws = Constants.WS + "saveSubscriberComment";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("comment", comment));
        wsParameters.add(new Util.WSParameter("complaintType", type));
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String date = getDate(calendar);
        wsParameters.add(new Util.WSParameter("commentDate", date));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Utils.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                SendCommentResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

    @SuppressLint("DefaultLocale")
    public String createTransactionID() {
        return String.format("%05d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
    }

    public void resumeAttachFile(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {

            long serverAttachFileId = DriverApplication.getInstance().getSharedPreferences().getLong(Constant.ServerAttachFileId, 0);
            String attachFileUserFileName = DriverApplication.getInstance().getSharedPreferences().getString(Constant.AttachFileUserFileName, "");
            String username = DriverApplication.getInstance().getSharedPreferences().getString(Constant.username, "");
            String password = DriverApplication.getInstance().getSharedPreferences().getString(Constant.password, "");
            String attachFileLocalPath = DriverApplication.getInstance().getSharedPreferences().getString(Constant.attachFileLocalPath, "");
            String serverEntityId = DriverApplication.getInstance().getSharedPreferences().getString(Constant.ServerEntityId, "");

            jsonObject.put("username", username);
            jsonObject.put("tokenPass", password);
            JSONObject attachFileJsonObject = new JSONObject();
            attachFileJsonObject.put("id", createTransactionID());
            attachFileJsonObject.put("serverId", serverAttachFileId);
            attachFileJsonObject.put("entityNameEn", 128);
            attachFileJsonObject.put("entityId", serverEntityId);
            attachFileJsonObject.put("attachFileSettingId", 1009);
            attachFileJsonObject.put("attachFileUserFileName", attachFileUserFileName);


            if (attachFileLocalPath != null) {
                File attachedFile = new File(attachFileLocalPath);
                if (attachedFile.exists()) {
                    FileInputStream inputStream = new FileInputStream(attachedFile);
                    Integer fileSize = inputStream.available();
                    byte[] fileBytes = new byte[MAX_ATTACH_FILE_PACKET_SIZE];
                    int res = inputStream.read(fileBytes);
                    byte[] fixedFileBytes = Arrays.copyOf(fileBytes, res);
                    JSONArray attachmentByteJsonArray = new JSONArray();
                    for (int i = 0; i < fixedFileBytes.length; i++) {
                        byte fileByte = fixedFileBytes[i];
                        attachmentByteJsonArray.put(fileByte);
                    }
                    attachFileJsonObject.put("attachmentBytes", attachmentByteJsonArray);
                    attachFileJsonObject.put("attachmentChecksum", ImageUtil.getMD5Checksum(attachFileLocalPath));

                    jsonObject.put("attachFile", attachFileJsonObject);
                    MyPostJsonService postJsonService = new MyPostJsonService(context);
                    String result = postJsonService.sendData("saveEntityAttachFileResumable", jsonObject, true);
                    if (result != null) {
                        JSONObject resultJson = new JSONObject(result);
                        if (!resultJson.isNull(Constants.SUCCESS_KEY)) {
                            if (!resultJson.isNull(Constants.RESULT_KEY)) {
                                JSONObject resultJsonObject = resultJson.getJSONObject(Constants.RESULT_KEY);
                                if (!resultJsonObject.isNull("savedAttachFile")) {
                                    JSONObject savedAttachFileJsonObject = resultJsonObject.getJSONObject("savedAttachFile");
                                    if (!savedAttachFileJsonObject.isNull("id")) {
                                        //attachFile.setServerAttachFileId(savedAttachFileJsonObject.getLong("id"));
                                        SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                        editor.putLong(Constant.ServerAttachFileId, savedAttachFileJsonObject.getLong("id"));
                                        editor.apply();
                                    }

                                    if (!savedAttachFileJsonObject.isNull("totalReceivedBytes")) {
                                        //attachFile.setAttachFileSentSize(savedAttachFileJsonObject.getInt("totalReceivedBytes"));
                                        SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                        editor.putInt(Constant.AttachFileSentSize, savedAttachFileJsonObject.getInt("totalReceivedBytes"));
                                        editor.apply();
                                    }
                                }
                                if (Constant.AttachFileSize != null && Constant.AttachFileSentSize != null && Constant.AttachFileSentSize.compareTo(Constant.AttachFileSize) > 0) {
                                    //attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                                    SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                                    editor.apply();
                                } else if (Constant.AttachFileSize == null || Constant.AttachFileSentSize == null || Constant.AttachFileSize.equals(Constant.AttachFileSentSize)) {
                                    SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                                    editor.apply();
                                } else {
                                    SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                                    editor.apply();
                                    resumeAttachFile(context);
                                }
                            }
                        } else {
                            SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                            editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                            editor.apply();
                        }
                    }

                } else {
                    SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                    editor.apply();
                }
            } else {
                SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = "AttachFileReceiver";
            }
            Log.d(errorMsg, errorMsg);
            SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
            editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
            editor.apply();
        }
    }

    public void getEtCardDataTranSumStatisticallyReportList(String username, String password,String id,
                                                            String fromDate,
                                                            Response.Listener listener,
                                                            Response.ErrorListener errorListener) {
        String ws = Constants.WS + "getDriverEtCardDataTranSumStatisticallyReportList";

        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("password", password));
        wsParameters.add(new Util.WSParameter("driverId", id));
        wsParameters.add(new Util.WSParameter("reportDate", fromDate));
        String json = Util.createJson(wsParameters);
        json = URLEncoder.encode(json);
        ws = ws + "?INPUT_PARAM=" + json;
        Util.printLogs("ws = " + ws);

        GsonRequest gsonRequest = new GsonRequest<>(
                Request.Method.GET,
                ws,
                ReportListResponseBean.class,
                listener,
                errorListener,
                false
        );

        RestClient.getInstance().addToRequestQueue(gsonRequest);

    }

}
