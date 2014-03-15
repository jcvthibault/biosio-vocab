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

package edu.utah.bmi.biosio.skos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import edu.utah.bmi.biosio.DBQueryUtils;
import edu.utah.bmi.biosio.model.Concept;
import edu.utah.bmi.biosio.model.Description;
import edu.utah.bmi.biosio.model.ExternalOntology;
import edu.utah.bmi.biosio.model.Mapping;
import edu.utah.bmi.biosio.model.Relationship;
import edu.utah.bmi.biosio.model.Synonym;

/**
 * SKOS exporter. Exports all stored concepts to a single file in Turtle/SKOS format. 
 * @author Julien Thibault, University of Utah
 *
 */
public class DbToSkosExporter {

	private String skosVocabularyName;
	private String skosVocabularyUri;
	private String skosVocabularyPrefix;
	private String skosVocabularyDescription;
	private String skosVocabularyAuthor;
	private SessionFactory sessionFactory = null;
	
	/**
	 * New exporter
	 * @param skosVocabularyName Name of the vocabulary.
	 * @param skosVocabularyDescription Vocabulary description
	 * @param skosVocabularyPrefix Prefix to use to represent the vocabulary in the SKOS file.
	 * @param skosVocabularyUri Vocabulary URI
	 * @param skosVocabularyAuthor Author
	 */
	public DbToSkosExporter(
			ApplicationContext context,
			String skosVocabularyName, 
			String skosVocabularyDescription, 
			String skosVocabularyPrefix, 
			String skosVocabularyUri,
			String skosVocabularyAuthor){
		this.setSkosVocabularyName(skosVocabularyName);
		this.setSkosVocabularyPrefix(skosVocabularyPrefix);
		this.setSkosVocabularyUri(skosVocabularyUri);
		this.setSkosVocabularyDescription(skosVocabularyDescription);
		this.setSkosVocabularyAuthor(skosVocabularyAuthor);
		this.sessionFactory = (SessionFactory)context.getBean("sessionFactory");
	}
	
