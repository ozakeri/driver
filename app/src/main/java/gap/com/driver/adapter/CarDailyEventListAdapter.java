package gap.com.driver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gap.com.driver.R;
import gap.com.driver.common.CalendarUtil;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.CarDailyEventList;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Util;

public class CarDailyEventListAdapter extends RecyclerView.Adapter<CarDailyEventListAdapter.CustomView> {
    private Context context;
    private ArrayList<CarDailyEventList> arrayList = new ArrayList<>();
    private DateFormat sdf;
    private Calendar calendar = Calendar.getInstance();
    private Globals sharedData = Globals.getInstance();
    private Roozh roozh = new Roozh();

    public CarDailyEventListAdapter(Context context, ArrayList<CarDailyEventList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tab_fragment2, parent, false);
        return new CustomView(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        CarDailyEventList carDailyEventList = arrayList.get(position);
        Date startDate = null, endDate = null;
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (carDailyEventList != null) {
            holder.txt_car.setText(carDailyEventList.getCar().getNameFv());

            try {

                if (carDailyEventList.getStartTime() != null) {
                    startDate = sdf.parse(carDailyEventList.getStartTime());
                }


                if (carDailyEventList.getEndTime() != null) {
                    endDate = sdf.parse(carDailyEventList.getEndTime());
                }


                roozh.PersianToGregorian(sharedData.getYear(), sharedData.getMonth(), sharedData.getDay());
                calendar.set(roozh.getYear(), roozh.getMonth(), roozh.getDay());
                calendar.add(Calendar.MONTH, -1);
                Date date = calendar.getTime();

                if (Util.compareDates(date, date) == 0) {
                    holder.txt_startDate.setText(" از  " + CalendarUtil.convertPersianDateTime(startDate, "HH:mm"));
                    holder.txt_endDate.setText(" تا  " + CalendarUtil.convertPersianDateTime(endDate, "HH:mm"));
                } else {
                    holder.txt_startDate.setText(" از " + CalendarUtil.convertPersianDateTime(startDate, "yyyy/MM/dd - HH:mm:ss"));
                    holder.txt_endDate.setText(" تا " + CalendarUtil.convertPersianDateTime(endDate, "yyyy/MM/dd - HH:mm:ss"));
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class CustomView extends RecyclerView.ViewHolder {
        TextView txt_car, txt_startDate, txt_endDate;

        public CustomView(View itemView) {
            super(itemView);
            txt_car = itemView.findViewById(R.id.txt_car);
            txt_startDate = itemView.findViewById(R.id.txt_startDate);
            txt_endDate = itemView.findViewById(R.id.txt_endDate);
        }
    }
}
