package cucumber.feature.distributor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import gherkin.formatter.JSONFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;


public class FeatureParserDevices {

    private String browserConfiguration;

    public FeatureParserDevices(String browserConfigurator){
        this.browserConfiguration = browserConfigurator;
    }

    private Feature[] getFeatures(String filepath) throws Exception{
        //Parse feature into JSON using Gherkin
        String featureText = FixJava.readReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
        StringBuilder json = new StringBuilder();
        JSONFormatter formatter = new JSONFormatter(json);
        Parser parser = new Parser(formatter);
        parser.parse(featureText, filepath, 0);
        formatter.done();
        formatter.close();

        //Convert the Features to plain old java object, and return an array of features
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json.toString(), Feature[].class);
    }

    private int countDeviceTags (ArrayList<Feature.Tag> tags){
        String deviceTags = writeTagString(tags);
        int count = 0;
        int index = deviceTags.indexOf("device");
        while (index != -1) {
            count++;
            deviceTags = deviceTags.substring(index + 1);
            index = deviceTags.indexOf("device");
        }
        return count;
    }

    private Set<String> getDeviceList (String platform, DeviceList devices){

        Set<String> deviceTags = new LinkedHashSet<>();
        HashMap<String, HashMap<String, String>> deviceList = new HashMap<>();

        switch (platform){
            case "all":
                deviceList.putAll(devices.getDesktops().getDevices());
                deviceList.putAll(devices.getTablets().getDevices());
                deviceList.putAll(devices.getMobiles().getDevices());
                break;
            case "desktop":
                deviceList.putAll(devices.getDesktops().getDevices());
                break;
            case "tablet":
                deviceList.putAll(devices.getTablets().getDevices());
                break;
            case "mobile":
                deviceList.putAll(devices.getMobiles().getDevices());
                break;
        }
        for (String key : deviceList.keySet()){
            deviceTags.add("@device=" + key);
        }
        return deviceTags;
    }

    private List<String> writeTags (ArrayList<Feature.Tag> tags) throws Exception {

        List<String> tagList = new ArrayList<>();
        Set<String> deviceTagList = new LinkedHashSet<>();
        String tagString = "";

        //Fetch the device list
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        DeviceList devices = mapper.readValue(new File(this.browserConfiguration), DeviceList.class);

        int wildCardTagIndex = -1;

        //User may use wild cards on the scenarios to add multiple devices
        for (Feature.Tag tag: tags) {
            if (tag.getName().toLowerCase().contains("alldevices")){
                deviceTagList.addAll(getDeviceList("all", devices));
                wildCardTagIndex = tags.indexOf(tag);
            }
            else if(tag.getName().toLowerCase().contains("desktopdevices")){
                deviceTagList.addAll(getDeviceList("desktop", devices));
                wildCardTagIndex = tags.indexOf(tag);
            }
            else if(tag.getName().toLowerCase().contains("tabletdevices")){
                deviceTagList.addAll(getDeviceList("tablet", devices));
                wildCardTagIndex = tags.indexOf(tag);
            }
            else if(tag.getName().toLowerCase().contains("mobiledevices")){
                deviceTagList.addAll(getDeviceList("mobile", devices));
                wildCardTagIndex = tags.indexOf(tag);
            }
            else if(!tag.getName().toLowerCase().contains("device")){
                tagString = tagString + tag.getName() + " ";
            }
        }

        //Remove wildcard tag
        if (wildCardTagIndex > -1){
            tags.remove(wildCardTagIndex);
        }

        //User may also use multiple device tags on a scenario
        if (countDeviceTags(tags) > 0){
            for (Feature.Tag tag: tags){
                String tagName = tag.getName().toLowerCase();
                if (tagName.toLowerCase().contains("device")){
                    deviceTagList.add(tag.getName());
                }
            }
        }

        if (deviceTagList.size() > 0) {
            for (String deviceTag : deviceTagList){
                tagList.add(tagString + " " + deviceTag + " ");
            }
        }
        else {
            tagList.add(tagString);
        }

        return tagList;
    }

    private String writeTagString (ArrayList<Feature.Tag> tags) {
        String tagString = "";
        for (Feature.Tag tag : tags){
            tagString = tagString + tag.getName() + " ";
        }
        return  tagString;
    }

    private List<String> getSeparatedRows(ArrayList<Feature.Row> rows){
        List<String> rowList = new ArrayList<>();
        String exampleHeading = writeExampleCells(rows.get(0).getCells()) + "\n";
        for (int i=1; i < rows.size(); i++){
            rowList.add(exampleHeading + writeExampleCells(rows.get(i).getCells()));
        }
        return rowList;
    }

    private String writeDataTable(ArrayList<Feature.Row> rows){
        List<String> rowList = getSeparatedRows(rows);
        String dataTable = "";
        for (int i=0; i< rows.size(); i++){
            dataTable = dataTable + writeExampleCells(rows.get(i).getCells()) + "\n";
        }
        return dataTable;
    }

    private String writeSteps (ArrayList<Feature.Step> steps){
        String stepsString = "";
        for (Feature.Step step: steps) {
            if (step.getDocString() == null){
                stepsString = stepsString + "\t\t" + step.getKeyword() + step.getName() + "\n";
                if (step.getRows() !=null){
                    stepsString = stepsString + writeDataTable(step.getRows()) + "\n";
                }
            }
            else {
                String docString = "\n\t\t\"\"\"\n\t\t" + step.getDocString().getValue().replaceAll("\r\n", "\r\n\t\t") + "\n\t\t\"\"\"\n ";
                stepsString = stepsString + "\t\t" + step.getKeyword() + step.getName() + docString;
                if (step.getRows() !=null){
                    stepsString = stepsString + writeDataTable(step.getRows()) + "\n";
                }
            }
        }
        return stepsString;
    }

    private String writeExampleCells (ArrayList<String> cells){
        String cellsText = null;
        for (String cell : cells){
            if (cellsText == null){
                cellsText = "\t\t| " + cell + " |";
            }
            else {
                cellsText = cellsText + " " + cell + " |";
            }

        }
        return cellsText;
    }

    private List<String> getSeparatedExampleRows (ArrayList<Feature.Row> rows){
        List<String> rowList = new ArrayList<>();
        String exampleHeading = writeExampleCells(rows.get(0).getCells()) + "\n";
        for (int i=1; i < rows.size(); i++){
            rowList.add(exampleHeading + writeExampleCells(rows.get(i).getCells()));
        }
        return rowList;
    }

    private List<String> getSeparatedExampleRowList (ArrayList<Feature.Example> examples){
        List<String> exampleList = new ArrayList<>();
        for (Feature.Example example : examples){
            String exampleString = "";
            if (example.getTags() != null){
                exampleString = "\t\t" + writeTagString(example.getTags()) + "\n";
            }
            for (String row : getSeparatedExampleRows(example.getRows())){
                exampleList.add(exampleString + "\t\tExamples: \n" + row);
            }
        }
        return exampleList;
    }

    private List<String> getSeparatedScenarioList(Feature.Scenario scenario) throws Exception {

        List<String> singularScenarios = new ArrayList<>();
        List<String> tags = (scenario.getTags() != null) ? writeTags(scenario.getTags()) : null;
        List<String> taggedSingularScenarios = new ArrayList<>();
        String steps = writeSteps(scenario.getSteps()) + "\n";

        //Segregate the scenario based on examples such that they can be run in parallel.
        if (scenario.getExamples() != null){
            for (String exampleRow: getSeparatedExampleRowList(scenario.getExamples())){
                singularScenarios.add("\t" + scenario.getKeyword() + ": " + scenario.getName() + "\n" + steps + exampleRow);
            }
        }
        else {
            singularScenarios.add("\t" + scenario.getKeyword() + ": " + scenario.getName() + "\n" + steps);
        }

        //Segregate the scenario based on the multiple device tags such that scenario is replicated on multiple devices.
        for (String singularScenario : singularScenarios){
            if (tags == null){
                taggedSingularScenarios.add(singularScenario);
                continue;
            }

            if (tags.size() > 0){
                for (String tag : tags){
                    taggedSingularScenarios.add("\t" + tag.trim() + "\n" + singularScenario.replace(scenario.getName(), scenario.getName() + " | " + tag.trim().replace("device=","")));
                }
            }
            else {
                taggedSingularScenarios.add(singularScenario);
            }
        }
        return taggedSingularScenarios;
    }

    private Feature.Scenario distributeDeviceTagFromFeatureToScenario (Feature feature, Feature.Scenario scenario){

        //Case : User applied device tag to feature, and certain scenarios may not have tags. Then feature tag should
        // be passed on to the scenarios while rewriting the features.
        boolean flagDeviceAtScenario = false;
        boolean flagDeviceAtFeature = false;
        ArrayList<Feature.Tag> deviceTags =new ArrayList<Feature.Tag>();

        //Check is feature has a device tag
        if(feature.getTags() != null){
            for (Feature.Tag tag : feature.getTags()){
                if (tag.getName().toLowerCase().contains("device")){
                    deviceTags.add(tag);
                    flagDeviceAtFeature = true;
                }
            }
        }

        //Tags available at scenarios
        if(scenario.getTags() != null){

            for (Feature.Tag tag : scenario.getTags()){
                flagDeviceAtScenario = tag.getName().toLowerCase().contains("device");
            }

            //Feature has a device tag & scenario does not.
            if (flagDeviceAtFeature && !flagDeviceAtScenario){
                ArrayList<Feature.Tag> scenarioTags = new ArrayList<>();
                scenarioTags.addAll(deviceTags);
                scenarioTags.addAll(scenario.getTags());
                scenario.setTags(scenarioTags);
            }
            //Feature and scenario do not have device tags
            else if (!flagDeviceAtFeature && !flagDeviceAtScenario){
                //Create a simple device tag
                Feature.Tag deviceTag = null;
                deviceTag.setLine("99");
                deviceTag.setName("@device=allDesktops");
                ArrayList<Feature.Tag> scenarioTags = new ArrayList<>();
                scenarioTags.add(deviceTag);
                scenario.setTags(scenarioTags);
            }
        }
        //Tag not available at Scenario
        else {
            if (flagDeviceAtFeature){
                ArrayList<Feature.Tag> scenarioTags = new ArrayList<>();
                scenarioTags.addAll(deviceTags);
                scenario.setTags(scenarioTags);
            }
            // Tag available not available at Feature
            else {
                //Create a simple device tag
                Feature.Tag deviceTag = null;
                deviceTag.setLine("99");
                deviceTag.setName("@device=allDesktops");
                ArrayList<Feature.Tag> scenarioTags = new ArrayList<>();
                scenarioTags.add(deviceTag);
                scenario.setTags(scenarioTags);
            }
        }
        return scenario;
    }

    public List<String> prepareExecutableScenarios(String filepath) throws Exception {

        String featureScenarioFile = "";
        String backGround = "";
        List<String> FeatureList = new ArrayList<>();
        List<Feature.Scenario> Scenarios;
        List<String> scenarioList = new ArrayList<>();

        Feature[] features = getFeatures(filepath);

        for (Feature feature : features){
            Scenarios = feature.getElements();
            for (Feature.Scenario scenario : Scenarios){
                //Case : Scenarios which are not background shall be replicated such that
                // - Number of examples tested for a scenario x Number of devices on which scenario needs to run
                if (!scenario.getKeyword().equals("Background")){
                    scenario = distributeDeviceTagFromFeatureToScenario(feature, scenario);
                    scenarioList.addAll(getSeparatedScenarioList(scenario));
                }
                else {
                    //Case: Background may or may not have tags.
                    if (scenario.getTags() != null ){
                        backGround = "\t" + writeTagString(scenario.getTags()) + "\n";
                    }
                    backGround = backGround + "\t" + scenario.getKeyword() + ": " + scenario.getName() + "\n";
                    backGround = backGround + writeSteps(scenario.getSteps()) + "\n";
                }
            }
            for (String scenario : scenarioList){
                if (feature.getTags() != null ) {
                    featureScenarioFile = writeTagString(feature.getTags()) + "\n";
                }
                featureScenarioFile = featureScenarioFile + feature.getKeyword() + ": " + feature.getName() + "\n\n";
                featureScenarioFile = featureScenarioFile + backGround;
                featureScenarioFile = featureScenarioFile + scenario;
                FeatureList.add(featureScenarioFile);
                featureScenarioFile = "";
            }
        }
        return FeatureList;
    }
}