	/**
	 * Export vocabulary concepts from the database to a single Turtle/SKOS file
	 * @param filePath Path to the output file
	 * @throws Exception 
	 */
	public void exportToFile(String filePath) throws Exception{
		BufferedWriter bw = null;
		File file = new File(filePath);
		try{
			bw = new BufferedWriter(new FileWriter(file));
			
			//add header with prefixes
			bw.append("@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n");
			bw.append("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
			bw.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n");
			bw.append("@prefix owl: <http://www.w3.org/2002/07/owl#> .\n");
			bw.append("@prefix dc: <http://purl.org/dc/elements/1.1/> .\n");
			bw.append("@prefix dct: <http://purl.org/dc/terms/> .\n");
			bw.append("@prefix "+skosVocabularyPrefix+": <"+skosVocabularyUri+"#> .\n");
			bw.append("@base <"+skosVocabularyUri+"> .\n");
			
			DBQueryUtils queryService = new DBQueryUtils(this.sessionFactory);

			//ontology imports
	        List<ExternalOntology> ontologies = queryService.getExternalOntologies();
	        if (ontologies!=null){
	        	for (ExternalOntology ontology : ontologies){
	        		bw.append("@prefix "+ontology.getId().toLowerCase()+": <"+ontology.getPrefix()+"#> .\n");
	        	}
	        }
	        
			/*//ontology information
			bw.append("\n<" + skosVocabularyUri + ">\n");
			bw.append("  rdf:type owl:Ontology ;\n");
			bw.append("  dc:title \""+skosVocabularyName+"\" ;\n");
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
			bw.append("  dc:date \""+formatter.format(new Date())+"\" ;\n");
			bw.append("  dc:creator \""+skosVocabularyAuthor+"\" .\n");*/
			
			//SKOS scheme (necessary?)
			bw.append("\n" + skosVocabularyPrefix+":"+skosVocabularyName+" rdf:type skos:ConceptScheme ;\n");
			bw.append("  rdfs:label \""+ skosVocabularyName + "\"@en ;\n");
			bw.append("  rdfs:comment \""+ skosVocabularyDescription + "\"@en ;\n");
			bw.append("  dc:title \""+skosVocabularyDescription+"\";\n");
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
			bw.append("  dc:date \""+formatter.format(new Date())+"\";\n");
			bw.append("  dc:creator \""+skosVocabularyAuthor+"\" .\n");
			
	        System.out.println("Retrieving concepts from database...");
	    	List<Concept> concepts = queryService.getConcepts();
	        System.out.println(" -> " + concepts.size() + " concepts and relationships found.\nExporting...");
	    	int c = 0;
	    	int rISA = 0;
	    	int rAssoc = 0;
	        
	    	/*//add citation as possible annotation property
	    	String citationPropertyStr = "\n"+rdfOntoPrefix+":citation rdf:type owl:AnnotationProperty ;\n";
	    	citationPropertyStr += "  "+"rdfs:label \"citation\"@en ;\n";
	    	citationPropertyStr += "  "+"rdfs:comment \"Reference to scientific literature or online resource\"@en .\n";
	    	bw.append(citationPropertyStr);*/
	    	
	        for (Concept concept : concepts){
	        	if (!concept.getIsRelationship())
	        	{
	        		StringBuilder conceptStringBuilder = new StringBuilder();	        		
	        		//concept
			        conceptStringBuilder.append("\n"+skosVocabularyPrefix+":"+concept.getCui() + " rdf:type skos:Concept ;\n");
			        conceptStringBuilder.append("  skos:inScheme " + skosVocabularyPrefix + ":" + skosVocabularyName + " ;\n");
			        conceptStringBuilder.append("  skos:prefLabel \""+concept.getTerm() + "\"@en ;\n");
					//concept terms
	        		List<Synonym> synonyms = queryService.getConceptTerms(concept.getCui());
			        for (Synonym synonym : synonyms){
			        	if (!synonym.getTerm().equals(concept.getTerm()))
			        		conceptStringBuilder.append("  skos:altLabel \""+synonym.getTerm() + "\"@" + synonym.getLanguage().toLowerCase() + " ;\n");
			        }
					//definitions
			        Set<Description> descriptions = concept.getDescriptions();
			        for (Description desc : descriptions){
			        	conceptStringBuilder.append("  skos:scopeNote \""+desc.getDescription() + "\"@" + desc.getLanguage().toLowerCase() + " ;\n");
				        conceptStringBuilder.append("  skos:definition \""+desc.getDescription() + "\"@" + desc.getLanguage().toLowerCase() + " ;\n");
			        }
			        //relationships (FROM this concept)
	        		boolean hasBroaderRel = false;
	        		List<Relationship> relationships = queryService.getConceptRelationships(concept.getCui());
			        for (Relationship rel : relationships){
			        	Concept conceptTo = rel.getConceptTo();
			        	Concept conceptRel = rel.getRelationshipDefinition();
			        	String concept2 = conceptTo.getCui();
			        	if (getNormalizedConceptTerm(conceptRel.getTerm()).equals("is_a")){
			        		conceptStringBuilder.append("  skos:broader "+skosVocabularyPrefix+":" + concept2 + " ;\n");
			        		rISA++;
			        		hasBroaderRel = true;
			        	}
			        	else {
			        		conceptStringBuilder.append("  skos:related "+skosVocabularyPrefix+":" + concept2 + " ;\n");
			        		rAssoc++;
			        	}
			        }
			        
			        //relationships (TO this concept)
	        		List<Relationship> toRelationships = queryService.getRelationshipsTargetingConcept(concept.getCui());
			        if (toRelationships!=null){
			        	for (Relationship rel : toRelationships){
				        	Concept conceptFrom = rel.getConceptFrom();
				        	Concept conceptRel = rel.getRelationshipDefinition();
				        	String concept1 = conceptFrom.getCui();
				        	if (getNormalizedConceptTerm(conceptRel.getTerm()).equals("is_a")){
				        		conceptStringBuilder.append("  skos:narrower "+skosVocabularyPrefix+":" + concept1 + " ;\n");
				        	}
				        }
			        }
			        
			        /*//citations
			        List<Citation> citations = queryService.getCitationsByCUI(concept.getCui());
			        if (citations!=null){
				        for (Citation cit : citations){
				        	conceptStringBuilder.append("  " + skosVocabularyPrefix+":citation \"" + cit.getText() + "\" ;\n");
				        }
			        }*/

	        		// mappings
			        List<Mapping> ontologyMappings = queryService.getOntologyMappingsByCUI(concept.getCui());
			        if (ontologyMappings!=null){
			        	for (Mapping ontologyMapping : ontologyMappings){
			        		conceptStringBuilder.append("  skos:exactMatch " 
			        				+ ontologyMapping.getOntologyDefinition().getId().toLowerCase() 
			        				+ ":" + ontologyMapping.getOntologyTerm() + " ;\n");
			        	}
			        }
			        
			        String conceptString = conceptStringBuilder.toString();
			        bw.append(conceptString.substring(0, conceptString.length()-2) + ".\n");
			        
			        //add to top concepts if no broader concept
			        if (!hasBroaderRel)
						bw.append("\n" + skosVocabularyPrefix+":"+skosVocabularyName+" skos:hasTopConcept "+skosVocabularyPrefix+":"+concept.getCui()+" .\n");

					c++;
	        	}
	        }
	        System.out.println(" -> " + c + " concepts, "+ rISA +" 'broader/narrower' relationships, and " + rAssoc + " associative relationships exported!");
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
	 * Get vocabulary name
	 * @return Vocabulary name
	 */
	public String getSkosVocabularyName() {
		return skosVocabularyName;
	}

	/**
	 * Set vocabulary name
	 * @param skosVocabularyName Vocabulary name
	 */
	public void setSkosVocabularyName(String skosVocabularyName) {
		this.skosVocabularyName = skosVocabularyName;
	}

	/**
	 * Get vocabulary prefix to use in SKOS file
	 * @return Vocabulary prefix to use in SKOS file
	 */
	public String getSkosVocabularyPrefix() {
		return skosVocabularyPrefix;
	}

	/**
	 * Set vocabulary prefix to use in SKOS file
	 * @param skosVocabularyPrefix Vocabulary prefix to use in SKOS file
	 */
	public void setSkosVocabularyPrefix(String skosVocabularyPrefix) {
		this.skosVocabularyPrefix = skosVocabularyPrefix;
	}

	/**
	 * Get vocabulary URI to use in SKOS file
	 * @return Vocabulary URI
	 */
	public String getSkosVocabularyUri() {
		return skosVocabularyUri;
	}

	/**
	 * Set vocabulary URI to use in SKOS file
	 * @param skosVocabularyUri Vocabulary URI
	 */ 
	public void setSkosVocabularyUri(String skosVocabularyUri) {
		this.skosVocabularyUri = skosVocabularyUri;
	}

	/** 
	 * Get vocabulary description
	 * @return Vocabulary description
	 */
	public String getSkosVocabularyDescription() {
		return skosVocabularyDescription;
	}

	/**
	 * Set vocabulary description
	 * @param skosVocabularyDescription Vocabulary description
	 */
	public void setSkosVocabularyDescription(String skosVocabularyDescription) {
		this.skosVocabularyDescription = skosVocabularyDescription;
	}

	/**
	 * Get author of vocabulary
	 * @return Author of vocabulary
	 */
	public String getSkosVocabularyAuthor() {
		return skosVocabularyAuthor;
	}

	/**
	 * Set author of vocabulary
	 * @param skosVocabularyAuthor Author of vocabulary
	 */
	public void setSkosVocabularyAuthor(String skosVocabularyAuthor) {
		this.skosVocabularyAuthor = skosVocabularyAuthor;
	}
}
