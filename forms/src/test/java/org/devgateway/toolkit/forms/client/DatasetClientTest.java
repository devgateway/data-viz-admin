package org.devgateway.toolkit.forms.client;


import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatasetClientTest {

    private DatasetClient client;

    private String baseUrl = "http://localhost:8084/";

    @Before
    public void initClient() {
        this.client = new DatasetClient(baseUrl);
    }

    @Test
    public void testJobStatus() throws DataSetClientException {
        System.out.println(client.getDatasetStatus("1142").toString());
    }

    @Test
    public void testTetsimDataset() {
        TetsimDataset dataset = new TetsimDataset();
        dataset.setYear(2020);
        try {
            client.publishDataset(dataset, Files.readAllBytes(Paths.get("./src/test/resources/2020_tetsim.csv")));
        } catch (DataSetClientException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testClient() {
        DatasetClient client = new DatasetClient(baseUrl);
        assertTrue(client.isUp());
    }

}