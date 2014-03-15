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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.utah.bmi.biosio.DBQueryUtils;
import edu.utah.bmi.biosio.model.Citation;
import edu.utah.bmi.biosio.model.Concept;
import edu.utah.bmi.biosio.model.Description;
import edu.utah.bmi.biosio.model.ExternalOntology;
import edu.utah.bmi.biosio.model.Relationship;
import edu.utah.bmi.biosio.model.Synonym;

/**
 * RDF importer. Import RDF concepts from a single file or URL. 
 * @author Julien Thibault, University of Utah
 *
 */
public class RdfToDbImporter {

	private SessionFactory sessionFactory = null;
	
	public RdfToDbImporter(ApplicationContext context){
		this.sessionFactory = (SessionFactory)context.getBean("sessionFactory");
	}
	
	public RdfToDbImporter(){
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		this.sessionFactory = (SessionFactory)context.getBean("sessionFactory");
	}
	
	/**
	 * Import vocabulary concepts from RDF file
	 * @throws Exception 
	 */
	public void importFromIRI(IRI iri) throws Exception{

		Session session = null;
		DBQueryUtils hbUtil = new DBQueryUtils(this.sessionFactory);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(iri);
		
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		
		System.out.println("  Ontology IRI: " + ontologyIRI);
		System.out.println("  Version IRI: " + ontology.getOntologyID().getVersionIRI());

        OWLDataFactory factory = manager.getOWLDataFactory();
		OWLAnnotationProperty labelAnnotation = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		OWLAnnotationProperty commentAnnotation = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		OWLAnnotationProperty citationAnnotation = factory.getOWLAnnotationProperty(IRI.create(ontologyIRI.toString()+"#citation"));
		
		Set<OWLClass> owlClasses = ontology.getClassesInSignature();
		
		session = null;
		session = sessionFactory.openSession();
        session.beginTransaction();
        
        
        //add external ontologies
        ExternalOntology externalOntology = new ExternalOntology();
        
        externalOntology.setId("CHEBI");
        externalOntology.setName("CHEBI");
        externalOntology.setPrefix("http://purl.obolibrary.org/obo/");
        externalOntology.setDocumentURL("ftp://ftp.ebi.ac.uk/pub/databases/chebi/ontology/chebi.owl");
        externalOntology.setDescription("Chemical Entities of Biological Interest (ChEBI) is a freely available dictionary of molecular entities focused on small chemical compounds.");
		session.save(externalOntology);
		
        externalOntology = new ExternalOntology();
        externalOntology.setId("IAO");
        externalOntology.setName("IAO");
        externalOntology.setPrefix("http://purl.obolibrary.org/obo/");
        externalOntology.setDocumentURL("http://purl.obolibrary.org/obo/iao/iao-main.owl");
        externalOntology.setDescription("Information Artifact Ontology");
		session.save(externalOntology);
        
        externalOntology = new ExternalOntology();
        externalOntology.setId("OBI");
        externalOntology.setName("OBI");
        externalOntology.setPrefix("http://purl.obolibrary.org/obo/");
        externalOntology.setDocumentURL("http://purl.obolibrary.org/obo/obi.owl");
        externalOntology.setDescription("Ontology for Biomedical Investigations");
		session.save(externalOntology);
        
        externalOntology = new ExternalOntology();
        externalOntology.setId("RO");
        externalOntology.setName("RO");
        externalOntology.setPrefix("http://purl.obolibrary.org/obo/");
        externalOntology.setDocumentURL("http://purl.obolibrary.org/obo/ro.owl");
        externalOntology.setDescription("OBO Relation Ontology (RO)");
		session.save(externalOntology);
        
        externalOntology = new ExternalOntology();
        externalOntology.setId("SO");
        externalOntology.setName("SO");
        externalOntology.setPrefix("http://purl.obolibrary.org/obo/");
        externalOntology.setDocumentURL("http://purl.obolibrary.org/obo/so.owl");
        externalOntology.setDescription("Sequence Ontology (SO)");
		session.save(externalOntology);
        
        externalOntology = new ExternalOntology();
        externalOntology.setId("CHEMINF");
        externalOntology.setName("CHEMINF");
        externalOntology.setPrefix("http://semanticscience.org/resource/");
        externalOntology.setDocumentURL(" http://semanticscience.org/ontology/cheminf.owl");
        externalOntology.setDescription("Chemical Information Ontology (CHEMINF)");
		session.save(externalOntology);
        
        //add concepts
		HashMap<String, Concept> conceptHashMap = new HashMap<String, Concept>();
		for (OWLClass owlClass : owlClasses){
			
			System.out.println("\n["+ owlClass.getIRI().toString()+"]");
			
			if (!owlClass.getIRI().toString().startsWith(ontologyIRI.toString())){
				System.out.println("[external ontology]");
			}
			else{
				String cui = owlClass.getIRI().toString().substring(ontologyIRI.toString().length()+1);
				System.out.println("["+ cui +"]");
				
				Concept concept = new Concept();
				concept.setCui(cui);
				concept.setIsRelationship(false);
				
				//labels
				String prefTerm = cui;
				Set<Synonym> synonyms = new HashSet<Synonym>();
				
				Set<OWLAnnotation> labels = owlClass.getAnnotations(ontology, labelAnnotation);
				boolean firstLabel = true;
				for (OWLAnnotation annotation : labels)
				{
					OWLLiteral literal = (OWLLiteral)annotation.getValue();
					Synonym syn = new Synonym();
					syn.setCui(cui);
					syn.setTerm(literal.getLiteral());
					syn.setLanguage(literal.getLang());
					syn.setIsAbbreviation(false);
					
					//first label assumed preferred label
					if (firstLabel){
						prefTerm = literal.getLiteral();
						syn.setIsPreferredTerm(true);					
						System.out.println("   Preferred label: \"" + literal.getLiteral()+ "\" @" + literal.getLang());
						firstLabel = false;
					}
					else {
						syn.setIsPreferredTerm(false);
						System.out.println("   Label: \"" + literal.getLiteral()+ "\" @" + literal.getLang());
					}
					synonyms.add(syn);
					session.save(syn);
				}
					
				//description
				Set<Description> descriptions = new HashSet<Description>();
				Set<OWLAnnotation> descAnnotations = owlClass.getAnnotations(ontology, commentAnnotation);
				for (OWLAnnotation descAnnotation : descAnnotations){
					OWLLiteral literal = (OWLLiteral)descAnnotation.getValue();
					System.out.println("   Description: \"" + literal.getLiteral()+ "\" @" + literal.getLang());
					
					Description desc = new Description();
					desc.setCui(cui);
					desc.setDescription(literal.getLiteral());
					desc.setLanguage(literal.getLang());
					descriptions.add(desc);
					session.save(desc);
				}
	
				//citations
				Set<Citation> citations = new HashSet<Citation>();
				Set<OWLAnnotation> citeAnnotations = owlClass.getAnnotations(ontology, citationAnnotation);
				for (OWLAnnotation citeAnnotation : citeAnnotations){
					OWLLiteral literal = (OWLLiteral)citeAnnotation.getValue();
					System.out.println("   Citation: \"" + literal.getLiteral());
					
					Citation citation = hbUtil.getCitation(literal.getLiteral());
					if (citation == null){
						citation = new Citation();
						citation.setText(literal.getLiteral());
						session.save(citation);
					}
					citations.add(citation);
				}
	
				concept.setTerm(prefTerm);
				concept.setSynonyms(synonyms);
				concept.setDescriptions(descriptions);
				concept.setCitations(citations);
				
				session.save(concept);
	
				conceptHashMap.put(cui, concept);
			}
		}
		
		//add "is a" relationship
		Concept isaRel = new Concept();
		isaRel.setCui("IS_A");
		isaRel.setIsRelationship(true);
		isaRel.setTerm("is a");
		session.save(isaRel);
        
		//subclasses
		for (OWLClass owlClass : owlClasses){
	        
			String subclassID = owlClass.getIRI().toString().substring(ontologyIRI.toString().length()+1);
			Set<OWLClassExpression> superClassesExpr = owlClass.getSuperClasses(ontology);
			for (OWLClassExpression superClassExpr : superClassesExpr){
				
				Set<OWLClass> superClasses = superClassExpr.getClassesInSignature();
				for (OWLClass superClass : superClasses){
					String superclassID = superClass.getIRI().toString().substring(ontologyIRI.toString().length()+1);
					System.out.println("[" + subclassID + "] is a [" + superclassID +"]");
					
					Relationship subclassRel = new Relationship();
					Concept conceptFrom = conceptHashMap.get(subclassID);
					Concept conceptTo = conceptHashMap.get(superclassID);
					subclassRel.setRelationshipDefinition(isaRel);
					subclassRel.setConceptFrom(conceptFrom);
					subclassRel.setConceptTo(conceptTo);
					
					session.save(subclassRel);
				}
			}
		}
		//commit!
		session.getTransaction().commit();
        session.close();
	}

	/**
	 * Import vocabulary concepts from RDF file
	 * @param filePath Path to the input file
	 * @throws Exception 
	 */
	public void importFile(String filePath) throws Exception{
		importFromIRI(IRI.create("file:"+ filePath));
	}
}
