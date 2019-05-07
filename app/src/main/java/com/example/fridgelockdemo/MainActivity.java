package com.example.fridgelockdemo;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fridge.Inventory;
import com.fridge.api.FridgeLockCtrol;
import com.fridge.api.LibError;

import com.network.RemoteProxy;
import com.network.Send_HTTP_Post_Request;
import com.rfid.api.ADReaderInterface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends Activity implements OnClickListener
{
    private static final Logger LOGGER = Logger.getLogger( MainActivity.class.getName() );
    private Spinner sn_com_list = null;
    private Spinner sn_com_list2 = null;
	private Button btn_connect = null;
    private Button btn_connect2 = null;
	private Button btn_disconnect = null;
	private Button btn_open_door = null;
	private Button btn_get_status = null;
	private Button btn_information = null;
    private Button btn_enable_polling = null;
    private Button btn_stop_polling = null;
    private Button btn_get_info = null;
    private Button btn_read_tags = null;
    private Button btn_read_logs = null;
    private Button btn_clear_logs = null;
    private Button test_api = null;
	private FridgeLockCtrol mFridgeDoor = new FridgeLockCtrol();
	private RemoteProxy remoteProxy = new RemoteProxy(1,"H9380-Android-IPC");
	//private Lock lock = new Lock();
    private Inventory inventory = new Inventory();
	private boolean pollingRunning = false;
	private ScrollView scrollView;
    private ADReaderInterface m_reader = new ADReaderInterface();

    private TextView tv;

    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sn_com_list = (Spinner) findViewById(R.id.sn_com_list);
        sn_com_list2 = (Spinner) findViewById(R.id.sn_com_list2);
		btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect2 = (Button) findViewById(R.id.btn_connect2);
		btn_disconnect = (Button) findViewById(R.id.btn_disconnect);
		btn_open_door = (Button) findViewById(R.id.btn_open_door);
		btn_get_status = (Button) findViewById(R.id.btn_get_status);
		btn_information = (Button) findViewById(R.id.btn_information);
        btn_enable_polling = (Button) findViewById(R.id.btn_enable_polling);
        btn_stop_polling = (Button) findViewById(R.id.btn_stop_polling);
        btn_read_logs = (Button) findViewById(R.id.btn_read_logs);
        btn_clear_logs = (Button) findViewById(R.id.btn_clear_logs);
        test_api = (Button) findViewById(R.id.test_api);
        btn_get_info= (Button) findViewById(R.id.btn_get_info);
        btn_read_tags = (Button) findViewById(R.id.btn_read_tags);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        tv = (TextView)findViewById(R.id.textView);


        clearLog();
        readLog();



		ArrayList<CharSequence> mSerialportList = new ArrayList<CharSequence>();
		ArrayAdapter<CharSequence> mAdaSerialportList = null;
		String[] paths = FridgeLockCtrol.getAllDevicesPath();
		for (String s : paths)
		{
			mSerialportList.add(s);
		}
		mAdaSerialportList = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_dropdown_item, mSerialportList);
		sn_com_list.setAdapter(mAdaSerialportList);
        sn_com_list2.setAdapter(mAdaSerialportList);

		btn_connect.setOnClickListener(this);
        btn_connect2.setOnClickListener(this);
		btn_disconnect.setOnClickListener(this);
		btn_open_door.setOnClickListener(this);
		btn_get_status.setOnClickListener(this);
		btn_information.setOnClickListener(this);
        btn_enable_polling.setOnClickListener(this);
        btn_stop_polling.setOnClickListener(this);
        btn_get_info.setOnClickListener(this);
        btn_read_tags.setOnClickListener(this);
        btn_clear_logs.setOnClickListener(this);
        test_api.setOnClickListener(this);
        btn_read_logs.setOnClickListener(this);
        btn_read_tags.setOnClickListener(this);

		UiVisible(false);


	}


	private void readLog(){
        try {
            LOGGER.log(Level.INFO, "readLog function");
            Process process = Runtime.getRuntime().exec("logcat -d ");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

            LOGGER.log(Level.INFO, "readLog function log: "+log.toString());
            tv.setText(log.toString());
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } catch (IOException e) {
            // Handle Exception
        }


    }

    private void clearLog(){

        try {
            LOGGER.log(Level.INFO, "clearLog function");
            Process process = Runtime.getRuntime().exec("logcat -b all -c");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            TextView tv = (TextView)findViewById(R.id.textView);
            LOGGER.log(Level.INFO, "clearLog function log: "+log.toString());
            tv.setText(log.toString());
        } catch (IOException e) {
            // Handle Exception
        }


    }

	private void UiVisible(boolean bConnect)
	{
        sn_com_list.setEnabled(!bConnect);
        btn_connect.setEnabled(!bConnect);
        btn_disconnect.setEnabled(bConnect);
        btn_open_door.setEnabled(bConnect);
        btn_get_status.setEnabled(bConnect);
        btn_information.setEnabled(bConnect);
        //bConnect=true; //TODO riga da togliere in produzione
        btn_enable_polling.setEnabled(bConnect && !pollingRunning);
        btn_stop_polling.setEnabled(bConnect && pollingRunning);
        btn_read_tags.setEnabled(bConnect);
	}


    private void stopPolling(){

        pollingRunning=false;
        UiVisible(true);

    }


	private void Connect()
	{
		if (sn_com_list.getCount() <= 0)
		{
			return;
		}
		int iret = mFridgeDoor.connect(
				sn_com_list.getSelectedItem().toString(), 38400, "8E1");
		if (iret == LibError.ERR_OK)
		{
			UiVisible(true);
		}
	}

    private void connect2()
    {
        if (sn_com_list2.getCount() <= 0)
        {
            return;
        }

        String connStr = String.format("RDType=RD5100;CommType=COM;ComPath=%s;Baund=38400;Frame=8E1;Addr=255", sn_com_list2.getSelectedItem().toString());
        tv.setText(connStr+"\n");

        if (m_reader.RDR_Open(connStr) != ApiErrDefinition.NO_ERROR)
        {
            Toast.makeText(this, "Failed to open the reader.",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            LOGGER.log(Level.INFO, "connected to reader");
            //sn_commType.setEnabled(false);
            //sn_serialPort.setEnabled(false);
            //btn_open.setEnabled(false);
            //btn_close.setEnabled(true);
            //btn_info.setEnabled(true);
            //chk_filtration.setEnabled(true);
            //btn_startInventory.setEnabled(true);
            //btn_stopInventory.setEnabled(false);
            //ed_ipAddr.setEnabled(false);
        }

    }

    private void getInformation()
    {
        StringBuffer buffer = new StringBuffer();
        int iret = m_reader.RDR_GetReaderInfor(buffer);
        if (iret == ApiErrDefinition.NO_ERROR)
        {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage(buffer.toString())
                    .setPositiveButton("OK", null).show();

        }
        else
        {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage("Failed to get the reader's information.")
                    .setPositiveButton("OK", null).show();
        }
    }

	private void Disconnect()
	{
		mFridgeDoor.disconnect();
		UiVisible(false);
	}

	private void Information()
	{
		StringBuffer strInfo = new StringBuffer();
		int iret = mFridgeDoor.getInformation(strInfo);
		if (iret == LibError.ERR_OK)
		{
			new AlertDialog.Builder(this).setTitle("")
					.setMessage(strInfo.toString())
					.setPositiveButton("OK", null).show();
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("")
					.setMessage("Failed to get the information.")
					.setPositiveButton("OK", null).show();
		}
	}

	private void openDoor()
	{
		int iret = mFridgeDoor.openDoor(1);
		if (iret == LibError.ERR_OK)
		{
			new AlertDialog.Builder(this).setTitle("")
					.setMessage("Open the door successfully.")
					.setPositiveButton("OK", null).show();
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("")
					.setMessage("Failed to open the door.")
					.setPositiveButton("OK", null).show();
		}
	}

    private boolean openLock()
    {
        int response = mFridgeDoor.openDoor(1);
        if (response == LibError.ERR_OK)
        {
            LOGGER.log(Level.INFO, "Open the door successfully.");
            return true;
        }
        else
        {
            LOGGER.log(Level.INFO, "Failed to open the door.");
            return false;
        }
    }

	@SuppressLint("DefaultLocale")
	private void printDoorStatus()
	{
		byte[] alarmFlg = new byte[2];
		byte[] doorFlg = new byte[2];
		//byte[] passingFlg = new byte[2];
		//float[] temperature = new float[2];
		int iret = mFridgeDoor.getStatus((byte) 1, doorFlg,alarmFlg, null,
				null);
		if (iret != LibError.ERR_OK)
		{
			new AlertDialog.Builder(this).setTitle("")
					.setMessage("Failed to get the door's status.")
					.setPositiveButton("OK", null).show();
			return;
		}
		String info = "";
		if (doorFlg[0]==0)
		{
			info = "The door is closed.";
		}
		else {
			info = "The door is open.";
		}
		
		if (alarmFlg[0]!=0)
		{
			info+="\nThe door is alarming.";
		}

		new AlertDialog.Builder(this).setTitle("").setMessage(info)
				.setPositiveButton("OK", null).show();
	}

    public boolean getLockStatus() throws Exception {
        byte[] alarmFlg = new byte[2];
        byte[] doorFlg = new byte[2];
        //byte[] passingFlg = new byte[2];
        //float[] temperature = new float[2];
        int status = mFridgeDoor.getStatus((byte) 1, doorFlg,alarmFlg, null, null);
        if (status != LibError.ERR_OK)
            throw new Exception("Failed to get the door's status.");
        if (doorFlg[0]==0)
        {
            LOGGER.log(Level.INFO, "The door is closed.");
            //originale tornava true
            return false;
        }
        else {
            LOGGER.log(Level.INFO, "The door is open.");
            return true;
        }
    }

    //public boolean read
    private void sendTag() {
        Vector<ISO15693Tag> tagList=inventory.readTagsList(m_reader);
        String[] codes = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            String code = Base64.encodeToString(tagList.get(i).uid, Base64.DEFAULT);
            codes[i] = code;
            tv.append("\ncode: " + code);
        }
        // System.out.println("element:" + inventory.vectorISO15693TagToJsonObject(tagList));
        scrollView.scrollTo(0, 0);
        remoteProxy.postInventory(inventory.vectorISO15693TagToJsonObject(tagList));
    }

	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_connect:
			Connect();
			break;
		case R.id.btn_disconnect:
			Disconnect();
			break;
		case R.id.btn_information:
			Information();
			break;
		case R.id.btn_open_door:
			openDoor();
			break;
		case R.id.btn_get_status:
			printDoorStatus();
			break;
        case R.id.btn_enable_polling:
                Polling polling = new Polling();
                pollingRunning=true;
                polling.start();
                UiVisible(true);
            break;
        case R.id.btn_connect2:
                this.connect2();
                break;
        case R.id.btn_read_tags:
            Vector<ISO15693Tag> tagList=inventory.readTagsList(m_reader);
            String[] codes = new String[tagList.size()];
            for (int i = 0; i < tagList.size(); i++) {
                String code = Base64.encodeToString(tagList.get(i).uid, Base64.DEFAULT);
                codes[i] = code;
                tv.append("\ncode: " + code);
            }
            // System.out.println("element:" + inventory.vectorISO15693TagToJsonObject(tagList));
            scrollView.scrollTo(0, 0);
            remoteProxy.postInventory(inventory.vectorISO15693TagToJsonObject(tagList));
            break;
        case R.id.btn_get_info:
                this.getInformation();
                break;
        case R.id.btn_stop_polling:
                stopPolling();
                break;
        case R.id.btn_read_logs:
                this.readLog();
                break;
        case R.id.btn_clear_logs:
            this.clearLog();
            break;
        case R.id.test_api:
            this.testApi();
            break;
		default:
			break;
		}
	}

    public void testApi() {
        LOGGER.log(Level.INFO, "test api!!!");
        remoteProxy.postInventory3("", tv);
    }

    private class Polling extends Thread {
        public void run() {
            boolean lock=remoteProxy.checkLockStatus();
            while(pollingRunning){
                try {
                    Thread.sleep(1000);
                    //System.out.println("************** RESTA CHIUSO **************\n");
                    lock=remoteProxy.checkLockStatus();
                    System.out.println("LOCK STATUS: "+lock+"\n");
                    if(!lock) { // così il frigo è aperto
                        //System.out.println("************** APRI FRIGO **************\n");
                        //openlock controlla l'apertura se è false va nell'else e esegue setlockstatus
                        if(openLock()) {
                            //System.out.println("************** FRIGO APERTO **************\n");
                            //TODO togliere pausa 3s e esempio "prendo da frigo"
                            //System.out.println("PRENDO ROBA DAL FRIGO......\n");
                            Thread.sleep(3000);
                            //System.out.println("FINITO, CHIUDO FRIGO FRIGO......\n");
                            while(getLockStatus()){//polling che verifica se il frigo è chiuso
                                System.out.println("ATTENDO CHIUSURA FRIGO......\n");
                                Thread.sleep(2000);
                            }
                            //System.out.println("HO CHIUSO IL FRIGO, MANDO ELENCO PRODOTTI.....\n");
                            sendTag();
                        }
                        else
                            System.out.println("************** PROBLEMA APERTURA FRIGO **************\n");

                        //If I'm here the fridge is closed, I communicate it to the server API
                        //provare a commentare la riga sotto. è lei che setta la chiusura del frigo
                        remoteProxy.setLockStatus();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            System.out.println("************** STOP POLLING **************\n");
        }
    }
}


