package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Rellena la plantilla oficial de SGAE (AcroForm) con los datos de un concierto
 * y su músico (organizador) y devuelve el PDF resultante en bytes.
 */
@Service
public class SgaePdfService {

    private static final String TEMPLATE = "templates/sgae-concert.pdf";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public byte[] fill(Concert concert) throws IOException {
        byte[] template;
        try (InputStream in = new ClassPathResource(TEMPLATE).getInputStream()) {
            template = in.readAllBytes();
        }

        try (PDDocument document = Loader.loadPDF(template)) {
            PDAcroForm form = document.getDocumentCatalog().getAcroForm();
            if (form == null) {
                throw new IOException("La plantilla SGAE no contiene formulario (AcroForm)");
            }

            Musician m = concert.getMusician();

            // --- Organizador (datos del músico) ---
            if (m != null) {
                setText(form, "ORGANIZADOR", fullName(m));
                setText(form, "DOMICILIO", m.getAddress());
                setText(form, "CP OK", m.getPostalCode());
                setText(form, "DNI", m.getDni());
                setText(form, "TEL", m.getPhone());
                setText(form, "EMAIL", m.getEmail());
                setText(form, "PERSONA", m.getContactPerson());
            }

            // --- Datos del concierto / local ---
            setText(form, "NOMBRE DEL CONCIERTO", concert.getShowTitle());
            setText(form, "ACTUANTES", concert.getPerformers());
            setText(form, "LUGAR CELEBRACION", venue(concert));
            setText(form, "FECHA", concert.getDate() != null ? concert.getDate().format(DATE_FMT) : null);
            setText(form, "HORA", concert.getTime() != null ? concert.getTime().format(TIME_FMT) : null);
            setText(form, "DOMICILIO LOCAL", concert.getVenueAddress());
            setText(form, "TITULAR LOCAL", concert.getVenueOwner());
            setText(form, "AFORO TOTAL", concert.getCapacity() != null ? String.valueOf(concert.getCapacity()) : null);

            // --- Entradas ---
            setText(form, "CLASE", concert.getTicketClass());
            String tickets = concert.getTotalTickets() != null ? String.valueOf(concert.getTotalTickets()) : null;
            setText(form, "N LOCALIDADES", tickets);
            setText(form, "TOTAL LOCALIDADES", tickets);
            setText(form, "PRECIO", formatPrice(concert.getTicketPrice()));

            // --- Tarifa (casillas) ---
            String tariff = concert.getTariffType();
            if ("PERCENTAGE".equalsIgnoreCase(tariff)) {
                check(form, "porcentual");
            } else if ("FLAT".equalsIgnoreCase(tariff)) {
                check(form, "tanto alzado");
            }

            // --- Setlist: títulos de obras en las 4 líneas disponibles ---
            fillSetlist(form, concert);

            // --- Lugar y año de la firma (se usa la fecha de hoy) ---
            setText(form, "en", concert.getCity());
            setText(form, "año", String.valueOf(LocalDate.now().getYear()));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }

    /** Porcentaje (0-100, 1 decimal) de campos SGAE clave que están rellenos. */
    public float completionPercentage(Concert concert) {
        Musician m = concert.getMusician();
        List<Boolean> checks = new ArrayList<>();
        // Organizador
        checks.add(m != null && notBlank(fullName(m)));
        checks.add(m != null && notBlank(m.getAddress()));
        checks.add(m != null && notBlank(m.getPostalCode()));
        checks.add(m != null && notBlank(m.getDni()));
        checks.add(m != null && notBlank(m.getPhone()));
        checks.add(m != null && notBlank(m.getEmail()));
        // Concierto / local
        checks.add(notBlank(concert.getShowTitle()));
        checks.add(notBlank(concert.getPerformers()));
        checks.add(notBlank(concert.getVenueName()));
        checks.add(notBlank(concert.getVenueAddress()));
        checks.add(notBlank(concert.getVenueOwner()));
        checks.add(concert.getDate() != null);
        checks.add(concert.getTime() != null);
        checks.add(concert.getCapacity() != null);
        // Entradas
        checks.add(notBlank(concert.getTicketClass()));
        checks.add(concert.getTotalTickets() != null);
        checks.add(notBlank(concert.getTariffType()));
        // Setlist
        checks.add(concert.getWorks() != null && !concert.getWorks().isEmpty());

        long filled = checks.stream().filter(Boolean::booleanValue).count();
        return Math.round((filled * 1000f) / checks.size()) / 10f;
    }

    private void fillSetlist(PDAcroForm form, Concert concert) {
        List<Work> works = concert.getWorks();
        if (works == null || works.isEmpty()) {
            return;
        }
        List<String> titles = new ArrayList<>();
        for (Work w : works) {
            if (w != null && notBlank(w.getTitle())) {
                titles.add(w.getTitle());
            }
        }
        String[] lineFields = {"TITULOS", "TITULOS 2", "TITULOS 3", "TITULOS 4"};
        for (int i = 0; i < lineFields.length && i < titles.size(); i++) {
            if (i == lineFields.length - 1 && titles.size() > lineFields.length) {
                // En la última línea juntamos los títulos que sobren
                setText(form, lineFields[i], String.join("; ", titles.subList(i, titles.size())));
            } else {
                setText(form, lineFields[i], titles.get(i));
            }
        }
    }

    private String venue(Concert c) {
        String name = c.getVenueName();
        String city = c.getCity();
        if (notBlank(name) && notBlank(city)) {
            return name + " (" + city + ")";
        }
        if (notBlank(name)) {
            return name;
        }
        return city;
    }

    private String fullName(Musician m) {
        String first = m.getFirstName() != null ? m.getFirstName() : "";
        String last = m.getLastName() != null ? m.getLastName() : "";
        return (first + " " + last).trim();
    }

    private String formatPrice(float price) {
        return price > 0f ? String.valueOf(price) : null;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private void setText(PDAcroForm form, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        try {
            PDField field = form.getField(name);
            if (field instanceof PDTextField textField) {
                textField.setValue(value);
            }
        } catch (Exception e) {
            // Campo ausente o problemático: lo ignoramos para no romper la generación.
        }
    }

    private void check(PDAcroForm form, String name) {
        try {
            PDField field = form.getField(name);
            if (field instanceof PDCheckBox checkBox) {
                checkBox.check();
            }
        } catch (Exception e) {
            // ignoramos
        }
    }
}
