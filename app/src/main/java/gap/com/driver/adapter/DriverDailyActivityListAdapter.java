package gap.com.driver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gap.com.driver.R;
import gap.com.driver.common.CalendarUtil;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.DailyEventList;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Util;

public class DriverDailyActivityListAdapter extends RecyclerView.Adapter<DriverDailyActivityListAdapter.CustomView> {
    private Context context;
    private ArrayList<DailyEventList> arrayList = new ArrayList<>();
    private DateFormat sdf;
    private Calendar calendar = Calendar.getInstance();
    private Globals sharedData = Globals.getInstance();
    private Roozh roozh = new Roozh();
    private String date;
    private String sysParamStr;
    private String locateStr;
    private String subString;

    public DriverDailyActivityListAdapter(Context context, ArrayList<DailyEventList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tab_fragment1, parent, false);
        return new CustomView(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        DailyEventList dailyEventList = arrayList.get(position);
        Date startDate = null, endDate = null;
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (dailyEventList != null) {

            try {
                Date date = sdf.parse(dailyEventList.getStartTime());
                roozh.PersianToGregorian(sharedData.getYear(), sharedData.getMonth(), sharedData.getDay());

                calendar.set(roozh.getYear(), roozh.getMonth(), roozh.getDay());
                calendar.add(Calendar.MONTH, -1);
                Date dateNow = calendar.getTime();

                if (Util.compareDates(date, dateNow) == 0) {
                    this.date = CalendarUtil.convertPersianDateTime(date, "HH:mm");
                } else {
                    this.date = CalendarUtil.convertPersianDateTime(date, "yyyy/MM/dd - HH:mm");
                }
                holder.txt_date.setText(this.date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String filename = dailyEventList.getEvent().getName();
            System.out.println("filename======" + filename);
            int iend = filename.indexOf("(");
            if (iend != -1) {
                subString = filename.substring(0, iend);
                holder.txt_name.setText(subString);
            }
            holder.txt_name.setText(dailyEventList.getEvent().getName());

            if (dailyEventList.getSysParamStr() != null) {
                sysParamStr = dailyEventList.getSysParamStr();
                holder.txt_locate.setText(sysParamStr);
            } else if (dailyEventList.getEventPositionEn() != null) {
                int locate = dailyEventList.getEventPositionEn();
                if (locate == 0) {
                    holder.txt_locate.setText(R.string.label_item_list_eventPositionEn0);
                } else if (locate == 1) {
                    holder.txt_locate.setText(R.string.label_item_list_eventPositionEn1);
                } else {
                    holder.txt_locate.setText(R.string.label_item_list_eventPositionEn2);
                }
            }

            if (dailyEventList.getCar() != null) {
                if (dailyEventList.getCar().getPropertyCode() != null) {
                    holder.txt_code.setText("( " + dailyEventList.getCar().getPropertyCode() + " )");
                }
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
        TextView txt_name, txt_date, txt_locate, txt_code;

        public CustomView(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_locate = itemView.findViewById(R.id.txt_locate);
            txt_code = itemView.findViewById(R.id.txt_code);
        }
    }
}
