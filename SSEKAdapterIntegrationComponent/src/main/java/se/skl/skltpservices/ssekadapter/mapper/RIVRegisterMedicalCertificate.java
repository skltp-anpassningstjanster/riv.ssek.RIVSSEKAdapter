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

import org.mule.api.MuleMessage;

import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.skl.skltpservices.ssekadapter.mapper.error.MapperException;


public class RIVRegisterMedicalCertificate extends RegisterMedicalCertificateMapper {

    @Override
    public MuleMessage mapRequest(final MuleMessage message) throws MapperException {
        try {
            final RegisterMedicalCertificateType request = unmarshal(payloadAsXMLStreamReader(message));
            // map to baseline model
            message.setPayload(marshal(request));
            return message;
        } catch (Exception err) {
            throw new MapperException("Error when transforming request", err);
        }
    }

    @Override
    public MuleMessage mapResponse(final MuleMessage message) throws MapperException {
        try {
            final RegisterMedicalCertificateResponseType response = unmarshalResponse(payloadAsXMLStreamReader(message));
            // map to baseline model
            message.setPayload(marshal(response));
            return message;
        } catch (Exception err) {
            throw new MapperException("Error when transforming response", err);
        }
    }
}
