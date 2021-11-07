package gap.com.driver.gapcalendar.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import gap.com.driver.R;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.gapcalendar.customweekview.DateConvertor;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.gapcalendar.customweekview.Roozh;
import gap.com.driver.model.SalaryAttributeList;
import gap.com.driver.util.Constant;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Util;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {
    private static final String tag = "JalaliAdapter";
    private DateConvertor dateConvertor;
    private Context _context = null;
    private ArrayList<String> list = null;
    private static final int DAY_OFFSET = 0;
    private int currentDayOfMonth;
    private DateConvertor tmpDateConvertor = new DateConvertor();
    private ArrayList<SalaryAttributeList> arrayList;

    public CalendarAdapter(Context context, int year, int month, int day, ArrayList<SalaryAttributeList> arrayList) {
        super();
        this._context = context;
        this.list = new ArrayList<String>();
        this.dateConvertor = new DateConvertor();
        this.dateConvertor.setIranianDate(year, month, day);
        this.currentDayOfMonth = this.dateConvertor.getIranianDay();
        this.printMonth(month, year);
        this.arrayList = arrayList;
    }


    /*
     * set and print month to month fragment list view
     * */
    private void printMonth(int mm, int yy) {
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = mm - 1;
        String currentMonthName = this.dateConvertor.getMonthName(currentMonth);//getMonthAsString(currentMonth);
        int daysInMonth = this.dateConvertor.getDaysInMonth(currentMonth);


        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = this.dateConvertor.getDaysInMonth(prevMonth);//getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = this.dateConvertor.getDaysInMonth(prevMonth);//getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = this.dateConvertor.getDaysInMonth(prevMonth);//getNumberOfDaysOfMonth(prevMonth);
        }

        int getStartWeek = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_START_WEEKDAY, 0);
        switch (getStartWeek) {
            case 1:
                tmpDateConvertor.setIranianDate(yy, mm, 1);
                break;
            case 2:
                tmpDateConvertor.setIranianDate(yy, mm, 0);
                break;
            case 3:
                tmpDateConvertor.setIranianDate(yy, mm, -1);
                break;

            default:
                tmpDateConvertor.setIranianDate(yy, mm, 1);
        }

        GregorianCalendar cal = new GregorianCalendar(tmpDateConvertor.getGregorianYear(), tmpDateConvertor.getGregorianMonth() - 1, tmpDateConvertor.getGregorianDay());

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;

        trailingSpaces = (currentWeekDay + 1) % 7;


        /*Calculate Leap Years*/
        if (tmpDateConvertor.IsLeap(yy))
            if (mm == 12)
                ++daysInMonth;
            else if (mm == 1)
                ++daysInPrevMonth;

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            list.add(String
                    .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                            + i)
                    + "-GREY"
                    + "-"
                    + this.dateConvertor.getMonthName(prevMonth)//getMonthAsString(prevMonth)
                    + "-"
                    + prevYear);

        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {

            if (i == this.currentDayOfMonth) {
                list.add(String.valueOf(i) + "-BLUE" + "-"
                        + this.dateConvertor.getMonthName(currentMonth) + "-" + yy);
            } else {
                list.add(String.valueOf(i) + "-WHITE" + "-"
                        + this.dateConvertor.getMonthName(currentMonth) + "-" + yy);
            }
        }

        // Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            list.add(String.valueOf(i + 1) + "-GREY" + "-"
                    + this.dateConvertor.getMonthName(nextMonth) + "-" + nextYear);
        }

        this.change2RTL();
    }

    /**
     * Changes the order of the List items to suite the RTL formatting.
     */
    private void change2RTL() {
        List<String> tmList1 = new ArrayList<String>();
        List<String> lstRTL = new ArrayList<String>();

        for (int i = 0; i < this.list.size(); i += 7) {
            tmList1.clear();
            for (int c = i; c < i + 7; c++) {
                tmList1.add(this.list.get(c));
            }

            Collections.reverse(tmList1);
            lstRTL.addAll(tmList1);
        }

        this.list.clear();
        this.list.addAll(lstRTL);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_gridcell, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];

        String monthNumber = DriverApplication.getInstance().monthNameNum(themonth);
        //Event event = databaseAssets.getEvent(theyear, name, theday);
        // Set the Day GridCell
        String s = Util.faToEn(theday);
        holder.gridCell.setText(s);
        holder.calendar_day_gridcell_1.setText(s);
        holder.calendar_day_gridcell_2.setText(s);

        holder.gridCell.setTag(theday + "-" + themonth + "-" + theyear);
        String currentDate = themonth + "  " + theyear;
        Globals shareData = Globals.getInstance();
        shareData.setMonthMenuDate(themonth);

        PersianDate persianDate = new PersianDate();
        String dayName = persianDate.dayName(Integer.parseInt(theyear), Integer.parseInt(monthNumber), Integer.parseInt(theday));
        String monthName = themonth;

        TextView spinner_txt = (TextView) ((Activity) _context).findViewById(R.id.spinner_txt);
        spinner_txt.setText(Util.faToEn(currentDate));

        if (day_color[1].equals("GREY")) {
            holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.lightgray02));
            holder.relativeLayout.setBackgroundResource(R.color.cal_background);
            holder.itemView.setClickable(false);
            System.out.println("GREY=======");
        }
        if (day_color[1].equals("WHITE")) {
            holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.base));
            holder.relativeLayout.setBackgroundResource(R.color.white);
            //holder.itemView.setClickable(true);
            System.out.println("WHITE=======");
        }
        if (day_color[1].equals("BLUE")) {


           if (Integer.parseInt(monthNumber) == persianDate.getShMonth() || shareData.isCurrentDate()) {
                System.out.println("===-=-=-=-=-====" + theday);
                System.out.println("===-=-=-=-=-====" + shareData.getCurrentDay());
                if (shareData.getCurrentDay() == 0){
                    shareData.setCurrentDay(Integer.parseInt(theday));
                }
                if (Integer.parseInt(theday) == shareData.getCurrentDay()){
                    holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.colorPrimaryDark));
                    holder.gridCell.setTextSize(20);
                    holder.layout_top.setBackgroundResource(R.drawable.border_current_edittext);
                }

            }else {
                holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.base));
                holder.relativeLayout.setBackgroundResource(R.color.white);
            }

            shareData.setDayMenuDate(holder.gridCell.getText() + "-" + themonth);
            shareData.setWeekMenuDate(1 + "-" + 7 + " " + themonth);
        }

        if (!day_color[1].equals("GREY")) {

            Roozh roozh = new Roozh();
            roozh.PersianToGregorian(Integer.parseInt(theyear), Integer.parseInt(monthNumber), Integer.parseInt(theday));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


            if (arrayList != null) {
                for (SalaryAttributeList salaryAttributeListBean : arrayList) {
                    try {

                        if (salaryAttributeListBean.getDate() != null) {
                            Date date1 = sdf.parse(String.valueOf(roozh));
                            Date date2 = sdf.parse(salaryAttributeListBean.getDate());

                            if (date1.compareTo(date2) == 0) {
                                if (salaryAttributeListBean.getDriverDailyActivity().getId() != null) {
                                    holder.id.setText(salaryAttributeListBean.getDriverDailyActivity().getId().toString());
                                }
                                holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.colorPrimaryDark));
                                //if (salaryAttributeListBean.getProcessStatus().equals(1)) {

                                if (salaryAttributeListBean.getCalender() != null) {
                                    if (salaryAttributeListBean.getCalender().getHolidayIs() == 1) {
                                        //روز تعطیل
                                        holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.orrange));
                                    }
                                }


                                //holder.calendar_description.setText(salaryAttributeListBean.getDriverDailyActivity().getShiftTypeEnText());
                                if (salaryAttributeListBean.getDailyWorkTypeEn() != null) {
                                    if (salaryAttributeListBean.getDailyWorkTypeEn().equals(0)) {
                                        //شاغل
                                        holder.gridCell.setBackgroundResource(R.drawable.white_circle_drawable);
                                    }

                                    if (salaryAttributeListBean.getDailyWorkTypeEn() == 2) {
                                        //مرخصی استحقاقی
                                        holder.calendar_description.setText(salaryAttributeListBean.getDailyWorkTypeEnText());
                                        //holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.blue1));
                                    }

                                    if (salaryAttributeListBean.getDailyWorkTypeEn().equals(3)) {
                                        //مرخصی بدون حقوق
                                        holder.calendar_description.setText(R.string.label_jalaliCalendar_TimeOFFWithoutRights);
                                        //holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.blue1));
                                    }

                                    if (salaryAttributeListBean.getDailyWorkTypeEn().equals(4)) {
                                        //مرخصی استعلاجی
                                        holder.calendar_description.setText(R.string.label_jalaliCalendar_TimeOFFSick);
                                        //holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.blue1));
                                    }

                                    if (salaryAttributeListBean.getDailyWorkTypeEn().equals(5)) {
                                        //غیبت
                                        holder.calendar_description.setText(R.string.label_jalaliCalendar_Absence);
                                    }

                                    if (salaryAttributeListBean.getDailyWorkTypeEn().equals(6)) {
                                        //ماموریت
                                        holder.calendar_description.setText(R.string.label_jalaliCalendar_Duty);
                                    }

                                    /*if (salaryAttributeListBean.getCalender() != null) {
                                        if (salaryAttributeListBean.getCalender().getHolidayIs() == 1) {
                                            //روز تعطیل
                                            holder.gridCell.setTextColor(this._context.getResources().getColor(R.color.orrange));
                                        }
                                    }*/
                                }
                                //   }


                                if (salaryAttributeListBean.getDriverDailyActivity() != null){
                                    if (salaryAttributeListBean.getDriverDailyActivity().getShiftTypeEn()!= null){
                                        switch (salaryAttributeListBean.getDriverDailyActivity().getShiftTypeEn()) {
                                            case 0:
                                                holder.shiftIcon.setBackgroundResource(R.drawable.sun);
                                                break;

                                            case 1:
                                                holder.shiftIcon.setBackgroundResource(R.drawable.afternoon);
                                                break;

                                            case 2:
                                                holder.shiftIcon.setBackgroundResource(R.drawable.night);
                                                break;

                                            case 5:
                                                holder.shiftIcon.setBackgroundResource(R.drawable.middle);
                                                break;
                                        }
                                    }
                                }



                                if (salaryAttributeListBean.getDriverDailyActivity() != null){

                                    if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3() != null){
                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(0)) {
                                            //ثبت اولیه
                                            holder.statusIcon.setBackgroundResource(R.drawable.sent);
                                        }

                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(1)) {
                                            //تایید شده
                                            holder.statusIcon.setBackgroundResource(R.drawable.deliverd);
                                        }

                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(5)) {
                                            //نتظر ورود
                                            holder.statusIcon.setBackgroundResource(R.drawable.waiting_driver_icon);
                                        }

                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(10)) {
                                            //عدم مراجعه
                                            holder.statusIcon.setBackgroundResource(R.drawable.pending);
                                        }

                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(20)) {
                                            //فاقد برنامه
                                            holder.statusIcon.setBackgroundResource(R.drawable.pending);
                                        }

                                        if (salaryAttributeListBean.getDriverDailyActivity().getProcessStatus3().equals(25)) {
                                            //عدم ثبت تعرفهه
                                            holder.statusIcon.setBackgroundResource(R.drawable.warning_icon);
                                        }
                                    }

                                }


                            }
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

           /* if (dayName.equals("جمعه")) {
                //relativeLayout.setBackgroundResource(R.color.orrange);
                holder.calendar_day_gridcell_1.setVisibility(View.VISIBLE);
                holder.calendar_day_gridcell_2.setVisibility(View.GONE);
                holder.gridCell.setText("");
            }*/

            /*if (dayName.equals("پنج\u200Cشنبه")) {
                //relativeLayout.setBackgroundResource(R.color.orange_low);
                holder.calendar_day_gridcell_2.setVisibility(View.VISIBLE);
                holder.calendar_day_gridcell_1.setVisibility(View.GONE);
                holder.gridCell.setText("");
            }*/
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    /*
     * custom view holder
     * */
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView gridCell, calendar_day_gridcell_1, calendar_day_gridcell_2, calendar_description, id;
        RelativeLayout relativeLayout,layout_top;
        ImageView statusIcon, shiftIcon;

        public MyViewHolder(View convertView) {
            super(convertView);
            gridCell = (TextView) convertView.findViewById(R.id.calendar_day_gridcell);
            calendar_day_gridcell_1 = (TextView) convertView.findViewById(R.id.calendar_day_gridcell_1);
            calendar_day_gridcell_2 = (TextView) convertView.findViewById(R.id.calendar_day_gridcell_2);
            calendar_description = (TextView) convertView.findViewById(R.id.calendar_description);
            id = (TextView) convertView.findViewById(R.id.id);
            relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            layout_top = (RelativeLayout) convertView.findViewById(R.id.layout_top);
            statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
            shiftIcon = (ImageView) convertView.findViewById(R.id.shiftIcon);
        }
    }

}
