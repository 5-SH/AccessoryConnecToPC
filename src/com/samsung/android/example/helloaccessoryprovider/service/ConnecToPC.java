package com.samsung.android.example.helloaccessoryprovider.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ConnecToPC {

	public static final String TAG_BLUETOOTH = "MyBluetooth";
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	public void enabledBluetoothAdapter() {

		if (mBluetoothAdapter.isEnabled()) {
			Log.e(TAG_BLUETOOTH, "mBluetoothAdapter is enabled");

			// get the paired devices
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();
			for (BluetoothDevice pairedDevice : pairedDevices) {

				Log.e(TAG_BLUETOOTH, pairedDevice.getName());
			}
		}

		else {
			Log.e(TAG_BLUETOOTH, "mBluetoothAdapter is NOT enabled");
		}

	}

	public void transferToDevice() {

		// UUID 설정 (SPP)
		UUID SPP_UUID = java.util.UUID
				.fromString("00001101-0000-1000-8000-00805F9B34FB");
		String deviceName = "QUKI";
		/*
		 * 클릭해서 가져온 Device의 String 값과 페어링 된 것이 일치하는지 한번 더 확인 후 BluetoothDevice
		 * 객체로 target저장
		 */
		BluetoothDevice targetDevice = null;
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		for (BluetoothDevice pairedDevice : pairedDevices) {
			if (pairedDevice.getName().equals(deviceName)) {
				targetDevice = pairedDevice;
				Log.e(TAG_BLUETOOTH, targetDevice.toString());
				break;
			}
		}

		// If the device was not found, toast an error and return
		if (targetDevice == null) {
			// Toast.makeText(getApplicationContext(),
			// "Cannot find any devices", Toast.LENGTH_SHORT).show();
			return;
		}

		// Create a connection to the device with the SPP UUID
		BluetoothSocket mBluetoothSocket = null;
		try {
			mBluetoothSocket = targetDevice
					.createRfcommSocketToServiceRecord(SPP_UUID);
		} catch (IOException e) {
			// Toast.makeText(this,
			// "Unable to open a serial socket with the device",
			// Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}

		// Connect to the device
		try {
			mBluetoothSocket.connect();
		} catch (IOException e) {
			// Toast.makeText(this, "Unable to connect with the device",
			// Toast.LENGTH_SHORT).show();
			Log.e(TAG_BLUETOOTH, "Unable to connect with the device");
			e.printStackTrace();
			return;
		}

		// Write the data by using OutputStreamWriter
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					mBluetoothSocket.getOutputStream());
			outputStreamWriter.write("hello");
			outputStreamWriter.flush();
			Log.e(TAG_BLUETOOTH, "--------------Write--------------");
		} catch (IOException e) {
			// Toast.makeText(this, "Unable to send message to the device",
			// Toast.LENGTH_SHORT).show();
			Log.e(TAG_BLUETOOTH,
					"--------------Unable to send message to the device--------------");
			e.printStackTrace();
		}

		try {
			mBluetoothSocket.close();
			Log.e(TAG_BLUETOOTH, "--------------Close--------------");
		} catch (IOException e) {
			// Toast.makeText(this, "Fail to close the connection to device",
			// Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
