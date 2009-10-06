package org.mailboxer.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {
	Context context;


	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		boolean installed;


		SharedPreferences preferences = context.getSharedPreferences("saymyname", Context.MODE_WORLD_WRITEABLE);
		if(!preferences.getBoolean("start", false)) {
			return;
		}


		try {
			context.createPackageContext("com.google.tts", 0);

			installed = true;
		} catch (NameNotFoundException e) {
			Toast.makeText(context, R.string.start_toast, Toast.LENGTH_SHORT).show();

			installed = false;
		}


		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && installed) {
			new Thread() {
				public void run() {
					try {
						sleep(30000);
					} catch (InterruptedException e) {}

					bootService();
				};
			}.start();
		} else if(installed) {
			bootService();
		}
	}

	private void bootService() {
		context.startService(new Intent(context, SpeakService.class));
	}
}