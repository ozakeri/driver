package gap.com.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gap.com.driver.R;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> countryNames;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, List<String> countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_select_day, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        System.out.println("iiiiii=====" + i);
        System.out.println("countryNames=====" + countryNames.get(i));
        //names.setText(countryNames.get(i));
        return view;
    }
}
