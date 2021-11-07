package gap.com.driver.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import gap.com.driver.R;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.fragment.LeaveFragment;
import gap.com.driver.model.driverprofile.DriverProfileModel;
import gap.com.driver.model.driverprofile.JsonArrayAttach;
import gap.com.driver.services.ServerCoordinator;
import gap.com.driver.widget.CircularProgress;

public class PersonTimeOffAttachAdapter extends RecyclerView.Adapter<PersonTimeOffAttachAdapter.CustomView> {
    private List<JsonArrayAttach> attachArrayList;
    private Context context;
    private int attachFileId;
    private Driver driver;
    private LeaveFragment leaveFragment;
    private int id;

    public PersonTimeOffAttachAdapter(Context context, List<JsonArrayAttach> arrayList, Driver driver, LeaveFragment leaveFragment, int id) {
        this.attachArrayList = arrayList;
        this.context = context;
        this.driver = driver;
        this.leaveFragment = leaveFragment;
        this.id = id;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_attach, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomView holder, int position) {

        final JsonArrayAttach jsonArrayAttach = attachArrayList.get(position);
        if (jsonArrayAttach != null) {
            List<Integer> myArray = jsonArrayAttach.getAttachFileJsonArray();
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
                holder.img_attach.setImageBitmap(bitmap);
            }

            holder.image_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attachFileId = jsonArrayAttach.getAttachFileId();
                    System.out.println("attachFileId======" + attachFileId);
                    deleteAttach(driver, attachFileId, holder.progress_circular);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return attachArrayList.size();
    }

    public static class CustomView extends RecyclerView.ViewHolder {

        private ImageView img_attach, image_View;
        private CircularProgress progress_circular;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            img_attach = itemView.findViewById(R.id.img_attach);
            image_View = itemView.findViewById(R.id.image_View);
            progress_circular = itemView.findViewById(R.id.progress_circular);
        }
    }

    public void deleteAttach(Driver driver, int attachFileId, final CircularProgress progress_circular) {

        if (driver == null)
            return;
        System.out.println("attachFileId=====" + attachFileId);
        progress_circular.setVisibility(View.VISIBLE);
        ServerCoordinator.getInstance().deleteAttachFile(driver.getUsername(), driver.getPassword(), attachFileId,
                new Response.Listener<DriverProfileModel>() {
                    @Override
                    public void onResponse(DriverProfileModel response) {
                        if (response != null) {
                            progress_circular.setVisibility(View.GONE);
                            leaveFragment.getPersonTimeOffAttach(id);
                            System.out.println("====deleted======");
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error======" + error);
                        progress_circular.setVisibility(View.GONE);
                    }
                });
    }

}
