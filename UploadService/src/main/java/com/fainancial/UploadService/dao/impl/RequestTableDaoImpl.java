package com.fainancial.UploadService.dao.impl;

import com.fainancial.UploadService.dao.RequestTableDao;
import com.fainancial.UploadService.repository.RequestTableRepository;
import com.fainancial.model.RequestTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RequestTableDaoImpl implements RequestTableDao {

    @Autowired
    private RequestTableRepository requestTableRepository;

    @Override
    public RequestTable save(RequestTable requestTable) {
        log.info("Saving data {} in mongo", requestTable);
        return requestTableRepository.save(requestTable);
    }
}
