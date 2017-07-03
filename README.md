
# react-native-quikkly-scanner

## Getting started

`$ npm install react-native-quikkly-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-quikkly-scanner`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.getwala.quikkly.reactnative.ReactNativeQuikklyScannerPackage;` to the imports at the top of the file
  - Add `new ReactNativeQuikklyScannerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-react-native-quikkly-scanner'
  	project(':react-native-react-native-quikkly-scanner').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-react-native-quikkly-scanner/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
	```
		compile project(':react-native-react-native-quikkly-scanner')
	```
4. Add your quikkly api key to your appManifest
	```
        <meta-data
            android:name="QUIKKLY_API_KEY"
            android:value="mySuperSecretLongKey" />

	```
5. Add the ScanQuikklyCode Activity to your manifest
	```
     <activity 
	 android:name="com.getwala.quikkly.reactnative.ScanQuikklyCodeActivity"></activity>
  
	```
## Usage

# Scanning
```   javascript
import ReactNativeQuikklyScanner from 'react-native-quikkly-scanner';

	// Opens the quikkly scanner. On code detection returns a promise with the code.
    ReactNativeQuikklyScanner.scanQuikklyCode().then((quikklyScannedCode) => {
      console.log(quikklyScannedCode);
    }).catch((error) => {
     //handle error here
    });

```

# Generating 
 The imagePath needs to be a path on the device where the image is stored, I would recommend using react-native-cached-image'

``` javascript

import CachedImage from 'react-native-cached-image';
const ImageCacheProvider = CachedImage.ImageCacheProvider;

 ImageCacheProvider.getCachedImagePath(options.imagePath, {})
    .then(imagePath => {
      options.imagePath = imagePath;
      ReactNativeQuikklyScanner.GenerateCode(options);
    })

options = {
	"templateName" : "", //string defaults to "template0002style5"
	"quikklyCode" : "", // string. This is what the scannable will return when scanned
	"backgroundColor":"", // will default to "#5cb7a6"
	"borderColor":"", // defaults to "#ffffff"
	"dataColor":"", // defaults to #000000"
	"maskColor":"", // defaults to "#5cb7a6"
	"overlayColor":"", // defaults to // "#ffffff"
	"imagePath": "" // defaults to "https://s3-eu-west-1.amazonaws.com/qkly-service-albums/temp_icons/squiddy.png"
}


```  