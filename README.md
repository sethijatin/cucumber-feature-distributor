[![](https://jitpack.io/v/sethijatin/cucumber-feature-distributor.svg)](https://jitpack.io/#sethijatin/cucumber-feature-distributor)
# Cucumber Feature-Browser Distributor

Selenium projects are required to be run on various browsers and cucumber doesn't provide this feature out of the box. This plugin aims to distribute the scenarios and features over an array of browser defined rewriting each scenario using different browser tags. Other than this it also re-writes the feature files such a way that each file will have single test execution balancing the load on each thread in a multi-threaded execution.

### Distributed Execution Of Tests
* A Feature File in cucumber is usually a group of scenarios.
* Each Scenario in the Feature File may in turn have multiple examples.
* Such feature files when run in parallel do not provide much advantage because each thread is assigned unequal number of test cases to execute. While one thread executing Feature 1 may have to execute 20 tests, the other thread executing Feature 2 may simply exeucte 5 tests. 
* While such a problem is usually ignored, this may well cut down the efficiency of the team.
* This plugin breaks the feature files into mutliple feature files such that each file contains a single scenario for execution.

### Scenarios Re-Written For Each Browser Defined In Browser Configuration File (YAML)
* Usually teams using Selenium run their test cases on multiple browsers.
* Cucumber does not provide a functionality out of box to run one scenario on multiple browsers.
* This means scenario may have to be manually re-written or tagged in multiple runners to achieve the objective. The process is error prone and time consuming.
* The plugin provies 4 generic tags @device=allDevices; @device=desktopDevices; @device=tabletDevices; @device=mobileDevices
* While distributing the features if the plugin identifies a scenario to be using these generic tags, then it automatically re-writes the scenario for the users for different desktop, tablet, or mobile devices indicated in a configuration file. All devices will tag the scenario with all the devices in the configuration file. 
* The browser configuration file is a YAML file which needs to follow the format as described below. 


## Browser YAML SAMPLE(BROWSER JSON UPDATED TO YAML FOR EASE OF USE)

```YML
DesktopDevices: #Do not change the nae DesktopDevices. This is hard mapped in the data mapper class.
  Edge_Win_10_1920x1080: # User defined device tag.
    priority_setting: false # Any number of properties per tag.
    desiredCapabilities: edge
    browser: Edge
    platform: Windows 10
    version: 14.14393
    screenResolution: 1920x1080

TabletDevices: #Do not change the nae DesktopDevices. This is hard mapped in the data mapper class.
  iOS_iPad_Air_Portrait: # User defined device tag.
    priority_setting: true # Any number of properties per tag.
    desiredCapabilities: iPhone
    appiumVersion: 1.6.4
    deviceName: iPad Air 2 Simulator
    deviceOrientation: portrait
    platformName: iOS
    browserName: Safari
    platformVersion: 10.3

MobileDevices: #Do not change the nae DesktopDevices. This is hard mapped in the data mapper class.
  iOS_iPhone_6s: # User defined device tag.
    priority_setting: true # Any number of properties per tag.
    desiredCapabilities: iPhone
    appiumVersion: 1.6.4
    deviceName: iPhone 6s Simulator
    deviceOrientation: portrait
    platformName: iOS
    browserName: Safari
    platformVersion: 10.3
```

## Including in Project
```java
<pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
 </pluginRepositories>

 <plugin>
      <groupId>com.github.sethijatin</groupId>
      <artifactId>cucumber-feature-distributor</artifactId>
      <version>1.6</version>
      <executions>
          <execution>
              <goals>
                  <goal>distribute-features</goal>
              </goals>
          </execution>
      </executions>
      <configuration>
          <featureDirectory>src/test/Resources/features/</featureDirectory> <!-- Path where actual feature files created for the projects are placed -->
          <distributedFeatureDirectory>src/test/Resources/features_automation/</distributedFeatureDirectory> <!-- Path where distributed features will be placed -->
          <pathToBrowserJSON>src/test/Resources/configurations/browser-config.yaml</pathToBrowserJSON> <!-- A Browser Config File (UPDATED TO YAML NO MORE JSON). Following a specific standard. Format is fixed and must not be changed.-->
      </configuration>
  </plugin>
```
