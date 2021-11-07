package gap.com.driver.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gap.com.driver.R;
import gap.com.driver.adapter.ComplaintListAdapter;
import gap.com.driver.database.DataBaseClint;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.model.driverprofile.ComplaintList;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.CustomTextViewTitle;

public class ComplaintFragment extends Fragment {

    private RecyclerView recycler_view;
    private List<ComplaintList> complaintLists;
    private List<Driver> driverList;
    private Driver driver;
    private RadioButton radioButton_one, radioButton_three, radioButton_six, radioButton_oneYear;
    private ImageView img_search;
    private String fromDate, toDate;
    private Calendar calendar = Calendar.getInstance();
    private Globals globals = Globals.getInstance();
    private int currentDay, currentMonth, currentYear, day, month, year;
    private CircularProgressView waitProgress;
    private CustomTextViewTitle txt_null;
    private RelativeLayout relativeLayout;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.complaint_fragment, container, false);
        recycler_view = view.findViewById(R.id.recycler_view);
        radioButton_one = view.findViewById(R.id.radioButton_one);
        radioButton_three = view.findViewById(R.id.radioButton_three);
        radioButton_six = view.findViewById(R.id.radioButton_six);
        radioButton_oneYear = view.findViewById(R.id.radioButton_oneYear);
        img_search = view.findViewById(R.id.img_search);
        waitProgress = view.findViewById(R.id.waitProgress);
        txt_null = view.findViewById(R.id.txt_null);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        radioButton_one.setChecked(true);

        currentDay = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
        currentMonth = calendar.get(Calendar.MONTH); // beware of month indexing from zero
        currentYear = calendar.get(Calendar.YEAR);
        toDate = currentYear + "-" + (currentMonth + 1) + "-" + currentDay;

        day = calendar.get(Calendar.DAY_OF_MONTH); // beware of month indexing from zero
        month = calendar.get(Calendar.MONTH) - 1; // beware of month indexing from zero
        year = calendar.get(Calendar.YEAR);
        fromDate = year + "-" + (month + 1) + "-" + day;

        radioButton_three.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "YekanBakhBold.ttf"));
        radioButton_one.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "YekanBakhBold.ttf"));
        radioButton_six.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "YekanBakhBold.ttf"));
        radioButton_oneYear.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "YekanBakhBold.ttf"));


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(mLayoutManager);

        radioButton_one.setOnClickListener(new View.OnClickListener() {
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
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitProgress.setVisibility(View.VISIBLE);
                getAllDriver();
            }
        });

        return view;
    }


    public void getComplaint() {

        complaintLists = new ArrayList<>();

        if (driver == null)
            return;

        ServerCoordinator.getInstance().getDriverComplaintList(driver.getUsername(), driver.getPassword(), globals.getDriverId(), fromDate, toDate,
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        waitProgress.setVisibility(View.GONE);
                        complaintLists = response.getrESULT().complaintList;
                        System.out.println("complaintLists====" + complaintLists.size());
                        if (complaintLists != null) {
                            if (complaintLists.size() != 0) {
                                relativeLayout.setVisibility(View.VISIBLE);
                            }

                            recycler_view.setAdapter(new ComplaintListAdapter(complaintLists));
                            txt_null.setVisibility(View.GONE);

                            if (complaintLists.size() == 0) {
                                txt_null.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        waitProgress.setVisibility(View.GONE);
                        Utils.showErrors(getActivity(), error);
                    }
                });
    }

    private void getAllDriver() {
        relativeLayout.setVisibility(View.GONE);
        class GetAllDriver extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                driverList = DataBaseClint.getInstance(getActivity()).getAppDataBase().driverDao().getAll();
                if (driverList.size() > 0) {
                    driver = driverList.get(0);
                    getComplaint();
                }
                return null;
            }
        }
        new GetAllDriver().execute();
    }

}
