package com.example.miniproject21.FragmentSet1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.miniproject21.R;

import java.util.ArrayList;

public class AcknowldegementFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_acknowldegement, container, false);
        ListView myListView=(ListView)view.findViewById(R.id.urllist);
        final ArrayList<String> url= new ArrayList<String>();
        url.add("Hebbars Kitchen");
        url.add("Youtube");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_expandable_list_item_1,url);
        myListView.setAdapter(arrayAdapter);
        return inflater.inflate(R.layout.fragment_acknowldegement, container, false);
    }
}