package org.wonday.pdf;

import android.content.Context;
import android.widget.LinearLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Created by muralids on 5/16/18.
 */

public class PdfMainView extends LinearLayout{


    public PdfMainView(Context context) {
        super(context);
    }
    public void onPageChanged(int page,int totalPages){
        WritableMap event = Arguments.createMap();
        event.putString("message", "pageChanged|"+page+"|"+totalPages);
        ReactContext reactContext = (ReactContext)this.getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                this.getId(),
                "topChange",
                event
        );
    }

    public void showReader(String url) {

        WritableMap event = Arguments.createMap();
        event.putString("message", "showReader|"+url);
        ReactContext reactContext = (ReactContext)this.getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                this.getId(),
                "topChange",
                event
        );
    }
    public void backButtonClick(){
        WritableMap event = Arguments.createMap();
        event.putString("message", "onBack|");
        ReactContext reactContext = (ReactContext)this.getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                this.getId(),
                "topChange",
                event
        );
    }
}
