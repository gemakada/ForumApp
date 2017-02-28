package com.example.giorgos.forumapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makan on 26/1/2017.
 */

public class DiscussionFragment extends android.support.v4.app.Fragment {

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
                R.layout.recycle_discussion, container, false);
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

            super(inflater.inflate(R.layout.discussion, parent, false));
            title = (TextView) itemView.findViewById(R.id.textView2);
            dist=(TextView) itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   // ((MainActivity)getActivity()).Test(getAdapterPosition());

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
                    .inflate(R.layout.discussion, parent, false);

            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Discussion t;
            t=((MainActivity)getActivity()).Discussion_List.get(position);
            holder.title.setText(t.Get_user());
            holder.dist.setText(t.Get_message());

            // no-op
        }

        @Override
        public int getItemCount() {
            return ((MainActivity)getActivity()).Discussion_List.size();
        }


    }

    public void Prepare() {
        //adapter.notify();
        this.getFragmentManager().beginTransaction().detach(this).attach(this).commit();


    }
    public void Notify() {
        adapter.notifyDataSetChanged();
    }




}
