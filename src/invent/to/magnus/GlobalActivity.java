package invent.to.magnus;

import java.util.Calendar;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GlobalActivity extends Activity {

	long lastActivity;
	CountDownTimer verificaAtividade;

	static final int MAX_BRIGHTNESS = 80;
	static final int NORMAL_BRIGHTNESS = 45;
	static final int MIN_BRIGHTNESS = 16;
	static final int TIME_TO_DARK_DISPLAY = 60000;
	static final int TIME_TO_OFF_DISPLAY = 180000;

	public void activity() {
		lastActivity = Calendar.getInstance().getTimeInMillis();
		setBrilho(MAX_BRIGHTNESS);
	}

	public GlobalActivity() {
		super();
		lastActivity = Calendar.getInstance().getTimeInMillis();
		if (verificaAtividade == null) {
			verificaAtividade = new CountDownTimer(Long.MAX_VALUE, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					if (lastActivity + TIME_TO_OFF_DISPLAY < Calendar.getInstance().getTimeInMillis()) {
						setBrilho(MIN_BRIGHTNESS / 2);
					} else if (lastActivity + TIME_TO_DARK_DISPLAY < Calendar.getInstance().getTimeInMillis()) {
						setBrilho(MIN_BRIGHTNESS);
					}
				}

				@Override
				public void onFinish() {
					verificaAtividade = null;
				}
			};
			verificaAtividade.start();
		}
		activity();
	}
	
	public void onClick(View v) {
		activity();
	}

	public void setBrilho(int brilho) {
		Window window = getWindow();
		if (window != null) {
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.screenBrightness = brilho / 100.0f;
			window.setAttributes(lp);
		}
	}

	private void retoreNormalBrightness() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (lp.screenBrightness < NORMAL_BRIGHTNESS) {
			lp.screenBrightness = NORMAL_BRIGHTNESS / 100.0f;
			getWindow().setAttributes(lp);
		}
		activity();
	}

	@Override
	public void onBackPressed() {
		retoreNormalBrightness();
		return;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		retoreNormalBrightness();
		return super.onTouchEvent(event);
	}

	@Override
	public void finish() {
		verificaAtividade.cancel();
		super.finish();
	}

}