package invent.to.magnus;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class VerifyVersionAndUpdate {
	private static final File DIRECTORY = Environment.getExternalStorageDirectory();// new File("/data/data/" + // MagnusPresencaActivity.class.getPackage().getName());

	private Context context;

	public ProgressDialog mProgressDialog;

	/** Called when the activity is first created. */
	public VerifyVersionAndUpdate(Context context) {
		this.context = context;
		Toast.makeText(context, "Verificando se há atualizações", Toast.LENGTH_SHORT).show();
		int versionCode = getVersionFromServer();
		checkInstalledApp(versionCode);
	}

	private Boolean checkInstalledApp(int versionCode) {
		Boolean isInstalled = false;
		PackageInfo p = null;
		try {
			p = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (p != null && versionCode > p.versionCode) {
			isInstalled = true;

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						downloadAndInstall();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_info);

			builder.setMessage("Nova versão(" + versionCode + ") disponível...");
			builder.setPositiveButton("Instalar", dialogClickListener);
			builder.setNegativeButton("Cancelar", dialogClickListener);
			AlertDialog alertDialog = builder.create();
			try {
				alertDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "Não há novas atualizações",
					Toast.LENGTH_SHORT).show();
		}

		return isInstalled;
	}

	public int getVersionFromServer() {
		String BuildVersionPath = GlobalActivity.ADDRESS + "/Version_"
				+ apkName() + ".txt";
		URL u;
		try {
			u = new URL(BuildVersionPath);

			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoInput(true);
			// c.setDoOutput(true);
			c.connect();

			InputStream in = c.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len1);
			}

			String s = baos.toString();

			int versionCode = Integer.parseInt(s.trim());

			baos.close();
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void downloadAndInstall() {
		new DownloadFileAsync().execute();
	}

	private String apkName() {
		return "ControleDeAcesso.apk";
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(context);
		}

		public void showDialog(Context c) {
			mProgressDialog = new ProgressDialog(c);
			mProgressDialog.setMessage("Baixando a nova versão...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {
				String url = GlobalActivity.ADDRESS + "/" + apkName();
				File path = DIRECTORY;
				File outputFile = new File(path, apkName());

				HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
				c.setRequestMethod("GET");
				c.setDoInput(true);
				c.connect();

				outputFile.deleteOnExit();

				outputFile.createNewFile();

				int lenghtOfFile = c.getContentLength();

				InputStream input = c.getInputStream();
				BufferedInputStream bis = new BufferedInputStream((InputStream) input);

				OutputStream output = new FileOutputStream(outputFile);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = bis.read(data)) != -1) {
					total += count;
					output.write(data, 0, count);
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
				}

				output.flush();
				output.close();
				input.close();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");

//				Intent installApp = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//				installApp.setData(Uri.fromFile(outputFile));
//				intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//				installApp.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//				intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.getApplicationInfo().packageName);
				context.startActivity(intent);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			mProgressDialog.dismiss();
		}
	}
}
