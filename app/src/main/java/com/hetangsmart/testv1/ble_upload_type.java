package com.hetangsmart.testv1;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Array;
import java.math.BigInteger;

/**
 * Created by jasonbu on 2015/11/13.
 */
public class ble_upload_type {
    private static final String TAG = "UploadModule";


    private DBManager _dm;
    public enum upload_state_enum_t {
        dis_connected,
        discover_finished,
        waiting_ble,
    }

    public upload_state_enum_t state_upload = upload_state_enum_t.dis_connected;

    private static final byte default_byte_fill = 0x00;
    private static final byte cmd_start = 0x04;
    private static final byte cmd_stop = 0x05;

//    public static final String command_start_str = "0400000000000000000000000000000000000000";
//    public static final String command_stop_str = "0500000000000000000000000000000000000000";


    ble_upload_type(){

    }

    ble_upload_type(DBManager dm){
        this._dm = dm;
    }
    public byte[] CommandDataForStart()
    {
        byte[] bytes = new byte[20];
        for(byte b : bytes){
            b = default_byte_fill;
        }
        bytes[0] = cmd_start;
        state_upload = upload_state_enum_t.waiting_ble;
        return bytes;
    }
    public byte[] CommandDataForStop()
    {
        byte[] bytes = new byte[20];
        for(byte b : bytes){
            b = default_byte_fill;
        }
        bytes[0] = cmd_stop;
        state_upload = upload_state_enum_t.discover_finished;
        return bytes;
    }

    public byte[] ConvertFromStringToByteArr(String s)
    {
        byte[] bytes = null;
        try {
            bytes = new BigInteger(s,16).toByteArray();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            return bytes;
        }
    }

    public  String ConvertFromByteArrToString(byte[] bytes)
    {
        String text = null;
        try {
            BigInteger bi = new BigInteger(bytes);
            text = bi.toString(16);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            return text;
        }
    }

    private int convertFromByte16ToInt(byte[] b2)
    {
        BigInteger bi = new BigInteger(b2);
        int target = bi.intValue();
        Log.d(TAG,"target:" + target);
        return target;
    }
    private int convertFromByte32ToInt(byte[] b4)
    {
        BigInteger bi = new BigInteger(b4);
        int target = bi.intValue();
        Log.d(TAG,"target:" + target);
        return target;
    }

    private void on_xyz_recieve(byte[] data)
    {
        DBAccelerationType d1 = new DBAccelerationType();
        DBAccelerationType d2 = new DBAccelerationType();

        byte[] stamp1 = new byte[4];
        byte[] x1 = new byte[2];
        byte[] y1 = new byte[2];
        byte[] z1 = new byte[2];
        byte[] stamp2 = new byte[4];
        byte[] x2 = new byte[2];
        byte[] y2 = new byte[2];
        byte[] z2 = new byte[2];

        stamp1[0] = data[3];
        stamp1[1] = data[2];
        stamp1[2] = data[1];
        stamp1[3] = data[0];
        x1[0] = data[5];
        x1[1] = data[4];
        y1[0] = data[7];
        y1[1] = data[6];
        z1[0] = data[9];
        z1[1] = data[8];
        stamp1[0] = data[13];
        stamp1[1] = data[12];
        stamp1[2] = data[11];
        stamp1[3] = data[10];
        x2[0] = data[15];
        x2[1] = data[14];
        y2[0] = data[17];
        y2[1] = data[16];
        z2[0] = data[19];
        z2[1] = data[18];

        d1.stamp = convertFromByte32ToInt(stamp1);
        d1.x = convertFromByte16ToInt(x1);
        d1.y = convertFromByte16ToInt(y1);
        d1.z = convertFromByte16ToInt(z1);
        d2.stamp = convertFromByte32ToInt(stamp2);
        d2.x = convertFromByte16ToInt(x2);
        d2.y = convertFromByte16ToInt(y2);
        d2.z = convertFromByte16ToInt(z2);

        if(d1.IsValid()) {
            this._dm.add(d1);
        }
        if(d2.IsValid()) {
            this._dm.add(d2);
        }
    }

    private void on_data_rx(byte[] rx)
    {
        switch (state_upload)
        {
            case dis_connected:
                break;
            case discover_finished:
                break;
            case waiting_ble:
                on_xyz_recieve(rx);
                break;
        }
    }


    public void on_recieve_evt_hook(Intent intent)
    {
        String action = intent.getAction();
        final Intent mIntent = intent;

        if (action.equals(HetangsmartFFC0Service.ACTION_GATT_SERVICES_DISCOVERED)) {
            state_upload = upload_state_enum_t.dis_connected;
        }

        if (action.equals(HetangsmartFFC0Service.ACTION_GATT_SERVICES_DISCOVERED)) {
            state_upload = upload_state_enum_t.discover_finished;
        }
        //*********************//
        if (action.equals(HetangsmartFFC0Service.ACTION_DATA_AVAILABLE)) {
            final byte[] txValue = intent.getByteArrayExtra(HetangsmartFFC0Service.EXTRA_DATA);
            this.on_data_rx(txValue);
        }
    }
}
