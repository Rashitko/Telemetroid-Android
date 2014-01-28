package sk.pixel.telemetroid.options_lists;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import sk.pixel.telemetroid.R;

public abstract class OptionsListFragment extends ListFragment {
    private final String TAG = "OptionsListFragment";

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    protected String[] options = {};
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public OptionsListFragment(String[] options) {
        this.options = options;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<HashMap<String, String>> titleData = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, Integer>> imageData = new ArrayList<HashMap<String, Integer>>();
        HashMap<String, String> titleEntry;
        HashMap<String, Integer> imageEntry;
        for (int i = 0; i < options.length; i++) {
            titleEntry = new HashMap<String, String>();
            titleEntry.put(OptionsAdapter.TITLE_KEY, options[i]);
            titleData.add(titleEntry);
            imageEntry = new HashMap<String, Integer>();
            imageEntry.put(OptionsAdapter.IMAGE_RESOURCE_KEY, android.R.drawable.ic_dialog_alert);
            imageData.add(imageEntry);
        }
        OptionsAdapter adapter = new OptionsAdapter(getActivity(), titleData, imageData);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        setActivateOnItemClick(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
