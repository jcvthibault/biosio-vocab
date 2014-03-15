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

import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.utah.bmi.biosio.DBQueryUtils;
import edu.utah.bmi.biosio.model.Citation;
import edu.utah.bmi.biosio.model.Concept;
import edu.utah.bmi.biosio.model.Description;
import edu.utah.bmi.biosio.model.Synonym;

/**
 * 
 * @author Julien Thibault, University of Utah
 *
 */
public class QueryTest
{
	@Test
    public void DBQuery() throws Exception
    {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
		DBQueryUtils queryService = new DBQueryUtils((SessionFactory)context.getBean("sessionFactory"));
    	
    	List<Concept> concepts = queryService.getConcepts();
    	int c = 0;
        for (Concept concept : concepts){
        	System.out.println("------------------------------------");
        	System.out.println("[" + concept.getCui() + "] " + concept.getTerm());
        	
        	Set<Description> descriptions = concept.getDescriptions();
	        for (Description desc : descriptions){
	        	System.out.println(desc.getDescription());
	        }
	        
        	List<Synonym> synonyms = queryService.getConceptTerms(concept.getCui());
	        for (Synonym syn : synonyms){
	        	System.out.println("\t [" + syn.getId() + "] " + syn.getTerm() +" ("+ syn.getLanguage() + ")");
	        }
	        
	    	List<Citation> citations = queryService.getCitationsByCUI(concept.getCui());
	    	if (citations.size()>0){
		    	System.out.println("Citations:");
		        for (Citation citation : citations){
		        	System.out.println("\t" + citation.getSource() +" "+ citation.getYear() + " ");
		        }
	    	}
	    	c++;
	    	if (c==20)
	    		break;
        }
    }
}
