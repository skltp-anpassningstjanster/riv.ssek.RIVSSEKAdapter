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
package se.skl.skltpservices.ssekadapter.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.mule.api.MuleMessage;

import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;
import se.skl.skltpservices.ssekadapter.mapper.AbstractMapper;
import se.skl.skltpservices.ssekadapter.mapper.RegisterMedicalCertificateMapper;

/**
 * Created by Peter on 2014-07-28.
 */
public class RegisterMedicalCertificateMapperTest {

    private static SimpleDateFormat timeStampFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    static {
        timeStampFormatter.setLenient(false);
    }

    // Make it easy to dump the resulting response after createTS (for dev purposes only)
    @XmlRootElement
    static class Root {
        @XmlElement
        private RegisterMedicalCertificateResponseType type;
    }

    //
    private void dump(final RegisterMedicalCertificateResponseType responseType) {
        Root root = new Root();
        root.type = responseType;
        //Util.dump(root);
    }

	private RegisterMedicalCertificateMapper getRegisterMedicalCertificateMapper() {
		RegisterMedicalCertificateMapper mapper = (RegisterMedicalCertificateMapper) AbstractMapper.getInstance(AbstractMapper.NS_EN_EXTRACT, AbstractMapper.NS_REGISTERMEDICALCERTIFICATE_3);
		return mapper;
	}

    @Test
    public void testMapFromEhrToCareContracts() {
        MuleMessage mockMessage = mock(MuleMessage.class);
        when(mockMessage.getUniqueId()).thenReturn("1234");
        RegisterMedicalCertificateMapper mapper = getRegisterMedicalCertificateMapper();
        
        RegisterMedicalCertificateResponseType r = new RegisterMedicalCertificateResponseType();
        
        ResultOfCall roc = new ResultOfCall();
        roc.setInfoText("OK");
        roc.setResultCode(ResultCodeEnum.OK);       
        r.setResult(roc);
        RegisterMedicalCertificateResponseType responseType = mapper.mapResponse(r, mockMessage);
        assertNotNull(responseType);

        dump(responseType);

        assertNotNull(responseType.getResult());
        assertEquals("OK", responseType.getResult().getInfoText());
    }

    
}
