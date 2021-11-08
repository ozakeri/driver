package gap.com.driver.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import gap.com.driver.R;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.SalaryAttributeResponseBean;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Utils;

public class FishActivity extends AppCompatActivity {

    private TextView txt_date, txt_fractionPerform, txt_salaryId, txt_company, txt_driverCode, txt_yearMonth, txt_nameFamily, txt_absentDay, txt_fractionWork, txt_doubleDay, txt_shiftDay, txt_dutyDay, txt_timeOfDay, txt_overTime, txt_overTimeWork, txt_overTimeFind, txt_workingDayCount, txt_dutyWork;
    private ImageView img_driver;
    private Globals globals = Globals.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fish);

        txt_date = findViewById(R.id.txt_date);
        txt_salaryId = findViewById(R.id.txt_salaryId);
        txt_company = findViewById(R.id.txt_company);
        txt_driverCode = findViewById(R.id.txt_driverCode);
        txt_yearMonth = findViewById(R.id.txt_yearMonth);
        txt_nameFamily = findViewById(R.id.txt_nameFamily);
        txt_absentDay = findViewById(R.id.txt_absentDay);
        txt_fractionWork = findViewById(R.id.txt_fractionWork);
        txt_doubleDay = findViewById(R.id.txt_doubleDay);
        txt_shiftDay = findViewById(R.id.txt_shiftDay);
        txt_dutyDay = findViewById(R.id.txt_dutyDay);
        txt_timeOfDay = findViewById(R.id.txt_timeOfDay);
        txt_overTime = findViewById(R.id.txt_overTime);
        txt_overTimeWork = findViewById(R.id.txt_overTimeWork);
        txt_overTimeFind = findViewById(R.id.txt_overTimeFind);
        txt_workingDayCount = findViewById(R.id.txt_workingDayCount);
        txt_dutyWork = findViewById(R.id.txt_dutyWork);
        txt_fractionPerform = findViewById(R.id.txt_fractionPerform);
        img_driver = findViewById(R.id.img_driver);

        SalaryAttributeResponseBean salaryAttributeResponseBean = globals.getSalaryMonthly();
        DriverProfileResponseBean driverProfileResponseBean = globals.getResponse();

        PersianDate persianDate = new PersianDate();
        txt_date.setText(Utils.latinNumberToPersian("تاریخ : " + persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay()));
        txt_salaryId.setText(Utils.latinNumberToPersian( "شماره : "  + salaryAttributeResponseBean.getSalaryAttributeId()));


        if (salaryAttributeResponseBean.getSalaryAttribute().getFractionWork() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getFractionWork();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }

            txt_fractionWork.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }

        if (salaryAttributeResponseBean.getSalaryAttribute().getOvertimeEncouraged() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getOvertimeEncouraged();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }
            txt_overTime.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }

        if (salaryAttributeResponseBean.getSalaryAttribute().getOvertimeWork() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getOvertimeWork();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }
            txt_overTimeWork.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }

        if (salaryAttributeResponseBean.getSalaryAttribute().getOvertimeFinal() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getOvertimeFinal();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }
            txt_overTimeFind.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }

        if (salaryAttributeResponseBean.getSalaryAttribute().getDutyWork() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getDutyWork();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }
            txt_dutyWork.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }

        if (salaryAttributeResponseBean.getSalaryAttribute().getFractionPerform() != null) {
            int t = salaryAttributeResponseBean.getSalaryAttribute().getFractionPerform();
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            String hoursStr = null;
            String minutesStr = null;

            if (hours<10){
                hoursStr = "0"+hours;
            }else {
                hoursStr = String.valueOf(hours);
            }
            if (minutes<10){
                minutesStr = "0"+minutes;
            }else {
                minutesStr= String.valueOf(minutes);
            }
            txt_fractionPerform.setText(Utils.latinNumberToPersian(hoursStr + ":" + minutesStr));
        }


        txt_absentDay.setText(Utils.latinNumberToPersian(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getAbsentDay())));
        //txt_fractionWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getFractionWork()));
        txt_doubleDay.setText(Utils.latinNumberToPersian(salaryAttributeResponseBean.getSalaryAttribute().getDoubleDay()));
        txt_shiftDay.setText(Utils.latinNumberToPersian(salaryAttributeResponseBean.getSalaryAttribute().getShiftDay()));
        txt_dutyDay.setText(Utils.latinNumberToPersian(salaryAttributeResponseBean.getSalaryAttribute().getDutyDay()));
        txt_timeOfDay.setText(Utils.latinNumberToPersian(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getTimeOffDay())));
        //  txt_overTime.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeEncouraged()));
        //  txt_overTimeWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeWork()));
        // txt_overTimeFind.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeFinal()));
        txt_workingDayCount.setText(Utils.latinNumberToPersian(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getWorkingDayCount())));
        // txt_dutyWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getDutyWork()));

        txt_company.setText(Utils.latinNumberToPersian(driverProfileResponseBean.getRESULT().getDriverProfile().getCompany().getName()));
        txt_driverCode.setText(Utils.latinNumberToPersian(driverProfileResponseBean.getRESULT().getDriverProfile().getDriverCode()));
        txt_yearMonth.setText(Utils.latinNumberToPersian(salaryAttributeResponseBean.getSalaryAttribute().getYearMonthLeFV2()));
        txt_nameFamily.setText(Utils.latinNumberToPersian(driverProfileResponseBean.getRESULT().getDriverProfile().getPerson().getName() + " " + driverProfileResponseBean.getRESULT().getDriverProfile().getPerson().getFamily()));
        List<Integer> myArray = driverProfileResponseBean.getRESULT().getDriverProfile().getPerson().getPictureBytes();
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
            img_driver.setImageBitmap(bitmap);
        }
    }
}
