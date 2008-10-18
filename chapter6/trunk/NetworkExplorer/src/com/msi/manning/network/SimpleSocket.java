package com.msi.manning.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Android direct to Socket example.
 * 
 * For this to work you need a server listening on the IP address and port listed below in code. 
 * See NetworkSocketServer project for an example.
 * 
 * 
 * @author charliecollins
 * 
 */
public class SimpleSocket extends Activity {

	private static final String CLASSTAG = SimpleSocket.class.getSimpleName();

	private EditText socketInput;
	private TextView socketOutput;
	private Button socketButton;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.simple_socket);

		socketInput = (EditText) this.findViewById(R.id.socket_input);
		socketOutput = (TextView) this.findViewById(R.id.socket_output);
		socketButton = (Button) this.findViewById(R.id.socket_button);

		socketButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				socketOutput.setText("");
				callSocket(socketInput.getText().toString());
			}
		});
	};

	private void callSocket(final String socketData) {

		String address = "192.168.0.12";
		int port = 8889;

		Socket socket = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;

		try {
			socket = new Socket(address, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			// send input terminated with \n
			String input = socketData;
			writer.write(input + "\n", 0, input.length() + 1);
			writer.flush();

			// read back output
			String output = reader.readLine();
			Log.d(Constants.LOGTAG, " " + CLASSTAG + " output - " + output);
			socketOutput.setText(output);

			// send EXIT and close
			writer.write("EXIT\n", 0, 5);
			writer.flush();

		} catch (IOException e) {		    
			Log.e(Constants.LOGTAG, " " + CLASSTAG
					+ " IOException calling socket", e);
		} finally {
			try {
				writer.close();				
			} catch (IOException e) {
				// swallow
			}
			try {
				reader.close();				
			} catch (IOException e) {
				// swallow
			}
			try {
				socket.close();				
			} catch (IOException e) {
				// swallow
			}
		}
	}

}