package com.tbowdev.starwarspeople.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbowdev.starwarspeople.Model.DataCache;
import com.tbowdev.starwarspeople.Model.SwapiClient;
import com.tbowdev.starwarspeople.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import apollo.AllPeopleQuery;

public class MainActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "MainActivity";

    private RecyclerView mPeopleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register this as Observer
        DataCache.getInstance().addObserver(this);

        // Query server for all people
        SwapiClient.queryAllPeople();

        // initialize an empty recyclerview
        LinearLayoutManager tripsLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mPeopleRecyclerView = (RecyclerView) findViewById(R.id.people_recycler_view);
        mPeopleRecyclerView.setLayoutManager(tripsLayoutManager);
    }

    private void initPeopleView() {

        PersonAdapter personAdapter = new PersonAdapter(DataCache.getInstance().getAllPeople());
        mPeopleRecyclerView.setAdapter(personAdapter);
    }

    // Observer pattern
    @Override
    public void update(Observable observable, Object o) {
        if (o != null) {
            // once the list of people has been received from the server, create the RecyclerView
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initPeopleView();
                }
            });
        }
        else {
            Log.d(TAG, "Invalid observer object received. Cannot process.");
        }
    }


    // RecyclerView code (Adapter and Holder classes)
    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder>
    {
        private List<AllPeopleQuery.person> people;

        public PersonAdapter(List<AllPeopleQuery.person> people)
        {
            this.people = people;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            View view = layoutInflater.inflate(R.layout.people_list_card, parent, false);

            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position)
        {
            AllPeopleQuery.person person = people.get(position);
            holder.bindPerson(person);
        }

        @Override
        public int getItemCount()
        {
            return people.size();
        }

        public List<AllPeopleQuery.person> getPeople()
        {
            return people;
        }

        public void setPeople(List<AllPeopleQuery.person> people)
        {
            this.people = (ArrayList<AllPeopleQuery.person>) people;
        }
    }

    private class PersonHolder extends RecyclerView.ViewHolder
    {
        AllPeopleQuery.person holderPerson;

        TextView name;
        TextView gender;
        TextView homeWorld;
        TextView inFilms;

        PersonHolder(View itemView)
        {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            gender = (TextView) itemView.findViewById(R.id.gender);
            homeWorld = (TextView) itemView.findViewById(R.id.home_world);
            inFilms = (TextView) itemView.findViewById(R.id.films);
        }

        void bindPerson(AllPeopleQuery.person person)
        {
            holderPerson = person;

            name.setText(holderPerson.name());
            gender.setText(holderPerson.gender());
            homeWorld.setText(holderPerson.homeworld().name());
            String films = formatFilmString(holderPerson.filmConnection().edges());
            inFilms.setText(films);
        }

        private String formatFilmString(List<AllPeopleQuery.Edge> films) {
            if (films == null) {
                return "Not in any films.";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < films.size(); i++) {
                sb.append(films.get(i).node().episodeID());
                if (i != films.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }
}
