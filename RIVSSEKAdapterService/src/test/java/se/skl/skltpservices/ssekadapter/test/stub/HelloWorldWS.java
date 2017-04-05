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


import javax.jws.WebService;

import org.ssek.schemas.helloworld._2011_11_17.HelloWorldRequest;
import org.ssek.schemas.helloworld._2011_11_17.HelloWorldResponse;
import org.ssek.schemas.helloworld._2011_11_17.wsdl.HelloWorldPortType;

@WebService(serviceName = "HelloWorldService",
        endpointInterface = "org.ssek.schemas.helloworld._2011_11_17.wsdl.HelloWorldPortType",
        portName = "HelloWorldPortType",
        targetNamespace = "http://schemas.ssek.org/helloworld/2011-11-17/wsdl", name = "HelloWorldPortType")
public class HelloWorldWS implements HelloWorldPortType {

	@Override
	public HelloWorldResponse helloWorld(HelloWorldRequest body) {
		HelloWorldResponse response = new HelloWorldResponse();
		response.setMessage("OK, recieved " + body.getMessage());
		return response;
	}
	


}
