package cucumber.feature.distributor;

import com.fasterxml.jackson.databind.ObjectMapper;
import gherkin.formatter.JSONFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;


public class FeatureParser {

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
        String tags = (scenario.getTags() != null) ? "\t" + writeTagString(scenario.getTags()) + "\n": "";
        String steps = writeSteps(scenario.getSteps()) + "\n";

        //Segregate the scenario based on examples such that they can be run in parallel.
        if (scenario.getExamples() != null){
            for (String exampleRow: getSeparatedExampleRowList(scenario.getExamples())){
                singularScenarios.add(tags + "\t" + scenario.getKeyword() + ": " + scenario.getName() + "\n" + steps + exampleRow);
            }
        }
        else {
            singularScenarios.add(tags + "\t" + scenario.getKeyword() + ": " + scenario.getName() + "\n" + steps);
        }

        return singularScenarios;
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
