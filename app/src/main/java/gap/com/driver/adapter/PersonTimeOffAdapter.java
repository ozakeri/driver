package gap.com.driver.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gap.com.driver.R;
import gap.com.driver.common.CalendarUtil;
import gap.com.driver.fragment.LeaveFragment;
import gap.com.driver.model.driverprofile.PersonTimeOffList;
import gap.com.driver.util.JalaliCalendarUtil;
import gap.com.driver.util.Utils;

import static gap.com.driver.R.id.content_frame;

public class PersonTimeOffAdapter extends RecyclerView.Adapter<PersonTimeOffAdapter.CustomView> {
    private List<PersonTimeOffList> personTimeOffArrayList = new ArrayList<>();
    private String type, fromDate, toDate, description = null;
    private int typeEn;
    private String fromDateCopy, toDateCopy;
    private Context context;

    public PersonTimeOffAdapter(Context context, List<PersonTimeOffList> arrayList) {
        this.personTimeOffArrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_person_time_off, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        final PersonTimeOffList personTimeOff = personTimeOffArrayList.get(position);
        if (personTimeOff != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date1 = formatter.parse(personTimeOff.getStartDate());
                Date date2 = formatter.parse(personTimeOff.getFinishDate());

                String countDate = CalendarUtil.datesDiff(context, date1, date2, "yMd");
                System.out.println("expireDateStr====" + countDate);

                Calendar calender1 = Calendar.getInstance();
                calender1.setTime(date1);
                fromDateCopy = personTimeOff.getStartDate();
                JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calender1);
                // holder.txt_startDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                fromDate = jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3();

                Calendar calender2 = Calendar.getInstance();
                calender2.setTime(date2);
                toDateCopy = personTimeOff.getFinishDate();
                jalaliCalendarUtil = new JalaliCalendarUtil(calender2);
                // holder.txt_endDate.setText(jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3());
                toDate = jalaliCalendarUtil.getIranianWeekDayStr() + " " + jalaliCalendarUtil.getIranianDate3();

                if (countDate.equals("0")) {
                    holder.txt_startDate.setText(Utils.latinNumberToPersian(fromDate));
                    holder.txt_date.setText(Utils.latinNumberToPersian(personTimeOff.getPersonTimeOffTypeEn_text() + " - " + 1 + " روز "));
                } else {
                    holder.txt_startDate.setText(Utils.latinNumberToPersian(fromDate));
                    holder.txt_date.setText(personTimeOff.getPersonTimeOffTypeEn_text() + " - " + Utils.latinNumberToPersian(countDate));
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }


            switch (personTimeOff.getPersonTimeOffTypeEn()) {
                case 0:
                    type = "استحقاقی";
                    typeEn = 0;
                    break;

                case 1:
                    type = "بدون حقوق";
                    typeEn = 1;
                    break;

                case 2:
                    type = "استعلاجی";
                    typeEn = 2;
                    break;
            }

            //holder.txt_startDate.setText(" مرخصی " + type + " از " + fromDate + " تا " + toDate);
            //holder.txt_startDate.setText(" از " + fromDate + " به مدت " + countDate+ "");
            // holder.txt_status.setText(String.valueOf(personTimeOff.getProcessStatus_text()));
            if (position % 2 == 0) {
                //  holder.relativeLayout.setBackgroundResource(R.color.mdtp_circle_color);
            } else {
                //   holder.relativeLayout.setBackgroundResource(R.color.mdtp_circle_color1);
            }

            if (personTimeOff.getProcessStatus() == 0) {
                holder.img_status.setBackgroundResource(R.drawable.sent);
            } else if (personTimeOff.getProcessStatus() == 1) {
                holder.img_status.setBackgroundResource(R.drawable.deliverd);
            } else if (personTimeOff.getProcessStatus() == 28) {
                holder.img_status.setBackgroundResource(R.drawable.warning_icon);
            }

            if (personTimeOff.getDescription() != null) {
                description = personTimeOff.getDescription();
            }

            if (personTimeOff.getProcessStatus() == 0) {
                holder.img_menu.setVisibility(View.VISIBLE);
            } else {
                holder.img_menu.setVisibility(View.GONE);
            }

            final int typeEn = personTimeOff.getPersonTimeOffTypeEn();
            final String fromDateCopy = personTimeOff.getStartDate();
            final String toDateCopy = personTimeOff.getFinishDate();
            final String description = personTimeOff.getDescription();
            final int id = personTimeOff.getId();

            System.out.println("fromDateCopy====" + fromDateCopy);
            System.out.println("id====" + id);

            holder.img_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showMenu(v, id, typeEn, fromDateCopy, toDateCopy, description);
                    LeaveFragment leaveFragment = new LeaveFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(id));
                    bundle.putString("typeEn", String.valueOf(typeEn));
                    bundle.putString("fromDate", String.valueOf(fromDateCopy));
                    bundle.putString("toDate", String.valueOf(toDateCopy));
                    bundle.putString("description", description);
                    bundle.putBoolean("isEdit", true);
                    leaveFragment.setArguments(bundle);
                    goToFragment(leaveFragment);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return personTimeOffArrayList.size();
    }

    public static class CustomView extends RecyclerView.ViewHolder {

        private TextView txt_startDate, txt_endDate, txt_type, txt_status, txt_date;
        private ImageView img_menu, img_status;
        private RelativeLayout relativeLayout;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            txt_startDate = itemView.findViewById(R.id.txt_startDate);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_endDate = itemView.findViewById(R.id.txt_endDate);
            txt_type = itemView.findViewById(R.id.txt_type);
            img_status = itemView.findViewById(R.id.img_status);
            img_menu = itemView.findViewById(R.id.img_menu);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    public void showMenu(View view, final int id, final int typeEn, final String fromDateCopy, final String toDateCopy, final String description) {
        PopupMenu menu = new PopupMenu(context, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int items = item.getItemId();
                switch (items) {
                    case R.id.action_edit:

                        break;

                }
                return true;
            }
        });
        menu.inflate(R.menu.edit_person_time_off_menu);
        menu.show();
    }

    private void goToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(content_frame, fragment, fragment.getClass().getCanonicalName());
        fragmentTransaction.commit();
    }
}
