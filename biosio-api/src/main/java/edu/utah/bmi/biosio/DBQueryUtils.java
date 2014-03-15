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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import edu.utah.bmi.biosio.model.Citation;
import edu.utah.bmi.biosio.model.Concept;
import edu.utah.bmi.biosio.model.ExternalOntology;
import edu.utah.bmi.biosio.model.Mapping;
import edu.utah.bmi.biosio.model.Relationship;
import edu.utah.bmi.biosio.model.Synonym;

public class DBQueryUtils {
	
	private SessionFactory sessionFactory = null;
		
	public DBQueryUtils(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	public DBQueryUtils(ApplicationContext context){
		this.sessionFactory = (SessionFactory)context.getBean("sessionFactory");
	}
	
	/**
	 * Get list of concepts
	 * @return List of concepts
	 * @throws Exception
	 */
	public List<Concept> getConcepts() throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Concept> concepts = session.createQuery("from Concept").list();
	        session.getTransaction().commit();
	        session.close();
	        
	        return concepts;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get concept by CUI
	 * @param cui CUI
	 * @return Matching concept
	 * @throws Exception
	 */
	public Concept getConceptByID(String cui) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Concept> concepts = session.createQuery("from Concept where cui='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        if (concepts.size()>0)
	        	return concepts.get(0);
	        else return null;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get list of terms for a given concept (through the CUI)
	 * @param cui
	 * @return
	 * @throws Exception
	 */
	public List<Synonym> getConceptTerms(String cui) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Synonym> synonyms = session.createQuery("from Synonym where cui='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        
	        return synonyms;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get relationships for a given concept (through the CUI)
	 * @param cui CUI
	 * @return Matching relationships
	 * @throws Exception
	 */
	public List<Relationship> getConceptRelationships(String cui) throws Exception {
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Relationship> relationships = session.createQuery("from Relationship where cui1='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        return relationships;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get relationships targeting a given concept (through the CUI)
	 * @param cui CUI
	 * @return Matching relationships
	 * @throws Exception
	 */
	public List<Relationship> getRelationshipsTargetingConcept(String cui) throws Exception {
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Relationship> relationships = session.createQuery("from Relationship where cui2='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        return relationships;
		}
		catch(org.hibernate.ObjectNotFoundException ne){
			if (session!=null && session.isOpen())
				session.close();
			return null;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get citation
	 * @return Citation
	 * @throws Exception
	 */
	public Citation getCitation(String str) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Citation> citations = session.createQuery(
					"from Citation where str='" + str.replaceAll("'", "''") + "'").list();
	        session.getTransaction().commit();
	        session.close();
	        if (citations.size()>0)
	        	return citations.get(0);
	        else return null;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get citations associated to a given concept (through the CUI)
	 * @param cui CUI
	 * @return List of matching citations
	 * @throws Exception
	 */
	public List<Citation> getCitationsByCUI(String cui) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Citation> citations = session.createQuery("select con.citations from Concept con where con.cui='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        
	        return citations;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}

	public List<Mapping> getOntologyMappingsByCUI(String cui) throws Exception {
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<Mapping> mappings = session.createQuery("from Mapping where cui='"+cui+"'").list();
	        session.getTransaction().commit();
	        session.close();
	        
	        return mappings;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
	
	/**
	 * Get list of defined ontologies
	 * @return List of defined ontologies
	 * @throws Exception 
	 */
	public List<ExternalOntology> getExternalOntologies() throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
	        session.beginTransaction();
			List<ExternalOntology> ontologies = session.createQuery("from ExternalOntology").list();
	        session.getTransaction().commit();
	        session.close();
	        
	        return ontologies;
		}
		catch (Exception e){
			e.printStackTrace();
			if (session!=null && session.isOpen())
				session.close();
			throw e;
		}
	}
}
