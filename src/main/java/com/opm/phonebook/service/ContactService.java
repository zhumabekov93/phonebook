package com.opm.phonebook.service;

import com.opm.phonebook.model.Contact;
import com.opm.phonebook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public void delete(String id) {
        contactRepository.deleteById(id);
    }

    public Contact findById(String id) {
        return contactRepository.findById(id).orElse(null);
    }
}
