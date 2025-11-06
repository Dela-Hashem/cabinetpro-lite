package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.CustomerUpdateRequestDto;
import com.cabinetpro.lite.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock CustomerDao customerDao;
    @InjectMocks CustomerServiceImpl service;

    @Test
    void create_ok() throws Exception {
        CustomerCreateRequestDto req = new CustomerCreateRequestDto();
        req.setFullName("Zoe");
        when(customerDao.create(any())).thenReturn(5L);

        Long id = service.create(req);
        assertEquals(5L, id);
        verify(customerDao).create(any(Customer.class));
    }

    @Test
    void update_ok() throws Exception {
        CustomerUpdateRequestDto req = new CustomerUpdateRequestDto();
        req.setFullName("New Name");
        when(customerDao.update(any())).thenReturn(true);

        assertTrue(service.update(7L, req));
        verify(customerDao).update(any(Customer.class));
    }

    @Test
    void findAll_ok() throws Exception {
        when(customerDao.findAll()).thenReturn(List.of(
                new Customer(1L, "A", null, null),
                new Customer(2L, "B", null, null)
        ));
        assertEquals(2, service.findAll().size());
        verify(customerDao).findAll();
    }

    @Test
    void findById_ok() throws Exception {
        when(customerDao.findById(9L)).thenReturn(Optional.of(new Customer(9L, "N", null, null)));
        assertTrue(service.findById(9L).isPresent());
        verify(customerDao).findById(9L);
    }

    @Test
    void delete_ok() throws Exception {
        when(customerDao.deleteById(3L)).thenReturn(true);
        assertTrue(service.delete(3L));
        verify(customerDao).deleteById(3L);
    }
}
