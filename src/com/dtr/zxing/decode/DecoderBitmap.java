package com.dtr.zxing.decode;

import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/***
 * decoder resouce bitmap
 * 
 * @author
 */
public class DecoderBitmap {
	MultiFormatReader multiFormatReader;

	public DecoderBitmap(Context context) {
		multiFormatReader = new MultiFormatReader();
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
			decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
			decodeFormats.addAll(EnumSet.of(BarcodeFormat.AZTEC));
			decodeFormats.addAll(EnumSet.of(BarcodeFormat.PDF_417));
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
		multiFormatReader.setHints(hints);

	}

	public Result getRawResult(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		try {
			return multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
