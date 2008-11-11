package com.msi.manning.telephonyexplorer;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneNumberUtilsExample extends Activity {
	private TextView pnOutput;
	private EditText pnInput;
	private EditText pnInPlaceInput;
	private Button pnFormat;

	@Override
	public void onCreate(Bundle icicle) {
		Log.d("TelExp ******", "PhoneNumberUtilsExample onCreate");

		super.onCreate(icicle);
		this.setContentView(R.layout.phonenumberutilsexample);

		this.pnOutput = (TextView) this.findViewById(R.id.pnoutput);
		this.pnInput = (EditText) this.findViewById(R.id.pninput);
		this.pnInPlaceInput = (EditText) this.findViewById(R.id.pninplaceinput);
		this.pnFormat = (Button) this.findViewById(R.id.pnformat);

		this.pnFormat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("TelExp ******", "format TextView input - "
						+ PhoneNumberUtilsExample.this.pnInput.getText().toString());

				// format as a phone number FIRST
				String phoneNumber = PhoneNumberUtils
						.formatNumber(PhoneNumberUtilsExample.this.pnInput
								.getText().toString());
				// then convert phone number keypad alpha to numeric
				phoneNumber = PhoneNumberUtils
						.convertKeypadLettersToDigits(PhoneNumberUtilsExample.this.pnInput
								.getText().toString());

				StringBuilder result = new StringBuilder();
				result.append(phoneNumber);
				result.append("\nisGlobal - "
						+ PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber));
				result.append("\nisEmergency - "
						+ PhoneNumberUtils.isEmergencyNumber(phoneNumber));

				PhoneNumberUtilsExample.this.pnOutput
						.setText(result.toString());

				PhoneNumberUtilsExample.this.pnInput.setText("");
			}
		});

		this.pnInPlaceInput
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean b) {
						if (v
								.equals(PhoneNumberUtilsExample.this.pnInPlaceInput)
								&& (b == false)) {
							Log.d("TelExp ******", "formatInPlace TextView input - "
								+ PhoneNumberUtilsExample.this.pnInPlaceInput.getText().toString());
							PhoneNumberUtils.formatNumber(
									PhoneNumberUtilsExample.this.pnInPlaceInput
											.getText(),
									PhoneNumberUtils.FORMAT_NANP);
						}
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}