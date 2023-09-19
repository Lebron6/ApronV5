package com.aros.apron.tools;

import android.graphics.Bitmap;

public class YUV2BitmapUtils {
//    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
//        int frameSize = width * height;
//        int[] rgba = new int[frameSize];
//        for (int i = 0; i < height; i++)
//            for (int j = 0; j < width; j++) {
//                int y = (0xff & ((int) data[i * width + j]));
//                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
//                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
//                y = y < 16 ? 16 : y;
//                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
//                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
//                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
//                r = r < 0 ? 0 : (r > 255 ? 255 : r);
//                g = g < 0 ? 0 : (g > 255 ? 255 : g);
//                b = b < 0 ? 0 : (b > 255 ? 255 : b);
//                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
//            }
//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        bmp.setPixels(rgba, 0 , width, 0, 0, width, height);
//        return bmp;
//    }

    /**
     * @param src 是原始yuv数组
     * @param w 原始图片的宽
     * @param h 原始图片的高
     **/
    public static void Mirror(byte[] src, int w, int h) {
        int i;
        int index;
        byte temp;
        int a, b;
        //mirror y
        for (i = 0; i < h; i++) {
            a = i * w;
            b = (i + 1) * w - 1;
            while (a < b) {
                temp = src[a];
                src[a] = src[b];
                src[b] = temp;
                a++;
                b--;
            }
        }

        // mirror u and v
        index = w * h;
        for (i = 0; i < h / 2; i++) {
            a = i * w;
            b = (i + 1) * w - 2;
            while (a < b) {
                temp = src[a + index];
                src[a + index] = src[b + index];
                src[b + index] = temp;

                temp = src[a + index + 1];
                src[a + index + 1] = src[b + index + 1];
                src[b + index + 1] = temp;
                a += 2;
                b -= 2;
            }
        }
    }

    /**
     * :yuv转bitmap
     *
     * @author: Charles-lun
     * creat at: 2021/5/19 10:15
     */

    public static Bitmap nv21ToBitmap(byte[] data, int w, int h) {
        return spToBitmap(data, w, h, 1, 0);
    }

    private static Bitmap spToBitmap(byte[] data, int w, int h, int uOff, int vOff) {
        int plane = w * h;
        int[] colors = new int[plane];
        int yPos = 0, uvPos = plane;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                // YUV byte to RGB int
                final int y1 = data[yPos] & 0xff;
                final int u = (data[uvPos + uOff] & 0xff) - 128;
                final int v = (data[uvPos + vOff] & 0xff) - 128;
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);
                colors[yPos] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);

                if ((yPos++ & 1) == 1) uvPos += 2;
            }
            if ((j & 1) == 0) uvPos -= w;
        }
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
    }


}
