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
    float lineWidth = .1;
    float OffsetFromRight = 0;
    // The center square has hypotenuse lineWidth and side length lineWidth / sqrt(2)
    // The triangles on the corner have hypotenuse (lineWidth / sqrt(2)) and edge length ((lineWidth / sqrt(2)) / sqrt(2)) == (lineWidth / 2)
    
    CGContextBeginPath(context);
    CGContextMoveToPoint(context, width * (1.0 - lineWidth / 2) - OffsetFromRight, height * 0.0/10.0);
    CGContextAddLineToPoint(context, width * 0.5 - OffsetFromRight, height * 5.0/10.0);
    CGContextAddLineToPoint(context, width * (1.0 - lineWidth / 2) - OffsetFromRight, height * 10.0/10.0);
    CGContextAddLineToPoint(context, width * 1.0 - OffsetFromRight, height * (1.0 - lineWidth / 2));
    CGContextAddLineToPoint(context, width * (.5 + lineWidth) - OffsetFromRight, height * 5.0/10.0);
    CGContextAddLineToPoint(context, width * 1.0 - OffsetFromRight, height * lineWidth / 2);
    CGContextClosePath(context);
    
    CGContextSetFillColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextFillPath(context);
    
}

@end

