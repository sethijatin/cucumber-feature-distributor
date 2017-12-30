package cucumber.feature.distributor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "distribute_features", threadSafe = true, defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class DistributeFeatures extends AbstractMojo
{
    @Parameter(readonly = true)
    private String featureDirectory;

    @Parameter(readonly = true)
    private String distributedFeatureDirectory;

    @Parameter(readonly = true)
    private String pathToBrowserJSON;

    private static List<String> distributedScenarioList = new ArrayList<>();

    private void  writeDistributedFeatures(String folderPath) throws IOException {
        int i = 1;
        FileUtils.cleanDirectory(new File(folderPath));
        for(String distributedFeature : distributedScenarioList){
            BufferedWriter bw = null;
            FileWriter fw = null;
            fw = new FileWriter(folderPath + i + ".feature");
            bw = new BufferedWriter(fw);
            bw.write(distributedFeature);
            bw.close();
            fw.close();
            i++;
        }
    }

    private void readCompositeFeatures(String folderPath) throws Exception {
        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                FeatureParser featureParser = new FeatureParser(pathToBrowserJSON);
                List<String> scenarioList = featureParser.prepareExecutableScenarios(child.getAbsolutePath());
                distributedScenarioList.addAll(scenarioList);
            }
        }
    }

    public void execute() throws MojoExecutionException {
        try {
            readCompositeFeatures(featureDirectory);
            writeDistributedFeatures(distributedFeatureDirectory);
            Thread.sleep(5000);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
