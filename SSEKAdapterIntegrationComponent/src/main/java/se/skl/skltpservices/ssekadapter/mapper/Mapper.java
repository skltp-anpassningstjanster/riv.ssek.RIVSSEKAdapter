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

import se.skl.skltpservices.ssekadapter.mapper.error.MapperException;

/**
 * The generic mapper interface. <p/>
 *
 * All mappers have to be stateless and thread-safe.
 *
 * @author Peter
 */
public interface Mapper {

    /**
     * Parses a RIV service contract request from the javax.xml.stream.XMLStreamReader and maps
     * the object to the actual object representation
     * which is returned as a XML String
     *
     * @param message the message to parse the RIV service contract request from.
     * @return the target message with payload.
     */
    MuleMessage mapRequest(MuleMessage message) throws MapperException;

    /**
     * Parses a response from the javax.xml.stream.XMLStreamReader and maps the object to the actual
     * RIV service contract representation
     *
     * @param message the message to parse.
     * @return the target message with payload as specified by the actual the RIV service contract.
     */
    MuleMessage mapResponse(MuleMessage message) throws MapperException;
}
