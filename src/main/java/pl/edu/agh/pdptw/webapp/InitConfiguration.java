package pl.edu.agh.pdptw.webapp;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Anna on 2015-06-15.
 */

public class InitConfiguration {
    private String config;
    private int numberOfIterations;
    private int algorithm;

    public InitConfiguration() {

    }

    public InitConfiguration(String configurationFile, int numberOfIterations, int algorithm) {
        this.config = configurationFile;
        this.numberOfIterations = numberOfIterations;
        this.algorithm = algorithm;
    }

    public String getConfig() throws UnsupportedEncodingException {
        byte[] decoded = Base64.decodeBase64(config.getBytes());
        return new String(decoded, "UTF-8");
    }

    public void setConfig(String config) {
        this.config = config;
    }


    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }
}
