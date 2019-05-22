package com.hpifive.line.bcs.webhook.invoice;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.oned.MultiFormatOneDReader;

public class BarcodeDetector {
	
	private BarcodeDetector() {}

	public static Result[] decodeQRCode(BufferedImage image) throws NotFoundException {
		return decodeQRCode(image, Boolean.FALSE);
	}

	public static Result[] decodeQRCode(BufferedImage image, Boolean isPureBarcode) throws NotFoundException {
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		EnumMap<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
		List<BarcodeFormat> formats = new ArrayList<>();
		formats.add(BarcodeFormat.QR_CODE);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hints.put(DecodeHintType.PURE_BARCODE, isPureBarcode);
		hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);

		MultipleBarcodeReader reader = new QRCodeMultiReader();
		return reader.decodeMultiple(bitmap, hints);
	}

	public static Result[] decodeCode39(BufferedImage image) throws NotFoundException {
		return decodeCode39(image, Boolean.FALSE);
	}

	public static Result[] decodeCode39(BufferedImage image, Boolean isPureBarcode) throws NotFoundException {
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		EnumMap<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class); 
		List<BarcodeFormat> formats = new ArrayList<>();
		formats.add(BarcodeFormat.CODE_39);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hints.put(DecodeHintType.PURE_BARCODE, isPureBarcode);
		hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);

		MultipleBarcodeReader reader = new GenericMultipleBarcodeReader(new MultiFormatOneDReader(hints));
		return reader.decodeMultiple(bitmap, hints);
	}

}
