package carworkshop2;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

public class PDFTest {

    public static void main(String[] args) {

        try (PDDocument document = new PDDocument()) {

            document.addPage(new PDPage());

            document.save(new File("PDFTest.pdf"));

            System.out.println("PDF created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}