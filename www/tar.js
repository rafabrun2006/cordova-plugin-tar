var exec = cordova.require('cordova/exec');

exports.untar = function(fileName, outputDirectory, callback) {

    var success = function() {
        callback(1);
    }, fail = function() {
        callback(0);
    };

    exec(success, fail, 'Tar', 'untar', [fileName, outputDirectory]);

};
