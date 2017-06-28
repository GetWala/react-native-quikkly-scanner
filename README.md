
# react-native-react-native-quikkly-scanner

## Getting started

`$ npm install react-native-react-native-quikkly-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-react-native-quikkly-scanner`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-react-native-quikkly-scanner` and add `RNReactNativeQuikklyScanner.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNReactNativeQuikklyScanner.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

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

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNReactNativeQuikklyScanner.sln` in `node_modules/react-native-react-native-quikkly-scanner/windows/RNReactNativeQuikklyScanner.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Com.Reactlibrary.RNReactNativeQuikklyScanner;` to the usings at the top of the file
  - Add `new RNReactNativeQuikklyScannerPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import ReactNativeQuikklyScanner from 'react-native-react-native-quikkly-scanner';

// TODO: What to do with the module?
ReactNativeQuikklyScanner;
```
  