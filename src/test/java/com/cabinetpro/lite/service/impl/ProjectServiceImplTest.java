package com.cabinetpro.lite.service.impl;

import com.cabinetpro.lite.dao.CustomerDao;
import com.cabinetpro.lite.dao.ProjectDao;
import com.cabinetpro.lite.dto.CustomerCreateRequestDto;
import com.cabinetpro.lite.dto.ProjectCreateForCustomerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock CustomerDao customerDao;
    @Mock ProjectDao projectDao;

    @InjectMocks
    ProjectServiceImpl service; // همون ایمپل سرویست

    @Test
    void createCustomerWithFirstProject_success() throws Exception {
        // arrange
        CustomerCreateRequestDto cust = new CustomerCreateRequestDto();
        cust.setFullName("Maryam A.");
        cust.setPhone("0400 555 123");
        cust.setEmail("maryam@example.com");

        ProjectCreateForCustomerDto proj = new ProjectCreateForCustomerDto();
        proj.setTitle("Laundry fitout");
        proj.setAddress("Nollamara WA");

        Mockito.when(customerDao.create(ArgumentMatchers.any())).thenReturn(10L);
        Mockito.when(projectDao.create(ArgumentMatchers.any())).thenReturn(20L);

        // act
        Long resultId = service.createCustomerWithFirstProject(cust, proj);

        // assert
        Assertions.assertEquals(10L, resultId);

        // ترتیب صحیح: اول مشتری، بعد پروژه با همان customerId
        InOrder inOrder = Mockito.inOrder(customerDao, projectDao);
        inOrder.verify(customerDao).create(ArgumentMatchers.argThat(c -> c.getId() == null && "Maryam A.".equals(c.getFullName())));
        inOrder.verify(projectDao).create(ArgumentMatchers.argThat(p ->
                p.getCustomerId() == 10L && "Laundry fitout".equals(p.getTitle())
        ));

        Mockito.verifyNoMoreInteractions(customerDao, projectDao);
    }

    @Test
    void createCustomerWithFirstProject_projectFails_propagatesException() throws Exception {
        CustomerCreateRequestDto cust = new CustomerCreateRequestDto();
        cust.setFullName("X");

        ProjectCreateForCustomerDto proj = new ProjectCreateForCustomerDto();
        proj.setTitle("Y");

        Mockito.when(customerDao.create(ArgumentMatchers.any())).thenReturn(33L);
        Mockito.when(projectDao.create(ArgumentMatchers.any())).thenThrow(new SQLException("boom"));

        SQLException ex = Assertions.assertThrows(SQLException.class,
                () -> service.createCustomerWithFirstProject(cust, proj));

        Assertions.assertTrue(ex.getMessage().contains("boom"));
        Mockito.verify(customerDao).create(ArgumentMatchers.any());
        Mockito.verify(projectDao).create(ArgumentMatchers.argThat(p -> p.getCustomerId() == 33L));
    }
}
