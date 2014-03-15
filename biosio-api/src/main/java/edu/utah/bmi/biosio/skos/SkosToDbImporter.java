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

import java.net.URI;
import java.util.Set;

import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataRelationAssertion;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSEntity;
import org.semanticweb.skos.SKOSLiteral;
import org.semanticweb.skos.SKOSObjectRelationAssertion;
import org.semanticweb.skos.SKOSTypedLiteral;
import org.semanticweb.skos.SKOSUntypedLiteral;
import org.semanticweb.skosapibinding.SKOSManager;


/**
 * SKOS importer. Import SKOS concepts from a single file or URL. 
 * @author Julien Thibault, University of Utah
 *
 */
public class SkosToDbImporter {

	
	public SkosToDbImporter(){
	}
	
	/**
	 * Import vocabulary concepts from SKOS file
	 * @param filePath Path to the input file
	 * @throws Exception 
	 */
	public void importFromURI(URI uri) throws Exception{
		try{
			SKOSManager manager = new SKOSManager();
			SKOSDataset dataset = manager.loadDataset(uri);
			
			for (SKOSConcept concept : dataset.getSKOSConcepts()) {
				System.out.println("Concept: " + concept.getURI());
				// print out object assertions
				Set<SKOSObjectRelationAssertion> objectRelationAssertions = dataset.getSKOSObjectRelationAssertions(concept);
                if (objectRelationAssertions.size()>0){
                	System.out.println("\tObject property assertions:");
	                for (SKOSObjectRelationAssertion objectAssertion : objectRelationAssertions) {
	                    System.out.println("\t\t" + objectAssertion.getSKOSProperty().getURI().getFragment() + " " + objectAssertion.getSKOSObject().getURI().getFragment());
	                }
	                System.out.println("");
                }

                // print out any data property assertions
                Set<SKOSDataRelationAssertion> assertions = dataset.getSKOSDataRelationAssertions(concept);
                if (assertions.size()>0){
	                System.out.println("\tData property assertions:");
	                for (SKOSDataRelationAssertion assertion : assertions) {
	
	                    // the object of a data assertion can be either a typed or untyped literal
	                    SKOSLiteral literal = assertion.getSKOSObject();
	                    String lang = "";
	                    if (literal.isTyped()) {
	
	                        SKOSTypedLiteral typedLiteral = literal.getAsSKOSTypedLiteral();
	                        System.out.println("\t\t" + assertion.getSKOSProperty().getURI().getFragment() + " " + literal.getLiteral() + " Type:" + typedLiteral.getDataType().getURI() );
	                    }
	                    else {
	
	                        // if it has  language
	                        SKOSUntypedLiteral untypedLiteral = literal.getAsSKOSUntypedLiteral();
	                        if (untypedLiteral.hasLang()) {
	                            lang = untypedLiteral.getLang();
	                        }
	                        System.out.println("\t\t" + assertion.getSKOSProperty().getURI().getFragment() + " " + literal.getLiteral() + " Lang:" + lang);
	
	                    }
	                }
	                System.out.println("");
                }

                // finally get any OWL annotations - the object of a annotation property can be a literal or an entity
                System.out.println("\tAnnotation property assertions:");
                for (SKOSAnnotation assertion : dataset.getSKOSAnnotations(concept)) {

                    // if the annotation is a literal annotation?
                    String lang = "";
                    String value = "";

                    if (assertion.isAnnotationByConstant()) {

                        SKOSLiteral literal = assertion.getAnnotationValueAsConstant();
                        value = literal.getLiteral();
                        if (!literal.isTyped()) {
                            // if it has  language
                            SKOSUntypedLiteral untypedLiteral = literal.getAsSKOSUntypedLiteral();
                            if (untypedLiteral.hasLang()) {
                                lang = untypedLiteral.getLang();
                            }
                        }
                    }
                    else {
                        // annotation is some resource
                        SKOSEntity entity = assertion.getAnnotationValue();
                        value = entity.getURI().getFragment();
                    }
                    System.out.println("\t\t" + assertion.getURI().getFragment() + ": \"" + value + "\" (lang:" + lang + ")");
                }
                System.out.println("");
            }
			
		}
		catch (Exception e){
			
		}
	}

	/**
	 * Import vocabulary concepts from SKOS file
	 * @param filePath Path to the input file
	 * @throws Exception 
	 */
	public void importFile(String filePath) throws Exception{
		importFromURI(URI.create("file:"+ filePath));
	}
}
