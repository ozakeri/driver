package gap.com.driver.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gap.com.driver.R;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.model.DriverEDAList;
import gap.com.driver.util.Utils;
import gap.com.driver.widget.BTextView;

public class DriverShiftListAdapter extends RecyclerView.Adapter<DriverShiftListAdapter.CustomView> {

    private List<DriverEDAList> driverEDALists = new ArrayList<>();
    private Context context;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    public DriverShiftListAdapter(Context context, List<DriverEDAList> driverEDALists) {
        this.driverEDALists = driverEDALists;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_shift_layout, viewGroup, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomView customView, int position) {

        final DriverEDAList eda = driverEDALists.get(position);
        customView.txt_hamShift.setText(eda.getPerson().getNameFv());
        if (eda.getDriverCode() != null) {
            customView.txt_code.setText(" ( " + eda.getDriverCode() + " ) ");
        } else {
            customView.txt_code.setText(" ( " + eda.getPerson().getEmployeeCode() + " ) ");
        }

        List<Integer> integerList = eda.getPerson().getPictureBytes();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < integerList.size(); i++) {
            jsonArray.put(integerList.get(i));
        }

        byte[] bytes = new byte[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                bytes[i] = Integer.valueOf(jsonArray.getInt(i)).byteValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        customView.img_hamShift.setImageBitmap(Utils.getCroppedBitmap(bitmap));


        customView.img_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.SEND_SMS) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                } else {
                    // Permission already granted. Enable the SMS button.
                    if (eda.getPerson().getMobileNo() != null) {
                     /*   System.out.println("getMobileNo=====" + eda.getPerson().getMobileNo());
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", eda.getPerson().getMobileNo());
                        context.startActivity(smsIntent);*/


                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto: " + eda.getPerson().getMobileNo()));
                        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                            sendIntent.setType("vnd.android-dir/mms-sms");
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        DriverApplication.getContext().startActivity(sendIntent);

                    }
                }

            }
        });

        customView.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eda.getPerson().getMobileNo() != null) {
                    System.out.println("getMobileNo=====" + eda.getPerson().getMobileNo());
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + eda.getPerson().getMobileNo()));
// Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);

                        // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    } else {
                        //You already have permission
                        try {
                            context.startActivity(callIntent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverEDALists.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        BTextView txt_hamShift, txt_code;
        ImageView img_message, img_call;
        CircleImageView img_hamShift;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            img_message = itemView.findViewById(R.id.img_message);
            img_hamShift = itemView.findViewById(R.id.img_hamShift);
            img_call = itemView.findViewById(R.id.img_call);
            txt_hamShift = itemView.findViewById(R.id.txt_hamShift);
            txt_code = itemView.findViewById(R.id.txt_code);

        }
    }
}
