package gap.com.driver.fragment;


import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import gap.com.driver.R;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.model.SalaryAttributeResponseBean;
import gap.com.driver.util.Globals;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishFragment extends Fragment {

    private TextView txt_date, txt_salaryId, txt_company, txt_driverCode, txt_yearMonth, txt_nameFamily, txt_absentDay, txt_fractionWork, txt_doubleDay, txt_shiftDay, txt_dutyDay, txt_timeOfDay, txt_overTime, txt_overTimeWork, txt_overTimeFind, txt_workingDayCount, txt_dutyWork;
    private ImageView img_driver;
    private Globals globals = Globals.getInstance();


    public FishFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fish, container, false);
        txt_date = view.findViewById(R.id.txt_date);
        txt_salaryId = view.findViewById(R.id.txt_salaryId);
        txt_company = view.findViewById(R.id.txt_company);
        txt_driverCode = view.findViewById(R.id.txt_driverCode);
        txt_yearMonth = view.findViewById(R.id.txt_yearMonth);
        txt_nameFamily = view.findViewById(R.id.txt_nameFamily);
        txt_absentDay = view.findViewById(R.id.txt_absentDay);
        txt_fractionWork = view.findViewById(R.id.txt_fractionWork);
        txt_doubleDay = view.findViewById(R.id.txt_doubleDay);
        txt_shiftDay = view.findViewById(R.id.txt_shiftDay);
        txt_dutyDay = view.findViewById(R.id.txt_dutyDay);
        txt_timeOfDay = view.findViewById(R.id.txt_timeOfDay);
        txt_overTime = view.findViewById(R.id.txt_overTime);
        txt_overTimeWork = view.findViewById(R.id.txt_overTimeWork);
        txt_overTimeFind = view.findViewById(R.id.txt_overTimeFind);
        txt_workingDayCount = view.findViewById(R.id.txt_workingDayCount);
        txt_dutyWork = view.findViewById(R.id.txt_dutyWork);
        img_driver = view.findViewById(R.id.img_driver);

        SalaryAttributeResponseBean salaryAttributeResponseBean = globals.getSalaryMonthly();
        DriverProfileResponseBean driverProfileResponseBean = globals.getResponse();

        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getWorkingDayCount());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getDutyWork());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getDoubleWork());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getShiftDay());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getDutyDay());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getTimeOffDay());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getOvertimeEncouraged());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getOvertimeWork());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getOvertimeFinal());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getAbsentDay());
        System.out.println("salaryAttributeResponseBean=====" + salaryAttributeResponseBean.getSalaryAttribute().getFractionWork());


        txt_salaryId.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getId()));
        txt_absentDay.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getAbsentDay()));
        txt_fractionWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getFractionWork()));
        txt_doubleDay.setText(salaryAttributeResponseBean.getSalaryAttribute().getDoubleDay());
        txt_shiftDay.setText(salaryAttributeResponseBean.getSalaryAttribute().getShiftDay());
        txt_dutyDay.setText(salaryAttributeResponseBean.getSalaryAttribute().getDutyDay());
        txt_timeOfDay.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getTimeOffDay()));
        txt_overTime.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeEncouraged()));
        txt_overTimeWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeWork()));
        txt_overTimeFind.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getOvertimeFinal()));
        txt_workingDayCount.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getWorkingDayCount()));
        txt_dutyWork.setText(String.valueOf(salaryAttributeResponseBean.getSalaryAttribute().getDutyWork()));

        txt_company.setText(driverProfileResponseBean.getRESULT().getDriverProfile().getCompany().getName());
        txt_driverCode.setText(driverProfileResponseBean.getRESULT().getDriverProfile().getDriverCode());
        txt_yearMonth.setText(salaryAttributeResponseBean.getSalaryAttribute().getYearMonthLeFV2());
        txt_nameFamily.setText(driverProfileResponseBean.getRESULT().getDriverProfile().getPerson().getName() + " " + driverProfileResponseBean.getRESULT().getDriverProfile().getPerson().getFamily());
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

        return view;
    }

}
