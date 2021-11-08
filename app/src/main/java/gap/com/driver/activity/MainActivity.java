package gap.com.driver.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gap.com.datepicker.DatePicker;
import gap.com.datepicker.interfaces.DateSetListener;
import gap.com.driver.BuildConfig;
import gap.com.driver.R;
import gap.com.driver.adapter.DrawerItemCustomAdapter;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.fragment.AboutFragment;
import gap.com.driver.fragment.CommentFragment;
import gap.com.driver.fragment.ComplaintFragment;
import gap.com.driver.fragment.ListLeaveFragment;
import gap.com.driver.fragment.ListReportFragment;
import gap.com.driver.fragment.MonthlyFragment;
import gap.com.driver.fragment.PrivacyFragment;
import gap.com.driver.fragment.ProfileFragment;
import gap.com.driver.fragment.SettingsFragment;
import gap.com.driver.fragment.WeekDayFragment;
import gap.com.driver.gapcalendar.customweekview.DateConvertor;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.model.DataModel;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.EventBusModel;
import gap.com.driver.util.Constant;
import gap.com.driver.util.Globals;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.Util;

import static gap.com.driver.R.id.content_frame;

//import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private DateConvertor dateConvertor, tmpDateConvertor;
    private Fragment current;
    private RelativeLayout relativeLayout;
    private RelativeLayout calendarIconLayout;
    //private String recognize;
    private TextView spinner_txt, txt_version;
    private ImageView menuIcon;
    private CircleImageView img_profile;
    private Button calendarIcon;
    private Globals sharedData = Globals.getInstance();
    private PersianDate persianDate;
    private DriverApplication application;
    private Driver driver;
    private FragmentManager fragmentManager;
    private boolean stop = false;
    private String currentPageName = "";
    private List<Driver> driverList;
    private boolean loginIs = false;
    private String token;
    boolean doubleBackToExitPressedOnce = false;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int activePass = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_CONFIRM_LOCALPASS, 0);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        init();
        sharedData.setSelectedMenuItem(Constant.ACTION_MONTh);
        closeDrawer();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(cal);
        spinner_txt.setText(jalaliCalendarUtil.getIranianDate3());


        //String udata = Util.faToEn(Util.Shamsi_Date());
        //SpannableString content = new SpannableString(udata);
        //content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
        //spinner_txt.setText(content);
        calendarIcon.setText(Util.faToEn(Util.Shamsi_Date()));


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction(new ProfileFragment(), "ProfileFragment");
                EventBus.getDefault().post(new EventBusModel("ProfileFragment"));
                closeDrawer();
            }
        });

        findViewById(R.id.profileLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction(new ProfileFragment(), "ProfileFragment");
                EventBus.getDefault().post(new EventBusModel("ProfileFragment"));
                closeDrawer();
            }
        });


        ////******relativeLayout for pop up menu*******////
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, relativeLayout);
                //popup.setGravity(Gravity.RIGHT);
                callMenu(popup);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_today:
                                sharedData.setSelectedMenuItem(Constant.ACTION_DAY);
                                sharedData.setGridOnClick(Constant.ACTION_DAY);
                                fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
                                EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));

                                break;
                            case R.id.action_week:
                  /*              sharedData.setSelectedMenuItem(Constant.ACTION_WEEK);
                                sharedData.setGridOnClick(Constant.ACTION_WEEK);
                                fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
                                EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));*/
                                break;

                            case R.id.action_month:
                                sharedData.setSelectedMenuItem(Constant.ACTION_MONTh);
                                sharedData.setGridOnClick(Constant.ACTION_MONTh);
                                fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
                                EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
                                break;
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });


        ////******calendar Icon toolbar click*******////
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, calendarIconLayout);
                popup.getMenu().add(1, R.id.action_today, 1, "امروز                   ");
                popup.getMenu().add(1, R.id.action_custom, 2, "برو به                   ");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_today:
                                sharedData.setCurrentDay(dateConvertor.getIranianDay());
                                sharedData.setYear(dateConvertor.getIranianYear());
                                sharedData.setMonth(dateConvertor.getIranianMonth());
                                sharedData.setDay(dateConvertor.getIranianDay());

                                sharedData.setSelectedMenuItem(Constant.ACTION_DAY);
                                fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
                                EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));

                               /* if (sharedData.getSelectedMenuItem() != null) {
                                    if (sharedData.getSelectedMenuItem().equals(Constant.ACTION_DAY)) {
                                        sharedData.setSelectedMenuItem(Constant.ACTION_DAY);
                                        fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
                                        EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));

                                    } else if (sharedData.getSelectedMenuItem().equals(Constant.ACTION_WEEK)) {
                                       // sharedData.setSelectedMenuItem(Constant.ACTION_WEEK);
                                        //fragmentTransaction(new


                                        (), "WeekDayFragment");

                                    } else if (sharedData.getSelectedMenuItem().equals(Constant.ACTION_MONTh)) {
                                        sharedData.setSelectedMenuItem(Constant.ACTION_MONTh);
                                        fragmentTransaction(new MonthlyFragment(), "WeekDayFragment");
                                        EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
                                    }
                                }*/

                                break;

                            case R.id.action_custom:
                                sharedData.setMonthlyPage(true);
                                sharedData.setLeavePage(false);
                                //Util.recognizeSelectedItems(Constant.RECOGNIZE, "MonthlyFragment");
                                Calendar minDate = Calendar.getInstance();
                                Calendar maxDate = Calendar.getInstance();
                                minDate.set(Calendar.MONTH, minDate.get(Calendar.MONTH) - 6);
                                //maxDate.set(Calendar.MONTH, maxDate.get(Calendar.MONTH) + 1);

                                new DatePicker.Builder()
                                        .id(1)
                                        .minDate(minDate)
                                        .maxDate(maxDate)
                                        .build(new DateSetListener() {
                                            @Override
                                            public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                                                setDate(calendar);
                                            }
                                        })
                                        .show(getSupportFragmentManager(), "");
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        //**************************

        ////******custom slide menu*******////

        final DataModel[] drawerItem = new DataModel[6];
        drawerItem[0] = new DataModel(R.drawable.calendar_menu_icon, "مــاهــانه");
        drawerItem[1] = new DataModel(R.drawable.report_icon, "پیشنهادات");
        drawerItem[2] = new DataModel(R.drawable.comllaints, "شکایات");
        drawerItem[3] = new DataModel(R.drawable.leave_record, "درخواست مرخصی");
        drawerItem[4] = new DataModel(R.drawable.setting_icon, "تـنظیمـات");
        drawerItem[5] = new DataModel(R.drawable.about_us, "درباره مــا");

        //drawerItem[2] = new DataModel(R.drawable.report_icon, "ارسال گزارش");
        // drawerItem[3] = new DataModel(R.drawable.leave_icon, "درخواست مرخصی");
        // drawerItem[4] = new DataModel(R.drawable.news_icon, "اخبار و اعلانات");
        //drawerItem[5] = new DataModel(R.drawable.settings_icon, "تنظیمات");


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ((DrawerLayout) findViewById(R.id.drawer_layout)).addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Whatever you want
                //Toast.makeText(getApplicationContext(),"onDrawerSlide",Toast.LENGTH_LONG).show();

                if (currentPageName.equals("MonthlyFragment")){
                    mDrawerList.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (currentPageName.equals("CommentFragment")){
                    mDrawerList.getChildAt(1).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(1).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (currentPageName.equals("ComplaintFragment")){
                    mDrawerList.getChildAt(2).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(2).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (currentPageName.equals("ListLeaveFragment")){
                    mDrawerList.getChildAt(3).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(3).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (currentPageName.equals("SettingsFragment")){
                    mDrawerList.getChildAt(4).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(4).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (currentPageName.equals("AboutFragment")){
                    mDrawerList.getChildAt(5).setBackgroundColor(getResources().getColor(R.color.lightgray03));
                }else {
                    mDrawerList.getChildAt(5).setBackgroundColor(getResources().getColor(R.color.transparent));
                }

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Whatever you want
                menuIcon.setBackgroundResource(R.mipmap.close_menu);
                getInfo();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Whatever you want
                menuIcon.setBackgroundResource(R.mipmap.menu_icon);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Whatever you want
            }
        });


        // mDrawerLayout.setDrawerListener(mDrawerToggle);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean loginIs = bundle.getBoolean("loginIs");
            boolean firstLogin = bundle.getBoolean("firstLogin");

            if (firstLogin) {
                fragmentTransaction(new PrivacyFragment(), "PrivacyFragment");
                closeDrawer();
                return;
            }

        }

        if (activePass == 1) {
            if (bundle != null) {
                boolean loginIs = bundle.getBoolean("loginIs");
                if (loginIs) {
                    //selectItem(0);
                    fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
                    EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
                    closeDrawer();
                    return;
                }
            }
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // selectItem(0);
            fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
            EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
            closeDrawer();
        }
    }

    private void getJsonData() {

    }


    ////******pop up menu*******////
    private void callMenu(PopupMenu popup) {
        persianDate = new PersianDate();
        String dayNam = persianDate.dayName(sharedData.getYear(), sharedData.getMonth(), sharedData.getDay());
        String monthName = persianDate.monthName(sharedData.getMonth());
        String daily = dayNam + " " + sharedData.getDay() + " " + monthName;

        String weekly = sharedData.getWeekMenuDate();
        String monthly = String.valueOf(sharedData.getMonth());


        StringBuffer sbSpace = new StringBuffer();
        for (int i = 0; i <= daily.length(); i++) {
            sbSpace.append(" ");
        }

        String actionDay = "روزانه" + "         " + daily;
        // String actionWeek = "هفتگی" + "               " + Util.getWeek();
        String actionMonth = "ماهانه" + "        " + sbSpace + monthName;
        popup.getMenu().add(1, R.id.action_today, 1, Util.faToEn(actionDay));
        //popup.getMenu().add(1, R.id.action_week, 2, Util.faToEn(actionWeek));
        popup.getMenu().add(1, R.id.action_month, 3, Util.faToEn(actionMonth));
    }

  /*  public String getWeek() {

        int year = sharedData.getYear();
        int month = sharedData.getMonth();
        int day = sharedData.getDay();
        roozh.PersianToGregorian(year, month, day);
        calendar.set(roozh.getYear(), roozh.getMonth() - 1, roozh.getDay());
        NextPreWeekday = getWeekDay();
        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);
        TextView textViewDate = findViewById(R.id.textViewDate);
        return firstDayOfWeek + " " + CommonMethod.convertWeekDaysMouth(NextPreWeekday[0]) +
                "-" + lastDayOfWeek + " " + CommonMethod.convertWeekDaysMouth(NextPreWeekday[6]);}*/

