//
//  Tar.m
//  stepfolio
//
//  Created by Matthis Buschtöns on 21.01.16.
//
//

#import "Tar.h"
#import "CDVFile.h"

@implementation Tar

- (void) untar:(CDVInvokedUrlCommand *)command {

    self->_command = command;

    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        @try {
            NSString *tarURL = [command.arguments objectAtIndex:0];
            NSString *extractPathURL = [command.arguments objectAtIndex:1];

            NSString *filePath = [self pathForURL:tarURL];
            NSString *extractPath = [self pathForURL:extractPathURL];

            NSError *error;

            if ([[NSFileManager defaultManager] createFilesAndDirectoriesAtPath:extractPath withTarPath:filePath error:&error progress:NULL]) {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

            } else {
                NSLog(@"%@ : %@", @"Error during tar extraction", [error localizedDescription]);
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Error during tar extraction"];
            }


        } @catch(NSException* exception) {
            NSLog(@"%@ : %@", @"Error during tar extraction", [exception debugDescription]);
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Error during tar extraction"];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];

}

- (NSString *)pathForURL:(NSString *)urlString
{

    NSString *path = nil;
    id filePlugin = [self.commandDelegate getCommandInstance:@"File"];
    if (filePlugin != nil) {
        CDVFilesystemURL* url = [CDVFilesystemURL fileSystemURLWithString:urlString];
        path = [filePlugin filesystemPathForURL:url];
    }

    return path;
}

@end
