package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import edu.wpi.cs525h.ayeg.virtualgraffiti.R;

/**
 * Tags the given image at the given coordinates
 * This Activity expects to be called by another to return a result
 * 
 * @author Andrew Yee
 *
 */
public class GraffitiTaggerActivity extends Activity {

	Bundle tagInfo;
	String title;
	String tagger;
	ProgressDialog progress;
	
	/** dialog **/
	private final int TITLE_DIALOG = 1;
	private final int TAGGER_DIALOG = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tagInfo = getIntent().getExtras();
		if (tagInfo.getString("path").equals("")) {
			returnFail();
		}
		//need to chain dialogs because that is how android operates
		showDialog(TITLE_DIALOG);
		//createTitleDialog().show();
		//createTaggerDialog().show();
		
		//tagImage();
		//returnOkay();
	}
	
	/**
	 * Create a dialog based on the ID
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		
		switch(id) {
			case TITLE_DIALOG:
				dialog = createTitleDialog();
				break;
			case TAGGER_DIALOG:
				dialog = createTaggerDialog();
				break;
			default:
				dialog = null;
		}
		
		return dialog;
	}
	
	/**
	 * Create a dialog for the tagger to name the tag
	 * 
	 * @return the dialog
	 */
	AlertDialog createTitleDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.nameyourtag);
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setCancelable(false);
		builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				title = input.getText().toString().trim();
				showDialog(TAGGER_DIALOG);
			}
		});
		builder.setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelThisActivity();
			}
		});		
		
		return builder.create();
	}
	
	/**
	 * Create a dialog for the tagger to put his name in
	 * 
	 * @return the dialog
	 */
	AlertDialog createTaggerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.nameyourself);
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setCancelable(false);
		builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tagger = input.getText().toString().trim();
				tagImage();
			}
		});
		builder.setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelThisActivity();
			}
		});		
		
		return builder.create();
	}

	/**
	 * Generate a tag with the given info
	 */
	void tagImage() {
		//Toast.makeText(this, "We got here", Toast.LENGTH_SHORT).show();
		progress = ProgressDialog.show(this, "", "Uploading " + title + " to server...", true, false);
			
		Thread t = new Thread() {
			public void run() {
				Tag tag = generateTag();
				boolean result = false;
				try {
					result = ConnectionUtil.postImage(tag);
				} catch (Exception e) {}
				if (result) {
					getThisContext().runOnUiThread(new Runnable() {
						public void run() {
							getThisContext().returnOkay();
						}
					});
				} else {
					getThisContext().runOnUiThread(new Runnable() {
						public void run() {
							getThisContext().returnFail();
						}
					});
				}
				
			}
		};
		t.start();
	}
	
	/**
	 * Generate a tag with the information from the bundle
	 */
	Tag generateTag() {
		String imagePath = tagInfo.getString("path");
		double latitude = tagInfo.getDouble("latitude");
		double longitude = tagInfo.getDouble("longitude");
		double altitude = tagInfo.getDouble("altitude");
		Tag newTag = new Tag(latitude, longitude, altitude, tagger, title);
		newTag.setImagePath(imagePath);
		return newTag;
	}
	
	/**
	 * get this activity
	 * 
	 * @return
	 */
	public GraffitiTaggerActivity getThisContext() {
		return this;
	}
	
	/**
	 * Return a success
	 */
	public void returnOkay() {
		progress.dismiss();
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Return a failure
	 */
	public void returnFail() {
		if (!tagInfo.getString("path").equals("")) {
			progress.dismiss();
		}
		Intent returnIntent = new Intent();
		setResult(TagModeActivity.RESULT_FAIL, returnIntent);
		finish();
	}
	
	/** 
	 * Cancel this activity
	 */
	public void cancelThisActivity() {
		if (progress != null) {
			progress.dismiss();
		}
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, returnIntent);
		finish();
	}
}
