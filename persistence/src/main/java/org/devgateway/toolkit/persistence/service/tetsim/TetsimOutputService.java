package org.devgateway.toolkit.persistence.service.tetsim;

import com.google.common.collect.ImmutableList;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.devgateway.toolkit.persistence.dao.data.TetsimDataset;
import org.devgateway.toolkit.persistence.dao.data.TobaccoProduct;
import org.devgateway.toolkit.persistence.dto.TetsimExportOutput;
import org.devgateway.toolkit.persistence.dto.TetsimOutput;
import org.devgateway.toolkit.persistence.service.data.TetsimDatasetService;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputOvershiftCalculator;
import org.devgateway.toolkit.persistence.util.tetsim.TetsimOutputPerfectshiftCalculator;
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

    public static final List<String> TETSIM_CSV_FIELDS = new ImmutableList.Builder()
            .add("year", "taxChange", "tobaccoProduct", "legalConsumptionOvershift", "legalConsumptionChangeOvershift",
                    "consumptionIllicitOvershift", "exciseRevOvershift", "exciseRevChangeOvershift",
                    "totalGovRevOvershift", "exciseBurdenOvershift", "totalTaxBurdenOvershift",
                    "baselineTotalTaxBurdenOvershift", "retailPriceOvershift", "notOvershift", "exciseTaxOvershift",
                    "vatOvershift", "levyOvershift", "legalConsumptionUndershift", "legalConsumptionChangeUndershift",
                    "consumptionIllicitUndershift", "exciseRevUndershift", "exciseRevChangeUndershift",
                    "totalGovRevUndershift", "exciseBurdenUndershift", "totalTaxBurdenUndershift",
                    "baselineTotalTaxBurdenUndershift", "retailPriceUndershift", "notUndershift", "exciseTaxUndershift",
                    "vatUndershift", "levyUndershift", "legalConsumptionPerfectshift",
                    "legalConsumptionChangePerfectshift", "consumptionIllicitPerfectshift", "exciseRevPerfectshift",
                    "exciseRevChangePerfectshift", "totalGovRevPerfectshift", "exciseBurdenPerfectshift",
                    "totalTaxBurdenPerfectshift", "baselineTotalTaxBurdenPerfectshift", "retailPricePerfectshift",
                    "notPerfectshift", "exciseTaxPerfectshift", "vatPerfectshift", "levyPerfectshift")
            .build();
    @Autowired
    private TetsimDatasetService tetsimDatasetService;

    /**
     * Get the tetsim outputs
     *
     * @param datasetId
     * @return
     */
    public List<TetsimExportOutput> getTetsimOutputs(Long datasetId) {
        TetsimDataset dataset = tetsimDatasetService.findById(datasetId).get();
        List<TetsimExportOutput> outputs = new ArrayList<>();

        for (int i = 0; i <= MAX_TAX_CHANGE; i++) {
            for (TobaccoProduct t : TobaccoProduct.values()) {
                TetsimOutput overShift = new TetsimOutputOvershiftCalculator(dataset, i).calculate(t);
                TetsimOutput underShift = new TetsimOutputUndershiftCalculator(dataset, i).calculate(t);
                TetsimOutput perfectShift = new TetsimOutputPerfectshiftCalculator(dataset, i).calculate(t);
                outputs.add(new TetsimExportOutput(overShift, underShift, perfectShift));
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

        HeaderColumnNameMappingStrategy<TetsimExportOutput> columnStrategy = new HeaderColumnNameMappingStrategy<>();
        columnStrategy.setType(TetsimExportOutput.class);
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
