package com.example.bank.bank.pdf;

import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.dao.TransakcjeRepository;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.AktualnyKlientService;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("reportView")
public class ReportPdfView extends AbstractView {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    TransakcjeRepository transakcjeRepository;


    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        Klient klient = aktualnyKlientService.getKlient();
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);
        System.out.println("Aktualny rachunek"+ rachunek.getNumerKonta());
        List<Transakcje> transakcjeList = transakcjeRepository.findByrachunekTransakcje(rachunek);
        List<Transakcje> transakcjeListOtrzymane = transakcjeRepository.findByrachunekTransakcje2(rachunek);

        for(int i = 0; i < transakcjeList.size(); i++) {
            if (transakcjeList.get(i).getTytulPrzelewu().trim().equals("brak")) {
                System.out.println("wchodzi tu czy nie ");
                transakcjeList.remove(i);
                i--;
            }

        }
        for(int i = 0; i < transakcjeListOtrzymane.size(); i++) {
            if (transakcjeListOtrzymane.get(i).getTytulPrzelewu().trim().equals("brak2")) {
                System.out.println("wchodzi tu czy nie ");
                transakcjeListOtrzymane.remove(i);
                i--;
            }

        }

        List<Daty> datyList = new ArrayList<>();
        List<Klient> klientList = new ArrayList<>();
        List<TypOperacji> typOperacjiList = new ArrayList<>();

        List<Daty> datyList2 = new ArrayList<>();
        List<Klient> klientList2 = new ArrayList<>();
        List<TypOperacji> typOperacjiList2 = new ArrayList<>();

        for(int i =0; i< transakcjeList.size(); i++) {
            Transakcje t = transakcjeList.get(i);
            datyList.add(t.getDatyTransakcje());
            typOperacjiList.add(t.getTypOperacjiTransakcje());
            klientList.add(t.getRachunekTransakcje2().getRachunekKlienta());

        }
        for(int i =0; i< transakcjeListOtrzymane.size(); i++) {
            Transakcje t2 = transakcjeListOtrzymane.get(i);
            datyList2.add(t2.getDatyTransakcje());
            typOperacjiList2.add(t2.getTypOperacjiTransakcje());
            klientList2.add(t2.getRachunekTransakcje2().getRachunekKlienta());

        }




        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String aktualnaData=date1.format(currentDate);
        System.out.println(aktualnaData);
        System.out.println(currentDate);




        response.setHeader("Content-Disposition", "attachment; filename=\"Raport "+aktualnaData+".pdf\"");
        Report report = (Report) model.get("report");

        //IText API
        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(pdfWriter);
        Document pdfDocument = new Document(pdf);

        //title
        Paragraph title = new Paragraph(report.getName());
        title.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
        title.setFontSize(18f);
        title.setItalic();
        pdfDocument.add(title);

        //date
        Paragraph date = new Paragraph(report.getDate().format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        date.setFontSize(16f);
        pdfDocument.add(date);

        //content
        Paragraph content = new Paragraph("KLIENT: "+klient.getImie()+" "+klient.getNazwisko()+
        "\n"+"PESEL: "+klient.getPesel()+
                "\n"+"E-MAIL: "+klient.getEmail()+"\n\n");
        pdfDocument.add(content);

       // System.out.println("Kwota na koncie klienta "+rachunek.getKwota());

      //  Paragraph content1 = new Paragraph(String.valueOf(rachunek.getKwota()));
       // pdfDocument.add(content1);
      /*  model.addAttribute("klient",klient);
        model.addAttribute("rachunek",rachunek);
        model.addAttribute("listaTransakcji",transakcjeList);
        model.addAttribute("transakcjeListOtrzymane",transakcjeListOtrzymane);
        model.addAttribute("datyList",datyList);
        model.addAttribute("typOperacjiList",typOperacjiList);
        model.addAttribute("datyList2",datyList2);
        model.addAttribute("typOperacjiList2",typOperacjiList2);
        model.addAttribute("klientList2",klientList2);
        model.addAttribute("klientList",klientList);*/

        float[] columnWidths = {1, 5, 5,5,5};
        Table table = new Table(columnWidths);
        table.setWidthPercent(100);
        PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Cell cell = new Cell(1, 5)
                .add(new Paragraph(String.valueOf("Numer rachunku: "+rachunek.getNumerKonta())))
                .setFont(f)
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell);
        Cell cell1 = new Cell(1, 5)
                .add(new Paragraph(String.valueOf("Kwota na koncie: "+rachunek.getKwota())))
                .setFont(f)
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell1);
        for (int i = 0; i < 4; i++) {
            Cell[] headerFooter = new Cell[] {
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(0.75f)).add("Kwota:"),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(0.75f)).add("Tytul przelewu:"),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(0.75f)).add("Data:"),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(0.75f)).add("Typ operacji:"),
                    new Cell().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceGray(0.75f)).add("Dla:")
            };
            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    table.addHeaderCell(hfCell);
                } else if(i==3) {
                    table.addFooterCell(hfCell);
                }
            }
        }

        for (int counter = 0; counter < datyList.size(); counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(transakcjeList.get(counter).getKwota())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(transakcjeList.get(counter).getTytulPrzelewu())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(datyList.get(counter).getData())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(typOperacjiList.get(counter).getRodzajOperacji())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(klientList.get(counter).getImie())+" "+klientList.get(counter).getNazwisko()));
        }
        for (int counter = 0; counter < datyList2.size(); counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(transakcjeListOtrzymane.get(counter).getKwota())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(transakcjeListOtrzymane.get(counter).getTytulPrzelewu())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(datyList2.get(counter).getData())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(typOperacjiList2.get(counter).getRodzajOperacji())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(klientList2.get(counter).getImie())+" "+klientList2.get(counter).getNazwisko()));
        }
        pdfDocument.add(table);

        pdfDocument.close();
    }
}