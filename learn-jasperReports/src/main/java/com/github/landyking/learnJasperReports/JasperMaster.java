package com.github.landyking.learnJasperReports;

import net.sf.jasperreports.engine.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/3 11:54
 * note:
 */
public abstract class JasperMaster {
    public static final String JASPER_REPORTS = "jasperReports";
    private final String classResourcePath;
    private final String pdfFileName;
    private String outputFilePath;

    public JasperMaster(String classResourcePath, String pdfFileName) {
        this.classResourcePath = classResourcePath;
        this.pdfFileName = pdfFileName;
    }

    public void showOutputFile() throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File(getOutputFilePath()));
        }
    }

    public void doWork() {
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        try {
            File targetDir = Utils.getTargetDir();
            System.out.println("target: " + targetDir.getAbsolutePath());
            File jasperReportsDir = new File(targetDir, JASPER_REPORTS);
            jasperReportsDir.mkdirs();
            URL resource = Utils.getResource(classResourcePath);
            if (resource == null) {
                System.out.println("Can't found resource: " + classResourcePath);
            }
            System.out.println("read resource: " + resource.getFile());
            jasperReport = JasperCompileManager.compileReport(new FileInputStream(resource.getFile()));
            jasperPrint = fillReport(jasperReport);
            outputFilePath = new File(jasperReportsDir, pdfFileName).getAbsolutePath();
            JasperExportManager.exportReportToPdfFile(
                    jasperPrint, outputFilePath);
            System.out.println("output file: " + outputFilePath);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public abstract JasperPrint fillReport(JasperReport jasperReport) throws JRException;

}
