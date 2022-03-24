package org.devgateway.toolkit.persistence.service.tetsim;

import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.TetsimOutputs;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputUndershiftCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TetsimOutputService {

    @Autowired
    private TetsimDatasetService tetsimDatasetService;

    public TetsimOutputs getTetsimOutputs(Long datasetId) {
        TetsimDataset dataset = tetsimDatasetService.findById(datasetId).get();
        TetsimOutputs outputs = new TetsimOutputs();

        for (int i = 0; i <= 100; i++) {
            outputs.getOvershift().add(new TetsimOutputOvershiftCalculator(dataset, i).calculate());
            outputs.getUndershift().add(new TetsimOutputUndershiftCalculator(dataset, i).calculate());
        }

        return outputs;
    }


}
