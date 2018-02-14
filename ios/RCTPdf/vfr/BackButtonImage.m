//
//  BackButtonImage.m
//  RCTPdf
//
//  Created by Claire Young on 2/14/18.
//  Copyright © 2018 wonday.org. All rights reserved.
//

#import "BackButtonImage.h"
#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@implementation BackButtonImage

- (void)drawRect:(CGRect)rect {
    float width = rect.size.width;
    float height = rect.size.height;
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextBeginPath(context);
    CGContextMoveToPoint(context, width * .95, height * 0.0/10.0);
    CGContextAddLineToPoint(context, width * 0.5, height * 5.0/10.0);
    CGContextAddLineToPoint(context, width * .95, height * 10.0/10.0);
    CGContextAddLineToPoint(context, width * 1.0, height * 9.5/10.0);
    CGContextAddLineToPoint(context, width * 0.6, height * 5.0/10.0);
    CGContextAddLineToPoint(context, width * 1.0, height * 0.5/10.0);
    CGContextClosePath(context);
    
    CGContextSetFillColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextFillPath(context);
}

@end
