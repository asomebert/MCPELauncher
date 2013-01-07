package com.mojang.minecraftpe;

import java.io.File;
import java.io.InputStream;

import java.text.DateFormat;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.util.DisplayMetrics;
import android.widget.Toast;

import net.zhuoweizhang.mcpelauncher.TexturePack;
import net.zhuoweizhang.mcpelauncher.ZipTexturePack;



public class MainActivity extends NativeActivity
{

	public static final int INPUT_STATUS_IN_PROGRESS = 0;

	public static final int INPUT_STATUS_OK = 1;

	public static final int INPUT_STATUS_CANCELLED = 2;

	public static final int DIALOG_CREATE_WORLD = 0;

	public static final int DIALOG_SETTINGS = 3;

	protected DisplayMetrics displayMetrics;

	protected TexturePack texturePack;

	protected Context minecraftApkContext;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("oncreate");
		try {
			System.load("/data/data/com.mojang.minecraftpe/lib/libminecraftpe.so");
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Can't load libminecraftpe.so from the original APK", Toast.LENGTH_LONG).show();
			finish();
		}
		nativeRegisterThis();

		displayMetrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		super.onCreate(savedInstanceState);

		try {
			texturePack = new ZipTexturePack(new File(Environment.getExternalStorageDirectory(), "tex.zip"));
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "No tex.zip found!", Toast.LENGTH_LONG).show();
			finish();
		}
		
		try {
			minecraftApkContext = createPackageContext("com.mojang.minecraftpe", 0);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Can't create package context for the original APK", Toast.LENGTH_LONG).show();
			finish();
		}
		//setContentView(R.layout.main);
	}

	public void onStart() {
		super.onStart();
	}

	public native void nativeRegisterThis();
	public native void nativeUnregisterThis();

	public void buyGame() {
	}

	public int checkLicense() {
		System.err.println("CheckLicense");
		return 0;
	}

	public void displayDialog(int dialogId) {
		System.out.println("displayDialog: " + dialogId);
		switch (dialogId) {
			case DIALOG_CREATE_WORLD:
				System.out.println("World creation");
				break;
			case DIALOG_SETTINGS:
				System.out.println("Settings");
				Intent intent = new Intent(this, MainMenuOptionsActivity.class);
				startActivityForResult(intent, 1234);
				break;
		}
	}

	/**
	 * @param time Unix timestamp
	 * @returns a formatted time value
	 */

	public String getDateString(int time) {
		System.out.println("getDateString: " + time);
		return DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(new Date(((long) time) * 1000));
	}

	public byte[] getFileDataBytes(String name) {
		System.out.println("Get file data: " + name);
		try {
			InputStream is = getInputStreamForAsset(name);
			if (is == null) return null;
			byte[] retval = new byte[(int) getSizeForAsset(name)];
			is.read(retval);
			return retval;
		} catch (Exception e) {
			return null;
		}
	}

	private InputStream getInputStreamForAsset(String name) {
		try {
			InputStream is = texturePack.getInputStream(name);
			if (is == null) {
				is = minecraftApkContext.getAssets().open(name);
			}
			return is;
		} catch (Exception e) {
			return null;
		}
	}

	private long getSizeForAsset(String name) {
		try {
			long size = texturePack.getSize(name);
			if (size == -1) {
				size = minecraftApkContext.getAssets().openFd(name).getLength();
			}
			return size;
		} catch (Exception e) {
			return -1;
		}
	}

	public int[] getImageData(String name) {
		System.out.println("Get image data: " + name);
		try {
			InputStream is = getInputStreamForAsset(name);
			if (is == null) return null;
			Bitmap bmp = BitmapFactory.decodeStream(is);
			int[] retval = new int[(bmp.getWidth() * bmp.getHeight()) + 2];
			retval[0] = bmp.getWidth();
			retval[1] = bmp.getHeight();
			bmp.getPixels(retval, 2, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
			is.close();

			return retval;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/* format: width, height, each integer a pixel */
		/* 0 = white, full transparent */
	}

	public String[] getOptionStrings() {
		System.err.println("OptionStrings");
		return new String[] {"zebra"};
	}

	public float getPixelsPerMillimeter() {
		System.out.println("Pixels per mm");
		return ((float) displayMetrics.densityDpi) / 25.4f ;
	}

	public String getPlatformStringVar(int a) {
		System.out.println("getPlatformStringVar: " +a);
		return "";
	}

	public int getScreenHeight() {
		System.out.println("height");
		return displayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		System.out.println("width");
		return displayMetrics.widthPixels;
	}

	public int getUserInputStatus() {
		System.out.println("User input status");
		return INPUT_STATUS_CANCELLED;
	}

	public String[] getUserInputString() {
		System.out.println("User input string");
		/* for the seed input: name, world type, seed */
		return new String[] {"elephant", "potato", "strawberry"};
	}

	public boolean hasBuyButtonWhenInvalidLicense() {
		return false;
	}

	public void initiateUserInput(int a) {
		System.out.println("initiateUserInput: " + a);
	}

	public boolean isNetworkEnabled(boolean a) {
		System.out.println("Network?:" + a);
		return true;
	}


	public boolean isTouchscreen() {
		System.err.println("Touchscreen");
		return true;
	}

	public void postScreenshotToFacebook(String name, int firstInt, int secondInt, int[] thatArray) {
	}

	public void quit() {
	}

	public void setIsPowerVR(boolean powerVR) {
		System.out.println("PowerVR: " + powerVR);
	}

	public void tick() {
	}

	public void vibrate(int duration) {
	}	

	public static void saveScreenshot(String name, int firstInt, int secondInt, int[] thatArray) {
	}

}
