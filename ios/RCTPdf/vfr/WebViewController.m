//
//  WebViewController.m
//  RCTPdf
//
//  Created by Claire Young on 2/20/18.
//  Copyright © 2018 wonday.org. All rights reserved.
//

#import "WebViewController.h"
#import <WebKit/WebKit.h>


@interface WebViewController ()
    @property (nonatomic, strong) WKWebView *webView;
    @property (nonatomic, strong) UINavigationBar *navBar;
@end

@implementation WebViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];

    self.view.frame = CGRectMake(0, 20, self.view.frame.size.width, self.view.frame.size.height-20);
    UINavigationBar* navBar = [[UINavigationBar alloc] initWithFrame:[self navBarRect]];
    
    UINavigationItem* navItem = [[UINavigationItem alloc] initWithTitle:@""];
    UIBarButtonItem* doneBtn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(onTapDone:)];
    navItem.rightBarButtonItem = doneBtn;
    
    [navBar setItems:@[navItem]];
    
    WKWebView* tempWebview = [[WKWebView alloc] initWithFrame:[self webViewRect]];
    NSURL *url = [NSURL URLWithString:self.url];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    self.webView = tempWebview;
    [self.webView loadRequest:requestObj];
    self.navBar = navBar;
    
    [self.view addSubview:self.webView];
    [self.view addSubview:self.navBar];
}

-(CGRect) navBarRect {
    return CGRectMake(0, 0, self.view.frame.size.width, 45);
}

-(CGRect) webViewRect {
    CGFloat verticalOffset = self.navBar.viewForBaselineLayout.frame.origin.y + self.navBar.viewForBaselineLayout.frame.size.height;
    return CGRectMake(0, verticalOffset, self.view.frame.size.width, self.view.frame.size.height-verticalOffset);
}

- (void) viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    self.view.frame = CGRectMake(0, 20, self.view.frame.size.width, self.view.frame.size.height);
    [self.navBar setFrame:[self navBarRect]];
    [self.webView setFrame:[self webViewRect]];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)onTapDone:(UIBarButtonItem*)item{
    [self dismissViewControllerAnimated:true completion:nil];

}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
