package com.smarttourist.agent.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.*;
import com.smarttourist.agent.model.Booking;
import com.smarttourist.agent.model.Payment;
import com.smarttourist.agent.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final PaymentRepository paymentRepository;

    public PdfService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ByteArrayInputStream generateInvoicePdf(Booking booking) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font configurations
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

            // Company Title
            Paragraph companyTitle = new Paragraph("SMART TOURIST AGENT", titleFont);
            companyTitle.setAlignment(Element.ALIGN_CENTER);
            companyTitle.setSpacingAfter(5);
            document.add(companyTitle);

            // Company details
            Paragraph companyDetails = new Paragraph(
                    "GSTIN1234567890Z | Email: contact@smarttourist.com | Contact: +91-9876543210\n" +
                    "Address: Connaught Place, New Delhi, India",
                    FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY)
            );
            companyDetails.setAlignment(Element.ALIGN_CENTER);
            companyDetails.setSpacingAfter(20);
            document.add(companyDetails);

            // Section divider line
            Paragraph divider = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------", FontFactory.getFont(FontFactory.HELVETICA, 8, Color.LIGHT_GRAY));
            divider.setSpacingAfter(15);
            document.add(divider);

            // Subtitle
            Paragraph subtitle = new Paragraph("BOOKING INVOICE / RECEIPT", subTitleFont);
            subtitle.setSpacingAfter(15);
            document.add(subtitle);

            // Payment reference lookup
            Payment payment = paymentRepository.findByBookingId(booking.getId()).orElse(null);
            String txnId = payment != null ? payment.getTransactionId() : "N/A";
            String payMethod = payment != null ? payment.getPaymentMethod() : "N/A";
            String payDateStr = payment != null ? payment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";

            // Metadata Table (Booking Details & Payment Reference)
            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);
            metaTable.setSpacingAfter(20);

            // Column 1: Customer Info
            PdfPCell cell1 = new PdfPCell(new Paragraph(
                    "Billed To:\n" +
                    "Name: " + booking.getUser().getName() + "\n" +
                    "Email: " + booking.getUser().getEmail() + "\n" +
                    "Mobile: " + booking.getUser().getMobileNumber(),
                    regularFont
            ));
            cell1.setBorder(Rectangle.NO_BORDER);
            metaTable.addCell(cell1);

            // Column 2: Invoice Info
            PdfPCell cell2 = new PdfPCell(new Paragraph(
                    "Invoice No: STA-" + booking.getId() + "\n" +
                    "Date: " + booking.getBookingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n" +
                    "Transaction ID: " + txnId + "\n" +
                    "Payment Method: " + payMethod + "\n" +
                    "Payment Status: PAID",
                    regularFont
            ));
            cell2.setBorder(Rectangle.NO_BORDER);
            metaTable.addCell(cell2);

            document.add(metaTable);

            // Tour details section
            Paragraph tourHeader = new Paragraph("Tour Itinerary Summary", subTitleFont);
            tourHeader.setSpacingAfter(10);
            document.add(tourHeader);

            PdfPTable tourTable = new PdfPTable(2);
            tourTable.setWidthPercentage(100);
            tourTable.setSpacingAfter(20);

            tourTable.addCell(new PdfPCell(new Paragraph("Start Location", boldFont)));
            tourTable.addCell(new PdfPCell(new Paragraph(booking.getTour().getStartLocation(), regularFont)));
            tourTable.addCell(new PdfPCell(new Paragraph("Destination", boldFont)));
            tourTable.addCell(new PdfPCell(new Paragraph(booking.getTour().getDestination().getName(), regularFont)));
            tourTable.addCell(new PdfPCell(new Paragraph("Travel Dates", boldFont)));
            tourTable.addCell(new PdfPCell(new Paragraph(booking.getTour().getStartDate() + " to " + booking.getTour().getEndDate() + " (" + booking.getTour().getNumberOfDays() + " Days)", regularFont)));
            tourTable.addCell(new PdfPCell(new Paragraph("Travelers count", boldFont)));
            tourTable.addCell(new PdfPCell(new Paragraph(booking.getTour().getTotalTravelers() + " (Adults: " + booking.getTour().getNumAdults() + ", Children: " + booking.getTour().getNumChildren() + ", Seniors: " + booking.getTour().getNumSeniors() + ")", regularFont)));

            document.add(tourTable);

            // Cost details table
            Paragraph chargeHeader = new Paragraph("Cost Breakdowns & Charges", subTitleFont);
            chargeHeader.setSpacingAfter(10);
            document.add(chargeHeader);

            PdfPTable costTable = new PdfPTable(2);
            costTable.setWidthPercentage(100);
            costTable.setSpacingAfter(30);

            // Set headers
            PdfPCell h1 = new PdfPCell(new Paragraph("Item Description", headerFont));
            h1.setBackgroundColor(new Color(32, 58, 67));
            h1.setPadding(5);
            costTable.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Paragraph("Amount (INR)", headerFont));
            h2.setBackgroundColor(new Color(32, 58, 67));
            h2.setPadding(5);
            costTable.addCell(h2);

            // Calculate details
            int days = booking.getTour().getNumberOfDays();
            double vehicleCost = booking.getVehicle() != null ? (booking.getVehicle().getFarePerDay() * days) + (booking.getVehicle().getFarePerKm() * 100.0 * days) : 0.0;
            int roomsNeeded = (int) Math.ceil((double) (booking.getTour().getNumAdults() + booking.getTour().getNumSeniors()) / 2.0);
            if (roomsNeeded == 0) roomsNeeded = 1;
            double hotelCost = booking.getRoom() != null ? booking.getRoom().getPricePerNight() * days * roomsNeeded : 0.0;
            double guideCost = booking.getGuide() != null ? booking.getGuide().getChargesPerDay() * days : 0.0;
            double foodCost = 800.0 * booking.getTour().getTotalTravelers() * days;

            double subtotal = vehicleCost + hotelCost + guideCost + foodCost;
            double taxes = subtotal * 0.18;
            double total = subtotal + taxes;

            // Add rows
            costTable.addCell(new Paragraph("Vehicle Rental (" + (booking.getVehicle() != null ? booking.getVehicle().getName() : "None Chosen") + ")", regularFont));
            costTable.addCell(new Paragraph(String.format("%.2f", vehicleCost), regularFont));

            costTable.addCell(new Paragraph("Hotel Room Lodging (" + (booking.getRoom() != null ? booking.getRoom().getHotel().getName() + " - " + booking.getRoom().getType() : "None Chosen") + ")", regularFont));
            costTable.addCell(new Paragraph(String.format("%.2f", hotelCost), regularFont));

            costTable.addCell(new Paragraph("Tour Guide Charges (" + (booking.getGuide() != null ? booking.getGuide().getName() : "None Chosen") + ")", regularFont));
            costTable.addCell(new Paragraph(String.format("%.2f", guideCost), regularFont));

            costTable.addCell(new Paragraph("Food & Meals Allowance Estimation", regularFont));
            costTable.addCell(new Paragraph(String.format("%.2f", foodCost), regularFont));

            // Totals
            costTable.addCell(new Paragraph("Subtotal", boldFont));
            costTable.addCell(new Paragraph(String.format("%.2f", subtotal), boldFont));

            costTable.addCell(new Paragraph("Taxes & GST (18%)", boldFont));
            costTable.addCell(new Paragraph(String.format("%.2f", taxes), boldFont));

            PdfPCell totalDescCell = new PdfPCell(new Paragraph("Grand Total Paid", headerFont));
            totalDescCell.setBackgroundColor(new Color(44, 83, 100));
            totalDescCell.setPadding(6);
            costTable.addCell(totalDescCell);

            PdfPCell totalValCell = new PdfPCell(new Paragraph("INR " + String.format("%.2f", total), headerFont));
            totalValCell.setBackgroundColor(new Color(44, 83, 100));
            totalValCell.setPadding(6);
            costTable.addCell(totalValCell);

            document.add(costTable);

            // Footer / Thank You Note
            Paragraph footer = new Paragraph("Thank you for choosing Smart Tourist Agent! We hope you have an incredible trip.\n" +
                    "For support or emergency assistance, call us at +91-9876543210 or email contact@smarttourist.com.\n\n" +
                    "This is a computer-generated receipt, no signature is required.", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
