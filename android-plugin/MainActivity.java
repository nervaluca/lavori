package it.iw1fzr.lavori;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

import it.iw1fzr.lavori.plugins.OcrPlugin;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(OcrPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
