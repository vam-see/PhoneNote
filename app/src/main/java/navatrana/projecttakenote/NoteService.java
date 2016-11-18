package navatrana.projecttakenote;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.*;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Vamsee on 8/26/2016.
 */
public class NoteService extends Service {
    private WindowManager windowManager;
    private LayoutInflater layoutInflater;
    View view;
    LayoutParams params;
    static String name;
    static String number;
    static String message;
    public NoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view = layoutInflater.inflate(R.layout.call_card_layout, null);

        name = intent.getStringExtra("NAME");
        TextView txtName = (TextView) view.findViewById(R.id.txtContactName);
        txtName.setText(name);

        number = intent.getStringExtra("NUMBER");
        TextView txtNumber = (TextView) view.findViewById(R.id.txtPhNumber);
        txtNumber.setText(number);

        message = intent.getStringExtra("MESSAGE");
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMsg);
        txtMessage.setText(message);

        ImageButton imgBtnClose = (ImageButton) view.findViewById(R.id.imgBtnClose);
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * .6);
        int height = (int)(dm.heightPixels* .2);

        params = new WindowManager.LayoutParams
                (
                        width,
                        height,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        windowManager.addView(view, params);


        view.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return false;
                    case MotionEvent.ACTION_UP:
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(v, params);
                        return false;
                }
                return false;
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy", "Reached onDestroy()");
        if (view != null)
            windowManager.removeView(view);
        super.onDestroy();
    }
}