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

package edu.utah.bmi.biosio.rdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import edu.utah.bmi.biosio.DBQueryUtils;
import edu.utah.bmi.biosio.model.Citation;
import edu.utah.bmi.biosio.model.Concept;
import edu.utah.bmi.biosio.model.Description;
import edu.utah.bmi.biosio.model.ExternalOntology;
import edu.utah.bmi.biosio.model.Mapping;
import edu.utah.bmi.biosio.model.Relationship;
import edu.utah.bmi.biosio.model.Synonym;

/**
 * RDF exporter. Exports all stored concepts to a single file in Turtle/RDF format. 
 * @author Julien Thibault, University of Utah
 *
 */
public class DbToRdfExporter {

	private String rdfOntoName;
	private String rdfOntoUri;
	private String rdfOntoPrefix;
	private String rdfOntoDescription;
	private String rdfOntoAuthor;
	private SessionFactory sessionFactory = null;
	
	/**
	 * New exporter
	 * @param rdfOntoName Name of the ontology.
	 * @param rdfOntoDescription Vocabulary description
	 * @param rdfOntoPrefix Prefix to use to represent the ontology in the RDF file.
	 * @param rdfOntoUri Vocabulary URI
	 * @param rdfOntoAuthor Author
	 */
	public DbToRdfExporter(
			ApplicationContext context,
			String rdfOntoName, 
			String rdfOntoDescription, 
			String rdfOntoPrefix, 
			String rdfOntoUri,
			String rdfOntoAuthor){
		this.setRdfOntologyName(rdfOntoName);
		this.setRdfOntologyPrefix(rdfOntoPrefix);
		this.setRdfOntologyUri(rdfOntoUri);
		this.setRdfOntologyDescription(rdfOntoDescription);
		this.setRdfOntologyAuthor(rdfOntoAuthor);
		this.sessionFactory = (SessionFactory)context.getBean("sessionFactory");
	}
	
