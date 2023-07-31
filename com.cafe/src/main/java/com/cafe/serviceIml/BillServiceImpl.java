package com.cafe.serviceIml;

import com.cafe.JWT.JwtFilter;
import com.cafe.constants.CafeConstants;
import com.cafe.dao.BillDao;
import com.cafe.model.Bill;
import com.cafe.service.BillService;
import com.cafe.util.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@Service
public class BillServiceImpl implements BillService {
    @Autowired
    BillDao billDao;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("name");
                } else {
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);

                    String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " + requestMap.get("contactNumber") +
                            "\n" + "Email: " + requestMap.get("email") + "\n" + "Payment method: " + requestMap.get("paymentMethod");

                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

                    document.open();

                    // setting border of report
                    setRectanglePdf(document);

                    // setting header of report
                    setHeaderOfPdf(document);

                    // setting data of report
                    setDataOfPdf(document, data + "\n \n");

                    // setting product table headers
                    PdfPTable table = new PdfPTable(5);
                    table.setWidthPercentage(100);
                    addTableHeader(table);

                    // setting table data
                    JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addRow(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                    }
                    document.add(table);

                    // setting footer
                    Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount") + "\n" +
                            "Thank you for visiting, do visit again", getFont("Data"));
                    document.add(footer);

                    document.close();

                    return CafeUtils.getResponseEntity("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRow(PdfPTable table, Map<String, Object> mapFromJson) {
        log.info("Inside add row");
        table.addCell((String) mapFromJson.get("name"));
        table.addCell((String) mapFromJson.get("category"));
        table.addCell((String) mapFromJson.get("quantity"));
        table.addCell(Double.toString((Double) mapFromJson.get("price")));
        table.addCell(Double.toString((Double) mapFromJson.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Name", "Category", "Quantity", "Price", "Sub total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            default:
                return new Font();
        }
    }

    private void setHeaderOfPdf(Document document) throws DocumentException {
        Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
        chunk.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
    }

    private void setDataOfPdf(Document document, String data) throws DocumentException {
        Paragraph details = new Paragraph(data, getFont("Data"));
        document.add(details);
    }

    private void setRectanglePdf(Document document) throws DocumentException {
        log.info("Inside set rectangle border");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());

            billDao.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }
}
