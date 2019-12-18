//
//  Tar.h
//  stepfolio
//
//  Created by Matthis Buschtöns on 21.01.16.
//
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import "NSFileManager+Tar.h"

@interface Tar : CDVPlugin {
    @private CDVInvokedUrlCommand* _command;
}

- (void)untar:(CDVInvokedUrlCommand*)command;


@end
