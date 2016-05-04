package com.h4115.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    protected ListView lsv;
    protected SwipeRefreshLayout lrl;
    protected ArrayList<Happening> happenings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,null);

        lsv = (ListView) v.findViewById(R.id.list_view);

        happenings = ((MainActivity)getActivity()).getHappeningList();
        HappeningAdapter hpa = new HappeningAdapter(getContext(), happenings);
        lsv.setAdapter(hpa);

        lrl = (SwipeRefreshLayout) v.findViewById(R.id.list_refresh_layout);
        lrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshTask refresh = new RefreshTask(((MainActivity)getActivity()));
                refresh.execute();
            }
        });

        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Happening happening = (Happening) lsv.getItemAtPosition(position);
                ((MainActivity) getActivity()).launchHappeningActivity(happening);
            }
        });

        return v;
    }

    public void refreshingFinished(){
        happenings = ((MainActivity)getActivity()).getHappeningList();
        HappeningAdapter hpa = new HappeningAdapter(getContext(), happenings);
        lsv.setAdapter(hpa);
        lrl.setRefreshing(false);
    }

    public class RefreshTask extends AsyncTask<Void, Void, Void> {

        protected MainActivity mainActivity;

        public RefreshTask(MainActivity mainActivity){
            this.mainActivity = mainActivity;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            this.mainActivity.refreshHappeningList();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... arg1) {
        }

        @Override
        protected void onPostExecute(Void arg2) {
        }
    }
}