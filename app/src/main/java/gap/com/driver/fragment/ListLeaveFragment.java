package gap.com.driver.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gap.com.driver.R;
import gap.com.driver.adapter.PersonTimeOffAdapter;
import gap.com.driver.database.DataBaseClint;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.gapcalendar.customweekview.CommonMethod;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.EventBusModel;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.driverprofile.PersonTimeOffList;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.Globals;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.Utils;

public class ListLeaveFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Globals sharedData = Globals.getInstance();
    private FloatingActionButton floatingActionButton;
    private List<PersonTimeOffList> personTimeOffLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private PersonTimeOffAdapter adapter;
    private AppCompatTextView txt_null;
    private List<Driver> driverList;
    private Driver driver;
    private String fromDate, toDate;
    private Calendar calendar = Calendar.getInstance();
    private int currentDay, currentMonth, currentYear, day, month, year;
    private CircularProgressView waitProgress;
    private View bottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private TextView txt_showDate, txt_ok, txt_cancel;
    private NumberPicker numberpicker_year, numberpicker_month;
    private EditText editTextStartDate, editTextEndDate;
    private RelativeLayout bottomSheetLayout, layout_right, layout_left;
    private boolean isStart, isEnd = false;
    //private Spinner spin;
    List<String> itemDate;
    private PersianDate persianDate;
    private int counter = 0;
    private Roozh roozh;


    public ListLeaveFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_leave_layout, container, false);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        txt_null = view.findViewById(R.id.txt_null);
        waitProgress = view.findViewById(R.id.waitProgress);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        // spin = (Spinner) view.findViewById(R.id.selectDay);
        //spin.setOnItemSelectedListener(this);
        persianDate = new PersianDate();
        roozh = new Roozh();

        editTextStartDate = view.findViewById(R.id.editTextStartDate);
        editTextEndDate = view.findViewById(R.id.editTextEndDate);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        txt_showDate = view.findViewById(R.id.txt_showDate);
        layout_right = view.findViewById(R.id.layout_right);
        layout_left = view.findViewById(R.id.layout_left);

      /*  currentDay = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
        currentMonth = calendar.get(Calendar.MONTH); // beware of month indexing from zero
        currentYear = calendar.get(Calendar.YEAR);
        toDate = currentYear + "-" + (currentMonth + 1) + "-" + currentDay;

        day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
        month = calendar.get(Calendar.MONTH) - 1; // beware of month indexing from zero
        year = calendar.get(Calendar.YEAR);
        fromDate = year + "-" + (month + 1) + "-" + day;*/

        //fromDate = year + "-" + (month + 2) + "-" + day;
        //toDate = currentYear + "-" + (currentMonth + 2) + "-" + currentDay;

        moveToMonth();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new LeaveFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                EventBus.getDefault().post(new EventBusModel("LeaveFragment"));
            }
        });


        layout_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("counter========" + counter);
                if (counter != 11) {
                    counter++;
                    System.out.println("counter++=====" + counter);
                    moveToMonth();
                }
            }
        });

        layout_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter != 0) {
                    counter--;
                    System.out.println("counter--=====" + counter);
                    moveToMonth();
                }
            }
        });

        /*radioButton_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_one.setChecked(true);
                radioButton_three.setChecked(false);
                radioButton_six.setChecked(false);
                radioButton_oneYear.setChecked(false);

                day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                month = calendar.get(Calendar.MONTH) - 1; // beware of month indexing from zero
                year = calendar.get(Calendar.YEAR);
                fromDate = year + "-" + (month + 1) + "-" + day;
            }
        });

        radioButton_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_one.setChecked(false);
                radioButton_three.setChecked(true);
                radioButton_six.setChecked(false);
                radioButton_oneYear.setChecked(false);

                day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                month = calendar.get(Calendar.MONTH) - 3; // beware of month indexing from zero
                year = calendar.get(Calendar.YEAR);
                fromDate = year + "-" + (month + 1) + "-" + day;
            }
        });

        radioButton_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_one.setChecked(false);
                radioButton_three.setChecked(false);
                radioButton_six.setChecked(true);
                radioButton_oneYear.setChecked(false);

                day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                month = calendar.get(Calendar.MONTH) - 6; // beware of month indexing from zero
                year = calendar.get(Calendar.YEAR);
                fromDate = year + "-" + (month + 1) + "-" + day;
            }
        });

        radioButton_oneYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_one.setChecked(false);
                radioButton_three.setChecked(false);
                radioButton_six.setChecked(false);
                radioButton_oneYear.setChecked(true);

                day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                month = calendar.get(Calendar.MONTH); // beware of month indexing from zero
                year = calendar.get(Calendar.YEAR) - 1;
                fromDate = year + "-" + (month + 1) + "-" + day;

            }
        });*/

       /* img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitProgress.setVisibility(View.VISIBLE);
                getAllDriver();
            }
        });*/
        // init_bottomsheet();

        // showMenu();

        return view;
    }

    public void getPersonTimeOff() {
        personTimeOffLists = new ArrayList<>();

        System.out.println("getPersonTimeOff==fromDate==" + fromDate);
        System.out.println("getPersonTimeOff==toDate==" + toDate);

        if (driver == null)
            return;
        ServerCoordinator.getInstance().getDriverPersonTimeOffList(driver.getUsername(), driver.getPassword(), sharedData.getDriverId(), fromDate, toDate,
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        waitProgress.setVisibility(View.GONE);
                        personTimeOffLists = response.getrESULT().personTimeOffList;
                        System.out.println("complaintLists====" + personTimeOffLists.size());
                        if (personTimeOffLists != null) {
                            recyclerView.setAdapter(new PersonTimeOffAdapter(getActivity(), personTimeOffLists));
                            if (personTimeOffLists.size() == 0) {
                                txt_null.setVisibility(View.VISIBLE);
                            } else {
                                txt_null.setVisibility(View.GONE);
                            }

                        } else {
                            txt_null.setVisibility(View.VISIBLE);
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        waitProgress.setVisibility(View.GONE);
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
                    getPersonTimeOff();
                }
                return null;
            }
        }
        new GetAllDriver().execute();
    }

    @SuppressLint("SetTextI18n")
    public void init_bottomsheet() {
        persianDate = new PersianDate();
        final Calendar _calendar = Calendar.getInstance();
        final Calendar _calendar_show = Calendar.getInstance();
        bottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
        bottomSheetLayout = bottomSheet.findViewById(R.id.bottomSheetLayout);
        //txt_showDate = bottomSheet.findViewById(R.id.txt_showDate);
        numberpicker_year = bottomSheet.findViewById(R.id.numberpicker_year);
        numberpicker_month = bottomSheet.findViewById(R.id.numberpicker_month);
        txt_ok = bottomSheet.findViewById(R.id.txt_ok);
        txt_cancel = bottomSheet.findViewById(R.id.txt_cancel);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet.setVisibility(View.GONE);

        numberpicker_year.setMaxValue(1399);
        numberpicker_year.setMinValue(1395);

        numberpicker_month.setMinValue(1);
        numberpicker_month.setMaxValue(CommonMethod.numNames1.length);
        numberpicker_month.setDisplayedValues(CommonMethod.numNames1);
        numberpicker_month.setValue(CommonMethod.numNames1.length);


        numberpicker_year.setValue(persianDate.getShYear());
        numberpicker_month.setValue(persianDate.getShMonth());
        //txt_showDate.setText(CommonMethod.numNames1[numberpicker_month.getValue() - 1] + " - " + Utils.latinNumberToPersian(String.valueOf(numberpicker_year.getValue())));

        numberpicker_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int _day = calendar.get(Calendar.DAY_OF_MONTH);
                roozh.PersianToGregorian(numberpicker_year.getValue(), numberpicker_month.getValue(), 1);
                _calendar.set(roozh.getYear(), roozh.getMonth(), _day);

                JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(_calendar);
                //txt_showDate.setText(jalaliCalendarUtil.getIranianMonthStr() + " - " + Utils.latinNumberToPersian(String.valueOf(jalaliCalendarUtil.getIranianYear())));
            }
        });

        numberpicker_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int _day = calendar.get(Calendar.DAY_OF_MONTH);
                roozh.PersianToGregorian(numberpicker_year.getValue(), numberpicker_month.getValue(), 1);
                _calendar.set(roozh.getYear(), roozh.getMonth(), _day);

                JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(_calendar);
                //txt_showDate.setText(jalaliCalendarUtil.getIranianMonthStr() + " - " + Utils.latinNumberToPersian(String.valueOf(jalaliCalendarUtil.getIranianYear())));
            }
        });

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = true;
                isEnd = false;
                bottomSheet.setVisibility(View.VISIBLE);
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnd = true;
                isStart = false;
                bottomSheet.setVisibility(View.VISIBLE);
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetLayout.setBackgroundResource(R.color.glass_white);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheet.setVisibility(View.GONE);

                System.out.println("numberpicker11======" + numberpicker_year.getValue());
                System.out.println("numberpicker22======" + numberpicker_month.getValue());

                if (isStart) {
                    //day = _calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                    month = _calendar.get(Calendar.MONTH); // beware of month indexing from zero
                    year = _calendar.get(Calendar.YEAR);
                    fromDate = year + "-" + (month + 1) + "-" + 1;
                    editTextStartDate.setText(CommonMethod.numNames1[numberpicker_month.getValue() - 1] + " - " + numberpicker_year.getValue());
                } else if (isEnd) {
                    //currentDay = _calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
                    currentMonth = _calendar.get(Calendar.MONTH); // beware of month indexing from zero
                    currentYear = _calendar.get(Calendar.YEAR);
                    toDate = currentYear + "-" + (currentMonth + 1) + "-" + 1;
                    editTextEndDate.setText(CommonMethod.numNames1[numberpicker_month.getValue() - 1] + " - " + numberpicker_year.getValue());
                }


                if (editTextStartDate.getText().length() != 0 && !editTextStartDate.getText().toString().isEmpty()
                        && editTextEndDate.getText().length() != 0 && !editTextEndDate.getText().toString().isEmpty()) {
                    waitProgress.setVisibility(View.VISIBLE);
                    getAllDriver();
                }
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheet.setVisibility(View.GONE);
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
                            bottomSheetLayout.setBackgroundResource(R.color.glass_white);
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomSheetLayout.setBackgroundResource(0);
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheetLayout.setBackgroundResource(R.color.glass_white);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showMenu() {
        PersianDate _persianDate = new PersianDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        _persianDate.initGrgDate(thisYear, thisMonth, thisDay);

        itemDate = new ArrayList<>();

        String customDate = "14/09/2020";
        String currentDate = thisDay + "/" + "0" + thisMonth + "/" + thisYear;
        System.out.println("date1======" + customDate);
        System.out.println("date2======" + currentDate);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM", Locale.ROOT);
        DateTimeFormatter monthFormatter1 = DateTimeFormatter.ofPattern("yyyy", Locale.ROOT);
        YearMonth endMonth = YearMonth.parse(currentDate, dateFormatter);
        for (YearMonth month = YearMonth.parse(customDate, dateFormatter);
             !month.isAfter(endMonth);
             month = month.plusMonths(1)) {
            System.out.println("=-=-=-=-=-=-=" + month.format(monthFormatter));
            System.out.println("=-=-=-=-=-=-=" + month.format(monthFormatter1));
            _persianDate.initGrgDate(Integer.parseInt(month.format(monthFormatter1)), Integer.parseInt(month.format(monthFormatter)), thisDay);
            System.out.println("numNames1======" + CommonMethod.numNames1[_persianDate.getShMonth() - 1]);
            System.out.println("_persianDate======" + _persianDate.getShMonth());
            itemDate.add(CommonMethod.numNames1[_persianDate.getShMonth() - 1]);
        }

        System.out.println("itemDate=====" + itemDate.size());
        // SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), itemDate);
        // spin.setAdapter(adapter);
        //spin.setSelection(persianDate.getShMonth() - 1);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), CommonMethod.numNames1[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void moveToMonth() {
        PersianDate persianDate = new PersianDate();
        DateTime now = DateTime.now();
        persianDate.initGrgDate(now.getYear(), now.getMonthOfYear() - counter, now.getDayOfMonth());
        roozh.PersianToGregorian(persianDate.getShYear(), persianDate.getShMonth(), 1);
        fromDate = roozh.getYear() + "-" + roozh.getMonth() + "-" + roozh.getDay();
        System.out.println("==========fromDate==========" + persianDate.getShYear() + "-" + persianDate.getShMonth() + "-" + 1);

        roozh.PersianToGregorian(persianDate.getShYear(), persianDate.getShMonth(), persianDate.getShDay());

        if (counter != 0){
            roozh.PersianToGregorian(persianDate.getShYear(), persianDate.getShMonth(), 31);
        }
        toDate = roozh.getYear() + "-" + roozh.getMonth() + "-" + roozh.getDay();
        System.out.println("==========toDate==========" + persianDate.getShYear() + "-" + persianDate.getShMonth() + "-" + persianDate.getShDay());

        txt_showDate.setText(CommonMethod.numNames1[persianDate.getShMonth() - 1] + " " + Utils.latinNumberToPersian(String.valueOf(persianDate.getShYear())));
        waitProgress.setVisibility(View.VISIBLE);
        getAllDriver();

        // DateTime to = now.minusMonths(counter - 1);
        // DateTime from = now.minusMonths(counter);
        //toDate = String.valueOf(to);
        // fromDate = String.valueOf(from);
    }
}
