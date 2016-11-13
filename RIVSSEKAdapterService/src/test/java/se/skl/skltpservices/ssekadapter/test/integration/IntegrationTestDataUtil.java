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
package se.skl.skltpservices.ssekadapter.test.integration;

import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.v2.PatientType;
import se.inera.mu7263.v3.LakarutlatandeType;
import iso.v21090.dt.v1.II;

public final class IntegrationTestDataUtil {
	private static final String PERSON_ID = "191212121212";
	private static final String PERSON_ID_TYPE = "1.2.752.129.2.1.3";
	public static final int TRIGGER_INFO_MESSAGE = 0;
	public static final int TRIGGER_WARNING_MESSAGE = 1;
	public static final int TRIGGER_ERROR_MESSAGE = 2;
	public static final int NO_TRIGGER = 3;
	

    public static RegisterMedicalCertificateType createRegisterMedicalCertificateType(int triggerType) {
		final RegisterMedicalCertificateType type = new RegisterMedicalCertificateType();
		
		LakarutlatandeType lu = new LakarutlatandeType();
		lu.setKommentar("Test");
		
		PatientType patient = new PatientType();
		II id = new II();
		id.setRoot(PERSON_ID_TYPE);
		id.setExtension(PERSON_ID);
		patient.setPersonId(id );
		lu.setPatient(patient);

		type.setLakarutlatande(lu);
		
		return type;
	}
	
}
