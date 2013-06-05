package invent.to.magnus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class StartMonitorFuncionalReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	final Toast t = Toast.makeText(context, "Monitor Funcional inicializado automaticamente.", Toast.LENGTH_LONG);
    	t.show();
        Intent activity = new Intent(context, MagnusPresencaActivity.class);  
        activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activity);
    }

}
