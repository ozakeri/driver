package gap.com.driver.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gap.com.driver.R;
import gap.com.driver.adapter.DriverDailyActivityListAdapter;
import gap.com.driver.model.DailyEventList;
import gap.com.driver.model.SalaryAttribute;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment3 extends Fragment {
    private LayoutInflater inflater;
    private ViewGroup container;
    private RecyclerView recyclerView;
    private TextView showEmpty_txt;

    public TabFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment fragment_tab_fragment3
        View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        showEmpty_txt = view.findViewById(R.id.showEmpty_txt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showEmpty_txt.setVisibility(View.GONE);

        SalaryAttribute salaryAttribute;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            salaryAttribute = bundle.getParcelable("salaryAttribute");
            if (salaryAttribute != null) {
                if (salaryAttribute.getDriverDailyActivity() != null) {
                    ArrayList<DailyEventList> dailyEventLists = (ArrayList<DailyEventList>) salaryAttribute.getDriverDailyActivity().getDailyEventList();
                    recyclerView.setAdapter(new DriverDailyActivityListAdapter(getActivity(), dailyEventLists));
                    if (dailyEventLists == null){
                        showEmpty_txt.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                showEmpty_txt.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }
}
