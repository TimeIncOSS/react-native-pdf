/**
 * Copyright (c) 2017-present, Wonday (@wonday.org)
 * All rights reserved.
 *
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.wonday.pdf;

import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.util.Log;
import android.graphics.PointF;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.SystemClock;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static java.lang.String.format;
import java.lang.ClassCastException;

import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.warkiz.widget.IndicatorSeekBar;


public class RCTPdfManager extends SimpleViewManager<PdfMainView> implements PdfLoadActions {
    private static final String REACT_CLASS = "RCTPdf";
    private Context context;
    private PdfView pdfView;
    private PdfMainView pdfMainView;
    private int totalPages,currentPageNumber;
    private IndicatorSeekBar indicatorSeekBar;

    public RCTPdfManager(ReactApplicationContext reactContext){
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public PdfMainView createViewInstance(ThemedReactContext context) {
        this.pdfMainView = new PdfMainView(context);
        this.pdfView = new PdfView(context,null,this);


        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        this.pdfMainView.setLayoutParams(parentLayoutParams);
        this.pdfMainView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout wrapLinearLayout = new LinearLayout(context);
        wrapLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,9.5f));
        RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.pdfView.setLayoutParams(params);

        wrapLinearLayout.addView(this.pdfView);
        this.pdfMainView.addView(wrapLinearLayout);
        int totalPages = this.pdfView.getPageCount();
        int currentPageNumber = this.pdfView.getCurrentPage();
        System.out.println("total pages"+totalPages);
        System.out.println("Current page"+currentPageNumber);

        this.indicatorSeekBar = new IndicatorSeekBar.Builder(context).setMin(1)
                .setBackgroundTrackSize(7)
                .setIndicatorColor(Color.GRAY)
//                .setProgress(this.currentPageNumber)
                .setProgressTrackColor(Color.BLACK)
                .setThumbColor(Color.BLACK)
                .build();
        LinearLayout.LayoutParams seekBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1.0f);
        seekBarLayoutParams.setMargins(0,10,0,0);
        indicatorSeekBar.setLayoutParams(seekBarLayoutParams);
        this.pdfMainView.addView(indicatorSeekBar);



        indicatorSeekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                navigateToPage(progress);

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        return this.pdfMainView;
    }
    public  void navigateToPage(int pageNumber) {
        this.pdfView.jumpTo(pageNumber-1);
    }
    @Override
    public void onDropViewInstance(PdfMainView PdfMainView) {

        this.pdfView = null;
    }

    @ReactProp(name = "path")
    public void setPath(PdfMainView pdfView, String path) {
        this.pdfView.setPath(path);
    }

    // page start from 1
    @ReactProp(name = "page")
    public void setPage(PdfMainView pdfView, int page) {
        System.out.println("last viewed page number setPage "+ page);
        this.pdfView.setPage(page);
    }

    @ReactProp(name = "scale")
    public void setScale(PdfMainView pdfView, float scale) {
        this.pdfView.setScale(scale);
    }

    @ReactProp(name = "horizontal")
    public void setHorizontal(PdfMainView pdfView, boolean horizontal) {
        this.pdfView.setHorizontal(horizontal);
    }

    @ReactProp(name = "spacing")
    public void setSpacing(PdfMainView pdfView, int spacing) {
        this.pdfView.setSpacing(spacing);
    }

    @ReactProp(name = "password")
    public void setPassword(PdfMainView pdfView, String password) {
        this.pdfView.setPassword(password);
    }

    @ReactProp(name = "enableAntialiasing")
    public void setEnableAntialiasing(PdfMainView pdfView, boolean enableAntialiasing) {
        this.pdfView.setEnableAntialiasing(enableAntialiasing);
    }

    @ReactProp(name = "fitPolicy")
    public void setFitPolycy(PdfMainView pdfView, int fitPolicy) {
        this.pdfView.setFitPolicy(fitPolicy);
    }

    @Override
    public void onAfterUpdateTransaction(PdfMainView pdfView) {
        super.onAfterUpdateTransaction(pdfView);
        this.pdfView.drawPdf();
    }

    @Override
    public void pdfLoadFinished() {
        this.totalPages = this.pdfView.getPageCount();
        this.currentPageNumber = this.pdfView.getCurrentPageNumber();
        this.indicatorSeekBar.getBuilder().setMax(this.totalPages).setProgress(this.pdfView.getCurrentPageNumber()).apply();
    }

}
