package gap.com.driver.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import gap.com.driver.R;
import gap.com.driver.adapter.CarDailyEventListAdapter;
import gap.com.driver.adapter.DescriptionListAdapter;
import gap.com.driver.adapter.DriverChangeShiftListAdapter;
import gap.com.driver.adapter.DriverDailyActivityListAdapter;
import gap.com.driver.adapter.DriverShiftListAdapter;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.CalendarUtil;
import gap.com.driver.common.Constants;
import gap.com.driver.database.DataBaseClint;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.gapcalendar.customweekview.CommonMethod;
import gap.com.driver.gapcalendar.customweekview.DateConvertor;
import gap.com.driver.gapcalendar.customweekview.PersianCalendar;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.CarDailyEventList;
import gap.com.driver.model.DailyEventList;
import gap.com.driver.model.DriverChangedEDAList;
import gap.com.driver.model.DriverDailyActivity;
import gap.com.driver.model.DriverEDAList;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.EventBusModel;
import gap.com.driver.model.ReportListResponseBean;
import gap.com.driver.model.SalaryAttribute;
import gap.com.driver.model.SalaryAttributeResponseBean;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.driverprofile.DriverSACommentList;
import gap.com.driver.model.update.UpdateVersionResponseBean;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.DateUtils;
import gap.com.driver.util.Globals;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.MyValueFormatter;
import gap.com.driver.util.Util;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CircularProgress;

import static android.view.View.VISIBLE;

/**
 * Created by GapCom on 01/08/2018.
 */

