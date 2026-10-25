package it.iw1fzr.lavori.plugins;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Base64;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/**
 * Lettura testo (OCR) con Google ML Kit, tutto sul telefono, senza rete.
 * JS: Capacitor.Plugins.Ocr.recognize({ base64: "data:image/jpeg;base64,..." })
 * Ritorna: { text, width, height, lines: [ { text, l, t, r, b } ] }
 */
@CapacitorPlugin(name = "Ocr")
public class OcrPlugin extends Plugin {

    @PluginMethod
    public void recognize(PluginCall call) {
        String b64 = call.getString("base64");
        if (b64 == null || b64.isEmpty()) {
            call.reject("Immagine mancante");
            return;
        }
        int comma = b64.indexOf(',');
        if (comma >= 0) b64 = b64.substring(comma + 1);

        final Bitmap bmp;
        try {
            byte[] data = Base64.decode(b64, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            call.reject("Immagine non leggibile: " + e.getMessage());
            return;
        }
        if (bmp == null) {
            call.reject("Immagine non leggibile");
            return;
        }

        final TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(InputImage.fromBitmap(bmp, 0))
            .addOnSuccessListener(result -> {
                JSArray lines = new JSArray();
                for (Text.TextBlock block : result.getTextBlocks()) {
                    for (Text.Line line : block.getLines()) {
                        Rect r = line.getBoundingBox();
                        JSObject o = new JSObject();
                        o.put("text", line.getText());
                        if (r != null) {
                            o.put("l", r.left);
                            o.put("t", r.top);
                            o.put("r", r.right);
                            o.put("b", r.bottom);
                        }
                        lines.put(o);
                    }
                }
                JSObject ret = new JSObject();
                ret.put("text", result.getText());
                ret.put("width", bmp.getWidth());
                ret.put("height", bmp.getHeight());
                ret.put("lines", lines);
                recognizer.close();
                call.resolve(ret);
            })
            .addOnFailureListener(e -> {
                recognizer.close();
                call.reject("Lettura non riuscita: " + e.getMessage());
            });
    }
}
