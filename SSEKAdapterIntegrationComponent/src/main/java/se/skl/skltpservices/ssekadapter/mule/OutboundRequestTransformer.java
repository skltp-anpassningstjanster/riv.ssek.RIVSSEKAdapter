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
package se.skl.skltpservices.ssekadapter.mule;


import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import se.skl.skltpservices.ssekadapter.mapper.Mapper;
import se.skl.skltpservices.ssekadapter.mapper.error.AdapterException;
import se.skl.skltpservices.ssekadapter.util.Sample;

/**
 * Transforms standard RIV Service Contract requests from consumers to a corresponding SSEK request. <p/>
 *
 * @see se.skl.skltpservices.ssekadapter.mapper.AbstractMapper
 *
 * @author Peter
 */
public class OutboundRequestTransformer extends AbstractOutboundTransformer {

    @Override
    public Object transformMessage(final MuleMessage message, final String outputEncoding) throws TransformerException {
   	try {
            final Mapper mapper = getMapper(message);
            final Sample sample = new Sample(mapper.getClass().getSimpleName() + ".mapRequest");
            try {
            	MuleMessage msg = mapper.mapRequest(message);
                return sample.ok(msg);
			} finally {
                sample.end();
           }
        } catch (AdapterException err) {
            throw new TransformerException(this, err);
        }
    }
}

