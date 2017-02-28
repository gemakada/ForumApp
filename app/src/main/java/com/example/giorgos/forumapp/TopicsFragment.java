package com.example.giorgos.forumapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by Giorgos on 25/1/2017.
 */

public class TopicsFragment extends android.support.v4.app.Fragment {

    private static View view;
    private static View mOriginalContentView;
    private List<Topic> Topic_List=new ArrayList<>();
    private ContentAdapter adapter;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public Fragment who;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Xartis kai prosvasi se upiresies topothesias

        // db = new DBHelper(getActivity());
        // File dbFile = getDatabasePath("MyDBName.db");
        // Log.e(LOG_TAG, dbFile.toString());


        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycle_view, container, false);
        adapter = new ContentAdapter();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter.notifyDataSetChanged();
        //adapter.notify();
        return recyclerView;
    }



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView dist;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_list, parent, false));
            title = (TextView) itemView.findViewById(R.id.list_title);
            dist=(TextView) itemView.findViewById(R.id.list_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((MainActivity)getActivity()).Test(getAdapterPosition());

                   // go(getAdapterPosition());

                   // Log.e(LOG_TAG, "You clickeeeed"+getAdapterPosition());

                    //Context context = v.getContext();
                    //Intent intent = new Intent(context, DetailActivity.class);
                    //context.startActivity(intent);
                }
            });
        }
    }


    public  class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 18;

        public ContentAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Topic t;
            t=((MainActivity)getActivity()).Topic_List.get(position);
            holder.title.setText(t.Get_name());
            holder.dist.setText(t.Get_author());

            // no-op
        }

        @Override
        public int getItemCount() {
            return ((MainActivity)getActivity()).Topic_List.size();
        }


    }

    public void Prepare() {
        //adapter.notify();
        this.getFragmentManager().beginTransaction().detach(this).attach(this).commit();


    }






}
