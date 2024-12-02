package com.example.event_lottery;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrganizerEventTestHelper { public Map<String, Object> createEventData(String eventName, Date eventDateTime, String capacity, String price, String description, boolean geolocationEnabled, String imagePath) {
    Map<String, Object> eventData = new HashMap<>();
    eventData.put("eventName", eventName);
    eventData.put("eventDateTime", eventDateTime);
    eventData.put("capacity", capacity);
    eventData.put("price", price);
    eventData.put("description", description);
    eventData.put("geolocationEnabled", geolocationEnabled);
    eventData.put("imagePath", imagePath);
    return eventData;
}

    public boolean validateEventData(String eventName, Date eventDateTime, String capacity, String price, String description) {
        return eventName != null && !eventName.isEmpty() &&
                eventDateTime != null &&
                capacity != null && !capacity.isEmpty() &&
                price != null && !price.isEmpty() &&
                description != null && !description.isEmpty();
    }
    public Bitmap generateQRCode(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}