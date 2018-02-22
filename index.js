/**
* Copyright (c) 2017-present, Wonday (@wonday.org)
* All rights reserved.
*
* This source code is licensed under the MIT-style license found in the
* LICENSE file in the root directory of this source tree.
*/

'use strict';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    ActivityIndicator,
    requireNativeComponent,
    View,
    Platform,
    ProgressBarAndroid,
    ProgressViewIOS,
    ViewPropTypes
} from 'react-native';

const SHA1 = require('crypto-js/sha1');
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';
import PdfView from './PdfView';

export default class Pdf extends Component {


    static propTypes = {
        ...ViewPropTypes,
        path: PropTypes.string,
        source: PropTypes.oneOfType([
            PropTypes.shape({
                uri: PropTypes.string,
                cache: PropTypes.bool,
            }),
            // Opaque type returned by require('./test.pdf')
            PropTypes.number,
        ]).isRequired,
        page: PropTypes.number,
        scale: PropTypes.number,
        horizontal: PropTypes.bool,
        spacing: PropTypes.number,
        password: PropTypes.string,
        progressBarColor: PropTypes.string,
        activityIndicator: PropTypes.any,
        activityIndicatorProps: PropTypes.any,
        enableAntialiasing: PropTypes.bool,
        fitPolicy: PropTypes.number,
        onLoadComplete: PropTypes.func,
        onPageChanged: PropTypes.func,
        onError: PropTypes.func,
        onPageSingleTap: PropTypes.func,
        onScaleChanged: PropTypes.func,
    };

    static defaultProps = {
        password: "",
        scale: 1,
        spacing: 10,
        fitPolicy: 2, //fit both
        horizontal: false,
        page: 1,
        activityIndicatorProps: {color:'#009900',progressTintColor:'#009900'},
        onLoadProgress: (percent) => { },
        onLoadComplete: (numberOfPages, path) => { },
        onPageChanged: (page, numberOfPages) => { },
        onError: (error) => { },
        onPageSingleTap: (page) => { },
        onScaleChanged: (scale) => { },
    };
    constructor(props) {

        super(props);
        this.state = {
            path: '',
            isDownloaded: false,
            progress: 0,
       };

        this.uri = '';
        this.lastRNBFTask = null;

    }

    componentWillReceiveProps(nextProps) {

        if (nextProps.source !== this.props.source) {
            //__DEV__ && console.log("componentWillReceiveProps: source changed");
            this._loadFromSource(nextProps.source);
        }

    }

    componentDidMount() {

        this._loadFromSource(this.props.source);

    }

    componentWillUnmount() {

        if (this.lastRNBFTask) {
            this.lastRNBFTask.cancel(err => {
                //__DEV__ && console.log("Load pdf from url was cancelled.");
            });
            this.lastRNBFTask = null;
        }

    }

    _loadFromSource = (newSource) => {


    };

    _prepareFile = (source) => {

    };

    _downloadFile = (source, tempCacheFile, cacheFile) => {

    };

    setNativeProps = nativeProps => {

        this._root.setNativeProps(nativeProps);

    };

    _onChange = (event) => {


    };
    _onError = (error) => {

        this.props.onError && this.props.onError(error);

    }

    render() {

        if (!this.state.isDownloaded) {
            return (
                <View
                    style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}
                >
                    {this.props.activityIndicator
                        ? this.props.activityIndicator
                        : Platform.OS === 'android'
                            ? <ProgressBarAndroid
                                progress={this.state.progress}
                                indeterminate={false}
                                styleAttr="Horizontal"
                                style={{ width: 200, height: 2 }}
                                {...this.props.activityIndicatorProps}
                            />
                            : <ProgressViewIOS
                                progress={this.state.progress}
                                style={{ width: 200, height: 2 }}
                                {...this.props.activityIndicatorProps}
                            />}
                </View>
            )
        } else {
            if (Platform.OS === "android") {
                return (
                    <PdfCustom
                        ref={component => (this._root = component)}
                        {...this.props}
                        style={[{ backgroundColor: '#EEE' }, this.props.style]}
                        path={this.state.path}
                        onChange={this._onChange}
                    />
                );
            } else if (Platform.OS === "ios") {
                return (
                    <PdfView
                        {...this.props}
                        style={[{ backgroundColor: '#EEE' }, this.props.style]}
                        path={this.state.path}
                        onLoadComplete={this.props.onLoadComplete}
                        onPageChanged={this.props.onPageChanged}
                        onError={this._onError}
                        onPageSingleTap={this.props.onPageSingleTap}
                        onScaleChanged={this.props.onScaleChanged}
                    />
                );
            } else {
                return (null);
            }
        }

    }
}


if (Platform.OS === "android") {
    var PdfCustom = requireNativeComponent('RCTPdf', Pdf, {
        nativeOnly: { path: true, onChange: true },
    })
}