/*    public String[] getWeekDay() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];

        selectStartWeek = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_START_WEEKDAY, -1);

        switch (selectStartWeek) {
            case 1:
                delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK);
                break;
            case 2:
                delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK) + 1;
                break;
            case 3:
                delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK) + 2;
                break;

            default:
                delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK);
        }
        //calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }
        return days;
    }*/



    /* init
     * */

    private void init() {
        String[] mNavigationDrawerItemTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        RelativeLayout relativeLayoutDrawer = findViewById(R.id.RelativeLayout_drawer);
        mDrawerList = findViewById(R.id.left_drawer);
        menuIcon = findViewById(R.id.menuIcon_button);
        img_profile = findViewById(R.id.img_profile);
        calendarIcon = findViewById(R.id.calendar_icon);
        //spinner = (Spinner) findViewById(R.id.spinner);
        LinearLayout rLayout = findViewById(R.id.text);
        relativeLayout = findViewById(R.id.relativeLayout_selectMonth);
        calendarIconLayout = findViewById(R.id.calendarIconLayout);
        spinner_txt = findViewById(R.id.spinner_txt);
        txt_version = findViewById(R.id.txt_version);
        TextView showDateName = findViewById(R.id.showDateName_txt);
        dateConvertor = new DateConvertor();
        tmpDateConvertor = new DateConvertor();
        txt_version.setText(" version " + BuildConfig.VERSION_NAME);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            //EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
        }
    }


    /*
     * select item for toolbar menu
     * */
    private void selectItem(int position) {

        //Spinner spinner;
        Fragment fragment = null;

        switch (position) {

            case 0:
                fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
                EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
                // Util.recognizeSelectedItems(Constant.RECOGNIZE, "ProfileFragment");
                break;

            case 1:
                fragmentTransaction(new CommentFragment(), "CommentFragment");
                EventBus.getDefault().post(new EventBusModel("CommentFragment"));
                break;

            case 2:
                fragmentTransaction(new ComplaintFragment(), "ComplaintFragment");
                EventBus.getDefault().post(new EventBusModel("ComplaintFragment"));
                break;

            case 3:
                fragmentTransaction(new ListLeaveFragment(), "ListLeaveFragment");
                EventBus.getDefault().post(new EventBusModel("ListLeaveFragment"));
                break;


            case 4:
                fragmentTransaction(new SettingsFragment(), "SettingsFragment");
                EventBus.getDefault().post(new EventBusModel("SettingsFragment"));
                break;

            case 5:
                fragmentTransaction(new AboutFragment(), "AboutFragment");
                EventBus.getDefault().post(new EventBusModel("AboutFragment"));
                break;
        }
        //spinner.setSelection(0);
        closeDrawer();

    }

    private void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);

        } else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }


    /*
     * method for goto fragment*/
    public void fragmentTransaction(Fragment fragment, final String subtitle) {

        //recognize = DriverApplication.getInstance().getSharedPreferences().getString(Constant.SELECT_DAY, Constant.SELECT_DAY);
        //Util.recognizeSelectedItems(Constant.RECOGNIZE, subtitle);

        if (fragment != null) {
            goToFragment(fragment);
        }
    }

    private void goToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(content_frame, fragment, fragment.getClass().getCanonicalName());
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }

        if (currentPageName.equals("MonthlyFragment")) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Util.showToast(MainActivity.this, R.string.exit_confirm, true);
            System.out.println("==Please click BACK again to exit==");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }

        System.out.println("currentPageName=====" + currentPageName);

        if (currentPageName.equals("")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("ReportFragment")) {
            fragmentTransaction(new ListReportFragment(), "ListReportFragment");
            EventBus.getDefault().post(new EventBusModel("ListReportFragment"));
            return;
        }

        if (currentPageName.equals("LeaveFragment")) {
            fragmentTransaction(new ListLeaveFragment(), "ListLeaveFragment");
            EventBus.getDefault().post(new EventBusModel("ListLeaveFragment"));
            return;
        }

        if (currentPageName.equals("ProfileFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("WeekDayFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("CommentFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("ListLeaveFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("ComplaintFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("AboutFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("SettingsFragment")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
            return;
        }

        if (currentPageName.equals("PrivacyFragment")) {
            fragmentTransaction(new SettingsFragment(), "SettingsFragment");
            EventBus.getDefault().post(new EventBusModel("SettingsFragment"));
            return;
        }

        if (currentPageName.equals("UpdateFragment")) {
            fragmentTransaction(new SettingsFragment(), "SettingsFragment");
            EventBus.getDefault().post(new EventBusModel("SettingsFragment"));
            return;
        }
    }


    /*
     * set date for custom date piker
     * */
    private void setDate(final Calendar calendar) {
        if (calendar == null)
            return;

        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
        Date from_date = calendar.getTime();
        Date to_date = calendar.getTime();
        Date date = calendar.getTime();
        sharedData.setYear(jalaliCalendarUtil.getIranianYear());
        sharedData.setMonth(jalaliCalendarUtil.getIranianMonth());
        sharedData.setDay(jalaliCalendarUtil.getIranianDay());
        sharedData.setSelectedMenuItem(Constant.ACTION_DAY);

        String checkClick = DriverApplication.getInstance().getSharedPreferences().getString(Constants.checkClick, "");
        System.out.println("checkClick===" + checkClick);
        if (checkClick.equals("month")) {
            fragmentTransaction(new MonthlyFragment(), "MonthlyFragment");
            EventBus.getDefault().post(new EventBusModel("MonthlyFragment"));
        } else if (checkClick.equals("day")) {
            fragmentTransaction(new WeekDayFragment(), "WeekDayFragment");
            EventBus.getDefault().post(new EventBusModel("WeekDayFragment"));
        }


    }


    @Subscribe
    public void getEvent(EventBusModel model) {
        currentPageName = model.getCurrentPage();
        System.out.println("=========currentPageName=========" + currentPageName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void getInfo() {
        Globals globals = Globals.getInstance();
        DriverProfileResponseBean response = globals.getResponse();
        System.out.println("response=====" + response);
        if (response != null) {
            // jsonDate.setResponse(response);
            sharedData.setResponse(response);
            List<Integer> myArray = response.getRESULT().getDriverProfile().getPerson().getPictureBytes();
            if (myArray != null) {
                JSONArray jsArray = new JSONArray();
                for (int i = 0; i < myArray.size(); i++) {
                    jsArray.put(myArray.get(i));
                }
                byte[] bytes = new byte[jsArray.length()];
                for (int i = 0; i < jsArray.length(); i++) {
                    try {
                        bytes[i] = Integer.valueOf(jsArray.getInt(i)).byteValue();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img_profile.setImageBitmap(bitmap);
            }


            stop = true;
            String liceName = response.getRESULT().getDriverProfile().getDrivingLicence().getNameFv();
            String[] parts = liceName.split("-");
            String first = parts[0];
            String second = parts[1];

            String[] firstSplit = first.split(" ");
            String first1 = firstSplit[0];
            String first2 = firstSplit[1];

            String[] secondSplit = second.split("تا");
            String second1 = secondSplit[0];
            String second2 = secondSplit[1];

            TextView driverName_txt = findViewById(R.id.driverName);
            driverName_txt.setText(response.getRESULT().getDriverProfile().getPerson().getNameFv());
            if (response.getRESULT().getDriverProfile().getId() != null){
                sharedData.setDriverId(response.getRESULT().getDriverProfile().getId());
            }

            TextView driverCode = findViewById(R.id.driverCode);
            driverCode.setText(String.valueOf(response.getRESULT().getDriverProfile().getDriverCode()));

            TextView liceExpire_txt = findViewById(R.id.liceExpire);
            liceExpire_txt.setText("پایان اعتبار : " + second2);
        }
    }
}