public class WeekDayFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
    //private Calendar calendar = Calendar.getInstance();
    private PersianCalendar persianCalendar = new PersianCalendar();
    private int day;
    private int month;
    private int year;
    private String monthName;
    private Roozh roozh = new Roozh();
    private PersianDate persianDate;
    private Globals sharedData;
    private TextView showEmptyMessage, txt_showEmpty1, txt_showEmpty2, dailyWorkTypeEn, startDate, dutyWork, fractionPerform, overtimeWorkReal, timeInLine, halfPathNoGps, endDate, fractionWork, nightWork, txt_company, txt_companyChanged, txt_shift, txt_shiftChanged, txt_car, txt_carChanged, txt_line, txt_lineChanged, txt_driverStatus, txt_overTimeWork, txt_overTimeEn;
    private ImageView img_change1, img_change2, img_change3, img_change4, img_status, img_fab1, img_fab2, img_fab3,img_fab4, img_back;
    private RelativeLayout relativeLayout22, relativeLayout33, disable_layout, disable_right_layout;
    private LinearLayout end_layout, start_layout, bottom_layout, linearLayout3, linearLayout1, linearLayout2;
    private String str_processStatus;
    private CircularProgress progressbar;
    private DateFormat sdf;
    private SalaryAttribute salaryAttribute;
    private RecyclerView recycler_view, changeRecycler_view, recyclerView1, recyclerView2;
    private boolean relativeLayout22_status, relativeLayout33_status, relativeLayout44_status,chart_status = false;
    private ArrayList<CarDailyEventList> carDailyEventList;
    private ArrayList<DailyEventList> dailyEventLists;
    private ScrollView main_layout;
    private CardView shift_layout, changeShift_layout, relativeLayout44,relativeLayout_chart, card_left, card_right;
    private boolean showCaseViewFirFirst = false;
    private ImageView img_progress, btnListFab;
    //private static final String SHOWCASE_ID = "WeekDayFragment";
    private List<Driver> driverList;
    private Driver driver;
    private int salaryAttributeId = 0;
    private List<DriverSACommentList> driverSACommentList;
    private CircularProgressView waitProgress;
    private TextView txt_null;
    private float xCoOrdinate, yCoOrdinate;
    private BarChart chart;

    public WeekDayFragment() {

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup rootView, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dayview_layout_test, rootView, false);

        persianDate = new PersianDate();
        sharedData = Globals.getInstance();
        DateConvertor dateConvertor = new DateConvertor();
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
        getAllDriver();

        // if (sharedData.getSelectedMenuItem() != null && sharedData.getSelectedMenuItem().equals(Constant.ACTION_DAY)) {


        System.out.println("showCaseViewFirFirst====" + showCaseViewFirFirst);

        if (sharedData.getYear() != 0 && sharedData.getMonth() != 0 && sharedData.getDay() != 0) {
            year = sharedData.getYear();
            month = sharedData.getMonth();
            day = sharedData.getDay();

        } else {
            year = dateConvertor.getIranianYear();
            month = dateConvertor.getIranianMonth();
            day = dateConvertor.getIranianDay();

            sharedData.setYear(year);
            sharedData.setMonth(month);
            sharedData.setDay(day);
        }

           /* if (!DriverApplication.getInstance().getSharedPreferences().getBoolean(Constants.PREF_GOTO_SETTING, false)) {
                Fragment fragment = new PrivacyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                Util.recognizeSelectedItems(Constant.RECOGNIZE, "PrivacyFragment");
            }*/


        txt_company = view.findViewById(R.id.txt_company);
        txt_companyChanged = view.findViewById(R.id.txt_companyChanged);
        txt_shift = view.findViewById(R.id.txt_shift);
        txt_shiftChanged = view.findViewById(R.id.txt_shiftChanged);
        txt_car = view.findViewById(R.id.txt_car);
        txt_carChanged = view.findViewById(R.id.txt_carChanged);
        txt_line = view.findViewById(R.id.txt_line);
        txt_lineChanged = view.findViewById(R.id.txt_lineChanged);
        //txt_hamShift = view.findViewById(R.id.txt_hamShift);
        //img_hamShift = view.findViewById(R.id.img_hamShift);
        txt_driverStatus = view.findViewById(R.id.txt_driverStatus);
        img_status = view.findViewById(R.id.img_status);
        recycler_view = view.findViewById(R.id.recycler_view);
        changeRecycler_view = view.findViewById(R.id.changeRecycler_view);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        card_right = view.findViewById(R.id.card_right);
        card_left = view.findViewById(R.id.card_left);
        img_back = view.findViewById(R.id.img_back);
        img_progress = view.findViewById(R.id.img_progress);
        btnListFab = view.findViewById(R.id.btnListFab);
        // linearLayoutMain = view.findViewById(R.id.flipLayout);

        Glide.with(this)
                .load(R.raw.myanimation)
                .into(img_progress);

        RelativeLayout relativeLayout2 = view.findViewById(R.id.relativeLayout2);
        RelativeLayout relativeLayout3 = view.findViewById(R.id.relativeLayout3);
        RelativeLayout relativeLayout4 = view.findViewById(R.id.relativeLayout4);
        RelativeLayout relativeLayout5 = view.findViewById(R.id.relativeLayout5);
        relativeLayout22 = view.findViewById(R.id.relativeLayout22);
        relativeLayout33 = view.findViewById(R.id.relativeLayout33);
        relativeLayout44 = view.findViewById(R.id.relativeLayout44);
        relativeLayout_chart = view.findViewById(R.id.relativeLayout_chart);
        disable_layout = view.findViewById(R.id.disable_layout);
        disable_right_layout = view.findViewById(R.id.disable_right_layout);
        main_layout = view.findViewById(R.id.main_layout);
        end_layout = view.findViewById(R.id.end_layout);
        start_layout = view.findViewById(R.id.start_layout);
        bottom_layout = view.findViewById(R.id.bottom_layout);
        linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
        main_layout.setVisibility(View.GONE);
        bottom_layout.setVisibility(View.GONE);


        shift_layout = view.findViewById(R.id.shift_layout);
        changeShift_layout = view.findViewById(R.id.changeShift_layout);
        showEmptyMessage = view.findViewById(R.id.showEmpty_txt);
        txt_showEmpty1 = view.findViewById(R.id.txt_showEmpty1);
        txt_showEmpty2 = view.findViewById(R.id.txt_showEmpty2);
        img_change1 = view.findViewById(R.id.img_change1);
        img_change2 = view.findViewById(R.id.img_change2);
        img_change3 = view.findViewById(R.id.img_change3);
        img_change4 = view.findViewById(R.id.img_change4);


        img_fab1 = view.findViewById(R.id.img_fab1);
        img_fab2 = view.findViewById(R.id.img_fab2);
        img_fab3 = view.findViewById(R.id.img_fab3);
        img_fab4 = view.findViewById(R.id.img_fab4);
        chart = view.findViewById(R.id.chart);


        dailyWorkTypeEn = view.findViewById(R.id.txt_dailyWorkTypeEn);
        startDate = view.findViewById(R.id.txt_startDate);
        dutyWork = view.findViewById(R.id.txt_dutyWork);
        fractionPerform = view.findViewById(R.id.txt_fractionPerform);
        overtimeWorkReal = view.findViewById(R.id.txt_overtimeWorkReal);
        timeInLine = view.findViewById(R.id.txt_timeInLine);
        halfPathNoGps = view.findViewById(R.id.txt_halfPathNoGps);
        txt_overTimeEn = view.findViewById(R.id.txt_overTimeEn);
        txt_overTimeWork = view.findViewById(R.id.txt_overTimeWork);
        endDate = view.findViewById(R.id.txt_endDate);
        fractionWork = view.findViewById(R.id.txt_fractionWork);
        nightWork = view.findViewById(R.id.txt_nightWork);
        progressbar = view.findViewById(R.id.progressbar);

        isDeviceDateTimeValid(this.year, this.month, this.day);

        /*
         * set date for current day
         * */
        Date date = new Date();
        roozh.PersianToGregorian(year, month, day);
        cal.set(roozh.getYear(), roozh.getMonth(), roozh.getDay());
        cal.add(Calendar.MONTH, -1);
        Date date12 = cal.getTime();

        if (Util.compareDates(date, date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("امروز" + " , " + persianDate.dayName(year, month, day));
            card_left.setEnabled(false);
        } else {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText(persianDate.dayName(year, month, day));
        }

        gap.com.driver.gapcalendar.persianweekview.PersianCalendar persianCalendar1 =
                new gap.com.driver.gapcalendar.persianweekview.PersianCalendar(roozh.getYear(), roozh.getMonth(), roozh.getDay());

        int day = persianCalendar1.getIranianDay();
        int month = persianCalendar1.getIranianMonth();
        String monthName = CommonMethod.convertLessThanOneThousand(month);
        int year = persianCalendar1.getIranianYear();
        sharedData.setDayMenuTitle(day + " " + monthName + " " + year);
        TextView txtView = ((Activity) getContext()).findViewById(R.id.spinner_txt);
        txtView.setText(Util.faToEn(day + " " + monthName + " " + year));

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!relativeLayout22_status) {
                    relativeLayout22.setVisibility(VISIBLE);
                    relativeLayout22_status = true;
                    img_fab1.setBackgroundResource(R.drawable.negative_icon);

                } else {

                    relativeLayout22.setVisibility(View.GONE);
                    relativeLayout22_status = false;
                    img_fab1.setBackgroundResource(R.drawable.plus_icon);
                }
            }
        });

        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!relativeLayout33_status) {
                    relativeLayout33.setVisibility(VISIBLE);
                    relativeLayout33_status = true;
                    img_fab2.setBackgroundResource(R.drawable.negative_icon);

                } else {
                    relativeLayout33.setVisibility(View.GONE);
                    relativeLayout33_status = false;
                    img_fab2.setBackgroundResource(R.drawable.plus_icon);
                }
            }
        });

        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!relativeLayout44_status) {
                    relativeLayout44.setVisibility(VISIBLE);
                    relativeLayout44_status = true;
                    img_fab3.setBackgroundResource(R.drawable.negative_icon);

                } else {
                    relativeLayout44.setVisibility(View.GONE);
                    relativeLayout44_status = false;
                    img_fab3.setBackgroundResource(R.drawable.plus_icon);
                }
            }
        });

        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chart_status) {
                    relativeLayout_chart.setVisibility(VISIBLE);
                    chart_status = true;
                    img_fab4.setBackgroundResource(R.drawable.negative_icon);

                } else {
                    relativeLayout_chart.setVisibility(View.GONE);
                    chart_status = false;
                    img_fab4.setBackgroundResource(R.drawable.plus_icon);
                }
            }
        });


        card_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousCalendarDate();
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });


        card_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextCalendarDate();
                getActivity().overridePendingTransition(R.anim.left_out, R.anim.right_in);
            }
        });


        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        changeRecycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMonthlyFragment();
            }
        });


        btnListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog();
            }
        });

      /*  btnListFab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xCoOrdinate = view.getX() - event.getRawX();
                        yCoOrdinate = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });*/

        return view;
    }


    /*
     *
     * previous date
     * */
    private void previousCalendarDate() {

        cal.add((GregorianCalendar.DATE), -1);
        // calendar.add(Calendar.DAY_OF_MONTH, -1);

        Date date12 = cal.getTime();
        Date date = new Date();
        System.out.println("changeDay=" + date);
        System.out.println("changeDay=" + date12);
        // Util.compareDates(date, date12);

        persianDate = new PersianDate(cal.getTime());
        System.out.println("=-==-=-=-=-=-" + persianDate.getShDay());

        day = persianDate.getShDay();
        month = persianDate.getShMonth();
        monthName = CommonMethod.convertLessThanOneThousand(month);
        year = persianDate.getShYear();
        System.out.println("=-==-=-=-=-=-" + year + "/" + month + "/" + day);

        sharedData.setDayMenuTitle(day + " " + monthName + " " + year);
        TextView txtView = ((Activity) getContext()).findViewById(R.id.spinner_txt);
        txtView.setText(Util.faToEn(day + " " + monthName + " " + year));
        sharedData.setYear(year);
        sharedData.setMonth(month);
        sharedData.setDay(day);
        card_left.setEnabled(true);

        if (Util.compareDates(getYesterdayDate(date), date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("دیروز" + " , " + persianDate.dayName(year, month, day));

        } else if (Util.compareDates(getTomorrowDate(date), date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("فردا" + " , " + persianDate.dayName(year, month, day));
        } else if (Util.compareDates(date, date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("امروز" + " , " + persianDate.dayName(year, month, day));
            card_left.setEnabled(false);
        } else {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText(persianDate.dayName(year, month, day));
        }

        isDeviceDateTimeValid(this.year, this.month, this.day);
    }


    /*
     *
     * next date
     * */
    private void nextCalendarDate() {
        cal.add((GregorianCalendar.DATE), 1);
        // calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date12 = cal.getTime();
        Date date = new Date();
        System.out.println("changeDay=" + date);
        System.out.println("changeDay=" + date12);

        persianDate = new PersianDate(cal.getTime());
        System.out.println("=-==-=-=-=-=-" + persianDate.getShDay());

        day = persianDate.getShDay();
        month = persianDate.getShMonth();
        monthName = CommonMethod.convertLessThanOneThousand(month);
        year = persianDate.getShYear();
        System.out.println("=-==-=-=-=-=-" + year + "/" + month + "/" + day);

        sharedData.setDayMenuTitle(day + " " + monthName + " " + year);
        TextView txtView = ((Activity) getContext()).findViewById(R.id.spinner_txt);
        txtView.setText(Util.faToEn(day + " " + monthName + " " + year));
        sharedData.setYear(year);
        sharedData.setMonth(month);
        sharedData.setDay(day);
        card_left.setEnabled(true);

        if (Util.compareDates(getYesterdayDate(date), date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("دیروز" + " , " + persianDate.dayName(year, month, day));

        } else if (Util.compareDates(getTomorrowDate(date), date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("فردا" + " , " + persianDate.dayName(year, month, day));
        } else if (Util.compareDates(date, date12) == 0) {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText("امروز" + " , " + persianDate.dayName(year, month, day));
            card_left.setEnabled(false);
        } else {
            TextView showDateName = (TextView) ((Activity) getContext()).findViewById(R.id.showDateName_txt);
            showDateName.setText(persianDate.dayName(year, month, day));
        }

        isDeviceDateTimeValid(this.year, this.month, this.day);
    }

    // Method to get Yesterday Date
    public static Date getYesterdayDate(Date date) {

        Date yesterdayDate = null;

        if (date != null) {

            Calendar c = Calendar.getInstance();

            c.setTime(date); // Setting the today date

            c.add(Calendar.DATE, -1); // Decreasing 1 day

            yesterdayDate = c.getTime();
        }

        return yesterdayDate;
    }

    /*
     * Method to get tomorrow Date
     * */
    public static Date getTomorrowDate(Date date) {

        Date tomorrowDate = null;

        if (date != null) {

            Calendar c = Calendar.getInstance();

            c.setTime(date); // Setting the today date

            c.add(Calendar.DATE, 1); // Increasing 1 day

            tomorrowDate = c.getTime();

        }

        return tomorrowDate;
    }


    /*
     * navigation bottom button in day fragment
     * */
    /*public void init_persistent_bottomsheet() {
        persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
        iv_trigger = (ImageView) persistentbottomSheet.findViewById(R.id.iv_fab);
        relativeLayout = (RelativeLayout) persistentbottomSheet.findViewById(R.id.relativeLayout);
        linearLayout = (LinearLayout) persistentbottomSheet.findViewById(R.id.linearLayout);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(persistentbottomSheet);


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    linearLayout.setBackgroundResource(R.color.glass_white);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        if (behavior != null)
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            iv_trigger.setBackgroundResource(R.drawable.negative_icon);
                            linearLayout.setBackgroundResource(R.color.glass_white);
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            iv_trigger.setBackgroundResource(R.drawable.plus_icon);
                            linearLayout.setBackgroundResource(0);
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            linearLayout.setBackgroundResource(R.color.glass_white);
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // React to dragging events

                }
            });
    }*/

    /*
     * get invalid date time and call monthly salary attribute with type 0
     * */
    private void isDeviceDateTimeValid(final int year, final int month, final int day) {
        EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
        DriverApplication application = (DriverApplication) getActivity().getApplication();
        final Driver driver = application.getCurrentUser();
        btnListFab.setVisibility(View.GONE);
        if (driver != null) {
            final String username = driver.getUsername();
            final String password = driver.getPassword();
            final int type = 0;
            final String fromDate = year + "-" + month + "-" + day;
            progressbar.setVisibility(VISIBLE);
            img_progress.setVisibility(VISIBLE);
            main_layout.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.GONE);

            showEmptyMessage.setVisibility(View.GONE);
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //final String toDate = "";

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
                @Override
                public void onResponse(ServerDateTimeResponseBean response) {
                    progressbar.setVisibility(View.GONE);
                    img_progress.setVisibility(View.GONE);
                    try {
                        Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                        if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                            progressbar.setVisibility(VISIBLE);
                            img_progress.setVisibility(VISIBLE);
                            main_layout.setVisibility(View.GONE);
                            bottom_layout.setVisibility(View.GONE);
                            ServerCoordinator.getInstance().getDriverSalaryAttribute(username, password, type, fromDate,
                                    new Response.Listener<SalaryAttributeResponseBean>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onResponse(SalaryAttributeResponseBean response) {
                                            progressbar.setVisibility(View.GONE);
                                            img_progress.setVisibility(View.GONE);
                                            main_layout.setVisibility(VISIBLE);
                                            bottom_layout.setVisibility(VISIBLE);

                                            if (response.getSalaryAttribute() != null) {
                                                salaryAttribute = response.getSalaryAttribute();
                                                showEmptyMessage.setVisibility(View.GONE);

                                                if (salaryAttribute.getProcessStatus() != null && salaryAttribute.getProcessStatus() == 0) {

                                                }

                                                if (salaryAttribute.getDriverDailyActivity() != null) {
                                                    btnListFab.setVisibility(VISIBLE);
                                                    carDailyEventList = (ArrayList<CarDailyEventList>) salaryAttribute.getDriverDailyActivity().getCarDailyEventList();
                                                    recyclerView1.setAdapter(new CarDailyEventListAdapter(getActivity(), carDailyEventList));

                                                    dailyEventLists = (ArrayList<DailyEventList>) salaryAttribute.getDriverDailyActivity().getDailyEventList();
                                                    recyclerView2.setAdapter(new DriverDailyActivityListAdapter(getActivity(), dailyEventLists));

                                                    if (dailyEventLists != null) {
                                                        txt_showEmpty1.setVisibility(View.GONE);
                                                    } else {
                                                        txt_showEmpty1.setVisibility(VISIBLE);
                                                    }

                                                    if (carDailyEventList != null) {
                                                        txt_showEmpty2.setVisibility(View.GONE);
                                                    } else {
                                                        txt_showEmpty2.setVisibility(VISIBLE);
                                                    }

                                                    DriverDailyActivity driverDailyActivity = salaryAttribute.getDriverDailyActivity();

                                                    txt_company.setText(driverDailyActivity.getCompanyProfit().getNameFv());

                                                    if (driverDailyActivity.isCompanyIsChangedFV()) {
                                                        if (driverDailyActivity.getCompanyChangedFV() != null) {
                                                            txt_companyChanged.setVisibility(VISIBLE);
                                                            // img_change1.setVisibility(VISIBLE);
                                                            txt_companyChanged.setText(driverDailyActivity.getCompanyChangedFV().getName());
                                                        }

                                                    } else {
                                                        txt_companyChanged.setVisibility(View.INVISIBLE);
                                                        img_change1.setVisibility(View.INVISIBLE);
                                                    }

                                                    int shiftType = driverDailyActivity.getShiftTypeEn();
                                                   /* switch (shiftType) {
                                                        case 0:
                                                            //str_shiftType = getResources().getString(R.string.label_shiftType_Morning);
                                                            break;

                                                        case 1:
                                                            //str_shiftType = getResources().getString(R.string.label_shiftType_Afternoon);
                                                            break;

                                                        case 2:
                                                            //str_shiftType = getResources().getString(R.string.label_shiftType_Night);
                                                            break;

                                                        case 3:
                                                            //str_shiftType = getResources().getString(R.string.label_shiftType_Other);
                                                            break;

                                                        case 4:
                                                            // str_shiftType = getResources().getString(R.string.label_shiftType_General);
                                                            break;

                                                        case 5:
                                                            //str_shiftType = getResources().getString(R.string.label_shiftType_MidDay);
                                                            break;
                                                    }*/
                                                    txt_shift.setText(driverDailyActivity.getDriverShiftPlanStrFV());

                                                    if (driverDailyActivity.isDriverShiftPlanIsChangedFV()) {
                                                        txt_shiftChanged.setVisibility(VISIBLE);
                                                        //img_change2.setVisibility(VISIBLE);
                                                        txt_shiftChanged.setText(driverDailyActivity.getDriverShiftChangedStrFV());
                                                    } else {
                                                        txt_shiftChanged.setVisibility(View.INVISIBLE);
                                                        img_change2.setVisibility(View.INVISIBLE);
                                                    }


                                                    txt_car.setText(driverDailyActivity.getCarPlanStrFV());
                                                    if (driverDailyActivity.isCarPlanIsChangedFV()) {
                                                        txt_carChanged.setVisibility(VISIBLE);
                                                        //img_change3.setVisibility(VISIBLE);
                                                        txt_carChanged.setText(driverDailyActivity.getCarChangedStrFV());
                                                    } else {
                                                        txt_carChanged.setVisibility(View.INVISIBLE);
                                                        img_change3.setVisibility(View.INVISIBLE);
                                                    }


                                                    txt_line.setText(driverDailyActivity.getLinePlanStrFV());
                                                    if (driverDailyActivity.isLinePlanIsChangedFV() && !driverDailyActivity.getLineChangedStrFV().equals("---")) {
                                                        txt_lineChanged.setVisibility(VISIBLE);
                                                        //img_change4.setVisibility(VISIBLE);
                                                        txt_lineChanged.setText(driverDailyActivity.getLineChangedStrFV());
                                                    } else {
                                                        txt_lineChanged.setVisibility(View.INVISIBLE);
                                                        img_change4.setVisibility(View.INVISIBLE);
                                                    }

                                                    List<DriverEDAList> driverEDALists = new ArrayList<>();
                                                    if (driverDailyActivity.getDriverEDAList() != null) {
                                                        for (DriverEDAList driverEDAList : driverDailyActivity.getDriverEDAList()) {
                                                            if (driverEDAList != null) {
                                                                driverEDALists.add(driverEDAList);
                                                                shift_layout.setVisibility(VISIBLE);
                                                            }
                                                        }

                                                        recycler_view.setAdapter(new DriverShiftListAdapter(getActivity(), driverEDALists));
                                                    } else {
                                                        shift_layout.setVisibility(View.GONE);
                                                    }

                                                    List<DriverChangedEDAList> driverChangedEDALists = new ArrayList<>();
                                                    if (driverDailyActivity.getDriverChangedEDALists() != null) {
                                                        for (DriverChangedEDAList driverChangedEDAList : driverDailyActivity.getDriverChangedEDALists()) {
                                                            if (driverChangedEDAList != null) {
                                                                driverChangedEDALists.add(driverChangedEDAList);
                                                                changeShift_layout.setVisibility(VISIBLE);
                                                            }
                                                        }

                                                        changeRecycler_view.setAdapter(new DriverChangeShiftListAdapter(getActivity(), driverChangedEDALists));
                                                    } else {
                                                        changeShift_layout.setVisibility(View.GONE);
                                                    }

                                                    int driverJobType = driverDailyActivity.getDriverJobTypeEn();
                                                   /* switch (driverJobType) {
                                                        case 0:
                                                            //str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_DetermineCarForDriver);
                                                            break;

                                                        case 1:
                                                            //str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_RotatoryDriverInLine);
                                                            break;

                                                        case 2:
                                                            // str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_DriverInParking);
                                                            break;

                                                        case 3:
                                                            // str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_RescuerSOS);
                                                            break;

                                                        case 4:
                                                            // str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_AssistantRescuerSOS);
                                                            break;

                                                        case 5:
                                                            //str_driverJobType = getResources().getString(R.string.label_driverJobTypeEn_WorkOnContract);
                                                            break;
                                                    }*/

                                                    linearLayout1.setVisibility(VISIBLE);
                                                    linearLayout2.setVisibility(View.VISIBLE);
                                                    linearLayout3.setVisibility(View.VISIBLE);
                                                    disable_layout.setVisibility(View.GONE);
                                                    disable_right_layout.setVisibility(View.GONE);
                                                   // card_left.setEnabled(true);
                                                    //card_right.setEnabled(true);

                                                    int processStatus = driverDailyActivity.getProcessStatus();
                                                    boolean b = DriverApplication.getInstance().getSharedPreferences().getBoolean("firstLogin", false);

                                                    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd");
                                                    try {
                                                        Date d1 = sdformat.parse(sdformat.format(new Date()));
                                                        Date d2 = sdformat.parse(salaryAttribute.getDate());

                                                        monthsBetweenDates(d1, d2);

                                                        String firstDate = CalendarUtil.convertPersianDateTime(d1, "yyyy/MM/dd");
                                                        String[] separated1 = firstDate.split("/");
                                                        String year1 = separated1[0];
                                                        String month1 = separated1[1];
                                                        String day1 = separated1[2];

                                                        String secondDate = CalendarUtil.convertPersianDateTime(d2, "yyyy/MM/dd");
                                                        String[] secondDate2 = secondDate.split("/");
                                                        String year2 = secondDate2[0];
                                                        String month2 = secondDate2[1];
                                                        String day2 = secondDate2[2];

                                                        if (Integer.parseInt(year1) == Integer.parseInt(year2) && (Integer.parseInt(month1) - Integer.parseInt(month2) == 6)
                                                                && Integer.parseInt(day1) >= Integer.parseInt(day2)) {
                                                            card_right.setEnabled(false);
                                                            disable_right_layout.setVisibility(VISIBLE);
                                                        }

                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }


                                                    start_layout.setVisibility(VISIBLE);
                                                    end_layout.setVisibility(VISIBLE);
                                                    try {
                                                        switch (processStatus) {
                                                            case 5:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State1);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 8:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State4);
                                                                start_layout.setVisibility(View.GONE);
                                                                end_layout.setVisibility(View.GONE);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 9:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State5);

                                                                break;

                                                            case 10:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State6);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 20:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State7);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 21:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State8);
                                                                break;

                                                            case 22:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State9);
                                                                break;

                                                            case 24:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State11);
                                                                start_layout.setVisibility(View.GONE);
                                                                end_layout.setVisibility(View.GONE);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 25:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State12);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 50:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_State50);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;

                                                            case 27:
                                                                str_processStatus = getResources().getString(R.string.label_ProcessStatus_EmployeeDuty);
                                                                linearLayout1.setVisibility(View.GONE);
                                                                linearLayout2.setVisibility(View.GONE);
                                                                break;
                                                        }
                                                    } catch (Exception e) {
                                                        e.getMessage();
                                                    }

                                                    if (str_processStatus != null) {
                                                        txt_driverStatus.setText(str_processStatus);
                                                    }

                                                   /* if (salaryAttribute.getProcessStatus().equals(5)) {
                                                        //منتظر ورود
                                                        //txt_status.setText("منتظر ورود");
                                                        img_status.setBackgroundResource(R.drawable.waiting_driver_icon);
                                                    }

                                                    if (salaryAttribute.getProcessStatus().equals(10)) {
                                                        //عدم مراجعه
                                                        //txt_status.setText("عدم مراجعه");
                                                        //img_status.setBackgroundResource(R.drawable.deliverd);
                                                    }

                                                    if (salaryAttribute.getProcessStatus().equals(20)) {
                                                        //فاقد برنامه
                                                        //txt_status.setText("فاقد برنامه");
                                                        img_status.setBackgroundResource(R.drawable.deliverd);
                                                    }

                                                    if (salaryAttribute.getProcessStatus().equals(25)) {
                                                        //عدم ثبت تعرفهه
                                                        //txt_status.setText("عدم ثبت تعرفه");
                                                        img_status.setBackgroundResource(R.drawable.warning_icon);
                                                    }

                                                    if (salaryAttribute.getProcessStatus().equals(0)) {
                                                        //ثبت اولیه
                                                        //txt_status.setText("ثبت اولیه");
                                                        img_status.setBackgroundResource(R.drawable.pending);
                                                    }

                                                    if (salaryAttribute.getProcessStatus().equals(1)) {
                                                        //ثبت اولیه
                                                        //txt_status.setText("تایید");
                                                        img_status.setBackgroundResource(R.drawable.deliverd);
                                                    }*/

                                                    //====getDutyWork=====
                                                    int t = salaryAttribute.getDutyWork();
                                                    int hours = t / 60; //since both are ints, you get an int
                                                    int minutes = t % 60;
                                                    String hoursStr = null;
                                                    String minutesStr = null;

                                                    if (hours < 10) {
                                                        hoursStr = "0" + hours;
                                                    } else {
                                                        hoursStr = String.valueOf(hours);
                                                    }
                                                    if (minutes < 10) {
                                                        minutesStr = "0" + minutes;
                                                    } else {
                                                        minutesStr = String.valueOf(minutes);
                                                    }

                                                    dutyWork.setText(hoursStr + ":" + minutesStr);


                                                    Date startDateTime;
                                                    Date endDateTime;

                                                    try {
                                                        startDateTime = sdf.parse(driverDailyActivity.getStartTime());
                                                        endDateTime = sdf.parse(driverDailyActivity.getEndTime());

                                                        GregorianCalendar cal1 = (GregorianCalendar) GregorianCalendar.getInstance();
                                                        roozh.PersianToGregorian(year, month, day);
                                                        cal1.set(roozh.getYear(), roozh.getMonth(), roozh.getDay());
                                                        cal1.add(Calendar.MONTH, -1);
                                                        Date date = cal1.getTime();

                                                        String dutyStr = "";
                                                        if (salaryAttribute.getProcessStatus() != null && salaryAttribute.getProcessStatus() == 1) {
                                                            dutyStr = " ( " + hoursStr + ":" + minutesStr + " ) ";
                                                        }

                                                        if (Util.compareDates(date, date) == 0) {
                                                            startDate.setText(CalendarUtil.convertPersianDateTime(startDateTime, "HH:mm"));
                                                            endDate.setText(dutyStr + CalendarUtil.convertPersianDateTime(endDateTime, "HH:mm"));
                                                        } else {
                                                            startDate.setText(CalendarUtil.convertPersianDateTime(startDateTime, "yyyy/MM/dd - HH:mm"));
                                                            endDate.setText(CalendarUtil.convertPersianDateTime(endDateTime, "yyyy/MM/dd - HH:mm"));
                                                        }


                                                        if (salaryAttribute.getDriverDailyActivity().getEtCardDataVOListSumCountEt() != null) {
                                                            dailyWorkTypeEn.setText(String.valueOf(salaryAttribute.getDriverDailyActivity().getEtCardDataVOListSumCountEt()));
                                                        }


                                                        //====getFractionPerform=====
                                                        t = salaryAttribute.getFractionPerform();
                                                        hours = t / 60; //since both are ints, you get an int
                                                        minutes = t % 60;
                                                        if (hours < 10) {
                                                            hoursStr = "0" + hours;
                                                        } else {
                                                            hoursStr = String.valueOf(hours);
                                                        }
                                                        if (minutes < 10) {
                                                            minutesStr = "0" + minutes;
                                                        } else {
                                                            minutesStr = String.valueOf(minutes);
                                                        }
                                                        fractionPerform.setText(hoursStr + ":" + minutesStr);

                                                        //====getOvertimeWorkReal=====
                                                        t = salaryAttribute.getOvertimeWorkReal();
                                                        hours = t / 60; //since both are ints, you get an int
                                                        minutes = t % 60;
                                                        if (hours < 10) {
                                                            hoursStr = "0" + hours;
                                                        } else {
                                                            hoursStr = String.valueOf(hours);
                                                        }
                                                        if (minutes < 10) {
                                                            minutesStr = "0" + minutes;
                                                        } else {
                                                            minutesStr = String.valueOf(minutes);
                                                        }
                                                        overtimeWorkReal.setText(hoursStr + ":" + minutesStr);

                                                        //====getHalfPathNoSum=====
                                                        if (driverDailyActivity.getHalfPathNoSum() != null) {
                                                            t = driverDailyActivity.getHalfPathNoSum();
                                                            hours = t / 60; //since both are ints, you get an int
                                                            minutes = t % 60;
                                                            if (hours < 10) {
                                                                hoursStr = "0" + hours;
                                                            } else {
                                                                hoursStr = String.valueOf(hours);
                                                            }
                                                            if (minutes < 10) {
                                                                minutesStr = "0" + minutes;
                                                            } else {
                                                                minutesStr = String.valueOf(minutes);
                                                            }
                                                            timeInLine.setText(hoursStr + ":" + minutesStr);
                                                        }

                                                        if (salaryAttribute.getOvertimeEncouraged() != null) {
                                                            t = salaryAttribute.getOvertimeEncouraged();
                                                            hours = t / 60; //since both are ints, you get an int
                                                            minutes = t % 60;
                                                            if (hours < 10) {
                                                                hoursStr = "0" + hours;
                                                            } else {
                                                                hoursStr = String.valueOf(hours);
                                                            }
                                                            if (minutes < 10) {
                                                                minutesStr = "0" + minutes;
                                                            } else {
                                                                minutesStr = String.valueOf(minutes);
                                                            }
                                                            txt_overTimeEn.setText(hoursStr + ":" + minutesStr);
                                                        }

                                                        if (salaryAttribute.getOvertimeWork() != null) {
                                                            t = salaryAttribute.getOvertimeWork();
                                                            hours = t / 60; //since both are ints, you get an int
                                                            minutes = t % 60;
                                                            if (hours < 10) {
                                                                hoursStr = "0" + hours;
                                                            } else {
                                                                hoursStr = String.valueOf(hours);
                                                            }
                                                            if (minutes < 10) {
                                                                minutesStr = "0" + minutes;
                                                            } else {
                                                                minutesStr = String.valueOf(minutes);
                                                            }
                                                            txt_overTimeWork.setText(hoursStr + ":" + minutesStr);
                                                        }

                                                        halfPathNoGps.setText(salaryAttribute.getHalfPathNo().toString());

                                                        //====getFractionWork=====
                                                        t = salaryAttribute.getFractionWork();
                                                        hours = t / 60; //since both are ints, you get an int
                                                        minutes = t % 60;
                                                        if (hours < 10) {
                                                            hoursStr = "0" + hours;
                                                        } else {
                                                            hoursStr = String.valueOf(hours);
                                                        }
                                                        if (minutes < 10) {
                                                            minutesStr = "0" + minutes;
                                                        } else {
                                                            minutesStr = String.valueOf(minutes);
                                                        }
                                                        fractionWork.setText(hoursStr + ":" + minutesStr);

                                                        t = salaryAttribute.getNightWork();
                                                        hours = t / 60; //since both are ints, you get an int
                                                        minutes = t % 60;
                                                        if (hours < 10) {
                                                            hoursStr = "0" + hours;
                                                        } else {
                                                            hoursStr = String.valueOf(hours);
                                                        }
                                                        if (minutes < 10) {
                                                            minutesStr = "0" + minutes;
                                                        } else {
                                                            minutesStr = String.valueOf(minutes);
                                                        }
                                                        nightWork.setText(hoursStr + ":" + minutesStr);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    showCaseViewFirFirst = DriverApplication.getInstance().getSharedPreferences().getBoolean(Constants.WDF_SHOW_CASE_VIEW_FOR_FIRST, false);
                                                    if (showCaseViewFirFirst) {
                                                        SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
                                                        editor.putBoolean(Constants.WDF_SHOW_CASE_VIEW_FOR_FIRST, false);
                                                        editor.apply();
                                                        backShowcaseView(getActivity(), img_back);
                                                    }

                                                } else {
                                                    showEmptyMessage.setVisibility(VISIBLE);
                                                    main_layout.setVisibility(View.GONE);
                                                    bottom_layout.setVisibility(View.GONE);
                                                }

                                                if (response != null) {
                                                    salaryAttributeId = response.getSalaryAttributeId();
                                                    System.out.println("salaryAttributeId====" + salaryAttributeId);
                                                    if (salaryAttributeId != 0) {
                                                        getCommentsCopy(driver);
                                                    }
                                                }

                                               // etCard();


                                            } else {
                                                showEmptyMessage.setVisibility(VISIBLE);
                                                // persistentbottomSheet.setVisibility(View.GONE);
                                                main_layout.setVisibility(View.GONE);
                                                bottom_layout.setVisibility(View.GONE);
                                            }
                                        }

                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressbar.setVisibility(View.GONE);
                                            img_progress.setVisibility(View.GONE);
                                            main_layout.setVisibility(View.GONE);
                                            bottom_layout.setVisibility(View.GONE);
                                            //persistentbottomSheet.setVisibility(View.GONE);
                                            showEmptyMessage.setVisibility(VISIBLE);

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
                    progressbar.setVisibility(View.GONE);
                    img_progress.setVisibility(View.GONE);
                    main_layout.setVisibility(View.GONE);
                    bottom_layout.setVisibility(View.GONE);
                    showEmptyMessage.setVisibility(VISIBLE);
                    Utils.showErrors(getActivity(), error);

                }
            });
        }

    }

    private void goToMonthlyFragment() {
        Fragment fragment = new MonthlyFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
    }

    public static void backShowcaseView(final Context context, View view) {

        String title = "برای رفتن به قسمت ماهانه ، از این دکمه استفاده کنید";
        String description = "";

        TapTargetView.showFor((Activity) context, TapTarget.forView(view, title, description)
                .cancelable(false)
                .drawShadow(false)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                cardLeftShowcaseView(context);
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    public static void cardLeftShowcaseView(final Context context) {
        String title = "برای رفتن به روزهای قبل و بعد ، از این قسمت استفاده کنید";
        String description = "";

        TapTargetView.showFor((Activity) context, TapTarget.forView(((Activity) context).findViewById(R.id.bottom_layout), title, description)
                .cancelable(false)
                .drawShadow(false)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .targetRadius(200)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                //cardRightShowcaseView(context, title, description);
                calendarShowcaseView(context);
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    public static void cardRightShowcaseView(final Context context, final String title, final String description) {

        TapTargetView.showFor((Activity) context, TapTarget.forView(((Activity) context).findViewById(R.id.card_right), title, description)
                .cancelable(false)
                .drawShadow(false)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);

            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    public static void calendarShowcaseView(final Context context) {
        String title = "برای انتخاب روز مورد نظر ، روی این دکمه ضربه بزنید";
        String description = "";

        Button button = ((Activity) context).findViewById(R.id.calendar_icon);
        TapTargetView.showFor((Activity) context, TapTarget.forView(button, title, description)
                .cancelable(false)
                .drawShadow(false)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                selectMonthShowcaseView(context);
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    public static void selectMonthShowcaseView(Context context) {
        RelativeLayout relativeLayout = ((Activity) context).findViewById(R.id.relativeLayout_selectMonth);
        String title = "دسترسی به قسمت روزانه و ماهانه";
        String description = "";
        TapTargetView.showFor((Activity) context, TapTarget.forView(relativeLayout, title, description)
                .cancelable(false)
                .drawShadow(false)
                .targetRadius(100)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);

            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    public int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    public void showDetailDialog() {
        final Dialog dialog_wait = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog_wait.setContentView(R.layout.custom_dialog_description_list);
        dialog_wait.show();
        dialog_wait.setCancelable(false);
        Window window = dialog_wait.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        LinearLayout layout_add = dialog_wait.findViewById(R.id.layout_add);
        ImageView img_close = dialog_wait.findViewById(R.id.img_close);
        waitProgress = dialog_wait.findViewById(R.id.waitProgress);
        txt_null = dialog_wait.findViewById(R.id.txt_null);
        recycler_view = dialog_wait.findViewById(R.id.recycler_view);

        getComments();

        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salaryAttributeId != 0) {
                    showAddDialog();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_wait.dismiss();
            }
        });

        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recycler_view.setAdapter();
    }

    public void showAddDialog() {
        final Dialog dialog_wait = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog_wait.setContentView(R.layout.show_add_dialog);
        dialog_wait.show();
        dialog_wait.setCancelable(false);
        Window window = dialog_wait.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        final EditText comment = dialog_wait.findViewById(R.id.comment);
        TextView btn_send = dialog_wait.findViewById(R.id.btn_send);
        ImageView img_close = dialog_wait.findViewById(R.id.img_close);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.getText().toString().isEmpty()) {

                    System.out.println("driver====" + driver.getUsername());
                    System.out.println("driver====" + driver.getPassword());
                    System.out.println("salaryAttributeId====" + salaryAttributeId);
                    if (driver == null)
                        return;

                    ServerCoordinator.getInstance().saveDriverSAComment(driver.getUsername(), driver.getPassword(), salaryAttributeId, comment.getText().toString(),
                            new Response.Listener<UpdateVersionResponseBean>() {
                                @Override
                                public void onResponse(UpdateVersionResponseBean response) {
                                    if (response.getsUCCESS() != null) {
                                        Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                                        getComments();
                                        dialog_wait.dismiss();
                                    } else {
                                        Utils.showToast(getActivity(), R.string.error_unknown, false);
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utils.showErrors(getActivity(), error);
                                }
                            });
                } else {
                    Utils.showToast(getActivity(), R.string.label_comment_editText_warning, false);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_wait.dismiss();
            }
        });
    }

    public void getComments() {

        if (waitProgress != null) {
            waitProgress.setVisibility(VISIBLE);
        }

        driverSACommentList = new ArrayList<>();

        if (driver == null)
            return;

        System.out.println("driver.getUsername()" + driver.getUsername());
        System.out.println("driver.getUsername()" + driver.getPassword());

        ServerCoordinator.getInstance().getDriverSACommentList(driver.getUsername(), driver.getPassword(), String.valueOf(salaryAttributeId),
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        if (waitProgress != null) {
                            waitProgress.setVisibility(View.GONE);
                        }

                        driverSACommentList = response.getrESULT().driverSACommentList;
                        if (driverSACommentList != null) {
                            recycler_view.setAdapter(new DescriptionListAdapter(driver, driverSACommentList, getActivity(), WeekDayFragment.this));
                            if (driverSACommentList.size() == 0) {
                                txt_null.setVisibility(VISIBLE);
                                // btnListFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_list));
                                btnListFab.setBackgroundResource(R.drawable.empty_list);
                            } else {
                                txt_null.setVisibility(View.GONE);
                                //  btnListFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.full_list));
                                btnListFab.setBackgroundResource(R.drawable.full_list);
                            }

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (waitProgress != null) {
                            waitProgress.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void getAllDriver() {
        class GetAllDriver extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                driverList = DataBaseClint.getInstance(getActivity()).getAppDataBase().driverDao().getAll();
                if (driverList.size() > 0) {
                    driver = driverList.get(0);
                }
                return null;
            }
        }
        new GetAllDriver().execute();
    }

    public void getCommentsCopy(final Driver driver) {
        driverSACommentList = new ArrayList<>();

        ServerCoordinator.getInstance().getDriverSACommentList(driver.getUsername(), driver.getPassword(), String.valueOf(salaryAttributeId),
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        driverSACommentList = response.getrESULT().driverSACommentList;
                        if (driverSACommentList.size() == 0) {
                            btnListFab.setBackgroundResource(R.drawable.empty_list);
                        } else {
                            btnListFab.setBackgroundResource(R.drawable.full_list);
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    public void etCard() {
        DriverApplication application = (DriverApplication) getActivity().getApplication();
        final Roozh roozh = new Roozh();
        roozh.PersianToGregorian(year, month, day);
        final String fromDate = roozh.getYear() + "-" + roozh.getMonth() + "-" + roozh.getDay();
        DriverProfileResponseBean response = sharedData.getResponse();
        final Driver driver = application.getCurrentUser();
        final String username = driver.getUsername();
        final String password = driver.getPassword();
        if (response != null) {
            if (response.getRESULT().getDriverProfile().getDriverJob().getCar().getId() != null) {
                String driverId = String.valueOf(response.getRESULT().getDriverProfile().getId());
                System.out.println("fromDate====" + fromDate);
                ServerCoordinator.getInstance().getEtCardDataTranSumStatisticallyReportList(username, password, driverId, fromDate,
                        new Response.Listener<ReportListResponseBean>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(ReportListResponseBean response) {
                                List<BarEntry> entries = new ArrayList<BarEntry>();
                                final ArrayList<String> xVals = new ArrayList<>();
                                int count = response.getrESULT().getTimeSeriesList().size();
                                String xval = "";

                                for (int i = 0; i < count; i++) {
                                    int balance = response.getrESULT().getTimeSeriesList().get(i).getCount();
                                    entries.add(new BarEntry(balance, i));
                                    String out = response.getrESULT().getTimeSeriesList().get(i).getDate();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());
                                    try {
                                        Date date = simpleDateFormat.parse(out);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                        xval = String.valueOf(i);
                                        xVals.add(xval);
                                        System.out.println("xval===" + xval);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                BarDataSet bardataset = new BarDataSet(entries, "");
                                chart.animateY(3000);
                                chart.getLegend().setEnabled(false);
                                XAxis xAxis = chart.getXAxis();
                                xAxis.setTypeface(DriverApplication.getInstance().getPersianTypeFace());
                                xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setLabelRotationAngle(-90f);
                                xAxis.setTextSize(14f);
                                xAxis.setDrawGridLines(false);
                                xAxis.setAxisLineColor(getResources().getColor(android.R.color.transparent));
                                xAxis.setSpaceBetweenLabels(1);
                                //-----------------------------
                                YAxis yAxis = chart.getAxisLeft();
                                YAxis rightYAxis = chart.getAxisRight();
                                rightYAxis.setEnabled(false);
                                yAxis.setEnabled(false);
                                yAxis.setTypeface(DriverApplication.getInstance().getPersianTypeFace());
                                yAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
                                yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                                yAxis.setGridColor(getResources().getColor(R.color.colorPrimary));
                                yAxis.setDrawGridLines(false);
                                yAxis.setTextSize(14f);
                                yAxis.setZeroLineColor(getResources().getColor(android.R.color.transparent));
                                bardataset.setColor(getResources().getColor(R.color.light_green));
                                BarData barData = new BarData(xVals, bardataset);
                                barData.setValueFormatter(new MyValueFormatter());
                                barData.setValueTypeface(DriverApplication.getInstance().getPersianTypeFace());
                                barData.setValueTextSize(12f);
                                chart.setDescription("");
                                chart.setData(barData);
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
            }
        }
    }

}