	/**
	 * Export ontology concepts from the database to a single Turtle/RDF file
	 * @param filePath Path to the output file
	 * @throws Exception 
	 */
	public void exportToFile(String filePath) throws Exception {
		BufferedWriter bw = null;
		File file = new File(filePath);
		try{
			bw = new BufferedWriter(new FileWriter(file));
			
			DBQueryUtils queryService = new DBQueryUtils(this.sessionFactory);
			
			//ontology prefixes
			bw.append("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
			bw.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n");
			bw.append("@prefix owl: <http://www.w3.org/2002/07/owl#> .\n");
			bw.append("@prefix dc: <http://purl.org/dc/elements/1.1/> .\n");
			bw.append("@prefix dct: <http://purl.org/dc/terms/> .\n");
			bw.append("@prefix "+rdfOntoPrefix+": <"+rdfOntoUri+"#> .\n");
	        List<ExternalOntology> ontologies = queryService.getExternalOntologies();
	        if (ontologies!=null){
	        	for (ExternalOntology ontology : ontologies){
	        		bw.append("@prefix "+ontology.getId().toLowerCase()+": <"+ontology.getPrefix()+"> .\n");
	        	}
	        }
			bw.append("@base <"+rdfOntoUri+"> .\n");

			//ontology information
			bw.append("\n<" + rdfOntoUri + ">\n");
			bw.append("  rdf:type owl:Ontology ;\n");
			/*//ontology imports
	        if (ontologies!=null){
	        	for (ExternalOntology ontology : ontologies){
	        		bw.append("   owl:imports <"+ontology.getDocumentURL()+"> ;\n");
	        	}
	        }*/
			bw.append("  dc:title \""+rdfOntoDescription+"\" ;\n");
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
			bw.append("  dc:date \""+formatter.format(new Date())+"\" ;\n");
			bw.append("  dc:creator \""+rdfOntoAuthor+"\" .\n");
			
	        System.out.println("Retrieving concepts from database...");
	    	List<Concept> concepts = queryService.getConcepts();
	        System.out.println(" -> " + concepts.size() + " concepts and relationships found.\nExporting...");
	    	int c = 0;
	    	int r = 0;
	    	
	    	//add citation as possible annotation property
	    	String citationPropertyStr = "\n"+rdfOntoPrefix+":citation rdf:type owl:AnnotationProperty ;\n";
	    	citationPropertyStr += "  rdfs:label \"citation\"@en ;\n";
	    	citationPropertyStr += "  rdfs:comment \"Reference to scientific literature or online resource\"@en ;\n";
	    	citationPropertyStr += "  rdfs:subClassOf <http://purl.obolibrary.org/obo/IAO_0000115> .\n";
	    	
	    	bw.append(citationPropertyStr);
	    	
	    	//separate concepts (classes) and relationship definitions (properties)
    		List<Concept> conceptDefinitions = new ArrayList<Concept>();
	    	for (Concept concept : concepts){
	        	if (concept.getIsRelationship()){	
	        		//relationshipDefinitions.put(getNormalizedConceptTerm(concept.getTerm()), concept);
	        		String relTerm = getNormalizedConceptTerm(concept.getTerm());
	        		if (!relTerm.equals("is_a")){
	        			String relationshipStr = "\n"+rdfOntoPrefix+":"+concept.getCui() + " rdf:type owl:ObjectProperty ;\n";
    					//relationship terms
    	        		List<Synonym> synonyms = queryService.getConceptTerms(concept.getCui());
    			        for (Synonym synonym : synonyms){
    			        	relationshipStr += "  rdfs:label \""+synonym.getTerm() + "\"@" + synonym.getLanguage().toLowerCase() + " ;\n";
    			        }
    					//relationship definitions
    			        Set<Description> descriptions = concept.getDescriptions();
    			        for (Description desc : descriptions){
    			        	relationshipStr += "  rdfs:comment \""+desc.getDescription() + "\"@" + desc.getLanguage().toLowerCase() + " ;\n";
    			        }
    			        bw.append(relationshipStr.substring(0, relationshipStr.length()-2) + ".\n");
	        			r++;
	        		}
	        	}
	        	else{
	        		conceptDefinitions.add(concept);
					c++;
	        	}
	        }
	    	//get concepts (classes)
	        for (Concept concept : conceptDefinitions){

        		StringBuilder conceptStringBuilder = new StringBuilder();
		        	
        		//concept
		        conceptStringBuilder.append("\n"+rdfOntoPrefix+":"+concept.getCui() + " rdf:type owl:Class ;\n");
		        //conceptStringBuilder.append("\n"+rdfOntoPrefix+":"+getNormalizedConceptTerm(concept.getTerm()) + " rdf:type rdfs:Class;\n");

		        //concept terms
        		List<Synonym> synonyms = queryService.getConceptTerms(concept.getCui());
		        for (Synonym synonym : synonyms){
		        	conceptStringBuilder.append("  rdfs:label \""+synonym.getTerm() + "\"@" + synonym.getLanguage().toLowerCase() + " ;\n");
		        }
				//definitions
		        Set<Description> descriptions = concept.getDescriptions();
		        for (Description desc : descriptions){
		        	conceptStringBuilder.append("  rdfs:comment \""+desc.getDescription() + "\"@" + desc.getLanguage().toLowerCase() + " ;\n");
		        }
				
		        //citations
		        List<Citation> citations = queryService.getCitationsByCUI(concept.getCui());
		        if (citations!=null){
			        for (Citation cit : citations){
			        	conceptStringBuilder.append("  " + rdfOntoPrefix+":citation \"" + cit.getText() + "\" ;\n");
			        }
		        }

		        //relationships
        		List<Relationship> relationships = queryService.getConceptRelationships(concept.getCui());
		        for (Relationship rel : relationships){
		        	Concept conceptTo = rel.getConceptTo();
		        	Concept conceptRel = rel.getRelationshipDefinition();
		        	//String relTerm = conceptRel.getCui();
		        	String concept2 = conceptTo.getCui();
		        	String relTerm = getNormalizedConceptTerm(conceptRel.getTerm());
		        	//String concept2 = getNormalizedConceptTerm(conceptTo.getTerm());
		        	if (relTerm.equals("is_a")){
		        		conceptStringBuilder.append("  rdfs:subClassOf "+rdfOntoPrefix+":" + concept2 + " ;\n");
		        	}
		        	else if (relTerm.equals("has_part"))
		        	{
		        		conceptStringBuilder.append("  <http://purl.obolibrary.org/obo/BFO_0000051> "+rdfOntoPrefix+":" + concept2 + " ;\n");
		        	}
		        	else{
		        		conceptStringBuilder.append(
		        				"  "+rdfOntoPrefix+":" + conceptRel.getCui() 
		        				+ " "+rdfOntoPrefix+":" + concept2 + ";\n");
		        	}
		        }
		        
		        String conceptString = conceptStringBuilder.toString();
		        bw.append(conceptString.substring(0, conceptString.length()-2) + ".\n");
		        
		        // add mappings ('subclassOf' relationships with other ontology concepts)
		        List<Mapping> ontologyMappings = queryService.getOntologyMappingsByCUI(concept.getCui());
		        if (ontologyMappings!=null){
		        	for (Mapping ontologyMapping : ontologyMappings){
		        		String concept2 = ontologyMapping.getOntologyDefinition().getId().toLowerCase() + ":" + ontologyMapping.getOntologyTerm();
		        		bw.append("\n"+rdfOntoPrefix+":"+concept.getCui() +" rdf:type owl:Class ; \n");
		        		bw.append("  rdfs:subClassOf "+ concept2 + " .\n");
		        	}
		        }
	        }
	        System.out.println(" -> " + c + " concepts (RDF classes) and "+ r +" relationship definitions (RDF properties) exported!");
			bw.close();
		}
		catch (Exception e){
			if (bw!=null){
				try{
					bw.close();
				} catch (IOException ioe){
					ioe.printStackTrace();
				}
			}
			throw e;
		}
	}
	
	private String getNormalizedConceptTerm(String term){
		return term.replaceAll("[\\[\\(\\)\\]\\/ ]", "_")
				.replaceAll("\\.", "_dot_")
				.replaceAll("\\+", "_plus_")
				.replaceAll("\\*", "_star_")
				.replaceAll("#", "_sharp_")
				.replaceAll("@", "_at_")
				.replaceAll("\\$", "_dollar_")
				.replaceAll("\\%", "_percent_");
	}

	/**
	 * Get ontology name
	 * @return Vocabulary name
	 */
	public String getRdfOntologyName() {
		return rdfOntoName;
	}

	/**
	 * Set ontology name
	 * @param rdfOntoName Vocabulary name
	 */
	public void setRdfOntologyName(String rdfOntoName) {
		this.rdfOntoName = rdfOntoName;
	}

	/**
	 * Get ontology prefix to use in RDF file
	 * @return Vocabulary prefix to use in RDF file
	 */
	public String getRdfOntologyPrefix() {
		return rdfOntoPrefix;
	}

	/**
	 * Set ontology prefix to use in RDF file
	 * @param rdfOntoPrefix Vocabulary prefix to use in RDF file
	 */
	public void setRdfOntologyPrefix(String rdfOntoPrefix) {
		this.rdfOntoPrefix = rdfOntoPrefix;
	}

	/**
	 * Get ontology URI to use in RDF file
	 * @return Vocabulary URI
	 */
	public String getRdfOntologyUri() {
		return rdfOntoUri;
	}

	/**
	 * Set ontology URI to use in RDF file
	 * @param rdfOntoUri Vocabulary URI
	 */ 
	public void setRdfOntologyUri(String rdfOntoUri) {
		this.rdfOntoUri = rdfOntoUri;
	}

	/** 
	 * Get ontology description
	 * @return Vocabulary description
	 */
	public String getRdfOntologyDescription() {
		return rdfOntoDescription;
	}

	/**
	 * Set ontology description
	 * @param rdfOntoDescription Vocabulary description
	 */
	public void setRdfOntologyDescription(String rdfOntoDescription) {
		this.rdfOntoDescription = rdfOntoDescription;
	}

	/**
	 * Get author of ontology
	 * @return Author of ontology
	 */
	public String getRdfOntologyAuthor() {
		return rdfOntoAuthor;
	}

	/**
	 * Set author of ontology
	 * @param rdfOntoAuthor Author of ontology
	 */
	public void setRdfOntologyAuthor(String rdfOntoAuthor) {
		this.rdfOntoAuthor = rdfOntoAuthor;
	}
}
