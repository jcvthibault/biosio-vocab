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

import edu.utah.bmi.biosio.skos.DbToSkosExporter;

/**
 * Test suite for SKOS exporter
 * @author Julien Thibault, University of Utah
 *
 */
public class DbToSkosTest
{
	@Test
    public void SKOSExport() throws Exception
    {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		DbToSkosExporter exporter = new DbToSkosExporter(
				context,
				"IBIOMES", 
				"Vocabulary for biomolecular simulations", 
				"ib",
				"http://edu.utah.bmi.ibiomes/skos/ibiomes.owl",
				"Julien Thibault");
		exporter.exportToFile("target/ibiomes-skos.ttl");
    }
}
