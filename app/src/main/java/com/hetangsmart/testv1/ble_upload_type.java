package com.hetangsmart.testv1;

import android.content.Intent;
import android.util.Log;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jasonbu on 2015/11/13.
 */
public class ble_upload_type {
    private static final String TAG = "UploadModule";
    private enum upload_state_enum_t {
        dis_connected,
        discover_finished,
        waiting_ble,
    }

    private upload_state_enum_t state_upload = upload_state_enum_t.dis_connected;

    private static final byte default_byte_fill = 0x00;
    private static final byte cmd_start = 0x04;
    private static final byte cmd_stop = 0x05;

//    public static final String command_start_str = "0400000000000000000000000000000000000000";
//    public static final String command_stop_str = "0500000000000000000000000000000000000000";


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

    private void on_data_rx(byte[] rx)
    {
        switch (state_upload)
        {
            case dis_connected:
                break;
            case discover_finished:
                break;
            case waiting_ble:
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
