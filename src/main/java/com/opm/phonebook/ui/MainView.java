package com.opm.phonebook.ui;

import com.opm.phonebook.model.Contact;
import com.opm.phonebook.service.ContactService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.reports.PrintPreviewReport;

@Route("")
public class MainView extends VerticalLayout {
    private final ContactService contactService;
    private final Grid<Contact> grid;
    private final ContactForm form;

    @Autowired
    public MainView(ContactService contactService) {
        this.contactService = contactService;
        this.grid = new Grid<>(Contact.class);
        this.form = new ContactForm();

        addClassName("main-view");
        setSizeFull();

        configureGrid();
        configureForm();

        Button addContactButton = new Button("Add Contact", e -> {
            form.setContact(new Contact());
            form.setVisible(true);
        });

        PrintPreviewReport contactsReport = new PrintPreviewReport(Contact.class, "firstName", "lastName", "phoneNumber", "email", "address");
        contactsReport.setItems(contactService.findAll());
        contactsReport.getReportBuilder().setTitle("Contacts");
        StreamResource csv = contactsReport.getStreamResource("contacts.csv", contactService::findAll, PrintPreviewReport.Format.CSV);
        Button exportButton = new Button("CSV");
        Anchor downloadLink = new Anchor(csv, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(exportButton);
        HorizontalLayout toolbar = new HorizontalLayout(addContactButton, downloadLink);

        add(toolbar, grid, form);
        form.setVisible(false);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "phoneNumber", "email", "address");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private void configureForm() {
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateList() {
        grid.setItems(contactService.findAll());
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
        }
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.delete(event.getContact().getId());
        updateList();
        closeEditor();
    }
}