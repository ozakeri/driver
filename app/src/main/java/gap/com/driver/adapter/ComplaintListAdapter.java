package gap.com.driver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gap.com.driver.R;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.model.driverprofile.ComplaintList;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.widget.BTextView;

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.CustomView> {

    private List<ComplaintList> complaintLists = new ArrayList<>();
    private Driver driver;

    public ComplaintListAdapter(List<ComplaintList> complaintLists) {
        this.complaintLists = complaintLists;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_complaint_layout, viewGroup, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomView customView, int position) {

        ComplaintList complaintList = complaintLists.get(position);

        if (complaintList != null) {

            if (complaintList.car2 != null) {
                customView.txt_car.setText(complaintList.car2.getPropertyCode());
            }

            if (complaintList.getRequestDescription() != null) {
                customView.txt_comment.setText(complaintList.getRequestDescription());
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = null;
            if (complaintList.requestDate != null) {
                try {
                    date = sdf.parse(complaintList.requestDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                    customView.txt_date.setText(jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return complaintLists.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        BTextView txt_car, txt_comment, txt_date;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            txt_car = itemView.findViewById(R.id.txt_car);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_date = itemView.findViewById(R.id.txt_date);
        }

    }

}
