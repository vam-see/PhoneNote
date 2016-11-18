package navatrana.projecttakenote;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vamsee on 8/26/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Contact> list = Collections.emptyList();
    Context context;
    MyViewHolder holder;
    final private static int PERMISSIONS_REQUEST_CODE = 1;

    public RecyclerViewAdapter(List<Contact> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtName.setText(list.get(position).name);
        holder.txtNumber.setText(list.get(position).phNumber);
        holder.txtNumber.setVisibility(View.VISIBLE);
        holder.txtMsg.setText(list.get(position).msg);
        holder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteContact(holder.txtName.getText().toString());
            }
        });
        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = holder.txtName.getText().toString();
                String number = holder.txtNumber.getText().toString();
                String message = holder.txtMsg.getText().toString();
                MessageDialog messageDialog = MessageDialog.newInstance("UPDATE", name, number, message, Integer.toString(position));
                FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
                messageDialog.show(manager, "Message Dialog");
            }
        });

        holder.imgBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String callNumber = "tel:"+ holder.txtNumber.getText().toString();
                callIntent.setData(Uri.parse(callNumber));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((MainActivity)context,new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
                    }
                }
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void insert(int position, Contact contact){
            list.add(position, contact);
            notifyItemInserted(position);
    }

    public void updateItem(int position, Contact contact){
        list.set(position, contact);
        notifyItemChanged(position);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
