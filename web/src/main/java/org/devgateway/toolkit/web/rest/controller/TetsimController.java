package org.devgateway.toolkit.web.rest.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.ApiOperation;
import org.devgateway.toolkit.persistence.dto.TetsimExportOutput;
import org.devgateway.toolkit.persistence.service.tetsim.TetsimOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author vchihai
 */

@RestController
@RequestMapping("{dg-toolkit.forms.base-path}/api/tetsim")
public class TetsimController {

    private static final Logger logger = LoggerFactory.getLogger(TetsimController.class);

    private final TetsimOutputService tetsimOutputService;

    public TetsimController(final TetsimOutputService tetsimOutputService) {
        this.tetsimOutputService = tetsimOutputService;
    }

    @ApiOperation(value = "TETSIM Output API")
    @GetMapping(value = "/output", produces = "application/json")
    public List<TetsimExportOutput> getTetsimOutput(@RequestParam() final Long id) {
        List<TetsimExportOutput> outputs = tetsimOutputService.getTetsimOutputs(id);

        return outputs;
    }

    @ApiOperation(value = "Download TETSIM dataset in csv format")
    @GetMapping(value = "/output/csv", produces = "text/csv")
    public void exportTetsimOutput(HttpServletResponse response, @RequestParam() final Long id)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"dataset.csv\"");
        response.getOutputStream().write(tetsimOutputService.getTetsimCSVDatasetOutputs(id));
    }

}
