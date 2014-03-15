/*
 * iBIOMES - Integrated Biomolecular Simulations
 * Copyright (C) 2014  Julien Thibault, University of Utah
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.utah.bmi.biosio;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.utah.bmi.biosio.rdf.DbToRdfExporter;

/**
 * Test suite for RDF exporter
 * @author Julien Thibault, University of Utah
 *
 */
public class DbToRdfTest
{
	@Test
    public void RDFExport() throws Exception
    {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		DbToRdfExporter exporter = new DbToRdfExporter(
				context,
				"IBIOMES", 
				"Vocabulary for biomolecular simulations", 
				"ib",
				"http://ibiomes.bmi.utah.edu/ontology/ibiomes.owl",
				"Julien Thibault");
		exporter.exportToFile("target/ibiomes-rdf.ttl");
    }
}
