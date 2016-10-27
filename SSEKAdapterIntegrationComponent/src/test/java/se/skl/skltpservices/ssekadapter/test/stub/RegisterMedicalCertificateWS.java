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


import se.inera.ifv.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;
import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

/**
 * Created by Peter on 2014-07-30.
 */
@WebService(serviceName = "RegisterMedicalCertificateResponderService",
        endpointInterface = "se.inera.ifv.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface",
        portName = "RegisterMedicalCertificateResponderPort",
        targetNamespace = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20")
public class RegisterMedicalCertificateWS implements RegisterMedicalCertificateResponderInterface {
	
	@Override
	public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress,
			RegisterMedicalCertificateType parameters) {

		System.out.println("So far so good");
		final RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

		ResultOfCall value = new ResultOfCall();
		value.setResultCode(ResultCodeEnum.OK);
		value.setInfoText("Returning result");
		response.setResult(value);

        return response;
	}

}
