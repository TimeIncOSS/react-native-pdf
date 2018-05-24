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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.graphics.PointF;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
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
import java.util.HashMap;

import com.github.barteksc.pdfviewer.util.FitPolicy;



public class RCTPdfManager extends SimpleViewManager<PdfMainView> implements PdfLoadActions {
    private static final String REACT_CLASS = "RCTPdf";
    private Context context;
    private PdfView pdfView;
    private PdfMainView pdfMainView;
    private int totalPages,currentPageNumber;
    private HashMap<String,Object> articlesMap;
    private TextView readerModeButton;
    private ImageButton backButton;
    private  String titleText;
    private TextView leftPageIndicator,rightPageIndicator,titleView;


    private CrystalSeekbar crystalSeekbar;

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


        // Header View in Pdfview
        RelativeLayout relativeLayoutHeaderView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.header_layout,this.pdfMainView,false);
        this.pdfMainView.addView(relativeLayoutHeaderView);

        readerModeButton = (TextView) relativeLayoutHeaderView.findViewById(R.id.reader_button);
        backButton = (ImageButton) relativeLayoutHeaderView.findViewById(R.id.backButton);
        titleView = (TextView) relativeLayoutHeaderView.findViewById(R.id.titleView);
        titleView.setText(titleText);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfMainView.backButtonClick();
            }
        });
        //

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


        LinearLayout.LayoutParams seekBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1.0f);
        seekBarLayoutParams.setMargins(0,10,0,0);


        RelativeLayout seekBarLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.seek_bar_layout,this.pdfMainView,false);
        this.crystalSeekbar = (CrystalSeekbar) seekBarLayout.findViewById(R.id.crystal_bar);

        this.pdfMainView.addView(seekBarLayout);

        leftPageIndicator = (TextView)seekBarLayout.findViewById(R.id.current_page);
        rightPageIndicator = (TextView)seekBarLayout.findViewById(R.id.totol_page);

        readerModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String)articlesMap.get(String.valueOf(getCurrentPageNumber()));
                pdfMainView.showReader(url);
            }
        });
        this.crystalSeekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                navigateToPage(value.intValue());
            }
        });
        return this.pdfMainView;
    }

    public int getCurrentPageNumber() {
        return  this.currentPageNumber;
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
    @ReactProp(name = "title")
    public void setPage(PdfMainView pdfView, String title) {
        System.out.println("title of the page is  "+ title);
        this.titleText = title;
        titleView.setText(title);
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
    //articleUrlMap
    @ReactProp(name = "articleUrlMap")
    public void setFitPolycy(PdfMainView pdfView, ReadableMap articleUrlMap) {
        this.articlesMap = articleUrlMap.toHashMap();



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
        this.crystalSeekbar.setMinStartValue(this.pdfView.getCurrentPageNumber());
        this.crystalSeekbar.setMaxValue(this.totalPages);
        this.rightPageIndicator.setText(String.valueOf(this.totalPages));
        this.leftPageIndicator.setText(String.valueOf(this.currentPageNumber));
    }

    @Override
    public void onPageChanged(int page, int totalPages) {
        this.pdfMainView.onPageChanged(page,totalPages);
        this.totalPages = totalPages;
        this.currentPageNumber = page;
        this.leftPageIndicator.setText(String.valueOf(this.currentPageNumber));

        if(articlesMap.containsKey(String.valueOf(currentPageNumber))){
            readerModeButton.setVisibility(View.VISIBLE);
        }
        else {
            readerModeButton.setVisibility(View.INVISIBLE);
        }
        this.crystalSeekbar.setMinStartValue(page).apply();
    }


}
