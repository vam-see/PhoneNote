package navatrana.projecttakenote;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView recyclerView;
    List<Contact> contactsList;
    DatabaseHandler db;
    RecyclerViewAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcvRecordings);
        db = new DatabaseHandler(getContext());
        new AsyncTask<Void, Void, List<Contact>>() {
            @Override
            protected List<Contact> doInBackground(Void... voids) {
                contactsList = db.getAllContacts();
                return contactsList;
            }

            @Override
            protected void onPostExecute(List<Contact> contacts) {
                super.onPostExecute(contacts);
                adapter = new RecyclerViewAdapter(contactsList, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        }.execute();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addItem(Contact contact){
        adapter.insert(0, contact);
    }

    public void updateItem(int position, Contact contact){
        adapter.updateItem(position, contact);
    }
}
