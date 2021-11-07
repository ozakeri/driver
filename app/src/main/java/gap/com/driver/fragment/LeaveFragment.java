package gap.com.driver.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gap.com.datepicker.DatePicker;
import gap.com.datepicker.interfaces.DateSetListener;
import gap.com.driver.R;
import gap.com.driver.activity.FullScreenActivity;
import gap.com.driver.adapter.PersonTimeOffAttachAdapter;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.enumtype.SendingStatusEn;
import gap.com.driver.gapcalendar.materialdatetimepicker.date.DatePickerDialog;
import gap.com.driver.gapcalendar.materialdatetimepicker.utils.PersianCalendar;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.SavedPersonTimeOffResponseBean;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.driverprofile.JsonArrayAttach;
import gap.com.driver.services.MyPostJsonService;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.Constant;
import gap.com.driver.util.DateUtils;
import gap.com.driver.util.Globals;
import gap.com.driver.util.ImageUtil;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CircularProgress;

import static gap.com.driver.R.id.content_frame;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    EditText editTextStartDate, editTextEndDate;
    private int year;
    private int month;
    private int day;
    private ImageView imgStartDate, imgEndDate, img_attach, img_select;
    Globals sharedData = Globals.getInstance();
    private boolean b;
    private RadioGroup selectedLeaveType, selectedRequestTypeEN;
    private RadioButton timeOfDay1, timeOfDay2, requestTypeEN3, requestTypeEN2, requestTypeEN1;
    private TextView txt_description, txt_fileName;
    private Button btn_send;
    private int typeEn, id;
    private CircularProgress progressbar;
    DriverApplication application;
    private Driver driver;
    private Calendar calFrom = null;
    private Calendar calTo = null;
    private Date startDate = null;
    private Date finishDate = null;
    private boolean isOneDate = true;
    private String type, fromDate, toDate, description = null;
    private String fromDateCopy, toDateCopy;
    private JalaliCalendarUtil jalaliCalendarUtil;
    private boolean isEdit = false;
    private CardView card_attach, card_imgShow;
    private RelativeLayout attach_layout;
    private final int PICK_IMAGE = 1;
    private final int TAKE_PICTURE = 2;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    private Bitmap bitmap;
    private String path, filename;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String resId;
    private String filePath;
    public static Integer MAX_ATTACH_FILE_PACKET_SIZE = 8192;
    private String createTransactionID;
    private Uri mCapturedImageURI;
    private int attachFileId;
    private RecyclerView recycler_view;
    private PersonTimeOffAttachAdapter adapter;
    private Fragment fragment;
    SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();


    public LeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave, container, false);
        sharedData.setLeavePage(true);
        sharedData.setMonthlyPage(false);

        editTextStartDate = view.findViewById(R.id.editTextStartDate);
        editTextEndDate = view.findViewById(R.id.editTextEndDate);
        txt_description = view.findViewById(R.id.desc1);
        imgStartDate = view.findViewById(R.id.imgStartDate);
        imgEndDate = view.findViewById(R.id.imgEndDate);
        selectedLeaveType = view.findViewById(R.id.selected_leave_type);
        selectedRequestTypeEN = view.findViewById(R.id.selected_requestTypeEN);
        timeOfDay1 = view.findViewById(R.id.selected_timeOfDay1);
        timeOfDay2 = view.findViewById(R.id.selected_timeOfDay2);
        requestTypeEN1 = view.findViewById(R.id.requestTypeEN1);
        requestTypeEN2 = view.findViewById(R.id.requestTypeEN2);
        requestTypeEN3 = view.findViewById(R.id.requestTypeEN3);
        btn_send = view.findViewById(R.id.btn_send);
        attach_layout = view.findViewById(R.id.attach_layout);
        card_attach = view.findViewById(R.id.card_attach);
        card_imgShow = view.findViewById(R.id.card_imgShow);
        img_attach = view.findViewById(R.id.img_attach);
        img_select = view.findViewById(R.id.img_select);
        txt_fileName = view.findViewById(R.id.txt_fileName);
        recycler_view = view.findViewById(R.id.recycler_view);
        progressbar = (CircularProgress) view.findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        //img_attach.setVisibility(View.GONE);
        //sharedData.setCheckOp(true);
        editTextStartDate.requestFocus();
        Bundle arguments = getArguments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setVisibility(View.GONE);
        card_imgShow.setVisibility(View.GONE);

        if (arguments != null) {
            typeEn = Integer.parseInt(arguments.getString("typeEn"));
            fromDateCopy = arguments.getString("fromDate");
            toDateCopy = arguments.getString("toDate");
            description = arguments.getString("description");
            isEdit = arguments.getBoolean("isEdit");
            id = Integer.parseInt(arguments.getString("id"));
            fromDate = fromDateCopy;
            toDate = toDateCopy;
            getPersonTimeOffAttach(id);
        }

        timeOfDay1.setChecked(true);
        if (toDateCopy != null) {
            timeOfDay2.setChecked(true);
            isOneDate = false;
            editTextStartDate.setHint("      تاریخ شروع      ");
        }

        if (description != null) {
            txt_description.setText(description);
        }

        requestTypeEN1.setChecked(true);
        if (isOneDate) {
            timeOfDay1.setChecked(true);
            timeOfDay2.setChecked(false);
            editTextEndDate.setVisibility(View.GONE);
            imgEndDate.setVisibility(View.GONE);
            editTextStartDate.setHint("      تاریخ      ");

        } else {
            timeOfDay2.setChecked(true);
            timeOfDay1.setChecked(false);
            editTextEndDate.setVisibility(View.VISIBLE);
            imgEndDate.setVisibility(View.VISIBLE);
            editTextStartDate.setHint("      تاریخ شروع      ");
        }

        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        if (fromDateCopy != null) {
            try {
                Date date1 = formatter.parse(fromDateCopy);
                Calendar calender1 = Calendar.getInstance();
                calender1.setTime(date1);
                calFrom = calender1;
                jalaliCalendarUtil = new JalaliCalendarUtil(calender1);
                editTextStartDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (toDateCopy != null) {
            try {
                Date date2 = formatter.parse(toDateCopy);
                Calendar calender2 = Calendar.getInstance();
                calender2.setTime(date2);
                calTo = calender2;
                jalaliCalendarUtil = new JalaliCalendarUtil(calender2);
                editTextEndDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (isEdit) {
            btn_send.setText("ویرایش");
        }
        selectedLeaveType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.selected_timeOfDay1:
                        isOneDate = true;
                        editTextEndDate.setVisibility(View.GONE);
                        imgEndDate.setVisibility(View.GONE);
                        editTextStartDate.setHint("      تاریخ      ");
                        sharedData.setCheckOp(false);
                        break;

                    case R.id.selected_timeOfDay2:
                        isOneDate = false;
                        editTextEndDate.setVisibility(View.VISIBLE);
                        imgEndDate.setVisibility(View.VISIBLE);
                        sharedData.setCheckOp(true);
                        editTextStartDate.setHint("      تاریخ شروع      ");
                        break;
                }
            }
        });

        switch (typeEn) {
            case 0:
                selectedRequestTypeEN.check(R.id.requestTypeEN1);
                attach_layout.setVisibility(View.GONE);
                break;

            case 1:
                selectedRequestTypeEN.check(R.id.requestTypeEN2);
                attach_layout.setVisibility(View.GONE);
                break;

            case 2:
                selectedRequestTypeEN.check(R.id.requestTypeEN3);
                attach_layout.setVisibility(View.VISIBLE);
                break;
        }

        selectedRequestTypeEN.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.requestTypeEN1:
                        typeEn = 0;
                        attach_layout.setVisibility(View.GONE);
                        break;

                    case R.id.requestTypeEN2:
                        attach_layout.setVisibility(View.GONE);
                        typeEn = 1;
                        break;

                    case R.id.requestTypeEN3:
                        attach_layout.setVisibility(View.VISIBLE);
                        typeEn = 2;
                        break;
                }
            }
        });

        /*Bundle bundle = this.getArguments();
        if (bundle != null) {

            year = bundle.getInt("year");
            month = bundle.getInt("monthOfYear");
            day = bundle.getInt("dayOfMonth");

            String date = year + "-" + month + "-" + day;
            SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
            editor.putString("date", date);
            editor.apply();

            editTextStartDate.setText(sharedData.getDateOne());
            editTextEndDate.setText(sharedData.getDateTwo());

        }*/




        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.WEEK_OF_MONTH, maxDate.get(Calendar.WEEK_OF_MONTH) + 2);
                // minDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) - 2);
                new DatePicker.Builder()
                        .id(1)
                        .minDate(minDate)
                        .maxDate(maxDate)
                        .build(new DateSetListener() {
                            @Override
                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                if (calendar == null)
                                    return;

                                calFrom = calendar;
                                jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                editTextStartDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                            }
                        })
                        .show(fm, "");
            }
        });


        imgStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.WEEK_OF_MONTH, maxDate.get(Calendar.WEEK_OF_MONTH) + 2);
                // minDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) - 2);
                new DatePicker.Builder()
                        .id(1)
                        .minDate(minDate)
                        .maxDate(maxDate)
                        .build(new DateSetListener() {
                            @Override
                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                if (calendar == null)
                                    return;

                                calFrom = calendar;
                                jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                editTextStartDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                            }
                        })
                        .show(fm, "");
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) + 1);
                //minDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) - 2);
                new DatePicker.Builder()
                        .id(1)
                        .minDate(minDate)
                        .maxDate(maxDate)
                        .build(new DateSetListener() {
                            @Override
                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                if (calendar == null)
                                    return;

                                calTo = calendar;

                                System.out.println("calendar111=========" + calendar);
                                jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                editTextEndDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                            }
                        })
                        .show(fm, "");
            }
        });

        imgEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) + 1);
                //minDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) - 2);
                new DatePicker.Builder()
                        .id(1)
                        .minDate(minDate)
                        .maxDate(maxDate)
                        .build(new DateSetListener() {
                            @Override
                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                if (calendar == null)
                                    return;

                                calTo = calendar;

                                System.out.println("calendar111=========" + calendar);
                                jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                editTextEndDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                            }
                        })
                        .show(fm, "");
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isDeviceDateTimeValidforEdit();
                } else {
                    isDeviceDateTimeValid();
                    editor.putString(Constant.username, driver.getUsername());
                    editor.putString(Constant.password, driver.getPassword());
                    editor.apply();
                }
            }
        });

        editor.putString(Constant.attachFileLocalPath, null);
        editor.putString(Constant.AttachFileSize, null);
        editor.putString(Constant.AttachFileSentSize, null);
        editor.putString(Constant.ServerAttachFileId, null);
        editor.putString(Constant.AttachFileUserFileName, null);
        editor.apply();

        img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        showAddDialog();
                    } else {
                        requestPermission(); // Code for permission
                    }
                } else {
                    showAddDialog();
                }
            }
        });

        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path != null){
                    Intent intent = new Intent(getActivity(), FullScreenActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void toDate() {
        editor.putBoolean("b", false);
        editor.apply();

        PersianCalendar now = new PersianCalendar();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) getActivity(),
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
        PersianCalendar[] dates = new PersianCalendar[13];
        for (int i = -6; i <= 6; i++) {
            PersianCalendar date = new PersianCalendar();
            date.add(PersianCalendar.MONTH, i);
            dates[i + 6] = date;
        }
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        dpd.setSelectableDays(dates);
        dpd.show(fm, "Datepickerdialog");
    }

    private void fromDate() {
        editor.putBoolean("b", true);
        editor.apply();

        PersianCalendar now = new PersianCalendar();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) getActivity(),
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
        PersianCalendar[] dates = new PersianCalendar[13];
        for (int i = -6; i <= 6; i++) {
            PersianCalendar date = new PersianCalendar();
            date.add(PersianCalendar.MONTH, i);
            dates[i + 6] = date;
        }
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        dpd.setSelectableDays(dates);
        dpd.show(fm, "Datepickerdialog");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, String dayName) {

    }


    private void isDeviceDateTimeValid() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

        application = (DriverApplication) getActivity().getApplication();
        driver = application.getCurrentUser();
        final String username = driver.getUsername();
        final String password = driver.getPassword();
        final String str_description = txt_description.getText().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if (calFrom != null) {
            startDate = calFrom.getTime();
            fromDate = formatter.format(startDate);
        }

        if (calTo != null) {
            finishDate = calTo.getTime();
            toDate = formatter.format(finishDate);
        }


        if (isOneDate) {
            if (calFrom != null) {
                finishDate = calFrom.getTime();
                toDate = formatter.format(finishDate);
            }
        }

        if (fromDate == null) {
            Utils.showToast(getActivity(), "تاریخ شروع را انتخاب کنید", false);
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
            @Override
            public void onResponse(ServerDateTimeResponseBean response) {
                progressbar.setVisibility(View.GONE);
                try {
                    Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                    if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                        progressbar.setVisibility(View.VISIBLE);

                        ServerCoordinator.getInstance().savePersonTimeOff(username, password, Utils.farsiNumberReplacement(fromDate), Utils.farsiNumberReplacement(toDate), typeEn, str_description,
                                new Response.Listener<SavedPersonTimeOffResponseBean>() {
                                    @Override
                                    public void onResponse(SavedPersonTimeOffResponseBean response) {
                                        progressbar.setVisibility(View.GONE);
                                        if (response.getSUCCESS() != null) {
                                            Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                                            Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                                            resId = String.valueOf(response.getRESULT().getSavedPersonTimeOff().getPersonTimeOff().getId());

                                            editor.putString(Constant.ServerEntityId, resId);
                                            createTransactionID = createTransactionID();
                                            System.out.println("createTransactionID-=-=-=-=" + createTransactionID);

                                            editor.putString(Constant.AttachFileSize, null);
                                            editor.putString(Constant.AttachFileSentSize, null);
                                            editor.putString(Constant.ServerAttachFileId, null);
                                            editor.apply();

                                            resumeAttachFile(getActivity());
                                            //resId = String.valueOf(response.getRESULT().personTimeOffVO.getId());
                                           /* try {
                                                saveAttachFile(username, password, String.valueOf(response.getRESULT().getSavedPersonTimeOff().getPersonTimeOff().getId()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }*/
                                            // goToFragment(new ListLeaveFragment());
                                        } else {
                                            Utils.showToast(getActivity(), R.string.error_unknown, false);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressbar.setVisibility(View.GONE);
                                        Utils.showErrors(getActivity(), error);
                                    }
                                });

                    } else {
                        Utils.showToast(getActivity(), R.string.message_toast_invalidDateTime, false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showErrors(getActivity(), error);
            }
        });
    }

    private void isDeviceDateTimeValidforEdit() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        progressbar.setVisibility(View.VISIBLE);

        application = (DriverApplication) getActivity().getApplication();
        driver = application.getCurrentUser();
        final String username = driver.getUsername();
        final String password = driver.getPassword();
        final String str_description = txt_description.getText().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if (calFrom != null) {
            startDate = calFrom.getTime();
            fromDate = formatter.format(startDate);
        }

        if (calTo != null) {
            finishDate = calTo.getTime();
            toDate = formatter.format(finishDate);
        }


        if (isOneDate) {
            if (calFrom != null) {
                finishDate = calFrom.getTime();
                toDate = formatter.format(finishDate);
            }
        }


        ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
            @Override
            public void onResponse(ServerDateTimeResponseBean response) {
                progressbar.setVisibility(View.GONE);
                try {
                    Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                    if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                        progressbar.setVisibility(View.VISIBLE);

                        ServerCoordinator.getInstance().editPersonTimeOff(String.valueOf(id), username, password, Utils.farsiNumberReplacement(fromDate), Utils.farsiNumberReplacement(toDate), typeEn, str_description,
                                new Response.Listener<DriverProfileResponseBean>() {
                                    @Override
                                    public void onResponse(DriverProfileResponseBean response) {
                                        progressbar.setVisibility(View.GONE);
                                        if (response.getSUCCESS() != null) {
                                            Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                                            resId = String.valueOf(response.getRESULT().personTimeOffVO.getId());
                                            editor.putString(Constant.ServerEntityId, resId);
                                            createTransactionID = createTransactionID();
                                            System.out.println("createTransactionID-=-=-=-=" + createTransactionID);

                                            editor.putString(Constant.AttachFileSize, null);
                                            editor.putString(Constant.AttachFileSentSize, null);
                                            editor.putString(Constant.ServerAttachFileId, null);
                                            editor.apply();

                                            resumeAttachFile(getActivity());
                                            // goToFragment(new ListLeaveFragment());
                                        } else {
                                            Utils.showToast(getActivity(), R.string.error_unknown, false);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressbar.setVisibility(View.GONE);
                                        Utils.showErrors(getActivity(), error);
                                    }
                                });

                    } else {
                        Utils.showToast(getActivity(), R.string.message_toast_invalidDateTime, false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showErrors(getActivity(), error);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedData.setLeavePage(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        sharedData.setLeavePage(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedData.setLeavePage(false);
    }

    private void goToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(content_frame, fragment, fragment.getClass().getCanonicalName());
        fragmentTransaction.commit();
    }

    public void showAddDialog() {
        final Dialog dialog_wait = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog_wait.setContentView(R.layout.show_attach_dialog);
        dialog_wait.show();
        dialog_wait.setCancelable(false);
        Window window = dialog_wait.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView txt_gallery = dialog_wait.findViewById(R.id.txt_gallery);
        TextView txt_camera = dialog_wait.findViewById(R.id.txt_camera);
        RelativeLayout layout_close = dialog_wait.findViewById(R.id.layout_close);


        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_ACCESS_PERMISSION);
                } else {
                    getImageFromCamera();
                }
                dialog_wait.dismiss();
            }
        });
        txt_gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                getImageFromGallery();
                dialog_wait.dismiss();
            }
        });
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_wait.dismiss();
            }
        });
    }

    private void getImageFromCamera() {
        String fileName = "temp.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getImageFromGallery() {
       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }*/

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri outputFileUri;
            path = null;
            editor.putString(Constant.attachFileLocalPath, null);
            editor.apply();

            if (requestCode == TAKE_PICTURE) {
                card_imgShow.setVisibility(View.VISIBLE);
                img_attach.setVisibility(View.VISIBLE);
                path = getPathCamera();
                bitmap = resizeBitmap(path, 100, 100);
                img_attach.setImageBitmap(bitmap);
                saveAttachImageFile(path);

            } else if (requestCode == PICK_IMAGE) {
                card_imgShow.setVisibility(View.VISIBLE);
                img_attach.setVisibility(View.VISIBLE);
                outputFileUri = data.getData();
                path = getRealPathFromURI(outputFileUri);
                bitmap = resizeBitmap(path, 100, 100);
                img_attach.setImageBitmap(bitmap);
                saveAttachImageFile(path);
            }
        }


        /*if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                try {


                    Uri selectedImageUri = data.getData();
                    path = getPathFromURI(selectedImageUri);
                    System.out.println("PICK_IMAGE=======" + path);
                    if (path != null) {
                        filename = path.substring(path.lastIndexOf("/") + 1);
                        txt_fileName.setText(filename);
                        editor.putString(Constant.AttachFileUserFileName, filename);
                        editor.putString(Constant.attachFileLocalPath, path);
                        editor.apply();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == TAKE_PICTURE) {
                path = getPathCamera();
                System.out.println("TAKE_PICTURE=======" + path);
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img_attach.setImageBitmap(bitmap);
                }
                card_imgShow.setVisibility(View.VISIBLE);
                img_attach.setVisibility(View.VISIBLE);
                filename = path.substring(path.lastIndexOf("/") + 1);
                txt_fileName.setText(filename);
                editor.putString(Constant.AttachFileUserFileName, filename);
                editor.putString(Constant.attachFileLocalPath, path);
                editor.apply();
            }


        }*/
    }

    //========= getRealPathFromURI
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] pro = {MediaStore.MediaColumns.DATA};
        try {
            Cursor cursor = getActivity().getContentResolver().query(contentUri, pro, null, null, null);
            assert cursor != null;
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                path = cursor.getString(column_index);
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("Err" + e.getMessage());
        }
        return path;
    }

    /*getPathCamera */
    private String getPathCamera() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index_data);
    }


    public void saveAttachFile(final String username, final String password, final String serverEntityId) throws Exception {
        ServerCoordinator.getInstance().saveAttachPersonTimeOff(username, password, path, "id", "serverAttachFileId", "entityNameEn",
                serverEntityId, "serverAttachFileSettingId", filename,
                new Response.Listener<SavedPersonTimeOffResponseBean>() {
                    @Override
                    public void onResponse(SavedPersonTimeOffResponseBean response) {
                        progressbar.setVisibility(View.GONE);
                        if (response.getSUCCESS() != null) {
                            Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                            editor.putString(Constant.ServerEntityId, serverEntityId);
                            createTransactionID = createTransactionID();

                            editor.putString(Constant.AttachFileSize, null);
                            editor.putString(Constant.AttachFileSentSize, null);
                            editor.putString(Constant.ServerAttachFileId, null);
                            editor.apply();

                            resumeAttachFile(getActivity());


                        } else {
                            Utils.showToast(getActivity(), R.string.error_unknown, false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        Utils.showErrors(getActivity(), error);
                    }
                });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public void resumeAttachFile(Context context) {

        if (img_attach.getVisibility() == View.GONE) {
            System.out.println("=-=-===-getBackground=-==-=--");
            goToFragment(new ListLeaveFragment());
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {

            String serverAttachFileId = DriverApplication.getInstance().getSharedPreferences().getString(Constant.ServerAttachFileId, null);
            String attachFileUserFileName = DriverApplication.getInstance().getSharedPreferences().getString(Constant.AttachFileUserFileName, null);
            String attachFileLocalPath = DriverApplication.getInstance().getSharedPreferences().getString(Constant.attachFileLocalPath, null);
            String serverEntityId = DriverApplication.getInstance().getSharedPreferences().getString(Constant.ServerEntityId, null);

            jsonObject.put("username", driver.getUsername());
            jsonObject.put("password", driver.getPassword());
            JSONObject attachFileJsonObject = new JSONObject();
            attachFileJsonObject.put("id", createTransactionID);
            attachFileJsonObject.put("serverId", serverAttachFileId);
            attachFileJsonObject.put("entityNameEn", 128);
            attachFileJsonObject.put("entityId", serverEntityId);
            attachFileJsonObject.put("attachFileSettingId", 1009);
            attachFileJsonObject.put("attachFileUserFileName", attachFileUserFileName);

            System.out.println("serverAttachFileId=-=-=-=-==" + serverAttachFileId);
            System.out.println("createTransactionID=-=-=-=-==" + createTransactionID);


            if (attachFileLocalPath != null) {
                File attachedFile = new File(attachFileLocalPath);
                if (attachedFile.exists()) {
                    String attachFileSentSize = DriverApplication.getInstance().getSharedPreferences().getString(Constant.AttachFileSentSize, null);
                    if (attachFileSentSize == null) {
                        editor.putString(Constant.AttachFileSentSize, null);
                        editor.apply();
                    }


                    FileInputStream inputStream = new FileInputStream(attachedFile);
                    Integer fileSize = inputStream.available();
                    editor.putString(Constant.AttachFileSize, String.valueOf(fileSize));
                    editor.apply();

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
                            progressbar.setVisibility(View.GONE);
                            if (!resultJson.isNull("RESULT")) {
                                System.out.println("---------1----------");
                                JSONObject resultJsonObject = resultJson.getJSONObject("RESULT");
                                if (!resultJsonObject.isNull("savedAttachFile")) {
                                    JSONObject savedAttachFileJsonObject = resultJsonObject.getJSONObject("savedAttachFile");
                                    if (!savedAttachFileJsonObject.isNull("id")) {
                                        editor.putString(Constant.ServerAttachFileId, savedAttachFileJsonObject.getString("id"));
                                        editor.apply();
                                    }

                                    if (!savedAttachFileJsonObject.isNull("totalReceivedBytes")) {
                                        editor.putString(Constant.AttachFileSentSize, savedAttachFileJsonObject.getString("totalReceivedBytes"));
                                        editor.apply();
                                    }
                                }

                                String attachFileSize = DriverApplication.getInstance().getSharedPreferences().getString(Constant.AttachFileSize, null);
                                attachFileSentSize = DriverApplication.getInstance().getSharedPreferences().getString(Constant.AttachFileSentSize, null);

                                System.out.println("attachFileSize-----" + attachFileSize);
                                System.out.println("attachFileSentSize-----" + attachFileSentSize);

                                if (attachFileSize != null && attachFileSentSize != null && Integer.valueOf(attachFileSentSize).compareTo(Integer.valueOf(attachFileSize)) > 0) {

                                    //attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                                    editor.apply();
                                    System.out.println("---------5----------");
                                    progressbar.setVisibility(View.GONE);
                                    goToFragment(new ListLeaveFragment());
                                } else if (attachFileSize == null || attachFileSentSize == null || Integer.valueOf(attachFileSize).equals(Integer.valueOf(attachFileSentSize))) {
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Sent.ordinal());
                                    editor.apply();
                                    goToFragment(new ListLeaveFragment());
                                } else {
                                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.AttachmentResuming.ordinal());
                                    editor.apply();
                                    System.out.println("---------7----------");
                                    System.out.println("---------7----------");

                                    resumeAttachFile(context);

                                }
                            }
                        } else {
                            editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                            editor.apply();
                            System.out.println("---------8----------");
                            progressbar.setVisibility(View.GONE);
                        }
                    }

                } else {
                    editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                    editor.apply();
                    System.out.println("---------9----------");
                    progressbar.setVisibility(View.GONE);
                }
            } else {
                editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
                editor.apply();
                System.out.println("---------10----------");
                progressbar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = "AttachFileReceiver";
            }
            Log.d(errorMsg, errorMsg);
            editor.putInt(Constant.SendingStatusEn, SendingStatusEn.Fail.ordinal());
            editor.apply();
            System.out.println("---------11----------");
            progressbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("DefaultLocale")
    public String createTransactionID() {
        return String.format("%05d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
    }


    public void getPersonTimeOffAttach(final int id) {
        recycler_view.setVisibility(View.VISIBLE);
        card_attach.setVisibility(View.VISIBLE);
        application = (DriverApplication) getActivity().getApplication();
        driver = application.getCurrentUser();
        application = (DriverApplication) getActivity().getApplication();
        driver = application.getCurrentUser();

        if (driver == null)
            return;

        ServerCoordinator.getInstance().getDriverPersonTimeOffAttachList(driver.getUsername(), driver.getPassword(), id,
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        if (response != null) {
                            List<JsonArrayAttach> jsonArrayAttachList = response.getrESULT().getJsonArrayAttach();
                            if (jsonArrayAttachList.size() > 0) {
                                card_attach.setVisibility(View.GONE);
                                card_imgShow.setVisibility(View.VISIBLE);
                                adapter = new PersonTimeOffAttachAdapter(getActivity(), jsonArrayAttachList, driver, LeaveFragment.this, id);
                                recycler_view.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                card_imgShow.setVisibility(View.GONE);
                            }

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }

    public void saveAttachImageFile(String filePath) {
        File file = new File(String.valueOf(filePath));
        if (file.exists() && Long.valueOf(file.length()).compareTo((long) 1e+7) <= 0) {
            String userFileName = file.getName();
            long length = file.length();
            length = length / 1024;
            System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
            String filePostfix = userFileName.substring(userFileName.indexOf("."), userFileName.length());
            String path = Environment.getExternalStorageDirectory().toString() + Constants.DEFAULT_OUT_PUT_DIR + Constants.DEFAULT_IMG_OUT_PUT_DIR;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            editor.putString(Constant.AttachFileUserFileName, userFileName);
            editor.apply();

            String newFilePath = path + "/" + createTransactionID() + filePostfix;
            try {
                InputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = new FileOutputStream(newFilePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; //try to decrease decoded image
                options.inPurgeable = true; //purgeable to disk

                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
                outputStream.close();
                Long fileSize = new File(newFilePath).length();
                editor.putString(Constant.AttachFileSize, String.valueOf(fileSize.intValue() / 1024));
                editor.putString(Constant.attachFileLocalPath, filePath);
                editor.apply();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
}
