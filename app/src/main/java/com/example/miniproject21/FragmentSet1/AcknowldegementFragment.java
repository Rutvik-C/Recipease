package com.example.miniproject21.FragmentSet1;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        return inflater.inflate(R.layout.fragment_acknowldegement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView myListView=(ListView)view.findViewById(R.id.urllist);
        final ArrayList<String> url= new ArrayList<String>();
        url.add("https://hebbarskitchen.com/");
        url.add("https://www.allrecipes.com/");
        url.add("https://www.myfooddata.com/");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, url){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        myListView.setAdapter(adapter);

    }
}