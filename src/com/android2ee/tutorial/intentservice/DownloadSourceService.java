package com.android2ee.tutorial.intentservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;

import com.android2ee.tutorial.intentservice.MainActivity.MyReceiver;

import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * @author florian
 * Notre IntentService, permettant de récupérer les donneés depuis une url donnée
 */
public class DownloadSourceService extends IntentService {

	// constante
	public static final String URL = "urlpath";
	public static final String SOURCE_URL = "destination_source";
	public static final String CITY = "city_location";
		
	/**
	 * constructeur
	 */
	public DownloadSourceService() {
		 super("DownloadService");
		 // on autorise à relancer le service si celui-ci est terminé par le système
		 setIntentRedelivery(true);
	}
	
	/**
     * You should not override this method for your IntentService. Instead,
     * override {@link #onHandleIntent}, which the system calls when the IntentService
     * receives a start request.
     * @see android.app.Service#onStartCommand
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       int result = super.onStartCommand(intent, flags, startId);
      
       return result;
    }

	@Override
	protected void onHandleIntent(Intent intent) {
		String urlPath = intent.getStringExtra(URL);
		InputStream is = null;
		BufferedReader r = null;
		StringBuilder result = null;
    		
		// on récupère les données depuis l'url
		try {
        			URL aURL = new URL(urlPath);
        			URLConnection conn = aURL.openConnection();
        			conn.connect();
        			is = conn.getInputStream();
        			r = new BufferedReader(new 	InputStreamReader(is));
					result = new StringBuilder();
					String line;
			while ((line = r.readLine()) != null) {
   				 result.append(line);
			}
		} catch (IOException e) {
			// message d'erreur
			
		} finally { 
			// on ferme bien tout les flux
			if ( r != null) {
				try {
					// nous n'avons pas besoin de ferme les autres flux, la fermeture est inclus depuis la méthode close() du BufferedReader
					r.close();
				} catch (IOException e) {
					// message d'erreur
				}
			}
		}
		// maintenant on transmet le résultat
		// on pourrait avoir un Handler, BroadCast, Notification, etc.
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(MyReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT); 
		broadcastIntent.putExtra(SOURCE_URL, result.toString());
		broadcastIntent.putExtra(CITY, intent.getStringExtra(CITY));
		sendBroadcast(broadcastIntent);
	
   	}
}
