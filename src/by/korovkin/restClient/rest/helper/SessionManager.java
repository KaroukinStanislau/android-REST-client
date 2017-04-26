package by.korovkin.restClient.rest.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import by.korovkin.restClient.rest.constants.Constants;


public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context context;

	// Shared preferences file name


	public SessionManager(Context context) {
		this.context = context;
		pref = this.context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	public void setLogin(String token) {

        editor.putString(Constants.TOKEN, token);
		editor.commit();
		Log.d(TAG, "User login session modified!");
	}
	
	public String isLoggedIn(){
		return pref.getString(Constants.TOKEN, "");
	}
}
