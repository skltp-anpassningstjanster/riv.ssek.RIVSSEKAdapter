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
package se.skl.skltpservices.ssekadapter.test;

import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by Peter on 2014-07-30.
 */
public class Util {

    public static final String REGISTERMEDICALCERTIFICATE_TEST_FILE_1 = "/data/Underskning_unrecognised_type.xml";
    public static final String REGISTERMEDICALCERTIFICATE_TEST_FILE_2 = "/data/Underskning_unrecognised_type.xml";
    public static final String REGISTERMEDICALCERTIFICATE_TEST_FILE_3 = "/data/Underskning_unrecognised_type.xml";
    public static final String REGISTERMEDICALCERTIFICATE_TEST_FILE_4 = "/data/Underskning_unrecognised_type.xml";

    public static <T> void dump(final T jaxbObject) throws JAXBException {
        dump(jaxbObject, new OutputStreamWriter(System.out));
    }

    public static <T> void dump(final T jaxbObject, Writer writer) throws JAXBException {
        final JAXBContext context;
        context = JAXBContext.newInstance(jaxbObject.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(jaxbObject, writer);
    }

}
