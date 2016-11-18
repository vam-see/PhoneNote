package navatrana.projecttakenote;

import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by Vamsee on 9/1/2016.
 */
public class CallReceiver extends PhoneStateReceiver {

    Intent myIntent;
    private static boolean isRunning = false;
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        myIntent = new Intent(ctx, NoteService.class);
        ContactNameTask csTask = new ContactNameTask(ctx, myIntent);
        if (!isRunning){
            isRunning = true;
            csTask.execute(number);
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        myIntent = new Intent(ctx, NoteService.class);
        ContactNameTask csTask = new ContactNameTask(ctx, myIntent);
        if (!isRunning){
            isRunning = true;
            csTask.execute(number);
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        if (isRunning){
            ctx.stopService(new Intent(ctx, NoteService.class));
            isRunning = false;
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        if (isRunning){
            ctx.stopService(new Intent(ctx, NoteService.class));
            isRunning = false;
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        if (isRunning){
            ctx.stopService(new Intent(ctx, NoteService.class));
            isRunning = false;
        }
    }
}
