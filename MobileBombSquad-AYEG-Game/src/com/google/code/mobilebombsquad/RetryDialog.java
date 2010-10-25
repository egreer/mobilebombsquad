package com.google.code.mobilebombsquad;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

public class RetryDialog extends AlertDialog {

	MediaPlayer areyouokay;
	//AlertDialog dialog;
	
	//public RetryDialog(Context context, AlertDialog dialog) {
	public RetryDialog (Context context) {
		//this.dialog = dialog;
		super(context);
		areyouokay = MediaPlayer.create(context, R.raw.terryareyouokay);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	//void startDialog() {
		areyouokay.start();
		super.onCreate(savedInstanceState);
		
		
	}
}
