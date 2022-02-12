package com.infotaf.restapi.service;

import com.infotaf.restapi.data.ParamDao;
import com.infotaf.restapi.model.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ParamService implements IParamService {

    private static final Logger logger = LoggerFactory.getLogger(ParamService.class);

    @Autowired
    Environment env;

    @Autowired
    protected ParamDao paramDao;

    @Transactional(readOnly = false)
    public int saveParam(String key, String value) {
        logger.debug("IN - key : {} value :{}", key, value);
        return paramDao.saveParam(key, value);
    }

    @Transactional(readOnly = true)
    public Param getParam(String key) {
        logger.debug("IN - key : {}", key);
        return paramDao.getParam(key);
    }

    /**
     * Récupération de la date de mise à jour enregistrée en base
     *
     * @return date de mise à jour enregistrée en base
     * @throws ParseException
     */
    @Transactional(readOnly = true)
    public Date getDateFromParam() throws ParseException {
        String key = env.getRequiredProperty("param.key.updatedate");
        Param updateDateParam = this.getParam(key);
        String updateDateString = "";
        if (updateDateParam != null) {
            updateDateString = updateDateParam.getValue();
        }
        Date updateDate = null;
        if (!updateDateString.equals("")) {
            String dateFormat = (String) env.getRequiredProperty("infotaf.dateformat");
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            updateDate = formatter.parse(updateDateString);
        }

        return updateDate;
    }
}
