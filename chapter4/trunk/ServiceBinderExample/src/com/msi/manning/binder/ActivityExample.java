package com.msi.manning.binder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityExample extends Activity {

    private ISimpleMathService service;
    private boolean bound;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iservice) {
            service = ISimpleMathService.Stub.asInterface(iservice);
            Toast.makeText(ActivityExample.this, "connected to Service", Toast.LENGTH_SHORT).show();
            bound = true;
        }
        public void onServiceDisconnected(ComponentName className) {
            service = null;
            Toast.makeText(ActivityExample.this, "disconnected from Service", Toast.LENGTH_SHORT).show();
            bound = false;
        }
    };
    
    private EditText inputa;
    private EditText inputb;
    private TextView output;
    private Button addButton;
    private Button subtractButton;
    private Button echoButton;    

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_example);

        inputa = (EditText) findViewById(R.id.inputa);
        inputb = (EditText) findViewById(R.id.inputb);
        output = (TextView) findViewById(R.id.output);
        addButton = (Button) findViewById(R.id.add_button);
        subtractButton = (Button) findViewById(R.id.subtract_button);
        echoButton = (Button) findViewById(R.id.echo_button);        
        
        addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                int result = service.add(
                        Integer.parseInt(inputa.getText().toString()), 
                        Integer.parseInt(inputb.getText().toString()));
                output.setText(String.valueOf(result));
                } catch (DeadObjectException e) {
                    Log.e("ActivityExample", "error", e);
                } catch (RemoteException e) {
                    Log.e("ActivityExample", "error", e);
                }               
            }
        }); 
        
        subtractButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                int result = service.subtract(
                        Integer.parseInt(inputa.getText().toString()), 
                        Integer.parseInt(inputb.getText().toString()));
                output.setText(String.valueOf(result));
                } catch (DeadObjectException e) {
                    Log.e("ActivityExample", "error", e);
                }  catch (RemoteException e) {
                    Log.e("ActivityExample", "error", e);
                }             
            }
        }); 
        
        echoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                String result = service.echo(inputa.getText().toString() + inputb.getText().toString());
                output.setText(result);
                } catch (DeadObjectException e) {
                    Log.e("ActivityExample", "error", e);
                } catch (RemoteException e) {
                    Log.e("ActivityExample", "error", e);
                }              
            }
        });    
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        if (!bound) {
            this.bindService(
                    new Intent(ActivityExample.this, SimpleMathService.class), 
                    connection,
                    Context.BIND_AUTO_CREATE);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (bound) {
            bound = false;
            this.unbindService(connection);
        }
    }
}