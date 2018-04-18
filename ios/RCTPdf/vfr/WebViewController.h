//
//  WebViewController.h
//  RCTPdf
//
//  Created by Claire Young on 2/20/18.
//  Copyright © 2018 wonday.org. All rights reserved.
//

#import <UIKit/UIKit.h>
#if __has_include(<React/RCTAssert.h>)
    #import <React/RCTEventEmitter.h>
#else
    #import "RCTEventEmitter.h"
#endif

@interface WebViewController : UIViewController
    @property NSMutableDictionary *viewerOptions;
    @property NSString *url;
    @property RCTEventEmitter* analytics;

@end
