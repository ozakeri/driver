package gap.com.driver.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gap.com.driver.R;
import gap.com.driver.common.CalendarUtil;
import gap.com.driver.gapcalendar.customweekview.PersianDate;
import gap.com.driver.model.DriverProfileResponseBean;
import gap.com.driver.util.Globals;

import static gap.com.driver.R.id.content_frame;

public class ProfileFragment extends Fragment {
    private TextView txt_driverName, txt_nationalNo, txt_mobileNo, txt_phoneNo, txt_liceNo, txt_expDate, txt_insuranceNo, txt_address, txt_between, txt_postalCode, txt_loginDate,txt_baseShift;
    private ImageView img_editProfile;
    private CircleImageView img_driverImage;
    private Globals globals = Globals.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        txt_driverName = (TextView) view.findViewById(R.id.txt_driverName);
        txt_nationalNo = (TextView) view.findViewById(R.id.txt_nationalNo);
        txt_mobileNo = (TextView) view.findViewById(R.id.txt_mobileNo);
        txt_phoneNo = (TextView) view.findViewById(R.id.txt_phoneNo);
        txt_liceNo = (TextView) view.findViewById(R.id.txt_liceNo);
        txt_expDate = (TextView) view.findViewById(R.id.txt_expDate);
        txt_insuranceNo = (TextView) view.findViewById(R.id.txt_insuranceNo);
        txt_address = (TextView) view.findViewById(R.id.txt_address);
        txt_between = (TextView) view.findViewById(R.id.txt_between);
        txt_postalCode = (TextView) view.findViewById(R.id.txt_postalCode);
        txt_loginDate = (TextView) view.findViewById(R.id.txt_loginDate);
        txt_baseShift = (TextView) view.findViewById(R.id.txt_baseShift);

        img_driverImage = (CircleImageView) view.findViewById(R.id.img_driverImage);
        img_editProfile = (ImageView) view.findViewById(R.id.img_editProfile);

        PersianDate persianDate = new PersianDate();
        txt_loginDate.setText(persianDate.dayName() + " " + persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay() + " ساعت " + persianDate.getHour() + ":" + persianDate.getMinute());

        DriverProfileResponseBean response = globals.getResponse();
        System.out.println("ProfileFragment=====" + response);
        if (response != null) {

            List<Integer> myArray = response.getRESULT().getDriverProfile().getPerson().getPictureBytes();
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
                img_driverImage.setImageBitmap(bitmap);
            }


            txt_driverName.setText(response.getRESULT().getDriverProfile().getPerson().getName() + " " + response.getRESULT().getDriverProfile().getPerson().getFamily());
            txt_nationalNo.setText(response.getRESULT().getDriverProfile().getPerson().getNationalCode());
            txt_mobileNo.setText(response.getRESULT().getDriverProfile().getPerson().getAddress().getMobileNo());
            txt_phoneNo.setText(response.getRESULT().getDriverProfile().getPerson().getAddress().getTelNo());
            txt_liceNo.setText(response.getRESULT().getDriverProfile().getDrivingLicence().getLicenceNo());
            txt_insuranceNo.setText(response.getRESULT().getDriverProfile().getPerson().getInsuranceNo());
            txt_address.setText(response.getRESULT().getDriverProfile().getPerson().getAddress().getAddress());
            txt_postalCode.setText(response.getRESULT().getDriverProfile().getPerson().getAddress().getPostalCode());
            txt_baseShift.setText(response.getRESULT().getDriverProfile().getShiftTypeBaseEnNameFv());

            String strExpireDate = response.getRESULT().getDriverProfile().getDrivingLicence().getExpireDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date expireDate = null;
            try {
                expireDate = simpleDateFormat.parse(strExpireDate);
                Date currentDate = new Date(System.currentTimeMillis());
                String expireDateStr = CalendarUtil.datesDiff(getActivity(), currentDate, expireDate, "yMd");
                int between = CalendarUtil.monthsBetween(currentDate, expireDate);

                txt_expDate.setText(expireDateStr);
                if (between <= 2) {
                    txt_between.setText(" ( " + between + " ماه تا پایان اعتبار " + " ) ");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        img_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new EditProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(content_frame, fragment, fragment.getClass().getCanonicalName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }

}

