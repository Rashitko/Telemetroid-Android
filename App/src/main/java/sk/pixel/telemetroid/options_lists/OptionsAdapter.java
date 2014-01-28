package sk.pixel.telemetroid.options_lists;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import sk.pixel.telemetroid.R;

public class OptionsAdapter extends BaseAdapter {
    public static final String TITLE_KEY = "title", IMAGE_RESOURCE_KEY = "image_resource";
    private Activity activity;
    private ArrayList<HashMap<String, String>> titleData;
    ArrayList<HashMap<String, Integer>> imageData;
    private static LayoutInflater inflater = null;

    public OptionsAdapter(Activity activity, ArrayList<HashMap<String, String>> titleData, ArrayList<HashMap<String, Integer>> imageData) {
        this.activity = activity;
        this.titleData = titleData;
        this.imageData = imageData;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return titleData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_row, null);
        }
        TextView title = (TextView) vi.findViewById(R.id.title);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image);
        HashMap<String, String> titleEntry = titleData.get(position);
        HashMap<String, Integer> imageEntry = imageData.get(position);
        title.setText(titleEntry.get(TITLE_KEY));
        thumb_image.setImageResource(imageEntry.get(IMAGE_RESOURCE_KEY));
        return vi;
    }

}
