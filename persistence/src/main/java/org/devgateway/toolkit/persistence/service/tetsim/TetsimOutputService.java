package org.devgateway.toolkit.persistence.service.tetsim;

import com.google.common.collect.ImmutableList;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputUndershiftCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TetsimOutputService {

    public static final int MAX_TAX_CHANGE = 100;

    public static final List<String> TOBACCO_PRODUCTS = new ImmutableList.Builder()
            .add("Imported", "Premium", "Popular", "Discount", "Illicit")
            .build();

    public static final List<String> TETSIM_CSV_FIELDS = new ImmutableList.Builder()
            .add("taxChange", "tobaccoProduct", "shifting", "consumptionLegal", "consumptionIllicit", "exciseRev",
                    "totalGovRev", "exciseBurden", "totalTaxBurden", "retailPrice", "not", "exciseTax", "vat", "levy")
            .build();
    @Autowired
    private TetsimDatasetService tetsimDatasetService;

    /**
     * Get the tetsim outputs
     *
     * @param datasetId
     * @return
     */
    public List<TetsimOutput> getTetsimOutputs(Long datasetId) {
        TetsimDataset dataset = tetsimDatasetService.findById(datasetId).get();
        List<TetsimOutput> outputs = new ArrayList<>();

        for (int i = 0; i <= MAX_TAX_CHANGE; i++) {
            for (String t : TOBACCO_PRODUCTS) {
                outputs.add(new TetsimOutputOvershiftCalculator(dataset, i).calculate(t));
                outputs.add(new TetsimOutputUndershiftCalculator(dataset, i).calculate(t));
            }
        }

        return outputs;
    }


    /**
     * Get the tetsim outputs in csv format
     * @param datasetId
     * @return
     * @throws IOException
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     */
    public byte[] getTetsimCSVDatasetOutputs(Long datasetId) throws IOException, CsvRequiredFieldEmptyException,
            CsvDataTypeMismatchException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);

        final List<String> columns = TETSIM_CSV_FIELDS.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        HeaderColumnNameMappingStrategy<TetsimOutput> columnStrategy = new HeaderColumnNameMappingStrategy<>();
        columnStrategy.setType(TetsimOutput.class);
        columnStrategy.setColumnOrderOnWrite(Comparator.comparingInt(columns::indexOf));

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(streamWriter)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withApplyQuotesToAll(false)
                .withMappingStrategy(columnStrategy)
                .build();

        sbc.write(getTetsimOutputs(datasetId));
        streamWriter.flush();

        return stream.toByteArray();
    }

}
