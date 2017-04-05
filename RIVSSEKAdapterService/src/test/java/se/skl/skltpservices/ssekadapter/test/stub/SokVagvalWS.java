/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.ssekadapter.test.stub;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.skltpservices.ssekadapter.mapper.AbstractMapper;
import se.skl.skltpservices.ssekadapter.util.SpringPropertiesUtil;
import skl.tp.vagvalsinfo.v2.HamtaAllaAnropsBehorigheterResponseType;
import skl.tp.vagvalsinfo.v2.HamtaAllaVirtualiseringarResponseType;
import skl.tp.vagvalsinfo.v2.SokVagvalsInfoInterface;
import skl.tp.vagvalsinfo.v2.VirtualiseringsInfoType;

/**
 * Stub service for retrieving all VirtualiseringsInfoType.
 * 
 * Only used by Integration tests. Not part of the distribution.
 */
@WebService(serviceName = "SokVagvalsServiceSoap11LitDocService",
        targetNamespace = "urn:skl:tp:vagvalsinfo:v2",
      endpointInterface = "skl.tp.vagvalsinfo.v2.SokVagvalsInfoInterface",
               portName = "SokVagvalsSoap11LitDocPort")
public class SokVagvalWS implements SokVagvalsInfoInterface {
    
    private static final Logger log = LoggerFactory.getLogger(SokVagvalWS.class);

    @PostConstruct
    public void loadTestData() {
        log.info("Load test data");
        try {
            //resetCache();
        } catch (Exception e) {
            log.error("Error loading testdata", e);
        }
    }
    @Override
    @WebMethod
    @WebResult(name = "hamtaAllaVirtualiseringarResponse", targetNamespace = "urn:skl:tp:vagvalsinfo:v2", partName = "response")
    public HamtaAllaVirtualiseringarResponseType hamtaAllaVirtualiseringar(
                                                 @WebParam(partName = "parameters", name = "hamtaAllaVirtualiseringar", targetNamespace = "urn:skl:tp:vagvalsinfo:v2")
                                                 Object parameters) {
        
        // incoming parameters Object is ignored - it needs to be:
        //
        // <soapenv:Body>
        //   <urn:hamtaAllaVirtualiseringar xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true" />
        // </soapenv:Body>
        //
        
    	
        log.info("SokVagvalsInfoInterface hamtaAllaVirtualiseringar");

        final HamtaAllaVirtualiseringarResponseType responseType = new HamtaAllaVirtualiseringarResponseType();
        VirtualiseringsInfoType infoType = new VirtualiseringsInfoType();
        infoType.setReceiverId("SSEK-1");
        infoType.setRivProfil("RIVTABP20");
        infoType.setTjansteKontrakt(AbstractMapper.NS_RIV_RMC);
        infoType.setVirtualiseringsInfoId("ID-1");
        infoType.setAdress(SpringPropertiesUtil.getProperty("ENDPOINT_HTTPS_SSEK_STUB"));
        infoType.setFromTidpunkt(fromNow(-2));
        infoType.setTomTidpunkt(null);
        responseType.getVirtualiseringsInfo().add(infoType);

        infoType = new VirtualiseringsInfoType();
        infoType.setReceiverId("SSEK-2");
        infoType.setRivProfil("RIVTABP20");
        infoType.setTjansteKontrakt(AbstractMapper.NS_RIV_RMC);
        infoType.setVirtualiseringsInfoId("ID-2");
        infoType.setAdress(SpringPropertiesUtil.getProperty("ENDPOINT_HTTP_SSEK_STUB"));
        infoType.setFromTidpunkt(fromNow(-2));
        infoType.setTomTidpunkt(null);
        responseType.getVirtualiseringsInfo().add(infoType);
        
        infoType = new VirtualiseringsInfoType();
        infoType.setReceiverId("SSEK-3");
        infoType.setRivProfil("RIVTABP20");
        infoType.setTjansteKontrakt(AbstractMapper.NS_RIV_HWR);
        infoType.setVirtualiseringsInfoId("ID-2");
        infoType.setAdress(SpringPropertiesUtil.getProperty("OUT_ENDPOINT_HELLO_STUB"));
        infoType.setFromTidpunkt(fromNow(-2));
        infoType.setTomTidpunkt(null);
        responseType.getVirtualiseringsInfo().add(infoType);

        
        return responseType;
    }

    @Override
    /**
     * Throws IllegalArgumentException. Not implemented.
     */
    public HamtaAllaAnropsBehorigheterResponseType hamtaAllaAnropsBehorigheter(Object parameters) {
        throw new IllegalArgumentException("Method is not implemented (not valid in this context)!");
    }

    //
    protected XMLGregorianCalendar fromNow(int days) {
    	try {
        final GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        final XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        return date;
    	} catch (Exception err) {
    		return null;
    	}
    }
}
