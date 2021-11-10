package gap.com.driver.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import gap.com.datepicker.DatePicker;
import gap.com.datepicker.interfaces.DateSetListener;
import gap.com.driver.R;
import gap.com.driver.activity.FishActivity;
import gap.com.driver.adapter.DescriptionListAdapter;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.database.DataBaseClint;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.gapcalendar.adapter.CalendarAdapter;
import gap.com.driver.gapcalendar.customweekview.DateConvertor;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.EventBusModel;
import gap.com.driver.model.ReportListResponseBean;
import gap.com.driver.model.SalaryAttributeList;
import gap.com.driver.model.SalaryAttributeResponseBean;
import gap.com.driver.model.ServerDateTimeResponseBean;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.driverprofile.DriverSACommentList;
import gap.com.driver.model.update.UpdateVersionResponseBean;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.Constant;
import gap.com.driver.util.DateUtils;
import gap.com.driver.util.Globals;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.MyValueFormatter;
import gap.com.driver.util.OnSwipeTouchListener;
import gap.com.driver.util.RecyclerItemClickListener;
import gap.com.driver.util.Util;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CircularProgress;
import gap.com.driver.widget.MovableFloatingActionButton;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MonthlyFragment extends Fragment {

    private static final String tag = "WeekViewActivity";
    private int month, year, day = 0;
    private TextView txt_overTimeEn;
    private TextView timeOffDay;
    private TextView dutyWork;
    private TextView overtimeWorkReal;
    private TextView doubleWork;
    private TextView txt_overTimeWork;
    private TextView fractionWork;
    private TextView overtimeWork;
    private TextView nightWork;
    private AppCompatTextView txt_selectedDate;
    private CalendarAdapter calendarAdapter;
    private RecyclerView recyclerView, recycler_view;
    private Fragment fragment;
    private int i = 0;
    private DateConvertor tmpDateConvertor;
    private FragmentManager fragmentManager;
    private int startSelectDate;
    private ArrayList<String> StringArray = new ArrayList<String>();
    private Globals sharedData = Globals.getInstance();
    private PersianDate persianDate;
    private ImageView iv_trigger, img_progress,img_fish,btnListFab,img_graph,img_back;
    private CircularProgress progressbar;
    private String str_overTimeEn, str_timeOffDay, str_dutyWork, str_overtimeWorkReal, str_doubleWork, str_fractionWork, str_overtimeWork, str_nightWork;
    private ArrayList<SalaryAttributeList> arrayList = new ArrayList<>();
    private RelativeLayout relativeLayout,relativeLayout_top;
    private SalaryAttributeResponseBean getSalaryAttributeResponseBean;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout linearLayout,linearLayout_graph;
    private View persistentbottomSheet;
    private boolean isErrorListener;
    private CardView cardView;
    private Driver driver;
    private int salaryAttributeId = 0;
    private List<Driver> driverList;
    private List<DriverSACommentList> driverSACommentList;
    private DriverApplication application;
    private Dialog dialog_wait;
    private TextView txt_null;
    private CircularProgressView waitProgress;
    private float xCoOrdinate, yCoOrdinate;
    private Globals globals = Globals.getInstance();
    private BarChart chart;
    public MonthlyFragment() {

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_home, view, false);
        application = (DriverApplication) getActivity().getApplication();
        startSelectDate = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_START_WEEKDAY, -1);
        progressbar = (CircularProgress) view1.findViewById(R.id.progressbar);
        img_progress = view1.findViewById(R.id.img_progress);
        cardView = view1.findViewById(R.id.cardView);
        relativeLayout_top = view1.findViewById(R.id.relativeLayout_top);
        progressbar.setVisibility(View.GONE);
        img_progress.setVisibility(View.GONE);
        //btnListFab = view1.findViewById(R.id.btnListFab);
        coordinatorLayout = (CoordinatorLayout) view1.findViewById(R.id.coordinator);
        init_persistent_bottomsheet();
        init(view1);
        persianDate = new PersianDate();
        compareTo();
        sharedData.setLeavePage(true);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(cal);
        txt_selectedDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());

        Glide.with(this)
                .load(R.raw.myanimation)
                .into(img_progress);
        getAllDriver();


        /*
         * select custom date in month fragment
         * */
        txt_selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                //maxDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) + 1);
                minDate.set(Calendar.MONTH, minDate.get(Calendar.MONTH) - 2);
                new DatePicker.Builder()
                        .id(1)
                        .minDate(minDate)
                        .maxDate(maxDate)
                        .build(new DateSetListener() {
                            @Override
                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                if (calendar == null)
                                    return;

                                JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                txt_selectedDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                                setDate(calendar);
                            }
                        })
                        .show(fm, "");
            }
        });


        /*
         * for swipe monthly calendar to next or before
         * */
        recyclerView.setOnTouchListener(new OnSwipeTouchListener() {

            @Override
            public boolean onSwipeLeft() {
               /* if (month == (persianDate.getShMonth() - 2) && year == persianDate.getShYear() || month == (persianDate.getShMonth() + 10) && year == persianDate.getShYear() - 1) {
                    return false;
                }*/

                if (month == persianDate.getShMonth() - 6) {
                    return false;
                }

                prevMethod();
                getActivity().overridePendingTransition(R.anim.left_out, R.anim.right_in);
                return true;
            }

            @Override
            public boolean onSwipeRight() {
                if (month == persianDate.getShMonth() && year == persianDate.getShYear()) {
                    return false;
                }

                nextMethod();
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
            }

            @Override
            public boolean onSwipeBottom() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public boolean onSwipeTop() {
                // TODO Auto-generated method stub
                return true;
            }

        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.removeOnScrollListener(this);
            }
        });


        /*
         * click list and get id and pass to day fragment with bundle
         * */
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // TODO Auto-generated method stub
                String id = (String) ((TextView) view.findViewById(R.id.id)).getText();
                System.out.println("Monthly Fragment id====" + id);
                if (!id.trim().isEmpty()) {
                    String txt_day1 = ((TextView) view.findViewById(R.id.calendar_day_gridcell)).getText().toString();
                    String txt_day2 = ((TextView) view.findViewById(R.id.calendar_day_gridcell_1)).getText().toString();
                    String txt_day3 = ((TextView) view.findViewById(R.id.calendar_day_gridcell_2)).getText().toString();
                    String strDate = "";
                    if (!txt_day1.equals("")) {
                        strDate = txt_day1;
                    } else if (!txt_day2.equals("")) {
                        strDate = txt_day2;
                    } else if (!txt_day3.equals("")) {
                        strDate = txt_day3;
                    }


                    if (!strDate.equals("")) {
                        day = Integer.parseInt(strDate);
                        tmpDateConvertor.setGregorianDate(year, month, day);

                        sharedData.setYear(year);
                        sharedData.setMonth(month);
                        sharedData.setDay(day);

                        sharedData.setGridOnClick(Constant.ACTION_GRID_ONCLICK);
                        sharedData.setSelectedMenuItem(Constant.ACTION_DAY);
                        //sharedData.setSalaryListItemId(id);
                    }

                    fragment = new WeekDayFragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    Bundle args = new Bundle();
                    args.putParcelable("getSalaryAttributeResponseBean", getSalaryAttributeResponseBean);
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    Util.recognizeSelectedItems(Constant.RECOGNIZE, "WeekDayFragment");
                    EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));

                    sharedData.setCurrentDate(true);
                    sharedData.setCurrentDay(day);

                } else {
                    Utils.showToast(getActivity(), R.string.message_toast_recyclerView_click, false);
                }

            }
        }));


        btnListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog();
            }
        });

        img_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(GONE);
                linearLayout_graph.setVisibility(View.VISIBLE);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout_graph.setVisibility(GONE);
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

        return view1;
    }


    /*
     *
     *init widget
     * */
    private void init(View view1) {
        DateConvertor dateConvertor = new DateConvertor();
        recyclerView = view1.findViewById(R.id.recyclerView);
        TextView txt_p1 = view1.findViewById(R.id.txt_p1);
        TextView txt_p2 = view1.findViewById(R.id.txt_p2);
        TextView txt_p3 = view1.findViewById(R.id.txt_p3);
        TextView txt_p4 = view1.findViewById(R.id.txt_p4);
        TextView txt_p5 = view1.findViewById(R.id.txt_p5);
        TextView txt_p6 = view1.findViewById(R.id.txt_p6);
        TextView txt_p7 = view1.findViewById(R.id.txt_p7);
        txt_selectedDate = view1.findViewById(R.id.txt_selectedDate);
        txt_overTimeEn = view1.findViewById(R.id.txt_overTimeEn);
        timeOffDay = view1.findViewById(R.id.txt_timeOffDay);
        dutyWork = view1.findViewById(R.id.txt_dutyWork);
        overtimeWorkReal = view1.findViewById(R.id.txt_overtimeWorkReal);
        doubleWork = view1.findViewById(R.id.txt_doubleWork);
        txt_overTimeWork = view1.findViewById(R.id.txt_overTimeWork);
        img_fish = view1.findViewById(R.id.img_fish);
        fractionWork = view1.findViewById(R.id.txt_fractionWork);
        overtimeWork = view1.findViewById(R.id.txt_overtimeWork);
        nightWork = view1.findViewById(R.id.txt_nightWork);
        img_graph = view1.findViewById(R.id.img_graph);
        img_back = view1.findViewById(R.id.img_back);

        ArrayValueAddFunction();
        txt_p1.append(StringArray.get(0));
        txt_p2.append(StringArray.get(1));
        txt_p3.append(StringArray.get(2));
        txt_p4.append(StringArray.get(3));
        txt_p5.append(StringArray.get(4));
        txt_p6.append(StringArray.get(5));
        txt_p7.append(StringArray.get(6));

        tmpDateConvertor = new DateConvertor();
        if (sharedData.getYear() != 0 && sharedData.getMonth() != 0 && sharedData.getDay() != 0) {
            this.year = sharedData.getYear();
            this.month = sharedData.getMonth();
            this.day = sharedData.getDay();

        } else {
            this.year = dateConvertor.getIranianYear();
            this.month = dateConvertor.getIranianMonth();
            this.day = dateConvertor.getIranianDay();

            sharedData.setYear(this.year);
            sharedData.setMonth(this.month);
            sharedData.setDay(this.day);
        }

        recyclerView.setHasFixedSize(true);
        //gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));


        if (Utils.isConnected(getActivity())) {
            isDeviceDateTimeValid(this.year, this.month, 1);
        } else {
            Utils.showToast(getActivity(), R.string.error_noConnection, false);
        }

        img_fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FishActivity.class));
            }
        });

        calendarAdapter = new CalendarAdapter(getActivity(), this.year, this.month, this.day, arrayList);
        recyclerView.setAdapter(calendarAdapter);
    }

    /*
     * for select start from which day of month
     * */
    private void ArrayValueAddFunction() {

        switch (startSelectDate) {
            case 1:
                StringArray.add(getResources().getString(R.string.label_persian_p1));
                StringArray.add(getResources().getString(R.string.label_persian_p2));
                StringArray.add(getResources().getString(R.string.label_persian_p3));
                StringArray.add(getResources().getString(R.string.label_persian_p4));
                StringArray.add(getResources().getString(R.string.label_persian_p5));
                StringArray.add(getResources().getString(R.string.label_persian_p6));
                StringArray.add(getResources().getString(R.string.label_persian_p7));
                break;
            case 2:
                StringArray.add(getResources().getString(R.string.label_persian_p2));
                StringArray.add(getResources().getString(R.string.label_persian_p3));
                StringArray.add(getResources().getString(R.string.label_persian_p4));
                StringArray.add(getResources().getString(R.string.label_persian_p5));
                StringArray.add(getResources().getString(R.string.label_persian_p6));
                StringArray.add(getResources().getString(R.string.label_persian_p7));
                StringArray.add(getResources().getString(R.string.label_persian_p1));
                break;
            case 3:
                StringArray.add(getResources().getString(R.string.label_persian_p3));
                StringArray.add(getResources().getString(R.string.label_persian_p4));
                StringArray.add(getResources().getString(R.string.label_persian_p5));
                StringArray.add(getResources().getString(R.string.label_persian_p6));
                StringArray.add(getResources().getString(R.string.label_persian_p7));
                StringArray.add(getResources().getString(R.string.label_persian_p1));
                StringArray.add(getResources().getString(R.string.label_persian_p2));
                break;

            default:
                StringArray.add(getResources().getString(R.string.label_persian_p1));
                StringArray.add(getResources().getString(R.string.label_persian_p2));
                StringArray.add(getResources().getString(R.string.label_persian_p3));
                StringArray.add(getResources().getString(R.string.label_persian_p4));
                StringArray.add(getResources().getString(R.string.label_persian_p5));
                StringArray.add(getResources().getString(R.string.label_persian_p6));
                StringArray.add(getResources().getString(R.string.label_persian_p7));
        }
    }


    /*
     * set and init adapter for list view in month calendar
     * */
    private void setGridCellAdapterToDate() {

        sharedData.setYear(this.year);
        sharedData.setMonth(this.month);
        sharedData.setDay(this.day);

        recyclerView.setHasFixedSize(true);
        //gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        calendarAdapter = new CalendarAdapter(getActivity(), this.year, this.month, this.day, arrayList);
        recyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /*
     * goto next month
     * */
    @SuppressLint("SetTextI18n")
    public void nextMethod() {
        // TODO Auto-generated method stub
        if (isErrorListener) {
            Utils.showToast(getActivity(), "isErrorListener", false);
        } else {

            if (this.month > 11) {
                this.month = 1;
                this.year++;
            } else {
                this.month++;
            }

            Log.d(tag, "Setting Next Month in GridCellAdapter: "
                    + "Month: " + this.month + " Year: " + this.year);

            System.out.println("Setting Prev Month in GridCellAdapterout: " + "Month: " + this.month + " Year: " + this.year);
            //setGridCellAdapterToDate(this.month, year);
            compareTo();
        }

        if (Utils.isConnected(getActivity())) {
            isDeviceDateTimeValid(this.year, this.month, 1);
        } else {
            Utils.showToast(getActivity(), R.string.error_noConnection, false);
        }
    }


    /*
     * go to prev month
     * */
    @SuppressLint("SetTextI18n")
    public void prevMethod() {
        // TODO Auto-generated method stub

        if (isErrorListener) {
            Utils.showToast(getActivity(), "isErrorListener", false);
        } else {

            if (this.month <= 1) {
                this.month = 12;
                this.year--;
            } else {
                this.month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: "
                    + "Month: " + this.month + " Year: " + this.year);

            System.out.println("Setting Prev Month in GridCellAdapterout: " + "Month: " + this.month + " Year: " + this.year);
            //setGridCellAdapterToDate(this.month, this.year);
            compareTo();
        }

        if (Utils.isConnected(getActivity())) {
            isDeviceDateTimeValid(this.year, this.month, 1);
        } else {
            Utils.showToast(getActivity(), R.string.error_noConnection, false);
        }


    }


    /*
     * for recognize month such as befor and next on current month
     * */
    private void compareTo() {
        TextView showDateName = ((Activity) getContext()).findViewById(R.id.showDateName_txt);
        showDateName.setText("");

        if (month == (persianDate.getShMonth() - 1) && year == persianDate.getShYear() || month == (persianDate.getShMonth() + 11) && year == persianDate.getShYear() - 1) {

            showDateName.setText("ماه قبل");

        } else if (month == (persianDate.getShMonth() + 1) && year == persianDate.getShYear()) {
            showDateName.setText("ماه بعد");

        } else if (month == persianDate.getShMonth() && year == persianDate.getShYear()) {
            showDateName.setText("ماه جاری");

        }
    }


    /*
     * for navigation button in bottom page monthly
     * */
    public void init_persistent_bottomsheet() {
        persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
        iv_trigger = (ImageView) persistentbottomSheet.findViewById(R.id.iv_fab);
        relativeLayout = (RelativeLayout) persistentbottomSheet.findViewById(R.id.relativeLayout);
        btnListFab = persistentbottomSheet.findViewById(R.id.btnListFab);
        linearLayout = (LinearLayout) persistentbottomSheet.findViewById(R.id.linearLayout);
        linearLayout_graph = (LinearLayout) persistentbottomSheet.findViewById(R.id.linearLayout_graph);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(persistentbottomSheet);
        chart = persistentbottomSheet.findViewById(R.id.chart);
        persistentbottomSheet.setVisibility(View.GONE);


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //iv_trigger.setBackgroundResource(R.drawable.negative_icon);
                    linearLayout.setBackgroundResource(R.color.glass_white);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    // iv_trigger.setBackgroundResource(R.drawable.plus_icon);
                    //linearLayout.setBackgroundResource(0);
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
    }


    /*
     * get invalid date time and call monthly salary attribute with type 1
     * */
    private void isDeviceDateTimeValid(final int year, final int month, int day) {
        final Driver driver = application.getCurrentUser();
        persistentbottomSheet.setVisibility(View.GONE);
        if (driver != null) {
            final String username = driver.getUsername();
            final String password = driver.getPassword();
            final int type = 1;
            final String fromDate = year + "-" + month + "-" + day;

            //final String toDate = "";

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            progressbar.setVisibility(View.VISIBLE);
            img_progress.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            ServerCoordinator.getInstance().getServerDateTime(new Response.Listener<ServerDateTimeResponseBean>() {
                @Override
                public void onResponse(ServerDateTimeResponseBean response) {
                    progressbar.setVisibility(View.GONE);
                    img_progress.setVisibility(View.GONE);
                    try {
                        Date serverDateTime = simpleDateFormat.parse(response.getRESULT().getServerDateTime());
                        if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                            progressbar.setVisibility(View.VISIBLE);
                            img_progress.setVisibility(View.VISIBLE);
                            ServerCoordinator.getInstance().getDriverSalaryAttribute(username, password, type, fromDate,
                                    new Response.Listener<SalaryAttributeResponseBean>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onResponse(SalaryAttributeResponseBean response) {
                                            progressbar.setVisibility(View.GONE);
                                            img_progress.setVisibility(View.GONE);
                                            cardView.setVisibility(View.VISIBLE);
                                            persistentbottomSheet.setVisibility(View.VISIBLE);
                                            isErrorListener = false;

                                            sharedData.setSalaryMonthly(response);

                                            btnListFab.setVisibility(View.VISIBLE);
                                            getSalaryAttributeResponseBean = response;

                                            str_timeOffDay = String.valueOf(response.getSalaryAttribute().getTimeOffDay());

                                            System.out.println("getId=====" + response.getSalaryAttribute().getId());
                                            if (response.getSalaryAttribute().getId() != null) {
                                                salaryAttributeId = response.getSalaryAttribute().getId();
                                                if (salaryAttributeId != 0){
                                                    getCommentsCopy(driver);
                                                }
                                            }

                                          //  etCard();


                                            if (response.getProcessStatus() != 0) {
                                                img_fish.setVisibility(View.VISIBLE);
                                            } else {
                                                img_fish.setVisibility(View.GONE);
                                            }


                                            if (response.getSalaryAttribute().getDutyWork() != null) {
                                                int t = response.getSalaryAttribute().getDutyWork();
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
                                                str_dutyWork = hoursStr + ":" + minutesStr;
                                            }

                                            if (response.getSalaryAttribute().getOvertimeEncouraged() != null) {
                                                int t = response.getSalaryAttribute().getOvertimeEncouraged();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_overTimeEn = hoursStr + ":" + minutesStr;
                                            }

                                            if (response.getSalaryAttribute().getOvertimeWorkReal() != null) {
                                                int t = response.getSalaryAttribute().getOvertimeWorkReal();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_overtimeWorkReal = hoursStr + ":" + minutesStr;
                                            }

                                            if (response.getSalaryAttribute().getDutyWork() != null) {
                                                int t = response.getSalaryAttribute().getDutyWork();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_doubleWork = hoursStr + ":" + minutesStr;
                                            }


                                            if (response.getSalaryAttribute().getFractionWork() != null) {
                                                int t = response.getSalaryAttribute().getFractionWork();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_fractionWork = hoursStr + ":" + minutesStr;
                                            }

                                            if (response.getSalaryAttribute().getOvertimeWork() != null) {
                                                int t = response.getSalaryAttribute().getOvertimeWork();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_overtimeWork = hoursStr + ":" + minutesStr;
                                            }

                                            if (response.getSalaryAttribute().getNightWork() != null) {
                                                int t = response.getSalaryAttribute().getNightWork();
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
                                                //dutyWork.setText(hours + ":" + minutes);
                                                str_nightWork = hoursStr + ":" + minutesStr;
                                            }


                                            /*if (response.getSalaryAttribute().getId() == null) {
                                                persistentbottomSheet.setVisibility(View.GONE);
                                            } else {
                                                persistentbottomSheet.setVisibility(View.VISIBLE);
                                            }*/

                                            txt_overTimeEn.setText(str_overTimeEn);
                                            timeOffDay.setText(str_timeOffDay + " : ");
                                            dutyWork.setText(str_dutyWork + " : ");
                                            overtimeWorkReal.setText(str_overtimeWorkReal + " : ");
                                            doubleWork.setText(str_doubleWork + " : ");
                                            txt_overTimeWork.setText(str_overtimeWork);
                                            fractionWork.setText(str_fractionWork + " : ");
                                            overtimeWork.setText(str_overtimeWork + " : ");
                                            nightWork.setText(str_nightWork + " : ");
                                            arrayList = (ArrayList<SalaryAttributeList>) response.getSalaryAttribute().getSalaryAttributeList();
                                            setGridCellAdapterToDate();

                                            List<BarEntry> entries = new ArrayList<BarEntry>();
                                            final ArrayList<String> xVals = new ArrayList<>();
                                            String xval = "";
                                            for (int i = 0; i < arrayList.size(); i++) {
                                                int balance = response.getSalaryAttribute().getSalaryAttributeList().get(i).getEtCardNo();
                                                entries.add(new BarEntry(balance, i));
                                                String out = response.getSalaryAttribute().getSalaryAttributeList().get(i).getDate();
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());

                                                try {
                                                    Date date = simpleDateFormat.parse(out);
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTime(date);
                                                    JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                    xval = String.valueOf(i+1);
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
                                            xAxis.setTextColor(application.getResources().getColor(R.color.colorPrimary));
                                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                            xAxis.setLabelRotationAngle(-90f);
                                            xAxis.setTextSize(14f);
                                            xAxis.setDrawGridLines(false);
                                            xAxis.setAxisLineColor(application.getResources().getColor(android.R.color.transparent));
                                            xAxis.setSpaceBetweenLabels(1);
                                            //-----------------------------
                                            YAxis yAxis = chart.getAxisLeft();
                                            YAxis rightYAxis = chart.getAxisRight();
                                            rightYAxis.setEnabled(false);
                                            yAxis.setEnabled(false);
                                            yAxis.setTypeface(DriverApplication.getInstance().getPersianTypeFace());
                                            yAxis.setTextColor(application.getResources().getColor(R.color.colorPrimary));
                                            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                                            yAxis.setGridColor(application.getResources().getColor(R.color.colorPrimary));
                                            yAxis.setDrawGridLines(false);
                                            yAxis.setTextSize(14f);
                                            yAxis.setZeroLineColor(application.getResources().getColor(android.R.color.transparent));
                                            bardataset.setColor(application.getResources().getColor(R.color.light_green));
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
                                            persistentbottomSheet.setVisibility(View.GONE);
                                            progressbar.setVisibility(View.GONE);
                                            img_progress.setVisibility(View.GONE);
                                            Utils.showErrors(getActivity(), error);
                                            isErrorListener = true;
                                        }
                                    });

                        } else {
                            Utils.showToast(getActivity(), R.string.message_toast_invalidDateTime, false);
                            progressbar.setVisibility(View.GONE);
                            img_progress.setVisibility(View.GONE);
                            isErrorListener = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showErrors(getActivity(), error);
                    progressbar.setVisibility(View.GONE);
                    img_progress.setVisibility(View.GONE);
                    isErrorListener = true;

                }
            });

        }


    }

    /*
     * set date for get calendar value from date piker chooser
     * */

    private void setDate(final Calendar calendar) {
        if (calendar == null)
            return;

        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
        this.year = jalaliCalendarUtil.getIranianYear();
        this.month = jalaliCalendarUtil.getIranianMonth();
        this.day = jalaliCalendarUtil.getIranianDay();

        sharedData.setYear(this.year);
        sharedData.setMonth(this.month);
        sharedData.setDay(this.day);
        sharedData.setSelectedMenuItem(Constant.ACTION_MONTh);


        if (Utils.isConnected(getActivity())) {
            System.out.println("======isDeviceDateTimeValid4=====");
            isDeviceDateTimeValid(this.year, this.month, 1);
        } else {
            Utils.showToast(getActivity(), R.string.error_noConnection, false);
        }

        //calendarAdapter = new CalendarAdapter(getActivity(), jalaliCalendarUtil.getIranianYear(),jalaliCalendarUtil.getIranianMonth(), jalaliCalendarUtil.getIranianDay(), arrayList);
        //recyclerView.setAdapter(calendarAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        //sharedData.setMonthlyPage(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //sharedData.setMonthlyPage(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        //sharedData.setMonthlyPage(false);
    }

    public void showDetailDialog() {
        dialog_wait = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog_wait.setContentView(R.layout.custom_dialog_description_list);
        dialog_wait.show();
        dialog_wait.setCancelable(false);
        Window window = dialog_wait.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        LinearLayout layout_add = dialog_wait.findViewById(R.id.layout_add);
        txt_null = dialog_wait.findViewById(R.id.txt_null);
        waitProgress = dialog_wait.findViewById(R.id.waitProgress);
        RelativeLayout layout_close = dialog_wait.findViewById(R.id.layout_close);
        ImageView img_close = dialog_wait.findViewById(R.id.img_close);

        getComments(driver);

        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialog_wait.dismiss();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_wait.dismiss();
            }
        });

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
        RelativeLayout layout_close = dialog_wait.findViewById(R.id.layout_close);
        waitProgress = dialog_wait.findViewById(R.id.waitProgress);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.getText().toString().isEmpty()) {
                    waitProgress.setVisibility(View.VISIBLE);
                    System.out.println("driver====" + driver.getUsername());
                    System.out.println("driver====" + driver.getPassword());

                    System.out.println("salaryAttributeId====" + salaryAttributeId);
                    ServerCoordinator.getInstance().saveDriverSAComment(driver.getUsername(), driver.getPassword(), salaryAttributeId, comment.getText().toString(),
                            new Response.Listener<UpdateVersionResponseBean>() {
                                @Override
                                public void onResponse(UpdateVersionResponseBean response) {
                                    waitProgress.setVisibility(View.GONE);
                                    if (response.getsUCCESS() != null) {
                                        Utils.showToast(getActivity(), R.string.success_comment_sent, false);
                                        getComments(driver);
                                        dialog_wait.dismiss();
                                    } else {
                                        Utils.showToast(getActivity(), R.string.error_unknown, false);
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utils.showErrors(getActivity(), error);
                                    waitProgress.setVisibility(View.GONE);
                                }
                            });
                } else {
                    Utils.showToast(getActivity(), R.string.label_comment_editText_warning, false);
                }
            }
        });

        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_wait.dismiss();
                System.out.println("====layout_close====");
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

    public void getComments(final Driver driver) {
        final RecyclerView recycler_view = dialog_wait.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(mLayoutManager);
        waitProgress.setVisibility(View.VISIBLE);
        driverSACommentList = new ArrayList<>();

        ServerCoordinator.getInstance().getDriverSACommentList(driver.getUsername(), driver.getPassword(), String.valueOf(salaryAttributeId),
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        waitProgress.setVisibility(View.GONE);

                        driverSACommentList = response.getrESULT().driverSACommentList;
                        if (driverSACommentList != null) {
                            recycler_view.setAdapter(new DescriptionListAdapter(driver, driverSACommentList, getActivity(), MonthlyFragment.this));
                            if (driverSACommentList.size() == 0) {
                                txt_null.setVisibility(View.VISIBLE);
                                btnListFab.setBackgroundResource(R.drawable.empty_list);
                            } else {
                                btnListFab.setBackgroundResource(R.drawable.full_list);
                                txt_null.setVisibility(View.GONE);
                            }

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showErrors(getActivity(), error);
                        waitProgress.setVisibility(View.GONE);
                    }
                });
    }

    public void getCommentsCopy(final Driver driver) {
        driverSACommentList = new ArrayList<>();

        System.out.println("salaryAttributeId=-=-=-=-" + driver.getUsername());
        System.out.println("salaryAttributeId=-=-=-=-" + driver.getPassword());
        System.out.println("salaryAttributeId=-=-=-=-" + salaryAttributeId);

        ServerCoordinator.getInstance().getDriverSACommentList(driver.getUsername(), driver.getPassword(), String.valueOf(salaryAttributeId),
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {

                        driverSACommentList = response.getrESULT().driverSACommentList;
                        System.out.println("driverSACommentList=-=-=-=-" + driverSACommentList.size());
                        if (driverSACommentList != null) {
                            if (driverSACommentList.size() == 0) {
                                btnListFab.setBackgroundResource(R.drawable.empty_list);
                              //  btnListFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_list));
                            } else {
                               // btnListFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.full_list));
                                btnListFab.setBackgroundResource(R.drawable.full_list);
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showErrors(getActivity(), error);
                    }
                });
    }


}
