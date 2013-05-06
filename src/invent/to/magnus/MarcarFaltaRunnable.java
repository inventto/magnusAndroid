package invent.to.magnus;

import android.util.Log;

public class MarcarFaltaRunnable implements Runnable {

	private MagnusPresencaActivity magnusActivity;

	public MarcarFaltaRunnable(MagnusPresencaActivity magnusActivity) {
		this.magnusActivity = magnusActivity;
	}

	@Override
	public void run() {
		while(true){
			this.magnusActivity.getHandler().post(new Runnable() {
				
				@Override
				public void run() {
					new MarcarFalta().execute();
				}
			});
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				Log.i("=>THREADEXCEPT", e.getMessage());	
				e.printStackTrace();
			}
		}
	}

}
