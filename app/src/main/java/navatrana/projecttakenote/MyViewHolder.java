package navatrana.projecttakenote;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Vamsee on 8/26/2016.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    CardView cardView;
    TextView txtName;
    TextView txtNumber;
    TextView txtMsg;
    ImageButton imgbtnDelete;
    ImageButton imgBtnEdit;
    ImageButton imgBtnCall;

    public MyViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        txtName = (TextView) itemView.findViewById(R.id.txtContactName);
        txtNumber = (TextView) itemView.findViewById(R.id.txtPhNumber);
        txtMsg = (TextView) itemView.findViewById(R.id.txtMsg);
        imgbtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDel);
        imgBtnEdit = (ImageButton) itemView.findViewById(R.id.imgBtnEdit);
        imgBtnCall = (ImageButton) itemView.findViewById(R.id.imgBtnCall);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
    }
}
